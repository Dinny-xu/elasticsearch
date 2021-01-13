package com.study.elasticsearch.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.study.elasticsearch.bean.req.EsSearchReq;
import com.study.elasticsearch.bean.res.EsQueryRes;
import com.study.elasticsearch.bean.res.EsSearchDataRes;
import com.study.elasticsearch.bean.res.EsSearchRes;
import com.study.elasticsearch.constants.CommonConstants;
import com.study.elasticsearch.constants.EsConstant;
import com.study.elasticsearch.exception.BasicException;
import com.study.elasticsearch.mapper.EsSyncMapper;
import com.study.elasticsearch.mapper.SkuMapper;
import com.study.elasticsearch.mapper.SpuMapper;
import com.study.elasticsearch.service.EsSearchService;
import com.study.elasticsearch.utils.ESUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @program: poney
 * @description: 搜索实现类
 * @author: Xu·yan
 * @create: 2020-12-04 10:56
 **/
@Service
@Slf4j
public class EsSearchServiceImpl implements EsSearchService {

    @Resource
    private SkuMapper skuMapper;

    @Resource
    private SpuMapper spuMapper;

    @Autowired
    RestHighLevelClient restHighLevelClient;

    private Map<String, EsSyncMapper> mapperMap = Maps.newHashMap();


    @PostConstruct
    public void init() {
        mapperMap.put(EsConstant.KNOWLEDGE_LAW_TYPE, skuMapper);
        mapperMap.put(EsConstant.KNOWLEDGE_CHEMICALS_TYPE, spuMapper);
    }

    @Value("${es.data.timeinterval}")
    private Integer timeInterval;

    /**
     * 搜索
     *
     * @param req
     * @return
     */
    @Override
    public PageInfo<EsSearchRes> search(EsSearchReq req) {

        SearchRequest searchRequest = getSearchRequest(req);
        SearchResponse searchResponse;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("未查询到匹配结果", e);
            return null;
        }
        PageInfo<EsSearchRes> info = new PageInfo<>();
        info.setPageNum(req.getPageNum());
        info.setPageSize(req.getPageSize());
        SearchHits hits1 = searchResponse.getHits();
        long totalHits = hits1.getTotalHits().value;
        info.setTotal(Convert.toInt(totalHits));
        info.setList(buildEsSearchResList(hits1, totalHits));
        return info;
    }

    private List<EsSearchRes> buildEsSearchResList(SearchHits hits1, long totalHits) {
        if (totalHits > 0) {
            SearchHit[] hits = hits1.getHits();
            List<EsSearchRes> res = Arrays.stream(hits)
                    .map(h -> {
                        EsSearchRes esSearchRes = EsSearchRes.create();
                        Map<String, Object> sourceAsMap = h.getSourceAsMap();
                        if (Objects.nonNull(sourceAsMap)) {
                            Map<String, HighlightField> highlightFields = h.getHighlightFields();
                            if (Objects.nonNull(highlightFields)) {
                                HighlightField title = highlightFields.get("title");
                                if (Objects.nonNull(title)) {
                                    String titleStr = Arrays.stream(title.getFragments())
                                            .map(Text::toString)
                                            .collect(Collectors.joining());
                                    esSearchRes.setTitle(titleStr);
                                }
                                HighlightField content = highlightFields.get("content");
                                if (Objects.nonNull(content)) {
                                    String contentStr = Arrays.stream(content.getFragments())
                                            .map(Text::toString)
                                            .collect(Collectors.joining());
                                    esSearchRes.setContent(contentStr);
                                }
                            }
                            if (StrUtil.isBlank(esSearchRes.getTitle())) {
                                esSearchRes.setTitle(Convert.toStr(sourceAsMap.get("title"), ""));
                            }
                            if (StrUtil.isBlank(esSearchRes.getContent())) {
                                String content = Convert.toStr(sourceAsMap.get("content"), "");
                                if (StrUtil.length(content) > 150) {
                                    content = StrUtil.sub(content, 0, 140);
                                }
                                esSearchRes.setContent(content);
                            }
                            esSearchRes.setCategory(Convert.toStr(sourceAsMap.get("category")));
                            esSearchRes.setUrl(Convert.toStr(sourceAsMap.get("url")));
                            Long createDate = Convert.toLong(sourceAsMap.get("createTime"), DateTime.now().getTime());
                            esSearchRes.setCreateTime(DateUtil.format(DateUtil.date(createDate), DatePattern.NORM_DATETIME_PATTERN));
                        }
                        return esSearchRes;
                    })
                    .filter(r -> StrUtil.isNotBlank(r.getTitle()))
                    .collect(Collectors.toList());
            return res;
        } else {
            return Lists.newArrayList();
        }
    }

    /**
     * 同步数据
     */
    @Override
    public void syncData() {

        DateTime now = DateTime.now();
        DateTime time = DateUtil.offsetDay(now, timeInterval);
        //SKU
        List<EsSearchDataRes> syncSkuData = getSyncData(EsConstant.KNOWLEDGE_LAW_TYPE, time);
        //SPU
        List<EsSearchDataRes> syncSpuData = getSyncData(EsConstant.KNOWLEDGE_CHEMICALS_TYPE, time);

        List<EsSearchDataRes> collect =
                Stream.of(syncSkuData, syncSpuData)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
        try {
            if (CollUtil.isNotEmpty(collect)) {
                ESUtil.bulkData(restHighLevelClient, EsConstant.INDEX, EsConstant.TYPE, collect);
            }
        } catch (IOException e) {
            log.error("数据同步失败", e);
            throw new BasicException("数据同步失败");
        }
    }

    @Override
    public void deleteData(String id, String category) {
        if (Objects.isNull(id)) {
            throw new BasicException("请选择需要删除的记录");
        }
        String deleteId = ESUtil.getId(category, id);
        try {
            ESUtil.deleteData(restHighLevelClient, EsConstant.INDEX, EsConstant.TYPE, deleteId);
        } catch (IOException e) {
            throw new BasicException("数据删除失败");
        }
    }

    private List<EsSearchDataRes> getSyncData(String category, DateTime time) {
        //对数据进行查询同步
        List<EsQueryRes> syncList = getSyncList(category, time);
        if (CollUtil.isEmpty(syncList)) {
            return Lists.newArrayList();
        }
        return syncList.stream()
                .map(a -> {
                    String content = a.getContent();
                    if (StrUtil.isNotBlank(content)) {
                        content = StrUtil.subWithLength(HtmlUtil.cleanHtmlTag(content).replaceAll("&nbsp;", "").replaceAll("\\s+", ""), 0, 255);
                    }
                    return EsSearchDataRes.create()
                            .setContent(content)
                            .setId(ESUtil.getId(category, a.getId()))
                            .setCategory(category)
                            .setViewCount(a.getViewCount())
                            .setTitle(a.getTitle())
                            .setCreateTime(a.getCreateTime().getTime())
                            .setUrl(ESUtil.getUri(category, a.getId()));
                })
                .collect(Collectors.toList());
    }

    /**
     * 同步查询
     *
     * @param category
     * @param time
     * @return
     */
    private List<EsQueryRes> getSyncList(String category, DateTime time) {
        EsSyncMapper esSyncMapper = mapperMap.get(category);
        if (Objects.isNull(esSyncMapper)) {
            throw new BasicException("请指定需要同步数据的类型");
        }
        return esSyncMapper.syncDataList(time);
    }


    private SearchRequest getSearchRequest(EsSearchReq request) {

        //高亮查询
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(EsConstant.INDEX);
        searchRequest.types(EsConstant.TYPE);

        // 查询构建器
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title").field("content");

        int from = request.getPageSize() * (request.getPageNum() - 1);
        QueryBuilder queryBuilder = queryBuilder(request);
        ScoreSortBuilder order = SortBuilders.scoreSort().order(SortOrder.DESC);
        FieldSortBuilder sortBuilder;
        if (StrUtil.equals(EsConstant.KNOWLEDGE_SEARCH_TYPE_LATEST, request.getType())) {
            sortBuilder = SortBuilders.fieldSort("createTime").order(SortOrder.DESC);
        } else {
            sortBuilder = SortBuilders.fieldSort("viewCount").order(SortOrder.DESC);
        }
        searchSourceBuilder
                .query(queryBuilder)
                .highlighter(highlightBuilder)
                .sort(order)
                .sort(sortBuilder)
                .from(from).size(request.getPageSize());
        searchRequest.source(searchSourceBuilder);
        return searchRequest;

    }


    private QueryBuilder queryBuilder(EsSearchReq request) {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (StrUtil.isNotBlank(request.getCategory())) {
            String[] split = request.getCategory().split(",");
            TermsQueryBuilder category = QueryBuilders.termsQuery("category", split);
            boolQueryBuilder.must(category);
        }
        if (StrUtil.isNotBlank(request.getKeyword())) {
            MultiMatchQueryBuilder range =
                    StrUtil.isNotBlank(request.getRange()) ? QueryBuilders.multiMatchQuery(request.getKeyword(), request.getRange()) :
                            QueryBuilders.multiMatchQuery(request.getKeyword(), "title", "content");
            boolQueryBuilder.must(range);
        }

        if (StrUtil.isNotBlank(request.getTime()) && !StrUtil.equals("0", request.getTime())) {
            String time = request.getTime();
            Date fromTime;
            Date now = DateTime.now();

            switch (time) {
                case CommonConstants.TODAY:
                    fromTime = DateUtil.beginOfDay(now);
                    break;
                case CommonConstants.ONE_WEEK:
                    fromTime = DateUtil.offsetWeek(now, -1);
                    break;
                case CommonConstants.ONE_MONTH:
                    fromTime = DateUtil.offsetMonth(now, -1);
                    break;
                case CommonConstants.ONE_YEAR:
                    fromTime = DateUtil.offset(now, DateField.YEAR, -1);
                    break;
                default:
                    fromTime = DateUtil.offset(now, DateField.YEAR, -10);
                    break;
            }

            long fromTimeStr = fromTime.getTime();
            long nowStr = now.getTime();
            RangeQueryBuilder dateQuery = QueryBuilders
                    .rangeQuery("createTime")
                    .gte(fromTimeStr)
                    .lte(nowStr);
            boolQueryBuilder.filter(dateQuery);
        }
        return boolQueryBuilder;
    }
}

package com.study.elasticsearch.utils;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.study.elasticsearch.bean.res.EsSearchDataRes;
import com.study.elasticsearch.constants.EsConstant;
import com.study.elasticsearch.exception.BasicException;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @description es操作工具类
 * @date 2019/11/26 8:55
 **/
public class ESUtil {


    private static Map<String, String> idMap = Maps.newHashMap();
    private static Map<String, String> uriTemplateMap = Maps.newHashMap();

    static {
        idMap.put(EsConstant.KNOWLEDGE_LAW_TYPE, EsConstant.KNOWLEDGE_LAW_ID_PREFFIX);
        uriTemplateMap.put(EsConstant.KNOWLEDGE_LAW_TYPE, EsConstant.KNOWLEDGE_LAW_URI);

        idMap.put(EsConstant.KNOWLEDGE_CHEMICALS_TYPE, EsConstant.KNOWLEDGE_CHEMICALS_ID_PREFFIX);
        uriTemplateMap.put(EsConstant.KNOWLEDGE_CHEMICALS_TYPE, EsConstant.KNOWLEDGE_CHEMICALS_URI);

    }


    private ESUtil() {
    }

    public static String getUri(String category, String id) {
        String uriTemplate = uriTemplateMap.get(category);
        if (StrUtil.isBlank(uriTemplate)) {
            throw new BasicException("没有指定模板");
        }
        return StrUtil.format(uriTemplate, id);
    }

    public static String getId(String category, String id) {
        String idTemplate = idMap.get(category);
        if (StrUtil.isBlank(idTemplate)) {
            try {
                throw new BasicException("没有指定模板");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return StrUtil.format(idTemplate, id);
    }

    public static void bulkData(RestHighLevelClient client, String index, String type, List<EsSearchDataRes> data) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        data.forEach(d -> {
            IndexRequest source = new IndexRequest(index, type, d.getId())
                    .source(XContentType.JSON,
                            "category", d.getCategory(),
                            "title", d.getTitle(),
                            "content", d.getContent(),
                            "url", d.getUrl(),
                            "viewCount",d.getViewCount(),
                            "createTime", d.getCreateTime()
                    );
            UpdateRequest updateRequest = new UpdateRequest(index, type, d.getId())
                    .doc(XContentType.JSON,
                            "category", d.getCategory(),
                            "title", d.getTitle(),
                            "content", d.getContent(),
                            "url", d.getUrl(),
                            "viewCount",d.getViewCount(),
                            "createTime", d.getCreateTime()
                    ).upsert(source);
            bulkRequest.add(updateRequest);
        });
        //添加之后立即刷新
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        BulkResponse bulkRes = client.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    public static void deleteData(RestHighLevelClient client, String index, String type, String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(index, type, id);
        client.delete(deleteRequest, RequestOptions.DEFAULT);
    }

    public static void updateData(RestHighLevelClient client, String index, String type, EsSearchDataRes data) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(index, type, data.getId())
                .doc(XContentType.JSON,
                        "type", data.getCategory(),
                        "title", data.getTitle(),
                        "content", data.getContent(),
//                        "uri", data.getUri(),
                        "createDate", data.getCreateTime());
        UpdateResponse update = client.update(updateRequest, RequestOptions.DEFAULT);
    }


}

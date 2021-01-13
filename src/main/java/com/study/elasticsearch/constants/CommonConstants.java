package com.study.elasticsearch.constants;

/**
 * @date 2019/9/18 12 54
 * @description: 系统常量配置
 */
public interface CommonConstants {

    /**
     * 新闻浏览量存放
     */
    String CACHE_PREFIX_NEWS_PAGE_VIEWS = "newsViews:";

    /**
     * 关键字搜索缓存
     */
    String CACHE_PREFIX_SEARCH_KEYWORD = "searchKeyword:";

    /**
     *  搜索范围
     * 1：今天
     * 2：一周内
     * 3：一月内
     * 4：一年内
     */
    String TODAY = "1";
    String ONE_WEEK = "2";
    String ONE_MONTH = "3";
    String ONE_YEAR = "4";
}

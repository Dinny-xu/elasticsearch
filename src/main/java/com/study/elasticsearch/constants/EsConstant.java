package com.study.elasticsearch.constants;

public interface EsConstant {

    String INDEX = "study";
    String TYPE = "_doc";

    /**
     * 法律法规
     */
    String KNOWLEDGE_LAW_TYPE = "0";
    String KNOWLEDGE_LAW_ID_PREFFIX = "knowledge_law_{}";
    String KNOWLEDGE_LAW_URI = "/dashboard/law/detail?id={}";

    /**
     * 危化品
     */
    String KNOWLEDGE_CHEMICALS_TYPE = "1";
    String KNOWLEDGE_CHEMICALS_ID_PREFFIX = "knowledge_chemicals_{}";
    String KNOWLEDGE_CHEMICALS_URI = "/dashboard/chemistry/detail?id={}";

    /**
     * 知识库搜索热门分类
     * 最近更新
     * 热门推荐
     * 我的关注
     */
    String KNOWLEDGE_SEARCH_TYPE_LATEST = "latest";
    String KNOWLEDGE_SEARCH_TYPE_HOT = "hot";
    String KNOWLEDGE_SEARCH_TYPE_FAVORITE = "favorite";

}

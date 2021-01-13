package com.study.elasticsearch.service;


import com.github.pagehelper.PageInfo;
import com.study.elasticsearch.bean.req.EsSearchReq;
import com.study.elasticsearch.bean.res.EsSearchRes;

public interface EsSearchService {


    PageInfo<EsSearchRes> search(EsSearchReq req);

    void syncData();

    void deleteData(String id, String category);
}

package com.study.elasticsearch.controller;


import com.study.elasticsearch.bean.req.EsSearchReq;
import com.study.elasticsearch.entity.Result;
import com.study.elasticsearch.service.EsSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: ES 搜索
 * @author: Xu·yan
 * @create: 2020-12-04 10:10
 **/
@Api(tags = "搜索")
@RestController
@RequestMapping("/home")
public class EsSearchController {

    @Autowired
    private EsSearchService searchService;


    @ApiOperation(value = "搜索")
    @GetMapping("/search")
    public Result search(EsSearchReq req) {
        return Result.ok(searchService.search(req));
    }

    @ApiOperation(value = "数据同步")
    @Transactional(rollbackFor = Exception.class)
    @GetMapping("/dataSync")
    public Result search() {
        searchService.syncData();
        return Result.ok();
    }

    @ApiOperation(value = "数据同步删除")
    @GetMapping("/dataSync/delete")
    public Result deleteData(String id, String category) {
        searchService.deleteData(id, category);
        return Result.ok();
    }

}

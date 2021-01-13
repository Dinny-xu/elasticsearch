package com.study.elasticsearch.task;

import com.study.elasticsearch.service.EsSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @description es数据定时同步
 * @date 2019/11/27 15:21
 **/
@Component
public class EsSyncDataTask {

    @Autowired
    private EsSearchService elasticsearchService;

    /**
     * es数据同步时间间隔  10分钟同步一次
     */
    @Scheduled(cron = "0 0/10 * * * ? ")
    public void syncDataTask() {
        elasticsearchService.syncData();
    }

}

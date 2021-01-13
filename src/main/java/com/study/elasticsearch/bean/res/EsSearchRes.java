package com.study.elasticsearch.bean.res;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @description 搜索返回实体
 * @date 2019/12/20 9:41
 **/
@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor(staticName = "create")
public class EsSearchRes {

    private String category;

    private String title;

    private String content;

    private String url;

    private String createTime;
}

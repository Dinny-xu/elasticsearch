package com.study.elasticsearch.bean.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


/**
 * @program: poney
 * @description: 数据内容返回
 * @author: Xu·yan
 * @create: 2020-12-04 13:33
 **/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@RequiredArgsConstructor(staticName = "create")
public class EsSearchDataRes {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "title")
    private String title;

    @ApiModelProperty(value = "content")
    private String content;

    @ApiModelProperty(value = "category")
    private String category;

    @ApiModelProperty(value = "createTime")
    private Long createTime;

    private String url;

    private Integer viewCount;


}

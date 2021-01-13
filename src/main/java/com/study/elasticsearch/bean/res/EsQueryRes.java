package com.study.elasticsearch.bean.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: poney
 * @description: 查询返回
 * @author: Xu·yan
 * @create: 2020-12-04 16:29
 **/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@RequiredArgsConstructor(staticName = "create")
public class EsQueryRes implements Serializable {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "title")
    private String title;

    @ApiModelProperty(value = "content")
    private String content;

    @ApiModelProperty(value = "type")
    private Integer type;

    @ApiModelProperty(value = "createTime")
    private Date createTime;

    private String url;

    @ApiModelProperty(value = "viewCount")
    private Integer viewCount;

}

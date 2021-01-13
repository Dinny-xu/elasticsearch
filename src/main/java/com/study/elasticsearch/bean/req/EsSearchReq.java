package com.study.elasticsearch.bean.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: 首页搜索查询请求
 * @author: Xu·yan
 * @create: 2020-12-04 10:50
 **/
@Getter
@Setter
public class EsSearchReq {

    @ApiModelProperty(value = "关键字")
    private String keyword;

    @ApiModelProperty(value = "1：sku 2: spu")
    private String category;

    @ApiModelProperty(value = "时间范围")
    private String time;

    private Integer pageSize;

    private Integer pageNum;

    private String type;

    @ApiModelProperty(value = "0: 标题 1：正文 2：附件")
    private String range;

}

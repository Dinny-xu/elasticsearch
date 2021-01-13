package com.study.elasticsearch.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TbSpu {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;

    /**
     * 货号
     */
    @ApiModelProperty(value = "货号")
    private String sn;

    /**
     * SPU名
     */
    @ApiModelProperty(value = "SPU名")
    private String name;

    /**
     * 副标题
     */
    @ApiModelProperty(value = "副标题")
    private String caption;

    /**
     * 品牌ID
     */
    @ApiModelProperty(value = "品牌ID")
    private Integer brandId;

    /**
     * 一级分类
     */
    @ApiModelProperty(value = "一级分类")
    private Integer category1Id;

    /**
     * 二级分类
     */
    @ApiModelProperty(value = "二级分类")
    private Integer category2Id;

    /**
     * 三级分类
     */
    @ApiModelProperty(value = "三级分类")
    private Integer category3Id;

    /**
     * 模板ID
     */
    @ApiModelProperty(value = "模板ID")
    private Integer templateId;

    /**
     * 运费模板id
     */
    @ApiModelProperty(value = "运费模板id")
    private Integer freightId;

    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String image;

    /**
     * 图片列表
     */
    @ApiModelProperty(value = "图片列表")
    private String images;

    /**
     * 售后服务
     */
    @ApiModelProperty(value = "售后服务")
    private String saleService;

    /**
     * 介绍
     */
    @ApiModelProperty(value = "介绍")
    private String introduction;

    /**
     * 规格列表
     */
    @ApiModelProperty(value = "规格列表")
    private String specItems;

    /**
     * 参数列表
     */
    @ApiModelProperty(value = "参数列表")
    private String paraItems;

    /**
     * 销量
     */
    @ApiModelProperty(value = "销量")
    private Integer saleNum;

    /**
     * 评论数
     */
    @ApiModelProperty(value = "评论数")
    private Integer commentNum;

    /**
     * 是否上架
     */
    @ApiModelProperty(value = "是否上架")
    private String isMarketable;

    /**
     * 是否启用规格
     */
    @ApiModelProperty(value = "是否启用规格")
    private String isEnableSpec;

    /**
     * 是否删除
     */
    @ApiModelProperty(value = "是否删除")
    private String isDelete;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态")
    private String status;

}

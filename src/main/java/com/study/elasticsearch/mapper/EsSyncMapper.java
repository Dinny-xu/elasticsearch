package com.study.elasticsearch.mapper;


import com.study.elasticsearch.bean.res.EsQueryRes;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface EsSyncMapper {

    List<EsQueryRes> syncDataList(@Param("time") Date time);


}

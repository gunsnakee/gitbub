package com.meiliwan.emall.accountlog.dao;

import com.meiliwan.emall.accountlog.bean.BizLog;

public interface BizLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BizLog record);

    int insertSelective(BizLog record);

    BizLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BizLog record);

    int updateByPrimaryKeyWithBLOBs(BizLog record);

    int updateByPrimaryKey(BizLog record);
}
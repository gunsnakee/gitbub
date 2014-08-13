package com.meiliwan.emall.antispam.dao;

import com.meiliwan.emall.antispam.bean.AntispamLog;
import com.meiliwan.emall.core.db.IDao;

public interface AntispamLogDao extends IDao<Integer, AntispamLog>{
    int deleteByPrimaryKey(Integer id);

    int insert(AntispamLog record);

    int insertSelective(AntispamLog record);

    AntispamLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AntispamLog record);

    int updateByPrimaryKey(AntispamLog record);
}
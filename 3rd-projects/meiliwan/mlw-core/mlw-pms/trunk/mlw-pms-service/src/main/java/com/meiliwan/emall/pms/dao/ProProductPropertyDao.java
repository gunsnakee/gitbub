package com.meiliwan.emall.pms.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.pms.bean.ProProductProperty;
import com.meiliwan.emall.pms.bean.ProProductPropertyKey;

import java.util.List;

public interface ProProductPropertyDao extends IDao<ProProductPropertyKey,ProProductProperty>{

    int insertByBatch(int spuId,List<ProProductProperty> properties);
    int updateBySpuAndPropId(int spuId,int propId,String valueId,String value);
}
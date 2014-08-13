package com.meiliwan.emall.pms.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.pms.bean.ProSelfProperty;

import java.util.List;

public interface ProSelfPropertyDao extends IDao<Integer,ProSelfProperty>{

    int insertByBatch(int spuId,List<ProSelfProperty> propertys);
}
package com.meiliwan.emall.pms.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.pms.bean.SkuSelfProperty;

import java.util.List;

public interface SkuSelfPropertyDao extends IDao<Integer, SkuSelfProperty> {

    int insertByBatch(int proId, List<SkuSelfProperty> properties);
}
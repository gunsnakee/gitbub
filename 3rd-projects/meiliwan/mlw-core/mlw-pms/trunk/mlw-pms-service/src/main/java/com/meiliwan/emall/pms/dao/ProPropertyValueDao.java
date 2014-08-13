package com.meiliwan.emall.pms.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.pms.bean.ProPropertyValue;

import java.util.List;

public interface ProPropertyValueDao extends IDao<Integer,ProPropertyValue>{

    List<ProPropertyValue> getValuesByIds(String[] ids);
}
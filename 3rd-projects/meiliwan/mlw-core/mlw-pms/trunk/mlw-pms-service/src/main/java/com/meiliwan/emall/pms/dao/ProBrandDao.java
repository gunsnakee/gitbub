package com.meiliwan.emall.pms.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.pms.bean.ProBrand;

import java.util.List;

public interface ProBrandDao extends IDao<Integer,ProBrand>{

    List<ProBrand> getListByName(String name);

}
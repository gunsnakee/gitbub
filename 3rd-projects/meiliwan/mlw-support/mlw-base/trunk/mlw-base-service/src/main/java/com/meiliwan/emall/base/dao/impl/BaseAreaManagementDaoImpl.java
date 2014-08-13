package com.meiliwan.emall.base.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.base.bean.BaseAreaManagement;
import com.meiliwan.emall.base.dao.BaseAreaManagementDao;
import com.meiliwan.emall.core.db.BaseDao;
/**
 * 地域管理接口实现类
 * @author yiyou.luo
 * date 2013-06-03
 */

@Repository
public class BaseAreaManagementDaoImpl extends BaseDao<Integer, BaseAreaManagement> implements BaseAreaManagementDao {

	@Override
	public String getMapperNameSpace() {
		// TODO Auto-generated method stub
		return BaseAreaManagementDao.class.getName();
	}

	@Override
	public List<BaseAreaManagement> getAreasByParentCode(String parentCode) {
		// TODO Auto-generated method stub
		BaseAreaManagement area = new BaseAreaManagement();
		area.setParentCode(parentCode);
		area.setIsDel(0);
		return getListByObj(area, " ", "order by area_code");
	}

}

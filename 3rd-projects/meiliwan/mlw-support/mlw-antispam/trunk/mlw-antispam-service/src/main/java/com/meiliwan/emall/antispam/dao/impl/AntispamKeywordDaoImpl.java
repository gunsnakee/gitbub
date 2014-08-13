package com.meiliwan.emall.antispam.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.antispam.bean.AntispamKeyword;
import com.meiliwan.emall.antispam.dao.AntispamKeywordDao;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.core.db.BaseDao;

@Repository
public class AntispamKeywordDaoImpl extends BaseDao<Integer, AntispamKeyword> implements AntispamKeywordDao {
	
	@Override
	public String getMapperNameSpace() {
		return AntispamKeywordDao.class.getName();
	}

	@Override
	public List<AntispamKeyword> getPageKeywords(PageInfo pageInfo, AntispamKeyword entity) {
		return this.getPagerByObj(entity, pageInfo, null, null).getEntityList();
	}

}

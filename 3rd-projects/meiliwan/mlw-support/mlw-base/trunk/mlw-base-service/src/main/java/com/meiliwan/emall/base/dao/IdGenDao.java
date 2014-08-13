package com.meiliwan.emall.base.dao;

import com.meiliwan.emall.core.bean.BaseEntity;
import com.meiliwan.emall.core.db.IDao;

/**
 * 
 * @author lsf
 * 
 */
public interface IdGenDao extends IDao<String, BaseEntity>{

	public long nextPaySeq();

	public long nextCmbSeq();

}

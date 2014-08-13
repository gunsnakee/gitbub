package com.meiliwan.emall.oms.dao;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.oms.bean.OrdException;
import com.meiliwan.emall.oms.dto.OrdExceptionDTO;


public interface OrdExceptionDao extends IDao<Integer, OrdException>{

	PagerControl<OrdException> getPagerByDTO(OrdExceptionDTO dto,
			PageInfo pageInfo, String where, String orderBy);
 
	
}
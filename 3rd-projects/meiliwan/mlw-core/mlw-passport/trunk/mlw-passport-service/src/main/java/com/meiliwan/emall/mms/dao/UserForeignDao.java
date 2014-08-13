package com.meiliwan.emall.mms.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.mms.bean.UserForeign;

public interface UserForeignDao extends IDao<Integer, UserForeign>{
	
	boolean update2NewUid(int newUid, int oldUid, String source);
	
}
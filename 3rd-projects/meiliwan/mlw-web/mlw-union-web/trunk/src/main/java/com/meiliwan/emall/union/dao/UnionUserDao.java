package com.meiliwan.emall.union.dao;

import java.util.List;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.mms.bean.UserPassport;

public interface UnionUserDao extends IDao<Integer,UserPassport>{
	List<UserPassport> getUserList(String emailList);
}

package com.meiliwan.emall.mms.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.mms.bean.UserIntegral;
/**
 * 用户积分 dao接口
 * @author Administrator
 *2013-06-03
 */
public interface UserIntegralDao extends IDao<Integer, UserIntegral>{
    /**
     * 根据用户积分id更改用户积分值
     * @param id
     * @param balance
     * @return
     */
    int updateIntegralBalance(Integer id,Integer balance);
    
}
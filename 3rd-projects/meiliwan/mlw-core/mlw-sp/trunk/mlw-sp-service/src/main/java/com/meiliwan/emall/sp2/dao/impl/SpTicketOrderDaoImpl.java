package com.meiliwan.emall.sp2.dao.impl;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.sp2.bean.SpTicket;
import com.meiliwan.emall.sp2.bean.SpTicketOrder;
import com.meiliwan.emall.sp2.dao.SpTicketOrderDao;

import org.springframework.stereotype.Repository;

/**
 * User: wuzixin
 * Date: 14-5-28
 * Time: 下午4:33
 */
@Repository
public class SpTicketOrderDaoImpl extends BaseDao<String, SpTicketOrder> implements SpTicketOrderDao {
	
    @Override
    public String getMapperNameSpace() {
        return SpTicketOrderDao.class.getName();
    }

}

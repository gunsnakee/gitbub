package com.meiliwan.emall.monitor.dao;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.monitor.bean.Player;

public interface OrderDao  {

	int getCount(String sql) throws ServiceException;

	int getCount(String sql, Object[] params) throws ServiceException;
}
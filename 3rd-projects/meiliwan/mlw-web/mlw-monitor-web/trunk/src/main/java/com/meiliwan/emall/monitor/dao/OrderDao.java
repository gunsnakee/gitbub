package com.meiliwan.emall.monitor.dao;

import com.meiliwan.emall.commons.exception.ServiceException;



public interface OrderDao  {

	int getCount(String sql) throws ServiceException;

	int getCount(String sql, Object[] params) throws ServiceException;
}
package com.meiliwan.emall.monitor.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meiliwan.emall.monitor.dao.OrderDao;

@Service
public class OrderService   {

	@Autowired
	private OrderDao orderDao;
	
	/**
	 * 下单数－货到付款,时间需要传过来才行，网络有延迟
	 * @param minuteAgo
	 * @return
	 */
	public int getCreateCODCount(Date minuteAgo){
		
		String sql="SELECT count(*) FROM oms_ord where order_type='RCD' and create_time >= ?";
		Object[] objs = new Object[]{minuteAgo};
		//logger.debug(sql, minuteAgo, null);
		return orderDao.getCount(sql,objs);
	}
	
	/**
	 * 
	 * @param minuteAgo
	 * @return
	 */
	public int getCreatePayCount(Date minuteAgo){
		
		String sql="SELECT count(*) FROM oms_ord where order_type='ROD' and create_time >= ?";
		//logger.debug(sql, minuteAgo, null);
		Object[] objs = new Object[]{minuteAgo};
		return orderDao.getCount(sql,objs);
	}
	
	public int getCancelCount(Date minuteAgo){
		
		String sql="SELECT count(*) FROM oms_ord where order_status='80' and create_time >= ?";
		//logger.debug(sql, minuteAgo, null);
		Object[] objs = new Object[]{minuteAgo};
		return orderDao.getCount(sql,objs);
	}
	
	public int getCODCancelCount(Date minuteAgo){
		
		String sql="SELECT count(*) FROM oms_ord where order_type='RCD' and order_status='80' and create_time >= ?";
		//logger.debug(sql, minuteAgo, null);
		Object[] objs = new Object[]{minuteAgo};
		return orderDao.getCount(sql,objs);
	}

	public int getPayFinishCount(Date minuteAgo){
		
		String sql="SELECT count(*) FROM oms_ord where order_type='ROD' and order_status='30'  and create_time >= ?";
		//logger.debug(sql, minuteAgo, null);
		Object[] objs = new Object[]{minuteAgo};
		return orderDao.getCount(sql,objs);
	}
			
}

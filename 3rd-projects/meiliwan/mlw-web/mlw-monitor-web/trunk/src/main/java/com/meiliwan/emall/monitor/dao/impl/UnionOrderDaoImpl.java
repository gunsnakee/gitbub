package com.meiliwan.emall.monitor.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.monitor.bean.UnionOrder;
import com.meiliwan.emall.monitor.dao.UnionOrderDao;
import com.meiliwan.emall.statistics.jdbc.ConnTool;

public class UnionOrderDaoImpl extends
      BaseDao<Integer, UnionOrder> implements UnionOrderDao {

	@Override
	public List<UnionOrder> get2wCodeListBySourceId(String prefixSourceId, Date startTime, Date endTime, Integer orderStatus) {
//		Map<String, Object> condMap = new HashMap<String, Object>();
//		condMap.put("prefixSourceId", prefixSourceId + "%");
//		condMap.put("sourceType", "2wCode");
//		condMap.put("startTime", startTime);
//		condMap.put("endTime", endTime);
//		condMap.put("orderStatus", orderStatus);
//		List<UnionOrder> list = getSqlSession().selectList(getMapperNameSpace() + ".get2wCodeListBySourceId",
//				condMap);
		
		String sql = "select  id,source_id,source_type,order_id,uid,total_price,create_time,mlw_query_info,pay_status,pay_method,order_status from union_order where source_type = '2wCode' ";
		if(StringUtils.isNotBlank(prefixSourceId)){
			sql += " and source_id like '" + prefixSourceId + "%'";
		}
		if(startTime != null){
			sql += " and create_time >= '" + DateUtil.getDateTimeStr(startTime) + "'";
		}
		if(endTime != null){
			sql += " and create_time <= '" + DateUtil.getDateTimeStr(endTime) + "'";
		}
		if(orderStatus != null){
			sql += " and order_status = " + orderStatus;
		}
		
		List<UnionOrder> list = new ArrayList<UnionOrder>();
		Connection conn = ConnTool.getUnionConnection();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				UnionOrder uo = new UnionOrder();
				uo.setId(rs.getInt("id"));
				uo.setSourceId(rs.getString("source_id"));
				uo.setSourceType(rs.getString("source_type"));
				uo.setOrderId(rs.getString("order_id"));
				uo.setUid(rs.getInt("uid"));
				uo.setTotalPrice(rs.getDouble("total_price"));
				uo.setCreateTime(rs.getTimestamp("create_time"));
				uo.setMlwQueryInfo(rs.getString("mlw_query_info"));
				uo.setPayStatus(rs.getInt("pay_status"));
				uo.setPayMethod(rs.getString("pay_method"));
				uo.setOrderStatus(rs.getInt("order_status"));
				
				list.add(uo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	

	@Override
	public String getMapperNameSpace() {
		return UnionOrderDaoImpl.class.getName();
	}
	
	
}

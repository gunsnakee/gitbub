package com.meiliwan.emall.union.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.stereotype.Repository;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.union.bean.OrdLogistics;
import com.meiliwan.emall.union.bean.Transport;
import com.meiliwan.emall.union.dao.dbtool.DBTool;
import com.meiliwan.emall.union.dao.dbtool.TransportDBTool;

@Repository
public class TransportDao {

	private static final MLWLogger LOG = MLWLoggerFactory.getLogger(TransportDao.class);

	/**
	 * 
	 */
	public boolean insertTransportLog(Transport transport){
		
		Connection conn = TransportDBTool.getConn();
		PreparedStatement pstmt = null;
	    boolean rs = false;
	    try {
		    	pstmt = conn.prepareStatement("insert into oms_ord_transport (order_id,info,logistics_company,cust_data_id,transport_time,create_time,logistics_number) values (?,?,?,?,?,?,?)");
		    	pstmt.setString(1, transport.getOrderId());
		    	pstmt.setString(2, transport.getInfo());
		    	pstmt.setString(3, transport.getLogisticsCompany());
		    	pstmt.setString(4, transport.getCustDataId());
		    	if(transport.getTransportTime()!=null){
		    		Timestamp transport_time = DateUtil.convert(transport.getTransportTime());
		    		pstmt.setTimestamp(5, transport_time);
		    	}else{
		    		pstmt.setTimestamp(5, DateUtil.getCurrentTimestamp());
		    	}
		    	pstmt.setTimestamp(6, DateUtil.getCurrentTimestamp());
		    	pstmt.setString(7, transport.getLogisticsNumber());
		    	
		    	rs = pstmt.execute();
		} catch (SQLException e) {
			LOG.error(e, "exec jdbc error", null);
		}finally{
			DBTool.closeConn(conn, pstmt);
		}
		
		return rs;
	}
	
	public boolean queryOrderByOrderId(String orderId){
		
		Connection conn = TransportDBTool.getConn();
		PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    boolean has=false;
	    try {
		    	pstmt = conn.prepareStatement("SELECT * FROM mlw_order.oms_ord where order_id=?");
		    	pstmt.setString(1, orderId);
		    	
		    	rs = pstmt.executeQuery();
		    	has = rs.first();
		} catch (SQLException e) {
			LOG.error(e, "exec jdbc error", null);
		}finally{
			DBTool.closeConn(conn, pstmt);
		}
	    return has;
	}
	
	public boolean queryByCustCodeAndId(String custCode,String custDataId){
		
		
		Connection conn = TransportDBTool.getConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean has=false;
		try {
			pstmt = conn.prepareStatement("SELECT * FROM mlw_order.oms_ord_transport where logistics_company=? and cust_data_id=?");
			pstmt.setString(1, custCode);
			pstmt.setString(2, custDataId);
			
			rs = pstmt.executeQuery();
			has = rs.first();
		} catch (SQLException e) {
			LOG.error(e, "exec jdbc error", null);
		}finally{
			DBTool.closeConn(conn, pstmt);
		}
		return has;
	}

	public Set<OrdLogistics> queryOrderLogisticsMinitueAgo(Date ago){
		
		Connection conn = TransportDBTool.getConn();
		QueryRunner qRunner = new QueryRunner();
		String sql = "SELECT * FROM mlw_order.oms_ord_logistics  where  create_time >= ? ";
		Object[] params=new Object[]{ago};
		Set<OrdLogistics> set=null;
		try {
			LOG.debug(sql,ago);
			List<OrdLogistics> results = (List)  qRunner.query(conn,sql,new BeanListHandler(OrdLogistics.class),params);
			 set = new HashSet<OrdLogistics>(results);
		} catch (SQLException e) {
			LOG.error(e, "exec jdbc error", null);
		}finally{
			 DbUtils.closeQuietly(conn); 
		}
	    return set;
	}

}
         
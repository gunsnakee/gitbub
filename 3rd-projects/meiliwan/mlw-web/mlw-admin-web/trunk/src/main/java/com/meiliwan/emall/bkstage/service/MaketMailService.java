package com.meiliwan.emall.bkstage.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.lang.StringUtils;

import com.meiliwan.emall.bkstage.bean.MarketMail;
import com.meiliwan.emall.bkstage.web.util.FileUtil;
import com.meiliwan.emall.bkstage.web.util.QueryHelper;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;

public class MaketMailService {

	private static final MLWLogger logger = MLWLoggerFactory.getLogger(MaketMailService.class);
	

	public final static Pattern emailer = Pattern.compile("[\\w[.-]]+@[\\w[.-]]+\\.[\\w]+");

	
	public static String getMail(String content) {  
	          
        Matcher m=emailer.matcher(content);  
        while(m.find()){  
            //输出邮箱地址  
            return m.group();
        }  
        return null;
    }  
	
	public static void saveMails(InputStream inputStream){
		if(inputStream==null){
			return ;
		}
		String code = FileUtil.getFilecharset(inputStream);
		try {
			//InputStream instream = new FileInputStream(file);

			BufferedReader br = new BufferedReader(new InputStreamReader(
					inputStream, code));
			String temp = "";
			while ((temp = br.readLine()) != null) {
				logger.debug(temp);
				String mail = getMail(temp);
				if(mail!=null){
					if(mail.contains("mop.com")){
						continue;
					}
					logger.debug(mail);
					save(mail);
				}
			}
			inputStream.close();
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error(e, null, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e, null, null);
		}
	}
	
	public static boolean save(String mail){
		Connection conn = BkstageDBTool.getConn();
		PreparedStatement pstmt = null;
	    boolean rs = false;
	    try {
		    	pstmt = conn.prepareStatement("insert into bks_market_mail (mail) values (?)");
		    	pstmt.setString(1, mail);
		    	return rs = pstmt.execute();
		} catch (SQLException e) {
			logger.error(e, mail, null);
		}finally{
			DbUtils.closeQuietly(conn);
		}
		return rs;
	}
	
	
	public static boolean del(int id){
		Connection conn = BkstageDBTool.getConn();
		PreparedStatement pstmt = null;
	    boolean rs = false;
	    try {
		    	pstmt = conn.prepareStatement("delete from bks_market_mail where id=?");
		    	pstmt.setInt(1, id);
		    	pstmt.execute();
		    	rs = true;
		} catch (SQLException e) {
			logger.error(e, id, null);
		}finally{
			DbUtils.closeQuietly(conn);
		}
		return rs;
	}
	
	
	@SuppressWarnings("unchecked")
	public static List<String> queryByLimit(int start,int end){
		Connection conn = BkstageDBTool.getConn();
	    
	    QueryRunner qRunner = new QueryRunner();
	    Object[] params=null;
	    String sql = null;
    		sql = "select mail from bks_market_mail limit ?,?";
    		params=new Object[]{start,end};
		List<String> list=null;
		try {
			
			list =  (List<String>) qRunner.query(conn, sql, QueryHelper.stringHandler, params);
			
		} catch (SQLException e) {
			logger.error(e, "exec jdbc error", null);
		}finally{
			 DbUtils.closeQuietly(conn); 
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static List<MarketMail> queryByObjLimit(String whereSql,int start,int end){
		Connection conn = BkstageDBTool.getConn();
	    
	    QueryRunner qRunner = new QueryRunner();
	    Object[] params=null;
	    String sql = null;
	    if(StringUtils.isBlank(whereSql)){
	    		sql = "select id,mail,send_times,update_time from bks_market_mail limit ?,?";
	    		params=new Object[]{start,end};
	    }else{
			sql="select id,mail,send_times,update_time from bks_market_mail where mail like '?%' limit ?,?";
			params=new Object[]{whereSql,start,end};
		}
		List<MarketMail> list=null;
		try {
			
			list =  (List<MarketMail>) qRunner.query(conn, sql, new BeanListHandler(MarketMail.class), params);
			
		} catch (SQLException e) {
			logger.error(e, "exec jdbc error", null);
		}finally{
			 DbUtils.closeQuietly(conn); 
		}
		return list;
	}
	
	
	public static int countTotal(){
		return countTotal(null);
	}
	
	public static int countTotal(String whereSql){
		Connection conn = BkstageDBTool.getConn();
	    
	    QueryRunner qRunner = new QueryRunner();
	    Object[] params=null;
	    String sql = "select count(*) from bks_market_mail ";
	    if(StringUtils.isNotBlank(whereSql)){
	    		sql+=" where mail like '?%'";
	    		params=new Object[]{whereSql};
	    }
		int count=0;
		try {
			count = (Integer) qRunner.query(conn, sql,QueryHelper.integerHandler,params);
			
		} catch (SQLException e) {
			logger.error(e, "exec jdbc error", null);
		}finally{
			 DbUtils.closeQuietly(conn); 
		}
		return count;
	}
	
	public static PagerControl<MarketMail> getPagerByObj( PageInfo pageInfo, String whereSql) {
        PagerControl<MarketMail> pagerControl = new PagerControl<MarketMail>();
        pageInfo.startTime();
        List<MarketMail> list = new ArrayList<MarketMail>();
        int total = 0;
        try {
            total = countTotal(whereSql);
            if(total > 0){
                list = queryByObjLimit(whereSql, pageInfo.getStartIndex(), pageInfo.getPagesize());
            }
        } catch (Exception e) {
            throw new ServiceException(e);
        }

        pageInfo.setTotalCounts(total);
        if (list != null) {
            pagerControl.setEntityList(list);
        }
        pagerControl.setPageInfo(pageInfo);
        return pagerControl;
    }

}

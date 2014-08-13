package com.meiliwan.emall.bkstage.web.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.meiliwan.emall.bkstage.jdbc.PmsJdbcTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
/**
 * sql文件导出工作类
 * @author lzl
 */
public class FileReportUtil {
	private static final MLWLogger logger = MLWLoggerFactory.getLogger(FileReportUtil.class);
	private static String MLW_PMS = "pms" ;
	private static String MLW_OMS = "oms" ;
	
	/**
	 * 
	 * @param sql     完整的查询sql
	 * @param source  exp：pms、oms
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getSqlList(String sql, String source){
		if(StringUtils.isBlank(sql) || StringUtils.isBlank(source)){
			logger.warn("参数错误.", "sql:"+sql+", source:"+source, null);
		}
		
		Connection conn = null ;
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(MLW_PMS.equals(source)){
			conn = PmsJdbcTool.getConnection();
	    	
	    }else if(MLW_OMS.equals(source)){
//	    	conn = OmsJdbcTool.getConnection();
	    	
	    }else{
	    	logger.warn("不存在的导出途径.", "source:"+source, null);
	    }
		
		if(conn != null){
			try{
		    	QueryRunner qRunner = new QueryRunner();
		    	list = (List<Map<String, Object>>)qRunner.query(conn, sql, new MapListHandler());
		    	
	    	}catch (Exception e) {
	    		logger.error(e, "执行sql出错", null);
			}
		}
		
		return list ;
	}
	
	/**
	 * 暂时不通
	 * 原因：
	 *   1、sql语句生成的xls文件并非标准Excel文件，pio无法解析
	 *   2、生成txt文件后再解析到Excel文件性能比直接查询解析差
	 * @param fileName
	 * @param sql
	 * @param title
	 * @param source
	 * @return
	 */
	public static boolean create(String fileName, String sql, String[] title, String source){
		boolean isOk = false ;
		if(StringUtils.isBlank(fileName) 
				|| StringUtils.isBlank(sql) 
				|| title.length <=0 
				|| StringUtils.isBlank(source)){
			logger.warn("参数错误.", "fileName:"+fileName+", sql:"+sql+", title:"+title+", source:"+source, null);
		}
		
		File file = new File("/tmp/"+fileName);
		// 路径为文件且不为空则进行删除   
	    if (file.isFile() && file.exists()) {   
	        file.delete();
	    }
	    if(MLW_PMS.equals(source)){
	    	try{
		    	Connection conn = PmsJdbcTool.getConnection();
		    	isOk = conn.prepareStatement(sql).execute(sql);
		    	
		    	HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream("/tmp/"+fileName));
		    	HSSFSheet sheet = workbook.getSheetAt(0);  //获取工作表
		    	
	    	}catch (Exception e) {
	    		logger.error(e, "执行sql出错", null);
			}
	    	
	    	
	    }else if(MLW_OMS.equals(source)){
	    	
	    	
	    }else{
	    	logger.warn("不存在的导出途径.", "source:"+source, null);
	    }
		
		return isOk;
	}
	
	/**
	 * 暂时不通
	 * @param fileName
	 * @param response
	 */
	public static void download(String fileName, HttpServletResponse response){
		if(StringUtils.isBlank(fileName)){
			logger.warn("参数错误.", "fileName:"+fileName, null);
		}
		File file = new File("/tmp/"+fileName);
		//如果文件夹不存在  
		if (!file.exists() && !file.isDirectory()) {       
			logger.warn("下载文件不存在.", "fileName:"+fileName, null);
			return ;
		}
		try{
			BufferedInputStream bis = null;
	        BufferedOutputStream bos = null;
			long fileLength = file.length();
	        response.setContentType("txt");
	        response.setHeader("Content-disposition", "attachment; filename="
	                + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
	        response.setHeader("Content-Length", String.valueOf(fileLength));
	        bis = new BufferedInputStream(new FileInputStream("/tmp/"+fileName));
	        bos = new BufferedOutputStream(response.getOutputStream());
	        byte[] buff = new byte[2048];
	        int bytesRead;
	        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
	            bos.write(buff, 0, bytesRead);
	        }
	        bis.close();
	        bos.close();
	        
		}catch (Exception e) {
			logger.error(e, "执行文件导出有误", null);
		}
	}
	
	public static void main(String[] args) {
		try{
//			String[] title = {"商品ID","条形码", "商品名称", "美丽价", "进货价", "所属馆", "所在类目", "预售结束时间", "库存"} ;
//			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream("/data/product_info.xls"));
//	    	HSSFSheet sheet = workbook.getSheetAt(0);  //获取工作表
//	    	PoiExcelUtil.buildTitles(sheet, title);
			
			String sql = "SELECT p.pro_id,p.bar_code, p.pro_name, p.mlw_price, p.trade_price, s.store_name, c.category_name, p.state, p.presale_end_time,  k.stock  FROM   mlw_product.pro_product p, mlw_product.pro_store_category s , mlw_product.pro_category c,  mlw_product.pro_stock k  WHERE  p.third_category_id = s.third_category_id AND  p.third_category_id = c.category_id AND  p.pro_id = k.pro_id AND p.state != '-1' ORDER BY  p.pro_id" ;
			Connection conn = PmsJdbcTool.getConnection();
	    	QueryRunner qRunner = new QueryRunner();
	    	List<Map<String, Object>> list = (List<Map<String, Object>>)qRunner.query(conn, sql, new MapListHandler());
	    	System.out.println(list.get(0).get("pro_name"));
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package com.meiliwan.emall.bkstage.web.jreport.factory;

import java.util.ArrayList;
import java.util.List;

import com.meiliwan.emall.bkstage.web.jreport.vo.BaseReportVO;
import com.meiliwan.emall.bkstage.web.jreport.vo.DeliverVoucher;

public class DeliverVoucherFactory {

	 private static DeliverVoucher[] dios = { 
		 
		 new DeliverVoucher(1, "昵称1", "北京", "猫扑网", "北京天安门", "1234567") ,
		 new DeliverVoucher(2, "昵称2", "上海", "猫扑网", "上海外滩", "021-1234567") ,
		 new DeliverVoucher(3, "昵称3", "南宁", "猫扑网", "南宁青秀区", "0778-1234567") 
		 };
	       
	 public static Object[] getBeanArray() {
		 return dios;
	 }
	 

	 /**
	     * 这个方法在iReport的DataResource配置时也会用到
	     * 必须是静态方法 static
	     *
	     * @return
	     */
	    public static List<BaseReportVO> getBeanCollection() {
	    	List<BaseReportVO> beanCollection = new ArrayList<BaseReportVO>();

	        for (int i = 0; i < dios.length; i++) {
	            beanCollection.add(dios[i]);
	        }

	        return beanCollection;
	    }
}

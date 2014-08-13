package com.meiliwan.emall.oms.client;

import java.util.List;

import org.testng.annotations.Test;

import com.meiliwan.emall.oms.bean.OrdLogistics;

/**
 * Created by guangdetang on 13-8-26.
 */
public class OrdLogisticsClientTest {

   
    @Test
    public void  testGetOrderLogisticsNumberList(){
	    	OrdLogistics bean  = new OrdLogistics();
	    	
	    	List<OrdLogistics> list = OrdLogisticsClient.getOrderLogisticsNumberList(bean);
	    	System.out.println(list);
    	
	    	
    }
    
}

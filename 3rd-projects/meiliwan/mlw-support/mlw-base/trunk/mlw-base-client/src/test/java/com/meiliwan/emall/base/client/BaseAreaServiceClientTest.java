package com.meiliwan.emall.base.client;

import java.util.List;
import org.testng.annotations.Test;

import com.meiliwan.emall.base.bean.BaseAreaManagement;

/**
 * 
 * @author yinggao.zhuo
 * @date 2013-6-8
 */
public class BaseAreaServiceClientTest {

	
    //@Test
    public void testGetBypid(){
    	List<BaseAreaManagement>  list = BaseAreaServiceClient.getAreasByParentCode("0");
    	
    }
    @Test
    public void getAreasByNameAndGrade(){
        String result = BaseAreaServiceClient.getAreasByNameAndGrade("广西",1);
        System.out.print(result);
    }




}

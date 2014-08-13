package com.meiliwan.emall.pms.service;
//
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.meiliwan.emall.pms.BaseTest;
@Test
public class ProProductServiceTest extends  BaseTest {
	
	
    @Autowired
    private ProProductService service;
    
    private String initDbfile = "service/product_pre.xml";
	private String backupDbFile = "/data/backup/test/pms-service/product_back.xml";
	private String[] btables = {"pro_product"};
    
    
    @BeforeClass
    public void beforeClass() {
		this.init(initDbfile, backupDbFile, btables);
    }
    
	@AfterClass
	public void afterClass() {
		// db数据库初始化
		this.tearDown();  
	}

    @Test
    public void testUpdateProProduct() throws Exception {
    	System.out.println("Update ProProduct");
    }
    
    
    
}

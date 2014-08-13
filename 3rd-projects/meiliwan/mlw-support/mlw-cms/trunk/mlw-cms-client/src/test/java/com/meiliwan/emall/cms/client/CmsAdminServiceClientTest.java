package com.meiliwan.emall.cms.client;

//import com.alibaba.fastjson.TypeReference;

import com.meiliwan.emall.cms.bean.CmsPage;
import org.testng.annotations.Test;

/**
 * User: wuzixin
 * Date: 13-6-20
 * Time: 下午2:21
 */
public class CmsAdminServiceClientTest{

	@Test
    public void getPageBySiteId() {
    		CmsAdminServiceClient.getPageBySiteId(-111111);
    }

	@Test
    public void getAllTemplate() {
    		CmsAdminServiceClient.getAllTemplate();
    }
	
	@Test
    public void addCmsPage() {
    		CmsAdminServiceClient.addCmsPage(new CmsPage());
    }
}

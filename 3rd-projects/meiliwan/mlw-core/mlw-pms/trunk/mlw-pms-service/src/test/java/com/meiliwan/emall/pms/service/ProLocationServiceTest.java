package com.meiliwan.emall.pms.service;


import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.pms.BaseTest;
import com.meiliwan.emall.pms.bean.ProLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

public class ProLocationServiceTest extends BaseTest {

    @Autowired
    private ProLocationService service;
    private JsonObject resultObj = new JsonObject();

    @Test
     public void testAddProLocation(){
        ProLocation location = new ProLocation();
        location.setBarCode("401000068");
        location.setLocationName("A1-1-1-1");
        service.addProLocation(resultObj,location);
    }

    @Test
    public void testUpdateProLocation(){
        ProLocation location = new ProLocation();
        location.setLocationId(1);
        location.setLocationName("A1-1-1-2");
        service.updateProLocation(resultObj, location);
    }

    @Test
    public void testPageByObj(){
        ProLocation location = new ProLocation();
        location.setProName("泰国工艺品");
        service.pageByObj(resultObj,location,new PageInfo(1,10),null,null);
    }

    @Test
    public void testGetListByBarCode(){
        String[] codes = new String[2];
        codes[0] = "401000068";
        codes[1] = "401000088";
        service.getListByBarCode(resultObj,codes);
    }
}

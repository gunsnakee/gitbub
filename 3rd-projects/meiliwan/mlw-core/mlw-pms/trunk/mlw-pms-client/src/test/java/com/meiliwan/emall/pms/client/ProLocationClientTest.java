package com.meiliwan.emall.pms.client;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.pms.bean.ProLocation;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * User: wuzixin
 * Date: 13-11-15
 * Time: 下午5:22
 */
public class ProLocationClientTest {

    @Test
    public void testAddProLocation() throws Exception {
        ProLocation location = new ProLocation();
        location.setBarCode("401000070");
        ProLocationClient.addProLocation(location);
    }

    @Test
    public void testUpdateProLocation() throws Exception {
        ProLocation location = new ProLocation();
        location.setLocationId(1);
        location.setLocationName("A1-1-1-4");
        ProLocationClient.updateProLocation(location);
    }

    public void testGetLocationById() throws Exception {

    }

    @Test
    public void testGetPageByObj() throws Exception {
        ProLocation location = new ProLocation();
        location.setProName("泰国工艺品");
        PagerControl<ProLocation> page=ProLocationClient.getPageByObj(location, new PageInfo(1, 10), null, null);
        System.out.println(page);
    }

    @Test
    public void testGetListByBarCode() throws Exception {
        List<String> codes = new ArrayList<String>();
        codes.add("401000068");
        codes.add("401000088");
        List<ProLocation> list = ProLocationClient.getListByBarCode(codes);
        System.out.println(list);
    }
}

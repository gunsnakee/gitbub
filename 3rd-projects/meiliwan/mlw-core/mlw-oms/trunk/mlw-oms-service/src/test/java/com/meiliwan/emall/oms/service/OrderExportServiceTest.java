package com.meiliwan.emall.oms.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.oms.BaseTest;
import com.meiliwan.emall.oms.bean.OrdRemark;
import com.meiliwan.emall.oms.client.OrdClient;
import com.meiliwan.emall.oms.dto.OrdDTO;
import com.meiliwan.emall.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-6
 * Time: 下午3:51
 * To change this template use File | Settings | File Templates.
 */
public class OrderExportServiceTest extends BaseTest {

    @Autowired
    private OrderService orderService;
    private JsonObject resultObj;

    @BeforeMethod
    public void BeforeMethod(){
        if(resultObj == null)resultObj= new JsonObject();
    }

    @Test
    public void testGetOrdDTOListBy(){
        List<String> list = new ArrayList<String>();
        list.add("000004258188");
        list.add("000004258196");
        list.add("000004258197");
        list.add("000004258198");
        list.add("000004258203");
        list.add("000004258221");

        orderService.getOrdDTOListBy(resultObj,(String[])list.toArray());

        List<OrdDTO> result = new Gson().fromJson(resultObj.get(BaseService.RESULT_OBJ), new TypeToken<List<OrdDTO>>() {
        }.getType());

        System.out.println(result);
    }

    @Test
    public void testhey(){
       List<String> list = new ArrayList<String>();
        list.add("1");

        System.out.println(list);

        add(list);
        System.out.println(list);
        return;
    }

    private void add(List<String> list){
        list.add("2");
    }
}

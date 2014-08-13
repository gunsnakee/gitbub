package com.meiliwan.emall.pms.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.pms.BaseTest;
import com.meiliwan.emall.pms.bean.ProCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.List;

/**
 * User: wuzixin
 * Date: 13-8-19
 * Time: 下午4:22
 * 商品类目测试类
 */
public class ProCategoryServiceTest extends BaseTest{

    @Autowired
    private ProCategoryService service;
    @Autowired
    private ProPropertyService proPropertyService;


    @Test
    public void testMethod(){
        ProCategory category = new ProCategory();
        category.setCategoryId(126);
        category.setCategoryName("类目类目");

        JsonObject json = new JsonObject();

        service.getListByCategoryId(json, 2);

    }

    @Test
    public void testProperty(){
        JsonObject json = new JsonObject();

        proPropertyService.getPropListByProId(json,10235107);
    }

    @Test
    public void testGetListByCategoryId(){
        JsonObject json = new JsonObject();
        service.getListByCategoryId(json,4);
        List<ProCategory> list = new Gson().fromJson(json.get("resultObj"), new TypeToken<List<ProCategory>>() {
        }.getType());

        System.out.println("##########:"+list);
    }
    @Test
    public void testGetPorps(){
        JsonObject json = new JsonObject();
        proPropertyService.getPropByProProp(json,83,10000001);
    }
}

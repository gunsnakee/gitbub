package com.meiliwan.emall.sp2.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.sp2.base.BaseTest;
import com.meiliwan.emall.sp2.bean.ActivityProductBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Date;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-24
 * Time: 下午2:19
 * To change this template use File | Settings | File Templates.
 */
public class ActivityProductServiceTest extends BaseTest {
    @Autowired
    ActivityProductService activityProductService;

    /**
     *
     */
    // @Test
    public void testSaveSpActivityProduct(){
        ActivityProductBean act = new ActivityProductBean();
        act.setActId(5);
        act.setProId(10235405);
        act.setProName("泰国工艺品 小号花瓶A-蓝花竹筒形 芒果木花瓶 家居摆件");
        act.setActPrice(new BigDecimal(990.55));
        act.setMlwPrice(new BigDecimal(1000.55));
        act.setThirdCatId(173);
        act.setAdmin("admin");

        ActivityProductBean act1 = new ActivityProductBean();
        act1.setActId(4);
        act1.setProId(10235315);
        act1.setProName("广西南宁加工 红酸枝贴牛骨手串1.5cm 15颗");
        act1.setActPrice(new BigDecimal(26.1));
        act1.setMlwPrice(new BigDecimal(29));
        act1.setDiscount(90);   //九折
        act1.setThirdCatId(133);
        act1.setAdmin("admin");

        JsonObject json = new JsonObject();
        activityProductService.saveSpActivityProduct(json, act1);

        ActivityProductBean act2 = new ActivityProductBean();
        act2.setActId(4);
        act2.setProId(10235364);
        act2.setProName("马来西亚原装进口 旧街场2合1白咖啡360g  速溶白咖啡");
        act2.setActPrice(new BigDecimal(28));
        act2.setMlwPrice(new BigDecimal(35));
        act2.setDiscount(80);   //九折
        act2.setThirdCatId(94);
        act2.setAdmin("admin");

        JsonObject json2 = new JsonObject();
        activityProductService.saveSpActivityProduct(json2, act2);

        ActivityProductBean act3 = new ActivityProductBean();
        act3.setActId(4);
        act3.setProId(10235359);
        act3.setProName("越南进口 原装中原G7三合一速溶咖啡160g");
        act3.setActPrice(new BigDecimal(9.9));
        act3.setMlwPrice(new BigDecimal(11));
        act3.setDiscount(90);   //九折
        act3.setThirdCatId(94);
        act3.setAdmin("admin");

        JsonObject json3 = new JsonObject();
        activityProductService.saveSpActivityProduct(json3, act3);

        ActivityProductBean act4 = new ActivityProductBean();
        act4.setActId(4);
        act4.setProId(10235361);
        act4.setProName("马来西亚原装进口 旧街场3合1原味白咖啡 480g  速溶咖啡");
        act4.setActPrice(new BigDecimal(24.5));
        act4.setMlwPrice(new BigDecimal(35));
        act4.setDiscount(70);   //九折
        act4.setThirdCatId(94);
        act4.setAdmin("admin");

        JsonObject json4 = new JsonObject();
        activityProductService.saveSpActivityProduct(json4, act4);
    }

    @Test
    public void getAPByProIdAndTimes(){
        JsonObject json = new JsonObject();
        activityProductService.getAPByProIdAndTimes(json,10235359,new Date(),new Date());

    }
    /**
     *
     */
   /* @Test
    public void testgetAndUpdateSpActivityProduct(){
        JsonObject json = new JsonObject();
        spActivityProductService.getSpActivityProductById(json, 1);

        SpActivityProduct actPro = new SpActivityProduct();
        actPro.setId(1);
        actPro.setActId(3);
        actPro.setProId(10235405);
        actPro.setProName("泰国工艺品 小号花瓶A-蓝花竹筒形 芒果木花瓶 家居摆件");
        actPro.setActPrice(990.55);
        actPro.setMlwPrice(1000.55);
        actPro.setAdmin("admin01");

        JsonObject jso1 = new JsonObject();
        spActivityProductService.updateSpActivityProduct(jso1, actPro);
    }*/


    /**
     * 通过活动 实体参数获取对应的实体列表包含物理分页
     *
     */
    // @Test
   /* public void getSpActivityPaperByObj() {
        JsonObject  json = new JsonObject();
        SpActivityProduct actPro = new SpActivityProduct();
        PageInfo pageInfo = new PageInfo();
        spActivityProductService.getSpActivityProductPaperByObj(json, actPro, pageInfo);
    }*/
}

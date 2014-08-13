package com.meiliwan.emall.pms.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.pms.BaseTest;
import com.meiliwan.emall.pms.bean.ProPropertyValue;
import com.meiliwan.emall.pms.bean.ProSpu;
import com.meiliwan.emall.pms.dto.ProductNamesDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.List;

public class ProSpuServiceTest extends BaseTest {
    @Autowired
    private ProSpuService service;
    @Autowired
    private ProPropertyService propertyService;
    private JsonObject resultObj = new JsonObject();
    @Test
    public void testUpdateProductNams() throws Exception {
        ProductNamesDto dto = new ProductNamesDto();
        dto.setSpuId(10000001);
        dto.setProName("商品标题10000001");
        dto.setShortName("商品短标题10000001");
        dto.setAdvName("商品的宣传标题10000001");

        service.updateProductNames(resultObj,dto);

        System.out.println(resultObj.get("resultObj").getAsBoolean());

    }

    @Test
    public void testUpdatePropStr(){
        List<ProSpu> list = service.getAllListSpu();
        if (list!=null&&list.size()>0){
            for (ProSpu spu:list){
                if (StringUtils.isNotEmpty(spu.getPropertyString())){
                    String[] propVids = spu.getPropertyString().split(",");
                    StringBuffer propStr = new StringBuffer();
                    boolean suc = true;
                    for (String str:propVids){
                        ProPropertyValue value = propertyService.getValueById(Integer.parseInt(str));
                        if (value!=null){
                            if (suc){
                                propStr.append(value.getProPropId()+":"+value.getId());
                                suc = false;
                            }else {
                                propStr.append(","+value.getProPropId()+":"+value.getId());
                            }
                        }
                    }
                    service.updatePropStr(spu.getSpuId(),propStr.toString());
                }
            }
        }
    }

}

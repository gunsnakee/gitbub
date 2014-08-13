package com.meiliwan.emall.pms.service.helper;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.pms.bean.*;
import com.meiliwan.emall.pms.cache.dto.PropValueSkuDto;
import com.meiliwan.emall.pms.constant.Constant;
import com.meiliwan.emall.pms.dao.ProProductDao;
import com.meiliwan.emall.pms.dao.ProPropertyValueDao;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 14-2-26
 * Time: 下午3:49
 *  ProProductService帮助类
 */
public class ProProductServiceHelper {
    private static final MLWLogger logger = MLWLoggerFactory.getLogger(ProProductServiceHelper.class);

    /**
     * 根据spuId 获取sku集合
     * @param spuId
     * @param products
     * @return
     */
    public static Map<String,SimpleProduct> getProductMapBySkuId(Integer spuId,List<SimpleProduct> products){
        Map<String,SimpleProduct> productMap = new HashMap<String,SimpleProduct>();
        for(SimpleProduct sPro:products){
            if(StringUtils.isNotBlank(sPro.getSkuPropertyStr())){
                String[] props = sPro.getSkuPropertyStr().split(";");
                productMap.put(sPro.getSkuPropertyStr(),sPro);
               /* if(props.length==1){     //单规格
                    productMap.put(sPro.getSkuPropertyStr(),sPro);
                }
                if(props.length==2){   //双规格
                    try{
                        String prop1 = props[0];
                        String prop2 = props[1];
                        productMap.put(prop1,sPro);
                        productMap.put(prop2,sPro);
                    }catch (Exception e){
                        logger.warn("构建spu规格换成数据，sku.getSkuPropertyStr() 格式对！",sPro,null);
                    }
                }*/
            }
        }
        return  productMap;
    }

    /**
     *  获取属性 valueId
     * @param skuPropertyStr
     * @return
     */
    public static int getPropValueId(String skuPropertyStr){
        int vid = 0;
        if(StringUtils.isNotBlank(skuPropertyStr)){
            String[] pvIds = skuPropertyStr.split(";")[0].split(":");
            if(pvIds.length>1){
                if(StringUtils.isNumeric(pvIds[1])){
                    vid = Integer.parseInt(pvIds[1]);
                }
            }
        }
        return vid;
    }

    /**
     * 获取属性值对应sku列表
     * @param proProp
     * @param productMap
     * @return
     */
    public static List<PropValueSkuDto> getPvSkuList(ProProperty proProp,Map<String,SimpleProduct> productMap,
                                                     Integer proId, String propStr){
        List<PropValueSkuDto> pvSkuList = new ArrayList<PropValueSkuDto>();
        ProPropertyValue ppvQuery = new ProPropertyValue();
        ppvQuery.setProPropId(proProp.getProPropId());
        List<ProPropertyValue> ppvList = proProp.getProProValue();

        if(ppvList !=null && ppvList.size()>0 && productMap.size()>0){
            for(ProPropertyValue ppv : ppvList){
                PropValueSkuDto pvskd = new PropValueSkuDto();
                pvskd.setPropValueId(ppv.getId());
                pvskd.setPropValue(ppv.getName());
                SimpleProduct sPro = productMap.get(proProp.getProPropId()+":"+ppv.getId()+";"+propStr);
                if(sPro==null){
                    sPro = productMap.get(propStr+proProp.getProPropId()+":"+ppv.getId()+";");
                }
                if(sPro !=null){
                    if (sPro.getState()!=null && sPro.getState().intValue() == Constant.PRODUCT_ON){
                        pvskd.setSkuId(sPro.getId()); //设置skuid
                    }
                    if(proId== sPro.getProId()){ //设置选中
                        pvskd.setSelected("1");
                    }else{
                        pvskd.setSelected("0");
                    }
                }
                pvSkuList.add(pvskd);
            }
        }
        return  pvSkuList;
    }


    /**
     * 设置PropValueSkuDto的skuid
     * @param pvSkuList
     * @param productMap
     * @param proId
     * @param proProp
     */
    public static void setPropValueSkuDtoSkuId( List<PropValueSkuDto> pvSkuList,Map<String, SimpleProduct> productMap,ProProductProperty proProp,Integer proId){
        for(PropValueSkuDto pvs:pvSkuList) {
            String propValueId = pvs.getPropValueId().toString();
            String propPropIdStr = proProp.getProPropId().toString();
            SimpleProduct product = productMap.get(proProp.getProPropId()+":"+propValueId);
            if(product!=null && product.getProId()>0){
                pvs.setSkuId(product.getProId());
                if(proProp.getIsImage()==1){
                    pvs.setImageUrl(product.getDefaultImageUri());
                }
                if(proId== product.getProId()){
                    pvs.setSelected("1");
                }else{
                    pvs.setSelected("0");
                }
            }
        }
    }

    //异图时请把未构建的规格填充剩余的图
    public static void buildDefaultImage(List<PropValueSkuDto> pvSkuList, List<ProImages> imagesList){
        if(pvSkuList!=null && pvSkuList.size()>0 && imagesList!=null && imagesList.size()>0){
            Map<Integer, String > imagePvMap = new HashMap();
            for(ProImages images:imagesList){
                if(images.getProProvId()>0 && StringUtils.isNotBlank(images.getDefaultImageUri())){
                    imagePvMap.put(images.getProProvId(),images.getDefaultImageUri());
                }
            }
            for(PropValueSkuDto propValueSkuDto:pvSkuList){
                 if(propValueSkuDto.getPropValueId()>0){
                    String defaultImage = imagePvMap.get(propValueSkuDto.getPropValueId());
                    if(StringUtils.isNotBlank(defaultImage)){
                        propValueSkuDto.setImageUrl(defaultImage);
                    }
                 }
            }

        }

    }

}

package com.meiliwan.emall.pms.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.pms.bean.*;
import com.meiliwan.emall.pms.constant.Constant;
import com.meiliwan.emall.pms.dao.ProProductPropertyDao;
import com.meiliwan.emall.pms.dao.ProPropertyDao;
import com.meiliwan.emall.pms.dao.ProPropertyValueDao;
import com.meiliwan.emall.pms.dao.ProductCacheDao;
import com.meiliwan.emall.pms.dto.PropertyValueList;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * User: wuzixin
 * Date: 13-6-3
 * Time: 上午11:28
 */
@Service
public class ProPropertyService extends DefaultBaseServiceImpl {

    @Autowired
    private ProPropertyDao proPropertyDao;
    @Autowired
    private ProPropertyValueDao proPropertyValueDao;
    @Autowired
    private ProProductPropertyDao proProductPropertyDao;
    @Autowired
    private ProductCacheDao cacheDao;

    /**
     * 增加商品属性
     */
    public void addProProperty(JsonObject resultObj, ProProperty proProperty) {
        if (proProperty != null) {
            try {
                proPropertyDao.insert(proProperty);
                if (proProperty.getProPropId() > 0) {
                    if (proProperty.getProProValue() != null) {
                        for (ProPropertyValue ppv : proProperty.getProProValue()) {
                            if (ppv == null) continue;
                            if (!StringUtils.isEmpty(ppv.getName())) {
                                ppv.setProPropId(proProperty.getProPropId());
                                proPropertyValueDao.insert(ppv);
                            }
                        }
                    }
                    addToResult(true, resultObj);
                }
            } catch (Exception e) {
                addToResult(false, resultObj);
                throw new ServiceException("service-pms-ProPropertyService.addProProperty:{}", proProperty == null ? "" : proProperty.toString(), e);
            }
        } else {
            addToResult(false, resultObj);
        }
    }

    /**
     * 修改商品属性
     *
     * @param proProperty
     */
    public void updateProProperty(JsonObject resultObj, ProProperty proProperty) {
        try {
            if (proProperty.getProProValue() != null) {
                for (ProPropertyValue ppv : proProperty.getProProValue()) {
                    if (ppv == null) continue;
                    //判断是增加还是修改
                    if (ppv.getId().equals(0)) {
                        //判断属性值是否为空，为空则不增加
                        if (!StringUtils.isEmpty(ppv.getName())) {
                            ppv.setProPropId(proProperty.getProPropId());
                            proPropertyValueDao.insert(ppv);
                        }
                    } else {
                        //再分为两个流程，如果属性值为空，则删除原有的属性值，否则修改
                        if (StringUtils.isEmpty(ppv.getName())) {
                            //删掉数据
                            proPropertyValueDao.delete(ppv.getId());
                        } else {
                            proPropertyValueDao.update(ppv);
                        }
                    }
                }
            }
            proPropertyDao.update(proProperty);
            addToResult(true, resultObj);
        } catch (Exception e) {
            addToResult(false, resultObj);
            throw new ServiceException("service-pms-ProPropertyService.updateProProperty:{}", proProperty == null ? "" : proProperty.toString(), e);
        }
    }

    /**
     * 通过ID获取商品属性
     *
     * @param proPropId
     */
    public void getProPropertyById(JsonObject resultObj, int proPropId) {
        ProPropertyValue ppv = new ProPropertyValue();
        ppv.setProPropId(proPropId);
        List<ProPropertyValue> ppvList = proPropertyValueDao.getListByObj(ppv, null);
        ProProperty ppt = proPropertyDao.getEntityById(proPropId);
        ppt.setProProValue(ppvList);
        addToResult(ppt, resultObj);
    }

    /**
     * 通过实体参会获取商品属性
     *
     * @param proProperty
     */
    public void getProPropertyList(JsonObject resultObj, ProProperty proProperty) {
        if (proProperty != null) {
            List<ProProperty> proProperties = proPropertyDao.getListByObj(proProperty, "", "order by sequence asc");
            ProPropertyValue ppv = new ProPropertyValue();
            for (ProProperty pp : proProperties) {
                ppv.setProPropId(pp.getProPropId());
                List<ProPropertyValue> propertyValues = proPropertyValueDao.getListByObj(ppv, null);
                pp.setProProValue(propertyValues);
            }
            addToResult(proProperties, resultObj);
        }
    }

    /**
     * 获取商品属性，进行分页
     *
     * @param proProperty
     * @param pageInfo
     */
    public void getProPropertyPager(JsonObject resultObj, ProProperty proProperty, PageInfo pageInfo) {
        if (proProperty != null && pageInfo != null) {
            addToResult(proPropertyDao.getPagerByObj(proProperty, pageInfo, "order by sequence asc"), resultObj);
        }
    }

    /**
     * 商品的启用和禁用功能
     *
     * @param proProperty
     */
    public void resetPropertyBystus(JsonObject resultObj, ProProperty proProperty) {
        ProProductProperty ppp = new ProProductProperty();
        ppp.setProPropId(proProperty.getProPropId());
        PageInfo pageInfo = new PageInfo();
        pageInfo.setStartIndex(0);
        pageInfo.setPagesize(1);
        List<ProProductProperty> ppps = proProductPropertyDao.getListByObj(ppp, pageInfo, null, true);
        if (ppps.size() > 0) {
            addToResult(false, resultObj);
        } else {
            proPropertyDao.update(proProperty);
            addToResult(true, resultObj);
        }
    }

    public void getPropByProProp(JsonObject resultObj, int third, int spuId) {
        List<ProProperty> properties = getAll(third);
        Map<Integer, String> map = new HashMap<Integer, String>();
        Map<Integer, String> imagMap = new HashMap<Integer, String>();
        Map<Integer, String> skuMap = new HashMap<Integer, String>();
        ProProductProperty pppt = new ProProductProperty();
        pppt.setSpuId(spuId);
        pppt.setIsSku(null);
        List<ProProductProperty> list = proProductPropertyDao.getListByObj(pppt, null);
        if (list != null && list.size() > 0) {
            for (ProProductProperty ppp : list) {
                if (StringUtils.isEmpty(ppp.getValueId())) {
                    map.put(ppp.getProPropId(), ppp.getValue());
                } else {
                    map.put(ppp.getProPropId(), "," + ppp.getValueId() + ",");
                }
                //标记是否是意图
                if (ppp.getIsImage() == 1) {
                    imagMap.put(ppp.getProPropId(), "1");
                } else {
                    imagMap.put(ppp.getProPropId(), "0");
                }
                //标记是否是SKU属性
                if (ppp.getIsSku() == 1) {
                    skuMap.put(ppp.getProPropId(), "1");
                } else {
                    skuMap.put(ppp.getProPropId(), "0");
                }
            }
            for (ProProperty pp : properties) {
                //排除不是对应商品sku属性的类目属性，也就是把其置为普通属性
                if (skuMap.get(pp.getProPropId()) != null && skuMap.get(pp.getProPropId()).equals("1")) {
                    pp.setIsSku((short) 1);
                } else {
                    if (pp.getIsSku() == Constant.IS_SKU_YES) {
                        pp.setCheckSku(1);
                    }
                    pp.setIsSku((short) 0);
                }
                //标记是异图的属性
                if (imagMap.get(pp.getProPropId()) != null && imagMap.get(pp.getProPropId()).equals("1")) {
                    pp.setIsImage((short) 1);
                }
                if (pp.getPropertyType() == 3) {
                    pp.setDescp(map.get(pp.getProPropId()));
                } else {
                    for (ProPropertyValue ppv : pp.getProProValue()) {
                        String key = map.get(ppv.getProPropId());
                        if (StringUtils.isEmpty(key)) {
                        } else {
                            int index = map.get(ppv.getProPropId()).indexOf("," + ppv.getId() + ",");
                            if (index >= 0) {
                                ppv.setFill(1);
                            }
                        }
                    }
                }
            }
        }
        addToResult(properties, resultObj);
    }

    /**
     * 获取对应的三级类目属性
     *
     * @param resultObj
     * @param third
     */
    public void getPropertyListByCategoryId(JsonObject resultObj, int third) {
        addToResult(getAll(third), resultObj);
    }

    /**
     * 通过商品ID获取商品属性列表
     *
     * @param resultObj
     * @param spuId
     */
    public void getPropListByProId(JsonObject resultObj, int spuId) {
        List<ProProperty> properties = new ArrayList<ProProperty>();
        //获取商品和属性的关系
        ProProductProperty ppp = new ProProductProperty();
        ppp.setSpuId(spuId);
        ppp.setIsSku(null);
        List<ProProductProperty> ppps = proProductPropertyDao.getListByObj(ppp, null);
        for (ProProductProperty pppv : ppps) {
            //获取对应的属性
            ProProperty pp = proPropertyDao.getEntityById(pppv.getProPropId());
            if (pppv.getIsSku() == 1) {
                pp.setIsSku((short) 1);
            } else {
                pp.setIsSku((short) 0);
            }
            if (pppv.getIsImage() == 1) {
                pp.setIsImage(1);
            }
            if (!StringUtils.isEmpty(pppv.getValueId())) {
                String[] ids = pppv.getValueId().split(",");
                //获取属性值列表
                List<ProPropertyValue> ppvs = proPropertyValueDao.getValuesByIds(ids);
                pp.setProProValue(ppvs);
            } else {
                //获取属性值，这个属于用户自己填写的名称
                pp.setDescp(pppv.getValue());
            }
            properties.add(pp);
        }
        addToResult(properties, resultObj);
    }


    /**
     * 根据SPU ID 获取spu对应的规格属性信息
     *
     * @param resultObj
     * @param spuId
     */
    public void getSpuWithSkuPropsById(JsonObject resultObj, int spuId) {
        List<ProProperty> pps = cacheDao.getSpuWithSkuPropsCacheById(spuId);
        addToResult(pps, resultObj);
    }

    public List<ProProperty> getAll(int first, int second, int third) {
        String sqlStr = "(" + first + "," + second + "," + third + ")";
        List<ProProperty> properties = proPropertyDao.getListByObj(null, "category_id in " + sqlStr);
        ProPropertyValue ppv = new ProPropertyValue();
        for (ProProperty pp : properties) {
            ppv.setProPropId(pp.getProPropId());
            List<ProPropertyValue> propertyValues = proPropertyValueDao.getListByObj(ppv, null);
            pp.setProProValue(propertyValues);
        }
        return properties;
    }

    List<ProProperty> getAll(int thirdCategoryId) {
        ProProperty property = new ProProperty();
        property.setCategoryId(thirdCategoryId);
        property.setState((short) 1);
        List<ProProperty> properties = proPropertyDao.getListByObj(property, null);
        ProPropertyValue ppv = new ProPropertyValue();
        for (ProProperty pp : properties) {
            ppv.setProPropId(pp.getProPropId());
            List<ProPropertyValue> propertyValues = proPropertyValueDao.getListByObj(ppv, null);
            pp.setProProValue(propertyValues);
        }
        return properties;
    }

    /**
     * 获取全部商品属性和属性值列表，给搜索使用
     *
     * @param resultObj
     */
    public void getAllPPVList(JsonObject resultObj) {
        PropertyValueList list = new PropertyValueList();
        ProProperty proProperty = new ProProperty();
        proProperty.setState((short) 1);
        proProperty.setIsFilter((short) 1);
        String wheresql = " property_type != 3";
        List<ProProperty> properties = proPropertyDao.getListByObj(proProperty, wheresql);
        List<ProPropertyValue> propertyValues = proPropertyValueDao.getAllEntityObj();
        list.setProperties(properties);
        list.setPropertyValues(propertyValues);

        addToResult(list, resultObj);
    }

    /**
     * 通过属性值ID获取属性值
     *
     * @param resultObj
     * @param valueId
     */
    public void getPropertyValueById(JsonObject resultObj, int valueId) {
        ProPropertyValue ppv = proPropertyValueDao.getEntityById(valueId);
        addToResult(ppv, resultObj);
    }

    public ProPropertyValue getValueById(int valueId) {
        return proPropertyValueDao.getEntityById(valueId);
    }

    /**
     * 根据商品ID获取商品规格属性名称
     *
     * @param resultObj
     * @param proId
     */
    public void getSkuProVsByProId(JsonObject resultObj, int proId) {
        ProProduct product = cacheDao.getSkuCacheByProId(proId);
        List<ProPropertyValue> values = new ArrayList<ProPropertyValue>();
        if (product != null) {
            if (StringUtils.isNotBlank(product.getSkuPropertyStr())) {
                String[] vidStr = product.getSkuPropertyStr().split(";");
                if (vidStr.length > 1) {
                    for (String str : vidStr) {
                        ProPropertyValue value = proPropertyValueDao.getEntityById(Integer.parseInt(str.split(":")[1]));
                        values.add(value);
                    }
                } else {
                    ProPropertyValue value = proPropertyValueDao.getEntityById(Integer.parseInt(vidStr[0].split(":")[1]));
                    values.add(value);
                }
            }
        }
        addToResult(values, resultObj);
    }
}

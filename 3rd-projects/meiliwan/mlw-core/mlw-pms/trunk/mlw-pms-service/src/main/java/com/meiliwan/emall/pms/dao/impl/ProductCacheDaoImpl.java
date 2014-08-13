package com.meiliwan.emall.pms.dao.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.pms.bean.*;
import com.meiliwan.emall.pms.constant.Constant;
import com.meiliwan.emall.pms.dao.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * User: wuzixin
 * Date: 14-3-7
 * Time: 下午2:27
 */
@Repository
public class ProductCacheDaoImpl implements ProductCacheDao {
    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());
    @Autowired
    private ProProductDao proProductDao;
    @Autowired
    private ProSelfPropertyDao proSelfPropertyDao;
    @Autowired
    private SkuSelfPropertyDao skuSelfPropertyDao;
    @Autowired
    private ProProductPropertyDao proProductPropertyDao;
    @Autowired
    private ProPropertyDao propertyDao;
    @Autowired
    private ProPropertyValueDao propertyValueDao;

    @Override
    public boolean setSkuSelfPropsCacheByProId(int proId) {
        //获取商品固有属性
        SkuSelfProperty property = new SkuSelfProperty();
        property.setProId(proId);
        List<SkuSelfProperty> properties = skuSelfPropertyDao.getListByObj(property);
        Gson gson = new Gson();
        //先set缓存
        try {
            ShardJedisTool.getInstance().set(JedisKey.pms$sku_props, proId, gson.toJson(properties));
            return true;
        } catch (JedisClientException e) {
            logger.error(e, "设置SKU固有属性缓存失败,proId:" + proId, "");
            return false;
        }
    }

    @Override
    public boolean setSkuCacheByProId(int proId) {
        ProProduct product = proProductDao.getWholeProductById(proId);
        //先更新缓存
        Gson gson = new Gson();
        try {
            ShardJedisTool.getInstance().set(JedisKey.pms$sku, proId, gson.toJson(product));
            return true;
        } catch (JedisClientException e) {
            logger.error(e, "设置SKU缓存信息失败,proId:" + proId, "");
            return false;
        }
    }

    @Override
    public boolean setSpuSelfPropCacheBySpuId(int spuId) {
        //获取商品固有属性
        ProSelfProperty property = new ProSelfProperty();
        property.setSpuId(spuId);
        List<ProSelfProperty> properties = proSelfPropertyDao.getListByObj(property);
        Gson gson = new Gson();
        //先set缓存
        try {
            ShardJedisTool.getInstance().set(JedisKey.pms$spu_props, spuId, gson.toJson(properties));
            return true;
        } catch (JedisClientException e) {
            logger.error(e, "设置SPU固有属性缓存失败,spuId:" + spuId, "");
            return false;
        }
    }

    @Override
    public List<SkuSelfProperty> getSkuSelfPropsCacheByProId(int proId) {
        String valus = null;
        try {
            valus = ShardJedisTool.getInstance().get(JedisKey.pms$sku_props, proId);
        } catch (JedisClientException e) {
            logger.error(e, "获取SKU固有属性缓存信息失败,spuId:" + proId, "");
        }
        if (StringUtils.isNotEmpty(valus)) {
            return new Gson().fromJson(valus, new TypeToken<List<SkuSelfProperty>>() {
            }.getType());
        } else {
            //获取商品固有属性
            SkuSelfProperty property = new SkuSelfProperty();
            property.setProId(proId);
            List<SkuSelfProperty> properties = skuSelfPropertyDao.getListByObj(property);
            Gson gson = new Gson();
            //先set缓存
            try {
                ShardJedisTool.getInstance().set(JedisKey.pms$sku_props, proId, gson.toJson(properties));
            } catch (JedisClientException e) {
                logger.error(e, "获取SKU固有属性缓存信息失败,spuId:" + proId, "");
            }
            return properties;
        }
    }

    @Override
    public ProProduct getSkuCacheByProId(int proId) {
        String valus = null;
        try {
            valus = ShardJedisTool.getInstance().get(JedisKey.pms$sku, proId);
        } catch (JedisClientException e) {
            logger.error(e, "获取SKU缓存信息失败,proId:" + proId, "");
        }
        if (StringUtils.isNotEmpty(valus)) {
            return new Gson().fromJson(valus, ProProduct.class);
        } else {
            ProProduct product = proProductDao.getWholeProductById(proId);
            //先更新缓存
            Gson gson = new Gson();
            try {
                ShardJedisTool.getInstance().set(JedisKey.pms$sku, proId, gson.toJson(product));
            } catch (JedisClientException e) {
                logger.error(e, "设置SKU缓存信息失败,proId:" + proId, "");
            }
            return product;
        }
    }

    @Override
    public List<ProSelfProperty> getSpuSelfPropCacheBySpuId(int spuId) {
        String valus = null;
        try {
            valus = ShardJedisTool.getInstance().get(JedisKey.pms$spu_props, spuId);
        } catch (JedisClientException e) {
            logger.error(e, "获取SPU固有属性缓存信息失败,spuId:" + spuId, "");
        }
        if (StringUtils.isNotEmpty(valus)) {
            return new Gson().fromJson(valus, new TypeToken<List<ProSelfProperty>>() {
            }.getType());
        } else {
            //获取商品固有属性
            ProSelfProperty property = new ProSelfProperty();
            property.setSpuId(spuId);
            List<ProSelfProperty> properties = proSelfPropertyDao.getListByObj(property);
            //先set缓存
            Gson gson = new Gson();
            try {
                ShardJedisTool.getInstance().set(JedisKey.pms$spu_props, spuId, gson.toJson(properties));
            } catch (JedisClientException e) {
                logger.error(e, "获取SPU固有属性缓存信息失败,spuId:" + spuId, "");
            }
            return properties;
        }
    }

    @Override
    public List<ProProperty> getSpuWithSkuPropsCacheById(int spuId) {
        String valus = null;
        try {
            valus = ShardJedisTool.getInstance().get(JedisKey.pms$props, spuId);
        } catch (JedisClientException e) {
            logger.error(e, "从缓存获取SKU对应的类目属性的sku属性失败,spuId:" + spuId, "");
        }
        if (StringUtils.isNotEmpty(valus)) {
            return new Gson().fromJson(valus, new TypeToken<List<ProProperty>>() {
            }.getType());
        } else {
            List<ProProperty> pps = new ArrayList<ProProperty>();
            if (spuId > 0) {
                ProProductProperty ppp = new ProProductProperty();
                ppp.setSpuId(spuId);
                ppp.setIsSku(Constant.IS_SKU_YES);
                List<ProProductProperty> productProperties = proProductPropertyDao.getListByObj(ppp);
                if (productProperties != null && productProperties.size() > 0) {
                    for (ProProductProperty property : productProperties) {
                        ProProperty pp = propertyDao.getEntityById(property.getProPropId());
                        if (StringUtils.isNotEmpty(property.getValueId())) {
                            String[] ids = property.getValueId().split(",");
                            List<ProPropertyValue> ppv = propertyValueDao.getValuesByIds(ids);
                            pp.setProProValue(ppv);
                        }
                        //标记那个属性是异图，也就是能够在商品详情页显示主图部分
                        if (property.getIsImage() == 1) {
                            pp.setIsImage(1);
                        }
                        pps.add(pp);
                    }
                }
            }
            try {
                if (pps != null && pps.size() > 0) {
                    Gson gson = new Gson();
                    ShardJedisTool.getInstance().set(JedisKey.pms$props, spuId, gson.toJson(pps));
                }
            } catch (JedisClientException e) {
                logger.error(e, "更新缓存SKU对应的类目属性的sku属性失败,spuId:" + spuId, "");
            }
            return pps;
        }
    }

    @Override
    public boolean setSpuWithSkuPropsCacheById(int spuId) {
        List<ProProperty> pps = new ArrayList<ProProperty>();
        if (spuId > 0) {
            ProProductProperty ppp = new ProProductProperty();
            ppp.setSpuId(spuId);
            ppp.setIsSku(Constant.IS_SKU_YES);
            List<ProProductProperty> productProperties = proProductPropertyDao.getListByObj(ppp);
            if (productProperties != null && productProperties.size() > 0) {
                for (ProProductProperty property : productProperties) {
                    ProProperty pp = propertyDao.getEntityById(property.getProPropId());
                    if (StringUtils.isNotEmpty(property.getValueId())) {
                        String[] ids = property.getValueId().split(",");
                        List<ProPropertyValue> ppv = propertyValueDao.getValuesByIds(ids);
                        pp.setProProValue(ppv);
                    }
                    //标记那个属性是异图，也就是能够在商品详情页显示主图部分
                    if (property.getIsImage() == 1) {
                        pp.setIsImage(1);
                    }
                    pps.add(pp);
                }
            }
        }
        try {
            if (pps != null && pps.size() > 0) {
                Gson gson = new Gson();
                ShardJedisTool.getInstance().set(JedisKey.pms$props, spuId, gson.toJson(pps));
            }
            return true;
        } catch (JedisClientException e) {
            logger.error(e, "更新缓存SKU对应的类目属性的sku属性失败,spuId:" + spuId, "");
            return false;
        }
    }

}

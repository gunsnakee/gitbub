package com.meiliwan.emall.service.pms.cache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.rabbitmq.rcvmsgcenter.MsgTaskWorker;
import com.meiliwan.emall.pms.bean.SimpleProduct;
import com.meiliwan.emall.pms.client.ProProductClient;

import java.util.*;

/**
 * User: wuzixin
 * Date: 14-3-10
 * Time: 下午4:11
 */
public class ProductCacheMsgTaskWorker implements MsgTaskWorker {
    private final static MLWLogger LOG = MLWLoggerFactory
            .getLogger(ProductCacheMsgTaskWorker.class);

    @Override
    public void handleMsg(String msg) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Map<String, Object> map = gson.fromJson(msg, new TypeToken<Map<String, Object>>() {
        }.getType());
        System.out.println("更新缓存"+map.toString());
        //判断是否是修改spu，还是修改sku
        int spuId = Double.valueOf(map.get("spuId").toString()).intValue();
        if (spuId > 0) {
            List<SimpleProduct> products = ProProductClient.getListProBySpuId(spuId);
            if (products != null && products.size() > 0) {
                for (SimpleProduct product : products) {
                    if (product.getState() != 0) {
                        //更新商品详情页缓存
                        try {
                            ProProductClient.addAllProDetailToCache(product.getProId());
                        } catch (Exception e) {
                            LOG.error(e, "修改商品信息，更新SKU商品详情页缓存失败，proId=" + product.getProId(), "");
                        }
                    }
                }
            }
            try {
                ProProductClient.addSpuStandardInfoToCache(spuId);
            } catch (Exception e) {
                LOG.error(e, "修改商品信息，更新SKU商品详情页缓存失败，spuId=" + spuId, "");
            }
        } else {
            int cacheState = Double.valueOf(map.get("cacheState").toString()).intValue();
            if (cacheState > 0) {
                List<Double> ids = (List<Double>) map.get("ids");
                if (ids != null && ids.size() > 0) {
                    int[] proIds = new int[ids.size()];
                    for (int i = 0; i < ids.size(); i++) {
                        proIds[i] = ids.get(i).intValue();
                    }
                    List<SimpleProduct> list = ProProductClient.getSimpleListByProIds(proIds);
                    Set<Integer> spuIds = new HashSet<Integer>();
                    if (list != null && list.size() > 0) {
                        for (SimpleProduct product : list) {
                            if (product.getState() != 0) {
                                try {
                                    ProProductClient.addAllProDetailToCache(product.getProId());
                                } catch (Exception e) {
                                    LOG.error(e, "修改商品信息，更新SKU商品详情页缓存失败，proId=" + product.getProId(), "");
                                }
                                spuIds.add(product.getSpuId());
                            }
                        }
                    }
                    if (spuIds!=null&&spuIds.size()>0){
                        Iterator<Integer> iterator = spuIds.iterator();
                        while (iterator.hasNext()){
                            try {
                                ProProductClient.addSpuStandardInfoToCache(iterator.next());
                            } catch (Exception e) {
                                LOG.error(e, "修改商品信息，更新SKU商品详情页缓存失败，spuId=" + spuId, "");
                            }
                        }
                    }
                }
            }
        }
    }
}

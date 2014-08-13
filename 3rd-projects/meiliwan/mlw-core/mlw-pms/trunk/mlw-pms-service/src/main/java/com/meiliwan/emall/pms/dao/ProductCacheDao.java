package com.meiliwan.emall.pms.dao;

import com.meiliwan.emall.pms.bean.ProProduct;
import com.meiliwan.emall.pms.bean.ProProperty;
import com.meiliwan.emall.pms.bean.ProSelfProperty;
import com.meiliwan.emall.pms.bean.SkuSelfProperty;

import java.util.List;

/**
 * User: wuzixin
 * Date: 14-3-7
 * Time: 下午2:20
 */
public interface ProductCacheDao {

    /**
     * 获取对应sku固有属性，然后缓存到缓存里面
     *
     * @param proId
     * @return
     */
    boolean setSkuSelfPropsCacheByProId(int proId);

    /**
     * 获取SKU信息,保存到缓存里面
     *
     * @param proId
     * @return
     */
    boolean setSkuCacheByProId(int proId);

    /**
     * 获取对应spu固有属性，然后缓存到缓存里面
     *
     * @param spuId
     * @return
     */
    boolean setSpuSelfPropCacheBySpuId(int spuId);

    /**
     * 获取SKU固有属性信息
     * 业务场景：如果缓存有，直接从缓存获取SKU固有属性信息；如果不存在，从数据库查找，然后再set到缓存
     *
     * @param proId
     * @return
     */
    List<SkuSelfProperty> getSkuSelfPropsCacheByProId(int proId);

    /**
     * 获取SKU信息
     * 业务场景：如果缓存有，直接从缓存获取SKU信息；如果不存在，从数据库查找，然后再set到缓存
     *
     * @param proId
     * @return
     */
    ProProduct getSkuCacheByProId(int proId);

    /**
     * 获取SPU固有属性详情信息
     * 业务场景：如果缓存有，直接从缓存获取SPU固有属性信息；如果不存在，从数据库查找，然后再set到缓存
     *
     * @param spuId
     * @return
     */
    List<ProSelfProperty> getSpuSelfPropCacheBySpuId(int spuId);

    /**
     * 根据SPU ID 获取spu对应的规格属性信息,只包含规格相关的属性信息
     *
     * @param spuId
     */
    List<ProProperty> getSpuWithSkuPropsCacheById(int spuId);

    /**
     * 更新SPU ID 对应spu对应的规格属性信息,只包含规格相关的属性信息
     * 更新缓存
     *
     * @param spuId
     */
    boolean setSpuWithSkuPropsCacheById(int spuId);
}

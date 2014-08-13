package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.RedisSearchInfoUtil;
import com.meiliwan.emall.core.cache.JedisDaoCache;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.ProProduct;
import com.meiliwan.emall.pms.bean.SimpleProduct;
import com.meiliwan.emall.pms.dao.ProProductDao;
import com.meiliwan.emall.pms.dto.ProductDTO;
import com.meiliwan.emall.pms.dto.ProductNamesDto;
import com.meiliwan.emall.pms.dto.ProductPublicParasDto;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProProductDaoImpl extends BaseDao<Integer, SimpleProduct> implements ProProductDao {
    @Override
    public String getMapperNameSpace() {
        return ProProductDao.class.getName();
    }

    /**
     *
     */
    @Override
    public void changeState(int[] ids, ProProduct pro) {
        // TODO Auto-generated method stub
        for (int id : ids) {
            deleteCacheByPk(id);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entity", pro);
        map.put("ids", ids);
        try {
            getSqlSession().update(getMapperNameSpace() + ".changeState", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".changeState: {},{}", ids.toString(), e);

        }

    }

    @Override
    public JedisKey getUseJedisCacheKey() {
        return JedisKey.pms$pro;
    }

    protected Map<String, Object> getMapParams(Object dto, PageInfo pageInfo, String whereSql, String orderBySql) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != dto) map.put("entity", dto);
        if (null != pageInfo) map.put("pageInfo", pageInfo);
        if (null != whereSql) map.put("whereSql", whereSql);
        if (null != orderBySql) map.put("orderBy", orderBySql);
        return map;
    }

    @Override
    public int getCountByObj(ProductDTO orderQuery, String whereSql) {
        try {
            Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + ".getListAllByEntityAndPageInfoTotal",
                    getShardParam(orderQuery != null ? orderQuery.getProId() : null, getMapParams(orderQuery, null, whereSql, null), false));
            return (Integer) selectOne;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getCountByObj: {},{}", new String[]{orderQuery == null ? "" : orderQuery.toString(), whereSql}, e);
        }
    }

    @Override
    public int getCountByObj(ProductDTO orderQuery, String whereSql, boolean isMain) {
        try {
            Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + ".getListAllByEntityAndPageInfoTotal",
                    getShardParam(orderQuery != null ? orderQuery.getProId() : null, getMapParams(orderQuery, null, whereSql, null), false));
            return (Integer) selectOne;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getCountByObj: {},{}", new String[]{orderQuery == null ? "" : orderQuery.toString(), whereSql}, e);
        }
    }

    @Override
    public ProProduct getWholeProductById(Integer proId) {
        try {
            ProProduct product = getSqlSession().selectOne(getMapperNameSpace() + ".getWholeProductById", getShardParam(proId, proId, false));
            return product;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getWholeProductById: {},{}", proId.toString(), e);
        }
    }

    @Override
    public int insertPro(ProProduct product) {
        try {
            return getSqlSession().insert(getMapperNameSpace() + ".insertPro",
                    getShardParam(product != null ? product.getId() : null, product, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".insertPro: {}", product == null ? "" : product.toString(), e);
        }
    }

    @Override
    public int updatePro(ProProduct product) {
        deleteCacheByPk(product.getProId());
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updatePro",
                    getShardParam(product != null ? product.getId() : null, product, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updatePro: {}", product == null ? "" : product.toString(), e);
        }
    }

    @Override
    public int updateProByPrice(Integer id, String param, BigDecimal price) {
        deleteCacheByPk(id);
        Map<String, Object> map = new HashMap<String, Object>();
        String updateTime = DateUtil.getCurrentDateTimeStr();
        map.put("id", id);
        map.put("param", param);
        map.put("price", price);
        map.put("updateTime", updateTime);

        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateProByPrice", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateProByPrice: {},{}", new String[]{id.toString(), param.toString()}, e);
        }
    }

    @Override
    public PagerControl<ProductDTO> getPagerByDTO(ProductDTO entity,
                                                  PageInfo pageInfo, Object object, String orderBySql) {
        PagerControl<ProductDTO> pagerControl = new PagerControl<ProductDTO>();
        pageInfo.startTime();
        List<ProductDTO> list = null;
        int total = 0;
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + ".getListAllByEntityAndPageInfo",
                    getShardParam(entity.getProId(), getMapParams(entity, pageInfo, null, orderBySql), false));
            total = getCountByObj(entity, null);
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getListAllByEntityAndPageInfo: {},{}", entity.toString(), e);
        }

        pageInfo.endTime();
        pageInfo.setTotalCounts(total);
        if (list != null) {
            pagerControl.setEntityList(list);
        }
        pagerControl.setPageInfo(pageInfo);
        return pagerControl;
    }

    @Override
    public List<SimpleProduct> getSimpleListByIds(int[] ids) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ids", ids);
        List<SimpleProduct> list = null;
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + ".getSimpleListByIds", getShardParam(null, map, false));
            return list;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getSimpleListByIds: {}", ids.toString(), e);
        }
    }

    @Override
    public List<SimpleProduct> getListByIdsAndName(int[] ids, String proName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ids", ids);
        map.put("proName", proName);
        List<SimpleProduct> list = null;
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + ".getListByIdsAndName", getShardParam(null, map, false));
            return list;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getListByIdsAndName: {}", ids.toString(), e);
        }
    }

    @Override
    public int updateSkuProductNamesBySpuId(ProductNamesDto dto) {
        try {
            List<SimpleProduct> list = getListBySpuId(dto.getSpuId());
            if (list != null && list.size() > 0) {
                for (SimpleProduct product : list) {
                    deleteCacheByPk(product.getId());
                    delSkuCacheByPk(product.getId());
                    RedisSearchInfoUtil.addSearchInfo(product.getProId());
                }
                dto.setUpdateTime(DateUtil.getCurrentTimestamp());
                return getSqlSession().update(getMapperNameSpace() + ".updateSukProductNamsBySpuId", getShardParam(null, dto, true));
            } else {
                return 0;
            }

        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateSukProductNamsBySpuId: {}", dto.toString());
        }

    }

    @Override
    public List<SimpleProduct> getListBySpuId(int spuId) {
        List<SimpleProduct> list = null;
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + ".getListBySpuId", getShardParam(null, spuId, false));
            return list;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getListBySpuId: {}", spuId + "", e);
        }
    }


    @Override
    public int updateSkuProductPublicParas(ProductPublicParasDto dto) {
        try {
            List<SimpleProduct> list = getListBySpuId(dto.getSpuId());
            if (list != null && list.size() > 0) {
                for (SimpleProduct product : list) {
                    deleteCacheByPk(product.getId());
                    delSkuCacheByPk(product.getId());
                    RedisSearchInfoUtil.addSearchInfo(product.getProId());
                }
                return getSqlSession().update(getMapperNameSpace() + ".updateSkuProductPublicParas", getShardParam(null, dto, true));
            } else {
                return 0;
            }
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateSkuProductPublicParas: {}", dto.toString(), e);
        }

    }

    @Override
    public int delSkuByIds(int[] ids) {
        Map<String, Object> map = new HashMap<String, Object>();
        String deleteTime = DateUtil.getCurrentDateTimeStr();
        map.put("ids", ids);
        map.put("deleteTime", deleteTime);
        try {
            int count = getSqlSession().update(getMapperNameSpace() + ".delSkuByIds", getShardParam(null, map, true));
            if (count > 0) {
                if (count > 0) {
                    for (Integer id : ids) {
                        deleteCacheByPk(id);
                        delSkuCacheByPk(id);
                    }
                }
                RedisSearchInfoUtil.addSearchInfo(ids);
            }
            return count;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".delSkuByIds: {},{}", ids.toString(), e);
        }
    }

    @Override
    public int updateStateToOffByIds(int[] ids) {
        Map<String, Object> map = new HashMap<String, Object>();
        String offTime = DateUtil.getCurrentDateTimeStr();
        map.put("ids", ids);
        map.put("offTime", offTime);
        try {
            int count = getSqlSession().update(getMapperNameSpace() + ".updateStateToOffByIds", getShardParam(null, map, true));
            if (count > 0) {
                for (Integer id : ids) {
                    deleteCacheByPk(id);
                    delSkuCacheByPk(id);
                }
            }
            return count;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateStateToOffByIds: {},{}", ids.toString(), e);
        }
    }

    @Override
    public int updateStateToONByIds(int flag, List<Integer> ids) {
        Map<String, Object> map = new HashMap<String, Object>();
        String onTime = DateUtil.getCurrentDateTimeStr();
        map.put("ids", ids);
        map.put("flag", flag);
        map.put("onTime", onTime);
        try {
            int count = getSqlSession().update(getMapperNameSpace() + ".updateStateToONByIds", getShardParam(null, map, true));
            if (count > 0) {
                for (Integer id : ids) {
                    deleteCacheByPk(id);
                    delSkuCacheByPk(id);
                }
            }
            return count;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateStateToONByIds: {},{}", ids.toString(), e);
        }
    }

    private void delSkuCacheByPk(int pk) {
        try {
            ShardJedisTool.getInstance().del(JedisKey.pms$sku, pk);
        } catch (JedisClientException e) {
        }
    }

    @Override
    public int updateImageBySpuAndPropVid(Map<String, Object> mapDto) {
        try {
            List<SimpleProduct> list = getListBySpuIdAndPropVid(mapDto);
            if (list != null && list.size() > 0) {
                for (SimpleProduct simpleProduct : list) {
                    deleteCacheByPk(simpleProduct.getId());
                    delSkuCacheByPk(simpleProduct.getId());
                    RedisSearchInfoUtil.addSearchInfo(simpleProduct.getProId());
                }
                mapDto.put("updateTime",DateUtil.getCurrentDateTimeStr());
                return getSqlSession().update(getMapperNameSpace() + ".updateImageBySpuAndPropVid", getShardParam(null, mapDto, true));
            } else {
                return 0;
            }

        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateImageBySpuAndPropVid: {},{}", mapDto.toString(), e);
        }
    }

    @Override
    public List<SimpleProduct> getListBySpuIdAndPropVid(Map<String, Object> mapDto) {
        try {
            return getSqlSession().selectList(getMapperNameSpace() + ".getListBySpuIdAndPropVid", getShardParam(null, mapDto, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getListBySpuIdAndPropVid: {},{}", mapDto.toString(), e);
        }
    }

    @Override
    public int updateBrandIdToOne(int newId, int oldId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("oldId", oldId);
        map.put("newId", newId);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateBrandIdToOne", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateBrandIdToOne: {},{}", "", e);
        }
    }

    @Override
    public List<ProProduct> getListByUpdateTime(String min, String max) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("updateTimeMin", min);
        map.put("updateTimeMax", max);
        try {
            return getSqlSession().selectList(getMapperNameSpace() + ".getListByUpdateTime", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getListByUpdateTime: {},{}", map.toString(), e);
        }
    }

    @Override
    public List<ProProduct> getProductToOmsBySpuId(int spuId) {
        try {
            return getSqlSession().selectList(getMapperNameSpace() + ".getProductToOmsBySpuId", getShardParam(null, spuId, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getProductToOmsBySpuId: {},{}", spuId + "", e);
        }
    }

    @Override
    public int updatePresaleTime(int proId, String endTime, String sendTime) {
        Map<String, Object> map = new HashMap<String, Object>();
        String updateTime = DateUtil.getCurrentDateTimeStr();
        map.put("proId", proId);
        map.put("presaleEndTime", endTime);
        map.put("presaleSendTime", sendTime);
        map.put("updateTime", updateTime);
        try {
            int count = getSqlSession().update(getMapperNameSpace() + ".updatePresaleTime", getShardParam(null, map, true));
            if (count > 0) {
                deleteCacheByPk(proId);
                delSkuCacheByPk(proId);
            }
            return count;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updatePresaleTime: {},{}", map.toString(), e);
        }
    }


}
package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.ProSpu;
import com.meiliwan.emall.pms.dao.ProSpuDao;
import com.meiliwan.emall.pms.dto.ProductNamesDto;
import com.meiliwan.emall.pms.dto.ProductPublicParasDto;
import com.meiliwan.emall.pms.dto.SpuListDTO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: wuzixin
 * Date: 14-2-20
 * Time: 下午5:26
 */
@Repository
public class ProSpuDaoImpl extends BaseDao<Integer,ProSpu> implements ProSpuDao{
    @Override
    public String getMapperNameSpace() {
        return ProSpuDao.class.getName();
    }
    @Override
    public JedisKey getUseJedisCacheKey() {
        return JedisKey.pms$spu;
    }

    @Override
    public int getSpuListTotal(SpuListDTO dto, String whereSql, boolean isMain) {
        try {
            Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + ".getSpuListTotal",
                    getShardParam(dto != null ? dto.getSpuId() : null, getMapParams(dto, null, whereSql, null), false));
            return (Integer) selectOne;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getSpuListTotal: {},{}", new String[]{dto == null ? "" : dto.toString(), whereSql}, e);
        }
    }

    @Override
    public PagerControl<SpuListDTO> getListSpuPage(SpuListDTO entity, PageInfo pageInfo, Object object, String orderBySql) {
        PagerControl<SpuListDTO> pagerControl = new PagerControl<SpuListDTO>();
        List<SpuListDTO> list = null;
        int total = 0;
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + ".getListSpuPage",
                    getShardParam(entity.getSpuId(), getMapParams(entity, pageInfo, null, orderBySql), false));
            total = getSpuListTotal(entity,null,false);
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getListSpuPage: {},{}", entity.toString(), e);
        }
        pageInfo.setTotalCounts(total);
        if (list != null) {
            pagerControl.setEntityList(list);
        }
        pagerControl.setPageInfo(pageInfo);
        return pagerControl;
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
    public int updateSpuProductNames(ProductNamesDto dto) {
        try {
            deleteCacheByPk(dto.getSpuId());
            return getSqlSession().update(getMapperNameSpace()+".updateSpuProductNames",getShardParam(null,dto,true));
        }catch (Exception e){
            throw  new ServiceException("service-"+getMapperNameSpace()+".updateSpuProductNames:{}",dto.toString(),e);
        }

    }

    @Override
    public int updateSpuProductPublicParas(ProductPublicParasDto dto) {
        try {
            deleteCacheByPk(dto.getSpuId());
            return getSqlSession().update(getMapperNameSpace()+".updateSpuProductPublicParas",getShardParam(null,dto,true));
        }catch (Exception e){
            throw  new ServiceException("service-"+getMapperNameSpace()+".updateSpuProductPublicParas:{}",dto.toString(),e);
        }
    }

    @Override
    public int updatePropStr(int spuId, String propStr) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("spuId", spuId);
        map.put("propStr", propStr);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updatePropStr", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateProByPrice: {},{}", new String[]{String.valueOf(spuId), propStr}, e);
        }
    }
}

package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.ProDetail;
import com.meiliwan.emall.pms.dao.ProDetailDao;
import com.meiliwan.emall.pms.dto.ProductDetailDTO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ProDetailDaoImpl extends BaseDao<Integer,ProDetail> implements ProDetailDao {

    @Override
    public String getMapperNameSpace() {
        return ProDetailDao.class.getName();
    }

    @Override
    public JedisKey getUseJedisCacheKey() {
        return JedisKey.pms$prodetail;
    }

    @Override
    public int updateProductDetailFromDto(ProductDetailDTO detailDTO) {
        try {
            deleteCacheByPk(detailDTO.getSpuId());
            return getSqlSession().update(getMapperNameSpace() + ".updateProductDetailFromDto", getShardParam(null, detailDTO, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateProductDetailFromDto: {}", detailDTO.toString(), e);
        }
    }

    @Override
    public int updateEditorRec(ProductDetailDTO detailDTO) {
        try {
            deleteCacheByPk(detailDTO.getSpuId());
            return getSqlSession().update(getMapperNameSpace() + ".updateEditorRec", getShardParam(null, detailDTO, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateEditorRec: {}", detailDTO.toString(), e);
        }
    }


}

package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.ProImages;
import com.meiliwan.emall.pms.bean.ProImagesKey;
import com.meiliwan.emall.pms.dao.ProImagesDao;
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
public class ProImagesDaoImpl extends BaseDao<ProImagesKey,ProImages> implements ProImagesDao{
    @Override
    public String getMapperNameSpace() {
        return ProImagesDao.class.getName();
    }

    @Override
    public int insertByBatch(List<ProImages> imageses) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", imageses);
        try {
            return getSqlSession().insert(getMapperNameSpace() + ".insertByBatch",
                    getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".insertByBatch: {}", imageses.toString(), e);
        }
    }

    @Override
    public int updateDefaultImageUriByPkIds(Map<String, Object> dto) {
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateDefaultImageUriByPkIds",
                    getShardParam(null, dto, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateDefaultImageUriByPkIds: {}", dto.toString(), e);
        }
    }
}

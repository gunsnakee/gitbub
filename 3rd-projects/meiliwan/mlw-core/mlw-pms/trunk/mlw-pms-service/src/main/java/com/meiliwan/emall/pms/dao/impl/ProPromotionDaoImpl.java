package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.ProPromotion;
import com.meiliwan.emall.pms.dao.ProPromotionDao;
import org.springframework.stereotype.Repository;

/**
 * User: guangdetang
 * Date: 13-6-18
 * Time: 下午3:37
 */
@Repository
public class ProPromotionDaoImpl extends BaseDao<Integer, ProPromotion> implements ProPromotionDao {
    @Override
    public String getMapperNameSpace() {
        return ProPromotionDao.class.getName();
    }
}

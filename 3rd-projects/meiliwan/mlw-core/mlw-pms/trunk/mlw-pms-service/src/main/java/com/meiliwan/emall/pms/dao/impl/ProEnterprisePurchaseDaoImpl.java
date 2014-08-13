package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.ProEnterprisePurchase;
import com.meiliwan.emall.pms.dao.ProEnterprisePurchaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created by yiyou.luo on 13-6-18.
 */

@Repository
public class ProEnterprisePurchaseDaoImpl  extends BaseDao<Integer,ProEnterprisePurchase>
        implements ProEnterprisePurchaseDao {
    @Override
    public String getMapperNameSpace() {
        return ProEnterprisePurchaseDao.class.getName();
    }
}

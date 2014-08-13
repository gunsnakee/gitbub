package com.meiliwan.emall.base.dao.impl;

import com.meiliwan.emall.base.bean.BaseMsgTemplate;
import com.meiliwan.emall.base.dao.BaseMsgTemplateDao;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * User: wuzixin
 * Date: 14-5-28
 * Time: 下午5:27
 */
@Repository
public class BaseMsgTemplateDaoImpl extends BaseDao<Integer, BaseMsgTemplate> implements BaseMsgTemplateDao {
    @Override
    public String getMapperNameSpace() {
        return BaseMsgTemplateDao.class.getName();
    }
}

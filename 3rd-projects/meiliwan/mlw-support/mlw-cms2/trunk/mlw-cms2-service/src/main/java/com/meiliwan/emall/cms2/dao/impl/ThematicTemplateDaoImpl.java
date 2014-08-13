package com.meiliwan.emall.cms2.dao.impl;

import com.meiliwan.emall.cms2.bean.ThematicTemplate;
import com.meiliwan.emall.cms2.dao.ThematicTemplateDao;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 14-4-9
 * Time: 下午4:05
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ThematicTemplateDaoImpl extends BaseDao<Integer,ThematicTemplate> implements ThematicTemplateDao {
    @Override
    public String getMapperNameSpace() {
        return ThematicTemplateDao.class.getName();
    }
}

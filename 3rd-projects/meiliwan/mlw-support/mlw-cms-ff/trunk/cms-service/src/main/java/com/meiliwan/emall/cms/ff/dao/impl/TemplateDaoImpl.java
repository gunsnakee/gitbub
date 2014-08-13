package com.meiliwan.emall.cms.ff.dao.impl;

import com.meiliwan.emall.cms.ff.bean.Template;
import com.meiliwan.emall.cms.ff.dao.TemplateDao;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-12-18
 * Time: 下午6:31
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class TemplateDaoImpl extends BaseDao<Integer,Template> implements TemplateDao {
    @Override
    public String getMapperNameSpace() {
        return TemplateDao.class.getName();
    }
}

package com.meiliwan.emall.base.dao.impl;

import com.meiliwan.emall.base.bean.BaseHelpPages;
import com.meiliwan.emall.base.dao.BaseHelpPagesDao;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-12-6
 * Time: 上午11:09
 */
@Component
public class BaseHelpPagesDaoImpl extends BaseDao<Integer,BaseHelpPages> implements BaseHelpPagesDao {
    @Override
    public String getMapperNameSpace() {
        return BaseHelpPagesDao.class.getName();
    }

    @Override
    public JedisKey getUseJedisCacheKey() {
        return JedisKey.hc$cache$key;
    }
}

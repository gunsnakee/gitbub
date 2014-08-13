package com.meiliwan.emall.sp2.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.sp2.bean.ExamTopic;
import com.meiliwan.emall.sp2.dao.ExamTopicDao;
import org.springframework.stereotype.Repository;

/**
 * User: wuzixin
 * Date: 14-4-24
 * Time: 下午5:08
 */
@Repository
public class ExamTopicDaoImpl extends BaseDao<Integer, ExamTopic> implements ExamTopicDao {
    @Override
    public String getMapperNameSpace() {
        return ExamTopicDao.class.getName();
    }

    @Override
    public ExamTopic getTopicByRand() {
        try {
            ExamTopic topic = getSqlSession().selectOne(getMapperNameSpace() + ".getTopicByRand", getShardParam(null, null, false));
            return topic;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getTopicByRand: {},{}", "", e);
        }
    }
}

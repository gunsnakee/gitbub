package com.meiliwan.emall.sp2.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.sp2.bean.ExamTopic;

public interface ExamTopicDao extends IDao<Integer,ExamTopic>{
    /**
     * 随机获取一道题目
     */
    ExamTopic getTopicByRand();
}
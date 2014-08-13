package com.meiliwan.emall.sp2.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.sp2.bean.ExamResult;

public interface ExamResultDao extends IDao<Integer,ExamResult>{

    /**
     * 给对应用户礼包等级数加1
     * @param uid
     */
    int updateLevelToAddOne(int uid);

    int updateLevelToZero();
}
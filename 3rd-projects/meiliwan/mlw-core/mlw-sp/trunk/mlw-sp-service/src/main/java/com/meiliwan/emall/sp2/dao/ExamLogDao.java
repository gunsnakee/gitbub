package com.meiliwan.emall.sp2.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.sp2.bean.ExamLog;

public interface ExamLogDao extends IDao<Integer,ExamLog>{

    /**
     * 根据用户ID和当天时间 查询用户当天答题的机会次数
     * @param uid
     * @return
     */
    int getCountAnswerGroupNum(int uid);

    /**
     * 根据组ID 修改对应组数答题的数目
     * @param groupId
     */
    int updateCountAddOne(int groupId);

    /**
     * 修改对应题目组的状态
     * @param groupId
     * @param uid
     * @return
     */
    int updateGroupState(int groupId, int uid);
}
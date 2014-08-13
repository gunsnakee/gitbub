package com.meiliwan.emall.mms.dao;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.mms.bean.UserIntegralDetail;
import com.meiliwan.emall.mms.dto.UserIntegralDetailDto;

/**
 * 用户积分明细 dao接口
 * @author Administrator
 *2013-07-03
 */
public interface UserIntegralDetailDao  extends IDao<Integer, UserIntegralDetail> {
     PagerControl<UserIntegralDetail> getPagerByIntegralDetailDto(UserIntegralDetailDto integralDetailDto, PageInfo pageInfo);
}
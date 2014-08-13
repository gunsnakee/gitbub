package com.meiliwan.emall.mms.service;


import com.google.gson.JsonObject;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.mms.bean.UserIntegralDetail;
import com.meiliwan.emall.mms.dao.UserIntegralDetailDao;
import com.meiliwan.emall.mms.dto.UserIntegralDetailDto;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * 用户积分明细 service
 *
 * @author yiyou.luo
 *         2013-07-03
 */
@Service
public class UserIntegralDetailService extends DefaultBaseServiceImpl {
    @Autowired
    private UserIntegralDetailDao userIntegralDetailDao;

  /*  *//**
     * 通过id获实例
     *
     * @param resultObj
     * @param id
     *//*
    @IceServiceMethod
    public void getUserIntegralDetailById(JsonObject resultObj, String id) {
        if (id != null && id.matches("^[0-9]*$")) { //过滤空和非数字
            addToResult(userIntegralDetailDao.getEntityById(Integer.parseInt(id)), resultObj);
        }
    }

   *//**
     * 通过用户id获实例
     *
     * @param resultObj
     * @param uid
     *//*
    @IceServiceMethod
    public void getUserIntegralDetailByUid(JsonObject resultObj, String uid) {
        if (uid != null && uid.matches("^[0-9]*$")) { //过滤空和非数字
            UserIntegralDetail userIntegralDetail = new UserIntegralDetail();
            userIntegralDetail.setUid(Integer.parseInt(uid));
            addToResult(userIntegralDetailDao.getListByObj(userIntegralDetail, ""), resultObj);
        }
    }

    *//**
     * 添加用户积分明细
     *
     * @param userIntegralDetail
     * @param resultObj
     *//*
    @IceServiceMethod
    public void saveUserIntegralDetail(JsonObject resultObj, UserIntegralDetail userIntegralDetail) {
        if (userIntegralDetail != null) {
            userIntegralDetail.setCreateTime(new Date());
            userIntegralDetailDao.insert(userIntegralDetail);
            addToResult(userIntegralDetail.getId(), resultObj);
        }
    }

    *//**
     * 修改用户积分明细
     *
     * @param userIntegralDetail
     * @param resultObj
     *//*
    @IceServiceMethod
    public void updateUserIntegral(JsonObject resultObj, UserIntegralDetail userIntegralDetail) {
        if (userIntegralDetail != null) {
            addToResult(userIntegralDetailDao.update(userIntegralDetail) > 0 ? true : false, resultObj);
        }  else{
            addToResult(false, resultObj);
        }
    }*/

    /**
     * 通过积分明细 实体参数获取对应的实体列表包含物理分页
     * @param resultObj
     * @param dto
     * @param pageInfo
     */
    @IceServiceMethod
    public void getUserIntegralDetailPaperByObj(JsonObject resultObj, UserIntegralDetailDto dto, PageInfo pageInfo) {
        if (dto != null && pageInfo != null) {
            pageInfo.setOrderField("create_time");
            pageInfo.setOrderDirection("desc");
            addToResult(userIntegralDetailDao.getPagerByIntegralDetailDto(dto, pageInfo), resultObj);
        }
    }

}

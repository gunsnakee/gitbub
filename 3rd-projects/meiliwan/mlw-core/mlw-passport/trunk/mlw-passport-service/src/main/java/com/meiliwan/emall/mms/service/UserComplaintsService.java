package com.meiliwan.emall.mms.service;

//import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.mms.bean.UserComplaints;
import com.meiliwan.emall.mms.dao.UserComplaintsDao;
import com.meiliwan.emall.service.BaseService;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * User: guangdetang
 * Date: 13-6-15
 * Time: 下午5:21
 */
@Service
public class UserComplaintsService extends DefaultBaseServiceImpl implements BaseService {

    @Autowired
    private UserComplaintsDao complaintsDao;

    /**
     * 用户发起投诉
     * @param resultObj
     * @param complaints
     */
    @IceServiceMethod
    public void addComplaints(JsonObject resultObj, UserComplaints complaints) {
        if (complaints != null) {
            complaints.setCreateTime(DateUtil.getCurrentTimestamp());
            complaints.setState(GlobalNames.STATE_VALID);
            int result = complaintsDao.insert(complaints);
            addToResult(result > 0 ? true : false, resultObj);
        }
    }

    /**
     * 修改投诉
     * @param resultObj
     * @param complaints
     */
    @IceServiceMethod
    public void updateComplaints(JsonObject resultObj, UserComplaints complaints) {
        if (complaints != null) {
            complaints.setUpdateTime(DateUtil.getCurrentTimestamp());
            int result = complaintsDao.update(complaints);
            addToResult(result > 0 ? true : false, resultObj);
        }
    }

    /**
     * 修改投诉各种状态（适用于运营后台模块）
     * @param resultObj
     * @param ids
     * @param handle
     */
    @IceServiceMethod
    public void updateComplaintsState(JsonObject resultObj, int[] ids, int handle) {
        if (ids != null && handle > 0) {
            for (Integer i : ids) {
                UserComplaints complaints = new UserComplaints();
                complaints.setId(i);
                if (handle == 1 || handle == 2) {
                    complaints.setState((short) (handle - 2));
                } else if (handle == 3 || handle == 4) {
                    complaints.setIsAdminDel((short) (handle - 4));
                } else if (handle == 5 || handle == 6) {
                    complaints.setIsUserDel((short) (handle - 6));
                }
                complaints.setUpdateTime(DateUtil.getCurrentTimestamp());
                int result = complaintsDao.update(complaints);
                addToResult(result > 0 ? true : false, resultObj);
            }
        }
    }

    /**
     * 用户取消投诉（适用于前台模块）
     * @param resultObj
     * @param id
     */
    @IceServiceMethod
    public void updateCancelComplaints(JsonObject resultObj, int id) {
        if (id > 0) {
            UserComplaints complaints = new UserComplaints();
            complaints.setId(id);
            complaints.setState(GlobalNames.STATE_INVALID);
            complaints.setUpdateTime(DateUtil.getCurrentTimestamp());
            int result = complaintsDao.update(complaints);
            addToResult(result > 0 ? true : false, resultObj);
        }
    }

    /**
     * 管理员回复投诉
     * @param resultObj
     * @param id
     * @param replyContent
     */
    @IceServiceMethod
    public void updateReplyComplaints(JsonObject resultObj, int id, String replyContent) {
        if (id > 0 && !Strings.isNullOrEmpty(replyContent)) {
            UserComplaints complaints = new UserComplaints();
            complaints.setId(id);
            complaints.setReplyContent(replyContent);
            complaints.setUpdateTime(DateUtil.getCurrentTimestamp());
            complaints.setReplyTime(DateUtil.getCurrentTimestamp());
            complaints.setState((short) 1);
            int result = complaintsDao.update(complaints);
            addToResult(result > 0 ? true : false, resultObj);
        }
    }

    /**
     * 获得投诉详情
     * @param resultObj
     * @param id
     */
    @IceServiceMethod
    public void getComplaints(JsonObject resultObj, int id) {
        if (id > 0) {
            addToResult(complaintsDao.getEntityById(id), resultObj);
        }
    }

    /**
     * 通过对象获得投诉详情
     * @param resultObj
     * @param complaints
     */
    @IceServiceMethod
    public void getComplaintsByObj(JsonObject resultObj, UserComplaints complaints) {
        if (complaints != null) {
            addToResult(complaintsDao.getEntityByObj(complaints), resultObj);
        }
    }

    /**
     * 运营后台获得投诉分页列表
     * @param resultObj
     * @param complaints
     * @param pageInfo
     */
    @IceServiceMethod
    public void getComplaintsPagerByAdmin(JsonObject resultObj, UserComplaints complaints, PageInfo pageInfo) {
        if (complaints != null && pageInfo != null) {
            if (complaints.getNickName() != null && !Strings.isNullOrEmpty(complaints.getNickName().trim())) {
                complaints.setNickName("%" + complaints.getNickName() + "%");
            } else {
                complaints.setNickName(null);
            }
            if (complaints.getState() != null) {
                if (complaints.getState() == 0) {
                    complaints.setState((short) -1);
                } else if (complaints.getState() == 1) {
                    complaints.setState(null);
                    complaints.setReplyContent("0");
                } else if (complaints.getState() == 2) {
                    complaints.setState(null);
                    complaints.setReplyContent("1");
                }
            } else {
                complaints.setState(null);
            }
            addToResult(complaintsDao.getPagerByObj(complaints, pageInfo, null), resultObj);
        }
    }

    /**
     * 用户中心获得投诉分页列表
     * @param resultObj
     * @param complaints
     * @param pageInfo
     */

    public void getComplaintsPagerByUser(JsonObject resultObj, UserComplaints complaints, PageInfo pageInfo) {
        if (complaints != null && pageInfo != null) {
            complaints.setIsUserDel(GlobalNames.STATE_VALID);
            complaints.setIsAdminDel(GlobalNames.STATE_VALID);
            addToResult(complaintsDao.getPagerByObj(complaints, pageInfo, null,"order by create_time desc"), resultObj);
        }
    }
}

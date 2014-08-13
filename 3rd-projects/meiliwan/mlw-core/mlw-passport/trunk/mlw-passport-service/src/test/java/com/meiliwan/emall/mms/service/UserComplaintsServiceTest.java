package com.meiliwan.emall.mms.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.mms.BaseTest;
import com.meiliwan.emall.mms.bean.UserComplaints;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Created by guangdetang on 13-8-19.
 */
public class UserComplaintsServiceTest extends BaseTest {
    @Autowired
    private UserComplaintsService complaintsService;
    JsonObject resultObj = new JsonObject();

    @Test
    public void testAddComplaints() throws Exception {
        UserComplaints complaints = new UserComplaints();
        complaints.setIsAdminDel((short) 0);
        complaints.setIsUserDel((short) 0);
        complaints.setUpdateTime(DateUtil.getCurrentTimestamp());
        complaints.setReplyTime(DateUtil.getCurrentTimestamp());
        complaints.setContent("投诉内容");
        complaints.setAdminId(888);
        complaints.setAdminName("admin");
        complaints.setComplaintsType((short) 1);
        complaints.setContactInfo("15177940168");
        complaints.setNickName("nickName");
        complaints.setUid(12);
        complaints.setState((short) 0);
        complaints.setReplyContent("回复内容");
        complaints.setOrderId("1000000005");
        complaints.setProofImage("/////////////");
        complaintsService.addComplaints(resultObj, complaints);

    }

    @Test
    public void testUpdateComplaints() throws Exception {
        UserComplaints complaints = new UserComplaints();
        complaints.setId(1);
        complaints.setContent("修改投诉内容");
        complaintsService.updateComplaints(resultObj, complaints);
    }

    @Test
    public void testUpdateComplaintsState() throws Exception {
        int[] ids = {1};
        complaintsService.updateComplaintsState(resultObj, ids, 1);
    }

    @Test
    public void testUpdateCancelComplaints() throws Exception {
        complaintsService.updateCancelComplaints(resultObj, 2);
    }

    @Test
    public void testUpdateReplyComplaints() throws Exception {
        complaintsService.updateReplyComplaints(resultObj, 2, "回复投诉内容");
    }

    @Test
    public void testGetComplaints() throws Exception {
        complaintsService.getComplaints(resultObj, 2);
    }

    @Test
    public void testGetComplaintsByObj() throws Exception {
        UserComplaints complaints = new UserComplaints();
        complaints.setOrderId("1000000009");
        complaints.setUid(12);
        complaintsService.getComplaintsByObj(resultObj, complaints);
    }

    @Test
    public void testGetComplaintsPagerByAdmin() throws Exception {
        UserComplaints complaints = new UserComplaints();
        complaints.setId(1);
        complaints.setOrderId("1000000009");
        complaints.setNickName("nickName");
        complaints.setComplaintsType((short) 2);
        complaints.setState((short) 0);
        complaintsService.getComplaintsPagerByAdmin(resultObj, complaints, new PageInfo(1, 10));
    }

    @Test
    public void testGetComplaintsPagerByUser() throws Exception {
        UserComplaints complaints = new UserComplaints();
        complaints.setState((short) 0);
        complaintsService.getComplaintsPagerByUser(resultObj, complaints, new PageInfo(1, 10));
    }
}

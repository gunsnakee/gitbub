package com.meiliwan.emall.mms.client;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.mms.bean.UserComplaints;
import org.testng.annotations.Test;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: guangdetang
 * Date: 13-9-5
 * Time: 下午5:36
 * To change this template use File | Settings | File Templates.
 */
public class UserComplaintsClientTest {
    Random random = new Random();

    @Test(invocationCount = 1000, threadPoolSize = 50)
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
        UserComplaintsClient.addComplaints(complaints);

    }


    @Test(invocationCount = 1000, threadPoolSize = 50)
    public void testGetComplaints() throws Exception {
       UserComplaintsClient.getComplaints(random.nextInt(1000));
    }

    @Test(invocationCount = 1000, threadPoolSize = 50)
    public void testGetComplaintsByObj() throws Exception {
        UserComplaints complaints = new UserComplaints();
        complaints.setOrderId("1000000009");
        complaints.setUid(random.nextInt(1000));
        UserComplaintsClient.getComplaintsByObj(complaints);
    }

    @Test(invocationCount = 1000, threadPoolSize = 50)
    public void testGetComplaintsPagerByUser() throws Exception {
        UserComplaints complaints = new UserComplaints();
        complaints.setUid(random.nextInt(1000));
        complaints.setState((short) 0);
        UserComplaintsClient.getComplaintsPagerByUser(complaints,new PageInfo(1,15));
    }
}

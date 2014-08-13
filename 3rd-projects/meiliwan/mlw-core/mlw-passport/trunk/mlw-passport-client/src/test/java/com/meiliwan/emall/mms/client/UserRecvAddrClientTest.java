package com.meiliwan.emall.mms.client;

import com.meiliwan.emall.mms.bean.UserRecvAddr;
import org.testng.annotations.Test;

public class UserRecvAddrClientTest {

    @Test
    public void testFindById() {
        UserRecvAddr addr = UserRecvAddrClient.getUserAddressById(2);
        System.out.println(addr);
    }

    @Test(invocationCount = 5000, threadPoolSize = 5)
    public void getAddressesByUid(){
        UserRecvAddrClient.getUserAddressListByUserId(12313);
    }
}

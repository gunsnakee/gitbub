package com.meiliwan.emall.mms.filldata;

import com.meiliwan.emall.mms.bean.UserPassport;
import com.meiliwan.emall.mms.client.UserPassportClient;

/**
 * 灌用户数据
 * Created with IntelliJ IDEA.
 * User: jiawuwu
 * Date: 13-8-5
 * Time: 下午2:45
 * To change this template use File | Settings | File Templates.
 */
public class FillUserData {

    private FillUserData(){

    }

    public static void main(String[] args){
        UserPassport p = null;
        int initUid = 104856654;
        int end = 5000000+100000000;
        String email=null;
        for(int i= initUid;i<end;i++){
            p = new UserPassport();
            p.setUid(i);
            p.setUserName("test_" + i);
            email = "tiger_"+i+"@163.com";
            p.setNickName(email);
            p.setEmail(email);
            p.setMobile("13"+i);
            p.setPassword("e62a975bdf8236a9318ec77c5b0ae574");
            UserPassportClient.save(p);
        }
    }

}

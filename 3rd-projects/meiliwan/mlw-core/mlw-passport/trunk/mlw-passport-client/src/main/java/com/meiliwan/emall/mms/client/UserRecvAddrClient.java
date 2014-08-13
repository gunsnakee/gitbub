package com.meiliwan.emall.mms.client;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.mms.bean.UserRecvAddr;
import com.meiliwan.emall.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserRecvAddrClient {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static UserRecvAddr getUserAddressById(int id) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams("userRecvAddrService/getUserAddressById", id));
        UserRecvAddr addr = new Gson().fromJson(obj.get(BaseService.RESULT_OBJ).toString(), UserRecvAddr.class);
        return addr;
    }

    public static UserRecvAddr getUserDefaultAddressByUid(int uid) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams("userRecvAddrService/getUserDefaultAddressByUid", uid));
        UserRecvAddr addr = new Gson().fromJson(obj.get(BaseService.RESULT_OBJ).toString(), UserRecvAddr.class);
        return addr;
    }


    public static Integer addUserAddressByObj(UserRecvAddr userRecvAddr) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams("userRecvAddrService/addUserAddress", userRecvAddr));
        return obj.get("resultObj").getAsInt();
    }

    public static boolean editUserAddressByObj(UserRecvAddr userRecvAddr) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams("userRecvAddrService/updateUserAddress", userRecvAddr));
        return obj.get("resultObj").getAsBoolean();
    }

    public static boolean updateUserAddress(UserRecvAddr userRecvAddr) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams("userRecvAddrService/updateUserAddress", userRecvAddr));
        return obj.get("resultObj").getAsBoolean();
    }

    public static boolean settingDefaultAddressByUidAid(int userId, int defaultAId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams("userRecvAddrService/updateDefaultUserAddress", userId, defaultAId));
        return obj.get("resultObj").getAsBoolean();
    }

    public static boolean deleteUserAddressByObj(int userId, int addressId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams("userRecvAddrService/deleteUserAddress", userId, addressId));
        return obj.get("resultObj").getAsBoolean();
    }

    public static List<UserRecvAddr> getUserAddressListByUserId(int userId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams("userRecvAddrService/getUserAddressListByUserId", userId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<UserRecvAddr>>() {
        }.getType());
    }

    public static UserRecvAddr getValidRecvAddr(int userId, int addrId) {
        UserRecvAddr addr = new UserRecvAddr();
        addr.setUid(userId);
        addr.setRecvAddrId(addrId);
        addr.setIsDel((byte) 0);//0有效，-1删除
        return getRecvAddrByObj(addr);
    }

    public static UserRecvAddr getRecvAddrByObj(UserRecvAddr addr) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams("userRecvAddrService/getRecvAddrByObj", addr));
        return new Gson().fromJson(obj.get("resultObj"), UserRecvAddr.class);
    }

    public static List<UserRecvAddr> getCashierAddrListByUid(int userId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams("userRecvAddrService/getCashierAddrListByUid", userId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<UserRecvAddr>>() {
        }.getType());
    }

    public static void main(String[] args) {
        UserRecvAddr uraa = new UserRecvAddr();
        uraa.setUid(3);
        uraa.setRecvName("test");
        uraa.setProvince("广西");
        uraa.setProvinceCode("10045");
        uraa.setCity("南宁");
        uraa.setCityCode("1004501");

        uraa.setCounty("西乡塘区");
        uraa.setCountyCode("100450101");
        uraa.setDetailAddr("总部路1号");
        uraa.setTown("心虚街道");
        uraa.setTownCode("10045010101");
        uraa.setMobile("213213123");
        uraa.setPhone("2313");
        uraa.setEmail("dafdsa@qq.com");
        uraa.setZipCode("32131");
        uraa.setIsDefault((byte) 1);
        System.out.println("addresult============" + addUserAddressByObj(uraa));
    }

}

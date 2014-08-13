package com.meiliwan.emall.mms.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.web.UserLoginUtil;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.mms.bean.UserPassport;
import com.meiliwan.emall.mms.bean.UserPassportPara;
import com.meiliwan.emall.mms.bean.UserPassportSimple;
import com.meiliwan.emall.mms.exception.UserPassportException;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 用户passport模块client
 *
 * @author jiawu.wu
 */
public class UserPassportClient {


    //最大查询用户ID数
    public static final int MAX_UIDS = 100;


    private static final String SERVICE_NAME = "userPassportService";
    private static final MLWLogger LOGGER = MLWLoggerFactory.getLogger(UserPassportClient.class);



    private UserPassportClient(){

    }

    /**
     * 保存用户信息
     *
     * @param user
     * @return
     */
    public static boolean save(UserPassport user) {
        if(user==null){
            return false;
        }
        String iceFuncName = "add";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + "/" + iceFuncName, user));
        return new Gson().fromJson(obj.get("resultObj"), Boolean.class);
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    public static boolean update(UserPassport user) {
        if(user==null){
            return false;
        }
        String iceFuncName = "update";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + "/" + iceFuncName, user));
        return new Gson().fromJson(obj.get("resultObj"), Boolean.class);
    }

    /**
     * 批量更新用户信息
     *
     * @param users
     * @return
     */
    public static boolean updateBatch(UserPassport[] users) {
        if(users==null || users.length==0){
            return false;
        }
        String iceFuncName = "updateBatch";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildArrayParams(SERVICE_NAME + "/" + iceFuncName, users));
        return new Gson().fromJson(obj.get("resultObj"), Boolean.class);
    }

    /**
     * 软删除，把用户放进黑名单
     *
     * @param uid
     * @return
     */
    public static boolean softdel(Integer uid) {
        if(uid==null){
            return false;
        }
        String iceFuncName = "delete";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + "/" + iceFuncName, uid));
        return new Gson().fromJson(obj.get("resultObj"), Boolean.class);
    }

    /**
     * 根据多个UID来获取用户集合，返回list
     *
     * @param uids
     * @return
     */
    public static List<UserPassport> getPassportByUids(Integer[] uids) {
        if(uids==null || uids.length==0){
            return null;
        }
        String iceFuncName = "getPassportByUids";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildArrayParams(SERVICE_NAME + "/" + iceFuncName, uids));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<UserPassport>>() {
        }.getType());
    }
    
    /**
     * 
     * @param uid
     * @return 如果用户存在，并且手机已经激活，则返回true；否则返回false；
     * 
     */
    public static boolean isMobileActive(int uid){
    	UserPassport user = getPassportByUid(uid);
    	
    	return user != null && user.getMobileActive() == 1;
    }

    /**
     * 根据多个UID来获取用户集合,返回Map
     *
     * @param uids
     * @return
     */
    public static Map<Integer, UserPassport> getPassportMapByUids(Integer[] uids) {
        if(uids==null || uids.length==0){
            return null;
        }
        Map<Integer, UserPassport> resultMap = null;
        List<UserPassport> passportList = getPassportByUids(uids);
        if (passportList != null && passportList.size() > 0) {
            resultMap = new HashMap<Integer, UserPassport>();
            for (UserPassport passport : passportList) {
                resultMap.put(passport.getUid(), passport);
            }
        }
        return resultMap;
    }


    /**
     * 批量获取用户信息（字段仅限：用户昵称，用户名，邮箱，手机） 数量限制100
     *
     * @param uids
     * @return
     */
    public static List<UserPassportSimple> getPassportSimpleByUids(Integer[] uids) {
        if(uids==null || uids.length==0 || uids.length>MAX_UIDS){
            LOGGER.warn("批量获取用户信息时参数错误：用户ID数组不能为空或者长度超过"+MAX_UIDS+"。","批量获取用户信息时参数错误：用户ID数组不能为空或者长度超过100。{uids:"+ (uids==null?"null":Arrays.asList(uids))+"}",null);
            return null;
        }
        String iceFuncName = "getPassportSimpleByUids";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildArrayParams(SERVICE_NAME + "/" + iceFuncName, uids));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<UserPassportSimple>>() {
        }.getType());
    }

    /**
     * 批量获取用户信息（字段仅限：用户昵称，用户名，邮箱，手机） 数量限制100
     *
     * @param uids
     * @return
     */
    public static Map<Integer, UserPassportSimple> getPassportSimpleMapByUids(Integer[] uids) {
        Map<Integer, UserPassportSimple> resultMap = null;
        List<UserPassportSimple> passportList = getPassportSimpleByUids(uids);
        if (passportList != null && passportList.size() > 0) {
            resultMap = new HashMap<Integer, UserPassportSimple>();
            for (UserPassportSimple passport : passportList) {
                resultMap.put(passport.getUid(), passport);
            }
        }
        return resultMap;
    }



    /**
     * 从数据库获取passport ,建议不要直接调用该方法，该方法主要是为了做刷新缓存的
     *
     * @param uid
     * @return
     */
    public static UserPassport getPassportByUidFromDb(Integer uid) {
        if(uid==null){
            return null;
        }
        String iceFuncName = "getPassportByUid";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + "/" + iceFuncName, uid));
        return new Gson().fromJson(obj.get("resultObj"), UserPassport.class);
    }





    /**
     * 获取当前登陆用户uid，如果未登陆，返回null
     *
     * @param request
     * @param response
     * @return
     */
    public static Integer getLoginUid(HttpServletRequest request, HttpServletResponse response) {
        return UserLoginUtil.getLoginUid(request, response);
    }

    /**
     * 根据request,response获取当前登陆用户对象
     *
     * @param request
     * @param response
     * @return
     */
    public static UserPassport getPassport(HttpServletRequest request, HttpServletResponse response) {
        UserPassport passport = null;
        Integer uid = UserLoginUtil.getLoginUid(request, response);
        if (uid != null) {
            try {
                passport = getPassportByUidFromDb(uid);
            }catch (Exception e){
                throw new UserPassportException("获取当前登录用户数据时产生异常.",e);
            }
        }
        return passport;

    }

    /**
     * 获取用户passport，先从缓存中取，取不到则会从数据库拉数据刷新到缓存
     *
     * @param uid 用户ID
     * @return
     */
    public static UserPassport getPassportByUid(Integer uid) {
        return getPassportByUidFromDb(uid);

    }

    /**
     * 获取用户passport
     *
     * @param user_name 用户名
     * @return
     */
    public static UserPassport getPassportByUserName(String user_name) {
        if(StringUtils.isBlank(user_name)){
            return null;
        }
        String iceFuncName = "getPassportByUserName";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + "/" + iceFuncName, user_name));
        return new Gson().fromJson(obj.get("resultObj"), UserPassport.class);
    }

    /**
     * 从所有用户中获取passport
     *
     * @param nick_name 昵称
     * @return
     */
    public static UserPassport getPassportByNickName(String nick_name) {
        if(StringUtils.isBlank(nick_name)){
            return null;
        }
        String iceFuncName = "getPassportByNickName";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + "/" + iceFuncName, nick_name));
        return new Gson().fromJson(obj.get("resultObj"), UserPassport.class);
    }

    /**
     * 从所有用户中获取passport
     *
     * @param mobile 手机号码
     * @return
     */
    public static UserPassport getPassportByMobile(String mobile) {
        if(StringUtils.isBlank(mobile)){
            return null;
        }
        String iceFuncName = "getPassportByMobile";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + "/" + iceFuncName, mobile));
        return new Gson().fromJson(obj.get("resultObj"), UserPassport.class);
    }

    /**
     * 从所有用户中获取passport
     *
     * @param email 邮箱
     * @return
     */
    public static UserPassport getPassportByEmail(String email) {
        if(StringUtils.isBlank(email)){
            return null;
        }
        String iceFuncName = "getPassportByEmail";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + "/" + iceFuncName, email));
        return new Gson().fromJson(obj.get("resultObj"), UserPassport.class);
    }

    /**
     * 获取用户集合
     *
     * @param user     用户passport，查询参数
     * @param pageInfo 分页对象
     * @return
     */
    public static PagerControl<UserPassport> getPassportList(UserPassport user, PageInfo pageInfo) {
        String iceFuncName = "getPassportList";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + "/" + iceFuncName, user, pageInfo));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<UserPassport>>() {
        }.getType());
    }

    /**
     * 不能重复字段的安全更新 ,该更新方法使用   synchronized
     *
     * @param para
     * @return
     */
    public synchronized static HashMap<String, Object> updateKeepUnique(UserPassport para) {
        if(para==null){
            return null;
        }
        String iceFuncName = "updateKeepUnique";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + "/" + iceFuncName, para));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<HashMap<String, Object>>() {
        }.getType());
    }


    /**
     * 运营后台用户查询列表
     *
     * @param para
     * @param pageInfo
     * @return
     */
    public static PagerControl<UserPassportPara> getSearchList(UserPassportPara para, PageInfo pageInfo) {
        String iceFuncName = "getSearchList";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + "/" + iceFuncName, para, pageInfo));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<UserPassportPara>>() {
        }.getType());
    }

    /**
     * 获取序列Id
     * @return
     */
    public static String getSeqId(){
        String iceFuncName = "getSeqId";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + "/" + iceFuncName));
        return new Gson().fromJson(obj.get("resultObj"), String.class);
    }

    /**
     * 返回一个随机机器人用户
     * @return
     */
    public static UserPassport getRandomShamUser(){
        String iceFuncName = "getRandomShamUser";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + "/" + iceFuncName));
        return new Gson().fromJson(obj.get("resultObj"), UserPassport.class);
    }

}

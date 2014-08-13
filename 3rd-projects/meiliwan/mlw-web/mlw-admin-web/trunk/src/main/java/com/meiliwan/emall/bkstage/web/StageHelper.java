package com.meiliwan.emall.bkstage.web;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.meiliwan.emall.bkstage.web.controller.bkstage.view.LoginUser;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sean on 13-5-29.
 */
public class StageHelper {

    public static final String loginUserSessionName = "bkstage_user";

    public static LoginUser getLoginUser(HttpServletRequest request) {
        return (LoginUser) request.getSession().getAttribute(loginUserSessionName);
    }


    /**
     * Dwz 控制当前页面关闭   callbackType
     */
    public final static String DWZ_CLOSE_CURRENT = "closeCurrent";

    /**
     * Dwz 控制当前页面进行跳页   callbackType
     */
    public final static String DWZ_FORWARD = "forward";

    /**
     * { statusCode:${statusCode}, <br/>
     * message:"${message}", <br/>
     * navTabId:"${param.navTabId}", <br/>
     * callbackType:"${param.callbackType}",<br/>
     * forwardUrl:"${param.forwardUrl}${objectId}"<br/>
     * } <br/>
     * <br/>
     * { "statusCode":"状态码200或300", <br/>
     * "message":"提示信息", <br/>
     * "navTabId":"操作成功后需要指定navTab时使用", <br/>
     * "callbackType":"closeCurrent或forward",<br/>
     * "forwardUrl":"callbackType是forward时使用"<br/>
     * }
     */

    public static String writeDwzSignal(String statusCode, String message, String navTabId,
                                        String callbackType, String forwardUrl, HttpServletResponse response) {
        response.setHeader("Content-Language", "zh-cn");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write("{ \"statusCode\":\"" + statusCode + "\"," + "\"message\":\"" + message + "\","
                    + "\"navTabId\":\"" + navTabId + "\"," + "\"callbackType\":\"" + callbackType
                    + "\"," + "\"forwardUrl\":\"" + forwardUrl + "\" }");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String writeString(String string, HttpServletResponse response) {
        response.setHeader("Content-Language", "zh-cn");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String writeJson(HttpServletResponse response, Object obj) {
        response.setHeader("Content-Language", "zh-cn");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(new Gson().toJson(obj));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dwzSuccessClose(String message, String navTabId,
                                         String forwardUrl, HttpServletResponse response) {
        return StageHelper.writeDwzSignal("200", message, navTabId, DWZ_CLOSE_CURRENT, forwardUrl, response);
    }
    public static String dwzSuccessClose(String message, 	String forwardUrl, HttpServletResponse response) {
    	return StageHelper.writeDwzSignal("200", message, "", DWZ_CLOSE_CURRENT, forwardUrl, response);
    }

    public static String dwzFailClose(String message, String navTabId,
                                      String forwardUrl, HttpServletResponse response) {
        return StageHelper.writeDwzSignal("300", message, navTabId, DWZ_CLOSE_CURRENT, forwardUrl, response);
    }

    public static String dwzFailClose(String message, HttpServletResponse response) {
        return StageHelper.writeDwzSignal("300", message, "", DWZ_CLOSE_CURRENT, "", response);
    }

    public static String dwzSuccessForward(String message, String navTabId,
                                           String forwardUrl, HttpServletResponse response) {
        return StageHelper.writeDwzSignal("200", message, navTabId, DWZ_FORWARD, forwardUrl, response);
    }

    public static String dwzFailForward(String message, String navTabId,
                                        String forwardUrl, HttpServletResponse response) {
        return StageHelper.writeDwzSignal("300", message, navTabId, DWZ_FORWARD, forwardUrl, response);
    }

    /**
     * 获取分页信息
     *
     * @param request
     * @return
     */
    public static PageInfo getPageInfo(HttpServletRequest request) {
        return getPageInfo(request, null, null);
    }

    /**
     * 获取分页信息  以及定义默认排序字段以及排序规则
     *
     * @param request
     * @return
     */
    public static PageInfo getPageInfo(HttpServletRequest request, String defaultOrderField, String defaultOrderDirection) {
        return getPageInfo(request, defaultOrderField, defaultOrderDirection, 20);
    }

    /**
     * 获取分页信息  以及定义默认排序字段以及排序规则
     *
     * @param request
     * @return
     */
    public static PageInfo getPageInfo(HttpServletRequest request, String defaultOrderField, String defaultOrderDirection, int defaultPageNum) {
        int currentPage = ServletRequestUtils.getIntParameter(request, "pageNum", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "numPerPage", defaultPageNum);
        PageInfo pageInfo = new PageInfo(currentPage, pageSize);
        String orderField = ServletRequestUtils.getStringParameter(request, "orderField", defaultOrderField);
        String orderDirection = ServletRequestUtils.getStringParameter(request, "orderDirection", defaultOrderDirection);
        if (!Strings.isNullOrEmpty(orderField)) {
            pageInfo.setOrderField(orderField);
            pageInfo.setOrderDirection(orderDirection);
        }
        return pageInfo;
    }


    /**
     * 获取访问ip
     *
     * @param request
     * @return
     */
    public static String getUserIpByRequest(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        return ip == null ? request.getRemoteHost() : ip;
    }


    /**
     * 写入cookie
     *
     * @param response
     * @param cookieName
     * @param cookieValue
     * @param cookiePath
     * @param age
     */
    public static void addWriteCookie(HttpServletResponse response, String cookieName,
                                      String cookieValue, String cookiePath, Integer age) {
        Cookie c = new Cookie(cookieName, cookieValue);
        // c.setDomain(SystemConfig.getProperty("cookie.domain"));
        if (!Strings.isNullOrEmpty(cookiePath)) {
            c.setPath("/");
        }
        if (null != age) {
            c.setMaxAge(age);
        }
        response.addCookie(c);
    }

    /**
     * 取得后台管理员的ID
     *
     * @param request
     * @return
     */
    public static int getAdminId(HttpServletRequest request) {
        LoginUser user = getLoginUser(request);
        if (user == null) {
            return -1;
        }
        return user.getBksAdmin().getAdminId();
    }

    /**
     * 这个是 选择状态的控件 方法
     *
     * @param request
     * @param ctrId   类似 groupId
     * @param model   请求
     * @param optType 1 表示添加  2 表示取消Select -1 表示清空
     */
    public static Map<String, String> optSelectSaveState(HttpServletRequest request, String ctrId, Model model, int optType) {
        Map<String, String> idMap = new HashMap<String, String>();
        Map<String, String> returnMap = new HashMap<String, String>();
        try {
            LoginUser loginUser = (LoginUser) request.getSession().getAttribute(StageHelper.loginUserSessionName);

            String uid = loginUser.getBksAdmin().getId().toString();
            if (optType < 0) {
                ShardJedisTool.getInstance().del(JedisKey.bkstage$saveSelectState, ctrId + "_" + uid);
                returnMap.put("msg", "success");
            }
            String json = null;

            json = ShardJedisTool.getInstance().get(JedisKey.bkstage$saveSelectState, ctrId + "_" + uid);
            if (json != null) {
                idMap = new Gson().fromJson(json, Map.class);
            }

            String[] selected = ServletRequestUtils.getStringParameters(request, "optId");
            if (selected != null && selected.length > 0) {
                for (String s : selected) {
                    if (optType == 1)
                        idMap.put(s.trim(), s.trim());
                    if (optType == 2)
                        idMap.remove(s);
                }
            }
            if (idMap.size() > 200) {
                returnMap.put("msg", "full");
                return returnMap;
            }
            ShardJedisTool.getInstance().set(JedisKey.bkstage$saveSelectState, ctrId + "_" + uid, new Gson().toJson(idMap));

            if (model != null) {
                model.addAttribute(ctrId, idMap);
                model.addAttribute("SelectSaveCount", idMap.size());
            }
            returnMap.put("count", idMap.size() + "");
            returnMap.put("msg", "success");
        } catch (JedisClientException e) {
            e.printStackTrace();
            returnMap.put("msg", "error");
        }

        return returnMap;
    }

    /**
     * 获取选择状态 id 集合
     *
     * @param request
     * @param ctrId
     * @return
     */
    public static Set<String> getOptSelectSaveState(HttpServletRequest request, String ctrId) {
        LoginUser loginUser = (LoginUser) request.getSession().getAttribute(StageHelper.loginUserSessionName);
        Map<String, String> map = new HashMap<String, String>();
        String uid = loginUser.getBksAdmin().getId().toString();
        String json = null;
        try {
            json = ShardJedisTool.getInstance().get(JedisKey.bkstage$saveSelectState, ctrId + "_" + uid);
            if (json != null) {
                map = new Gson().fromJson(json, Map.class);
            }
        } catch (JedisClientException e) {
            e.printStackTrace();
        }
        return map.keySet();
    }

    public static boolean isNotPast(Date start, Date end) {
        if (isExistNullObj(new Object[]{start, end})) {
            return false;
        }
        return end.getTime() - start.getTime() > 0L;
    }

    public static boolean isAllObjNotNull(Object... objects) {
        return !isExistNullObj(objects);
    }

    public static boolean isExistNullObj(Object[] objects) {
        if (objects == null) return true;
        for (Object obj : objects) {
            if (obj == null) return true;
            if (((obj instanceof String)) &&
                    (Strings.isNullOrEmpty(obj.toString().trim()))) {
                return true;
            }
        }

        return false;
    }

    public static boolean isDate(String date) {
        return (date.matches("\\d{4}-\\d{2}-\\d{2}") || date.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}") );
    }


}

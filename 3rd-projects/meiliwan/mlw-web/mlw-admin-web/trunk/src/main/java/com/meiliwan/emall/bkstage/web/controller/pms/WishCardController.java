package com.meiliwan.emall.bkstage.web.controller.pms;

import com.google.common.base.Strings;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.bkstage.web.controller.bkstage.view.LoginUser;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.pms.bean.ProPlace;
import com.meiliwan.emall.pms.bean.WishCard;
import com.meiliwan.emall.pms.client.ProPlaceClient;
import com.meiliwan.emall.pms.client.WishCardClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 13-12-24.
 */
@Controller("WishCardController")
@RequestMapping(value = "/pms/wishcard")
public class WishCardController {

    @RequestMapping(value = "/list")
    public String list(HttpServletRequest request, HttpServletResponse response, @RequestParam(defaultValue = "asc") String asc, @RequestParam(defaultValue = "-1") Integer isRemark, @RequestParam(defaultValue = "") String createTimeMin, @RequestParam(defaultValue = "") String createTimeMax, @RequestParam(defaultValue = "") Integer status) {
        WishCard query = initSearchObj(request);
        PageInfo pi = StageHelper.getPageInfo(request);
        if (StageHelper.isAllObjNotNull(createTimeMin, createTimeMax) && !StageHelper.isNotPast(DateUtil.parseDateTime(createTimeMin), DateUtil.parseDateTime(createTimeMax))) {
            return "/pms/wishcard/list";
        }
        String timeWhereSql = initTimeWhereSql(createTimeMin, createTimeMax);
        PagerControl<WishCard> pc = WishCardClient.getWishCardsByObj(query, pi, timeWhereSql, ("asc".equals(asc)) ? true : false, isRemark(isRemark));
        query.setProName(ServletRequestUtils.getStringParameter(request, "proName", null));
        request.setAttribute("sourceCountryList", getAllOriginPlaces());
        request.setAttribute("pc", pc);
        request.setAttribute("search", query);
        request.setAttribute("asc", asc);
        request.setAttribute("isRemark", isRemark);
        request.setAttribute("status", status);
        request.setAttribute("createTimeMin", createTimeMin);
        request.setAttribute("createTimeMax", createTimeMax);
        return "/pms/wishcard/list";
    }

    private Boolean isRemark(Integer isRemark) {
        Boolean remark = null;
        if (isRemark < 0) {
            remark = null;
        } else if (isRemark == 0) {
            remark = false;
        } else if (isRemark == 1) {
            remark = true;
        }
        return remark;
    }

    private List<ProPlace> getAllOriginPlaces() {
        ProPlace temp = new ProPlace();
        temp.setIsDel((int) GlobalNames.STATE_VALID);
        return ProPlaceClient.getAllProPlaceList(temp);
    }

    private String initTimeWhereSql(String createTimeMin, String createTimeMax) {
        if (StageHelper.isAllObjNotNull(createTimeMin, createTimeMax) && StageHelper.isDate(createTimeMin) && StageHelper.isDate(createTimeMax)) {
            return "create_time >= '" + createTimeMin + "' and " + " create_time <= '" + createTimeMax + "'";
        } else if (Strings.isNullOrEmpty(createTimeMin) && !Strings.isNullOrEmpty(createTimeMax) && StageHelper.isDate(createTimeMax)) {
            return "create_time <= '" + createTimeMax + "'";
        } else if (!Strings.isNullOrEmpty(createTimeMin) && Strings.isNullOrEmpty(createTimeMax) && StageHelper.isDate(createTimeMin)) {
            return "create_time >= '" + createTimeMin + "'";
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/readRemark")
    public String readRemark(HttpServletRequest request, HttpServletResponse response, @RequestParam(defaultValue = "-1") int wishCardId, @RequestParam(defaultValue = "") String navTabId) {
        if (wishCardId < 0) {
            return StageHelper.dwzFailForward("操作失败请联系管理员", "",
                    "/pms/wishcard/readRemark", response);
        }

        try {
            WishCard result = WishCardClient.getWishCardById(wishCardId);
            if (request != null) {
                request.setAttribute("remarkList", parseRemark2List(result.getRemark()));
                request.setAttribute("wishCardId", wishCardId);
                request.setAttribute("navTabId", navTabId);
            }
        } catch (Exception e) {
            return StageHelper.dwzFailForward("操作失败请联系管理员", "",
                    "/pms/wishcard/readRemark", response);
        }
        return "/pms/wishcard/readRemark";
    }

    @RequestMapping(value = "/detail")
    public String detail(HttpServletRequest request, HttpServletResponse response, @RequestParam(defaultValue = "-1") int wishCardId) {
        if (wishCardId > 0) {
            WishCard detail = WishCardClient.getWishCardById(wishCardId);
            if (detail != null) {
                setReaded(wishCardId);
                request.setAttribute("wishCard", detail);
                request.setAttribute("remarkList", parseRemark2List(detail.getRemark()));
            }
        }
        return "/pms/wishcard/detail";
    }

    private void setReaded(int wishCardId) {
        WishCard update = new WishCard();
        update.setId(wishCardId);
        update.setStatus((byte) 1);
        WishCardClient.updateWishCardByObj(update);
    }

    @RequestMapping(value = "/addRemark")
    public String addRemark(HttpServletRequest request, HttpServletResponse response, @RequestParam(defaultValue = "-1") int wishCardId, @RequestParam(defaultValue = "") String remark, @RequestParam(defaultValue = "") String forwardUrl) throws IOException {
        String[] historyRemarks = ServletRequestUtils.getStringParameters(request, "remarks");
        if (!Strings.isNullOrEmpty(remark) && remark.split("(@@)") != null && remark.split("(@@)").length > 0 && wishCardId > 0) {
            LoginUser loginUser = (LoginUser) request.getSession().getAttribute(StageHelper.loginUserSessionName);
            WishCard updateObj = new WishCard();
            updateObj.setId(wishCardId);
            updateObj.setRemark(getRemark(remark, historyRemarks, loginUser));
            int updateId = WishCardClient.updateWishCardByObj(updateObj);
            if (updateId > 0) {
                return StageHelper.dwzSuccessClose("成功", "668d8b7ede6fcf4249e6c30cba152ea8", forwardUrl, response);
            }
        }
        return StageHelper.dwzFailClose("失败,备注不能为空", response);
    }

    /**
     * 拼串
     *
     * @param remark
     * @param historyRemarks
     * @param loginUser
     * @return
     */
    private String getRemark(String remark, String[] historyRemarks, LoginUser loginUser) {
        StringBuffer sb = new StringBuffer("");
        if (historyRemarks != null && historyRemarks.length > 0) {
            for (String temp : historyRemarks) {
                sb.append(temp).append("@@");
            }
        }

        if (Strings.isNullOrEmpty(remark)) {
            return sb.toString();
        } else {
            remark = remark.replace("(@@)", "") + " (备注人：" + loginUser.getBksAdmin().getAdminName() + ",备注时间：" + DateUtil.getCurrentDateStr() + ")" + "@@" + sb.toString();
        }
        return remark;
    }

    /**
     * 把remark字段分割成List
     *
     * @param remark
     * @return
     */
    private List<String> parseRemark2List(String remark) {
        if (Strings.isNullOrEmpty(remark)) {
            return null;
        }
        return Arrays.asList(remark.split("(@@)"));
    }

    private String removeSplitStr(String inputStr) {
        if (Strings.isNullOrEmpty(inputStr)) {
            return null;
        }
        return inputStr.replace("(@@)", "");
    }

    private WishCard initSearchObj(HttpServletRequest request) {
        WishCard wishCard = new WishCard();
        String createTime = ServletRequestUtils.getStringParameter(request, "createTime", null);
        wishCard.setCreateTime((Strings.isNullOrEmpty(createTime) ? null : DateUtil.parseDateTime(createTime)));
        String commitUser = ServletRequestUtils.getStringParameter(request, "commitUser", null);
        wishCard.setCommitUser((Strings.isNullOrEmpty(commitUser) ? null : commitUser));
        String sourceCountryName = ServletRequestUtils.getStringParameter(request, "sourceCountryName", null);
        wishCard.setSourceCountryName((Strings.isNullOrEmpty(sourceCountryName) || "-1".equals(sourceCountryName)) ? null : sourceCountryName);
        String proName = ServletRequestUtils.getStringParameter(request, "proName", null);
        wishCard.setProName(Strings.isNullOrEmpty(proName) ? null : "%" + proName + "%");
        int status = ServletRequestUtils.getIntParameter(request, "status", -1);
        wishCard.setStatus((status < 0) ? null : (byte) status);
        return wishCard;
    }

}

package com.meiliwan.emall.bkstage.web.controller.mms;

import com.google.common.base.Strings;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.web.TextUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.mms.bean.UserComplaints;
import com.meiliwan.emall.mms.client.UserComplaintsClient;
import com.meiliwan.emall.pms.constant.Constant;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;

/**
 * User: guangdetang
 * Date: 13-6-15
 * Time: 下午6:14
 */
@Controller("complaintsController")
@RequestMapping("/mms/complaints")
public class ComplaintsController {

    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

    /**
     * 投诉分页列表
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/list")
    public String list(Model model, HttpServletRequest request) {
        String compId = ServletRequestUtils.getStringParameter(request, "complaintsId", null);
        String orderId = ServletRequestUtils.getStringParameter(request, "orderId", null);
        String nickName = ServletRequestUtils.getStringParameter(request, "nickName", null);
        String compType = ServletRequestUtils.getStringParameter(request, "complaintsType", null);
        String state = ServletRequestUtils.getStringParameter(request, "state", null);

        UserComplaints complaints = new UserComplaints();
        if (!Strings.isNullOrEmpty(compId)) {
            complaints.setId(Integer.parseInt(compId));
        }
        if (!Strings.isNullOrEmpty(orderId)) {
            complaints.setOrderId(orderId);
        }
        if (!Strings.isNullOrEmpty(nickName)) {
            complaints.setNickName(nickName);
        }
        if (!Strings.isNullOrEmpty(compType) && Integer.parseInt(compType) > 0) {
            complaints.setComplaintsType(Short.parseShort(compType));
        }
        if (!Strings.isNullOrEmpty(state) && Integer.parseInt(state) > -1) {
            complaints.setState(Short.parseShort(state));
        }

        PagerControl<UserComplaints> pc = UserComplaintsClient.getComplaintsPagerByAdmin(complaints, StageHelper.getPageInfo(request, "create_time", "desc"));
        model.addAttribute("pc", pc);
        model.addAttribute("compId", compId);
        model.addAttribute("orderId", orderId);
        model.addAttribute("nickName", nickName);
        model.addAttribute("compType", compType);
        model.addAttribute("state", state);
        return "/mms/complaints/list";
    }

    /**
     * 修改状态
     * @param request
     * @param response
     */
    @RequestMapping(value = "/update")
    public void update(HttpServletRequest request, HttpServletResponse response) {
        try {
            int handle = ServletRequestUtils.getIntParameter(request, "handle", -1);
            int[] ids = ServletRequestUtils.getIntParameters(request, "ids");
            boolean isSuc = UserComplaintsClient.updateComplaintsState(ids, handle);
            StageHelper.writeDwzSignal(isSuc ? "200" : "300", isSuc ? "操作成功" : "操作失败", "163", StageHelper.DWZ_FORWARD, "/mms/complaints/list", response);
        } catch (Exception e) {
            logger.error(e, "管理员修改投诉各种状态时，遇到异常", WebUtils.getIpAddr(request));
            StageHelper.writeDwzSignal("300", "操作失败", "163", StageHelper.DWZ_FORWARD, "/mms/complaints/list", response);
        }
    }

    /**
     * 详情
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/detail")
    public String detail(Model model, HttpServletRequest request) {
        int id = ServletRequestUtils.getIntParameter(request, "id", -1);
        if (id > 0) {
            model.addAttribute("entity", UserComplaintsClient.getComplaints(id));
        }
        return "/mms/complaints/detail";
    }

    /**
     * 回复
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/reply")
    public String reply(Model model, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        try {
            int handle = ServletRequestUtils.getIntParameter(request, "handle", -1);
            int id = ServletRequestUtils.getIntParameter(request, "id", -1);
            if (handle == 0) {
                UserComplaints comment = UserComplaintsClient.getComplaints(id);
                model.addAttribute("entity", comment);
                return "/mms/complaints/reply";
            } else {
                String replyContent = ServletRequestUtils.getStringParameter(request, "replyContent", null);
                boolean isSuc = false;
                if (id > 0 && replyContent != null) {
                    int replyLength = new String(replyContent.getBytes("gbk"), "ISO8859_1").length();
                    if (replyLength >= Constant.COMPLAINTS_REPLY_LENGTH_START * 2 && replyLength <= Constant.COMPLAINTS_REPLY_LENGTH_END * 2) {
                        isSuc = UserComplaintsClient.replyComplaints(id, TextUtil.cleanHTML(replyContent));
                    }

                }
                return StageHelper.writeDwzSignal(isSuc ? "200" : "300", isSuc ? "操作成功" : "操作失败", "163", StageHelper.DWZ_CLOSE_CURRENT, "/mms/complaints/list", response);
            }
        } catch (Exception e) {
            logger.error(e, "管理员回复用户的投诉时，遇到异常", WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal( "300", "操作失败", "163", StageHelper.DWZ_CLOSE_CURRENT, "/mms/complaints/list", response);
        }
    }

}

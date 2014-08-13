package com.meiliwan.emall.bkstage.web.controller.pms;

import com.google.common.base.Strings;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.RegexUtil;
import com.meiliwan.emall.pms.bean.ProPlace;
import com.meiliwan.emall.pms.client.ProPlaceClient;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: guangdetang
 * Date: 13-6-6
 * Time: 下午4:56
 * 商品产地
 */
@Controller("pmsPlaceController")
@RequestMapping("/pms/place")
public class PlaceController {

    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

    /**
     * 查询产地list
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/list")
    public String list(Model model, HttpServletRequest request) {
        String id = ServletRequestUtils.getStringParameter(request, "id", null);
        ProPlace place = new ProPlace();
        if (!Strings.isNullOrEmpty(id) && RegexUtil.isInt(id)) {
            place.setPlaceId(Integer.parseInt(id));
        }
        PagerControl<ProPlace> pc = ProPlaceClient.getAllProPlacePager(place, StageHelper.getPageInfo(request));
        model.addAttribute("id", id);
        model.addAttribute("pc", pc);
        return "/pms/place/list";
    }

    /**
     * 添加
     * @param request
     * @param response
     * @return
     * @throws ServletRequestBindingException
     */
    @RequestMapping(value = "/add")
    public String add(HttpServletRequest request, HttpServletResponse response)
            throws ServletRequestBindingException {
        int handle = ServletRequestUtils.getIntParameter(request, "handle", 0);
        if (handle == 0) {
            String placeName = ServletRequestUtils.getStringParameter(request, "placeName", null);
            String enName = ServletRequestUtils.getStringParameter(request, "enName", null);
            String otherName = ServletRequestUtils.getStringParameter(request, "otherName", null);
            if (!Strings.isNullOrEmpty(placeName)) {
                ProPlace place = new ProPlace();
                place.setPlaceName(placeName);
                place.setEnName(enName);
                place.setOtherName(otherName);
                place.setCreateTime(DateUtil.getCurrentTimestamp());
                place.setIsDel((int) GlobalNames.STATE_VALID);
                boolean isU = ProPlaceClient.addPropPlace(place);
                return StageHelper.writeDwzSignal(isU ? "200" : "300", isU ? "操作成功" : "操作失败", "64", StageHelper.DWZ_CLOSE_CURRENT, "/pms/place/list", response);
            } else {
                return StageHelper.writeDwzSignal("300", "操作失败", "64", StageHelper.DWZ_CLOSE_CURRENT, "/pms/place/list", response);
            }
        } else {
            return "/pms/place/add";
        }
    }

    /**
     * 修改
     * @param model
     * @param request
     * @param response
     * @return
     * @throws ServletRequestBindingException
     */
    @RequestMapping(value = "/update")
    public String update(Model model, HttpServletRequest request, HttpServletResponse response)
            throws ServletRequestBindingException {
        int handle = ServletRequestUtils.getIntParameter(request, "handle", 0);
        if (handle == 0) {
            int id = ServletRequestUtils.getIntParameter(request, "id");
            model.addAttribute("entity", ProPlaceClient.getProPlaceById(id));
            return "/pms/place/edit";
        } else {
            String placeName = ServletRequestUtils.getStringParameter(request, "placeName", null);
            String enName = ServletRequestUtils.getStringParameter(request, "enName", null);
            String otherName = ServletRequestUtils.getStringParameter(request, "otherName", null);
            if (!Strings.isNullOrEmpty(placeName)) {
                ProPlace place = new ProPlace();
                place.setPlaceName(placeName);
                place.setEnName(enName);
                place.setOtherName(otherName);
                int id = ServletRequestUtils.getIntParameter(request, "id");
                place.setPlaceId(id);
                boolean isU = ProPlaceClient.updateProPlace(place);
                return StageHelper.writeDwzSignal(isU ? "200" : "300", isU ? "操作成功" : "操作失败", "64", StageHelper.DWZ_CLOSE_CURRENT, "/pms/place/list", response);
            } else {
                return StageHelper.writeDwzSignal("300", "操作失败", "64", StageHelper.DWZ_CLOSE_CURRENT, "/pms/place/list", response);
            }
        }
    }

    /**
     * 删除或恢复删除
     * @param request
     * @param response
     */
    @RequestMapping(value = "/del")
    public void del(HttpServletRequest request, HttpServletResponse response) {
        int[] ids = ServletRequestUtils.getIntParameters(request, "ids");
        int state = ServletRequestUtils.getIntParameter(request, "state", 0);
        if (ids != null && (state == 0 || state == -1)) {
            boolean flag = true;
            for (Integer i : ids) {
                ProPlace place = new ProPlace();
                place.setPlaceId(i);
                place.setIsDel(state);
                Boolean isSuccess = ProPlaceClient.updateProPlace(place);
                if (!isSuccess) flag = false;
            }
            StageHelper.writeDwzSignal(flag ? "200" : "300", flag ? "操作成功" : "可能有部分操作失败，请仔细核对！", "64", StageHelper.DWZ_FORWARD, "/pms/place/list", response);
        } else {
            StageHelper.writeDwzSignal("300", "操作失败", "64", StageHelper.DWZ_CLOSE_CURRENT, "/pms/place/list", response);
        }
    }
}

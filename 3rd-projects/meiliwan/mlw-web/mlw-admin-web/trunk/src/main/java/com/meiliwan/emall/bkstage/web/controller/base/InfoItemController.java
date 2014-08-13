package com.meiliwan.emall.bkstage.web.controller.base;

import com.meiliwan.emall.base.bean.BaseInfoItem;
import com.meiliwan.emall.base.client.BaseInfoItemClient;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.commons.PagerControl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 资讯类别管理
 * User: wuzixin
 * Date: 13-6-14
 * Time: 上午10:31
 */
@Controller
@RequestMapping("/base/infoitem")
public class InfoItemController {

    @RequestMapping("/list")
    public String list(Model mode, HttpServletRequest request) {
        //默认为资讯列表
        int itemType = ServletRequestUtils.getIntParameter(request, "itemType", 1);
        BaseInfoItem bit = new BaseInfoItem();
        bit.setItemType((short) itemType);
        PagerControl<BaseInfoItem> bitList = BaseInfoItemClient.getListByPager(bit, StageHelper.getPageInfo(request));
        mode.addAttribute("pc", bitList);
        mode.addAttribute("itemType", itemType);
        return "/base/infoitem/list";
    }

    @RequestMapping("/add")
    public String add(Model model, HttpServletRequest request, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        if (isHandle > 0) {
            //获取资讯类型
            int itemType = ServletRequestUtils.getIntParameter(request, "itemType", 1);
            //获取资讯类别名称
            String itemName = ServletRequestUtils.getStringParameter(request, "itemName", "");
            //获取文件名
            String fileName = ServletRequestUtils.getStringParameter(request,"fileName","");
            BaseInfoItem bit = new BaseInfoItem();
            bit.setItemType((short) itemType);
            bit.setInfoItemName(itemName);
            bit.setFileName(fileName);
            bit.setParentId(0);
            boolean suc = BaseInfoItemClient.addBaseInfoItem(bit);
            return StageHelper.writeDwzSignal(suc ? "200" : "300", suc ? "添加成功" : "添加失败", "147", StageHelper.DWZ_FORWARD, "/base/infoitem/list", response);
        } else {
            return "/base/infoitem/add";
        }
    }

    @RequestMapping("/update")
    public String update(Model model, HttpServletRequest request, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        if (isHandle > 0) {
            int itemId = ServletRequestUtils.getIntParameter(request, "itemId", 0);
            //获取资讯类别名称
            String itemName = ServletRequestUtils.getStringParameter(request, "itemName", "");
            //获取文件名
            String fileName = ServletRequestUtils.getStringParameter(request,"fileName","");
            BaseInfoItem bit = new BaseInfoItem();
            bit.setInfoItemId(itemId);
            bit.setInfoItemName(itemName);
            bit.setFileName(fileName);
            boolean suc = BaseInfoItemClient.updateBaseInfoItem(bit);
            return StageHelper.writeDwzSignal(suc ? "200" : "300", suc ? "修改成功" : "修改失败", "147", StageHelper.DWZ_CLOSE_CURRENT, "/base/infoitem/list", response);
        } else {
            int itemId = ServletRequestUtils.getIntParameter(request, "itemId", 0);
            BaseInfoItem baseIT = BaseInfoItemClient.getBaseInfoItemById(itemId);
            model.addAttribute("baseIT", baseIT);
            return "/base/infoitem/update";
        }
    }

    @RequestMapping("/delete")
    public String delete(Model model, HttpServletRequest request, HttpServletResponse response) {
        int itemId = ServletRequestUtils.getIntParameter(request, "itemId", 0);
        boolean suc = BaseInfoItemClient.deleteBaseInfoItem(itemId);
        return StageHelper.writeDwzSignal(suc ? "200" : "300", suc ? "删除成功" : "删除失败", "147", StageHelper.DWZ_FORWARD, "/base/infoitem/list", response);
    }
}

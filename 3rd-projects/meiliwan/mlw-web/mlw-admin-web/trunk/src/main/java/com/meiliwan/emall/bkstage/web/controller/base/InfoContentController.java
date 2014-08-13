package com.meiliwan.emall.bkstage.web.controller.base;

import com.meiliwan.emall.base.bean.BaseInfoContent;
import com.meiliwan.emall.base.bean.BaseInfoItem;
import com.meiliwan.emall.base.client.BaseInfoContentClient;
import com.meiliwan.emall.base.client.BaseInfoItemClient;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.IPUtil;
import com.meiliwan.emall.commons.web.WebUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * User: wuzixin
 * Date: 13-6-14
 * Time: 下午2:39
 */
@Controller
@RequestMapping("/base/infocontent")
public class InfoContentController {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    private final static MLWLogger logger = MLWLoggerFactory.getLogger(InfoContentController.class);
    @RequestMapping("/list")
    public String list(Model model, HttpServletRequest request) {
        List<BaseInfoItem> items = BaseInfoItemClient.getListByBaseIT(new BaseInfoItem());
        int itemId = ServletRequestUtils.getIntParameter(request, "itemId", 0);
        BaseInfoContent baseICT = new BaseInfoContent();
        if (itemId > 0) {
            baseICT.setInfoItemId(itemId);
        }
        PagerControl<BaseInfoContent> pc = BaseInfoContentClient.getBaseICTByPager(baseICT, StageHelper.getPageInfo(request));
        model.addAttribute("items", items);
        model.addAttribute("itemId", itemId);
        model.addAttribute("pc", pc);
        return "/base/infocontent/list";
    }

    @RequestMapping("/add")
    public String add(Model model, HttpServletRequest request, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        if (isHandle > 0) {
            //获取资讯类别
            int itemId = ServletRequestUtils.getIntParameter(request, "itemId", 0);
            String infoName = ServletRequestUtils.getStringParameter(request, "infoName", "");
            String content = ServletRequestUtils.getStringParameter(request, "infoContent", "");
            int sequence = ServletRequestUtils.getIntParameter(request, "sequence", 0);
            String  seoTitle = ServletRequestUtils.getStringParameter(request, "seoTitle", "");
            String  seoKeywords = ServletRequestUtils.getStringParameter(request,"seoKeywords","");
            String  seoDescp = ServletRequestUtils.getStringParameter(request,"seoDescp","");

            BaseInfoContent bict = new BaseInfoContent();
            bict.setInfoName(infoName);
            bict.setInfoItemId(itemId);
            bict.setInfoContent(content);
            bict.setSequence(sequence);
            bict.setState((short) 1);
            bict.setSeoTitle(seoTitle);
            bict.setSeoKeywords(seoKeywords);
            bict.setSeoDescp(seoDescp);
            bict.setCreateTime(DateUtil.getCurrentTimestamp());
            bict.setPublishTime(DateUtil.getCurrentTimestamp());
            boolean suc = BaseInfoContentClient.addBaseInfoContent(bict);

            return StageHelper.writeDwzSignal(suc ? "200" : "300", suc ? "添加成功" : "添加失败", "152", StageHelper.DWZ_FORWARD, "/base/infocontent/list", response);
        } else {
            List<BaseInfoItem> items = BaseInfoItemClient.getListByBaseIT(new BaseInfoItem());
            model.addAttribute("items", items);
            return "/base/infocontent/add";
        }
    }

    @RequestMapping("/update")
    public String update(Model model, HttpServletRequest request, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        if (isHandle > 0) {
            //获取资讯ID
            int infoId = ServletRequestUtils.getIntParameter(request, "infoId", 0);
            //获取资讯类别
            int itemId = ServletRequestUtils.getIntParameter(request, "itemId", 0);
            String infoName = ServletRequestUtils.getStringParameter(request, "infoName", "");
            String content = ServletRequestUtils.getStringParameter(request, "infoContent", "");
            int sequence = ServletRequestUtils.getIntParameter(request, "sequence", 0);
            String  seoTitle = ServletRequestUtils.getStringParameter(request,"seoTitle","");
            String  seoKeywords = ServletRequestUtils.getStringParameter(request,"seoKeywords","");
            String  seoDescp = ServletRequestUtils.getStringParameter(request,"seoDescp","");

            BaseInfoContent bict = new BaseInfoContent();
            bict.setInfoId(infoId);
            bict.setInfoName(infoName);
            bict.setInfoItemId(itemId);
            bict.setInfoContent(content);
            bict.setSequence(sequence);
            bict.setSeoTitle(seoTitle);
            bict.setSeoKeywords(seoKeywords);
            bict.setSeoKeywords(seoDescp);
            bict.setUpdateTime(DateUtil.getCurrentTimestamp());
            boolean suc = BaseInfoContentClient.updateBaseInfoContent(bict);

            //清除缓存

            if(suc){
                try {
                    suc = ShardJedisTool.getInstance().del(JedisKey.global$zixun,String.valueOf(infoId));
                } catch (JedisClientException e) {
                    logger.error(e,"修改资讯清除 rdeis 缓存异常"+e.getMessage(), WebUtils.getIpAddr(request));
                }
            }

            return StageHelper.writeDwzSignal(suc ? "200" : "300", suc ? "修改成功" : "修改失败", "152", StageHelper.DWZ_CLOSE_CURRENT, "/base/infocontent/list", response);
        } else {
            //获取资讯ID
            int infoId = ServletRequestUtils.getIntParameter(request, "infoId", 0);
            List<BaseInfoItem> items = BaseInfoItemClient.getListByBaseIT(new BaseInfoItem());
            BaseInfoContent content = BaseInfoContentClient.getBaseICTById(infoId);
            model.addAttribute("items", items);
            model.addAttribute("content", content);
            return "/base/infocontent/update";
        }
    }

    @RequestMapping("/delete")
    public String delete(Model model, HttpServletRequest request, HttpServletResponse response) {
        int infoId = ServletRequestUtils.getIntParameter(request, "infoId", 0);
        BaseInfoContent bict = new BaseInfoContent();
        bict.setInfoId(infoId);
        //表示删除
        bict.setState((short) 0);
        boolean suc = BaseInfoContentClient.updateBaseInfoContent(bict);
        try {
            suc = ShardJedisTool.getInstance().del(JedisKey.global$zixun, String.valueOf(infoId));
        } catch (JedisClientException e) {
            logger.error(e,"软删除资讯清除 rdeis 缓存异常"+e.getMessage(), WebUtils.getIpAddr(request));
        }
        return StageHelper.writeDwzSignal(suc ? "200" : "300", suc ? "删除成功" : "删除失败", "152", StageHelper.DWZ_FORWARD, "/base/infocontent/list", response);
    }

    @RequestMapping("/active")
    public String active(Model model, HttpServletRequest request, HttpServletResponse response) {
        int infoId = ServletRequestUtils.getIntParameter(request, "infoId", 0);
        BaseInfoContent bict = new BaseInfoContent();
        bict.setInfoId(infoId);
        //表示删除
        bict.setState((short) 1);
        boolean suc = BaseInfoContentClient.updateBaseInfoContent(bict);
        try {
            suc = ShardJedisTool.getInstance().del(JedisKey.global$zixun, String.valueOf(infoId));
        } catch (JedisClientException e) {
            logger.error(e,"软删除资讯清除 rdeis 缓存异常"+e.getMessage(), WebUtils.getIpAddr(request));
        }
        return StageHelper.writeDwzSignal(suc ? "200" : "300", suc ? "启用成功" : "启用失败", "152", StageHelper.DWZ_FORWARD, "/base/infocontent/list", response);
    }
}

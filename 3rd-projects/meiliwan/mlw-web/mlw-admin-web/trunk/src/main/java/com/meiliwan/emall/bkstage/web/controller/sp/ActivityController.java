package com.meiliwan.emall.bkstage.web.controller.sp;

import com.google.common.base.Strings;
import com.meiliwan.emall.bkstage.bean.BksAdmin;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.web.WebParamterUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.pms.bean.ProProduct;
import com.meiliwan.emall.pms.bean.SimpleProduct;
import com.meiliwan.emall.pms.client.ProProductClient;
import com.meiliwan.emall.pms.dto.ProductDTO;
import com.meiliwan.emall.sp2.bean.ActivityBean;
import com.meiliwan.emall.sp2.bean.ActivityDiscountRuleBean;
import com.meiliwan.emall.sp2.bean.ActivityProductBean;
import com.meiliwan.emall.sp2.bean.base.ActivityRuleBean;
import com.meiliwan.emall.sp2.client.ActivityClient;
import com.meiliwan.emall.sp2.client.ActivityProductClient;
import com.meiliwan.emall.sp2.client.ActivityRuleClient;
import com.meiliwan.emall.sp2.client.rule.ActivityDiscountRuleClient;
import com.meiliwan.emall.sp2.constant.ActAdminViewState;
import com.meiliwan.emall.sp2.constant.ActType;
import com.meiliwan.emall.sp2.dto.ActivityDTO;
import org.jsoup.helper.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * 活动
 * Created by guangdetang on 13-12-25.
 */
@Controller
@RequestMapping(value = "/sp/activity")
public class ActivityController {

    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

    /**
     * 查询活动列表
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/list")
    public String listActs(HttpServletRequest request, Model model) {
        int actId = ServletRequestUtils.getIntParameter(request, "actId", 0);
        int actType = ServletRequestUtils.getIntParameter(request, "actType", 0);
        String actState = ServletRequestUtils.getStringParameter(request, "actState", "");
        String actName = ServletRequestUtils.getStringParameter(request, "actName", "");
        String startTime = ServletRequestUtils.getStringParameter(request, "startTime", "");
        String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");
        Date dst = new Date();
        Date det = new Date();
        if (!Strings.isNullOrEmpty(startTime) && !Strings.isNullOrEmpty(endTime)) {
            dst = DateUtil.parseTimestamp(startTime);
            det = DateUtil.parseTimestamp(endTime);
            if (dst.getTime() > det.getTime()) {
                Date swap = new Date();
                swap = dst;
                dst = det;
                det = swap;
            }
        }
        ActivityDTO act = new ActivityDTO();
        act.setCurrentTime(new Date());
        if (actId > 0) {
            act.setActId(actId);
        }
        if (actType > 0) {
            if (actType==1) {
                act.setActType("DISCOUNT");
            }
        }
        if (!Strings.isNullOrEmpty(actState)) {
            act.setActViewState(Short.parseShort(actState));
        }
        if (!Strings.isNullOrEmpty(actName)) {
            act.setActName(actName);
        }
        if (!Strings.isNullOrEmpty(startTime)) {
            act.setStartTime(dst);
        }
        if (!Strings.isNullOrEmpty(endTime)) {
            act.setEndTime(det);
        }
        PagerControl<ActivityBean> pc = ActivityClient.getSpActivityPaperByActivityDTO(act, StageHelper.getPageInfo(request, "create_time", "desc"));
        if (pc != null && pc.getEntityList() != null && pc.getEntityList().size() > 0) {
            List<Integer> list = new ArrayList<Integer>(pc.getEntityList().size());
            for (ActivityBean ab : pc.getEntityList()) {
                list.add(ab.getActId());
            }
            Map<Integer, Integer> map = ActivityProductClient.getActProsByActIds(list);
            model.addAttribute("productCount", map);
        }
        model.addAttribute("actState", ActAdminViewState.values());
        model.addAttribute("actState1", actState);
        model.addAttribute("currentTime", act.getCurrentTime());
        model.addAttribute("pc", pc);
        model.addAttribute("act", act);
        model.addAttribute("actType", actType);
        return "/sp/activity/list";
    }

    /**
     * 添加或修改活动
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/addOrUpdate")
    public String addOrUpdate(HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
        int handle = ServletRequestUtils.getIntParameter(request, "handle", 0);
        if (handle == 1) {
            //确认添加活动
            ActivityBean act = getActObj(request);
            //保存活动
            int actId = ActivityClient.saveSpActivity(act);
            act.setActId(actId);
            List<ActivityRuleBean> list = getActRule(request, act);
            act.setSpRules(list);
            //保存活动规则
            ActivityRuleClient.saveSpActivityRules(act);
            request.setAttribute("actId", actId);
            return StageHelper.writeDwzSignal("200", "活动创建成功，请为该活动添加商品！", "actDetail", StageHelper.DWZ_FORWARD, "/sp/activity/listActPro?actId=" + actId + "&handle=1", response);
        } else if (handle == 2) {
            //修改活动上下架状态
            int actId = ServletRequestUtils.getIntParameter(request,"actId",0);
            ActivityBean act = new ActivityBean();
            act.setActId(actId);
            boolean result = ActivityClient.downSpActivity(act);
            return StageHelper.writeDwzSignal(result?"200":"300", result?"操作成功":"操作失败", "156", StageHelper.DWZ_FORWARD, "/sp/activity/list", response);
            //删除活动
        } else if (handle == 3) {
            int actId = ServletRequestUtils.getIntParameter(request, "actId", 0);
            boolean result = ActivityClient.deleteUnOnlineSpActivity(actId);
            return StageHelper.writeDwzSignal(result?"200":"300", result?"操作成功":"操作失败", "156", StageHelper.DWZ_FORWARD, "/sp/activity/list", response);
        } else {
            //去添加活动页
            return "/sp/activity/addOrUpdate";
        }
    }

    /**
     * 查询活动及其活动商品列表
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/listActPro")
    public String listActPro(HttpServletRequest request, Model model) {
        int handle = ServletRequestUtils.getIntParameter(request, "handle", 0);
        int actId = ServletRequestUtils.getIntParameter(request,"actId",0);
        ActivityBean act = ActivityClient.getSpActivityById(actId);
        List spRules = ActivityRuleClient.getSpActRules(act) ;
        if (act != null) {
            ActivityProductBean sp = new ActivityProductBean();
            sp.setActId(actId);
            PagerControl<ActivityProductBean> pc = ActivityProductClient.getSpActivityProductPaperByObj(sp, StageHelper.getPageInfo(request, "create_time", "desc"));
            model.addAttribute("pc", pc);
            //如果活动未上架则可编辑
            if (act.getState().equals(0)) {
                handle = 1;
            }
        }
        model.addAttribute("handle", handle);
        model.addAttribute("act", act);
        if(spRules!=null && spRules.size()>0){
            model.addAttribute("rule", spRules.get(0));
        }
        return "/sp/activity/addOrUpdate";
    }


    /**
     * 查询商品列表
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/showProList")
    public String showProList(HttpServletRequest request, Model model, ProductDTO bean) {

        int actId = ServletRequestUtils.getIntParameter(request, "actId", 0);

        try {
            PagerControl<ProductDTO> pc = null;
            String orderField = ServletRequestUtils.getStringParameter(request, "orderField", "");
            String orderDirection = ServletRequestUtils.getStringParameter(request, "orderDirection", "");

            if (bean == null) {
                bean = new ProductDTO();
            }

            PageInfo pageInfo = WebParamterUtil.getPageInfo(request);
            pageInfo.setOrderField(orderField);
            pageInfo.setOrderDirection(orderDirection);
            if ("".equals(orderField)) {
                pageInfo.setOrderField("update_time");
                pageInfo.setOrderDirection("desc");
            }

            ActivityProductUtils.setProductSearchParam(request, model, bean);
            pc = ProProductClient.pageByObj(bean, pageInfo, pageInfo.getOrderField(), pageInfo.getOrderDirection());
            ActivityProductUtils.setSupplier(model);
            ActivityProductUtils.setState(model);
            ActivityProductUtils.setSellType(model);
            ActivityProductUtils.setCategory(model);
            if(actId > 0){
                ActivityBean act = ActivityClient.getSpActivityById(actId);
                //校验那些商品已经参与了活动
                List<ProductDTO> dtoList = pc.getEntityList();
                if(dtoList!=null){
                    List<Integer> proIdList = new ArrayList<Integer>(dtoList.size());
                    for(ProductDTO dto : dtoList){
                        proIdList.add(dto.getProId());
                    }
                    //根据商品ids检查商品是否处于有效（创建、上架）活动的内
                    List<Integer> usedProIds = ActivityProductClient.checkActUsingProId(act.getStartTime(), act.getEndTime(), proIdList);
                    model.addAttribute("usedProIds", usedProIds);
                }
            }
            model.addAttribute("pc", pc);
            model.addAttribute("actId", actId);
        } catch (Exception e) {
            logger.error(e, bean, WebUtils.getIpAddr(request));
        }

        return "/sp/activity/showProList";
    }

    /**
     * 给活动添加商品
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/addActPro")
    public String addActProduct(HttpServletRequest request, HttpServletResponse response) {
        int method = ServletRequestUtils.getIntParameter(request, "method", 0);
        int[] proIds = ServletRequestUtils.getIntParameters(request, "ids");
        int actId = ServletRequestUtils.getIntParameter(request, "actId", 0);
        ActivityBean act = ActivityClient.getSpActivityById(actId);
        List<ActivityProductBean> listSp = getActProListByProIds(request, proIds, actId, act);
        if (listSp != null && listSp.size() > 0) {
            List<ActivityProductBean> list= ActivityProductClient.saveSpActivityProducts(listSp, act.getStartTime(), act.getEndTime());
            return StageHelper.writeDwzSignal("200", "操作成功"+(listSp.size()-list.size())+"条", method==0?"actDetail":"", StageHelper.DWZ_FORWARD, "/sp/activity/listActPro?actId="+actId+"&handle=1", response);
        } else {
            return StageHelper.writeDwzSignal("300", "操作失败", "actDetail", StageHelper.DWZ_CLOSE_CURRENT, "/sp/activity/listActPro", response);
        }

    }

    /**
     * 修改活动商品的优惠
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/updateActPro")
    public String updateActProduct(HttpServletRequest request, Model model, HttpServletResponse response) {
        int handle = ServletRequestUtils.getIntParameter(request, "handle", 0);
        int actId = ServletRequestUtils.getIntParameter(request, "actId", 0);
        int actProId = ServletRequestUtils.getIntParameter(request, "actProId", 0);
        if (handle==1) {
            //确认修改
            int action = ServletRequestUtils.getIntParameter(request, "action", 0);
            if (action == 1) {
                String dp = ServletRequestUtils.getStringParameter(request, "discountPrice", "");
                String discount = ServletRequestUtils.getStringParameter(request, "discount", "");
                String lostPrice = ServletRequestUtils.getStringParameter(request, "lostPrice", "");
                ActivityProductBean sp = new ActivityProductBean();
                sp.setId(actProId);
                int dis = Integer.parseInt(discount.replace(".", ""));
                if (discount.indexOf(".") < 0) {
                    dis = dis*10;
                }
                sp.setDiscount(dis);
                sp.setDiscounts(new BigDecimal(lostPrice));
                sp.setActPrice(new BigDecimal(dp));
                ActivityProductClient.updateSpActivityProduct(sp);
                return StageHelper.writeDwzSignal("200", "操作成功", "actDetail", StageHelper.DWZ_CLOSE_CURRENT, "/sp/activity/listActPro?actId="+actId+"&handle=1", response);
            } else {
                //移除活动商品
                ActivityProductClient.deleteActProById(actProId);
                return StageHelper.writeDwzSignal("200", "操作成功", "actDetail", StageHelper.DWZ_FORWARD, "/sp/activity/listActPro?actId="+actId+"&handle=1", response);
            }
        } else {
            //去修改
            ActivityProductBean sp = ActivityProductClient.getSpActivityProductById(actProId);
            //查询成本价
            ProProduct pp = ProProductClient.getWholeProductById(sp.getProId());
            model.addAttribute("costPrice", pp.getTradePrice());
            model.addAttribute("entity", sp);
            return "/sp/activity/updateDiscount";
        }
    }

    @RequestMapping(value = "/editDone")
    public String editDone(HttpServletRequest request, HttpServletResponse response) {
        int actId = ServletRequestUtils.getIntParameter(request, "actId", 0);
        ActivityBean act = new ActivityBean();
        act.setActId(actId);
        boolean result = ActivityClient.upSpActivity(act);
        return StageHelper.writeDwzSignal(result ? "200" : "300", result ? "操作成功" : "操作失败", "156", result ? StageHelper.DWZ_CLOSE_CURRENT : StageHelper.DWZ_FORWARD, result ? "/sp/activity/list" : "/sp/activity/listActPro?actId=" + actId, response);
    }


    //获得活动对象
    private ActivityBean getActObj(HttpServletRequest request){
        ActivityBean act = new ActivityBean();
        BksAdmin admin = getAdmin(request);
        act.setUid(admin.getAdminId() + "");
        act.setAdmin(admin.getAdminName());
        act.setActName(ServletRequestUtils.getStringParameter(request, "actName", ""));
        act.setStartTime(DateUtil.parseDateTime(ServletRequestUtils.getStringParameter(request, "startTime", "")));
        act.setEndTime(DateUtil.parseDateTime(ServletRequestUtils.getStringParameter(request, "endTime", "")));
        act.setActType(ServletRequestUtils.getStringParameter(request, "actType", ""));

        String wenAn = ServletRequestUtils.getStringParameter(request, "wenAn", "");
        if(wenAn.equals("default")){
            act.setActWenan(ServletRequestUtils.getStringParameter(request, "defaultWenAn", ""));
        }else{
            act.setActWenan(ServletRequestUtils.getStringParameter(request, "actWenAn", ""));
        }

        return act;
    }

    //获得活动规则
    private List<ActivityRuleBean> getActRule(HttpServletRequest request, ActivityBean act) {
        List<ActivityRuleBean> list = new ArrayList<ActivityRuleBean>();
        ActivityDiscountRuleBean rule = new ActivityDiscountRuleBean();
        BksAdmin admin = getAdmin(request);
        rule.setActId(act.getActId());
        rule.setUid(admin.getAdminId());
        rule.setAdmin(admin.getAdminName());
        rule.setRuleName(act.getActName());
        rule.setDiscount(ServletRequestUtils.getIntParameter(request, "defaultDiscount", 100));//默认折扣
        list.add(rule);
        return list;
    }

    //根据商品IDS活动活动商品列表
    private List<ActivityProductBean> getActProListByProIds(HttpServletRequest request, int[] proIds, int actId, ActivityBean act) {
        List<ActivityProductBean> listSp = new ArrayList<ActivityProductBean>();
        if (act != null && proIds != null) {
            //如果是限时折扣
            if (act.getActType().equals(ActType.DISCOUNT.name())) {
                //获得折扣活动规则
                List<ActivityDiscountRuleBean> listRule = ActivityDiscountRuleClient.getDiscountRuleByActId(actId);
                if (listRule != null && listRule.size() > 0) {
                    List<SimpleProduct> listProduct = ProProductClient.getSimpleListByProIds(proIds);
                    if (listProduct != null && listProduct.size() > 0) {
                        for (SimpleProduct p : listProduct) {
                            BksAdmin admin = getAdmin(request);
                            ActivityProductBean sp = new ActivityProductBean();
                            sp.setUid(admin.getAdminId());
                            sp.setActId(actId);
                            sp.setAdmin(admin.getAdminName());
                            sp.setProId(p.getProId());
                            sp.setProName(p.getProName());
                            sp.setMlwPrice(p.getMlwPrice());
                            sp.setDiscount(listRule.get(0).getDiscount());
                            sp.setActPrice(new BigDecimal(p.getMlwPrice().doubleValue() * listRule.get(0).getDiscount()/100d));
                            sp.setDiscounts(new BigDecimal(p.getMlwPrice().doubleValue()-sp.getActPrice().doubleValue()));
                            sp.setThirdCatId(p.getThirdCategoryId());
                            listSp.add(sp);
                        }
                    }
                }
            } else {
                //TODO 如果是其他活动
            }

        }
        return listSp;
    }

    private BksAdmin getAdmin(HttpServletRequest request){
        return StageHelper.getLoginUser(request).getBksAdmin();
    }
}

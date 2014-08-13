package com.meiliwan.emall.bkstage.web.controller.oms;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meiliwan.emall.commons.bean.PayCode;
import com.meiliwan.emall.oms.bean.*;
import com.meiliwan.emall.oms.constant.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.base.Strings;
import com.meiliwan.emall.bkstage.client.RoleServiceClient;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.bkstage.web.controller.bkstage.view.LoginUser;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.NumberUtil;
import com.meiliwan.emall.commons.util.RegexUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.mms.bean.UserPassport;
import com.meiliwan.emall.mms.client.UserPassportClient;
import com.meiliwan.emall.oms.client.OrdClient;
import com.meiliwan.emall.oms.client.ReturnOrderClient;
import com.meiliwan.emall.oms.constant.OrdIRetStatus;
import com.meiliwan.emall.oms.constant.OrdITotalStatus;
import com.meiliwan.emall.oms.constant.RetType;
import com.meiliwan.emall.oms.constant.RetordCreateType;
import com.meiliwan.emall.oms.constant.TransportCompany;
import com.meiliwan.emall.pms.bean.SimpleProduct;
import com.meiliwan.emall.pms.client.ProProductClient;

/**
 * Created by Sean on 13-10-2.
 * 退换货 后台新controller
 */
@Controller("omsRetOrdController")
@RequestMapping("/oms/returnOrder")
public class ReturnOrderController {
    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

    /**
     * 退换货列表
     */
    @RequestMapping("/list_{method}")
    public String list(HttpServletRequest request, HttpServletResponse response,
                       @PathVariable(value = "method") String method,
                       Model model, @ModelAttribute Retord ret) {
        String st = ServletRequestUtils.getStringParameter(request, "startTime", "");
        String et = ServletRequestUtils.getStringParameter(request, "endTime", "");
        PageInfo pageInfo = StageHelper.getPageInfo(request, "create_time", "desc");
        model.addAttribute("retordStateList", OrdIRetStatus.values());
        model.addAttribute("retTypeList", RetType.values());
        model.addAttribute("startTime", st);
        model.addAttribute("endTime", et);
        model.addAttribute("ret", ret);
        model.addAttribute("retCreateTypeList", RetordCreateType.values());
        //如果传入非时间格式字符串。直接返回空页面

        StringBuilder sb = new StringBuilder();
        if ("".equals(ret.getCreateType() + "")) {
            ret.setCreateType(null);
        }
        //method（1等待仓库确认收货，2等待仓库发货，3等待财务退款，4等待客服审核，5仓库已经发货，6仓库拒收）
        if ("1".equals(method)){
            if (Strings.isNullOrEmpty(ret.getRetStatus())) {
                ret.setRetStatus(OrdIRetStatus.RET_BUYER_WAIT_CONSIGNMENT.getCode());
            }
        } else if ("2".equals(method)) {
            if (Strings.isNullOrEmpty(ret.getRetStatus())) {
                ret.setRetStatus(OrdIRetStatus.RET_SELLER_WAITING_SEND.getCode());
            }
        }else if ("3".equals(method)) {
            ret.setRetStatus(OrdIRetStatus.RET_REFUND_WAIT.getCode());
        } else if ("4".equals(method)) {
            if (Strings.isNullOrEmpty(ret.getRetStatus())) {
                ret.setRetStatus(OrdIRetStatus.RET_APPLY.getCode());
            }
        } else if ("5".equals(method)) {
            if (Strings.isNullOrEmpty(ret.getRetStatus())) {
                ret.setRetStatus(OrdIRetStatus.RET_SELLER_AG_SEND.getCode());
            }
        } else if ("6".equals(method)) {
            if (Strings.isNullOrEmpty(ret.getRetStatus())) {
                ret.setRetStatus(OrdIRetStatus.RET_SELLER_NO_RECEIPTED.getCode());
            }
        } else {
            if ("".equals(ret.getRetStatus())) {
                ret.setRetStatus(null);
            }
        }
        if ("".equals(ret.getRetType()) || Strings.isNullOrEmpty(ret.getRetType())) {
            ret.setRetType(null);
        }
        if ("".equals(ret.getUserName() + "")) {
            ret.setUserName(null);
        }
        if ("".equals(ret.getOldOrderId())) {
            ret.setOldOrderId(null);
        }
        if ("".equals(ret.getAdMobile())) {
            ret.setAdMobile(null);
        }
        if ("".equals(ret.getRetordOrderId())) {
            ret.setRetordOrderId(null);
        }
        if (!Strings.isNullOrEmpty(st)) {
            if (!RegexUtil.isDateTime(st)) {
                return "/oms/retord/list";
            }
            sb.append(" create_time>\"" + st + "\"");
        }
        if (!Strings.isNullOrEmpty(et)) {
            if (!RegexUtil.isDateTime(et)) {
                return "/oms/retord/list";
            }
            if (!Strings.isNullOrEmpty(sb.toString())) {
                sb.append(" and create_time<\"" + et + "\"");
            } else {
                sb.append(" create_time<\"" + et + "\"");
            }
        }
        PagerControl<Retord> pc = ReturnOrderClient.getRetordPager(ret, pageInfo, sb.toString());
        model.addAttribute("method", method);
        model.addAttribute("pc", pc);
        return "/oms/retord/list";
    }

    /**
     * 退换货列表_查看信息
     */
    @RequestMapping("/view_{method}")
    public String view(HttpServletRequest request, HttpServletResponse response,
                       @PathVariable(value = "method") String method,
                       Model model, @ModelAttribute Retord retord) throws Exception {
        String id = ServletRequestUtils.getStringParameter(request, "retordId", "");
        Retord ret = ReturnOrderClient.getRetordById(id);
        sendObj(ret, model, 0);
        int isEditMode = 0;

        List<OrdPay> ordPays = OrdClient.getOrdPayList(retord.getOldOrderId());

        for (OrdPay ordPay : ordPays) {
            if (PayCode.MLW_C.toString().equals(ordPay.getPayCode())) {
                model.addAttribute("isUseCardPay", 1);
            }
        }
        //如果是等待审核的申请，则查看是否已经申请过
        /*if (ret.getRetStatus().equals(OrdIRetStatus.RET_APPLY.getCode())) {
            List<Retord> listRet = ReturnOrderClient.getRetordListByOid(ret.getOldOrderId());
            if (listRet != null && listRet.size() > 0) {
                //拿到正常退换货已经结束了的退换货记录列表
                List<Retord> retDoneList = new ArrayList<Retord>();
                for (Retord r : listRet) {
                    if (r.getRetStatus().equals(OrdIRetStatus.RET_DONE.getCode())) {
                        List<RetordLogs> listLogs = ReturnOrderClient.getRetordLogsListByRetId(r.getRetordOrderId());
                        if (listLogs != null && listLogs.size() > 0) {
                            for (RetordLogs logs : listLogs) {
                                if (logs.getOptStatusCode().equals(OrdIRetStatus.RET_FINISHED.getCode())) {
                                    retDoneList.add(r);
                                }
                            }
                        }
                    }
                }
                //存在正常退换货结束的记录
                if (retDoneList.size() > 0) {
                    for (Ordi oi : ret.getOrdiList()) {

                    }


                    List<RetOrdDoneDTO> listDoneDTO = new ArrayList<RetOrdDoneDTO>();
                    for (Retord ro : retDoneList) {
                        List<Ordi> listOrdi = OrdClient.getOrdiListByOrderId(ro.getRetordOrderId());
                        if (listOrdi != null && listOrdi.size() > 0) {
                            for (Ordi o : listOrdi) {
                                if (o.getState().equals(1)) {
                                    RetOrdDoneDTO doneDTO = new RetOrdDoneDTO();
                                    doneDTO.setProId(o.getProId());
                                    doneDTO.setRetType(ro.getRetType());
                                    doneDTO.setRetCount(o.getSaleNum() + "");
                                }
                            }
                        }
                    }
                }
            }
        }*/
        model.addAttribute("isEditMode", isEditMode);
        return "/oms/retord/handle";
    }

    private void sendObj(Retord ret, Model model, int flow) throws Exception {
        Ord ord = OrdClient.getOrdByOrdId(ret.getOldOrderId());
        boolean isUseCardPay = false;
        Double cardPayAmount = new Double(0.00);
        Double mlwPayAmount = new Double(0.00);
        if (ord.getOrderType().equals(OrderType.REAL_ORDER_COD.getCode())) {
            model.addAttribute("isCOD", 1);
        } else {
            List<OrdPay> ordPays = OrdClient.getOrdPayList(ret.getOldOrderId());
            Double thirdPayAmount = new Double(0.00);
            for (OrdPay ordPay : ordPays) {
                if (PayCode.MLW_C.toString().equals(ordPay.getPayCode())) {
                    cardPayAmount = ordPay.getPayAmount();
                    isUseCardPay = true;
                    //是否使用礼品卡支付
                    model.addAttribute("isUseCardPay", 1);
                }
                if (PayCode.MLW_W.toString().equals(ordPay.getPayCode())) {
                    mlwPayAmount = ordPay.getPayAmount();
                }
                if (PayCode.isThirdPay(ordPay.getPayCode().toString())) {
                    thirdPayAmount = ordPay.getPayAmount();
                    PayCode[] payCodes = PayCode.values();
                    String thirdPayDesc = "";
                    for (PayCode payCode : payCodes) {
                        if (payCode.name().equals(ordPay.getPayCode())) {
                            thirdPayDesc = payCode.getDesc();
                            break;
                        }
                    }
                    model.addAttribute("thirdPayDesc", thirdPayDesc);
                }
            }
            //使用礼品卡支付金额数
            model.addAttribute("cardPayAmount", cardPayAmount);
            //使用美丽币支付金额
            model.addAttribute("mlwPayAmount", mlwPayAmount);
            //使用第三方支付金额
            model.addAttribute("thirdPayAmount", thirdPayAmount);
            //如果使用了礼品卡支付，则计算可退回礼品卡剩余金额
        }
        if (isUseCardPay) {
            List<Retord> retordList = ReturnOrderClient.getRetordListByOid(ret.getOldOrderId());
            if (retordList != null && retordList.size() > 0) {
                Double cardRetAmount = new Double(0.00);//已退货礼品卡金额
                for (Retord r : retordList) {
                    //存在礼品卡支付且退换货订单未取消、且审核通过
                    if (r.getRetRealCardAmount() != null && r.getRetRealCardAmount() > 0 && !r.getRetStatus().equals(OrdIRetStatus.RET_BUYER_OPT_CANCEL.getCode())
                            && !r.getRetStatus().equals(OrdIRetStatus.RET_CANCEL.getCode()) && !r.getRetStatus().equals(OrdIRetStatus.RET_APPLY_REJECTED.getCode())) {
                        if (flow==18) {//如果是去修改，则需要判断是已退款成功
                            if (r.getRetStatus().equals("80")) {
                                cardRetAmount = NumberUtil.add(cardRetAmount, r.getRetRealCardAmount(), 2);
                            }
                        } else {
                            cardRetAmount = NumberUtil.add(cardRetAmount, r.getRetRealCardAmount(), 2);
                        }
                    }
                }
                //已经退回礼品卡金额
                model.addAttribute("cardRetAmount", NumberUtil.add(0, cardRetAmount, 2));
                //可退金额
                model.addAttribute("cardCanRetAmount", NumberUtil.subtract(cardPayAmount, cardRetAmount, 2));
            }
        }
        String picstr = ret.getUploadPic();
        String[] pics = null;
        if (picstr != null) {
            pics = picstr.split(",");
        }
        //获得用户账号信息
        model.addAttribute("user", UserPassportClient.getPassportByUid(ret.getUid()));
        model.addAttribute("ord", ord);//为了展示用户是否开发票
        model.addAttribute("pics", pics);
        model.addAttribute("iniRetProdAmount",iniRetProdAmount(ret.getOrdiList()));//初始化计算好商品退换货的商品总额
        model.addAttribute("iniRetCartPayAmount",iniRetProdAmount(ret.getOrdiList()) == 0.0 ? 0.00 : iniRetProdAmount(ret.getOrdiList())/ord.getRealPayAmount()*cardPayAmount);//初始化计算好商品可退回礼品卡金额
        model.addAttribute("iniRetWalletPayAmount",iniRetProdAmount(ret.getOrdiList()) == 0.0 ? 0.00 : iniRetProdAmount(ret.getOrdiList())/ord.getRealPayAmount()*mlwPayAmount);//初始化计算好商品可退回礼品卡金额
        model.addAttribute("retStatus", OrdIRetStatus.values());
        model.addAttribute("ret", ret);
    }

    /**
     *计算退换货商品总额
     * @param ordiList
     * @return
     */
    private Double iniRetProdAmount(List<Ordi> ordiList) {
        Double subAmount = new Double(0.00);
        for (Ordi temp:ordiList){
            if (temp.getState()==1) {
                subAmount = NumberUtil.add(subAmount, NumberUtil.multiply(temp.getUintPrice(),new Double(temp.getSaleNum())));
            }
        }
        return NumberUtil.add(0, subAmount, 2);
    }

    private boolean parseForm(Retord retord, HttpServletRequest request) {
        //处理客服选择
        String selectStr = ServletRequestUtils.getStringParameter(request, "userSelect", null);
        if (!Strings.isNullOrEmpty(selectStr)) {
            String[] item = selectStr.split(",");
            List<Ordi> list = new ArrayList<Ordi>();
            for (String objStr : item) {
                Ordi obj = new Ordi();
                String[] params = objStr.split("_");
                obj.setOrderItemId(params[0]);
                obj.setSaleNum(Integer.parseInt(params[1]));
                list.add(obj);
            }
            retord.setOrdiList(list);
            return true;
        }
        return false;
    }

    /**
     * 处理退换货流程信息
     */
    @RequestMapping("/handle_{flowNum}")
    public String handle(HttpServletRequest request, HttpServletResponse response,
                         @PathVariable(value = "flowNum") Integer flowNum,
                         Model model, @ModelAttribute Retord retord, @ModelAttribute RetordLogs retordLogs) throws Exception {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        //判断是否访问页面还是处理 添加信息
        if (isHandle > 0) {
            boolean isSuc = false;
            parseForm(retord, request);
            int nextStatus = ServletRequestUtils.getIntParameter(request, "nextStatus", -1);
            String[] returnParams = ServletRequestUtils.getStringParameters(request, "retordParams");
            //走下一个流程
            if (returnParams != null && returnParams.length > 1) {
                int adminId = Integer.parseInt(returnParams[0].split("=")[1]);
                String pwd = returnParams[1].split("=")[1];
                boolean pwdRight = RoleServiceClient.checkFinancePwd(adminId, pwd);
                if (!pwdRight) {
                    logger.warn("财务密码输入不正确", "adminId(" + adminId + ")", WebUtils.getIpAddr(request));
                    return StageHelper.writeDwzSignal("300", "财务密码输入有误", "10139", StageHelper.DWZ_CLOSE_CURRENT, "/oms/returnOrder/list_all", response);
                }
            }
            if (nextStatus > 0) {
                isSuc = ReturnOrderClient.optRetord(retord, retordLogs, nextStatus, returnParams) > 0;
            }
            return StageHelper.writeDwzSignal(isSuc ? "200" : "300", isSuc ? "操作成功" : "操作失败请联系管理员", "10139", StageHelper.DWZ_CLOSE_CURRENT, "/oms/returnOrder/list_all", response);
        } else {
            retord = ReturnOrderClient.getRetordById(retord.getRetordOrderId());
            //审核处理 为编辑状态
            int isEditMode = flowNum == 10 || flowNum == 18 ? 1 : 0;
            LoginUser loginUser = (LoginUser) request.getSession().getAttribute(StageHelper.loginUserSessionName);
            model.addAttribute("flow", flowNum);
            model.addAttribute("isEditMode", isEditMode);
            model.addAttribute("adminObj", loginUser.getBksAdmin());
            model.addAttribute("handleUrl", request.getRequestURI());
            model.addAttribute("transportCompany", TransportCompany.values());
            sendObj(retord, model, flowNum);
            return "/oms/retord/handle";
        }
    }

    /**
     * 添加退换货
     * @param model
     * @param request
     * @param response
     * @param ret
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/add")
    public String addReturnOrd(Model model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute Retord ret) throws Exception {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        //确定添加
        if (isHandle > 0) {
            try {
                String[] evidencePic = ServletRequestUtils.getStringParameters(request, "uploadPic");
                String selectStr = ServletRequestUtils.getStringParameter(request, "userSelect", null);
                Timestamp time = DateUtil.getCurrentTimestamp();
                ret.setCreateTime(time);
                ret.setUpdateTime(time);
                ret.setRetTime(time);
                ret.setUserRetType(ret.getRetType());
                StringBuilder sb = new StringBuilder();
                String pic = null;
                if (evidencePic != null && evidencePic.length > 0) {
                    for (int i = 0; i < evidencePic.length; i++) {
                        sb.append(evidencePic[i]).append(",");
                    }
                    pic = sb.substring(0, sb.length() - 1);
                }
                ret.setUploadPic(pic);
                int result = ReturnOrderClient.addRetord(ret, selectStr.split(","), StageHelper.getLoginUser(request).getBksAdmin().getAdminName());
                return StageHelper.writeDwzSignal(result > 0 ? "200" : "300", result > 0 ? "操作成功" : "操作失败请联系管理员", "10139", StageHelper.DWZ_CLOSE_CURRENT, "/oms/returnOrder/list_all", response);
            } catch (Exception e) {
                logger.error(e, "管理员确定添加退换货时，遇到异常, orderId(" + ret.getOldOrderId() + ")", WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "操作失败请联系管理员", "10139", StageHelper.DWZ_FORWARD, "/oms/returnOrder/list_all", response);
            }
            //跳转到添加页面
        } else if (isHandle == 0) {
            try {
                String ordId = ServletRequestUtils.getStringParameter(request, "ordId", "");
                //判断所填订单是否存在或者是否存在正在处理中的退换货申请
                if (!Strings.isNullOrEmpty(ordId) && ordId.matches("[0-9]+")) {
                    Ord ord = OrdClient.getOrdByOrdId(ordId);
                    if (ord != null && (ord.getOrderStatus().equals(OrdITotalStatus.ORDI_FINISHED.getCode()) || ord.getOrderStatus().equals(OrdITotalStatus.ORDI_CONSINGMENT.getCode()))) {
                        boolean repeat = ReturnOrderClient.getIsRepeatByOrdId(ordId);
                        if (repeat) {
                            model.addAttribute("handle", -1);
                            model.addAttribute("oid", ordId);
                            model.addAttribute("msg", 1);//存在未处理完毕的申请
                            return "/oms/retord/add";
                        } else {
                            List<Ordi> ordiList = OrdClient.getOrdiListByOrderId(ordId);
                            boolean isUseCardPay = false;
                            Double cardPayAmount = new Double(0.00);
                            //封装商品条形码
                            if (ordiList != null && ordiList.size() > 0) {
                                for (Ordi o : ordiList) {
                                    SimpleProduct product = ProProductClient.getProductById(o.getProId());
                                    if (product != null) {
                                        o.setProBarCode(product.getBarCode());
                                    }
                                }
                            }
                            //查询订单是否是货到付款订单
                            if (ord.getOrderType().equals(OrderType.REAL_ORDER_COD.getCode())) {
                                model.addAttribute("isCOD", 1);
                            } else {
                                //获取原订单支付详情
                                List<OrdPay> ordPays = OrdClient.getOrdPayList(ordId);
                                Double mlwPayAmount = new Double(0.00);
                                Double thirdPayAmount = new Double(0.00);
                                for (OrdPay ordPay : ordPays) {
                                    if (PayCode.MLW_C.toString().equals(ordPay.getPayCode())) {
                                        cardPayAmount = ordPay.getPayAmount();
                                        isUseCardPay = true;
                                        //是否使用礼品卡支付
                                        model.addAttribute("isUseCardPay", 1);
                                    }
                                    if (PayCode.MLW_W.toString().equals(ordPay.getPayCode())) {
                                        mlwPayAmount = ordPay.getPayAmount();
                                    }
                                    if (PayCode.isThirdPay(ordPay.getPayCode().toString())) {
                                        thirdPayAmount = ordPay.getPayAmount();
                                        String thirdPayDesc = "";
                                        PayCode[] payCodes = PayCode.values();
                                        for (PayCode payCode : payCodes) {
                                            if (payCode.name().equals(ordPay.getPayCode())) {
                                                thirdPayDesc = payCode.getDesc();
                                                break;
                                            }
                                        }
                                        model.addAttribute("thirdPayDesc", thirdPayDesc);
                                    }
                                }
                                //使用礼品卡支付金额数
                                model.addAttribute("cardPayAmount", cardPayAmount);
                                //使用美丽币支付金额
                                model.addAttribute("mlwPayAmount", mlwPayAmount);
                                //使用第三方支付金额
                                model.addAttribute("thirdPayAmount", thirdPayAmount);
                            }
                            if (isUseCardPay) {
                                List<Retord> retordList = ReturnOrderClient.getRetordListByOid(ordId);
                                if (retordList != null && retordList.size() > 0) {
                                    Double cardRetAmount = new Double(0.00);//已退货礼品卡金额
                                    for (Retord r : retordList) {
                                        if (r.getRetRealCardAmount() != null && r.getRetRealCardAmount() > 0 && !r.getRetStatus().equals(OrdIRetStatus.RET_BUYER_OPT_CANCEL.getCode())
                                                && !r.getRetStatus().equals(OrdIRetStatus.RET_CANCEL.getCode()) && !r.getRetStatus().equals(OrdIRetStatus.RET_APPLY_REJECTED.getCode())) {
                                            cardRetAmount = NumberUtil.add(cardRetAmount, r.getRetRealCardAmount(), 2);
                                        }
                                    }
                                    model.addAttribute("cardRetAmount", cardRetAmount);
                                }
                            }
                            if (ord != null) {
//                                UserRecvAddr addr = UserRecvAddrClient.getUserAddressById(ord.getRecvAddrId());
                            	OrdAddr addr = OrdClient.getOrdAddrById(ord.getOrderId());
                                UserPassport user = UserPassportClient.getPassportByUid(ord.getUid());
                                model.addAttribute("handle", isHandle);
                                model.addAttribute("ord", ord);
                                model.addAttribute("addr", addr);
                                model.addAttribute("user", user);
                                model.addAttribute("ordiList", ordiList);
                            }
                            return "/oms/retord/add";
                        }
                    } else {
                        model.addAttribute("handle", -1);
                        model.addAttribute("oid", ordId);
                        model.addAttribute("msg", 0);//订单不存在
                        return "/oms/retord/add";
                    }
                } else {
                    model.addAttribute("handle", -1);
                    model.addAttribute("oid", ordId);
                    model.addAttribute("msg", 2);//订单不合法
                    return "/oms/retord/add";
                }
            } catch (Exception e) {
                logger.error(e, "管理员添加退换货跳第二步时，遇到异常", WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "操作失败请联系管理员", "10139", StageHelper.DWZ_CLOSE_CURRENT, "/oms/returnOrder/list_all", response);
            }
            //第一步：填写订单页面
        } else {
            model.addAttribute("handle", isHandle);
            return "/oms/retord/add";
        }
    }
}

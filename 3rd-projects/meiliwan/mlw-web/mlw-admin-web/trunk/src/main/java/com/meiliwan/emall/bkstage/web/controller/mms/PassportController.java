package com.meiliwan.emall.bkstage.web.controller.mms;

import com.meiliwan.emall.account.bean.AccountWallet;
import com.meiliwan.emall.account.client.AccountWalletClient;
import com.meiliwan.emall.base.client.BaseMailAndMobileClient;
import com.meiliwan.emall.base.client.ValidateCodeSendClient;
import com.meiliwan.emall.base.dto.MsgTemplete;
import com.meiliwan.emall.bkstage.bean.BksAdmin;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.bean.ErrorPageCode;
import com.meiliwan.emall.commons.exception.BaseRuntimeException;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.exception.WebRuntimeException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.IPUtil;
import com.meiliwan.emall.commons.util.RegexUtil;
import com.meiliwan.emall.commons.util.StringUtil;
import com.meiliwan.emall.commons.web.TextUtil;
import com.meiliwan.emall.commons.web.UserLoginUtil;
import com.meiliwan.emall.commons.web.WebParamterUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.mms.bean.UserExtra;
import com.meiliwan.emall.mms.bean.UserPassport;
import com.meiliwan.emall.mms.bean.UserPassportPara;
import com.meiliwan.emall.mms.bean.UserStationMsg;
import com.meiliwan.emall.mms.client.UserDmdeliveryClient;
import com.meiliwan.emall.mms.client.UserExtraClient;
import com.meiliwan.emall.mms.client.UserPassportClient;
import com.meiliwan.emall.mms.client.UserStationMsgClient;
import com.meiliwan.emall.oms.client.OrdClient;
import com.meiliwan.emall.oms.constant.OrdITotalStatus;
import com.meiliwan.emall.oms.dto.OrdDTO;
import com.meiliwan.emall.oms.dto.OrderQueryDTO;

import com.meiliwan.emall.pay.constant.Payment;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jiawu.wu
 * Date: 13-6-6
 * Time: 下午4:56
 * 用户管理
 */
@Controller
@RequestMapping("/mms/passport")
public class PassportController {

    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

    private boolean filldata(Model model, HttpServletRequest request, UserPassportPara para) {
        String userNameSearch = request.getParameter("userNameSearch");
        String nickNameSearch = request.getParameter("nickNameSearch");
        String emailSearch = request.getParameter("emailSearch");
        String mobileSearch = request.getParameter("mobileSearch");

        String birthdayBegin = request.getParameter("birthdayBegin");
        String birthdayEnd = request.getParameter("birthdayEnd");
        String createTimeBegin = request.getParameter("createTimeBegin");
        String createTimeEnd = request.getParameter("createTimeEnd");

        String mlwCoinBegin = request.getParameter("mlwCoinBegin");
        String mlwCoinEnd = request.getParameter("mlwCoinEnd");


        para.setUserNameSearch(userNameSearch);
        para.setNickNameSearch(nickNameSearch);
        para.setEmailSearch(emailSearch);
        para.setMobileSearch(mobileSearch);

        if (birthdayBegin != null && !birthdayBegin.equals("")) {
            try {
                para.setBirthdayBegin(new SimpleDateFormat("yyyy-MM-dd").parse(birthdayBegin));
            } catch (ParseException e) {
                logger.error(e,"new SimpleDateFormat(\"yyyy-MM-dd\").parse(birthdayBegin): {birthdayBegin:"+birthdayBegin+"}", WebUtils.getIpAddr(request));
                return false;
            }
        }

        if (birthdayEnd != null && !birthdayEnd.equals("")) {
            try {
                para.setBirthdayEnd(new SimpleDateFormat("yyyy-MM-dd").parse(birthdayEnd));
            } catch (ParseException e) {
                logger.error(e,"new SimpleDateFormat(\"yyyy-MM-dd\").parse(birthdayEnd): {birthdayEnd:"+birthdayEnd+"}", WebUtils.getIpAddr(request));
                return false;
            }
        }

        if (createTimeBegin != null && !createTimeBegin.equals("")) {
            try {
                para.setCreateTimeBegin(new SimpleDateFormat("yyyy-MM-dd").parse(createTimeBegin));
            } catch (ParseException e) {
                logger.error(e,"new SimpleDateFormat(\"yyyy-MM-dd\").parse(createTimeBegin): {createTimeBegin:"+createTimeBegin+"}", WebUtils.getIpAddr(request));
                return false;
            }
        }

        if (createTimeEnd != null && !createTimeEnd.equals("")) {
            try {
                para.setCreateTimeEnd(new SimpleDateFormat("yyyy-MM-dd").parse(createTimeEnd));
            } catch (ParseException e) {
                logger.error(e,"new SimpleDateFormat(\"yyyy-MM-dd\").parse(createTimeEnd): {createTimeEnd:"+createTimeEnd+"}", WebUtils.getIpAddr(request));
                return false;
            }
        }


        if (mlwCoinBegin != null && !mlwCoinBegin.equals("")) {
            try {
                para.setMlwCoinBegin(new BigDecimal(mlwCoinBegin));
            } catch (Exception e) {
                logger.error(e,"new BigDecimal(mlwCoinBegin): {mlwCoinBegin:"+mlwCoinBegin+"}",WebUtils.getIpAddr(request));
                return false;
            }

        }

        if (mlwCoinEnd != null && !mlwCoinEnd.equals("")) {
            try {
                para.setMlwCoinEnd(new BigDecimal(mlwCoinEnd));
            } catch (Exception e) {
                logger.error(e,"new BigDecimal(mlwCoinEnd): {mlwCoinEnd:"+mlwCoinEnd+"}",WebUtils.getIpAddr(request));
                return false;
            }
        }


        model.addAttribute("userNameSearch", userNameSearch);
        model.addAttribute("nickNameSearch", nickNameSearch);
        model.addAttribute("emailSearch", emailSearch);
        model.addAttribute("mobileSearch", mobileSearch);
        model.addAttribute("birthdayBegin", birthdayBegin);
        model.addAttribute("birthdayEnd", birthdayEnd);
        model.addAttribute("createTimeBegin", createTimeBegin);
        model.addAttribute("createTimeEnd", createTimeEnd);
        model.addAttribute("mlwCoinBegin", mlwCoinBegin);
        model.addAttribute("mlwCoinEnd", mlwCoinEnd);


        return true;

    }

    /**
     * 查询正常用户list
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/searchlist")
    public String list(Model model, HttpServletRequest request) {

        UserPassportPara para = new UserPassportPara();
        para.setStateSearch((short) 1);

        filldata(model, request, para);

        PageInfo pageInfo = StageHelper.getPageInfo(request);
        PagerControl<UserPassportPara> pc = UserPassportClient.getSearchList(para, pageInfo);

        fillMlwCoin(pc);

        model.addAttribute("pc", pc);

        return "/mms/passport/list";
    }

    /**
     * 单个用户的订单列表
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/myOrderList")
    public String myOrderList(Model model, HttpServletRequest request) {
        PageInfo pi = WebParamterUtil.getPageInfo(request);

        Integer uid = Integer.parseInt(request.getParameter("uid"));
        String order_state = request.getParameter("order_state");
        model.addAttribute("uid", uid);
        model.addAttribute("order_state", order_state);

        //针对页面的
        OrderQueryDTO dto = new OrderQueryDTO();
        dto.setUid(uid);
        if (StringUtils.isNotBlank(order_state)) {
            dto.setOrderItemStatus(order_state);
        }


        //排序
        boolean sort_value = ServletRequestUtils.getBooleanParameter(request, "sort_value", true);
        PagerControl<OrdDTO> pc = OrdClient.getOrderList(dto, pi, sort_value);
        //查找收货人

        //查询条件
        model.addAttribute("payFormMap", Payment.getPaymentList());
        model.addAttribute("sortValue", sort_value);
        model.addAttribute("search", dto);
        model.addAttribute("pc", pc);
        setOrderState(model);
        return "/mms/passport/myOrderList";
    }

    /**
     * 订单状态
     *
     * @param model
     */
    private void setOrderState(Model model) {
        OrdITotalStatus[] list = {
                OrdITotalStatus.ORDI_COMMITTED,
                OrdITotalStatus.ORDI_EFFECTIVED,
                OrdITotalStatus.ORDI_CONSINGMENT,
                OrdITotalStatus.ORDI_FINISHED,
                OrdITotalStatus.ORDI_CANCEL
        };
        model.addAttribute("orderStateList", list);
    }


    /**
     * 黑名单列表
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/blacklist")
    public String blacklist(Model model, HttpServletRequest request) {

        UserPassportPara para = new UserPassportPara();
        para.setStateSearch((short) -1);

        filldata(model, request, para);

        PageInfo pageInfo = StageHelper.getPageInfo(request);
        PagerControl<UserPassportPara> pc = UserPassportClient.getSearchList(para, pageInfo);

        fillMlwCoin(pc);

        model.addAttribute("pc", pc);

        return "/mms/passport/blacklist";
    }

    /**
     * 查询钱包余额，再放到结果列表中
     *
     * @param pc
     */
    private void fillMlwCoin(PagerControl<UserPassportPara> pc) {
        List<UserPassportPara> newlist = new ArrayList<UserPassportPara>();
        List<UserPassportPara> list = pc.getEntityList();
        if (list != null && list.size() > 0) {
            for (UserPassportPara o : list) {
                AccountWallet wallet = AccountWalletClient.getAccountWalletByUid(o.getUid());
                if (wallet != null) {
                    o.setMlwCoin(wallet.getMlwCoin());
                } else {
                    o.setMlwCoin(new BigDecimal(0));
                }
                newlist.add(o);
            }
        }
        pc.setEntityList(newlist);
    }


    /**
     * 放到黑名单
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/del")
    public void del(HttpServletRequest request, HttpServletResponse response) {
        boolean success = false;

        String uid = ServletRequestUtils.getStringParameter(request,
                "uid", "");

        if (StringUtils.isNotEmpty(uid)) {

            Integer id = Integer.parseInt(uid);

            success = UserPassportClient.softdel(id);

            UserPassport user = UserPassportClient.getPassportByUid(id);

            if(user!=null){
                UserLoginUtil.updateUserState(id,user.getState()+"");
            }


        }

        StageHelper.writeDwzSignal(success ? "200" : "300", success ? "操作成功" : "可能有部分操作失败，请仔细核对！", "64", StageHelper.DWZ_FORWARD, "/mms/passport/searchlist", response);
    }

    /**
     * 移出黑名单
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/undoDel")
    public void undoDel(HttpServletRequest request, HttpServletResponse response) {
        boolean success = false;
        String uid = ServletRequestUtils.getStringParameter(request, "uid", "");

        if (StringUtils.isNotEmpty(uid)) {

            Integer uids = Integer.parseInt(uid);

            UserPassport para = new UserPassport();
            para.setUid(uids);
            para.setState((short) 1);
            success = UserPassportClient.update(para);

            UserPassport user = UserPassportClient.getPassportByUid(uids);

            if(user!=null){
                UserLoginUtil.updateUserState(uids,user.getState()+"");
            }

        }
        StageHelper.writeDwzSignal(success ? "200" : "300", success ? "操作成功" : "可能有部分操作失败，请仔细核对！", "64", StageHelper.DWZ_FORWARD, "/mms/passport/blacklist", response);
    }

    /**
     * 批量移出黑名单
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/undoDelBatch", method = RequestMethod.POST)
    public void undoDelBatch(HttpServletRequest request, HttpServletResponse response) {
        boolean success = false;
        int[] uids = ServletRequestUtils.getIntParameters(request, "ids");

        if (uids != null && uids.length > 0) {

            UserPassport[] updates = new UserPassport[uids.length];
            UserPassport para;

            int i = 0;
            for (int uid : uids) {
                para = new UserPassport();
                para.setUid(uid);
                para.setState((short) 1);
                updates[i++] = para;
            }

            if(UserPassportClient.updateBatch(updates)){
                for(int uid:uids){
                    UserPassport user = UserPassportClient.getPassportByUid(uid);
                    if(user!=null){
                        UserLoginUtil.updateUserState(uid,user.getState()+"");
                    }
                }
            };

        }
        StageHelper.writeDwzSignal(success ? "200" : "300", success ? "操作成功" : "可能有部分操作失败，请仔细核对！", "64", StageHelper.DWZ_FORWARD, "/mms/passport/blacklist", response);
    }


    /**
     * 查看用户详情
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/detail")
    public String detail(Model model, HttpServletRequest request, HttpServletResponse response) {

        String uid = ServletRequestUtils.getStringParameter(request, "uid", "");

        if (StringUtils.isNotEmpty(uid)) {

            Integer uuid = Integer.parseInt(uid);
            UserPassport passport = UserPassportClient.getPassportByUid(uuid);
            UserExtra extra = UserExtraClient.getExtraByUid(uuid);

            model.addAttribute("passport", passport);
            model.addAttribute("extra", extra);
        }

        return "/mms/passport/detail";

    }

    /**
     * 生成随机密码
     *
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/createCode", method = RequestMethod.GET)
    public String createCode(Model model, HttpServletRequest request, HttpServletResponse response) {
        String uid = ServletRequestUtils.getStringParameter(request, "uid", "");
        boolean success = false;

        if (StringUtils.isNotEmpty(uid)) {

            String sessionId = request.getSession().getId();
            String code = ValidateCodeSendClient.createCodeNum(sessionId, 8);

            success = StringUtils.isBlank(code) ? false : true;

            if (success) {
                UserPassport user = UserPassportClient.getPassportByUid(Integer.parseInt(uid));
                model.addAttribute("pc", user);
            }
        }

        model.addAttribute("uid", uid);
        model.addAttribute("success", success);
        return "/mms/passport/createCode";
    }

    /**
     * 更新登陆密码
     *
     * @param code
     * @param uid
     */
    private boolean updateLoginPwd(String code,String uid){

        UserPassport para = new UserPassport();
        para.setUid(Integer.parseInt(uid));
        para.setPassword(UserLoginUtil.encrypt(code));

        return UserPassportClient.update(para);
    }

    /**
     * 通过邮箱发送登陆密码
     *
     * @param code
     * @param uid
     * @param nickName
     * @param email
     * @return
     */
    private boolean sendCodeByEmail(String code,String uid,String nickName,String email){


        if(!RegexUtil.isEmail(email)){
            logger.info("邮箱格式不正确:"+email,"邮箱格式不正确:"+email,null);
            return false;
        }

        boolean success =  updateLoginPwd(code,uid);

        if(!success){
            return false;
        }

        if (StringUtils.isNotBlank(email)) {

            Map<String,Object> dataMap = new HashMap<String, Object>();
            dataMap.put("nickname",nickName);
            dataMap.put("pwd",code);

            success = UserDmdeliveryClient.sendMailResetPwd(dataMap, email);
        }

        return success;
    }

    /**
     * 检查邮件格式是否正确
     *
     * @param request
     * @return
     */
        @RequestMapping("/checkMail")
    @ResponseBody
    public boolean checkMail(HttpServletRequest request){
        return RegexUtil.isEmail(request.getParameter("mail"));
    }

    @RequestMapping("/checkPhone")
    @ResponseBody
    public boolean checkPhone(HttpServletRequest request){
        return RegexUtil.isPhone(request.getParameter("phone"));
    }

    /**
     * 通过手机发送登陆密码
     *
     * @param code
     * @param uid
     * @param nickName
     * @param mobile
     * @return
     */
    private boolean sendCodeByMobile(String code,String uid,String nickName,String mobile){

        if(!RegexUtil.isPhone(mobile)){
            logger.info("手机格式不正确:"+mobile,"手机格式不正确:"+mobile,null);
            return false;
        }



        boolean success =  updateLoginPwd(code,uid);

        if(!success){
            return false;
        }

        HashMap<String, String> msgMap = new HashMap<String, String>();
        msgMap.put("code", code);
        msgMap.put("nickName", nickName);

        String msg = MsgTemplete.PASSWORD.getMsg(msgMap);

        if (StringUtils.isNotEmpty(mobile) && StringUtils.isNotEmpty(msg)) {
            success = BaseMailAndMobileClient.sendMobile(mobile,msg);
        }

        return success;
    }

    /**
     * 发送密码到手机或者邮箱
     *
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
    public void sendCode(Model model, HttpServletRequest request, HttpServletResponse response) {


        String uid = ServletRequestUtils.getStringParameter(request, "uid", "");
        String type = ServletRequestUtils.getStringParameter(request, "submitType", "");
        boolean success = false;
        if(StringUtils.isNotBlank(uid) && StringUtils.isNotBlank(type)){

            String sessionId = request.getSession().getId();

            UserPassport user = UserPassportClient.getPassportByUid(Integer.parseInt(uid));

            String password = null;
            try{
                password = ShardJedisTool.getInstance().get(JedisKey.base$validcode, sessionId);
            }catch(JedisClientException e){
                logger.error(e, "ShardJedisTool.getInstance().get(JedisKey.base$validcode, sessionId): {sessionId:"+sessionId+"}", WebUtils.getIpAddr(request));
            }
            try {
                if (StringUtils.isNotBlank(password)) {

                    if ( "mobile".equals(type)) {

                        success = sendCodeByMobile(password,uid,user.getNickName(),user.getMobile());

                    } else if( "email".equals(type)) {

                        success = sendCodeByEmail(password,uid,user.getNickName(),user.getEmail());

                    } else  if( "mobile-manual".equals(type)){

                        success = sendCodeByMobile(password,uid,user.getNickName(),request.getParameter("mobile-manual"));

                    }else if( "email-manual".equals(type)){

                        success = sendCodeByEmail(password,uid,user.getNickName(),request.getParameter("email-manual"));

                    }
                }

            } catch (Exception e) {
                logger.error(e,e.getMessage(),WebUtils.getIpAddr(request));
                throw  new WebRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT);
            }
        }

        model.addAttribute("uid", uid);
        model.addAttribute("success", success);
        String statusCode = "300";
        String statusMsg = "操作失败";
        if (success) {
            statusCode = "200";
            statusMsg = "操作成功";
        }
        StageHelper.writeDwzSignal(statusCode, statusMsg, "dlg_page8888", StageHelper.DWZ_CLOSE_CURRENT, "", response);
    }


    /**
     * 发送站内信时的内容填写页面
     *
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/message")
    public String message(Model model, HttpServletRequest request, HttpServletResponse response) {
        String sendMore = ServletRequestUtils.getStringParameter(request, "sendMore", "");
        model.addAttribute("sendMore", sendMore);
        if (StringUtils.isNotBlank(sendMore) && "true".equals(sendMore)) {
            return "/mms/passport/message";
        }
        String uid = ServletRequestUtils.getStringParameter(request, "uid", "");
        if (StringUtils.isNotEmpty(uid)) {
            Integer uidd = Integer.parseInt(uid);
            //获取用户信息
            UserPassport user = UserPassportClient.getPassportByUid(uidd);
            model.addAttribute("pc", user);
        }
        return "/mms/passport/message";
    }


    @RequestMapping(value = "/editBlackUser")
    public String editBlackUser() {
        return "/mms/passport/editBlack";
    }

    @RequestMapping(value = "/blackSubmit")
    @ResponseBody
    public boolean blackSubmit(Model model, HttpServletRequest request, HttpServletResponse response) {
        String userName = request.getParameter("userName");
        boolean successFlag = false;

        if (StringUtils.isNotBlank(userName)) {
            userName = userName.trim();
            UserPassport user = UserPassportClient.getPassportByUserName(userName);
            if (user != null) {
                UserPassport update = new UserPassport();
                update.setUid(user.getUid());
                update.setState((short) -1);
                successFlag = UserPassportClient.update(update);

            }
        }

        return successFlag;

    }


    /**
     * 站内信的提交发送
     *
     * @param model
     * @param request
     * @param response
     */
    @RequestMapping(value = "/messageSubmit", method = RequestMethod.POST)
    public void messageSubmit(Model model, HttpServletRequest request, HttpServletResponse response) {

        String statusCode = "300";
        String statusMsg = "操作失败";

        String sendMore = ServletRequestUtils.getStringParameter(request, "sendMore", "");
        model.addAttribute("sendMore", sendMore);

        BksAdmin bksAdmin = StageHelper.getLoginUser(request).getBksAdmin();

        String msgContent = ServletRequestUtils.getStringParameter(request, "msgContent", "");

        if (StringUtils.isBlank(msgContent)) {
            StageHelper.writeDwzSignal(statusCode, statusMsg, "dlg_page_message", StageHelper.DWZ_CLOSE_CURRENT, "", response);
            return;
        }

        if (StringUtils.isNotBlank(sendMore) && "true".equals(sendMore)) {

            int[] uids = ServletRequestUtils.getIntParameters(request, "uids");

            if (uids != null && uids.length > 0) {


                int[] uidsArray = new int[uids.length];

                int i = 0;
                for (int uid : uids) {
                    uidsArray[i++] = uid;
                }

                boolean isUc = UserStationMsgClient.saveSendMsgs(bksAdmin.getAdminId(), bksAdmin.getAdminName(), uidsArray, TextUtil.cleanHTML(msgContent));
                if (isUc) {
                    statusCode = "200";
                    statusMsg = "操作成功";
                }
            }
            StageHelper.writeDwzSignal(statusCode, statusMsg, "dlg_page_message", StageHelper.DWZ_CLOSE_CURRENT, "", response);
            return;
        }

        String uid = ServletRequestUtils.getStringParameter(request, "uid", "");

        String[] ids = ServletRequestUtils.getStringParameters(request, "ids");


        UserStationMsg msg = new UserStationMsg();
        msg.setAdminId(bksAdmin.getAdminId());
        msg.setAdminName(bksAdmin.getAdminName());
        msg.setContent(ServletRequestUtils.getStringParameter(request, "msgContent", ""));
        msg.setUid(Integer.parseInt(uid));
        msg.setNickName(ServletRequestUtils.getStringParameter(request, "nickName", ""));
        msg.setMsgType((short) 1);

        int rs = UserStationMsgClient.saveUserStationMsg(msg);

        if (rs > 0) {
            statusCode = "200";
            statusMsg = "操作成功";
        }
        StageHelper.writeDwzSignal(statusCode, statusMsg, "dlg_page_message", StageHelper.DWZ_CLOSE_CURRENT, "", response);
    }

    @RequestMapping(value = "/randomShamUser")
    @ResponseBody
    public UserPassport randomShamUser() {
    	return UserPassportClient.getRandomShamUser();
    }
}

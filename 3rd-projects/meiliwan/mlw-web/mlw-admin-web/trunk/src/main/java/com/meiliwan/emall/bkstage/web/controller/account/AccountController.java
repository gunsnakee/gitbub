package com.meiliwan.emall.bkstage.web.controller.account;

import com.meiliwan.emall.account.bean.AccountCard;
import com.meiliwan.emall.account.bean.AccountWallet;
import com.meiliwan.emall.account.bean.CardOptRecord;
import com.meiliwan.emall.account.bean.WalletOptRecord;
import com.meiliwan.emall.account.bo.AccountBizLogOp;
import com.meiliwan.emall.account.client.AccountWalletClient;
import com.meiliwan.emall.account.client.GiftCardClient;
import com.meiliwan.emall.account.client.WalletOptRecordClient;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.util.DateUtil;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 钱包
 * Created by jiawu.wu on 13-6-20.
 */
@Controller
@RequestMapping("/account")
public class AccountController {

    /**
     * 钱包余额
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/info")
    public String  info(Model model, HttpServletRequest request,HttpServletResponse response) {
        Integer uid = Integer.parseInt(request.getParameter("uid"));
        AccountWallet wall = AccountWalletClient.getAccountWalletByUid(uid);
        model.addAttribute("uid",uid);
        model.addAttribute("ob",wall);
        return uselist(uid, model, request, response);
    }



    /**
     * 使用记录
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/uselist",method= RequestMethod.POST)
    public String  uselist(Integer uid,Model model,HttpServletRequest request,HttpServletResponse response){
        if(uid==null){
            uid = Integer.parseInt(request.getParameter("uid"));
        }
        PageInfo pageInfo = StageHelper.getPageInfo(request);
        int optType = ServletRequestUtils.getIntParameter(request, "opt", 1);
        int queryTime = ServletRequestUtils.getIntParameter(request,"time",1);

        WalletOptRecord queryParam=new WalletOptRecord();
        queryParam.setUid(uid);
        queryParam.setSuccessFlag(1);


        //拼凑条件字符串
        StringBuilder stringBuilder=new StringBuilder();
        if(queryTime == 2){
            String time = DateUtil.timeAddToStr(new Date(), -93, TimeUnit.DAYS);
            stringBuilder.append(" opt_time >=' ").append(time).append(" '");
        }else if(queryTime == 3){
            String time = DateUtil.timeAddToStr(new Date(),-93, TimeUnit.DAYS);
            stringBuilder.append(" opt_time <=' ").append(time).append(" '");
        }else{
            String time = DateUtil.timeAddToStr(new Date(),-31, TimeUnit.DAYS);
            stringBuilder.append(" opt_time >=' ").append(time).append(" '");
        }

        if(optType == 2){
            stringBuilder.append(" and ").append(" (opt_type='").append(AccountBizLogOp.FREEZE_MONEY.toString()).append(" '");
            stringBuilder.append(" or ").append(" opt_type='").append(AccountBizLogOp.SUB_MONEY.toString()).append(" ')");
        }else if(optType == 3){
            stringBuilder.append(" and ").append(" opt_type='").append(AccountBizLogOp.ADD_MONEY.toString()).append(" '");
        }else if(optType == 4){
            stringBuilder.append(" and ").append(" (opt_type='").append(AccountBizLogOp.REFUND_FROM_FREEZE.toString()).append(" '");
            stringBuilder.append(" or ").append(" opt_type='").append(AccountBizLogOp.REFUND_FROM_GATEWAY.toString()).append(" ')");
        }

        PagerControl<WalletOptRecord> payAndChargePc= WalletOptRecordClient.getRecordByPage(pageInfo, queryParam, stringBuilder.toString());

        model.addAttribute("pc", payAndChargePc);
        model.addAttribute("opt",optType);
        model.addAttribute("time",queryTime);
        model.addAttribute("uid",uid);
        model.addAttribute("page",pageInfo.getPage());
        return "/account/info";

       // StageHelper.writeDwzSignal("200",null, "64", StageHelper.DWZ_FORWARD, "/account/info", response);

    }
    
    
    @RequestMapping(value = "/giftCardInfo")
    public String  giftCardInfo(Model model, HttpServletRequest request,HttpServletResponse response) {
    	
    	Integer uid = Integer.parseInt(request.getParameter("uid"));
    	AccountCard accountCard = GiftCardClient.getAccountCardByUid(uid);
    	model.addAttribute("uid", uid);
    	model.addAttribute("accountCard", accountCard);
    	
    	return giftCardUseList(uid, model, request, response);
    }
    
    /**
     * 礼品卡使用记录
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/giftCardUseList",method= RequestMethod.POST)
    public String  giftCardUseList(Integer uid,Model model,HttpServletRequest request,HttpServletResponse response){
        if(uid==null){
            uid = Integer.parseInt(request.getParameter("uid"));
        }
        
        PageInfo pageInfo = StageHelper.getPageInfo(request);
        int optType = ServletRequestUtils.getIntParameter(request, "opt", 1);
        int queryTime = ServletRequestUtils.getIntParameter(request,"time",1);

        //WalletOptRecord queryParam=new WalletOptRecord();
        CardOptRecord queryParam = new CardOptRecord();
        queryParam.setUid(uid);
        queryParam.setSuccessFlag(1);


        //拼凑条件字符串
        StringBuilder stringBuilder=new StringBuilder();
        if(queryTime == 2){
            String time = DateUtil.timeAddToStr(new Date(), -93, TimeUnit.DAYS);
            stringBuilder.append(" opt_time >=' ").append(time).append(" '");
        }else if(queryTime == 3){
            String time = DateUtil.timeAddToStr(new Date(),-93, TimeUnit.DAYS);
            stringBuilder.append(" opt_time <=' ").append(time).append(" '");
        }else{
            String time = DateUtil.timeAddToStr(new Date(),-31, TimeUnit.DAYS);
            stringBuilder.append(" opt_time >=' ").append(time).append(" '");
        }

        if(optType == 2){
            stringBuilder.append(" and ").append(" (opt_type='").append(AccountBizLogOp.FREEZE_MONEY.toString()).append(" '");
            stringBuilder.append(" or ").append(" opt_type='").append(AccountBizLogOp.SUB_MONEY.toString()).append(" ')");
        }else if(optType == 3){
            stringBuilder.append(" and ").append(" opt_type='").append(AccountBizLogOp.ADD_MONEY.toString()).append(" '");
        }else if(optType == 4){
            stringBuilder.append(" and ").append(" (opt_type='").append(AccountBizLogOp.REFUND_FROM_FREEZE.toString()).append(" '");
            stringBuilder.append(" or ").append(" opt_type='").append(AccountBizLogOp.REFUND_FROM_GATEWAY.toString()).append(" ')");
        }

        
        PagerControl<CardOptRecord> payAndChargePc  = GiftCardClient.getOptRecordByPage(pageInfo, queryParam, stringBuilder.toString());

        model.addAttribute("pc", payAndChargePc);
        model.addAttribute("opt",optType);
        model.addAttribute("time",queryTime);
        model.addAttribute("uid",uid);
        model.addAttribute("page",pageInfo.getPage());
        return "/account/giftCardInfo";

       // StageHelper.writeDwzSignal("200",null, "64", StageHelper.DWZ_FORWARD, "/account/info", response);

    }

    /**
     * 封锁钱包
     * @param model
     * @param request
     * @param response
     */
    @RequestMapping(value="/lock",method= RequestMethod.POST)
    public void lock(Model model,HttpServletRequest request,HttpServletResponse response){
         Integer uid = Integer.parseInt(request.getParameter("uid"));
        model.addAttribute("uid",uid);
        AccountWallet wallet = new AccountWallet();
        wallet.setUid(uid);
        wallet.setState((short)-1);
        int update = AccountWalletClient.update(wallet);
        boolean success = update>0 ? true : false;
        //StageHelper.writeDwzSignal(success  ? "200" : "300", success ? "操作成功" : "可能有部分操作失败，请仔细核对！", "dialog_account_info", StageHelper.DWZ_FORWARD, "/account/info?uid="+uid, response);
    }

    /**
     * 恢复钱包，解除封锁
     * @param model
     * @param request
     * @param response
     */
    @RequestMapping(value="/unlock",method= RequestMethod.POST)
    public void unlock(Model model,HttpServletRequest request,HttpServletResponse response){
        Integer uid = Integer.parseInt(request.getParameter("uid"));
        model.addAttribute("uid",uid);
        AccountWallet wallet = new AccountWallet();
        wallet.setUid(uid);
        wallet.setState((short)1);
        int update = AccountWalletClient.update(wallet);
        boolean success = update>0 ? true : false;
        //StageHelper.writeDwzSignal(success  ? "200" : "300", success ? "操作成功" : "可能有部分操作失败，请仔细核对！", "dialog_account_info", StageHelper.DWZ_FORWARD, "/account/info?uid="+uid, response);
    }



}

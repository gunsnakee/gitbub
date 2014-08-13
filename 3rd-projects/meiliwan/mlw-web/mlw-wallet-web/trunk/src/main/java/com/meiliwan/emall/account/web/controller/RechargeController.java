package com.meiliwan.emall.account.web.controller;

import com.google.common.base.Strings;
import com.meiliwan.emall.account.bean.AccountWallet;
import com.meiliwan.emall.account.bean.WalletOptRecord;
import com.meiliwan.emall.account.bo.AccountBizLogOp;
import com.meiliwan.emall.account.client.AccountWalletClient;
import com.meiliwan.emall.account.client.WalletOptRecordClient;
import com.meiliwan.emall.account.constants.WalletAccountConstant;
import com.meiliwan.emall.commons.bean.PayCode;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.mms.bean.UserPassport;
import com.meiliwan.emall.mms.client.UserPassportClient;
import com.meiliwan.emall.pay.constant.PayType;
import com.meiliwan.emall.pay.dto.PayParam;
import com.meiliwan.emall.pay.dto.PaymentDTO;
import com.meiliwan.emall.base.client.IdGenClient;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-6-27
 * Time: 下午9:41
 */
@Controller
@RequestMapping("/ucenter")
public class RechargeController extends BaseController{

//    private final static MLWLogger logger = MLWLoggerFactory.getLogger(RechargeController.class);
    private final static String LOGIN_URI="https://passport.meiliwan.com/user/login";

    @RequestMapping("/recharge")
    public String getRechargePage(HttpServletRequest request,HttpServletResponse response,Model model){

      UserPassport userPassport=UserPassportClient.getPassport(request,response);
      if(userPassport != null){
         model.addAttribute("nickName",userPassport.getNickName());
         AccountWallet accountWallet=AccountWalletClient.getAccountWalletByUid(userPassport.getUid());
         if(accountWallet != null){
             model.addAttribute("mlwCoin",accountWallet.getMlwCoin());
         }
         return "recharge";
      }
      return "redirect:"+LOGIN_URI;
    }

    @RequestMapping("/rechargeway/add")
    public String selectRechargeWay(HttpServletRequest request,HttpServletResponse response,Model model){
        double rechargeMoney= ServletRequestUtils.getDoubleParameter(request,"rechargeMoney",10);
       if(rechargeMoney < 10){
            rechargeMoney = 10;
        }
        model.addAttribute("rechargeMoney",rechargeMoney);
        String rechargeId=IdGenClient.getPayId();
        model.addAttribute("rechargeId",rechargeId);
        UserPassport userPassport=UserPassportClient.getPassport(request,response);
        if(userPassport != null){
            //插入记录
            WalletOptRecord walletOptRecord=new WalletOptRecord();
            walletOptRecord.setInnerNum(rechargeId);
            walletOptRecord.setMoney(new BigDecimal(rechargeMoney));
            walletOptRecord.setUid(userPassport.getUid());
            walletOptRecord.setOptType(AccountBizLogOp.ADD_MONEY.toString());
            walletOptRecord.setOptTime(new Date());
            AccountWalletClient.addOptRecord(walletOptRecord);


            //构造表单提交数据
            PaymentDTO payment = new PaymentDTO();
            payment.setOrderId(rechargeId);
            payment.setPayType(PayType.CHARGE);
            payment.setSubject("给美丽湾钱包充值");
            payment.setUid(userPassport.getUid());
            payment.setTargetUid(userPassport.getUid());
            payment.setTotalAmount(rechargeMoney);

            PayParam payParam = new PayParam();
            payParam.setAmount(rechargeMoney);
            //充值的时候是必须有的
            payParam.setPayId(rechargeId);
            payParam.setPayCode(PayCode.ALIPAY);
            payment.setPayParams(new PayParam[] { payParam });

            request.setAttribute("payment", payment);
        }

        //生成银行
       /* model.addAttribute("banks",PayCode.getThirdPayCodes());
        model.addAttribute("aliPay",PayCode.ALIPAY);*/
        return "/recharge_way";
    }

    @RequestMapping("/rechargeResult")
    public String showRechargeResult(HttpServletRequest request,HttpServletResponse response,Model model){
        double money=ServletRequestUtils.getDoubleParameter(request,"money",0);
        String innerNum=ServletRequestUtils.getStringParameter(request,"payId",null);
        int result=0;
        UserPassport user=UserPassportClient.getPassport(request,response);
        if(user != null && money != 0 && !Strings.isNullOrEmpty(innerNum)){
            WalletOptRecord  walletOptRecord= WalletOptRecordClient.getRecord(user.getUid(), innerNum);
            int payMoney= (int) (money*100);
            if(walletOptRecord!=null &&  walletOptRecord.getSuccessFlag() == 1){
                int moneyInRecord= (int)(walletOptRecord.getMoney().doubleValue()*100);
                if( payMoney == moneyInRecord){
                    result=1;
                }
            }

        }

        model.addAttribute("result",result);
        model.addAttribute("rechargeMoney",money);
        model.addAttribute("rechargeId",innerNum);
        return "/recharge_result";
    }

    @RequestMapping("/getRechargeResult")
    public void getRechargeResult(HttpServletRequest request,HttpServletResponse response,Model model){
        double money=ServletRequestUtils.getDoubleParameter(request,"money",0);
        String innerNum=ServletRequestUtils.getStringParameter(request,"payId",null);
        int result=0;
        UserPassport user=UserPassportClient.getPassport(request,response);
        if(user != null && money != 0 && !Strings.isNullOrEmpty(innerNum)){
            WalletOptRecord  walletOptRecord= WalletOptRecordClient.getRecord(user.getUid(), innerNum);
            int payMoney= (int) (money*100);
            if(walletOptRecord!=null &&  walletOptRecord.getSuccessFlag() == 1){
                int moneyInRecord= (int)(walletOptRecord.getMoney().doubleValue()*100);
                if( payMoney == moneyInRecord){
                    result=1;
                }
            }

        }
        writeJsonByObj(result,request,response);
    }


    @RequestMapping("/account/get/isToLimitTop")
    public void isToLimitTop(HttpServletRequest request,HttpServletResponse response){
        double money=ServletRequestUtils.getDoubleParameter(request,"money",0);
        Map<String,String> map = new HashMap<String,String>();
        map.put("result","false");
        UserPassport user=UserPassportClient.getPassport(request,response);
        if(user != null){
            AccountWallet accountWallet = AccountWalletClient.getAccountWalletByUid(user.getUid());
            if(accountWallet != null){
                double result = accountWallet.getMlwCoin().doubleValue() + money;
                if(result > WalletAccountConstant.WALLET_MAX_MONEY){
                   map.put("result","true");
                }
            }
        }
        WebUtils.writeJsonByObj(map,response);
    }

}

package com.meiliwan.emall.account.web.controller;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.JsonObject;
import com.meiliwan.emall.account.bean.AccountCard;
import com.meiliwan.emall.account.bean.CardOptRecord;
import com.meiliwan.emall.account.bo.AccountBizLogOp;
import com.meiliwan.emall.account.client.GiftCardClient;
import com.meiliwan.emall.account.web.vo.WalletVo;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.bean.ErrorPageCode;
import com.meiliwan.emall.commons.exception.WebRuntimeException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.NumberUtil;
import com.meiliwan.emall.commons.util.RegexUtil;
import com.meiliwan.emall.commons.web.CaptchaUtil;
import com.meiliwan.emall.commons.web.ResponseUtil;
import com.meiliwan.emall.commons.web.UserLoginUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.mms.bean.UserPassport;
import com.meiliwan.emall.mms.client.UserPassportClient;
import com.meiliwan.emall.pms.client.CardClient;
import com.meiliwan.emall.pms.dto.CardParmsDTO;
import com.meiliwan.emall.pms.util.CardStatus;
import org.springframework.ui.Model;

/**
 * 
 * @author lsf
 *
 */
@Controller
@RequestMapping("/ucenter")
public class GiftCardController extends BaseController {
	
	private static final MLWLogger LOG = MLWLoggerFactory.getLogger(GiftCardController.class);
	
	@RequestMapping("/giftcard/actview")
	public String getGiftCardActView(HttpServletRequest request,HttpServletResponse response,Model model){
		 UserPassport userPassport= UserPassportClient.getPassport(request,response);
        if( userPassport != null && userPassport.getUid() > 0){
        	return "/gift_card_view";
        }
		
		//未登录跳转到登录页面
        return "redirect:https://passport.meiliwan.com/user/login";
	}
	
	@RequestMapping("/giftcard/list")
	public String getGiftCardLists(HttpServletRequest request,HttpServletResponse response,Model model){
        /**
         *    全部 1 购物 2 充值 3 退款 4
         *    最近一个月 1 最近三个月 2 三个月之前 3
         */
        int optType = ServletRequestUtils.getIntParameter(request,"opt",1);
        int queryTime = ServletRequestUtils.getIntParameter(request,"time",1);

        int currentPageNum= ServletRequestUtils.getIntParameter(request,"page",1);
        int recordNumPerPage=10;
        UserPassport userPassport= UserPassportClient.getPassport(request,response);
        if( userPassport != null && userPassport.getUid() > 0){

            try{

                int uId=userPassport.getId();
                //获取钱包信息
                AccountCard accountCard= GiftCardClient.getAccountCardByUid(uId);
                WalletVo walletVo=WalletVo.fromCardToVo(accountCard);
                model.addAttribute("walletVo", walletVo);



                PageInfo pageInfo=new PageInfo(currentPageNum,recordNumPerPage);
                pageInfo.setOrderField(" opt_time ");
                pageInfo.setOrderDirection(" desc ");

                CardOptRecord queryParam=new CardOptRecord();
                queryParam.setUid(uId);
                queryParam.setSuccessFlag(1);
                //拼凑条件字符串
                StringBuilder stringBuilder=new StringBuilder();
                if(queryTime == 2){
                    String time = DateUtil.timeAddToStr(new Date(), -93, TimeUnit.DAYS);
                    stringBuilder.append(" opt_time >=' ").append(time).append(" '");
                }else if(queryTime == 3){
                    String time = DateUtil.timeAddToStr(new Date(), -93, TimeUnit.DAYS);
                    stringBuilder.append(" opt_time <=' ").append(time).append(" '");
                }else{
                    String time = DateUtil.timeAddToStr(new Date(), -31, TimeUnit.DAYS);
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



                PagerControl<CardOptRecord> payAndChargePc= GiftCardClient.getOptRecordByPage(pageInfo,queryParam,stringBuilder.toString());

                //判断页码范围是否超出总页数
                if(currentPageNum < 0 || (currentPageNum > payAndChargePc.getPageInfo().getAllPage()) && payAndChargePc.getPageInfo().getAllPage() > 0){
                    currentPageNum=1;
                    pageInfo.setPage(currentPageNum);
                    payAndChargePc= GiftCardClient.getOptRecordByPage(pageInfo,queryParam,stringBuilder.toString());
                }
                model.addAttribute("pc",payAndChargePc);
                model.addAttribute("opt",optType);
                model.addAttribute("time",queryTime);
                model.addAttribute("page",currentPageNum);
                return "/gift_card_list";

            }catch (Exception e){
            	LOG.error(e,"礼品卡账户 client 抛出异常"+e.getMessage(), WebUtils.getIpAddr(request));
                throw new WebRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_SERVICE);
            }

        }
        //未登录跳转到登录页面
        return "redirect:https://passport.meiliwan.com/user/login";
    }
	
	/**
	 * 礼品卡激活
	 * @param request
	 * @param response
	 * @param cardPwd 礼品卡密码
	 * @param imgCode 图片验证码
	 */
	@RequestMapping(value = "/giftcard/activate")
	public void chargeByGiftCard(HttpServletRequest request, HttpServletResponse response){
		String cardPwd = request.getParameter("cardPwd");
		String imgCode = request.getParameter("imgCode");
//		LOG.info("激活参数", "cardPwd:"+cardPwd+",imgCode:"+imgCode, WebUtils.getIpAddr(request));
		JsonObject resultObj = new JsonObject();
		if(StringUtils.isEmpty(cardPwd) || StringUtils.isEmpty(imgCode)){
			writeError(request, response, resultObj, -1, "密码或验证码不能为空！");
			return;
		}
		
		if(cardPwd.length() != 16){
			writeError(request, response, resultObj, -2, "密码长度必须为16位！");
			return;
		}
		
		if(!RegexUtil.isEnglishOrDigital(cardPwd)){
			writeError(request, response, resultObj, -3, "密码长度必须为16位的英文或数字！");
			return;
		}
		
		//对图片验证码进行校验
		boolean result = CaptchaUtil.validateImgCode(request, response, imgCode);
		if(!result){
			writeError(request, response, resultObj, -4, "验证码不正确！");
			return;
		}
		
		int userId = UserLoginUtil.getLoginUid(request, response);
		String userName = UserLoginUtil.getNickName(request, response);
		//调用礼品卡接口进行激活
		CardParmsDTO cardParams = new CardParmsDTO();
		cardParams.setUserId(userId);
		cardParams.setPassword(cardPwd);
		cardParams.setUserName(userName);
		
		//查看用户是否已经激活超过上限
		double isMax = GiftCardClient.isCardAddMax(userId);
		if(isMax == -20){
			writeError(request, response, resultObj, -5, "一天只能激活20张礼品卡，请稍后再激活");
			LOG.warn("gift card charge error", "errorCode:" + isMax +",cardPwd:" + cardPwd + ",userId:" + userId + ",userName:" + userName ,WebUtils.getIpAddr(request));
			return ;
		}
		
		Map<String, Object> resultMap = CardClient.activeCard(cardParams);
		
		if(resultMap == null || resultMap.size() <= 0){
			writeError(request, response, resultObj, -5, "未知错误，请联系管理员");
			return;
		}
		
		Object sttObj = resultMap.get("status");
		if(sttObj == null || !CardStatus.SUCCESS.name().equals(sttObj.toString())){
			if(sttObj != null && CardStatus.LIMITACTNUM.equals(CardStatus.valueOf(sttObj.toString()))){
				writeError(request, response, resultObj, -5, "本卡为活动礼品卡，最多激活"+NumberUtil.format(Double.valueOf(resultMap.get("actNum").toString()), "0")+"张");
			}else{
				writeError(request, response, resultObj, -5, sttObj == null ? "激活失败" : CardStatus.valueOf(sttObj.toString()).getDesc());
			}
			return;
		}
		
		if(resultMap.get("price") == null || resultMap.get("cardId") == null){
			writeError(request, response, resultObj, -5, sttObj == null ? "激活失败" : CardStatus.valueOf(sttObj.toString()).getDesc());
			LOG.warn("gift card activate error", "cardPwd:" + cardPwd + ",userId:" + userId + ",userName:" + userName + ",price:" + resultMap.get("price") + ",cardId:" + resultMap.get("cardId") + ",imgCode:" + imgCode , WebUtils.getIpAddr(request));
			return ;
		}
		double price = Double.valueOf(resultMap.get("price").toString());
		String cardId = resultMap.get("cardId").toString();
		
		//调用充值接口进行充值
		double ownCoin = GiftCardClient.addMoneyWithIp(userId, price, "礼品卡激活", cardId, WebUtils.getIpAddr(request));
		if(ownCoin < 0){
			if(ownCoin == -20){
				writeError(request, response, resultObj, -5, "一天只能激活20张礼品卡，请稍后再激活");
			}else{
				writeError(request, response, resultObj, -5, "礼品卡激活成功，但充值失败，请联系客服人员");
			}
			LOG.warn("gift card charge error", "errorCode:" + ownCoin +",cardPwd:" + cardPwd + ",userId:" + userId + ",userName:" + userName + ",price:" + resultMap.get("price") + ",cardId:" + resultMap.get("cardId") + ",imgCode:" + imgCode , WebUtils.getIpAddr(request));
			return ;
		}
		
		resultObj.addProperty("state", 0);
		resultObj.addProperty("msg", "激活成功");
		resultObj.addProperty("chargeCoin", price);
		resultObj.addProperty("ownCoin", ownCoin);
		ResponseUtil.writeJsonByObj(resultObj, request, response);
	}
	
	private void writeError(HttpServletRequest request, HttpServletResponse response, JsonObject resultObj, int code, String msg){
		resultObj.addProperty("state", !StringUtils.isBlank(String.valueOf(code)) ? code : -5);
		resultObj.addProperty("msg", !StringUtils.isBlank(msg) ? msg : "未知错误，请联系管理员");
		ResponseUtil.writeJsonByObj(resultObj, request, response);
	}
	
	/**
	 * 查看用户是否已经激活超过上限
	 * @param request
	 * @param response
	 * @return 异常返回 -111
	 *         返回满况 -20
	 *         返回未满 >0
	 */
	@RequestMapping("/giftcard/ajax/getCardAddCounts")
	public void getCardAddCounts(HttpServletRequest request,HttpServletResponse response){
		String uidStr = request.getParameter("uid");
		Integer uid = null ;
		if(!StringUtils.isBlank(uidStr)){
			uid = Integer.parseInt(uidStr);
		}
		if(uid == null || uid < 0){
			uid = UserLoginUtil.getLoginUid(request, response);
		}
		
		double isMax = GiftCardClient.isCardAddMax(uid);
		JsonObject resultObj = new JsonObject();
		//查看用户是否已经激活超过上限
		try{
			if(isMax == -20){
				resultObj.addProperty("result", isMax);
				resultObj.addProperty("message", "用户激活已达到上限！");
				writeJsonByObj(resultObj, request, response);
			}else{
				resultObj.addProperty("result", isMax);
				writeJsonByObj(resultObj, request, response);
			}
			
		}catch (Exception e) {
			LOG.error(e, "uid:" + uid + ",isMax:" +isMax, WebUtils.getIpAddr(request));
		}
	}
	
}

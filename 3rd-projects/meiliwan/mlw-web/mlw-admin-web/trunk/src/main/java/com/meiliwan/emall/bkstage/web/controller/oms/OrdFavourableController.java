package com.meiliwan.emall.bkstage.web.controller.oms;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.bkstage.web.controller.bkstage.view.LoginUser;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.RegexUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.exception.IceServiceRuntimeException;
import com.meiliwan.emall.oms.bean.Ord;
import com.meiliwan.emall.oms.bean.OrdFavourable;
import com.meiliwan.emall.oms.bean.OrdFavourableLogs;
import com.meiliwan.emall.oms.client.OrdClient;
import com.meiliwan.emall.oms.client.OrdFavourableClient;

/**
 * 订单优惠/折扣
 * @author lzl
 */
@Controller
@RequestMapping("/oms/favourable")
public class OrdFavourableController {
	private static final MLWLogger logger = MLWLoggerFactory.getLogger(OrdFavourableController.class);
	
	/**
	 * 获取优惠方式列表
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
    public String list(HttpServletRequest request, Model model) {
		try{
			List<OrdFavourable> list = OrdFavourableClient.allList() ;
			model.addAttribute("list", list);
			
		}catch (Exception e) {
			logger.error(e, "OrdFavourableClient.allList() erro .", WebUtils.getIpAddr(request));
		}
		return "/oms/order/favourable_list";
	}
	
	/**
	 * 更新优惠方式对象
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/update")
	public String update(HttpServletRequest request, HttpServletResponse response, Model model){
		int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
		int ofId = ServletRequestUtils.getIntParameter(request, "ofId", 0);
		
		//判断是否是更新操作，是则进行更新逻辑，不是则获取更新内容展示
		if(isHandle>0){
			OrdFavourable ofav = OrdFavourableClient.getById(String.valueOf(ofId)) ;
			if(ofav == null){
				return StageHelper.writeDwzSignal("300", "操作失败请联系管理员", "10214", StageHelper.DWZ_FORWARD, "/oms/order/favourable_update", response);
			}
			String upper = ServletRequestUtils.getStringParameter(request, "upper", null);
			String lower = ServletRequestUtils.getStringParameter(request, "lower", null);
			// 折扣极限范围：1%-100% 减价格极限范围：0%-99%
			if("ORD_DISCOUNT".equals(ofav.getType()) 
					&& (!RegexUtil.isInt(upper) || !RegexUtil.isInt(lower) || Integer.parseInt(lower) < 1 || Integer.parseInt(upper) >100 || Integer.parseInt(lower) > Integer.parseInt(upper))){
				return StageHelper.writeDwzSignal("300", "折扣极限范围：1%-100%，且下限不可大于上限，请检查", "10214", StageHelper.DWZ_FORWARD, "/oms/order/favourable_update", response);
				
			}else if("ORD_MINUS".equals(ofav.getType())
					&& (!RegexUtil.isInt(upper) || !RegexUtil.isInt(lower) || Integer.parseInt(lower) < 0 || Integer.parseInt(upper) >99 || Integer.parseInt(lower) > Integer.parseInt(upper))){
				return StageHelper.writeDwzSignal("300", "减价格极限范围：0%-99%，且下限不可大于上限，请检查", "10214", StageHelper.DWZ_FORWARD, "/oms/order/favourable_update", response);
			}
			LoginUser loginUser = StageHelper.getLoginUser(request);
			OrdFavourable of = new OrdFavourable() ;
			of.setId(ofId);
			of.setUpperDiscount(Integer.parseInt(upper));
			of.setLowerDiscount(Integer.parseInt(lower));
			if(loginUser != null){
				of.setOpteratorId(loginUser.getBksAdmin().getAdminId());
				of.setOpteratorName(loginUser.getBksAdmin().getAdminName());
//				of.setOpteratorType(loginUser.getBksAdmin().getDepartment());
			}
			try{
				boolean updateState = OrdFavourableClient.update(of);
				if (updateState) {
                    return StageHelper.writeDwzSignal("200", "修改成功", "10214", StageHelper.DWZ_CLOSE_CURRENT, "/oms/favourable/list", response);
                } else {
                    return StageHelper.writeDwzSignal("300", "操作失败请联系管理员", "10214", StageHelper.DWZ_FORWARD, "/oms/order/favourable_update", response);
                }
				
			}catch (IceServiceRuntimeException e) {
                logger.error(e, "isHandle:" + isHandle + ",ordFavourable_id:" + ofId, WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "操作失败请联系管理员", "10214", StageHelper.DWZ_FORWARD, "/oms/order/favourable_update", response);
            } catch (Exception e) {
                logger.error(e, "isHandle:" + isHandle + ",ordFavourable_id:" + ofId, WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "操作失败请联系管理员", "10214", StageHelper.DWZ_FORWARD, "/oms/order/favourable_update", response);
            }
			
		}else{
			OrdFavourable of = OrdFavourableClient.getById(String.valueOf(ofId));
			model.addAttribute("ordFavourable", of);
		}
		
		return "/oms/order/favourable_update" ;
	}
	
	/**
	 * 修改订单价格
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/discount")
	public String discount(HttpServletRequest request, HttpServletResponse response, Model model){
		int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
		String orderId = ServletRequestUtils.getStringParameter(request, "orderId", "");
		String needToPay = ServletRequestUtils.getStringParameter(request, "needToPay", "");
		
		if(isHandle>0){
			int favId = ServletRequestUtils.getIntParameter(request, "favName", -1);
			String discountValue = ServletRequestUtils.getStringParameter(request, "discountValue", "");
			String content = ServletRequestUtils.getStringParameter(request, "content", "");
			LoginUser loginUser = StageHelper.getLoginUser(request);
			//没有选择优惠方式或折扣为空
			if(StringUtils.isBlank(String.valueOf(favId)) || StringUtils.isBlank(discountValue)){
				return StageHelper.writeDwzSignal("300", "优惠方式或折扣不完整", "10214", StageHelper.DWZ_FORWARD, "/oms/order/favourable_discount", response);
			}
			//订单不存在了
			Ord ord = OrdClient.getOrdByOrdId(orderId);
			if(ord == null){
				return StageHelper.writeDwzSignal("300", "订单不存在", "10214", StageHelper.DWZ_FORWARD, "/oms/order/favourable_discount", response);
			}
			//优惠方式不存在
			OrdFavourable of = OrdFavourableClient.getById(String.valueOf(favId)) ;
			if(of == null){
				return StageHelper.writeDwzSignal("300", "优惠方式不存在", "10214", StageHelper.DWZ_FORWARD, "/oms/order/favourable_discount", response);
			}
			//记录修改价格日志信息
			OrdFavourableLogs flogs = new OrdFavourableLogs() ;
			flogs.setOrderId(orderId);
			flogs.setFavourableId(favId);
			flogs.setFavourableName(of.getName());
			flogs.setFavourableValue(discountValue);
			
			//判断优惠类型然后重新计算discountValue
			double discount = 0.00 ;
			if("ORD_MINUS".equals(of.getType())){
//				 discount = NumberUtil.formatToDouble(((ord.getOrderSaleAmount()*100 - Double.valueOf(discountValue)*100)/(ord.getOrderSaleAmount()*100))*100, "0.00");
				 discount = ((ord.getOrderSaleAmount()*100 - Double.valueOf(discountValue)*100)/(ord.getOrderSaleAmount()*100))*100 ;
				 discountValue = String.valueOf(discount);
			}
			
			//调用oms服务更新订单价格
			boolean updateStatus = OrdFavourableClient.updateOrdFavForAdmin(orderId, discountValue);
			
			flogs.setContent(content+"( 修改结果："+updateStatus+" )");
			if(loginUser != null){
				flogs.setOpteratorId(loginUser.getBksAdmin().getAdminId());
				flogs.setOpteratorName(loginUser.getBksAdmin().getAdminName());
//				flogs.setOpteratorType(loginUser.getBksAdmin().getDepartment());
			}
			flogs.setCreateTime(new Date());
			boolean addState = OrdFavourableClient.addFavLog(flogs);
			if (updateStatus && addState) {
                return StageHelper.writeDwzSignal("200", "修改成功", "10214", StageHelper.DWZ_CLOSE_CURRENT, "", response);
            } else {
                return StageHelper.writeDwzSignal("300", "操作失败请联系管理员", "10214", StageHelper.DWZ_FORWARD, "/oms/order/favourable_discount", response);
            }
			
		}else{
			List<OrdFavourable> list = OrdFavourableClient.allList() ;
			Ord ord = OrdClient.getOrdByOrdId(orderId);
			model.addAttribute("needToPay", needToPay);
			model.addAttribute("favList", list);
			model.addAttribute("order", ord);
		}
		
		return "/oms/order/favourable_discount" ;
	}
}

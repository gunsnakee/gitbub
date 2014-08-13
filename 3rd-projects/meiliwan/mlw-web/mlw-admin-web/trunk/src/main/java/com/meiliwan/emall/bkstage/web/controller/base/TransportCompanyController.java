package com.meiliwan.emall.bkstage.web.controller.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meiliwan.emall.base.bean.BaseTransportCompany;
import com.meiliwan.emall.base.client.BaseTransportCompanyClient;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.web.WebParamterUtil;
import com.meiliwan.emall.commons.web.WebUtils;

/**
 * 物流公司
 * 
 * @author yinggao.zhuo
 * 
 */
@Controller
@RequestMapping(value = "/base/trans")
public class TransportCompanyController {

	private final MLWLogger logger = MLWLoggerFactory
			.getLogger(this.getClass());

	@RequestMapping(value = "/add")
	public String add(HttpServletRequest request, HttpServletResponse response) {

		int isHandle = ServletRequestUtils.getIntParameter(request, "handle",
				-1);
		BaseTransportCompany bean = null;
		try {
			// 判断是否访问页面还是处理 添加信息
			if (isHandle > 0) {
				bean = setFormBean(request);
				BaseTransportCompanyClient.add(bean);
				return StageHelper.dwzSuccessClose("添加物流公司成功", "118", "/base/trans/list", response);
			} 
		} catch (Exception e) {
			logger.error(e, bean, WebUtils.getIpAddr(request));
			return StageHelper.dwzFailClose("请确认参数，添加失败。",  response);
		}
		//增加页面
		return "/base/company/add";
	}

	@RequestMapping(value = "/findById")
	public String findById(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		int id = ServletRequestUtils.getIntParameter(request, "id", 0);
		try {
			BaseTransportCompany bean = BaseTransportCompanyClient.findById(id);

			model.addAttribute("bean", bean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e, String.format("id(%s)", id), WebUtils.getIpAddr(request));
			return StageHelper.dwzFailForward("操作失败。", "", "/base/trans/updateCompany", response);
		}

		return "/base/company/update";
	}

	@RequestMapping(value = "/changeState")
	public void changeState(HttpServletRequest request,
			HttpServletResponse response) {
		int id = ServletRequestUtils.getIntParameter(request, "id", 0);
		try {
			short state = (short) ServletRequestUtils.getIntParameter(request,
					"state", 0);
			short isDel = (short) ServletRequestUtils.getIntParameter(request,
					"isDel", 0);
			BaseTransportCompany bean = new BaseTransportCompany();
			bean.setId(id);
			bean.setState(state);
			if (isDel != 0) {
				bean.setIsDel(isDel);
			}
			BaseTransportCompanyClient.update(bean);
		} catch (Exception e) {
			logger.error(e, String.format("id(%s)", id), WebUtils.getIpAddr(request));
			StageHelper.dwzFailForward("操作失败。", "118", "/base/trans/list", response);
			return ;
		}
		StageHelper.dwzSuccessForward("操作成功。", "118", "/base/trans/list", response);
		
	}

	@RequestMapping(value = "/update")
	public String update(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		BaseTransportCompany bean = null;
		try {
			int adminId = StageHelper.getAdminId(request);
			if(adminId==-1){
				return StageHelper.dwzFailForward("未登录,无法操作。", "118", "/base/trans/list", response);
			}
			int id = ServletRequestUtils.getIntParameter(request, "id", 0);
			bean = setFormBean(request);
			bean.setId(id);
			BaseTransportCompanyClient.update(bean);
		} catch (Exception e) {
			logger.error(e, bean, WebUtils.getIpAddr(request));
			return StageHelper.dwzFailForward("操作失败。", "118", "/base/trans/list", response);
		}
		return StageHelper.dwzSuccessForward("操作成功。", "118", "/base/trans/list", response);
	}

	/**
	 * 设置表单提交的数据
	 */
	private BaseTransportCompany setFormBean(HttpServletRequest request) {
		String name = ServletRequestUtils.getStringParameter(request, "name",
				"");
		int cash = ServletRequestUtils.getIntParameter(request, "supportCash",
				0);
		int pos = ServletRequestUtils.getIntParameter(request, "supportPos", 0);
		int check = ServletRequestUtils.getIntParameter(request,"supportCheck", 0);
		int adminId = StageHelper.getAdminId(request);

		BaseTransportCompany bean = new BaseTransportCompany();
		bean.setAdminId(adminId);
		if(!StringUtils.isBlank(name)){
			bean.setName(name);
		}
		if(cash==1||cash==0){
			bean.setSupportCash((short) cash);
		}
		if(pos==1||pos==0){
			bean.setSupportPos((short) pos);
		}
		if(check==1||check==0){
			bean.setSupportCheck((short) check);
		}
		return bean;
	}

	private int getAdminId(HttpServletRequest request) {
		return StageHelper.getLoginUser(request).getBksAdmin().getAdminId();
	}
	
	/**
	 * 分页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request,HttpServletResponse response, Model model) {

		PagerControl<BaseTransportCompany> pc = null;
		BaseTransportCompany bean = new BaseTransportCompany();
		try {
			PageInfo pageInfo = WebParamterUtil.getPageInfo(request);
			bean.setIsDel((short) 0);
			pc = BaseTransportCompanyClient.pageByObj(bean, pageInfo, "", "");
			model.addAttribute("pc", pc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e, bean, WebUtils.getIpAddr(request));
			return StageHelper.dwzFailForward("操作失败。", "118", "/base/trans/companyList", response);
		} 
		return "/base/company/list";
	}

}

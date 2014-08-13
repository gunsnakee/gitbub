package com.meiliwan.emall.bkstage.web.controller.pms;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.bean.JsonResult;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.web.WebParamterUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.pms.bean.ProBrand;
import com.meiliwan.emall.pms.bean.ProCategory;
import com.meiliwan.emall.pms.client.ProBrandClient;
import com.meiliwan.emall.pms.client.ProCategoryClient;

/**
 * 品牌管理
 * 
 * @author yinggao.zhuo
 * 
 */
@Controller
@RequestMapping("/pms/brand")
public class BrandController {

	private final MLWLogger logger = MLWLoggerFactory
			.getLogger(this.getClass());
	
	//状态有效
		public static final short STATE_VALID = 1;

	@RequestMapping(value = "/add")
	public String add(HttpServletRequest request, HttpServletResponse response,
			Model model) {
		ProBrand bean = null;
		try {
			int isHandle = ServletRequestUtils.getIntParameter(request,
					"handle", -1);
			// 判断是否访问页面还是处理 添加信息
			if (isHandle > 0) {
				bean = setBrand(request);
				ProBrandClient.add(bean);

				return StageHelper.dwzSuccessClose("添加品牌成功", "54",
						"/pms/brand/list", response);

			} else {
				setCategory(model);
				return "/pms/brand/add";
			}
		} catch (Exception e) {
			logger.error(e, bean, WebUtils.getIpAddr(request));
			return StageHelper.dwzFailClose("操作失败", "54", "/pms/brand/list",
					response);
		}

	}

	private ProBrand setBrand(HttpServletRequest request) {
		String name = ServletRequestUtils.getStringParameter(request, "name",
				"");
		String enName = ServletRequestUtils.getStringParameter(request,
				"en_name", "");
		String otherName = ServletRequestUtils.getStringParameter(request,
				"other_name", "");
		String descp = ServletRequestUtils.getStringParameter(request, "descp",
				"");
		String logoUri = ServletRequestUtils.getStringParameter(request,
				"logo_uri", "");
		String brandUri = ServletRequestUtils.getStringParameter(request,
				"brand_uri", "");
		String firstChar = ServletRequestUtils.getStringParameter(request,
				"first_char", "");
		int category_id = ServletRequestUtils.getIntParameter(request,
				"third_category_id", 0);

		ProBrand bean = new ProBrand();
		bean.setName(name);
		bean.setEnName(enName);
		bean.setOtherName(otherName);
		bean.setBrandUri(brandUri);
		bean.setDescp(descp);
		bean.setFirstChar(firstChar);
		bean.setLogoUri(logoUri);
		bean.setState(STATE_VALID);
		bean.setCategoryId(category_id);
		return bean;
	}

	/**
	 * 获取第一级类目.第一次
	 * 
	 * @param model
	 */
	private void setCategory(Model model) {

		ProCategory category = new ProCategory();
		category.setParentId(0);
		List<ProCategory> categoryList = ProCategoryClient
				.getListByPId(category);
		model.addAttribute("categoryList", categoryList);

	}

	@RequestMapping(value = "/del")
	public void del(HttpServletRequest request, HttpServletResponse response) {
		int id = -1;
		try {
			id = ServletRequestUtils.getIntParameter(request, "brand_id", -1);
			if (id <= 0) {
				StageHelper.dwzFailForward("参数不正确,操作失败", "54", "/pms/brand/list",
						response);
				return ;
			}
			JsonResult result = ProBrandClient.del(id);
			if(result!=null&&result.getCode()!=null&&result.getCode().equals("RELEVANCE")){
				StageHelper.dwzFailForward("商品关联此品牌不能删除", "54", "/pms/brand/list", response);
				return ;
			}
		} catch (Exception e) {
			logger.error(e, String.format("BrandId(%s)", id),
					WebUtils.getIpAddr(request));
			StageHelper.dwzFailForward("操作失败", "54", "/pms/brand/list",response);
			return;
		}
		StageHelper.dwzSuccessForward("操作成功", "54", "/pms/brand/list", response);
	}

	@RequestMapping(value = "/update")
	public String update(HttpServletRequest request,
			HttpServletResponse response, Model model) {

		int isHandle = ServletRequestUtils.getIntParameter(request, "handle",
				-1);
		int id = ServletRequestUtils.getIntParameter(request, "brand_id", 0);
		// 判断是否访问页面还是处理 添加信息
		try {
			if (isHandle > 0) {
					ProBrand bean = setBrand(request);
					bean.setBrandId(id);
					ProBrandClient.update(bean);
				return StageHelper.dwzSuccessClose("更新品牌成功", "54",
						"/pms/brand/list", response);
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e, String.format("BrandId(%s)", id), WebUtils.getIpAddr(request));
			return StageHelper.dwzFailForward("更新品牌失败", "54",
					"/pms/brand/list", response);
		}
		
		return findById(request, model);

	}

	private String findById(HttpServletRequest request, Model model) {

		int brandId = ServletRequestUtils.getIntParameter(request, "brand_id",
				0);
		try {
			ProBrand bean = ProBrandClient.findById(brandId);
			model.addAttribute("bean", bean);
			setCategory(model);
			//这一大段代码可以封装成一个js插件
			ProCategory third = ProCategoryClient.getProCategoryById(bean
					.getCategoryId());
			if(third==null){
				model.addAttribute("third", new ProCategory());
				return "/pms/brand/update";
			}else{
				model.addAttribute("third", third);
			}
			ProCategory second = ProCategoryClient.getProCategoryById(third
					.getParentId());
			if(second==null){
				model.addAttribute("second", new ProCategory());
				return "/pms/brand/update";
			}else{
				model.addAttribute("second", second);
			}
			List<ProCategory> secondlist = ProCategoryClient.getListByPId(second);
			model.addAttribute("secondlist", secondlist);
			ProCategory first = ProCategoryClient.getProCategoryById(second
					.getParentId());
			model.addAttribute("first", first);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e, String.format("BrandId(%s)", brandId), WebUtils.getIpAddr(request));
		}

		return "/pms/brand/update";
	}

	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request,HttpServletResponse response, Model model) throws IOException {

        int type = ServletRequestUtils.getIntParameter(request,"type",0);
        if (type == 1){
            String brandName = ServletRequestUtils.getStringParameter(request,"inputValue","");
            List<ProBrand> list = ProBrandClient.getListByName(brandName,true);
            StringBuffer buffer = new StringBuffer();
            buffer.append("[");
            boolean first = true;
            if (list!=null && list.size()>0){
                for (ProBrand brand: list){
                   if (first){
                       buffer.append("{\"id\":\""+brand.getBrandId()+"\",\"name\":\""+brand.getName()+"\"}");
                       first = false;
                   }else {
                       buffer.append(",{\"id\":\""+brand.getBrandId()+"\",\"name\":\""+brand.getName()+"\"}");
                   }
                }
            }
            buffer.append("]");
            StageHelper.writeString(buffer.toString(),response);
            return null;
        }else if (type == 2){
            String name = ServletRequestUtils.getStringParameter(request,"name","");
            boolean suc = false;
            if (StringUtils.isNotEmpty(name)){
                List<ProBrand> list = ProBrandClient.getListByName(name,false);
                if (list!=null&&list.size()>0){
                }else {
                    suc = true;
                }
            }
            response.setHeader("Content-Language", "zh-cn");
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("application/json; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(suc);
            return null;
        }else {
            ProBrand bean = new ProBrand();
            try {
                bean.setState(STATE_VALID);
                PageInfo pageInfo = WebParamterUtil.getPageInfo(request);
                PagerControl<ProBrand> pc = ProBrandClient.pageByBrand(bean,
                        pageInfo);
                model.addAttribute("pc", pc);
            } catch (Exception e) {
                logger.error(e, bean, WebUtils.getIpAddr(request));
            }
            return "/pms/brand/list";
        }
	}

}

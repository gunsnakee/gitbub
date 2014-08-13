package com.meiliwan.emall.commons.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestUtils;

import com.meiliwan.emall.commons.PageInfo;

/**
 * Controller参数的工具类
 * @author rubi
 *
 */
public class WebParamterUtil {
	
	/**
	 * 通过request获取pageInfo
	 * 当前页的参数pageNum
	 * 页大小的参数numPerPage
	 * @param request
	 * @return
	 */
	public static PageInfo getPageInfo(HttpServletRequest request) {
		// TODO Auto-generated method stub
		int page = ServletRequestUtils.getIntParameter(request, "pageNum", 0);
		int pagesize = ServletRequestUtils.getIntParameter(request, "numPerPage", 0);
		PageInfo pageInfo = new PageInfo();
		if(pagesize>0){
			pageInfo.setPagesize(pagesize);
		}//要先setPageSize
		pageInfo.setPage(page);
		return pageInfo;
	}
	
	public static PageInfo getPageInfo(HttpServletRequest request,int pagesize) {
		// TODO Auto-generated method stub
		int page = ServletRequestUtils.getIntParameter(request, "pageNum", 0);
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPagesize(pagesize);
		pageInfo.setPage(page);
		return pageInfo;
	}
	
	/**
	 * 此方法仅用户H5的参数情况，因为request不在携带分页参数
	 * @param params
	 * @param pagesize
	 * @return
	 */
	public static PageInfo getPageInfo(Map<String, String> params, int pagesize){
		int page = Integer.parseInt(params.get("pageNum"));
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPagesize(pagesize);
		pageInfo.setPage(page);
		return pageInfo;
	}
}

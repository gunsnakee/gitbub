package com.meiliwan.emall.account.view;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

/**
 * 处理 Referer 的 ViewResolver
 * @author jiawu.wu
 *
 */
public class RefererViewResolver implements ViewResolver, Ordered {

	// 以referer:起始的viewName，将被此ViewResolver处理
	public static final String REFERER_PROFIX = "referer:";

	@Override
	public int getOrder() {
		return Integer.MAX_VALUE - 1;// 这个ViewResolver排在UrlBasedViewResolver前一个的位置
	}

	@Override
	public View resolveViewName(String viewName, Locale locale){
		if (!viewName.startsWith(REFERER_PROFIX)) {
			return null;
		}

		return new RefererRedirectView();
	}

}

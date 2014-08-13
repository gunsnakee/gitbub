package com.meiliwan.emall.commons.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 转换器 1:将JavaBean 转换成Map、JSONObject 2:将JSONObject 转换成Map
 * 
 * @author yinggao.zhuo
 */
public class BeanConverter {
	/**
	 * 将javaBean转换成Map
	 * 
	 * @param javaBean
	 *            javaBean
	 * @return Map对象
	 */
	public static Map<String, Object> toMap(Object javaBean) {
		Map<String, Object> result = new HashMap<String, Object>();
		Method[] methods = javaBean.getClass().getDeclaredMethods();

		for (Method method : methods) {
			try {
				if (method.getName().startsWith("get")) {
					String field = method.getName();
					field = field.substring(field.indexOf("get") + 3);
					field = field.toLowerCase().charAt(0) + field.substring(1);

					Object value = method.invoke(javaBean, (Object[]) null);
					result.put(field, null == value ? "" : value.toString());
				}
			} catch (Exception e) {
			}
		}

		return result;
	}

	public static void main(String[] args) {
		
		/*ProBrand brand = new ProBrand();
		brand.setName("杰克琼斯");
		brand.setEnName("jack jomes");
	    brand.setBrandUri("http://list.jd.com/1315-1342-1348-71814-0-0-0-0-0-0-1-1-1-1.html");
	    brand.setDescp("Jack & Jones杰克琼斯，欧洲服饰品牌，是丹麦BESTSELLER集团旗下的主要品牌之一。");
	    brand.setLogoUri("http://baike.baidu.cn/picview/494754/494754/0/9c16fdfaaf51f3def0bac1fb94eef01f3a297920.html#albumindex=4&picindex=0");
	    brand.setOtherName("JJ");
	    brand.setFirstChar("J");
	    Map<String, Object> map = BeanConverter.toMap(brand);
		System.out.println(map);*/
	}
}
package com.meiliwan.emall.commons.web;

import org.testng.annotations.Test;

public class CaptchaUtilTest {
	
	@Test
	public void testCaptchaUtil(){
		String url = "https://passport.meiliwan.com/user/captcha/get?date=1387778140402" ;
		String code = "skwxy" ;
		String _captcha_key = "bc89cb6d6f5e993a971e9bd130c97b961387521790813A6685" ;
		try{
			System.out.println(CaptchaUtil.validate(url, code, _captcha_key));
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}

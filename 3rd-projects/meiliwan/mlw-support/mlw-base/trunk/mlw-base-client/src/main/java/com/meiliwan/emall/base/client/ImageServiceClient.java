package com.meiliwan.emall.base.client;

import com.google.gson.JsonObject;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.service.BaseService;


/**
 * 
 * @author lsf
 *
 */
public class ImageServiceClient {

	/**
	 * 
	 * @param primFileName 原图相对路径，即/file/uploadImg 这个API返回的 primFileName 属性
	 * @param objId 如果是头像，则为用户id；如果是商品，则为商品id
	 * @param sizeRule 需要压缩的尺寸
	 * @param index 如果是头像，传null即可； 如果是商品，则传商品图的序号即可
	 * @return 如果压缩成功，则返回true；否则返回false；
	 */
	public static String replaceImg(String primFileName,
			String objId, String sizeRule, int index){
		
		JsonObject resultObj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE, JSONTool.buildParams("imageService/replaceImg", primFileName, objId, sizeRule, index));
		
		return resultObj.get(BaseService.RESULT_OBJ).getAsString();
	}
	
}

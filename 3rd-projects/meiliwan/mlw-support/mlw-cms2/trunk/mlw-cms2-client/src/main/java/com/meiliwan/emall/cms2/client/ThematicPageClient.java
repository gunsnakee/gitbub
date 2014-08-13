package com.meiliwan.emall.cms2.client;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.cms2.bean.ThematicModel;
import com.meiliwan.emall.cms2.bean.ThematicPage;
import com.meiliwan.emall.cms2.bean.ThematicPageModel;
import com.meiliwan.emall.cms2.bean.ThematicTemplate;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;

/**
 * User: wuzixin
 * Date: 14-4-14
 * Time: 上午11:23
 */
public class ThematicPageClient {
	
	 private final static MLWLogger  logger = MLWLoggerFactory.getLogger(ThematicPageClient.class);
    /**
     * 增加专题页
     *
     * @param page
     * @return
     */
    public static int addPage(ThematicPage page) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
                JSONTool.buildParams("thematicPageService/addPage", page));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 增加专题页对应区域，列如：图片区或者商品区
     *
     * @param model 专题和模块的关系，也就是页面区域
     */
    public static int addPageModel(ThematicPageModel model) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
                JSONTool.buildParams("thematicPageService/addPageModel", model));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 修改专题页
     *
     * @param page
     * @return
     */
    public static int updatePage(ThematicPage page) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
                JSONTool.buildParams("thematicPageService/updatePage", page));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 修改专题页与模块关系表，也就是pageModel表
     *
     * @param model
     */
    public static int updatePageModel(ThematicPageModel model) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
                JSONTool.buildParams("thematicPageService/updatePageModel", model));
        return obj.get("resultObj").getAsInt();
    }
    
    public static int updateThematicModel(ThematicModel model) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
                JSONTool.buildParams("thematicPageService/updateThematicModel", model));
        return obj.get("resultObj").getAsInt();
    }
    
    /**
     * 修改专题页jsonData 字段数据
     *
     * @param pageId
     * @param json
     */
    public static int updataPagdJsonData(int pageId, String json) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
                JSONTool.buildParams("thematicPageService/updataPagdJsonData", pageId, json));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 修改专题页与区域关系表的json数据
     *
     * @param id
     * @param json
     */
    public static int updatePageModelJsonData(int id, String json) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
                JSONTool.buildParams("thematicPageService/updatePageModelJsonData", id, json));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 根据专题页ID，修改专题包含的商品IDS
     *
     * @param pageId
     * @param ids
     */
    public static int updataPageProIds(int pageId, String ids) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
                JSONTool.buildParams("thematicPageService/updataPageProIds", pageId, ids));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 根据关系ID，删除专题页相关区域
     *
     * @param id
     * @return
     */
    public static int updatePMHide(int id) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
                JSONTool.buildParams("thematicPageService/updatePMHide", id));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 通过pageId获取专题实体
     *
     * @param pageId
     */
    public static ThematicPage getPageById(int pageId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
                JSONTool.buildParams("thematicPageService/getPageById", pageId));
        return new Gson().fromJson(obj.get("resultObj"), ThematicPage.class);
    }

    /**
     * 获取专题列表，分页查询
     *
     * @param page
     * @param pageInfo
     * @param order_name
     * @param order_form
     */
    public static PagerControl<ThematicPage> getPageByObj(ThematicPage page, PageInfo pageInfo, String order_name, String order_form) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
                JSONTool.buildParams("thematicPageService/getPageByObj", page, pageInfo, order_name, order_form));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<ThematicPage>>() {
        }.getType());
    }

    /**
     * 获取全部专题模板列表
     */
    public static List<ThematicTemplate> getTemplateList() {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
                JSONTool.buildParams("thematicPageService/getTemplateList"));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<ThematicTemplate>>() {
        }.getType());
    }

    /**
     * 删除专题页
     *
     * @param pageId
     */
    public static int delPage(int pageId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
                JSONTool.buildParams("thematicPageService/delPage", pageId));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 根据专题ID获取专题对应的区域列表
     *
     * @param pageId
     */
    public static List<ThematicPageModel> getPMListByPageId(int pageId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
                JSONTool.buildParams("thematicPageService/getPMListByPageId", pageId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<ThematicPageModel>>() {
        }.getType());
    }

    /**
     * 根据专题页与区域关系id，获取页面相关信息,返回PageMode实体
     *
     * @param id
     */
    public static ThematicPageModel getPageModeById(int id) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
                JSONTool.buildParams("thematicPageService/getPageModeById", id));
        return new Gson().fromJson(obj.get("resultObj"), ThematicPageModel.class);
    }

    /**
     * 根据模块实体，获取模块列表
     *
     * @param model
     */
    public static List<ThematicModel> getModelListByEntity(ThematicModel model) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
                JSONTool.buildParams("thematicPageService/getModelListByEntity", model));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<ThematicModel>>() {
        }.getType());
    }
    
    /**
     * 根据专题页名称生成专题页,返回生成状态
     */
    public static ThematicModel getModelByModelId(int modelId){
    	JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
    			JSONTool.buildParams("thematicPageService/getModelByModelId", modelId));
    	return new Gson().fromJson(obj.get("resultObj"), new TypeToken<ThematicModel>() {
        }.getType());
    }
    
    public static List<ThematicModel> getModelListByType(int type) {
    		ThematicModel model = new ThematicModel();
    		model.setType(type);
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
                JSONTool.buildParams("thematicPageService/getModelListByEntity", model));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<ThematicModel>>() {
        }.getType());
    }
    
    /**
     * 根据专题页名称生成专题页,返回生成状态
     */
    public static ThematicModel getModelByModelName(String modelName){
    	JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
    			JSONTool.buildParams("thematicPageService/getModelByModelName", modelName));
    	return new Gson().fromJson(obj.get("resultObj"), new TypeToken<ThematicModel>() {
        }.getType());
    }
    
    /**
     * 根据专题页名称生成专题页,返回生成状态
     */
    public static boolean getThematicPageHtml(String thematicPageName){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
                JSONTool.buildParams("thematicPageService/getThematicPageHtml", thematicPageName));
        return obj.get("resultObj").getAsBoolean();
    }
   
    /**
     * 根据专题页名称生成专题页,返回生成状态
     */
    public static void getThematicPageHtmlRedis(){
    		try {
			IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
					JSONTool.buildParams("thematicPageService/getThematicPageHtmlRedis"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e, null, null);
		}
    }

	public static ThematicPage getPageByEnName(String enName) {
		// TODO Auto-generated method stub
		JsonObject obj =  IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
    			JSONTool.buildParams("thematicPageService/getPageByEnName",enName));
		return new Gson().fromJson(obj.get("resultObj"), ThematicPage.class);
	}

}

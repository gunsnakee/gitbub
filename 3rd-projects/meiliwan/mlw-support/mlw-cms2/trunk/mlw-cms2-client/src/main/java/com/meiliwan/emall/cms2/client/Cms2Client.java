package com.meiliwan.emall.cms2.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.cms2.bean.*;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-12-24
 * Time: 下午6:03
 * To change this template use File | Settings | File Templates.
 */
public class Cms2Client {
    private static final String SERVICE_NAME = "cms2Service/";


    /****************************************  cmsFragment *************************************/
    public static int addFragment(CmsFragment cmsFragment){
        String iceFuncName = "addFragment";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + iceFuncName, cmsFragment));
        return new Gson().fromJson(obj.get("resultObj"), Integer.class);
    }

    public static int updateFragment(CmsFragment cmsFragment){
        String iceFuncName = "updateFragment";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + iceFuncName, cmsFragment));
        return new Gson().fromJson(obj.get("resultObj"), Integer.class);
    }
    
    public static CmsFragment getFragmentByObj(CmsFragment cmsFragment){
        String iceFuncName = "getFragmentByObj";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME +iceFuncName, cmsFragment));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<CmsFragment>(){}.getType());
    }

    public static String getLastestFragmentByCacheCode(int cacheCode){
    		CmsFragment cmsFragment = new CmsFragment();
    		cmsFragment.setCacheCode(cacheCode);
    		CmsFragment obj =  getFragmentByObj(cmsFragment);
    		if(obj!=null){
    			return obj.getFragmentContent();
    		}
    		return "";
    }
    
    public static List<CmsFragment> getFragmentListByObj(CmsFragment cmsFragment,PageInfo pageInfo){
        String iceFuncName = "getFragmentListByObj";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME +iceFuncName, cmsFragment,pageInfo));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<CmsFragment>>(){}.getType());
    }

    /***************************************** cmsPage **********************************************/
    public static int addPage(CmsPage cmsPage){
        String iceFuncName = "addPage";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + iceFuncName, cmsPage));
        return new Gson().fromJson(obj.get("resultObj"), Integer.class);
    }

    public static CmsPage getPageByObj(CmsPage cmsPage){
        String iceFuncName = "getPageByObj";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME +iceFuncName, cmsPage));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<CmsPage>(){}.getType());
    }

    public static CmsPage getPageById(Integer pageId){
        String iceFuncName = "getPageById";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME +iceFuncName, pageId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<CmsPage>(){}.getType());
    }

    public static int updatePage(CmsPage cmsPage){
        String iceFuncName = "updatePage";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME +iceFuncName, cmsPage));
        return new Gson().fromJson(obj.get("resultObj"), Integer.class);
    }

    public static List<CmsPage> getPageLists(CmsPage cmsPage){
        String iceFuncName = "getPageLists";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME +iceFuncName, cmsPage));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<CmsPage>>(){}.getType());
    }

    public static List<CmsPage> blurSeachByPageName(String pageName){
        String iceFuncName = "blurSeachByPageName";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME +iceFuncName, pageName));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<CmsPage>>(){}.getType());
    }


    /****************************************** cms_page_block *************************************/
    public static int addBlock(CmsPageBlock cmsPageBlock){
        String iceFuncName = "addBlock";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + iceFuncName, cmsPageBlock));
        return new Gson().fromJson(obj.get("resultObj"), Integer.class);
    }

    public static CmsPageBlock getBlockByName(String name){
    		CmsPageBlock block = new CmsPageBlock();
    		block.setBlockName(name);
    		return getBlockByObj(block);
    }
    
    public static CmsPageBlock getBlockByObj(CmsPageBlock cmsPageBlock){
        String iceFuncName = "getBlockByObj";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME +iceFuncName, cmsPageBlock));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<CmsPageBlock>(){}.getType());
    }

    public static CmsPageBlock getBlockById(Integer blockId){
        String iceFuncName = "getBlockById";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME +iceFuncName, blockId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<CmsPageBlock>(){}.getType());
    }

    public static int updateBlock(CmsPageBlock cmsPageBlock){
        String iceFuncName = "updateBlock";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME +iceFuncName, cmsPageBlock));
        return new Gson().fromJson(obj.get("resultObj"), Integer.class);
    }

    public static List<CmsPageBlock> getBlockListByObj(CmsPageBlock cmsPageBlock){
        String iceFuncName = "getBlockListByObj";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME +iceFuncName, cmsPageBlock));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<CmsPageBlock>>(){}.getType());
    }
    /**************************************************** cms_position *************************************************************************/
    public static int addPosition(CmsPosition cmsPosition){
        String iceFuncName = "addPosition";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + iceFuncName, cmsPosition));
        return new Gson().fromJson(obj.get("resultObj"), Integer.class);
    }

    public static CmsPosition getPositionByObj(CmsPosition cmsPosition){
        String iceFuncName = "getPositionByObj";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME +iceFuncName, cmsPosition));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<CmsPosition>(){}.getType());
    }

    public static CmsPosition getPositionById(Integer positionId){
        String iceFuncName = "getPositionById";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME +iceFuncName, positionId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<CmsPosition>(){}.getType());
    }

    public static int updatePosition(CmsPosition cmsPosition){
        String iceFuncName = "updatePosition";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME +iceFuncName, cmsPosition));
        return new Gson().fromJson(obj.get("resultObj"), Integer.class);
    }

    public static List<CmsPosition> getPositionListByGroup(Integer blockId,Integer groupId){
        String iceFuncName = "getPositionListByGroup";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME +iceFuncName, blockId,groupId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<CmsPosition>>(){}.getType());
    }

    public static List<Integer> getAllGroupByBlockId(Integer blockId){
        String iceFuncName = "getAllGroupByBlockId";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME +iceFuncName,blockId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<Integer>>(){}.getType());
    }

    /*************************************************** cms_link *********************************************************/
    public static int addLink(CmsLink cmsLink){
        String iceFuncName = "addLink";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + iceFuncName, cmsLink));
        return new Gson().fromJson(obj.get("resultObj"), Integer.class);
    }

    public  static CmsLink getLinkByObj(CmsLink cmsLink){
        String iceFuncName = "getLinkByObj";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME +iceFuncName, cmsLink));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<CmsLink>(){}.getType());
    }
    public  static CmsLink getLinkById(int id){
    	String iceFuncName = "getLinkById";
    	JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME +iceFuncName, id));
    	return new Gson().fromJson(obj.get("resultObj"), new TypeToken<CmsLink>(){}.getType());
    }

    public static List<CmsLink> getLinkList(Integer blockId,Integer groupId){
        String iceFuncName = "getLinkList";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME +iceFuncName, blockId,groupId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<CmsLink>>(){}.getType());
    }

    public static List<CmsLink> getLinkList(int blockId){
        String iceFuncName = "getLinkList";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME +iceFuncName, blockId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<CmsLink>>(){}.getType());
    }
    
    public static int updateLink(CmsLink cmsLink){
        String iceFuncName = "updateLink";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + iceFuncName, cmsLink));
        return new Gson().fromJson(obj.get("resultObj"), Integer.class);
    }


    /************ publish **********************/
    public static boolean publish(CmsFragment cmsFragment){
        String iceFuncName = "publish";
        if(cmsFragment.getCacheCode() == null || cmsFragment.getCacheCode().intValue() < 0){
            return false;
        }else{
            JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + iceFuncName, cmsFragment));
            return new Gson().fromJson(obj.get("resultObj"), Boolean.class);
        }
    }

}

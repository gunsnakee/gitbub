package com.meiliwan.emall.cms2.client;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.cms2.bean.TvPage;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;

public class TvPageClient {

	public static final String DIMENSIONAL_CODE = "DimensionalCode";
	
	
	public static PagerControl<TvPage> getPageByObj(int id,String name, PageInfo pageInfo) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
                JSONTool.buildParams("tvPageService/getPageByObj",id, name,pageInfo));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<TvPage>>() {
        }.getType());
    }
	
	public static List<TvPage> getListValid() {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
                JSONTool.buildParams("tvPageService/getListValid"));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<TvPage>>() {
        }.getType());
    }
	
	public static TvPage getById(int id) throws ServiceException{
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
                JSONTool.buildParams("tvPageService/getById",id));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<TvPage>() {
        }.getType());
    }
	
	public static void add(TvPage page) throws ServiceException{
        IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
                JSONTool.buildParams("tvPageService/add",page));
    }
	
	public static void update(TvPage page) throws ServiceException{
        IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
                JSONTool.buildParams("tvPageService/update",page));
    }
	public static void updateProIds(int id,String proIds) throws ServiceException{
		IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
				JSONTool.buildParams("tvPageService/updateProIds",id,proIds));
	}
	public static void updateUp(int id,String proId) throws ServiceException{
		IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
				JSONTool.buildParams("tvPageService/updateUp",id,proId));
	}
	public static void delPro(int id,String proId) throws ServiceException{
		IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
				JSONTool.buildParams("tvPageService/delPro",id,proId));
	}
	public static void updateDown(int id,String proId) throws ServiceException{
		IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
				JSONTool.buildParams("tvPageService/updateDown",id,proId));
	}
	
	public static void delete(int id) throws ServiceException{
        IceClientTool.sendMsg(IceClientTool.CMS2_ICE_SERVICE,
                JSONTool.buildParams("tvPageService/delete",id));
    }
	
	public static void updateDimensionalCode(String name,String imageUrl) throws JedisClientException{
        ShardJedisTool.getInstance().hset(JedisKey.global$inc,DIMENSIONAL_CODE,"",imageUrl+","+name);
    }
	
}

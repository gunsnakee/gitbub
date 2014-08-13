package com.meiliwan.emall.sp2.client;

import static com.meiliwan.emall.icetool.IceClientTool.sendMsg;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.sp2.bean.SpTicket;
import com.meiliwan.emall.sp2.bean.SpTicketOrder;
import com.meiliwan.emall.sp2.bean.view.SimpleOrdi;
import com.meiliwan.emall.sp2.bean.view.SimpleSpTicket;
import com.meiliwan.emall.sp2.bean.view.SpTicketUsageResult;
import com.meiliwan.emall.sp2.constant.SpTicketActiveR;
import com.meiliwan.emall.sp2.constant.SpTicketState;
import com.meiliwan.emall.sp2.constant.SpTicketType;

/**
 * 优惠券接口; 优惠券某种程度上类似 多品活动;
 * 
 * @author leo
 *
 */
public class SpTicketClient {
	
    static String getMethod(String method) {
		return String.format("spTicketService/%s", method);
	}
	
    //获取优惠券 by id
    public static SpTicket getTicketByTicketId(final String ticketId) {
    	JsonObject obj = sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams(getMethod("getTicketByTicketId"), ticketId));
    	return new Gson().fromJson(obj.get("resultObj"), SpTicket.class);
    }
    
    //获取优惠券 by pwd
    public static SpTicket getTicketByPwd(final String ticketPwd) {
    	JsonObject obj = sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams(getMethod("getTicketByPwd"), ticketPwd));
    	return new Gson().fromJson(obj.get("resultObj"), SpTicket.class);
    }
    
	//优惠券激活
    public static SpTicketActiveR activateTicket(final String ticketPwd, int uid) {
    	JsonObject obj = sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams(getMethod("activateTicket"), ticketPwd, uid));
    	
    	return new Gson().fromJson(obj.get("resultObj"), SpTicketActiveR.class);
    }
    
    
    //选择优惠券
    //根据订单行和用户id得到该用户在订单中满足要求的优惠券列表;  用户核对订单页需要
    public static Map<SpTicketType, Set<SpTicket>> getSpTicketsOfType(int uid, int... proIds) {
    	
    	JsonObject obj = sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams(getMethod("getSpTicketsOfType"), uid, proIds));
    	
    	return new Gson().fromJson(obj.get("resultObj"), new TypeToken<LinkedHashMap<SpTicketType, Set<SpTicket>>>(){
        }.getType());
    }
    
    //优惠券计算
    public static SpTicketUsageResult getSrOnTicketSelective(int uid, SimpleOrdi[] simpleOrdis, SimpleSpTicket ... spTickets) {
    	JsonObject obj = sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams(getMethod("getSrOnTicketSelective"), uid, simpleOrdis, spTickets));
    	
    	return new Gson().fromJson(obj.get("resultObj"), 
    			new TypeToken<SpTicketUsageResult>(){}.getType());
    }
    
    //使用优惠券
    //根据订单行,用户id和优惠券id得到优惠处理的结果;
    public static void useSpTicketOnOrder(int uid, String orderId, SimpleSpTicket ... simpleSpTickets) {
    	sendMsg(IceClientTool.SP_ICE_SERVICE,
    		    JSONTool.buildParams(getMethod("useSpTicketOnOrderC"), uid, orderId, simpleSpTickets));
    } 
    
    //订单取消，取消优惠券使用
    public static void cancelSpTicketOnOrder(String orderId) {
    	sendMsg(IceClientTool.SP_ICE_SERVICE,
    		    JSONTool.buildParams(getMethod("cancelSpTicketStateOnOrder"), orderId));
    }
    
    //优惠券列表查询
    public static List<SpTicket> getSpTicketList(int uid, SpTicketState sts) {
    	JsonObject obj = sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams(getMethod("getSpTicketList"), uid, sts));
    	return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<SpTicket>>(){
        }.getType());
    }
    
    //优惠券列表查询
    public static PagerControl<SpTicket> getPagerOfSpTicket(PageInfo pageInfo, int uid, SpTicketState sts) {
    	JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
    			JSONTool.buildParams(getMethod("getPagerOfSpTicket"), pageInfo, uid, sts));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<SpTicket>>(){}.getType());
    }

    /**
     * 分页查找优惠券信息
     *
     * @param ticket
     * @param pageInfo
     * @param order_name
     * @param order_form
     */
    public static PagerControl<SpTicket> getPagerByObj(SpTicket ticket, PageInfo pageInfo, String order_name, String order_form) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams(getMethod("getPagerByObj"), ticket, pageInfo, order_name, order_form));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<SpTicket>>() {
        }.getType());
    }
    

    /**
     * 分页查找优惠券订单信息
     * @param pageInfo
     * @param sto
     * @param whereSql
     * @return
     */
    public static PagerControl<SpTicketOrder> getPagerOfSpTicketOrder(PageInfo pageInfo, SpTicketOrder sto, String whereSql) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams(getMethod("getPagerOfSpTicketOrder"), pageInfo, sto, whereSql));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<SpTicketOrder>>() {
        }.getType());
    }
    

}

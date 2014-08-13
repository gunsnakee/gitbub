
package com.meiliwan.emall.sp2.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.service.BaseService;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import com.meiliwan.emall.sp2.bean.ActivityBean;
import com.meiliwan.emall.sp2.bean.ActivityDiscountRuleBean;
import com.meiliwan.emall.sp2.bean.ActivityProductBean;
import com.meiliwan.emall.sp2.bean.view.UserProVO;
import com.meiliwan.emall.sp2.cache.ActCacheTool;
import com.meiliwan.emall.sp2.constant.ActState;
import com.meiliwan.emall.sp2.constant.ActType;
import com.meiliwan.emall.sp2.constant.PrivilegeType;
import com.meiliwan.emall.sp2.dao.ActivityDao;
import com.meiliwan.emall.sp2.dao.ActivityDiscountRuleDao;
import com.meiliwan.emall.sp2.dao.ActivityProductDao;
import com.meiliwan.emall.sp2.dto.ActivityDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.commons.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
import java.util.List;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * 活动 server
 *
 * @author yiyou.luo
 *         2013-12-24
*/
@Service
public class ActivityService extends DefaultBaseServiceImpl implements
        BaseService{
	
	private final static int STATE_VALID = 0;

    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private ActivityProductDao activityProductDao;
    @Autowired
    private ActivityDiscountRuleDao activityDiscountRuleDao;

    public void getCashierCart(JsonObject resultObj, List<UserProVO> userProVOList) {

    }

    /**
     * 根据活动ID获得活动对象
     *
     * @param actId
     * @param resultObj
     */
    @IceServiceMethod
    public void getSpActivityById(JsonObject resultObj, int actId) {
            ActivityBean entry = activityDao.getEntityById(actId);
            addToResult(entry, resultObj);
    }

    /**
     * 添加活动
     *
     * @param spActivity
     * @param resultObj
     */
    @IceServiceMethod
    public void saveSpActivity(JsonObject resultObj, ActivityBean spActivity) {
        if (spActivity != null) {
            spActivity.setActId(null);
            spActivity.setCreateTime(new Date());
            activityDao.insert(spActivity);
            addToResult(spActivity.getActId()>0?spActivity.getActId():-1, resultObj);
        }else {
            addToResult(-1, resultObj);
        }

    }

    /**
     * 修改活动
     *
     * @param spActivity
     * @param resultObj
     */
    @IceServiceMethod
    public void updateSpActivity(JsonObject resultObj,ActivityBean spActivity) {
        boolean isSuccess = false;
        if (spActivity != null) {
            spActivity.setUpdateTime(new Date());
            isSuccess = activityDao.update(spActivity)>0?true:false;
            if(isSuccess && spActivity.getState()!=null){
                if( ActState.UP.getState() == spActivity.getState().shortValue()){ //活动上架
                   //执行活动上架 缓存方法
                    ActCacheTool.addAct(activityDao.getEntityById(spActivity.getActId()));
                }
                if(ActState.DOWN.getState() == spActivity.getState().shortValue()){  //活动下架
                   //执行活动下架 缓存方法
                    ActCacheTool.updateAct(spActivity.getActId(),ActState.DOWN);
                }
            }
        }
        addToResult(isSuccess,resultObj);
    }


    /**
     * 通过活动 实体参数获取对应的实体列表包含物理分页
     *
     * @param pageInfo
     * @param resultObj
     */
    @IceServiceMethod
    public void getSpActivityPaperByObj(JsonObject resultObj, ActivityBean spActivity, PageInfo pageInfo) {
        if (spActivity != null && pageInfo != null) {
            addToResult(activityDao.getPagerByObj(spActivity, pageInfo, "", " order by create_time "), resultObj);
        }
    }

    /**
     * 通过活动查询DTO 实体参数获取对应的实体列表包含物理分页
     *
     * @param pageInfo
     * @param resultObj
     */
    @IceServiceMethod
    public void getSpActivityPaperByActivityDTO(JsonObject resultObj, ActivityDTO activityDTO, PageInfo pageInfo) {
        if (activityDTO != null && pageInfo != null) {
            addToResult(activityDao.getSpActivityPaperByActivityDTO(activityDTO, pageInfo), resultObj);
        }
    }


    /**
     * 删除一个未上线过的活动
     * (删除活动、删除活动商品、删除活动规则)
     * 当前活动的state必须等于 0
     * @param resultObj
     * @param actId
     */
    public void deleteUnOnlineSpActivity(JsonObject resultObj,Integer actId) {
        boolean isSuccess = false;
        ActivityBean act = activityDao.getEntityById(actId);
        if(act != null && act.getState() !=null &&
                Short.toString(ActState.CREATED.getState()).equals(act.getState().toString()))
        {
            int actDeleteNum = activityDao.delete(actId);
            activityProductDao.deleteByActId(actId);
            activityDiscountRuleDao.deleteByActId(actId);
            if(actDeleteNum>0){
                isSuccess =true;
            }
        }
        addToResult(isSuccess, resultObj);
    }

    /**
     * 根据活动名称获取当前最新上线的直降活动
     * @param resultObj
     * @param actName
     */
    public void getNewActByName(JsonObject resultObj,String actName){
        ActivityDTO actDto= new ActivityDTO();
        if(StringUtils.isNotBlank(actName)){
            actDto.setActName("%"+actName+"%");
        }

        actDto.setActViewState((short) 11);    //正在进行（(上线）：11
        actDto.setPrivilegeType(PrivilegeType.SINGLE.name()); //
        actDto.setActType(ActType.DISCOUNT.name());
        actDto.setCurrentTime(new Date());
        PagerControl<ActivityBean> pc = activityDao.getSpActivityPaperByActivityDTO(actDto, new PageInfo());
        ActivityBean actBean = null;
        if(pc!=null && pc.getEntityList()!=null && pc.getEntityList().size()>0){
            actBean = pc.getEntityList().get(0);
        }
        addToResult(actBean, resultObj);
    }


}





package com.meiliwan.emall.base.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.annotation.Param;
import com.meiliwan.emall.base.bean.BaseAreaManagement;
import com.meiliwan.emall.base.dao.BaseAreaManagementDao;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;





import static com.meiliwan.emall.icetool.JSONTool.addToResult;
import static com.meiliwan.emall.icetool.JSONTool.addToResultMap;

/**
 * 地域管理 server
 *
 * @author yiyou.luo
 *         2013-06-03
 */
@Service
public class BaseAreaManagementService extends DefaultBaseServiceImpl {
	
	private final static int STATE_VALID = 0;

    @Autowired
    private BaseAreaManagementDao baseAreaManagementDao;

    /**
     * 根据地域ID获得地域对象
     *
     * @param areaId
     * @param resultObj
     */
    @IceServiceMethod
    public void getAreaById(JsonObject resultObj, String areaId) {
        if (areaId != null && areaId.matches("^[0-9]*$")) { //过滤空和非数字
            BaseAreaManagement areaManagement = baseAreaManagementDao.getEntityById(Integer.parseInt(areaId));
            addToResult(areaManagement, resultObj);
        }
    }

    /**
     * 添加地域
     *
     * @param baseAreaManagement
     * @param resultObj
     */
    @IceServiceMethod
    public void saveArea(JsonObject resultObj, BaseAreaManagement baseAreaManagement) {
        if (baseAreaManagement != null) {
            baseAreaManagement.setAreaId(null);
            baseAreaManagement.setIsDel(STATE_VALID);
            baseAreaManagement.setAreaCode(getNewAreaCode(baseAreaManagement.getAreaGrade(),
                    baseAreaManagement.getParentCode()));
            baseAreaManagementDao.insert(baseAreaManagement);
           // addToResult(baseAreaManagement.getAreaId()>0?true:false, resultObj);
            addToResult(baseAreaManagement.getAreaId()>0?baseAreaManagement.getAreaId():-1, resultObj);
        }else {
            addToResult(-1, resultObj);
        }

    }

    /**
     * 修改地域
     *
     * @param baseAreaManagement
     * @param resultObj
     */
    @IceServiceMethod
    public void updateArea(JsonObject resultObj, @Param("baseAreaManagement") BaseAreaManagement baseAreaManagement) {
        if (baseAreaManagement != null) {
            addToResult(baseAreaManagementDao.update(baseAreaManagement)>0?true:false, resultObj);
        }
    }

    /**
     * 通过上级编码获取未删除的地域
     *
     * @param parentCode
     * @param resultObj
     */
    @IceServiceMethod
    public void getAreasByParentCode(JsonObject resultObj, String parentCode) {
        if (parentCode != null && parentCode.matches("^[0-9]*$")) { //过滤空和非数字
            addToResult(baseAreaManagementDao.getAreasByParentCode(parentCode), resultObj);
        }
    }

    /**
     * 通过地区级别获取地域（未逻辑删除）
     *
     * @param grade
     * @param resultObj
     */
    @IceServiceMethod
    public void getAreasByGrade(JsonObject resultObj, String grade) {
        if (null != grade && grade.matches("^[0-9]*$")) { //过滤空和非数字
            BaseAreaManagement area = new BaseAreaManagement();
            area.setAreaGrade(Integer.parseInt(grade));
            area.setIsDel(STATE_VALID);
            addToResult(baseAreaManagementDao.getListByObj(area, "", " order by area_code desc"), resultObj);
        }
    }

    /**
     * 根据地域名称和地域级别获取地址，以及同级别同parentid的地址
     * @param name
     * @param grade
     * @param resultObj
     */
    @IceServiceMethod
    public void getAreasByNameAndGrade(JsonObject resultObj, String name,Integer grade) {
        String areaCode = "";
        if(StringUtils.isNotBlank(name) && grade>0){
            BaseAreaManagement area = new BaseAreaManagement();
            area.setAreaGrade(grade);
            area.setAreaName("%"+name+"%");
            List<BaseAreaManagement> list = baseAreaManagementDao.getListByObj(area) ;
            if(list!=null && list.size()>0){
                BaseAreaManagement ba = list.get(0);
                areaCode = ba.getAreaCode();
            }
        }
        addToResult(areaCode,resultObj);
    }

    /**
     * 通过地域 实体参数获取对应的实体列表包含物理分页
     *
     * @param pageInfo
     * @param resultObj
     */
    @IceServiceMethod
    public void getAreaPaperByObj(JsonObject resultObj, BaseAreaManagement baseAreaManagement, PageInfo pageInfo) {
        if (baseAreaManagement != null && pageInfo != null) {
		    if(StringUtils.isNotBlank(baseAreaManagement.getAreaName())){
                baseAreaManagement.setAreaName("%"+baseAreaManagement.getAreaName()+"%");
            }
            baseAreaManagement.setIsDel(STATE_VALID);
            addToResult(baseAreaManagementDao.getPagerByObj(baseAreaManagement, pageInfo, "", " order by area_code  "), resultObj);
        }
    }

    /**
     * 生成地域 编码
     *1、如果上级区域已经有下级区域则获取最大子区域然后自增1
     *2、否则就生成该区域的第一个子区域：
     *      地区级别不是街道乡镇级（areaGrade=‘4“）在上级编码串后面加01，是的话就加001
     * @param areaGrade
     * @param parentCode
     * @return
     */
    public String getNewAreaCode(Integer areaGrade, String parentCode) {
        String newAreaCode="";
        BaseAreaManagement area = new BaseAreaManagement();
        area.setParentCode(parentCode);
        List<BaseAreaManagement> areaList = baseAreaManagementDao.getListByObj(area,"", "order by area_code",true);
        if (areaList != null && areaList.size()>0){
           String lastCode = areaList.get(areaList.size()-1).getAreaCode();
           if (lastCode.matches("^[0-9]*$")){
               newAreaCode = (Long.parseLong(lastCode)+1)+"";
           }
        } else{
            if(areaGrade==4){
                newAreaCode = parentCode+"001";
            }else{
                newAreaCode = parentCode+"01";
            }
        }
        return newAreaCode;
    }

}

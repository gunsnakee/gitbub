package com.meiliwan.emall.pms.service;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.pms.bean.ProStoreProd;
import com.meiliwan.emall.pms.dao.ProStoreProdDao;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

/**
 * 店铺商品 service
 *
 * @author yiyou.luo
 *         2013-09-24
 */
 @Service
public class ProStoreProdService extends DefaultBaseServiceImpl {
    @Autowired
    private ProStoreProdDao proStoreProdDao;

    /**
     * 通过id获取 店铺商品 实例
     * @param resultObj
     * @param id
     */
    @IceServiceMethod
    public void getProStoreProdById(JsonObject resultObj, int id) {
        if (id >0) {
            addToResult(proStoreProdDao.getEntityById(id) ,resultObj);
        }
    }


    /**
     * 添加 店铺商品
     *
     * @param proStoreProd
     * @param resultObj
     */
    @IceServiceMethod
    public void saveProStoreProd(JsonObject resultObj,ProStoreProd proStoreProd) {
        if (proStoreProd != null) {
            proStoreProdDao.insert(proStoreProd) ;
            addToResult(proStoreProd.getId()>0?proStoreProd.getId():-1,resultObj);
        }else{
            addToResult(-1,resultObj);
        }
    }

    /**
     * 修改 店铺商品
     *
     * @param proStoreProd
     * @param resultObj
     */
    @IceServiceMethod
    public void updateProStoreProd(JsonObject resultObj,ProStoreProd proStoreProd) {
        if (proStoreProd != null) {
                addToResult(proStoreProdDao.update(proStoreProd)>0?true:false, resultObj);
        } else{
            addToResult(false,resultObj);
        }
    }

    /**
     * 通过  /**
     * 通过 店铺商品 实体参数获取对应的实体列表包含物理分页
     *
     * @param pageInfo
     * @param resultObj
     */
    @IceServiceMethod
    public void getProStoreProdPaperByObj(JsonObject resultObj, ProStoreProd proStoreProd, PageInfo pageInfo) {
        if (proStoreProd != null && pageInfo != null) {
            addToResult(proStoreProdDao.getPagerByObj(proStoreProd, pageInfo, "", "order by create_time  "), resultObj);
        }
    }

}

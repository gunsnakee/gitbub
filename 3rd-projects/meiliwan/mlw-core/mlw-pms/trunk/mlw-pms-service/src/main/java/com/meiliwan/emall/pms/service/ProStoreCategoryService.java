package com.meiliwan.emall.pms.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.pms.bean.ProStoreCategory;
import com.meiliwan.emall.pms.dao.ProStoreCategoryDao;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * 店铺类目 service
 *
 * @author yiyou.luo
 *         2013-09-24
 */
@Service
public class ProStoreCategoryService extends DefaultBaseServiceImpl {
    @Autowired
    private ProStoreCategoryDao proStoreCategoryDao;

    /**
     * 通过id获取 店铺类目 实例
     *
     * @param resultObj
     * @param id
     */
    @IceServiceMethod
    public void getProStoreCategoryById(JsonObject resultObj, int id) {
        if (id > 0) {
            addToResult(proStoreCategoryDao.getEntityById(id), resultObj);
        }
    }


    /**
     * 添加 店铺类目
     *
     * @param proStoreCategory
     * @param resultObj
     */
    @IceServiceMethod
    public void saveProStoreCategory(JsonObject resultObj, ProStoreCategory proStoreCategory) {
        if (proStoreCategory != null) {
            proStoreCategoryDao.insert(proStoreCategory);
            addToResult(proStoreCategory.getId() > 0 ? proStoreCategory.getId() : -1, resultObj);
        } else {
            addToResult(-1, resultObj);
        }
    }

    /**
     * 添加 店铺类目
     *
     * @param proStoreCategoryList
     * @param resultObj
     */
    @IceServiceMethod
    public void saveProStoreCategoryList(JsonObject resultObj,  ProStoreCategory[] proStoreCategoryList) {
        int result = -1;
        if (proStoreCategoryList != null) {
            for (ProStoreCategory eachObj : proStoreCategoryList) {
                proStoreCategoryDao.insert(eachObj);
                addToResult(eachObj.getId() > 0 ? eachObj.getId() : -1, resultObj);
            }
        }
        addToResult(1, resultObj);
    }


    /**
     * 修改 店铺类目
     *
     * @param proStoreCategory
     * @param resultObj
     */
    @IceServiceMethod
    public void updateProStoreCategory(JsonObject resultObj, ProStoreCategory proStoreCategory) {
        if (proStoreCategory != null) {
            addToResult(proStoreCategoryDao.update(proStoreCategory) > 0 ? true : false, resultObj);
        } else {
            addToResult(false, resultObj);
        }
    }


    /**
     * 通过  /**
     * 通过 店铺类目 实体参数获取对应的实体列表包含物理分页
     *
     * @param pageInfo
     * @param resultObj
     */
    @IceServiceMethod
    public void getProStoreCategoryPaperByObj(JsonObject resultObj, ProStoreCategory proStoreCategory, PageInfo pageInfo) {
        if (proStoreCategory != null && pageInfo != null) {
            addToResult(proStoreCategoryDao.getPagerByObj(proStoreCategory, pageInfo, "", "  "), resultObj);
        }
    }

}

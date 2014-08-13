package com.meiliwan.emall.pms.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.pms.bean.ProCategory;
import com.meiliwan.emall.pms.bean.ProProperty;
import com.meiliwan.emall.pms.bean.ProSpu;
import com.meiliwan.emall.pms.dao.ProCategoryDao;
import com.meiliwan.emall.pms.dao.ProPropertyDao;
import com.meiliwan.emall.pms.dao.ProSpuDao;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

@Service
public class ProCategoryService extends DefaultBaseServiceImpl {

    @Autowired
    private ProCategoryDao proCategoryDao;
    @Autowired
    private ProSpuDao proSpuDao;
    @Autowired
    private ProPropertyDao proPropertyDao;

    /**
     * 增加商品类目
     *
     * @param proCategory
     * @return
     */
    public void addProCategory(JsonObject resultObj, ProCategory proCategory) {
        if (isExistNullCategory(proCategory)) {
            addToResult(false, resultObj);
        } else {
            if (proCategory != null) {
                int result = proCategoryDao.insert(proCategory);
                addToResult(result > 0 ? true : false, resultObj);
            }
        }
    }

    /**
     * 修改商品类目
     *
     * @param proCategory
     * @return
     */
    public void updateProCategory(JsonObject resultObj, ProCategory proCategory) {
        if (proCategory != null) {
            try {
                proCategoryDao.update(proCategory);
                addToResult(true, resultObj);
            } catch (Exception e) {
                addToResult(false, resultObj);
                throw new ServiceException("service-pms-ProCategoryService.updateProCategory:{}", proCategory == null ? "" : proCategory.toString(), e);

            }
        } else {
            addToResult(false, resultObj);
        }
    }

    /**
     * 删除商品类目，false表示存在关联不可以删除，ture 表示可以表述删除成功
     *
     * @param categoryId
     * @param level
     */
    public void deleteProCategory(JsonObject resultObj, int categoryId, int level) {
        if (isExitProduct(categoryId, level) || isExitProProperty(categoryId, level)) {
            addToResult(false, resultObj);
        } else {
            try {
                if (level == 1) {
                    //通过类目ID 获取颗树形结构
                    ProCategory proCategory = getTreeById(categoryId);
                    if (proCategory != null) {
                        if (proCategory.getChildren() != null) {
                            for (ProCategory pct : proCategory.getChildren()) {
                                if (pct == null) continue;
                                if (pct.getChildren() != null) {
                                    for (ProCategory pct1 : pct.getChildren()) {
                                        if (pct1 == null) continue;
                                        proCategoryDao.delete(pct1.getCategoryId());
                                    }
                                }
                                proCategoryDao.delete(pct.getCategoryId());
                            }
                        }
                    }
                } else if (level == 2) {
                    ProCategory proCategory = new ProCategory();
                    proCategory.setParentId(categoryId);
                    List<ProCategory> list = proCategoryDao.getListByObj(proCategory, null,true);
                    if (list != null) {
                        for (ProCategory pct : list) {
                            if (pct == null) continue;
                            proCategoryDao.delete(pct.getCategoryId());
                        }
                    }
                }
                proCategoryDao.delete(categoryId);
                addToResult(true, resultObj);
            } catch (Exception e) {
                addToResult(false, resultObj);
                throw new ServiceException("service-pms-ProCategoryService.deleteProCategory:{}", categoryId + "," + level, e);
            }
        }
    }

    /**
     * 获取所有的类目
     */
    public void getAllCategory(JsonObject resultObj) {
        List<ProCategory> proCategoryList = proCategoryDao.getListByObj(null, "", "order by sequence asc");
        addToResult(proCategoryList, resultObj);
    }

    /**
     * 获取所有类目组成树形结构
     *
     * @return
     */
    public void getTreeCategory(JsonObject resultObj) {
        List<ProCategory> allCategories = proCategoryDao.getListByObj(null, "", "order by sequence asc");
        addToResult(settingTree(allCategories, 0), resultObj);
    }

    /**
     * 通过pid查找对应的类目
     */
    public void getListByPId(JsonObject resultObj, ProCategory protCategory) {
        List<ProCategory> proCategoryList = proCategoryDao.getListByObj(protCategory, "", "order by sequence asc");
        addToResult(proCategoryList, resultObj);
    }



    /**
     * 通过pid查找对应的类目
     */
    public void getListByCategoryId(JsonObject resultObj, int categoryId) {
        ProCategory category = new ProCategory();
        category.setParentId(categoryId);
        category.setState((short)1);
        List<ProCategory> proCategoryList = proCategoryDao.getListByObj(category, "", "order by sequence asc");
        addToResult(proCategoryList, resultObj);
    }

    /**
     * 通过类目id查找类目信息
     */
    public void getProCategoryById(JsonObject resultObj, int categoryId) {
        addToResult(proCategoryDao.getEntityById(categoryId), resultObj);
    }

    /**
     * 通过一二三级类目查找对应的类目，然后组成树型结构
     */
    public void getCategoryByLevelId(JsonObject resultObj, int first, int second, int third) {
        addToResult(getByLevel(first,second,third), resultObj);
    }

    /**
     * 通过商品ID对应的类目，然后组成树型结构
     */
    public void getCategoruByProId(JsonObject resultObj,int spuId){
        ProSpu product = proSpuDao.getEntityById(spuId);
        if (product != null){
            ProCategory category = getByLevel(product.getFirstCategoryId(), product.getSecondCategoryId(), product.getThirdCategoryId());
            addToResult(category == null?new ProCategory():category, resultObj);
        }
    }

    public void getTreeSingleNoteCategoryById(JsonObject resultObj, int categoryId) {
        addToResult(getTreeById(categoryId), resultObj);
    }

    /**
     * 根据根据三级类目ID查找是否存在商品
     *
     * @param categoryId
     */
    private boolean isExitProduct(int categoryId, int level) {
        ProSpu product = new ProSpu();
        if (level == 1){
            product.setFirstCategoryId(categoryId);
        }else if (level == 2){
            product.setSecondCategoryId(categoryId);
        }else {
            product.setThirdCategoryId(categoryId);
        }
        PageInfo pageInfo = new PageInfo();
        pageInfo.setStartIndex(0);
        pageInfo.setPagesize(1);
        List<ProSpu> ppt = proSpuDao.getListByObj(product, pageInfo, null);
        if (ppt.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据根据类目ID查找是否存在商品属性
     *
     * @param categoryId
     */
    private boolean isExitProProperty(int categoryId, int level) {
        ProProperty property = new ProProperty();
        PageInfo pageInfo = new PageInfo();
        pageInfo.setStartIndex(0);
        pageInfo.setPagesize(1);
        if (level != 3) {
            ProCategory pct = new ProCategory();
            pct.setParentId(categoryId);
            List<ProCategory> pctList = proCategoryDao.getListByObj(pct, null,true);
            for (ProCategory pc : pctList) {
                property.setCategoryId(pc.getCategoryId());
                List<ProProperty> ppt = proPropertyDao.getListByObj(property, pageInfo, null,true);
                if (ppt.size() > 0) {
                    return true;
                }
            }
        } else {
            property.setCategoryId(categoryId);
            List<ProProperty> ppt = proPropertyDao.getListByObj(property, pageInfo, null,true);
            if (ppt.size() > 0) {
                return true;
            }
        }
        return false;
    }

    private List<ProCategory> settingTree(List<ProCategory> allItem, int level) {
        List<ProCategory> returnList = new ArrayList<ProCategory>();
        for (ProCategory temp : allItem) {
            if (temp.getParentId() == level) {
                settingChild(allItem, temp);
                returnList.add(temp);
            }
        }
        return returnList;
    }

    private void settingChild(List<ProCategory> allItem, ProCategory parent) {
        for (ProCategory item : allItem) {
            if (item.getParentId().equals(parent.getId())) {
                settingChild(allItem, item);

                List<ProCategory> list = parent.getChildren();
                if (list == null) {
                    list = new ArrayList<ProCategory>();
                }
                list.add(item);
                parent.setChildren(list);
            }
        }
    }

    private ProCategory getTreeById(int categoryId) {
        List<ProCategory> allCategories = proCategoryDao.getListByObj(null, "", "order by sequence asc",true);
        List<ProCategory> treeList = settingTree(allCategories, 0);
        for (ProCategory p : treeList) {
            if (p.getId() == categoryId) {
                return p;
            }
        }
        return null;
    }

    public boolean isExistNullCategory(ProCategory proCategory) {
        return StringUtils.isEmpty(proCategory.getCategoryName())
                ||StringUtils.isEmpty(proCategory.getCategoryName());
    }

    private ProCategory getByLevel(int first,int second,int third){
        ProCategory pcFirst = proCategoryDao.getEntityById(first);
        ProCategory pcSecond = proCategoryDao.getEntityById(second);
        ProCategory pcThird = proCategoryDao.getEntityById(third);

        List<ProCategory> listf = new ArrayList<ProCategory>();
        List<ProCategory> listc = new ArrayList<ProCategory>();
        listf.add(pcThird);
        if (pcSecond !=null){
            pcSecond.setChildren(listf);
        }

        listc.add(pcSecond);
        if (pcFirst != null){
            pcFirst.setChildren(listc);
        }
        return pcFirst;
    }
}
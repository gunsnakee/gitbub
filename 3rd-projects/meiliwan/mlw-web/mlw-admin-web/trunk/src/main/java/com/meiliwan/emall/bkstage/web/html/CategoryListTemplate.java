package com.meiliwan.emall.bkstage.web.html;

import com.meiliwan.emall.pms.bean.ProCategory;
import com.meiliwan.emall.pms.client.ProCategoryClient;
import java.util.List;


/**
 * User: guangdetang
 * Date: 13-6-6
 * Time: 上午10:32
 */
public class CategoryListTemplate {


    /**
     * 根据各级类目集合将类目拼接成HTML下拉列表
     * @param listCategory
     * @param levelId
     * @return
     */
    public static String getCategoryHtml(List<ProCategory> listCategory, int levelId) {
        if (listCategory != null && listCategory.size() > 0) {
            String levelHtml = "";
            for (ProCategory pc : listCategory) {
                String categoryHtml = "";
                if (pc.getId() == levelId) {
                    categoryHtml = "<option value=\'" + pc.getId() + "\' selected>" + pc.getCategoryName() + "</option>";
                } else {
                    categoryHtml = "<option value=\'" + pc.getId() + "\'>" + pc.getCategoryName() + "</option>";
                }
                levelHtml = levelHtml + categoryHtml;
            }
            return levelHtml;
        }
        return null;
    }

    /**
     * 根据父id获得类目list
     * @param pid
     * @return
     */
    public static List<ProCategory> getCategoryListByPid(int pid) {
        ProCategory category = new ProCategory();
        category.setParentId(pid);
        category.setState((short) 1);
        return ProCategoryClient.getListByPId(category);
    }
}

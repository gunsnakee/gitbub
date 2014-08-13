package com.meiliwan.emall.bkstage.web.html.bkstage;

import com.google.common.base.Strings;
import com.meiliwan.emall.pms.bean.ProCategory;
import com.meiliwan.emall.pms.bean.ProStoreCategory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sean on 13-9-26.
 */
public class StoreProCategoryTreeTemplate {

    public static String getProCategoryString(Collection<ProCategory> list,
                                              Integer pid, Collection<ProStoreCategory> proCate, String ulClassStr, int cutStoreId) {
        //需要排除的类目
        Map<Integer, ProStoreCategory> map = new HashMap<Integer, ProStoreCategory>();
        //如果是修改店铺，这部分的集合，属于，选中集合，不被排除
        Map<Integer, ProStoreCategory> needChecked = new HashMap<Integer, ProStoreCategory>();

        if (proCate != null) {
            for (ProStoreCategory temp : proCate) {
                map.put(temp.getThirdCategoryId(), temp);
                if (temp.getStoreId() == cutStoreId) {
                    needChecked.put(temp.getThirdCategoryId(), temp);
                }
            }
        }

        boolean isFirst = true;
        StringBuffer sb = new StringBuffer();
        for (ProCategory menu : list) {
            if (pid.equals(menu.getParentId())) {
                if (isFirst) {
                    isFirst = false;
                    sb.append("<ul class=\"" + ulClassStr + "\" >");
                }
                sb.append("<li>");

                String nodeStr = getLevel2(list, menu, map, needChecked);
                if (!Strings.isNullOrEmpty(nodeStr)) {
                    sb.append(madeHref(menu, map, "node", needChecked)).append(nodeStr);
                }
                sb.append("</li>");
            }
        }
        if (!isFirst) {
            sb.append("</ul>");
        }
        return sb.toString();
    }

    public static String getLevel2(Collection<ProCategory> list, ProCategory level_1_Obj, Map<Integer, ProStoreCategory> map, Map<Integer, ProStoreCategory> needChecked) {
        boolean isFirst = true;
        StringBuffer sb = new StringBuffer();
        int itemCount = 0;
        for (ProCategory proCategory : list) {
            if (level_1_Obj.getCategoryId().equals(proCategory.getParentId())) {
                if (isFirst) {
                    sb.append("<ul>");
                    isFirst = false;
                }
                sb.append("<li>");
                String nodeStr = getLevel3(list, proCategory, map, needChecked);
                if (!Strings.isNullOrEmpty(nodeStr)) {
                    sb.append(madeHref(proCategory, map, "node", needChecked)).append(nodeStr);
                    itemCount++;
                }
                sb.append("</li>");
            }
        }
        if (!isFirst) {
            sb.append("</ul>");
        }
        if (itemCount > 0)
            return sb.toString();
        return null;
    }

    public static String getLevel3(Collection<ProCategory> list, ProCategory level_2_Obj, Map<Integer, ProStoreCategory> map, Map<Integer, ProStoreCategory> needChecked) {
        boolean isFirst = true;
        int itemCount = 0;
        StringBuffer curHtml = new StringBuffer();
        for (ProCategory proCategory : list) {
            if (level_2_Obj.getCategoryId().equals(proCategory.getParentId())) {
                if (isFirst) {
                    curHtml.append("<ul>");
                    isFirst = false;
                }
                curHtml.append("<li>");

                String href = madeHref(proCategory, map, level_2_Obj.getParentId() + "_" + level_2_Obj.getCategoryId() + "_" + proCategory.getCategoryId(), needChecked);
                if (!Strings.isNullOrEmpty(href)) {
                    itemCount++;
                }
                curHtml.append(href);
                curHtml.append("</li>");
            }
        }
        if (!isFirst) {
            curHtml.append("</ul>");
        }
        if (itemCount > 0) {
            return curHtml.toString();
        }
        return null;
    }

    public static String madeHref(ProCategory proCategory, Map<Integer, ProStoreCategory> map, String tvalue, Map<Integer, ProStoreCategory> needChecked) {
        //如果当前类目 ，已经属于 其他店铺，并且 不包含在 当前店铺所属的类别当中，不展示 选择框
        if (map.containsKey(proCategory.getCategoryId()) && !needChecked.containsKey(proCategory.getCategoryId()))
            return "";
        //如果当前类目 属于店铺所属的类目 属于选中状态
        String isSelected = needChecked.containsKey(proCategory.getCategoryId()) ? " checked=\"true\"" : "";

        return "<a  tname=\"proCageId\" " + isSelected + "  tvalue=\""
                + tvalue + "\">" + proCategory.getCategoryName() + "</a>";

    }
}

package com.meiliwan.emall.bkstage.web.html.bkstage;

import com.google.common.base.Strings;
import com.meiliwan.emall.bkstage.bean.BksMenu;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RoleTreeMenuTemplate {

    public static String getMenuString(Collection<BksMenu> list,
                                       Integer pid, Collection<BksMenu> roleMenu, String ulClassStr) {
        Map<Integer, BksMenu> map = new HashMap<Integer, BksMenu>();
        for (BksMenu temp : roleMenu) map.put(temp.getMenuId(), temp);

        boolean isFirst = true;
        StringBuffer sb = new StringBuffer();
        for (BksMenu menu : list) {
            if (pid.equals(menu.getParentId())) {
                if (isFirst) {
                    isFirst = false;
                    sb.append("<ul class=\"" + ulClassStr + "\" >");
                }
                sb.append("<li>").append(madeHref(menu, map));
                getLevel2(list, menu.getMenuId(), sb, map);
                sb.append("</li>");
            }
        }
        if (!isFirst) {
            sb.append("</ul>");
        }
        return sb.toString();
    }

    public static String getLevel2(Collection<BksMenu> list, Integer pid, StringBuffer sb, Map<Integer, BksMenu> map) {
        boolean isFirst = true;
        for (BksMenu menu : list) {
            if (pid.equals(menu.getParentId())) {
                if (isFirst) {
                    sb.append("<ul>");
                    isFirst = false;
                }
                sb.append("<li>").append(madeHref(menu, map));
                getLevel2(list, menu.getMenuId(), sb, map);
                sb.append("</li>");
            }
        }
        if (!isFirst) {
            sb.append("</ul>");
        }
        return sb.toString();
    }

    public static String madeHref(BksMenu menu, Map<Integer, BksMenu> map) {
        String menuType = menu.getMenuType() == 1 ? "(菜单)" : "(功能)";
        String isSelected = map.containsKey(menu.getMenuId()) ? " checked=\"true\"" : "";
        String url = StringUtils.isNotEmpty(menu.getUrl()) ? "," + menu.getUrl() : "";
        String target = StringUtils.isNotEmpty(menu.getTarget()) ? "," + menu.getTarget() : "";
        String sequence = menu.getSequence() != null ? "(排序:" + menu.getSequence() + ")" : "";
        String roleKeyStr = menu.getRoleKey()!= null ? "(权限Key:" + menu.getRoleKey() + ")" : "";
        return "<a  tname=\"roleId\" " + isSelected + " tvalue=\""
                + menu.getMenuId() + "\">" + menu.getName() + Strings.repeat("&nbsp;", 5) + "(id:" + menu.getMenuId() + url + target + ")" + sequence + menuType + roleKeyStr + "</a>";

    }

}

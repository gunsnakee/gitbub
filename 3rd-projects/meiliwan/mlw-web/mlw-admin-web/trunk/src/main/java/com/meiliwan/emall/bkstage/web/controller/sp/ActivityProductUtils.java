package com.meiliwan.emall.bkstage.web.controller.sp;

import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.pms.bean.ProCategory;
import com.meiliwan.emall.pms.bean.ProSupplier;
import com.meiliwan.emall.pms.client.ProCategoryClient;
import com.meiliwan.emall.pms.client.ProSupplierClient;
import com.meiliwan.emall.pms.dto.ProductDTO;
import com.meiliwan.emall.pms.util.ProductStatus;
import org.apache.commons.lang.StringUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiongyu on 13-12-28.
 */
public class ActivityProductUtils {

    /**
     * 设置搜索条件
     *
     * @param request
     * @param model
     * @return
     */
    public static void setProductSearchParam(HttpServletRequest request, Model model, ProductDTO bean) {
        // TODO Auto-generated method stub
        //状态,默认上架
        int state = ServletRequestUtils.getIntParameter(request, "state", 1);
        bean.setState(state);
        //类目
        int first_category_id = ServletRequestUtils.getIntParameter(request, "first_category_id", 0);
        if (first_category_id != 0) {
            bean.setFirstCategoryId(first_category_id);
            ProCategory second_category = new ProCategory();
            second_category.setParentId(first_category_id);
            List<ProCategory> second_List = ProCategoryClient.getListByPId(second_category);
            model.addAttribute("second_List", second_List);
        }

        int second_category_id = ServletRequestUtils.getIntParameter(request, "second_category_id", 0);
        if (second_category_id != 0) {
            bean.setSecondCategoryId(second_category_id);
            ProCategory third_category = new ProCategory();
            third_category.setParentId(second_category_id);
            List<ProCategory> third_List = ProCategoryClient.getListByPId(third_category);
            model.addAttribute("third_List", third_List);
        }

        int third_category_id = ServletRequestUtils.getIntParameter(request, "third_category_id", 0);
        if (third_category_id != 0) {
            bean.setThirdCategoryId(third_category_id);
        }

        int pro_id = ServletRequestUtils.getIntParameter(request, "pro_id", 0);
        if (pro_id != 0) {
            bean.setProId(pro_id);
        }

        String pro_name = ServletRequestUtils.getStringParameter(request, "pro_name", "");
        if (!"".equals(pro_name.trim())) {
            bean.setProName(pro_name);
        }

        String mlw_price_min = ServletRequestUtils.getStringParameter(request, "mlw_price_min", "");
        if (!"".equals(mlw_price_min)) {
            bean.setMlwPriceMin(new BigDecimal(mlw_price_min));
        }
        String mlw_price_max = ServletRequestUtils.getStringParameter(request, "mlw_price_max", "");
        if (!"".equals(mlw_price_max)) {
            bean.setMlwPriceMax(new BigDecimal(mlw_price_max));
        }

        //时间
        String update_time_min = ServletRequestUtils.getStringParameter(request, "update_time_min", "");
        if (!"".equals(update_time_min)) {
            bean.setUpdateTimeMin(DateUtil.parseTimestamp(update_time_min));
        }

        String update_time_max = ServletRequestUtils.getStringParameter(request, "update_time_max", "");
        if (!"".equals(update_time_max)) {
            bean.setUpdateTimeMax(DateUtil.parseTimestamp(update_time_max));
        }


        int sell_type = ServletRequestUtils.getIntParameter(request, "sell_type", 0);
        if (sell_type != 0) {
            bean.setSellType(sell_type);
        }

        int supplier_id = ServletRequestUtils.getIntParameter(request, "supplier_id", 0);
        if (supplier_id != 0) {
            bean.setSupplierId(supplier_id);
        }
        // safeStock !＝null则筛选
        int safeStock = ServletRequestUtils.getIntParameter(request, "safeStock", 0);
        if (safeStock != 0) {
            bean.setSafeStock(safeStock);
        }

        //获取条形码
        String barCode = ServletRequestUtils.getStringParameter(request, "bar_code", null);
        if (!StringUtils.isEmpty(barCode)) {
            bean.setBarCode(barCode);
        }
        model.addAttribute("bean", bean);
    }

    /**
     * 获取第一级类目.第一次
     *
     * @param model
     */
    public static void setCategory(Model model) {

        ProCategory category = new ProCategory();
        category.setParentId(0);
        List<ProCategory> categoryList = ProCategoryClient.getListByPId(category);
        model.addAttribute("categoryList", categoryList);

    }

    /**
     * 状态列表
     *
     * @param model
     */
    public static void setState(Model model) {

        model.addAttribute("stateList", ProductStatus.values());

    }

    /**
     * 销售方式列表
     */
    public static void setSellType(Model model) {

        Map<Integer, Object> bean = new LinkedHashMap<Integer, Object>();
        bean.put(0, "请选择");
        bean.put(1, "购销");
        bean.put(2, "代销");
        bean.put(3, "联营");
        model.addAttribute("sellTypeMap", bean);

    }

    /**
     * 获取供应商列表
     *
     * @param model
     */
    public static void setSupplier(Model model) {
        List<ProSupplier> suppliers = ProSupplierClient.getSupplierList(new ProSupplier());
        model.addAttribute("suppliers", suppliers);

    }

}

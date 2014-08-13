package com.meiliwan.emall.bkstage.web.controller.pms;

import com.google.common.base.Strings;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.bkstage.web.html.CategoryListTemplate;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.oms.client.OrdClient;
import com.meiliwan.emall.oms.dto.SaleProOrdDTO;
import com.meiliwan.emall.pms.bean.ProCategory;
import com.meiliwan.emall.pms.bean.ProSupplier;
import com.meiliwan.emall.pms.client.ProCategoryClient;
import com.meiliwan.emall.pms.client.ProSupplierClient;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * User: guangdetang
 * Date: 13-6-9
 * Time: 下午1:56
 * 供应商
 */
@Controller("pmsSupplierController")
@RequestMapping("/pms/supplier")
public class SupplierController {

    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

    /**
     * 供应商pagerList
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/list")
    public String list(Model model, HttpServletRequest request) {
        String name = ServletRequestUtils.getStringParameter(request, "name", null);
        String linkman = ServletRequestUtils.getStringParameter(request, "linkman", null);
        String phone = ServletRequestUtils.getStringParameter(request, "phone", null);
        String otherPhone = ServletRequestUtils.getStringParameter(request, "otherPhone", null);
        String operateType = ServletRequestUtils.getStringParameter(request, "operateType", null);
        ProSupplier supplier = new ProSupplier();
        if (!Strings.isNullOrEmpty(operateType)) {
            supplier.setOperateType(Short.valueOf(operateType));
        }
        if (!Strings.isNullOrEmpty(linkman)) {
            supplier.setSupplierLinkman(linkman);
        }
        if (!Strings.isNullOrEmpty(name)) {
            supplier.setSupplierName(name);
        }
        if (!Strings.isNullOrEmpty(phone)) {
            supplier.setSupplierPhone(phone);
        }
        if (!Strings.isNullOrEmpty(otherPhone)) {
            supplier.setSupplierOtherPhone(otherPhone);
        }
        PagerControl<ProSupplier> pc = ProSupplierClient.getSupplierPager(supplier, StageHelper.getPageInfo(request));
        model.addAttribute("name", name);
        model.addAttribute("operateType", operateType);
        model.addAttribute("linkman", linkman);
        model.addAttribute("phone", phone);
        model.addAttribute("otherPhone", otherPhone);
        model.addAttribute("pc", pc);
        return "/pms/supplier/list";
    }

    /**
     * 检查是否已经存在与此名字相同的供应商
     * @param request
     * @param response
     * @throws java.io.IOException
     */
    @RequestMapping(value = "/add-re")
    public void checkRepeatSupplier(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = ServletRequestUtils.getStringParameter(request, "name", null);
        if (!Strings.isNullOrEmpty(name)) {
            boolean b = ProSupplierClient.getRepeatSupplier(name);
            response.getWriter().print(b ? "1" : "0");
        }
    }

    /**
     * 添加或修改
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/add")
    public String add(HttpServletRequest request, HttpServletResponse response) {
        int handle = ServletRequestUtils.getIntParameter(request, "handle", 0);
        if (handle == 0) {
            try {
                String name = ServletRequestUtils.getStringParameter(request, "name", null);
                String linkman = ServletRequestUtils.getStringParameter(request, "linkman", null);
                String phone = ServletRequestUtils.getStringParameter(request, "phone", null);
                String otherPhone = ServletRequestUtils.getStringParameter(request, "otherPhone", null);
                String operateType = ServletRequestUtils.getStringParameter(request, "operateType", null);
                if (!Strings.isNullOrEmpty(name) && !Strings.isNullOrEmpty(operateType)) {
                    ProSupplier supplier = new ProSupplier();
                    supplier.setSupplierName(name);
                    supplier.setSupplierLinkman(linkman);
                    supplier.setSupplierPhone(phone);
                    supplier.setSupplierOtherPhone(otherPhone);
                    supplier.setState(GlobalNames.STATE_VALID);
                    supplier.setOperateType(Short.parseShort(operateType));
                    supplier.setCreateTime(DateUtil.getCurrentTimestamp());
                    boolean isU = ProSupplierClient.addSupplier(supplier);
                    return StageHelper.writeDwzSignal(isU ? "200" : "300", isU ? "操作成功" : "操作失败", "90", StageHelper.DWZ_CLOSE_CURRENT, "/pms/supplier/list", response);
                } else {
                    return StageHelper.writeDwzSignal("300", "操作失败", "90", StageHelper.DWZ_CLOSE_CURRENT, "/pms/supplier/list", response);
                }
            } catch (Exception e) {
                logger.error(e, "管理员添加供应商时遇到异常", WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "操作失败", "90", StageHelper.DWZ_CLOSE_CURRENT, "/pms/supplier/list", response);
            }
        } else {
            return "/pms/supplier/add";
        }
    }

    /**
     * 添加或修改
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/update")
    public String update(Model model, HttpServletRequest request, HttpServletResponse response) {
        int handle = ServletRequestUtils.getIntParameter(request, "handle", 0);
        if (handle == 0) {
            int id = ServletRequestUtils.getIntParameter(request, "id", -1);
            model.addAttribute("entity", ProSupplierClient.getSupplierById(id));
            return "/pms/supplier/edit";
        } else {
            try {
                int id = ServletRequestUtils.getIntParameter(request, "id", -1);
                String name = ServletRequestUtils.getStringParameter(request, "name", null);
                String linkman = ServletRequestUtils.getStringParameter(request, "linkman", null);
                String phone = ServletRequestUtils.getStringParameter(request, "phone", null);
                String otherPhone = ServletRequestUtils.getStringParameter(request, "otherPhone", null);
                String operateType = ServletRequestUtils.getStringParameter(request, "operateType", null);
                if (!Strings.isNullOrEmpty(name)) {
                    ProSupplier supplier = new ProSupplier();
                    supplier.setSupplierId(id);
                    supplier.setSupplierName(name);
                    supplier.setSupplierLinkman(linkman);
                    supplier.setSupplierPhone(phone);
                    supplier.setSupplierOtherPhone(otherPhone);
                    supplier.setOperateType(Short.parseShort(operateType));
                    boolean isU = ProSupplierClient.updateSupplier(supplier);
                    return StageHelper.writeDwzSignal(isU ? "200" : "300", isU ? "操作成功" : "操作失败", "90", StageHelper.DWZ_CLOSE_CURRENT, "/pms/supplier/list", response);
                } else {
                    return StageHelper.writeDwzSignal("300", "操作失败", "90", StageHelper.DWZ_CLOSE_CURRENT, "/pms/supplier/list", response);
                }
            } catch (Exception e) {
                logger.error(e, "管理员修改供应商时遇到异常", WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "操作失败", "90", StageHelper.DWZ_CLOSE_CURRENT, "/pms/supplier/list", response);
            }
        }
    }

    /**
     * 删除或恢复删除
     * @param request
     * @param response
     */
    @RequestMapping(value = "/del")
    public void del(HttpServletRequest request, HttpServletResponse response) {
        try {
            int[] ids = ServletRequestUtils.getIntParameters(request, "ids");
            int state = ServletRequestUtils.getIntParameter(request, "state", 0);
            if (ids != null && (state == 0 || state == -1)) {
                boolean flag = true;
                for (Integer i : ids) {
                    ProSupplier supplier = new ProSupplier();
                    supplier.setSupplierId(i);
                    supplier.setState((short) state);
                    Boolean isSuccess = ProSupplierClient.updateSupplier(supplier);
                    if (!isSuccess) flag = false;
                }
                StageHelper.writeDwzSignal(flag ? "200" : "300", flag ? "操作成功" : "可能有部分操作失败，请仔细核对！", "90", StageHelper.DWZ_FORWARD, "/pms/supplier/list", response);
            } else {
                StageHelper.writeDwzSignal("300", "操作失败", "90", StageHelper.DWZ_CLOSE_CURRENT, "/pms/supplier/list", response);
            }
        } catch (Exception e) {
            logger.error(e, "管理员删除或恢复删除供应商时遇到异常", WebUtils.getIpAddr(request));
            StageHelper.writeDwzSignal("300", "操作失败", "90", StageHelper.DWZ_CLOSE_CURRENT, "/pms/supplier/list", response);
        }
    }

    /**
     * 已卖出商品查询
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/sellList")
    public String sellList(Model model, HttpServletRequest request) {
        int firstLevelId = ServletRequestUtils.getIntParameter(request, "firstLevelId", -1);
        int secondLevelId = ServletRequestUtils.getIntParameter(request, "secondLevelId", -1);
        int thirdLevelId = ServletRequestUtils.getIntParameter(request, "thirdLevelId", -1);
        int proId = ServletRequestUtils.getIntParameter(request, "proId", -1);
        String proName = ServletRequestUtils.getStringParameter(request, "proName", null);
        int supplierId = ServletRequestUtils.getIntParameter(request, "supplierId", -1);
        String startPrice = ServletRequestUtils.getStringParameter(request, "startPrice", null);
        String endPrice = ServletRequestUtils.getStringParameter(request, "endPrice", null);
        String startTime = ServletRequestUtils.getStringParameter(request, "startTime", null);
        String endTime = ServletRequestUtils.getStringParameter(request, "endTime", null);
        List<ProSupplier> listPS = ProSupplierClient.getSupplierList(new ProSupplier());
        String supplierHtml = "";
        if (listPS != null && listPS.size() > 0) {
            for (ProSupplier ps : listPS) {
                String supplierH = "";
                if (supplierId > 0 && ps.getSupplierId().equals(supplierId)) {
                    supplierH = "<option value=\'" + ps.getSupplierId() + "\' selected>" + ps.getSupplierName() + "</option>";
                } else {
                    supplierH = "<option value=\'" + ps.getSupplierId() + "\'>" + ps.getSupplierName() + "</option>";
                }
                supplierHtml = supplierHtml + supplierH;
            }
            model.addAttribute("supplierHtml", supplierHtml);
        }
        model.addAttribute("firstLevel", CategoryListTemplate.getCategoryHtml(CategoryListTemplate.getCategoryListByPid(0), firstLevelId));
        if (secondLevelId > 0) {
            model.addAttribute("secondLevel", CategoryListTemplate.getCategoryHtml(CategoryListTemplate.getCategoryListByPid(firstLevelId), secondLevelId));
        }
        if (thirdLevelId > 0) {
            model.addAttribute("thirdLevel", CategoryListTemplate.getCategoryHtml(CategoryListTemplate.getCategoryListByPid(secondLevelId), thirdLevelId));
        }
        SaleProOrdDTO dto = new SaleProOrdDTO();
        if (supplierId > 0) {
            dto.setSupplierId(supplierId);
        }
        if (proId > 0) {
            dto.setProId(proId);
        }
        if (!Strings.isNullOrEmpty(proName)) {
            dto.setProName("%" + proName + "%");
        }
        if (thirdLevelId > -1) {
            dto.setProCateId(thirdLevelId);
        }
        if (!Strings.isNullOrEmpty(startPrice)) {
            dto.setStartPrice(new BigDecimal(startPrice));
        }
        if (!Strings.isNullOrEmpty(endPrice)) {
            dto.setEndPrice(new BigDecimal(endPrice));
        }
        if (!Strings.isNullOrEmpty(startTime)) {
            dto.setStartTime(DateUtil.parseTimestamp(startTime));
        }
        if (!Strings.isNullOrEmpty(endTime)) {
            dto.setEndTime(DateUtil.parseTimestamp(endTime));
        }
        PagerControl<SaleProOrdDTO> pc = OrdClient.getSaleProductPager(dto, StageHelper.getPageInfo(request));
        if (pc != null && pc.getEntityList() != null && pc.getEntityList().size() > 0) {
            for (SaleProOrdDTO sp : pc.getEntityList()) {
                sp.setSupplier(ProSupplierClient.getSupplierById(sp.getSupplierId()));
                ProCategory category = ProCategoryClient.getProCategoryById(sp.getProCateId());
                if (category != null) {
                    sp.setProCateName(category.getCategoryName());
                }
            }
        }
        model.addAttribute("proId", proId == -1 ? null : proId);
        model.addAttribute("proName", proName);
        model.addAttribute("supplierId", supplierId);
        model.addAttribute("startPrice", startPrice);
        model.addAttribute("endPrice", endPrice);
        model.addAttribute("startTime", startTime);
        model.addAttribute("endTime", endTime);
        model.addAttribute("pc", pc);
        model.addAttribute("thirdLevelId", thirdLevelId);
        return "/pms/supplier/listSellPro";
    }


}


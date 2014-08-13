package com.meiliwan.emall.pms.service;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.pms.bean.ProSupplier;
import com.meiliwan.emall.pms.dao.ProSupplierDao;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * User: guangdetang
 * Date: 13-6-9
 * Time: 下午1:37
 */
@Service
public class ProSupplierService extends DefaultBaseServiceImpl {

    @Autowired
    private ProSupplierDao proSupplierDao;

    /**
     * 根据ID获得对象
     * @param supplierId
     * @param resultObj
     */
    @IceServiceMethod
    public void getSupplierById(JsonObject resultObj, int supplierId) {
        if (supplierId > 0) {
            addToResult(proSupplierDao.getEntityById(supplierId), resultObj);
        }
    }

    /**
     * 运营后台列表管理
     * @param supplier
     * @param pageInfo
     * @param resultObj
     */
    @IceServiceMethod
    public void getSupplierPager(JsonObject resultObj, ProSupplier supplier, PageInfo pageInfo) {
        if (supplier != null && pageInfo != null) {
            if (supplier.getSupplierName() != null && !Strings.isNullOrEmpty(supplier.getSupplierName().trim())) {
                supplier.setSupplierName("%" + supplier.getSupplierName() + "%");
            } else {
                supplier.setSupplierName(null);
            }
            if (supplier.getSupplierLinkman() != null && !Strings.isNullOrEmpty(supplier.getSupplierLinkman().trim())) {
                supplier.setSupplierLinkman("%" + supplier.getSupplierLinkman() + "%");
            } else {
                supplier.setSupplierLinkman(null);
            }
            addToResult(proSupplierDao.getPagerByObj(supplier, pageInfo, null, "order by create_time desc"), resultObj);
        }
    }

    /**
     * 获得所有未删除的供应商list列表。
     * 用于添加商品的时候查询出所有供应商下拉列表
     * @param supplier
     * @param resultObj
     */
    @IceServiceMethod
    public void getSupplierList(JsonObject resultObj, ProSupplier supplier) {
        if (supplier != null) {
            supplier.setState(GlobalNames.STATE_VALID);
            addToResult(proSupplierDao.getListByObj(supplier, "", "order by create_time desc"), resultObj);
        }
    }

    /**
     * 添加供应商
     * @param supplier
     * @param resultObj
     */
    @IceServiceMethod
    public void saveSupplier(JsonObject resultObj, ProSupplier supplier) {
        if (supplier != null) {
            supplier.setCreateTime(DateUtil.getCurrentTimestamp());
            int result = proSupplierDao.insert(supplier);
            addToResult(result > 0 ? true : false, resultObj);
        }
    }

    /**
     * 修改供应商
     * @param supplier
     * @param resultObj
     */
    @IceServiceMethod
    public void updateSupplier(JsonObject resultObj, ProSupplier supplier) {
        if (supplier != null) {
            int result = proSupplierDao.update(supplier);
            addToResult(result > 0 ? true : false, resultObj);
        }
    }

    /**
     * 检查供应商名字是否一样
     * @param supplierName
     * @param resultObj
     */
    @IceServiceMethod
    public void getRepeatSupplier(JsonObject resultObj, String supplierName) {
        if (!Strings.isNullOrEmpty(supplierName)) {
            ProSupplier supplier = new ProSupplier();
            supplier.setSupplierName(supplierName);
            List<ProSupplier> list = proSupplierDao.getListByObj(supplier);
            boolean noSame = false;
            if (list != null && list.size() > 0) {
                for (ProSupplier pp : list) {
                    if (pp.getSupplierName().equals(supplierName)) {
                        noSame = true;
                        break;
                    }
                }
                addToResult(noSame, resultObj);
            } else {
                addToResult(false, resultObj);
            }
        }
    }
}

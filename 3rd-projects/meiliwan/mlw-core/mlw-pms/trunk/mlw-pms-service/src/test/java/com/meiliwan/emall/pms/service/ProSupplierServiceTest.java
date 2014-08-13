package com.meiliwan.emall.pms.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.pms.BaseTest;
import com.meiliwan.emall.pms.bean.ProSupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Created by guangdetang on 13-8-20.
 */
public class ProSupplierServiceTest  extends BaseTest {
    @Autowired
    private ProSupplierService supplierService;
    private JsonObject resultObj = new JsonObject();
    @Test
    public void testGetSupplierById() throws Exception {
        supplierService.getSupplierById(resultObj,1);
    }

    @Test
    public void testGetSupplierPager() throws Exception {
        ProSupplier supplier = new ProSupplier();
        supplier.setOperateType((short)1);
        supplierService.getSupplierPager(resultObj,supplier,new PageInfo(1,10));
    }

    @Test
    public void testGetSupplierList() throws Exception {
        ProSupplier supplier = new ProSupplier();
        supplier.setState((short)0);
        supplierService.getSupplierList(resultObj,supplier);
    }

    @Test
    public void testSaveSupplier() throws Exception {
        ProSupplier supplier = new ProSupplier();
        supplier.setState((short)0);
        supplier.setOperateType((short) 1);
        supplier.setCreateTime(DateUtil.getCurrentTimestamp());
        supplier.setSupplierLinkman("联系人");
        supplier.setSupplierName("供应商名称");
        supplier.setSupplierPhone("15177940168");
        supplier.setSupplierOtherPhone("13999999999");
        supplierService.saveSupplier(resultObj,supplier);
    }

    @Test
    public void testUpdateSupplier() throws Exception {
        ProSupplier supplier = new ProSupplier();
        supplier.setSupplierId(1);
        supplier.setSupplierName("修改供应商名称");
        supplierService.updateSupplier(resultObj,supplier);
    }

    @Test
    public void testGetRepeatSupplier() throws Exception {
        supplierService.getRepeatSupplier(resultObj,"供应商");
    }
}

package com.meiliwan.emall.pms.client;

import com.meiliwan.emall.pms.bean.ProCategory;
import org.testng.annotations.Test;

import java.util.List;

/**
 * User: wuzixin
 * Date: 13-8-26
 * Time: 上午10:59
 */
public class ProCategoryClientTest {
    @Test
    public void testAddProCategory() throws Exception {
        ProCategory category = new ProCategory();
        category.setCategoryName("休闲食品");
        category.setAdminId(11111);
        category.setParentId(0);
        category.setState((short)1);
        category.setSeachKeyword("食品");
        boolean suc = ProCategoryClient.addProCategory(category);

    }

    @Test
    public void testUpdateProCategory() throws Exception {
        ProCategory category = new ProCategory();
        category.setCategoryId(2);
        category.setCategoryName("休闲食品");
        boolean suc = ProCategoryClient.updateProCategory(category);

    }

    @Test
    public void testGetProCategoryById() throws Exception {
        ProCategory category = ProCategoryClient.getProCategoryById(0);
        System.out.println(category);
    }

    @Test
    public void testGetAllCategory() throws Exception {
        List<ProCategory> list = ProCategoryClient.getAllCategory();
    }

    @Test
    public void testGetTreeCategory() throws Exception {
       List<ProCategory> list = ProCategoryClient.getTreeCategory();
    }

    @Test
    public void testGetListByPId() throws Exception {
        ProCategory category = new ProCategory();
        category.setParentId(2);
        List<ProCategory> list = ProCategoryClient.getListByPId(category);
    }

    @Test
    public void testGetListByCategoryId() throws Exception {
        List<ProCategory> list = ProCategoryClient.getListByCategoryId(2);
    }

    @Test
    public void testDeleteProCategory() throws Exception {
        boolean suc = ProCategoryClient.deleteProCategory(3,2);
    }

    @Test
    public void testGetCategoryByLevelId() throws Exception {
        ProCategory category = ProCategoryClient.getCategoryByLevelId(2,3,4);
    }

    @Test
    public void testGetTreeSingleNoteCategoryById() throws Exception {
        ProCategory category = ProCategoryClient.getTreeSingleNoteCategoryById(2);

    }

    @Test
    public void testGetCategoruByProId() throws Exception {
       ProCategory category = ProCategoryClient.getCategoruByProId(296);
       System.out.println(category);
    }

}

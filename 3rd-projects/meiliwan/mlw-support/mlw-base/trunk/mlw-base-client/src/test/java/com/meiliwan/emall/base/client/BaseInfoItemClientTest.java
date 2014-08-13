package com.meiliwan.emall.base.client;

import java.util.List;

import org.testng.annotations.Test;

import com.meiliwan.emall.base.bean.BaseInfoItem;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;

/**
 * User: wuzixin
 * Date: 13-8-26
 * Time: 下午4:24
 */
public class BaseInfoItemClientTest {
    @Test
    public void testAddBaseInfoItem() throws Exception {
        BaseInfoItem bif = new BaseInfoItem();
        bif.setParentId(0);
        bif.setInfoItemName("关于我们");
        bif.setItemType((short) 1);
        bif.setFileName("about");

        BaseInfoItemClient.addBaseInfoItem(bif);
    }

    @Test
    public void testUpdateBaseInfoItem() throws Exception {
       BaseInfoItem item = new BaseInfoItem();
        item.setInfoItemId(1);
        item.setFileName("关于我们1");
        BaseInfoItemClient.updateBaseInfoItem(item);

    }

    @Test
    public void testGetBaseInfoItemById() throws Exception {
        BaseInfoItem item = BaseInfoItemClient.getBaseInfoItemById(1);
    }

    @Test
    public void testGetListByPager() throws Exception {
        BaseInfoItem item = new BaseInfoItem();
        item.setInfoItemId(1);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPage(1);
        pageInfo.setStartIndex(1);

        PagerControl<BaseInfoItem> pc = BaseInfoItemClient.getListByPager(item,pageInfo);
    }

    @Test
    public void testDeleteBaseInfoItem() throws Exception {
       BaseInfoItemClient.deleteBaseInfoItem(1);
    }

    @Test
    public void testGetListByBaseIT() throws Exception {
        BaseInfoItem item = new BaseInfoItem();
        item.setInfoItemId(1);
        List<BaseInfoItem> list = BaseInfoItemClient.getListByBaseIT(item);
    }
}

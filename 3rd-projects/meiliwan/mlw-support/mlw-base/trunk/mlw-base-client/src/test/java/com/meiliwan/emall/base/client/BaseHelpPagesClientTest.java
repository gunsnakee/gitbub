package com.meiliwan.emall.base.client;

import com.meiliwan.emall.base.bean.BaseHelpPages;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-12-6
 * Time: 上午11:53
 * To change this template use File | Settings | File Templates.
 */
public class BaseHelpPagesClientTest  {

    @Test
    public void testCRU(){

        BaseHelpPages  pages = new BaseHelpPages();
        pages.setShortUrl("study");
        pages.setPageName("学习");
        pages.setAdminId(45);
        pages.setContent("i love study");

        BaseHelpPagesClient.addHelpPage(pages);

        BaseHelpPages queryPage = BaseHelpPagesClient.getHelpPageByShortUrl(pages.getShortUrl());
        System.out.println(queryPage);

        Assert.assertNotNull(queryPage);
        queryPage.setPageName("历史");
        BaseHelpPagesClient.updateHelpPageById(queryPage);

        pages = BaseHelpPagesClient.getHelpPageById(queryPage.getId());
        System.out.println(pages);

        Assert.assertTrue(pages.getPageName().equals("历史"));

    }
}

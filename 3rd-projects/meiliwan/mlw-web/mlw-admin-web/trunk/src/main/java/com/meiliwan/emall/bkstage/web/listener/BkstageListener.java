package com.meiliwan.emall.bkstage.web.listener;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.meiliwan.emall.bkstage.web.controller.bkstage.view.LoginUser;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sean on 13-6-10.
 */
public class BkstageListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Cache<Integer, LoginUser> cahce = CacheBuilder.newBuilder().expireAfterAccess(30, TimeUnit.MINUTES).maximumSize(1024).build(new CacheLoader<Integer, LoginUser>() {
            @Override
            public LoginUser load(Integer key) throws Exception {
                LoginUser o = new LoginUser();
                return o;
            }
        });
        sce.getServletContext().setAttribute("onlineUser", cahce);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}

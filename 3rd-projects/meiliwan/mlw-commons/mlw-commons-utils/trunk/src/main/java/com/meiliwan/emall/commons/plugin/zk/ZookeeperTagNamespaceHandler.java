package com.meiliwan.emall.commons.plugin.zk;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class ZookeeperTagNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("configurer", new ZookeeperConfigurerParser());
        registerBeanDefinitionParser("zkResource", new ZookeeperResourcerParser());
    }

}

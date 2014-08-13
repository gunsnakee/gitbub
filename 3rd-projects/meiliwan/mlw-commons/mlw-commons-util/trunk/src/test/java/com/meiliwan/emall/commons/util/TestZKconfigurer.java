package com.meiliwan.emall.commons.util;

import java.util.Calendar;
import java.util.Enumeration;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.testng.annotations.Test;

import com.meiliwan.emall.commons.BaseTest;
import com.meiliwan.emall.commons.util.configurer.ZookeeperConfigurer;

public class TestZKconfigurer extends BaseTest{


	@Test
	public void testConfig(){
		System.out.println("-----------------");
		String a = ZookeeperConfigurer.getProperty("a");
		System.out.println(a);
		System.out.println("-----------------");
	}
}

package com.meiliwan.emall.core.db.shard.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.meiliwan.emall.core.db.shard.demo.service.UserService;

@ContextConfiguration(locations = { "classpath:conf/spring/dal-spring.xml", "classpath:conf/spring/service-spring.xml" })
public class DemoTest extends AbstractTestNGSpringContextTests{

	@Autowired
	UserService userService;

	@Test
	public void testCreateUsers() {
		try {
			userService.createUsers();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

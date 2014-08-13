package com.meiliwan.emall.service.union.test;

import com.meiliwan.emall.service.union.UnionTaskService;

public class MQTest {

	public static void main(String[] args) {
		
		UnionTaskService unionTaskService = new UnionTaskService();
		unionTaskService.bgRun();

	}

}

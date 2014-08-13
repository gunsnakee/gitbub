package com.meiliwan.emall.bkstage.service;

import java.util.List;

import org.testng.annotations.Test;

public class MaketMailServiceTest {

	@Test
	public void test(){
		List<String> list = MaketMailService.queryByLimit(1, 1000);
		for (String string : list) {
			
			System.out.println(string);
		}
	}
}

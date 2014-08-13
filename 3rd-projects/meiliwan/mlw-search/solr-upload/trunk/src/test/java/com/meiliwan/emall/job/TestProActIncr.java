package com.meiliwan.emall.job;

import java.util.List;

import com.meiliwan.emall.commons.bean.ProActInfo;
import com.meiliwan.emall.commons.util.RedisProActInfoUtil;

public class TestProActIncr {

	public static void main(String[] args) throws InterruptedException {
		while(true){
			int[] updatedActIds = RedisProActInfoUtil.getUpdatedActIds(false);
			for(int i = 0 ; i < updatedActIds.length; i ++){
				System.out.println(updatedActIds[i]);
			}
			List<ProActInfo> delProActInfos = RedisProActInfoUtil.getDelProActInfos(false);
			System.out.println();
			for(ProActInfo pai : delProActInfos){
				System.out.println(pai.getActId() + "-" + pai.getProId());
			}
			Thread.sleep(10000);
			System.out.println("-------------------------------");
		}
	}

}

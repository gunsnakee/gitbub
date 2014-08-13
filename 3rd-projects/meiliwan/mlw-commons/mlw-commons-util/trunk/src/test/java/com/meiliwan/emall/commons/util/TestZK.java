package com.meiliwan.emall.commons.util;

import com.meiliwan.emall.commons.BaseTest;
import com.meiliwan.emall.commons.util.ZKClient.ChildrenWatcher;
import org.testng.annotations.Test;

public class TestZK extends BaseTest{

	@Test
	public void watchNode() throws InterruptedException{
		ChildrenWatcher cw = new ChildrenWatcher() {
			
			@Override
			public void nodeRemoved(String node) {
				
				System.out.println("removed : " + node);
			}
			
			@Override
			public void nodeAdded(String node) {
				// TODO Auto-generated method stub
				System.out.println("added : " + node);
			}
		};
		ZKClient.get().watchChildren("/redis/cacheGroupInfo", cw);
	}



	
	public static void main(String[] args) throws InterruptedException {
		new TestZK().watchNode();
	}

}

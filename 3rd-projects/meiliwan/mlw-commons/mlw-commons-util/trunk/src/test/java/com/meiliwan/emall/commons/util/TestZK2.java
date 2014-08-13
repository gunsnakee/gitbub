package com.meiliwan.emall.commons.util;

import org.apache.zookeeper.KeeperException;
import org.testng.annotations.Test;

import com.meiliwan.emall.commons.BaseTest;
import com.meiliwan.emall.commons.util.ZKClient.ChildrenWatcher;
import com.meiliwan.emall.commons.util.ZKClient.StringValueWatcher;



public class TestZK2 extends BaseTest{

	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 */
	@Test
	public void test() throws KeeperException, InterruptedException {
		// TODO Auto-generated method stub
		ZKClient.get().createIfNotExist("/mlwconf/demo");
		ZKClient.get().watchStrValueNode("/mlwconf/demo/ice_config.xml", new StringValueWatcher() {
			
			@Override
			public void valueChaned(String l) {
				System.out.println("9999");
				
			}
		});
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
		ZKClient.get().watchChildren("/mlwconf/demo", cw);
		System.out.println("------");
	}
	
	public static void main(String[] args) throws KeeperException, InterruptedException {
		new TestZK2().test();
	}

}

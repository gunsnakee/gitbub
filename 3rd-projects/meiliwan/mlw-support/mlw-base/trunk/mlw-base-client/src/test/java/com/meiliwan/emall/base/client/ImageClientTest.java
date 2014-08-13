package com.meiliwan.emall.base.client;

public class ImageClientTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String primFileName = "pro/tmp/20130715163901666.jpg.jpg.tmp.jpg";
		String objId = "32";
		String sizeRule = "100x100,200x200";
		int index = 1;
        String result = ImageServiceClient.replaceImg(primFileName, objId, sizeRule, index);
        
        System.out.println(result);

	}

}

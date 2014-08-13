package com.meiliwan.emall.commons.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.meiliwan.emall.commons.jmagick.ImageUtil;

public class ImageUtilTest {

	public static void main(String[] args) throws IOException {
		FileInputStream input = new FileInputStream(new File("/Users/lsf/Pictures/liushishi0708.jpg"));
		boolean result = ImageUtil.isImageType(input);
		
		System.out.println(result);
	}
	
}

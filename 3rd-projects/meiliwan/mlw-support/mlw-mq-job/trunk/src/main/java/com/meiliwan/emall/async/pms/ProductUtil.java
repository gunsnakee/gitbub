package com.meiliwan.emall.async.pms;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.pms.bean.ProProduct;
import com.meiliwan.emall.pms.client.ProProductClient;

public class ProductUtil {

	private final static MLWLogger LOG = MLWLoggerFactory
			.getLogger(ProductUtil.class);

	// 状态有效
	public static final short STATE_VALID = 1;

	public static void productPublish(int proId) {

		try {
			ProProduct product = ProProductClient.getWholeProductById(proId);
			product.setState(STATE_VALID);
			product.setOnTime(new Date());
			product.setUpdateTime(new Date());

			Boolean upResult = ProProductClient.updateByProduct(product);
			if (upResult != null && upResult) {
				String previewBaseDir = ConfigOnZk.getInstance().getValue(
						"web/system.properties", "product.prev.base.dir");
				String previewPath = previewBaseDir + File.separator
						+ getProDir(proId) + File.separator + proId + ".html";
				File file = new File(previewPath);
				if (!file.exists()) {

					// ProReleaseController.writeProDetail(product);
				}

				String publishBaseDir = ConfigOnZk.getInstance().getValue(
						"web/system.properties", "product.publish.base.dir");
				String proPath = publishBaseDir + File.separator
						+ getProDir(proId);
				File pubDir = new File(proPath);
				if (!pubDir.exists()) {
					pubDir.mkdirs();
				}

				FileUtils.copyFile(file, new File(proPath + File.separator
						+ proId + ".html"));

				// result.addProperty("status", "0");

				LOG.debug("product html '" + proPath + File.separator + proId
						+ ".html' published success.");
			}
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			LOG.error(e, proId, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error(e, proId, null);
		}
	}

	private static String getProDir(int proId) {
		String proIdStr = String.valueOf(proId);
		int diffNum = 8 - proIdStr.length();
		if (diffNum > 0) {
			String zeroStr = "";
			for (int i = 0; i < diffNum; i++) {
				zeroStr += "0";
			}

			proIdStr = zeroStr + proIdStr;
		}

		String part1 = proIdStr.substring(0, proIdStr.length() - 6);
		String part2 = proIdStr.substring(part1.length(), part1.length() + 3);
		// String part3 = proIdStr.substring(proIdStr.length() - 3);

		return part1 + File.separator + part2;
	}

}

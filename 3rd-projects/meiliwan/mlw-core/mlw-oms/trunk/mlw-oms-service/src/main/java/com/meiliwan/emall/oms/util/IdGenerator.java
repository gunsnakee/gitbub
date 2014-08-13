package com.meiliwan.emall.oms.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.util.BaseConfig;
import org.apache.commons.configuration.Configuration;

import com.meiliwan.emall.commons.util.ConfigOnZk;

/**
 * 订单号（生成规则：取系统配置文件的instance号2位+日期yyMMddhhmmss+2位流水号）
 * @author yuxiong
 *
 */
public class IdGenerator {

	/**
	 * 
	 * @return
	 */
	private static int ordNum = 0;
    private static String instanceId = null;
    static {
        //setInstanceId(BaseConfig.getValue("server.instanceId"));
        setInstanceId("01");
    }

	public static void setInstanceId(String instanceId) {
		if (instanceId == null || instanceId.length() != 2) {
			throw new IllegalArgumentException(
					"Set instanceId error: the length should be 2 '"
							+ (instanceId == null ? "" : instanceId)
							+ "' is not correct.");
		}
		IdGenerator.instanceId = instanceId;
	}

    /**
     * 生成订单号（生成规则：取系统配置文件的instance号2位+日期yyMMddhhmmss+2位流水号）
     * @return
     */
	public synchronized static String genOrderId() {
		ordNum = ordNum % 100;
		String index = null;
        if(ordNum < 10 )  {
            index = ("0" + ordNum);
        } else{
            index = Integer.toString(ordNum);
        }

		String date=new SimpleDateFormat("yyMMddhhmmss").format(new Date());
		String orderNum = instanceId + date + index;
		ordNum++;
		return orderNum;
	}

    /**
     * 生成逆向退换货订单号
     * @return
     */
    public static String genRetOrderId() {
        return genOrderId();
    }

	public static void main(String[] args) {
		for (int i = 1000; i < 2000; i++) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					System.err.println(genOrderId());
				}
			});
			t.start();
		}
        System.out.println(new Date().getTime());
	}

}

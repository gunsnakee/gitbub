package com.meiliwan.emall.tongji.task;

import com.meiliwan.emall.tongji.task.job.oms.OrderJob;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Sean on 13-11-19.
 */
public class MainApp {

    public static void main(String args[]) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:conf/spring/tongji-integration.xml");
        executeCommand(ctx, args);
    }

    private static void executeCommand(ApplicationContext ctx, String args[]) {
        if (args != null && args.length > 0) {
            //统计商品销量开始时间执行
            if ("exportProSale".equals(args[0]) && args.length == 3) {
                tongjiProSale(ctx, args[1], args[2]);
            }
        }
    }


    private static void tongjiProSale(ApplicationContext ctx, String startDate, String endDate) {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        try {
            df.parse(endDate);//验证string
            c.setTimeInMillis(df.parse(startDate).getTime());
            //List<String> days = new ArrayList<String>();
            OrderJob orderJob =   ctx.getBean(OrderJob.class);


            while (true) {
                if (startDate.compareTo(endDate) < 0) {
                    orderJob.addProSalesBy(startDate);
                    orderJob.mailProSalesExcelByDate(startDate);
                    c.add(Calendar.DAY_OF_YEAR, 1);
                    startDate = df.format(c.getTime());
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}

package com.meiliwan.emall.core.interceptor;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.rabbitmq.MqModel;
import com.meiliwan.emall.commons.rabbitmq.MsgSender;
import com.meiliwan.emall.commons.util.IPUtil;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Created by Sean on 13-8-21.
 */
public class DaoPerfMonitorInterceptor implements MethodInterceptor {
    public DaoPerfMonitorInterceptor() {
    }

    private static final MLWLogger LOG =  MLWLoggerFactory.getLogger(DaoPerfMonitorInterceptor.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        //用 commons-lang 提供的 StopWatch 计时，Spring 也提供了一个 StopWatch
        long start = System.currentTimeMillis();
        Object result = invocation.proceed();

        long end = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        if (invocation.getArguments() != null && invocation.getArguments().length > 0) {
            for (Object o : invocation.getArguments()) {
                sb.append(o).append("\n");
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        String sendMsg = "dao(" + invocation.getThis().getClass().getSimpleName() + "." + invocation.getMethod().getName() + ")" +
                "(" + start + ")(" + end + ")(localIP:" + IPUtil.getLocalIp() + ",param:" + sb + ")";
        try {
            MsgSender.getInstance(MqModel.WATCHLOG).send(
                    MqModel.WATCHLOG, "logService",
                    sendMsg );
        } catch (Exception e) {
        	LOG.error(e, sendMsg, null);
        }
        return result;
    }

//    public static void main(String[] args) {
//
//        Stopwatch clock = new Stopwatch();
//        clock.start(); //计时开始
//        try {
//            Thread.sleep(1300);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        clock.stop();  //计时结束
//
//        System.out.println(clock.elapsed(TimeUnit.MILLISECONDS));
//
//
//    }
}

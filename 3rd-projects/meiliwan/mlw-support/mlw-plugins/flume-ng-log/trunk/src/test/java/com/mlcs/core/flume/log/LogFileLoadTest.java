package com.mlcs.core.flume.log;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mlcs.core.common.util.DateUtil;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

public class LogFileLoadTest {
	private static final Logger log = LoggerFactory
			.getLogger(LogFileLoadTest.class);

	public static void main(String[] args) {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		JoranConfigurator configurator = new JoranConfigurator();
		configurator.setContext(lc);
		lc.reset();
		try {
			configurator.doConfigure("/Users/lsf/work/mlcs/flume-ng-log/src/main/resources/logback.xml");
		} catch (JoranException e) {
			e.printStackTrace();
		}
		StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
		System.out.println("===================");
		
		while(true){
			log.info("Hello {}", "debug message");
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
//		String ptn = "/tmp/md{yyyy/MM/dd}/mytest_%d{yyyy-MM-dd_HH_mm_ss}.log";
//		String result = parseMd(ptn);
//		System.out.print(result);
	}

	
	private static String parseMd(String filePattern){
		String mdPtn = "md\\{";
		if(StringUtils.isBlank(filePattern) || !filePattern.contains("md{")){
			return filePattern;
		}
		String[] parts = filePattern.split(mdPtn);
		String prefix = parts[0];
		
		String[] midParts = parts[1].split("\\}");
		
		String middlePtn = midParts[0];
		String suffix = "";
		if(midParts.length >= 2){
			for(int index = 1; index < midParts.length; index++){
				if(index == 1){
					suffix += midParts[index];
				}else{
					suffix += "}" + midParts[index];
				}
			}
		}
		
		return prefix + DateUtil.formatDate(new Date(), middlePtn) + suffix;
	}
}

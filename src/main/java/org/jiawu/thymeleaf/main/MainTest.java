package org.jiawu.thymeleaf.main;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring3.SpringTemplateEngine;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by jiawuwu on 14-7-21.
 */
public class MainTest {
    public static void main(String[] args){


        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");


        String templateContent = null;

        final Context thymeleafContext = new Context();
        thymeleafContext.setLocale(new Locale("zh"));


        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", new Date());

        thymeleafContext.setVariables(map);


        SpringTemplateEngine engine = (SpringTemplateEngine) ac.getBean("jyTemplateEngine");

        templateContent =  engine.process("header", thymeleafContext);

        System.out.println(templateContent);


    }
}

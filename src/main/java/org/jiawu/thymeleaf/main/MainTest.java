package org.jiawu.thymeleaf.main;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring3.SpringTemplateEngine;

import java.util.Locale;

/**
 * Created by jiawuwu on 14-7-21.
 */
public class MainTest {
    public static void main(String[] args){


        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");


        String templateContent = null;

        final Context thymeleafContext = new Context();
        thymeleafContext.setLocale(new Locale("zh"));


        //Map<String, Object> map = new HashMap<String, Object>();
        //map.put("test", "<wj:demo resultVar=\"ss\"/>\n" +
        //        "<div th:each=\"s : ${ss}\">\n" +
        //        "    <wj:demo resultVar=\"bb\"/>\n" +
        //        "    <p th:text=\"${s}\"></p>\n" +
        //        "    <a th:each=\"a:${bb}\" th:text=\"${a}\"></a>\n" +
        //        "</div>");
        //
        //thymeleafContext.setVariables(map);


        SpringTemplateEngine engine = (SpringTemplateEngine) ac.getBean("jyTemplateEngine");

        templateContent =  engine.process("zh", thymeleafContext);

        System.out.println(templateContent);


    }
}

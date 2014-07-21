package org.jiawu.thymeleaf.processors;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiawuwu on 14-7-21.
 */
public class WjDemoProcessor extends WjBaseProcessor {
    public WjDemoProcessor() {
        super("demo");
    }

    @Override
    protected void modifyModelAttributes(Arguments arguments, Element element) {
        String resultVar = element.getAttributeValue("resultVar");
        List<String> ss = new ArrayList<String>();
        ss.add("111");
        ss.add("222");
        addToModel(arguments,resultVar,ss);
    }
}

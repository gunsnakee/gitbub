package com.meiliwan.emall.commons.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;

import java.util.Calendar;
import java.util.Enumeration;
import java.util.Properties;

public class SystemConfig extends PropertyResourceConfigurer {

    private static Properties sysProperties;
    private static long lastLoadTime = 0l;
    private static long loadInterval = 10 * 60 * 1000;

    private static void appendProperties(Properties properties) {
        if (sysProperties == null) {
            sysProperties = properties;
        } else {
            Enumeration<?> propertyNames = properties.propertyNames();
            while (propertyNames.hasMoreElements()) {
                String pro = (String) propertyNames.nextElement();
                sysProperties.setProperty(pro, properties.getProperty(pro));
            }
        }
    }

    private static Properties getProperties() {
        return sysProperties;
    }

    public static String getDEFAULT_CONTEXT_ROOT() {
        return getProperty("default_context_root");
    }

    public static String getCssRootPath() {
        return getProperty("system.path.resource.css.root");
    }

    public static String getScriptRootPath() {
        return getProperty("system.path.resource.script.root");
    }

    public static String getDateTimePattern() {
        return getProperty("system.format.datetime.pattern");
    }

    public static String getProperty(String name) {
        return getProperties().getProperty(name);
    }

    public static String getProperty(String name, String defaultValue) {
        if (getProperties() == null) return defaultValue;
        String value = getProperties().getProperty(name);
        if (value != null) return value;
        return defaultValue;
    }

    public static int getPropertyInt(String name) {
        String value = getProperties().getProperty(name);
        if (value != null && value.length() > 0) {
            return Integer.valueOf(value);
        }
        return 0;

    }
    
    @Override
    protected void processProperties(ConfigurableListableBeanFactory arg0,
                                     Properties arg1) throws BeansException {
        appendProperties(arg1);
        lastLoadTime = Calendar.getInstance().getTimeInMillis();
    }

}

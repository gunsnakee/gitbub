package com.meiliwan.emall.monitor.web;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.zookeeper.KeeperException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;




@Controller
public class IndexController {
	private static final MLWLogger logger = MLWLoggerFactory.getLogger(IndexController.class);
	
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String home(Locale locale, Model model, HttpServletRequest request) throws InterruptedException, KeeperException {
       
            return "/home";
        
    }
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Locale locale, Model model, HttpServletRequest request) throws InterruptedException, KeeperException {
    			
            return "/home";
    }
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home2(Locale locale, Model model, HttpServletRequest request) throws InterruptedException, KeeperException {
       
            return "/home";
        
    }
    
}

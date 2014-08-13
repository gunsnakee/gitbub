package com.meiliwan.emall.union.controller;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.union.service.EtaoSearchService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: wuzixin
 * Date: 14-5-16
 * Time: 上午9:53
 */
@Controller
@RequestMapping("/etao/search")
public class EtaoSearchController {
    private final static MLWLogger logger = MLWLoggerFactory.getLogger(EtaoSearchController.class);
    private static final ApplicationContext ct = new ClassPathXmlApplicationContext("classpath*:conf/spring/pms/pms-data.xml");
    private static final EtaoSearchService searchService = (EtaoSearchService)ct.getBean("etaoSearchService");

    /**
     * 生成一淘搜索全局数据
     * @param request
     * @param response
     */
    @RequestMapping("/fullIndex")
    public void getFullIndex(HttpServletRequest request,HttpServletResponse response){
        try {
            searchService.getFullIndex();
            logger.info("生成一淘全量数据成功","", WebUtils.getIpAddr(request));
        }catch (Exception e){
            logger.error(e,"生成一淘搜索全局增量失败", WebUtils.getIpAddr(request));
        }
    }

    /**
     * 生成一淘搜索增量数据
     * @param request
     * @param response
     */
    @RequestMapping("/incrementIndex")
    public void getIncrementIndex(HttpServletRequest request,HttpServletResponse response){
        try {
            searchService.getIncrementIndex();
            logger.info("生成一淘增量数据成功","", WebUtils.getIpAddr(request));
        }catch (Exception e){
            logger.error(e,"生成一淘搜索全局增量失败", WebUtils.getIpAddr(request));
        }
    }

    @RequestMapping("/sellerCats")
    public void getSellerCats(HttpServletRequest request,HttpServletResponse response){
        try {
            searchService.getSellerCats();
            logger.info("生成一淘全量数据成功","", WebUtils.getIpAddr(request));
        }catch (Exception e){
            logger.error(e,"生成一淘卖家所卖类目数据失败", WebUtils.getIpAddr(request));
        }
    }

}

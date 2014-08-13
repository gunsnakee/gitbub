package com.meiliwan.emall.bkstage.web.controller.thematic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meiliwan.emall.pms.dto.SimpleProIfStock;
import com.meiliwan.emall.sp2.bean.SpTicketBatchProd;
import com.meiliwan.emall.sp2.client.SpTicketBatchClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.cms2.bean.ThematicModel;
import com.meiliwan.emall.cms2.bean.ThematicPage;
import com.meiliwan.emall.cms2.bean.ThematicPageModel;
import com.meiliwan.emall.cms2.bean.ThematicTemplate;
import com.meiliwan.emall.cms2.client.ThematicPageClient;
import com.meiliwan.emall.cms2.constant.Constant;
import com.meiliwan.emall.cms2.vo.PageImgJsonVo;
import com.meiliwan.emall.cms2.vo.PicAreaJsonVo;
import com.meiliwan.emall.cms2.vo.PicJsonVo;
import com.meiliwan.emall.cms2.vo.ProductAreaJsonVo;
import com.meiliwan.emall.cms2.vo.ProductJsonVo;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ArrayUtils;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.web.WebParamterUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.exception.IceServiceRuntimeException;
import com.meiliwan.emall.pms.bean.ProCategory;
import com.meiliwan.emall.pms.bean.ProSupplier;
import com.meiliwan.emall.pms.bean.SimpleProduct;
import com.meiliwan.emall.pms.client.ProCategoryClient;
import com.meiliwan.emall.pms.client.ProProductClient;
import com.meiliwan.emall.pms.client.ProSupplierClient;
import com.meiliwan.emall.pms.dto.ProductDTO;
import com.meiliwan.emall.sp2.bean.view.SimpleActVO;
import com.meiliwan.emall.sp2.client.ActivityInterfaceClient;
import com.meiliwan.emall.sp2.constant.PrivilegeType;

/**
 * User: wuzixin
 * Date: 14-4-10
 * Time: 下午1:55
 */
@Controller("thematicController")
@RequestMapping("/thematic")
public class ThematicController {
    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

    /**
     * 专题页管理
     *
     * @param model
     * @param request
     */
    @RequestMapping(value = "/list")
    public String list(Model model, HttpServletRequest request) {
        int themeticId = ServletRequestUtils.getIntParameter(request, "pageId", 0);
        String themeName = ServletRequestUtils.getStringParameter(request, "pageName", null);
        ThematicPage page = new ThematicPage();
        if (themeticId > 0) {
            page.setPageId(themeticId);
        }
        if (StringUtils.isNotEmpty(themeName)) {
            page.setPageName(themeName);
        }
        PageInfo pageInfo = StageHelper.getPageInfo(request, "page_id", "desc");
        PagerControl<ThematicPage> pc = ThematicPageClient.getPageByObj(page, pageInfo, pageInfo.getOrderField(), pageInfo.getOrderDirection());
        model.addAttribute("bean", page);
        model.addAttribute("pc", pc);
        return "/thematic/list";
    }

    /**
     * 增加专题
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/add")
    public String add(Model model, HttpServletRequest request, HttpServletResponse response) {
        int handle = ServletRequestUtils.getIntParameter(request, "handle", 0);
        if (handle > 0) {
            String pageName = ServletRequestUtils.getStringParameter(request, "pageName", "");
            String enName = ServletRequestUtils.getStringParameter(request, "enName", "");
            String endTime = ServletRequestUtils.getStringParameter(request, "endTime", null);
            int templateId = ServletRequestUtils.getIntParameter(request, "templateId", 1);
            String seoTitle = ServletRequestUtils.getStringParameter(request, "seoTitle", null);
            String seoDescp = ServletRequestUtils.getStringParameter(request, "seoDescp", null);
            String seoKey = ServletRequestUtils.getStringParameter(request, "seoKey", null);

            ThematicPage pageExt = ThematicPageClient.getPageByEnName(enName);
            if (pageExt != null) {
                return StageHelper.writeDwzSignal("300", "英文名称已经存在,请重新输入", "10346", StageHelper.DWZ_FORWARD, "/thematic/add", response);
            }

            ThematicPage page = new ThematicPage();
            page.setPageName(pageName);
            page.setEnName(enName);
            if (StringUtils.isNotEmpty(endTime)) {
                page.setEndTime(DateUtil.parseTimestamp(endTime));
            }
            page.setBeginTime(new Date());
            page.setTemplateId(templateId);
            page.setTitle(seoTitle);
            page.setDescp(seoDescp);
            page.setKeyword(seoKey);
            page.setAdminId(StageHelper.getLoginUser(request).getBksAdmin().getAdminId());
            page.setUrl("http://www.meiliwan.com/mopwan/" + enName + ".html");
            try {
                int pageId = ThematicPageClient.addPage(page);
                if (pageId > 0) {
                    return StageHelper.writeDwzSignal("200", "添加专题页成功", "10345", StageHelper.DWZ_CLOSE_CURRENT, "/thematic/list", response);
                } else {
                    return StageHelper.writeDwzSignal("300", "添加专题页失败，请联系管理员", "10346", StageHelper.DWZ_FORWARD, "/thematic/add", response);
                }
            } catch (IceServiceRuntimeException e) {
                logger.error(e, page, WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "操作失败，请联系管理员", "10346", StageHelper.DWZ_FORWARD, "/thematic/add", response);
            } catch (Exception e) {
                logger.error(e, page, WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "操作失败，请联系管理员", "10346", StageHelper.DWZ_FORWARD, "/thematic/add", response);
            }
        } else {
            List<ThematicTemplate> templates = ThematicPageClient.getTemplateList();
            model.addAttribute("templates", templates);
            return "/thematic/add";
        }
    }

    /**
     * 编辑专题
     *
     * @param model
     * @param request
     * @param response
     */
    @RequestMapping(value = "/update")
    public String update(Model model, HttpServletRequest request, HttpServletResponse response) {
        int handle = ServletRequestUtils.getIntParameter(request, "handle", 0);
        if (handle > 0) {
            int pageId = ServletRequestUtils.getIntParameter(request, "pageId", 0);
            String pageName = ServletRequestUtils.getStringParameter(request, "pageName", "");
            String enName = ServletRequestUtils.getStringParameter(request, "enName", "");
            String endTime = ServletRequestUtils.getStringParameter(request, "endTime", null);
            int templateId = ServletRequestUtils.getIntParameter(request, "templateId", 1);
            String seoTitle = ServletRequestUtils.getStringParameter(request, "seoTitle", null);
            String seoDescp = ServletRequestUtils.getStringParameter(request, "seoDescp", null);
            String seoKey = ServletRequestUtils.getStringParameter(request, "seoKey", null);

            ThematicPage page = new ThematicPage();
            page.setPageId(pageId);
            page.setPageName(pageName);
            page.setEnName(enName);
            if (StringUtils.isNotEmpty(endTime)) {
                page.setEndTime(DateUtil.parseTimestamp(endTime));
            }
            page.setTemplateId(templateId);
            page.setTitle(seoTitle);
            page.setDescp(seoDescp);
            page.setKeyword(seoKey);
            page.setUrl("http://www.meiliwan.com/mopwan/" + enName + ".html");
            try {
                ThematicPageClient.updatePage(page);
                return StageHelper.writeDwzSignal("200", "修改专题页成功", "10345", StageHelper.DWZ_CLOSE_CURRENT, "/thematic/list", response);
            } catch (IceServiceRuntimeException e) {
                logger.error(e, page, WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "操作失败，请联系管理员", "10354", StageHelper.DWZ_FORWARD, "/thematic/update", response);
            } catch (Exception e) {
                logger.error(e, page, WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "操作失败，请联系管理员", "10354", StageHelper.DWZ_FORWARD, "/thematic/update", response);
            }
        } else {
            int pageId = ServletRequestUtils.getIntParameter(request, "pageId", 0);
            ThematicPage page = ThematicPageClient.getPageById(pageId);
            List<ThematicTemplate> templates = ThematicPageClient.getTemplateList();
            model.addAttribute("templates", templates);
            model.addAttribute("bean", page);
            return "/thematic/update";
        }
    }

    /**
     * 删除专题
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/del")
    public String del(HttpServletRequest request, HttpServletResponse response) {
        int pageId = ServletRequestUtils.getIntParameter(request, "pageId", 0);
        try {
            ThematicPageClient.delPage(pageId);
            return StageHelper.writeDwzSignal("200", "删除专题成功", "10345", StageHelper.DWZ_FORWARD, "/thematic/list", response);
        } catch (IceServiceRuntimeException e) {
            logger.error(e, pageId, WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("300", "操作失败，请联系管理员", "10345", StageHelper.DWZ_FORWARD, "/thematic/list", response);
        } catch (Exception e) {
            logger.error(e, pageId, WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("300", "操作失败，请联系管理员", "10345", StageHelper.DWZ_FORWARD, "/thematic/update", response);
        }
    }

    /**
     * 专题页 页面设置
     *
     * @param model
     * @param request
     * @param response
     */
    @RequestMapping(value = "/pageSet")
    public String pageSet(Model model, HttpServletRequest request, HttpServletResponse response) {
        int handle = ServletRequestUtils.getIntParameter(request, "handle", 0);
        if (handle > 0) {
            int pageId = ServletRequestUtils.getIntParameter(request, "pageId", 0);
            PageImgJsonVo vo = setPageImgJsonVo(request);
            Gson gson = new Gson();
            String jsonData = gson.toJson(vo);
            try {
                int count = ThematicPageClient.updataPagdJsonData(pageId, jsonData);
                if (count > 0) {
                    int modelCount = ServletRequestUtils.getIntParameter(request, "modelCount", 0);
                    if (modelCount > 0) {
                        String pageName = ServletRequestUtils.getStringParameter(request, "pageName", null);
                        if (StringUtils.isNotEmpty(pageName)) {
                            ThematicPageClient.getThematicPageHtml(pageName);
                        }
                    }
                }
                return StageHelper.writeDwzSignal("200", "设置页面成功", "10345", StageHelper.DWZ_FORWARD, "/thematic/pageSet?pageId=" + pageId, response);
            } catch (IceServiceRuntimeException e) {
                logger.error(e, pageId, WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "设置页面失败，请联系管理员", "10345", StageHelper.DWZ_FORWARD, "/thematic/pageSet", response);
            } catch (Exception e) {
                logger.error(e, pageId, WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "设置页面失败，请联系管理员", "10345", StageHelper.DWZ_FORWARD, "/thematic/pageSet", response);
            }
        } else {//显示
            int pageId = ServletRequestUtils.getIntParameter(request, "pageId", 0);
            ThematicPage page = ThematicPageClient.getPageById(pageId);
            if (StringUtils.isNotEmpty(page.getJsonData())) {
                PageImgJsonVo vo = new Gson().fromJson(page.getJsonData(), PageImgJsonVo.class);
                model.addAttribute("json", vo);
            }
            List<ThematicPageModel> list = ThematicPageClient.getPMListByPageId(pageId);
            int modelCount = 0;
            for (ThematicPageModel pm : list) {
                if (pm.getModelId() > 0) {
                    modelCount = modelCount + 1;
                }
            }
            model.addAttribute("bean", page);
            model.addAttribute("models", list);
            model.addAttribute("modelCount", modelCount);
            return "/thematic/page_set";
        }
    }

    private PageImgJsonVo setPageImgJsonVo(HttpServletRequest request) {
        String bgColor = ServletRequestUtils.getStringParameter(request, "bgColor", "");
        String headImg = ServletRequestUtils.getStringParameter(request, "headImg", "");
        String html5HeadImg = ServletRequestUtils.getStringParameter(request, "html5HeadImg", "");
        String mdImg = ServletRequestUtils.getStringParameter(request, "mdImg", "");
        String returnImg = ServletRequestUtils.getStringParameter(request, "returnImg", "");

        PageImgJsonVo vo = new PageImgJsonVo();
        vo.setBgColor(bgColor);
        vo.setHeadImg(headImg);
        vo.setHtml5HeadImg(html5HeadImg);
        vo.setMdHeadImg(mdImg);
        vo.setReturnTopImg(returnImg);
        return vo;
    }

    /**
     * 增加专题页对应的区域，例如：商品区或者图片区
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/add-area")
    public String addArea(Model model, HttpServletRequest request, HttpServletResponse response) {
        int handle = ServletRequestUtils.getIntParameter(request, "handle", 0);
        if (handle > 0) {
            int pageId = ServletRequestUtils.getIntParameter(request, "pageId", 0);
            int areaType = ServletRequestUtils.getIntParameter(request, "areaType", 0);
            ThematicPageModel page = new ThematicPageModel();
            page.setPageId(pageId);
            page.setType(areaType);
            page.setIsHide((int) Constant.STATE_VALID);
            try {
                ThematicPageClient.addPageModel(page);
                return StageHelper.writeDwzSignal("200", "增加专题页面区域成功", "10355", StageHelper.DWZ_CLOSE_CURRENT, "/thematic/pageSet?pageId=" + pageId, response);
            } catch (IceServiceRuntimeException e) {
                logger.error(e, page, WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "增加页面区域失败，请联系管理员", "10359", StageHelper.DWZ_FORWARD, "/thematic/add-area", response);
            } catch (Exception e) {
                logger.error(e, page, WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "增加页面区域失败，请联系管理员", "10359", StageHelper.DWZ_FORWARD, "/thematic/add-area", response);
            }
        } else {
            int pageId = ServletRequestUtils.getIntParameter(request, "pageId", 0);
            model.addAttribute("pageId", pageId);
            return "/thematic/add_area";
        }
    }

    /**
     * 根据关系ID，删除专题页相关区域
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/del-area")
    public String delArea(HttpServletRequest request, HttpServletResponse response) {
        int id = ServletRequestUtils.getIntParameter(request, "pmId", 0);
        int pageId = ServletRequestUtils.getIntParameter(request, "pageId", 0);
        try {
            ThematicPageClient.updatePMHide(id);
            return StageHelper.writeDwzSignal("200", "删除专题页面区域成功", "10355", StageHelper.DWZ_FORWARD, "/thematic/pageSet?pageId=" + pageId, response);
        } catch (IceServiceRuntimeException e) {
            logger.error(e, pageId, WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("300", "删除专题页面区域失败，请联系管理员", "10355", StageHelper.DWZ_FORWARD, "/thematic/pageSet?pageId=" + pageId, response);
        } catch (Exception e) {
            logger.error(e, pageId, WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("300", "删除专题页面区域失败，请联系管理员", "10355", StageHelper.DWZ_FORWARD, "/thematic/pageSet?pageId=" + pageId, response);
        }
    }


    @RequestMapping(value = "/edit-area")
    public String editArea(Model model, HttpServletRequest request, HttpServletResponse response) {
        int handle = ServletRequestUtils.getIntParameter(request, "handle", 0);
        int areaType = ServletRequestUtils.getIntParameter(request, "type", Constant.PROD_AREA);
        if (handle > 0) {
            if (areaType == Constant.PROD_AREA) {
                ThematicPageModel pageModel = getProdAreaVO(request);
                try {
                    ThematicPageClient.updatePageModel(pageModel);
                    //return StageHelper.writeDwzSignal("200", "商品区编辑成功", "b1f540bbc840b3fd08007fc21435b81f", StageHelper.DWZ_CLOSE_CURRENT, "/thematic/pageSet?pageId=" + pageModel.getPageId(), response);
                    return StageHelper.writeDwzSignal("200", "商品区编辑成功", "10362", StageHelper.DWZ_FORWARD, "/thematic/edit-area?pageId=" + pageModel.getPageId() + "&pmId=" + pageModel.getId(), response);
                } catch (IceServiceRuntimeException e) {
                    logger.error(e, pageModel, WebUtils.getIpAddr(request));
                    return StageHelper.writeDwzSignal("300", "商品区编辑失败，请联系管理员", "10362", StageHelper.DWZ_FORWARD, "/thematic/edit-area?pageId=" + pageModel.getPageId() + "&pmId=" + pageModel.getId(), response);
                } catch (Exception e) {
                    logger.error(e, pageModel, WebUtils.getIpAddr(request));
                    return StageHelper.writeDwzSignal("300", "商品区编辑失败，请联系管理员", "10362", StageHelper.DWZ_FORWARD, "/thematic/edit-area?pageId=" + pageModel.getPageId() + "&pmId=" + pageModel.getId(), response);
                }
            } else {

            }
        } else {
            int id = ServletRequestUtils.getIntParameter(request, "pmId", 0);
            int pageId = ServletRequestUtils.getIntParameter(request, "pageId", 0);
            ThematicPage page = ThematicPageClient.getPageById(pageId);
            ThematicPageModel pageModel = ThematicPageClient.getPageModeById(id);
            model.addAttribute("bean", page);
            model.addAttribute("pm", pageModel);
            if (areaType == Constant.PROD_AREA) {
                ThematicModel md = new ThematicModel();
                md.setType(areaType);
                List<ThematicModel> models = ThematicPageClient.getModelListByEntity(md);
                ProductAreaJsonVo areaJsonVo = new Gson().fromJson(pageModel.getJsonData(), ProductAreaJsonVo.class);
                if (pageModel != null && StringUtils.isNotEmpty(pageModel.getJsonData())) {
                    if (areaJsonVo.getProList() != null && areaJsonVo.getProList().size() > 0) {
                        int size = areaJsonVo.getProList().size();
                        int[] ids = new int[size];
                        Map<Integer, String> markMap = new HashMap<Integer, String>();
                        for (int i = 0; i < size; i++) {
                            ids[i] = areaJsonVo.getProList().get(i).getProId().intValue();
                            markMap.put(areaJsonVo.getProList().get(i).getProId().intValue(), areaJsonVo.getProList().get(i).getMark());
                        }
                        List<SimpleProduct> products = ProProductClient.getSimpleListByProIds(ids);
                        //获取活动列表
                        Map<Integer, Map<PrivilegeType, List<SimpleActVO>>> prosMap = ActivityInterfaceClient.getSimpleActivitiesByProIds(ids);
                        Map<Integer, String> sp = getSPNameList(prosMap);
                        //获取活动价格
                        Map<Integer, BigDecimal> proSpPrice = ActivityInterfaceClient.getActProByProIdsForShowPrice(ids);
                        //
                        for (SimpleProduct product : products) {
                            product.setBarCode(markMap.get(product.getProId()));
                            product.setSkuPropertyStr(null);
                            product.setMarketPrice(null);
                            if (proSpPrice != null && proSpPrice.size() > 0) {
                                if (proSpPrice.get(product.getProId()) != null) {
                                    product.setMarketPrice(proSpPrice.get(product.getProId()));
                                }
                                if (sp.get(product.getProId()) != null) {
                                    product.setSkuPropertyStr(sp.get(product.getProId()));
                                }
                            }
                        }
                        model.addAttribute("prods", products);
                    }
                }
                model.addAttribute("jsonVo", areaJsonVo);
                model.addAttribute("models", models);
                return "/thematic/edit_prod_area";
            } else {
                PicAreaJsonVo areaJsonVo = new Gson().fromJson(pageModel.getJsonData(), PicAreaJsonVo.class);
                List<ThematicModel> models = ThematicPageClient.getModelListByType(Constant.PIC_AREA);
                model.addAttribute("jsonVo", areaJsonVo);
                model.addAttribute("models", models);
                return "/thematic/anchor";
            }
        }
        return "";
    }

    /**
     * 专题页区域编辑 添加商品功能
     *
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/add-area-prod")
    public String addAreaProd(Model model, HttpServletRequest request, HttpServletResponse response) {
        int handle = ServletRequestUtils.getIntParameter(request, "handle", 0);
        if (handle > 0) {
            int pageId = ServletRequestUtils.getIntParameter(request, "pageId", 0);
            int pmId = ServletRequestUtils.getIntParameter(request, "pmId", 0);
            int[] ids = ServletRequestUtils.getIntParameters(request, "ids");
            ThematicPageModel pageModel = ThematicPageClient.getPageModeById(pmId);
            ProductAreaJsonVo vo = new ProductAreaJsonVo();
            List<ProductJsonVo> pjList = new ArrayList<ProductJsonVo>();
            //先获取对应区域已经存在的商品
            if (StringUtils.isNotEmpty(pageModel.getJsonData())) {
                vo = new Gson().fromJson(pageModel.getJsonData(), ProductAreaJsonVo.class);
                if (vo.getProList() != null && vo.getProList().size() > 0) {
                    for (ProductJsonVo productJsonVo : vo.getProList()) {
                        pjList.add(productJsonVo);
                    }
                }
            }
            //获取新添加的商品
            if (ids != null && ids.length > 0) {
                List<SimpleProduct> products = ProProductClient.getSimpleListByProIds(ids);
                if (products != null && products.size() > 0) {
                    for (SimpleProduct product : products) {
                        ProductJsonVo pjv = new ProductJsonVo();
                        pjv.setProId(product.getProId());
                        pjv.setMlwPrice(product.getMlwPrice().doubleValue());
                        pjv.setMarketPrice(product.getMarketPrice().doubleValue());
                        pjv.setProName(product.getProName());
                        pjv.setPicUrl(product.getDefaultImageUri());
                        pjList.add(pjv);
                    }
                }
            }
            vo.setProList(pjList);
            try {
                String jsonData = new Gson().toJson(vo);
                int count = ThematicPageClient.updatePageModelJsonData(pmId, jsonData);
                //修改专题页与模块对应关系表里面的商品串
                if (count > 0) {
                    ThematicPage page = ThematicPageClient.getPageById(pageId);
                    if (ids != null && ids.length > 0) {
                        if (page != null) {
                            String pageJson = "";
                            if (StringUtils.isNotEmpty(page.getProIds())) {
                                pageJson = page.getProIds() + "," + getIdsStr(ids);
                            } else {
                                pageJson = getIdsStr(ids);
                            }
                            ThematicPageClient.updataPageProIds(pageId, pageJson);
                        }
                    }
                }
                return StageHelper.writeDwzSignal("200", "商品加入专题页成功", "4ae9b80a0ce75986a878f4341220c1fc", StageHelper.DWZ_CLOSE_CURRENT, "/thematic/edit-area?pageId=" + pageId + "&pmId=" + pmId, response);
            } catch (IceServiceRuntimeException e) {
                logger.error(e, pageId, WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "商品加入专题页失败，请联系管理员", "10363", StageHelper.DWZ_FORWARD, "/thematic/add-area-prod?pageId=" + pageId + "&pmId=" + pmId, response);
            } catch (Exception e) {
                logger.error(e, pageId, WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "商品加入专题页失败，请联系管理员", "10363", StageHelper.DWZ_FORWARD, "/thematic/add-area-prod?pageId=" + pageId + "&pmId=" + pmId, response);
            }
        } else {
            int pageId = ServletRequestUtils.getIntParameter(request, "pageId", 0);
            int pmId = ServletRequestUtils.getIntParameter(request, "pmId", 0);
            String tkBatchId = ServletRequestUtils.getStringParameter(request, "tkBatchId", null);
            Map<Integer, String> map = getExitsProdId(pageId);
            if (StringUtils.isNotBlank(tkBatchId)) {
                List<SpTicketBatchProd> list = SpTicketBatchClient.getBatchProdsByBatchId(Integer.parseInt(tkBatchId));
                if (list != null && list.size() > 0) {
                    int[] ids = getIdsBylist(list);
                    List<SimpleProIfStock> pds = ProProductClient.getSimpleListWithStockByIds(ids);
                    if (pds != null && pds.size() > 0) {
                        for (SimpleProIfStock pd : pds) {
                            if (map.get(pd.getProId()) != null) {
                                pd.getProduct().setIsCod((short) 1);
                            } else {
                                pd.getProduct().setIsCod((short) 0);
                            }
                        }
                    }
                    model.addAttribute("pds", pds);
                    model.addAttribute("tkType", "tk");
                    model.addAttribute("tkBatchId",tkBatchId);
                }
            } else {
                PagerControl<ProductDTO> pc = null;
                String orderField = ServletRequestUtils.getStringParameter(request, "orderField", "");
                String orderDirection = ServletRequestUtils.getStringParameter(request, "orderDirection", "");
                ProductDTO bean = new ProductDTO();
                bean.setState(1);
                PageInfo pageInfo = WebParamterUtil.getPageInfo(request);
                pageInfo.setOrderField(orderField);
                pageInfo.setOrderDirection(orderDirection);
                if ("".equals(orderField)) {
                    pageInfo.setOrderField("create_time");
                    pageInfo.setOrderDirection("desc");
                }
                setSkuSearchParam(request, model, bean);
                pc = ProProductClient.pageByObj(bean, pageInfo, pageInfo.getOrderField(), pageInfo.getOrderDirection());
                if (pc.getEntityList() != null && pc.getEntityList().size() > 0) {
                    for (ProductDTO pd : pc.getEntityList()) {
                        if (map.get(pd.getProId()) != null) {
                            pd.setIsDel(1);
                        } else {
                            pd.setIsDel(0);
                        }
                    }
                }
                model.addAttribute("pc", pc);
                model.addAttribute("tkType", "sp");
                setSupplier(model);
                setCategory(model);
            }
            model.addAttribute("pageId", pageId);
            model.addAttribute("pmId", pmId);
            return "/thematic/add_prod_list";
        }
    }

    @RequestMapping(value = "/del-area-prod")
    public String delAreaProd(HttpServletRequest request, HttpServletResponse response) {
        int[] ids = ServletRequestUtils.getIntParameters(request, "ids");
        if (ids != null && ids.length > 0) {
            int pageId = ServletRequestUtils.getIntParameter(request, "pageId", 0);
            int pmId = ServletRequestUtils.getIntParameter(request, "pmId", 0);
            ThematicPageModel pageModel = ThematicPageClient.getPageModeById(pmId);
            ProductAreaJsonVo vo = new ProductAreaJsonVo();
            List<ProductJsonVo> pjList = new ArrayList<ProductJsonVo>();
            //先获取对应区域已经存在的商品
            if (StringUtils.isNotEmpty(pageModel.getJsonData())) {
                vo = new Gson().fromJson(pageModel.getJsonData(), ProductAreaJsonVo.class);
                if (vo.getProList() != null && vo.getProList().size() > 0) {
                    Map<Integer, Integer> map = getProIdsMap(ids);
                    for (ProductJsonVo productJsonVo : vo.getProList()) {
                        if (map.get(productJsonVo.getProId()) == null) {
                            pjList.add(productJsonVo);
                        }
                    }
                }
            }
            vo.setProList(pjList);
            String pmJson = new Gson().toJson(vo);
            try {
                //重新修改PageModel表的json数据，也就是删除对应的商品
                int count = ThematicPageClient.updatePageModelJsonData(pmId, pmJson);
                if (count > 0) {
                    //修改专题表的proIds串，也就是删除对应的商品
                    ThematicPage page = ThematicPageClient.getPageById(pageId);
                    if (StringUtils.isNotEmpty(page.getProIds())) {
                        String jsonData = page.getProIds();
                        if (ids != null && ids.length > 0) {
                            jsonData = getArrayStr(jsonData.split(","), ids);
                        }
                        ThematicPageClient.updataPageProIds(pageId, jsonData);
                    }
                }
                //return StageHelper.writeDwzSignal("200", "刪除商品区对应商品成功", "10362", StageHelper.DWZ_FORWARD, "/thematic/edit-area?pageId=" + pageId + "&pmId=" + pmId, response);
                return StageHelper.writeDwzSignal("200", "刪除商品区对应商品成功", "10362", StageHelper.DWZ_FORWARD, "/thematic/edit-area?pageId=" + pageId + "&pmId=" + pmId, response);
            } catch (IceServiceRuntimeException e) {
                logger.error(e, "ids:" + ids + "pageId:" + pageId + ",pmId:" + pmId, WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "刪除商品区对应商品失败，请联系管理员", "10362", StageHelper.DWZ_FORWARD, "/thematic/edit-area?pageId=" + pageId + "&pmId=" + pmId, response);
            } catch (Exception e) {
                logger.error(e, "ids:" + ids + "pageId:" + pageId + ",pmId:" + pmId, WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "刪除商品区对应商品失败，请联系管理员", "10362", StageHelper.DWZ_FORWARD, "/thematic/edit-area?pageId=" + pageId + "&pmId=" + pmId, response);
            }
        }
        return StageHelper.writeDwzSignal("300", "刪除商品区对应商品失败，请联系管理员", "10362", StageHelper.DWZ_FORWARD, "/thematic/edit-area", response);
    }

    @RequestMapping(value = "/updateAreaPic")
    public void updateAreaPic(HttpServletRequest request, HttpServletResponse response) {

        try {

            int pmId = ServletRequestUtils.getIntParameter(request, "pmId", 0);
            int modelId = ServletRequestUtils.getIntParameter(request, "modelId", 0);
            String descp = ServletRequestUtils.getStringParameter(request, "descp", "");
            String anchorPic = ServletRequestUtils.getStringParameter(request, "anchorPic", "");
            String[] urls = ServletRequestUtils.getStringParameters(request, "url");
            String[] linkUrls = ServletRequestUtils.getStringParameters(request, "linkUrl");
            PicAreaJsonVo vo = new PicAreaJsonVo();
            vo.setAnchorPic(anchorPic);
            if (urls == null || linkUrls == null || urls.length <= 0 || linkUrls.length <= 0 || urls.length != linkUrls.length) {
                StageHelper.dwzFailClose("链接数据有误,操作失败", "", "", response);
                return;
            }
            for (int i = 0; i < urls.length; i++) {
                if (StringUtils.isBlank(urls[i])) {
                    continue;
                }
                PicJsonVo pic = new PicJsonVo();
                pic.setPicUrl(urls[i]);
                pic.setLinkUrl(linkUrls[i]);
                vo.addPicJsonVo(pic);
            }
            ThematicPageModel model = new ThematicPageModel();
            model.setId(pmId);
            model.setModelId(modelId);
            model.setJsonData(new Gson().toJson(vo));
            model.setDescp(descp);
            ThematicPageClient.updatePageModel(model);
            StageHelper.dwzSuccessClose("操作成功", "", "", response);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e, null, WebUtils.getIpAddr(request));
            StageHelper.dwzFailClose("操作失败", "", "", response);
        }
    }

    //sku管理 搜索条件设置
    private void setSkuSearchParam(HttpServletRequest request, Model model, ProductDTO bean) {
        //商品id
        int proId = ServletRequestUtils.getIntParameter(request, "pro_id", 0);
        if (proId > 0) {
            bean.setProId(proId);
        }

        //商品标题
        String proName = ServletRequestUtils.getStringParameter(request, "pro_name", "");
        if (!"".equals(proName.trim())) {
            bean.setProName(proName);
        }

        //类目
        int first_category_id = ServletRequestUtils.getIntParameter(request, "first_category_id", 0);
        if (first_category_id != 0) {
            bean.setFirstCategoryId(first_category_id);
            ProCategory second_category = new ProCategory();
            second_category.setParentId(first_category_id);
            List<ProCategory> second_List = ProCategoryClient.getListByPId(second_category);
            model.addAttribute("second_List", second_List);
        }

        int second_category_id = ServletRequestUtils.getIntParameter(request, "second_category_id", 0);
        if (second_category_id != 0) {
            bean.setSecondCategoryId(second_category_id);
            ProCategory third_category = new ProCategory();
            third_category.setParentId(second_category_id);
            List<ProCategory> third_List = ProCategoryClient.getListByPId(third_category);
            model.addAttribute("third_List", third_List);
        }
        int third_category_id = ServletRequestUtils.getIntParameter(request, "third_category_id", 0);
        if (third_category_id != 0) {
            bean.setThirdCategoryId(third_category_id);
        }

        //美丽价
        String mlw_price_min = ServletRequestUtils.getStringParameter(request, "mlw_price_min", "");
        if (!"".equals(mlw_price_min)) {
            bean.setMlwPriceMin(new BigDecimal(mlw_price_min));
        }
        String mlw_price_max = ServletRequestUtils.getStringParameter(request, "mlw_price_max", "");
        if (!"".equals(mlw_price_max)) {
            bean.setMlwPriceMax(new BigDecimal(mlw_price_max));
        }

        //更新时间
        String update_time_min = ServletRequestUtils.getStringParameter(request, "update_time_min", "");
        if (!"".equals(update_time_min)) {
            bean.setUpdateTimeMin(DateUtil.parseTimestamp(update_time_min));
        }
        String update_time_max = ServletRequestUtils.getStringParameter(request, "update_time_max", "");
        if (!"".equals(update_time_max)) {
            bean.setUpdateTimeMax(DateUtil.parseTimestamp(update_time_max));
        }
        //供应商
        int supplierId = ServletRequestUtils.getIntParameter(request, "supplier_id", 0);
        if (supplierId != 0) {
            bean.setSupplierId(supplierId);
        }
        model.addAttribute("bean", bean);
    }

    private void setSupplier(Model model) {
        List<ProSupplier> suppliers = ProSupplierClient.getSupplierList(new ProSupplier());
        model.addAttribute("suppliers", suppliers);

    }

    private void setCategory(Model model) {
        ProCategory category = new ProCategory();
        category.setParentId(0);
        List<ProCategory> categoryList = ProCategoryClient.getListByPId(category);
        model.addAttribute("categoryList", categoryList);

    }

    private Map<Integer, String> getExitsProdId(int pageId) {
        ThematicPage page = ThematicPageClient.getPageById(pageId);
        Map<Integer, String> map = new HashMap<Integer, String>();
        if (page != null) {
            if (StringUtils.isNotEmpty(page.getProIds())) {
                String[] ids = page.getProIds().split(",");
                for (String id : ids) {
                    map.put(Integer.parseInt(id), id);
                }
            }
        }
        return map;
    }

    private String getIdsStr(int[] ids) {
        StringBuffer str = new StringBuffer();
        boolean first = true;
        for (int id : ids) {
            if (first) {
                str.append(id);
                first = false;
            } else {
                str.append("," + id);
            }
        }
        return str.toString();
    }

    private ThematicPageModel getPicAreaVO(HttpServletRequest request) {
        int pmId = ServletRequestUtils.getIntParameter(request, "pmId", 0);
        int modelId = ServletRequestUtils.getIntParameter(request, "modelId", 0);
        String descp = ServletRequestUtils.getStringParameter(request, "descp", null);
        String bgColor = ServletRequestUtils.getStringParameter(request, "bgColor", null);
        String columnImg = ServletRequestUtils.getStringParameter(request, "columnImg", null);
        String prodMdImg = ServletRequestUtils.getStringParameter(request, "prodMdImg", null);

        ThematicPageModel pageModel = ThematicPageClient.getPageModeById(pmId);
        ProductAreaJsonVo vo = new ProductAreaJsonVo();
        //先获取对应区域已经存在的商品
        if (StringUtils.isNotEmpty(pageModel.getJsonData())) {
            vo = new Gson().fromJson(pageModel.getJsonData(), ProductAreaJsonVo.class);
            if (vo.getProList() != null && vo.getProList().size() > 0) {
                for (ProductJsonVo productJsonVo : vo.getProList()) {
                    String markStr = ServletRequestUtils.getStringParameter(request, "mark-" + productJsonVo.getProId(), null);
                    productJsonVo.setMark(markStr);
                }
            }
        }
        vo.setBgColor(bgColor);
        vo.setBannerPic(columnImg);
        vo.setAnchorPic(prodMdImg);
        pageModel.setId(pmId);
        pageModel.setDescp(descp);
        pageModel.setModelId(modelId);
        pageModel.setJsonData(new Gson().toJson(vo));

        return pageModel;
    }

    private ThematicPageModel getProdAreaVO(HttpServletRequest request) {
        int pmId = ServletRequestUtils.getIntParameter(request, "pmId", 0);
        int modelId = ServletRequestUtils.getIntParameter(request, "modelId", 0);
        String descp = ServletRequestUtils.getStringParameter(request, "descp", null);
        String bgColor = ServletRequestUtils.getStringParameter(request, "bgColor", null);
        String columnImg = ServletRequestUtils.getStringParameter(request, "columnImg", null);
        String prodMdImg = ServletRequestUtils.getStringParameter(request, "prodMdImg", null);

        ThematicPageModel pageModel = ThematicPageClient.getPageModeById(pmId);
        ProductAreaJsonVo vo = new ProductAreaJsonVo();
        //先获取对应区域已经存在的商品
        if (StringUtils.isNotEmpty(pageModel.getJsonData())) {
            vo = new Gson().fromJson(pageModel.getJsonData(), ProductAreaJsonVo.class);
            if (vo.getProList() != null && vo.getProList().size() > 0) {
                for (ProductJsonVo productJsonVo : vo.getProList()) {
                    String markStr = ServletRequestUtils.getStringParameter(request, "mark-" + productJsonVo.getProId(), null);
                    productJsonVo.setMark(markStr);
                }
            }
        }
        vo.setBgColor(bgColor);
        vo.setBannerPic(columnImg);
        vo.setAnchorPic(prodMdImg);
        pageModel.setId(pmId);
        pageModel.setDescp(descp);
        pageModel.setModelId(modelId);
        pageModel.setJsonData(new Gson().toJson(vo));

        return pageModel;
    }

    private String getArrayStr(String[] str, int[] ids) {
        int[] proIds = ArrayUtils.stringToInts(str);
        List<Integer> list = new ArrayList<Integer>();
        StringBuffer jsonData = new StringBuffer("");
        Map<Integer, Integer> map = getProIdsMap(ids);
        for (int proId : proIds) {
            if (map.get(proId) == null) {
                list.add(proId);
            }
        }
        if (list != null && list.size() > 0) {
            boolean first = true;
            for (Integer j : list) {
                if (first) {
                    jsonData.append(j.intValue());
                    first = false;
                } else {
                    jsonData.append("," + j.intValue());
                }
            }
        }
        return jsonData.toString();
    }

    /**
     * 获取商品参加的活动
     *
     * @param spMap
     * @return
     */
    private Map<Integer, String> getSPNameList(Map<Integer, Map<PrivilegeType, List<SimpleActVO>>> spMap) {
        Map<Integer, String> map = new HashMap<Integer, String>();
        if (spMap != null && spMap.size() > 0) {
            Iterator iterator = spMap.keySet().iterator();
            while (iterator.hasNext()) {
                String actName = "";
                int proId = (Integer) iterator.next();
                Map<PrivilegeType, List<SimpleActVO>> typeMap = spMap.get(proId);
                if (typeMap != null && typeMap.size() > 0) {
                    for (List<SimpleActVO> list : typeMap.values()) {
                        if (list != null && list.size() > 0) {
                            for (SimpleActVO vo : list) {
                                if (StringUtils.isNotEmpty(actName)) {
                                    actName = actName + vo.getActName();
                                } else {
                                    actName = actName + "," + vo.getActName();
                                }
                            }
                        }
                    }
                }
                map.put(proId, actName);
            }
        }
        return map;
    }

    private Map<Integer, Integer> getProIdsMap(int[] ids) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (Integer id : ids) {
            map.put(id, id);
        }
        return map;
    }

    private int[] getIdsBylist(List<SpTicketBatchProd> list) {
        int size = list.size();
        int[] ids = new int[size];
        for (int i = 0; i < size; i++) {
            ids[i] = list.get(i).getProId();
        }

        return ids;
    }
}

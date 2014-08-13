package com.meiliwan.emall.bkstage.web.controller.pms;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.sql.Connection;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.meiliwan.emall.bkstage.jdbc.PmsJdbcTool;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.util.NumberUtil;
import com.meiliwan.emall.pms.bean.*;
import com.meiliwan.emall.pms.client.*;
import com.meiliwan.emall.pms.dto.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.base.Strings;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.bkstage.web.html.CategoryListTemplate;
import com.meiliwan.emall.bkstage.web.util.PoiExcelUtil;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.web.WebParamterUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.exception.IceServiceRuntimeException;
import com.meiliwan.emall.mms.bean.UserProExtraIntegral;
import com.meiliwan.emall.mms.client.UserProExtraIntegralServiceClient;
import com.meiliwan.emall.pms.constant.Constant;
import com.meiliwan.emall.pms.util.ProductStatus;
import com.meiliwan.emall.stock.bean.ProStock;
import com.meiliwan.emall.stock.client.ProStockClient;

/**
 * User: wuzixin
 * Date: 13-6-8
 * Time: 上午11:02
 */
@Controller("pmsProductController")
@RequestMapping("/pms/product")
public class ProductController {
    final static String exportPath = "/data/www/exportfiles/";

    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

    /**
     * 增加商品
     *
     * @return
     */
    @RequestMapping("/add")
    public String add(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        int step = ServletRequestUtils.getIntParameter(request, "step", 1);
        if (step == 1) {
            model.addAttribute("firstLevel", CategoryListTemplate.getCategoryHtml(CategoryListTemplate.getCategoryListByPid(0), 0));
            return "/pms/product/category_template";
        } else {
            if (step == 2) {
                //获取一二三级类目ID
                int firstLevel = ServletRequestUtils.getIntParameter(request, "firstLevel", 11);
                int secondLevel = ServletRequestUtils.getIntParameter(request, "secondLevel", 12);
                int thirdLevel = ServletRequestUtils.getIntParameter(request, "thirdLevel", 13);

                ProStore store = ProStoreClient.getStoreByThirdId(thirdLevel);
                if (store != null) {
                    ProCategory firstPc = ProCategoryClient.getProCategoryById(firstLevel);
                    ProCategory secondPc = ProCategoryClient.getProCategoryById(secondLevel);
                    ProCategory thirdPc = ProCategoryClient.getProCategoryById(thirdLevel);
                    //获取所有的属性
                    List<ProProperty> pps = ProPropertyClient.getPropertyListByCategoryId(thirdLevel);
                    //获取产地
                    List<ProPlace> places = ProPlaceClient.getAllProPlaceList(new ProPlace());
                    //获取供应商
                    List<ProSupplier> suppliers = ProSupplierClient.getSupplierList(new ProSupplier());
                    //获取SKU属性
                    List<ProProperty> skuProp = new ArrayList<ProProperty>();
                    for (int i = 0; i < pps.size(); i++) {
                        ProProperty property = pps.get(i);
                        if (property.getIsSku() == 1) {
                            skuProp.add(property);
                        }
                    }

                    model.addAttribute("firstPc", firstPc);
                    model.addAttribute("secondPc", secondPc);
                    model.addAttribute("thirdPc", thirdPc);
                    model.addAttribute("pps", pps);
                    model.addAttribute("skuProp", skuProp);
                    model.addAttribute("places", places);
                    model.addAttribute("suppliers", suppliers);
                    return "/pms/product/add";
                } else {
                    return StageHelper.writeDwzSignal("300", "该类目不属于某个店铺，无法发布商品", "73", StageHelper.DWZ_FORWARD, "/pms/product/add", response);
                }
            } else if (step == 3) {
                ProSpu spu = getSpu(request);
                ProDetail detail = getDetail(request);
                List<ProSelfProperty> selfProperties = getSpuSelfProp(request);
                Map<String, Object> map = getPropertyAction(request);
                spu.setPsplist(selfProperties);
                spu.setDetail(detail);
                String propertyString = (String) map.get("propertyString");
                List<ProProductProperty> properties = (List<ProProductProperty>) map.get("property");
                spu.setPropertyString(propertyString);
                spu.setPppropertyList(properties);
                spu.setState(Constant.STATE_VALID);
                int skuFlag = (Integer)map.get("skuFlag");
                spu.setSkuFlag((short)skuFlag);
                if (map.get("skuPropA") != null) {
                    spu.setSkuPropA(map.get("skuPropA").toString());
                }
                if (map.get("skuPropB") != null) {
                    spu.setSkuPropB(map.get("skuPropB").toString());
                }
                if (spu.getBrandId() <= 0) {
                    return StageHelper.writeDwzSignal("300", "商品暂无该品牌，请重新选择或者编辑品牌", "73", StageHelper.DWZ_FORWARD, "/pms/product/add", response);
                }
                try {
                    int spuId = ProSpuClient.addSpu(spu);
                    return StageHelper.writeDwzSignal("200", "添加公共信息成功", "", StageHelper.DWZ_FORWARD, "/pms/product/add?step=4&spuId=" + spuId, response);
                } catch (IceServiceRuntimeException e) {
                    logger.error(e, "商品发布-添加spu失败," + spu, WebUtils.getIpAddr(request));
                    return StageHelper.writeDwzSignal("300", "商品发布-添加spu公共信息失败", "73", StageHelper.DWZ_FORWARD, "/pms/product/add?step=3", response);
                } catch (Exception e) {
                    logger.error(e, "商品发布-添加spu失败," + spu, WebUtils.getIpAddr(request));
                    return StageHelper.writeDwzSignal("300", "商品发布-添加spu公共信息失败", "73", StageHelper.DWZ_FORWARD, "/pms/product/add?step=3", response);
                }
            } else if (step == 4) { //增加图片页面
                int spuId = ServletRequestUtils.getIntParameter(request, "spuId", 0);
                ProSpu spu = ProSpuClient.getSpuById(spuId);
                ProCategory firstPc = ProCategoryClient.getProCategoryById(spu.getFirstCategoryId());
                ProCategory secondPc = ProCategoryClient.getProCategoryById(spu.getSecondCategoryId());
                ProCategory thirdPc = ProCategoryClient.getProCategoryById(spu.getThirdCategoryId());
                List<ProProperty> properties = ProPropertyClient.getSpuWithSkuPropsById(spuId);
                //判断是否有异图
                int imageFlag = 0;
                if (properties != null && properties.size() > 0) {
                    for (ProProperty property : properties) {
                        if (property.getIsImage() == 1) {
                            imageFlag = 1;
                        }
                    }
                }
                model.addAttribute("firstPc", firstPc);
                model.addAttribute("secondPc", secondPc);
                model.addAttribute("thirdPc", thirdPc);
                model.addAttribute("properties", properties);
                model.addAttribute("spu", spu);
                model.addAttribute("imageFlag", imageFlag);
                return "/pms/product/add_image";
            } else if (step == 5) { //增加图片弹出框
                int provId = ServletRequestUtils.getIntParameter(request, "provId", 0);
                model.addAttribute("provId", provId);
                return "/pms/product/upload_image";
            } else if (step == 6) {
                int spuId = ServletRequestUtils.getIntParameter(request, "spuId", 0);
                int proPropId = ServletRequestUtils.getIntParameter(request, "proPropId", 0);
                //获取SKU属性对应的图片地址
                String[] imageUris = {};
                try {
                    imageUris = ServletRequestUtils.getRequiredStringParameters(request, "imageUrls");
                } catch (Exception e) {
                }
                if (imageUris != null) {
                    List<ProImages> imageses = new ArrayList<ProImages>();
                    for (String str : imageUris) {
                        String[] propUrl = str.split(";");
                        if (propUrl.length > 1) {
                            ProImages images = new ProImages();
                            images.setSpuId(spuId);
                            images.setProPropId(proPropId);
                            images.setProProvId(Integer.parseInt(propUrl[0]));
                            images.setDefaultImageUri(propUrl[1].split(",")[0]);
                            images.setImageUris(propUrl[1]);
                            imageses.add(images);
                        }
                    }
                    if (imageses != null && imageses.size() > 0) {
                        try {
                            ProSpuClient.addImage(spuId, imageses);
                            return StageHelper.writeDwzSignal("200", "添加spu对应图片成功", "", StageHelper.DWZ_FORWARD, "/pms/product/add?step=7&spuId=" + spuId, response);
                        } catch (IceServiceRuntimeException e) {
                            logger.error(e, "商品发布-添加spu对应图片失败," + imageses, WebUtils.getIpAddr(request));
                            return StageHelper.writeDwzSignal("300", "商品发布-添加spu对应图片失败", "73", StageHelper.DWZ_FORWARD, "/pms/product/add?step=4&spuId=" + spuId, response);
                        } catch (Exception e) {
                            logger.error(e, "商品发布-添加spu对应图片失败," + imageses, WebUtils.getIpAddr(request));
                            return StageHelper.writeDwzSignal("300", "商品发布-添加spu对应图片失败", "73", StageHelper.DWZ_FORWARD, "/pms/product/add?step=4&spuId=" + spuId, response);
                        }
                    } else {
                        return StageHelper.writeDwzSignal("300", "还没有上传图片", "73", StageHelper.DWZ_FORWARD, "/pms/product/add?step=4&spuId=" + spuId, response);
                    }
                } else {
                    return StageHelper.writeDwzSignal("300", "还没有上传图片", "73", StageHelper.DWZ_FORWARD, "/pms/product/add?step=4&spuId=" + spuId, response);
                }
            } else if (step == 7) {
                int spuId = ServletRequestUtils.getIntParameter(request, "spuId", 0);
                ProSpu spu = ProSpuClient.getSpuById(spuId);
                ProCategory firstPc = ProCategoryClient.getProCategoryById(spu.getFirstCategoryId());
                ProCategory secondPc = ProCategoryClient.getProCategoryById(spu.getSecondCategoryId());
                ProCategory thirdPc = ProCategoryClient.getProCategoryById(spu.getThirdCategoryId());
                List<ProProperty> properties = ProPropertyClient.getSpuWithSkuPropsById(spuId);
                //获取已生成的规格
                List<SimpleProduct> list = ProProductClient.getListProBySpuId(spuId);
                //判断是否有异图
                int skuFlag = 0;//表示无规格
                Map<String, SimpleProduct> skuMap = new HashMap<String, SimpleProduct>();
                if (properties != null && properties.size() > 0) {
                    if (properties.size() == 1) {
                        skuFlag = 1;//表示单规格
                        if (list != null && list.size() > 0) {
                        }
                    } else {
                        skuFlag = 2;//表示双规格
                    }
                    if (list != null && list.size() > 0) {
                        skuMap = getPropvToSku(list);
                    }
                } else {
                    if (list != null && list.size() > 0) {
                        model.addAttribute("sku", list.get(0));
                    }
                }
                model.addAttribute("firstPc", firstPc);
                model.addAttribute("secondPc", secondPc);
                model.addAttribute("thirdPc", thirdPc);
                model.addAttribute("properties", properties);
                model.addAttribute("spu", spu);
                model.addAttribute("skuFlag", skuFlag);
                model.addAttribute("skuMap", skuMap);
                return "/pms/product/add_sku";
            } else if (step == 8) {
                //获取spu ID
                int spuId = ServletRequestUtils.getIntParameter(request, "spuId", 0);
                //获取sku名称
                String skuName = ServletRequestUtils.getStringParameter(request, "skuName", null);
                //获取sku详情
                String skuDetail = ServletRequestUtils.getStringParameter(request, "skuDetail", null);
                int skuProp1 = ServletRequestUtils.getIntParameter(request, "skuProp1", 0);
                int skuProp2 = ServletRequestUtils.getIntParameter(request, "skuProp2", 0);
                int skuProv1 = ServletRequestUtils.getIntParameter(request, "skuProv1", 0);
                int skuProv2 = ServletRequestUtils.getIntParameter(request, "skuProv2", 0);
                model.addAttribute("spuId", spuId);
                model.addAttribute("skuName", skuName);
                model.addAttribute("skuDetail", skuDetail);
                model.addAttribute("skuProp1", skuProp1);
                model.addAttribute("skuProp2", skuProp2);
                model.addAttribute("skuProv1", skuProv1);
                model.addAttribute("skuProv2", skuProv2);
                return "/pms/product/add_sku_result";
            } else {
                //获取spu ID
                int spuId = ServletRequestUtils.getIntParameter(request, "spuId", 0);
                //获取sku名称
                String skuName = ServletRequestUtils.getStringParameter(request, "skuName", null);
                int skuProp1 = ServletRequestUtils.getIntParameter(request, "skuProp1", 0);
                int skuProp2 = ServletRequestUtils.getIntParameter(request, "skuProp2", 0);
                int skuProv1 = ServletRequestUtils.getIntParameter(request, "skuProv1", 0);
                int skuProv2 = ServletRequestUtils.getIntParameter(request, "skuProv2", 0);
                String barCode = ServletRequestUtils.getStringParameter(request, "barCode", null);
                ProSpu spu = ProSpuClient.getSpuById(spuId);
                String skuStr = "";
                if (skuProp1 != 0) {
                    skuStr = skuProp1 + ":" + skuProv1 + ";";
                }
                if (skuProp2 != 0) {
                    skuStr = skuStr + skuProp2 + ":" + skuProv2 + ";";
                }
                ProImages images = ProSpuClient.getImagesById(spuId, skuProv1);
                if (images == null) {
                    images = ProSpuClient.getImagesById(spuId, skuProv2);
                    if (images == null) {
                        images = ProSpuClient.getImagesById(spuId, 0);
                    }
                }
                ProProduct product = new ProProduct();
                product.setSpuId(spuId);
                product.setBarCode(barCode);
                product.setProName(spu.getProName());
                product.setShortName(spu.getShortName());
                product.setAdvName(spu.getAdvName());
                if (images != null) {
                    product.setDefaultImageUri(images.getDefaultImageUri());
                    product.setImageUris(images.getImageUris());
                    product.setProPropVid(images.getProProvId());
                }
                product.setIsCod(spu.getIsCod());
                product.setBrandId(spu.getBrandId());
                product.setSupplierId(spu.getSupplierId());
                if (StringUtils.isNotEmpty(skuName)) {
                    product.setSkuName(skuName.replace(",", " "));
                }
                product.setAdminId(StageHelper.getLoginUser(request).getBksAdmin().getAdminId());
                product.setThirdCategoryId(spu.getThirdCategoryId());
                product.setSkuPropertyStr(skuStr);
                product.setState((short) Constant.PRODUCT_EDITFAIL);
                int proId = 0;
                try {
                    proId = ProProductClient.addProProduct(product);
                } catch (IceServiceRuntimeException e) {
                    logger.error(e, "商品发布-添加sku对应条形码失败," + product, WebUtils.getIpAddr(request));
                } catch (Exception e) {
                    logger.error(e, "商品发布-添加sku对应条形码失败," + product, WebUtils.getIpAddr(request));
                }
                return StageHelper.writeString(String.valueOf(proId),response);
            }
        }
    }

    /**
     * 修改商品
     *
     * @param request
     */
    @RequestMapping("/update")
    public String update(Model model, HttpServletRequest request, HttpServletResponse response) {
        int ustep = ServletRequestUtils.getIntParameter(request, "ustep", 1);
        if (ustep == 1) {
            int proId = ServletRequestUtils.getIntParameter(request, "proId", 0);
            int navFlag = ServletRequestUtils.getIntParameter(request,"navFlag",0);
            //获取sku详情
            String skuDetail = ServletRequestUtils.getStringParameter(request, "skuDetail", null);
            ProProduct product = ProProductClient.getWholeProductById(proId);
            if (product != null) {
                ProCategory thirdPc = ProCategoryClient.getProCategoryById(product.getThirdCategoryId());
                ProCategory secondPc = ProCategoryClient.getProCategoryById(thirdPc.getParentId());
                ProCategory firstPc = ProCategoryClient.getProCategoryById(secondPc.getParentId());
                model.addAttribute("firstPc", firstPc);
                model.addAttribute("secondPc", secondPc);
                model.addAttribute("thirdPc", thirdPc);
                model.addAttribute("product", product);
                model.addAttribute("skuDetail", skuDetail);
                model.addAttribute("navFlag", navFlag);
                return "/pms/product/edit_sku";
            }
            return "";
        } else if (ustep == 2) {
            int proId = ServletRequestUtils.getIntParameter(request, "proId", 0);
            String barCode = ServletRequestUtils.getStringParameter(request,"barCode",null);
            int navFlag = ServletRequestUtils.getIntParameter(request,"navFlag",0);
            ProProduct product = getProduct(request);
            product.getAction().setProId(proId);
            product.setProId(proId);
            product.setSkuSelfProps(getProSelfProp(request));
            int isSuc = 0;//表示未成功
            String stateStr = product.getState() == Constant.PRODUCT_OFF ? "下架" : "未编辑完成";
            try {
                boolean suc = ProProductClient.updateProAndAddAction(product);
                if (suc) {
                    //增加库存
                    //增加商品库存
                    try {
                        ProStock stock = getProStock(request);
                        stock.setProId(proId);
                        stock.setSellStock(stock.getStock());
                        stock.setOrderStock(0);
                        stock.setUnsellStock(0);
                        stock.setUpdateTime(new Date());
                        ProStockClient.insertStock(stock, StageHelper.getLoginUser(request).getBksAdmin().getId(), "admin");
                    } catch (Exception e) {
                        logger.error(e, product, WebUtils.getIpAddr(request));
                    }
                    //添加商品一条默认的储位信息,先判断是否已经添加了储位
                    ProLocation lt = ProLocationClient.getLocationByBarCode(barCode);
                    if (lt == null) {
                        try {
                            ProLocation location = new ProLocation();
                            location.setBarCode(barCode);
                            ProLocationClient.addProLocation(location);
                        } catch (Exception e) {
                            logger.error(e, product, WebUtils.getIpAddr(request));
                        }
                    }
                    isSuc = 1;
                }
            } catch (IceServiceRuntimeException e) {
                logger.error(e, product, WebUtils.getIpAddr(request));
            } catch (Exception e) {
                logger.error(e, product, WebUtils.getIpAddr(request));
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("proId", proId);
            map.put("suc", isSuc);
            map.put("stateStr", stateStr);
            map.put("navFlag",navFlag);
            return StageHelper.writeJson(response, map);
        } else {
            return "";
        }
    }

    @RequestMapping(value = "/spu-list")
    public String spuList(Model model, HttpServletRequest request, SpuListDTO bean) {

        PagerControl<SpuListDTO> pc = null;
        String orderField = ServletRequestUtils.getStringParameter(request, "orderField", "");
        String orderDirection = ServletRequestUtils.getStringParameter(request, "orderDirection", "");
        if (bean == null) {
            bean = new SpuListDTO();
        }
        PageInfo pageInfo = WebParamterUtil.getPageInfo(request);
        pageInfo.setOrderField(orderField);
        pageInfo.setOrderDirection(orderDirection);
        if ("".equals(orderField)) {
            pageInfo.setOrderField("create_time");
            pageInfo.setOrderDirection("desc");
        }
        setSpuSearchParam(request, model, bean);
        pc = ProSpuClient.getSpuPageByObj(bean, pageInfo, pageInfo.getOrderField(), pageInfo.getOrderDirection());
        setSupplier(model);
        setCategory(model);
        setStoreList(model);
        model.addAttribute("pc", pc);
        return "/pms/product/spu_list";
    }

    private void setBlandList(Model model) {
        model.addAttribute("brandList", ProBrandClient.getAll());
    }

    /**
     * 查看spu信息
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/spu-view")
    public String spuView(Model model, HttpServletRequest request) {
        int viewFlag = ServletRequestUtils.getIntParameter(request, "viewFlag", 0);
        if (viewFlag > 0) {
            int proVid = ServletRequestUtils.getIntParameter(request, "proVid", 0);
            int proPid = ServletRequestUtils.getIntParameter(request, "proPid", 0);
            int spuId = ServletRequestUtils.getIntParameter(request, "spuId", 0);

            if (spuId > 0 && proVid >= 0 && spuId >= 0) {
                Map<String, Object> mapDto = new HashMap<String, Object>();
                mapDto.put("spuId", spuId + "");
                mapDto.put("proVid", proVid + "");
                mapDto.put("proPid", proPid + "");
                ProImages proImages = ProSpuClient.getImages(mapDto);
                model.addAttribute("proImages", proImages);
            }

            model.addAttribute("proVid", proVid);
            model.addAttribute("proPid", proPid);

            return "/pms/product/view_spu_upload_image";
        } else {
            int spuId = ServletRequestUtils.getIntParameter(request, "spuId", 0);
            setSpu(model, spuId);
            return "/pms/product/view_spu";
        }
    }

    @RequestMapping("/updatespu")
    public String spuSave(Model model, HttpServletRequest request, HttpServletResponse response) {
        int spuId = ServletRequestUtils.getIntParameter(request, "spuId", 0);
        String submitType = ServletRequestUtils.getStringParameter(request, "submitType", "");
        if ("proTitleField".equals(submitType)) {
            // 商品标题的修改
            ProductNamesDto productNamesDto = setProductNames(request);
            productNamesDto.setSpuId(spuId);
            if (!ProSpuClient.updateProductNames(productNamesDto)) {
                logger.warn("更新商品标题失败！依据：数据库更新影响的行数=0", productNamesDto.toString(), WebUtils.getClientIp(request));
            }
        } else if ("publicPara".equals(submitType)) {
            // 公共参数的修改
            ProductPublicParasDto productPublicParasDto = setPublicParas(request);
            if (productPublicParasDto.getBrandId()<=0){
                return StageHelper.writeDwzSignal("300", "暂无对应品牌，请重新选择", "", StageHelper.DWZ_FORWARD, "/pms/product/view-spu", response);
            }
            productPublicParasDto.setSpuId(spuId);
            if (!ProSpuClient.updatePublicParas(productPublicParasDto)) {
                logger.warn("更新商品公共参数失败！依据：数据库更新影响的行数=0", productPublicParasDto.toString(), WebUtils.getClientIp(request));
            }
            ;

        } else if ("proDetail".equals(submitType)) {
            // 商品详情的修改
            ProductDetailDTO dto = setProDetail(request);
            dto.setSpuId(spuId);
            if (!ProSpuClient.updateProDetailByDto(dto)) {
                logger.warn("更新商品详情失败！依据：数据库更新影响的行数=0", dto.toString(), WebUtils.getClientIp(request));
            }
        } else if ("selfSpu".equals(submitType)) {
            // 自己属性的修改
            Map<String, Object> mapDto = new HashMap<String, Object>();
            mapDto.put("spuId", spuId + "");
            mapDto.put("editorRec", ServletRequestUtils.getStringParameter(request, "editorRec", ""));
            String[] pspids = ServletRequestUtils.getStringParameters(request, "pspid");
            String[] selfPropNames = ServletRequestUtils.getStringParameters(request, "selfPropName");
            String[] selfPropValues = ServletRequestUtils.getStringParameters(request, "selfPropValue");
            mapDto.put("pspids", pspids);
            mapDto.put("selfPropNames", selfPropNames);
            mapDto.put("selfPropValues", selfPropValues);
            ProSpuClient.updateSelfProperties(mapDto);


        } else if ("categoryPro".equals(submitType)) {
            // 类目属性的修改
            Map<String, Object> mapDto = new HashMap<String, Object>();
            mapDto.put("spuId", spuId + "");
            mapDto.put("updateSku",false);
            String[] proPropIds = ServletRequestUtils.getStringParameters(request, "cateProPropertyId");
            String[] proProProValues = ServletRequestUtils.getStringParameters(request, "cateProProValue");
            String[] proPropIsmages = ServletRequestUtils.getStringParameters(request, "cateIsImage");
            String[] proPropIsSkus = ServletRequestUtils.getStringParameters(request, "cateProIsSku");
            String[] proPropTypes = ServletRequestUtils.getStringParameters(request, "cateProPropertyType");
            if (proPropIds != null && proPropIds.length > 0) {
                mapDto.put("proPropIds", proPropIds);
                mapDto.put("proProProValues", proProProValues);
                mapDto.put("proPropIsmages", proPropIsmages);
                mapDto.put("proPropIsSkus", proPropIsSkus);
                mapDto.put("proPropTypes", proPropTypes);
                ProSpuClient.updateProProdProperty(mapDto);
            }

        } else if ("specification".equals(submitType)) {
            // 规格属性的修改
            Map<String, Object> mapDto = new HashMap<String, Object>();
            mapDto.put("spuId", spuId + "");
            mapDto.put("updateSku",true);
            String[] proPropIds = ServletRequestUtils.getStringParameters(request, "specProPropertyId");
            String[] proProProValues = ServletRequestUtils.getStringParameters(request, "specProProValue");
            String[] proPropIsmages = ServletRequestUtils.getStringParameters(request, "specIsImage");
            String[] proPropIsSkus = ServletRequestUtils.getStringParameters(request, "specProIsSku");
            if (proPropIds != null && proPropIds.length > 0) {
                mapDto.put("proPropIds", proPropIds);
                mapDto.put("proProProValues", proProProValues);
                mapDto.put("proPropIsmages", proPropIsmages);
                mapDto.put("proPropIsSkus", proPropIsSkus);
                ProSpuClient.updateProProdProperty(mapDto);
            }
        } else if ("setPrimaryImage".equals(submitType)) {
            // 设置主图
            String proPid = ServletRequestUtils.getStringParameter(request, "proPid", "");
            String proVid = ServletRequestUtils.getStringParameter(request, "proVid", "");
            String primaryImageUrl = ServletRequestUtils.getStringParameter(request, "primaryImageUrl", "");

            Map<String, Object> mapDto = new HashMap<String, Object>();
            mapDto.put("spuId", spuId + "");
            mapDto.put("proVid", proVid);
            mapDto.put("proPid", proPid);
            mapDto.put("primaryImageUrl", primaryImageUrl);

            if (ProSpuClient.updatePrimaryImage(mapDto) == 0) {
                logger.warn("更新商品主图失败！依据：数据库更新影响的行数=0", mapDto.toString(), WebUtils.getClientIp(request));
            }

        } else if ("uploadImages".equals(submitType)) {
            int proPid = ServletRequestUtils.getIntParameter(request, "proPid", 0);
            int proVid = ServletRequestUtils.getIntParameter(request, "proVid", 0);
            String imageUrls = ServletRequestUtils.getStringParameter(request, "imageUrlsSpu", "");

            if (StringUtils.isNotBlank(imageUrls)) {
                Map<String, Object> mapDto = new HashMap<String, Object>();
                mapDto.put("spuId", spuId + "");
                mapDto.put("proVid", proVid + "");
                mapDto.put("proPid", proPid + "");
                mapDto.put("imageUrls", imageUrls);
                ProSpuClient.updateOrInsertImages(mapDto);
            }
        }

        setSpu(model, spuId);
        return "/pms/product/view_spu";
    }

    /**
     * SPU管理 删除功能
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/spu-delete")
    public String spuDelete(HttpServletRequest request, HttpServletResponse response) {

        int spuId = ServletRequestUtils.getIntParameter(request, "spuId", 0);
        try {
            boolean suc = ProSpuClient.deleteSpu(spuId);
            if (suc) {
                return StageHelper.writeDwzSignal("200", "SPU管理-删除成功", "10315", StageHelper.DWZ_CLOSE_CURRENT, "/pms/product/spu-list", response);
            } else {
                return StageHelper.writeDwzSignal("300", "SPU管理-删除失败", "10315", StageHelper.DWZ_FORWARD, "/pms/product/spu-list", response);
            }
        } catch (IceServiceRuntimeException e) {
            logger.error(e, "SPU管理-删除失败," + spuId, WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("300", "SPU管理-删除失败", "10315", StageHelper.DWZ_FORWARD, "/pms/product/spu-list", response);
        } catch (Exception e) {
            logger.error(e, "商品发布-SPU管理-删除失败," + spuId, WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("300", "SPU管理-删除失败", "10315", StageHelper.DWZ_FORWARD, "/pms/product/spu-list", response);
        }
    }

    /**
     * sku 管理 (商品列表)
     *
     * @param model
     * @param request
     * @param bean
     * @return
     */
    @RequestMapping(value = "/list")
    public String list(HttpServletRequest request, Model model, HttpServletResponse response, ProductDTO bean) throws IOException {
        int selectFlag = ServletRequestUtils.getIntParameter(request,"selectFlag",0);
        if (selectFlag>0){
            //校验条形码的唯一性
             String barCode = ServletRequestUtils.getStringParameter(request,"barCode",null);
            String type = ServletRequestUtils.getStringParameter(request,"optType","add");
            boolean suc = ProProductClient.checkProductByBarCode(barCode,0,type);
            response.setHeader("Content-Language", "zh-cn");
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("application/json; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            if (suc){
                response.getWriter().print(false);
            }else {
                response.getWriter().print(true);
            }
            return null;
        }else {
            PagerControl<ProductDTO> pc = null;
            String orderField = ServletRequestUtils.getStringParameter(request, "orderField", "");
            String orderDirection = ServletRequestUtils.getStringParameter(request, "orderDirection", "");
            if (bean == null) {
                bean = new ProductDTO();
            }
            PageInfo pageInfo = WebParamterUtil.getPageInfo(request);
            pageInfo.setOrderField(orderField);
            pageInfo.setOrderDirection(orderDirection);
            if ("".equals(orderField)) {
                pageInfo.setOrderField("create_time");
                pageInfo.setOrderDirection("desc");
            }
            setSkuSearchParam(request, model, bean);
            pc = ProProductClient.pageByObj(bean, pageInfo, pageInfo.getOrderField(), pageInfo.getOrderDirection());
            setSupplier(model);
            setState(model);
            setCategory(model);
            model.addAttribute("pc", pc);
            return "/pms/product/sku_list";
        }
    }

    /**
     * sku
     *
     * @param model
     * @param request
     * @param bean
     * @return
     */
    @RequestMapping(value = "/getSkuExcel")
    public void  getSkuExcel(HttpServletRequest request, Model model, HttpServletResponse response, ProductDTO bean) throws IOException {
        if (bean == null) {
            bean = new ProductDTO();
        }
        try{
            setSkuSearchParam(request, model, bean);
            String sql = "select " +
                    "distinct p.pro_id ," +
                    "p.bar_code," +
                    "p.pro_name," +
                    "p.sku_name," +
                    "p.mlw_price," +
                    "p.trade_price," +
                    "s.store_name," +
                    "c.category_name," +
                    "p.state," +
                    "p.presale_end_time," +
                    "k.stock " +
                    "from " +
                    "mlw_product.pro_product p," +
                    "mlw_product.pro_store_category s ," +
                    "mlw_product.pro_category c," +
                    "mlw_product.pro_stock k ";
            String condition= "where p.third_category_id = s.third_category_id and p.third_category_id = c.category_id and p.pro_id = k.pro_id and p.state != '-1'";
            sql = sql + condition + getSelfCondition(bean)+"order by p.pro_id";
            Connection conn = PmsJdbcTool.getConnection();
            QueryRunner qRunner = new QueryRunner();
            List<Map> resultMap = (List<Map> )qRunner.query(conn,sql,new MapListHandler());
            conn.close();  //关闭连接

            if (resultMap != null && resultMap.size() > 0) {
                String sheet1Name = "商品明细表";
                // 创建一个EXCEL
                HSSFWorkbook wb = new HSSFWorkbook();
                // 创建一个SHEET
                HSSFSheet sheet1 = wb.createSheet(sheet1Name);
                String[] titles = {"id","条形码","名称","进货价","美丽价","库存","规格","是否上架","是否预售","所属馆","所属类目"};
                PoiExcelUtil.buildTitles(sheet1, titles);
                int rowNum = 1;//新行号
                int size = resultMap.size();
                for (int i = 0; i < size; i++) {
                    Map map = resultMap.get(i);
                    HSSFRow rowdata = sheet1.createRow(rowNum);
                    // 下面是填充数据
                    rowdata.createCell(0).setCellValue(map.get("pro_id")==null ? "" : map.get("pro_id").toString());//id
                    rowdata.createCell(1).setCellValue(map.get("bar_code").toString()); //条形码
                    rowdata.createCell(2).setCellValue(map.get("pro_name").toString()); //名称
                    BigDecimal tradePrice = (BigDecimal)map.get("trade_price");
                    rowdata.createCell(3).setCellValue(tradePrice!=null ? tradePrice.doubleValue() : new Double(0)); //进货价 没有则显示0
                    BigDecimal mlwPrice = (BigDecimal)map.get("mlw_price");
                    rowdata.createCell(4).setCellValue(mlwPrice!=null ? mlwPrice.doubleValue() : new Double(0)); //美丽价
                    rowdata.createCell(5).setCellValue(map.get("stock")==null ? "0" : map.get("stock").toString()); //库存
                    rowdata.createCell(6).setCellValue(map.get("sku_name")==null ? "" : map.get("sku_name").toString()); //规格
                    rowdata.createCell(7).setCellValue("1".equals(map.get("state").toString()) ? "是" : "否"); //是否上架

                    Date presaleEndTime =(Date) map.get("presale_end_time");
                    if(presaleEndTime!=null  && DateUtil.compare(new Date(), presaleEndTime) == -1){
                        rowdata.createCell(8).setCellValue("是"); //是否预售
                    } else {
                        rowdata.createCell(8).setCellValue("否"); //是否预售
                    }
                    rowdata.createCell(9).setCellValue(map.get("store_name").toString()); //所属馆
                    rowdata.createCell(10).setCellValue(map.get("category_name").toString()); //所属类目
                    rowNum++;
                }
                String excelName = "sku_export" + DateUtil.getCurrentDateStr();
                String writeName = "sku_export" + new Date().getTime();
                PoiExcelUtil.createFile(exportPath, writeName, wb);  //在服务器端写文件
                PoiExcelUtil.download(request, response, "xls", exportPath + writeName, excelName + ".xls");  //在服务器端下载指定文件，完成后删除该文件
            }
        }catch (Exception e){
            logger.error(e, model, WebUtils.getIpAddr(request));
            StageHelper.dwzFailForward("导出商品明细表失败，请联系技术部门", "115", "/pms/product/list", response);
        }
    }

    /**
     * 拼接 导出条件
     * @param bean
     * @return
     */
    private String getSelfCondition(ProductDTO bean){
        String selfCondition = "";
        if(bean!=null){
            if(bean.getProId()!=null){
                selfCondition = selfCondition +"and p.pro_id="+bean.getProId();
            }
            if(bean.getSpuId()!=null){
                selfCondition = selfCondition +" and p.spu_id="+bean.getSpuId();
            }
            if(bean.getThirdCategoryId()!=null){
                selfCondition = selfCondition +" and p.third_category_id="+bean.getThirdCategoryId();
            }
            if(bean.getBrandId()!=null){
                selfCondition = selfCondition +" and p.brand_id="+bean.getBrandId();
            }
            if(bean.getSellStock()!=null){
                selfCondition = selfCondition +" and p.sell_stock>0";
            }
            //库存预警
            if(bean.getSafeStock()!=null){
                selfCondition = selfCondition +" and k.sell_stock <= k.safe_stock";
            }
            if(bean.getState()!=null){
                selfCondition = selfCondition +" and p.state="+bean.getState();
            }
            if(bean.getSupplierId()!=null){
                selfCondition = selfCondition +" and p.supplier_id="+bean.getSupplierId();
            }
            if(bean.getProName()!=null){
                selfCondition = selfCondition +" and p.pro_name like'%"+bean.getProName()+"%'";
            }
            //价格范围 开始
            if(bean.getMlwPriceMin()!=null &&bean.getMlwPriceMin().intValue()>0){
                selfCondition = selfCondition +" and p.mlw_price >="+bean.getMlwPriceMin().doubleValue();
            }
            //价格范围  结束
            if(bean.getMlwPriceMax()!=null &&bean.getMlwPriceMax().intValue()>0){
                selfCondition = selfCondition +" and p.mlw_price <="+bean.getMlwPriceMax().doubleValue();
            }
            if(bean.getUpdateTimeMin()!=null){
                selfCondition = selfCondition+" and p.update_time>='"+DateUtil.getDateTimeStr(bean.getUpdateTimeMin())+"'";
            }
            if(bean.getUpdateTimeMax()!=null){
                selfCondition = selfCondition+" and p.update_time<='"+DateUtil.getDateTimeStr(bean.getUpdateTimeMax())+"'";
            }
            if(bean.getBarCode()!=null){
                selfCondition = selfCondition +" and p.bar_code='"+bean.getBarCode()+"'";
            }
        }
        return selfCondition+" ";
    } ;

    /**
     * 上架
     *
     * @param request
     * @param model
     * @param response
     */
    @RequestMapping(value = "/updatesku-up")
    public void up(HttpServletRequest request, Model model, HttpServletResponse response) throws IOException {
        int optType = ServletRequestUtils.getIntParameter(request,"optType",0);
        if (optType == 0){
            int[] ids = null;
            try {
                ids = ServletRequestUtils.getIntParameters(request, "ids");
                Map<Integer, String> map = ProProductClient.updateStateToONByIds(ids);
                if (map != null && map.size() > 0) {
                    StringBuffer msg = new StringBuffer("部分商品上架失败,数据列表如下:<br>");
                    for (Integer key : map.keySet()) {
                        msg.append(key).append(":").append(map.get(key) + "<br>");
                    }
                    StageHelper.writeDwzSignal("300", msg.toString(), "10315", StageHelper.DWZ_FORWARD, "/pms/product/list", response);
                } else {
                    StageHelper.dwzSuccessForward("操作成功", "", "", response);
                }
            } catch (Exception e) {
                logger.error(e, String.format("ids(%s)", ids), WebUtils.getIpAddr(request));
                StageHelper.dwzFailForward("操作失败请联系管理员", "", "", response);
                return;
            }
        }else {
            int proId = ServletRequestUtils.getIntParameter(request,"proId",0);
            int[] proIds = {proId};
            Map<Integer, String> map = ProProductClient.updateStateToONByIds(proIds);
            String str = "操作成功";
            String suc = "ok";
            if (map != null && map.size() > 0) {
                str = "操作失败，请检查必填参数";
                suc = "error";
            }
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("proId", proId);
            result.put("suc", suc);
            result.put("stateStr", str);
            StageHelper.writeJson(response,result);
        }

    }

    @RequestMapping(value = "/updatesku-down")
    public void changeState(HttpServletRequest request, Model model, HttpServletResponse response) throws IOException {
        int offFlag = ServletRequestUtils.getIntParameter(request, "offFlag", 0);
        if (offFlag > 0) {
            int spuId = ServletRequestUtils.getIntParameter(request, "spuId", 0);
            List<SimpleProduct> products = ProProductClient.getListProBySpuId(spuId);
            List<Integer> idsList = new ArrayList<Integer>();
            if (products != null && products.size() > 0) {
                for (SimpleProduct product:products){
                    if (product.getState() == Constant.PRODUCT_ON){
                        idsList.add(product.getProId());
                    }
                }
                if (idsList!=null&&idsList.size()>0){
                    int[] ids= new int[idsList.size()];
                    for (int i=0;i<idsList.size();i++){
                        ids[i]=idsList.get(i).intValue();
                    }
                    ProProductClient.updateStateToOffByIds(ids);
                }
            }
            response.getWriter().print("ok");
        } else {
            int[] ids = null;
            try {
                ids = ServletRequestUtils.getIntParameters(request, "ids");
                ProProductClient.updateStateToOffByIds(ids);
            } catch (Exception e) {
                logger.error(e, "", WebUtils.getIpAddr(request));
                StageHelper.dwzFailForward("操作失败请联系管理员", "", "", response);
                return;
            }
            StageHelper.dwzSuccessForward("操作成功", "", "", response);
        }
    }


    @RequestMapping(value = "/del")
    public void del(HttpServletRequest request, Model model, HttpServletResponse response) {
        int[] ids = null;
        try {
            ids = ServletRequestUtils.getIntParameters(request, "ids");
            if (ids.length <= 0) {
                throw new IllegalArgumentException("ids is Illegal");
            }
            ProProductClient.delProByIds(ids);
        } catch (Exception e) {
            StageHelper.writeDwzSignal("300", "操作失败请联系管理员", "115", StageHelper.DWZ_FORWARD, "/pms/product/list", response);
        }

        StageHelper.writeDwzSignal("200", "操作成功", "115", StageHelper.DWZ_FORWARD, "/pms/product/list", response);
    }

    /**
     * 增加一个商品列表，专属于客服使用
     *
     * @param request
     * @param model
     * @param response
     * @param bean
     * @return
     */
    @RequestMapping(value = "/listSelect")
    public String listSelect(HttpServletRequest request, Model model, HttpServletResponse response, ProductDTO bean) {
        try {
            PagerControl<ProductDTO> pc = null;
            String orderField = ServletRequestUtils.getStringParameter(request, "orderField", "");
            String orderDirection = ServletRequestUtils.getStringParameter(request, "orderDirection", "");

            if (bean == null) {
                bean = new ProductDTO();
            }

            PageInfo pageInfo = WebParamterUtil.getPageInfo(request);
            pageInfo.setOrderField(orderField);
            pageInfo.setOrderDirection(orderDirection);
            if ("".equals(orderField)) {
                pageInfo.setOrderField("update_time");
                pageInfo.setOrderDirection("desc");
            }

            setSkuSearchParam(request, model, bean);
            pc = ProProductClient.pageByObj(bean, pageInfo, pageInfo.getOrderField(), pageInfo.getOrderDirection());
            setSupplier(model);
            setState(model);
            setSellType(model);
            setCategory(model);
            model.addAttribute("pc", pc);
        } catch (Exception e) {
            logger.error(e, bean, WebUtils.getIpAddr(request));
        }
        return "/pms/product/list_select";
    }

    @RequestMapping(value = "/quickUpdate")
    public String quickUpdate(HttpServletRequest request, Model model, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        int proId = ServletRequestUtils.getIntParameter(request, "proId", 0);
        //从哪个按钮来的
        String where = ServletRequestUtils.getStringParameter(request, "where", "");
        try {
            if (isHandle > 0 && proId > 0) {
                //获取价格
                String mlwPrice = ServletRequestUtils.getStringParameter(request, "mlwPrice", "");
                //获取市场价
                String marketPrice = ServletRequestUtils.getStringParameter(request, "marketPrice", "");
                //获取进货价
                String tradePrice = ServletRequestUtils.getStringParameter(request, "tradePrice", "");
                //获取库存
                int stock = ServletRequestUtils.getIntParameter(request, "stock", -1);
                //获取显示销量
                int showSale = ServletRequestUtils.getIntParameter(request, "showSale", -1);
                //获取安全库存
                int safeStock = ServletRequestUtils.getIntParameter(request, "safeStock", -1);
                if (!StringUtils.isEmpty(mlwPrice) && !StringUtils.isEmpty(marketPrice) && !StringUtils.isEmpty(tradePrice)) {
                    BigDecimal price = new BigDecimal(mlwPrice);
                    if (price.compareTo(new BigDecimal("0.00")) > 0) {
                        try {
                            ProProduct product = new ProProduct();
                            product.setProId(proId);
                            product.setMlwPrice(price);
                            product.setMarketPrice(new BigDecimal(marketPrice));
                            product.setTradePrice(new BigDecimal(tradePrice));
                            ProProductClient.updateByPrice(product);
                        } catch (Exception e) {
                            logger.error(e, "isHandle:" + isHandle + ",proId:" + proId + ",mlwPrice:" + mlwPrice, WebUtils.getIpAddr(request));
                            return StageHelper.writeDwzSignal("300", "修改价格失败", "115", StageHelper.DWZ_CLOSE_CURRENT, "/pms/product/list", response);
                        }
                    } else {
                        return StageHelper.writeDwzSignal("300", "价格不能必须大于0，修改价格失败", "115", StageHelper.DWZ_CLOSE_CURRENT, "/pms/product/list", response);
                    }
                }
                if (stock >= 0) {
                    int upType = ServletRequestUtils.getIntParameter(request, "upType", 1);
                    try {
                        boolean suc = false;
                        if (upType == 1) {
                            suc = ProStockClient.addSellStock(proId, stock, StageHelper.getLoginUser(request).getBksAdmin().getId(), "admin");
                        } else {
                            suc = ProStockClient.subSellStock(proId, stock, StageHelper.getLoginUser(request).getBksAdmin().getId(), "admin");
                        }
                        if (!suc) {
                            return StageHelper.writeDwzSignal("300", "修改库存失败", "115", StageHelper.DWZ_CLOSE_CURRENT, "/pms/product/list", response);
                        }
                    } catch (Exception e) {
                        logger.error(e, "isHandle:" + isHandle + ",proId:" + proId + ",stock:" + stock + ",upType:" + upType, WebUtils.getIpAddr(request));
                        return StageHelper.writeDwzSignal("300", "修改库存失败", "115", StageHelper.DWZ_CLOSE_CURRENT, "/pms/product/list", response);
                    }
                }
                if (safeStock >= 0) {
                    try {
                        ProStockClient.updateSafeStock(proId, safeStock);
                    } catch (Exception e) {
                        logger.error(e, "proId:" + proId + ",safeStock:" + safeStock, WebUtils.getIpAddr(request));
                        return StageHelper.writeDwzSignal("300", "修改安全库存失败", "115", StageHelper.DWZ_CLOSE_CURRENT, "/pms/product/list", response);
                    }
                }
                if (showSale >= 0) {
                    ProAction action = new ProAction();
                    action.setProId(proId);
                    action.setShowSaleNum(showSale);
                    try {
                        ProActionClient.updateAction(action);
                    } catch (Exception e) {
                        logger.error(e, action, WebUtils.getIpAddr(request));
                        return StageHelper.writeDwzSignal("300", "修改销量失败", "115", StageHelper.DWZ_CLOSE_CURRENT, "/pms/product/list", response);
                    }
                }
                return StageHelper.writeDwzSignal("200", "操作成功", "115", StageHelper.DWZ_CLOSE_CURRENT, "/pms/product/list?proId=" + proId, response);
            }


            if (where.equals("sale")) {
                ProAction action = ProActionClient.getActionByProId(proId);
                model.addAttribute("action", action);
            } else if (where.equals("safe")) {
                ProStock stock = ProStockClient.getStockById(proId);
                model.addAttribute("safeStock", stock.getSafeStock());
            } else if (where.equals("stock")) {
                int sellStock = ProStockClient.getSellStock(proId);
                model.addAttribute("sellStock", sellStock);
            } else {
                ProProduct bean = ProProductClient.getWholeProductById(proId);
                model.addAttribute("bean", bean);
            }
            model.addAttribute("proId", proId);
            model.addAttribute("where", where);
        } catch (IceServiceRuntimeException e) {
            logger.error(e, "isHandle:" + isHandle + ",proId:" + proId + ",where:" + where, WebUtils.getIpAddr(request));
            StageHelper.writeDwzSignal("300", "操作失败请联系管理员", "115", StageHelper.DWZ_CLOSE_CURRENT, "/pms/product/list", response);
        } catch (Exception e) {
            logger.error(e, "isHandle:" + isHandle + ",proId:" + proId + ",where:" + where, WebUtils.getIpAddr(request));
            StageHelper.writeDwzSignal("300", "操作失败请联系管理员", "115", StageHelper.DWZ_CLOSE_CURRENT, "/pms/product/list", response);
        }
        return "/pms/product/quick_update";
    }


    /**
     * 自定义sku参数修改
     * add by yyluo
     *
     * @param request
     * @param model
     * @param response
     * @return
     */
    @RequestMapping(value = "/updateskuSelfProp")
    public String updateSkuSelfProp(HttpServletRequest request, Model model, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        int proId = ServletRequestUtils.getIntParameter(request, "proId", 0);
        if (isHandle > 0) { //保存
            boolean isSaveSuccess = false;
            try {
                String[] selfPropIds = request.getParameterValues("selfPropId");
                String[] selfPropNames = request.getParameterValues("selfPropName");
                String[] selfPropValues = request.getParameterValues("selfPropValue");

                List<SkuSelfProperty> sspList = new ArrayList<SkuSelfProperty>();
                for (int i = 0; i < selfPropIds.length; i++) {
                    SkuSelfProperty ssp = new SkuSelfProperty();
                    if (StringUtils.isNumeric(selfPropIds[i]) && Integer.parseInt(selfPropIds[i]) > 0) {
                        ssp.setId(Integer.parseInt(selfPropIds[i]));
                    }
                    ssp.setSelfPropName(selfPropNames[i]);
                    ssp.setSelfPropValue(selfPropValues[i]);
                    sspList.add(ssp);
                }
                isSaveSuccess = ProProductClient.updateSkuSelfProperty(sspList, proId);
            } catch (Exception e) {
                logger.error(e, "修改自定义sku属性异常 :isHandle:" + isHandle + ",proId:" + proId, WebUtils.getIpAddr(request));
            }
            if (isSaveSuccess) {
                return StageHelper.writeDwzSignal("200", "修改自定义sku属性成功", "viewDetail", StageHelper.DWZ_CLOSE_CURRENT, "/pms/product/product-detail?proId=" + proId, response);
            } else {
                return StageHelper.writeDwzSignal("300", "修改自定义sku属性失败", "119", StageHelper.DWZ_FORWARD, "/pms/product/updateSkuSelfProp", response);
            }

        } else {    //去修改页
            try {
                ProProduct product = ProProductClient.getProductForAdminShowById(proId);
                if (product != null) {
                    model.addAttribute("psplist", product.getSkuSelfProps());
                }
            } catch (Exception e) {
                logger.error(e, "去修改自定义sku属性页异常 :isHandle:" + isHandle + ",proId:" + proId, WebUtils.getIpAddr(request));
            }
        }

        model.addAttribute("proId", proId);
        return "/pms/product/update_skuSelfProp";
    }

    /**
     * 商品价格修改
     * add by yyluo
     *
     * @param request
     * @param model
     * @param response
     * @return
     */
    @RequestMapping(value = "/updateskuPrice")
    public String updatePrice(HttpServletRequest request, Model model, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        int proId = ServletRequestUtils.getIntParameter(request, "proId", 0);
        if (isHandle > 0) { //保存
            boolean isSaveSuccess = false;
            //获取价格
            String mlwPrice = ServletRequestUtils.getStringParameter(request, "mlwPrice", "");
            //获取市场价
            String marketPrice = ServletRequestUtils.getStringParameter(request, "marketPrice", "");
            //获取进货价
            String tradePrice = ServletRequestUtils.getStringParameter(request, "tradePrice", "");
            try {

                if (!StringUtils.isEmpty(mlwPrice) && !StringUtils.isEmpty(marketPrice) && !StringUtils.isEmpty(tradePrice)) {
                    BigDecimal price = new BigDecimal(mlwPrice);
                    if (price.compareTo(new BigDecimal("0.00")) > 0) {
                        try {
                            ProProduct product = new ProProduct();
                            product.setProId(proId);
                            product.setMlwPrice(price);
                            product.setMarketPrice(new BigDecimal(marketPrice));
                            product.setTradePrice(new BigDecimal(tradePrice));
                            isSaveSuccess = ProProductClient.updateByPrice(product);
                        } catch (Exception e) {
                            logger.error(e, "isHandle:" + isHandle + ",proId:" + proId + ",mlwPrice:" + mlwPrice, WebUtils.getIpAddr(request));
                            return StageHelper.writeDwzSignal("300", "修改价格失败", "115", StageHelper.DWZ_CLOSE_CURRENT, "/pms/product/updatePrice", response);
                        }
                    } else {
                        return StageHelper.writeDwzSignal("300", "价格不能必须大于0，修改价格失败", "115", StageHelper.DWZ_CLOSE_CURRENT, "/pms/product/updatePrice", response);
                    }
                }
            } catch (Exception e) {
                logger.error(e, "修改价格异常 :isHandle:" + isHandle + ",proId:" + proId + "mlwPrice:" + mlwPrice + "marketPrice:" + marketPrice + "tradePrice:" + tradePrice, WebUtils.getIpAddr(request));
            }
            if (isSaveSuccess) {
                return StageHelper.writeDwzSignal("200", "修改价格成功", "viewDetail", StageHelper.DWZ_CLOSE_CURRENT, "/pms/product/product-detail?proId=" + proId, response);
            } else {
                return StageHelper.writeDwzSignal("300", "修改价格失败", "119", StageHelper.DWZ_FORWARD, "/pms/product/updatePrice", response);
            }

        } else {    //去修改页
            try {
                ProProduct product = ProProductClient.getWholeProductById(proId);
                if (product != null) {
                    model.addAttribute("proId", product.getProId());
                    model.addAttribute("mlwPrice", product.getMlwPrice());
                    model.addAttribute("marketPrice", product.getMarketPrice());
                    model.addAttribute("tradePrice", product.getTradePrice());
                }
            } catch (Exception e) {
                logger.error(e, "修改价格失败异常 :isHandle:" + isHandle + ",proId:" + proId, WebUtils.getIpAddr(request));
            }
        }

        model.addAttribute("proId", proId);
        return "/pms/product/update_price";
    }

    /**
     * sku  库存修改
     * add by yyluo
     *
     * @param request
     * @param model
     * @param response
     * @return
     */
    @RequestMapping(value = "/updateskuStock")
    public String updateStock(HttpServletRequest request, Model model, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        int proId = ServletRequestUtils.getIntParameter(request, "proId", 0);
        StringBuffer updateResult = new StringBuffer();
        if (isHandle > 0) { //保存
            boolean isSellStockSuccess = true;
            boolean isSafeStockSuccess = true;
            //获取库存
            int stock = ServletRequestUtils.getIntParameter(request, "stock", -1);
            //获取安全库存
            int safeStock = ServletRequestUtils.getIntParameter(request, "safeStock", -1);
            //1、库存
            if (stock >= 0) {
                int upType = ServletRequestUtils.getIntParameter(request, "upType", 1);   //增加or减少库存
                try {
                    if (upType == 1) {
                        isSellStockSuccess = ProStockClient.addSellStock(proId, stock, StageHelper.getLoginUser(request).getBksAdmin().getId(), "admin");
                    } else {
                        isSellStockSuccess = ProStockClient.subSellStock(proId, stock, StageHelper.getLoginUser(request).getBksAdmin().getId(), "admin");
                    }
                    if (!isSellStockSuccess) {
                        updateResult.append("修改库存失败！");
                    } else {
                        updateResult.append("修改库存成功！");
                    }
                } catch (Exception e) {
                    isSellStockSuccess = false;
                    updateResult.append("修改库存异常！");
                    logger.error(e, "修改库存异常！:isHandle:" + isHandle + ",proId:" + proId + ",stock:" + stock + ",upType:" + upType, WebUtils.getIpAddr(request));
                    return StageHelper.writeDwzSignal("300", updateResult.toString(), "115", StageHelper.DWZ_CLOSE_CURRENT, "/pms/product/updateStock", response);
                }
            }
            //2、安全库存
            if (safeStock >= 0) {
                try {
                    isSafeStockSuccess = ProStockClient.updateSafeStock(proId, safeStock);
                    if (!isSafeStockSuccess) {
                        updateResult.append("修改安全库存失败！");
                    } else {
                        updateResult.append("修改安全库存成功！");
                    }
                } catch (Exception e) {
                    isSafeStockSuccess = false;
                    updateResult.append("修改安全库存异常！");
                    logger.error(e, "修改安全库存异常！:proId:" + proId + ",safeStock:" + safeStock, WebUtils.getIpAddr(request));
                }
            }

            if (isSellStockSuccess && isSafeStockSuccess) {
                return StageHelper.writeDwzSignal("200", "修改库存成功！", "viewDetail", StageHelper.DWZ_CLOSE_CURRENT,
                        "/pms/product/product-detail?proId=" + proId, response);
            } else {
                return StageHelper.writeDwzSignal("300", updateResult.toString(), "119", StageHelper.DWZ_FORWARD, "/pms/product/updatePrice", response);
            }


        } else {    //去修改页
            try {
                ProStock stock = ProStockClient.getStockById(proId);
                if (stock != null) {
                    model.addAttribute("proId", proId);
                    model.addAttribute("sellStock", stock.getSellStock());
                    model.addAttribute("safeStock", stock.getSafeStock());
                }
            } catch (Exception e) {
                logger.error(e, "去修改库存页异常！ :isHandle:" + isHandle + ",proId:" + proId, WebUtils.getIpAddr(request));
            }
        }
        model.addAttribute("proId", proId);
        return "/pms/product/update_stock";
    }

    /**
     * 商品条形码修改
     * add by yyluo
     *
     * @param request
     * @param model
     * @param response
     * @return
     */
    @RequestMapping(value = "/updateskuBarcode")
    public String updateSkuBarcode(HttpServletRequest request, Model model, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        int proId = ServletRequestUtils.getIntParameter(request, "proId", 0);
        if (isHandle > 0) { //保存
            boolean isSuccess = false;
            //获取条形码
            String barCode = ServletRequestUtils.getStringParameter(request, "barCode", null);
            if (barCode != null && proId > 0) {
                try {
                    boolean isUsed = ProProductClient.checkProductByBarCode(barCode, proId, "update");
                    if (!isUsed) {
                        ProProduct product = new ProProduct();
                        product.setProId(proId);
                        product.setBarCode(barCode);
                        isSuccess = ProProductClient.updateByProduct(product);
                        //储位信息修改
                        if (isSuccess) {
                            boolean isLocationSuc = false;
                            try {
                                //添加商品一条默认的储位信息,先判断是否已经添加了储位
                                ProLocation lt = ProLocationClient.getLocationByBarCode(product.getBarCode());
                                lt.setBarCode(barCode);
                                lt.setUpdateTime(new Date());
                                isLocationSuc = ProLocationClient.updateProLocation(lt);
                            } catch (Exception e) {
                                logger.error(e, "修改储位信息异常，barCode=" + barCode + ",product=" + product, WebUtils.getIpAddr(request));
                            }
                            if (!isLocationSuc) {
                                logger.warn("修改储位信息失败", "barCode=" + barCode + ",product=" + product, WebUtils.getIpAddr(request));
                            }
                        }

                    } else {
                        return StageHelper.writeDwzSignal("300", barCode + "该条形码已被占用！", "115", StageHelper.DWZ_FORWARD, "/pms/product/updateStock", response);
                    }
                } catch (Exception e) {
                    logger.error(e, "修改条形码异常！:isHandle:" + isHandle + ",proId:" + proId + ",barCode:" + barCode, WebUtils.getIpAddr(request));
                    return StageHelper.writeDwzSignal("300", "修改条形码失败！", "115", StageHelper.DWZ_CLOSE_CURRENT, "/pms/product/updateSkuBarcode", response);
                }
            }
            if (isSuccess) {
                return StageHelper.writeDwzSignal("200", "修改条形码成功！", "viewDetail", StageHelper.DWZ_CLOSE_CURRENT,
                        "/pms/product/product-detail?proId=" + proId, response);
            }
        } else {    //去修改页
            try {
                SimpleProduct product = ProProductClient.getProductById(proId);
                if (product != null) {
                    model.addAttribute("proId", proId);
                    model.addAttribute("barCode", product.getBarCode());
                }
            } catch (Exception e) {
                logger.error(e, "去条形码页修改异常！ :isHandle:" + isHandle + ",proId:" + proId, WebUtils.getIpAddr(request));
            }
        }
        model.addAttribute("proId", proId);
        return "/pms/product/update_barCode";
    }

    /**
     * 商品地方频道修改
     * add by yyluo
     *
     * @param request
     * @param model
     * @param response
     * @return
     */
    @RequestMapping(value = "/updatesku-nationimage")
    public String updateNationImage(HttpServletRequest request, Model model, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        int proId = ServletRequestUtils.getIntParameter(request, "proId", 0);
        String fallsImageUri = ServletRequestUtils.getStringParameter(request, "fallsImageUri", "");
        String isFalls = ServletRequestUtils.getStringParameter(request, "isFalls", "");
        int height = ServletRequestUtils.getIntParameter(request, "height", 0);

        if (isHandle > 0) { //保存
            boolean isSuccess = false;
            ProProduct proProduct = new ProProduct();
            if ("1".equals(isFalls)) {
                proProduct.setIsFalls((short) 1);
            } else {
                proProduct.setIsFalls((short) 0);
            }
            if (height > 0) {
                proProduct.setHeight(height);
            }
            proProduct.setFallsImageUri(fallsImageUri);
            try {
                if (proId > 0) {
                    proProduct.setProId(proId);
                    isSuccess = ProProductClient.updateByProduct(proProduct);
                }
            } catch (Exception e) {
                logger.error(e, "地方频道修改异常！ :isHandle:" + isHandle + ",proId:" + proId, WebUtils.getIpAddr(request));
            }
            if (isSuccess) {
                return StageHelper.writeDwzSignal("200", "地方频道修改成功！", "10141", StageHelper.DWZ_CLOSE_CURRENT,
                        "/pms/product/product-detail?proId=" + proId, response);
            } else {
                return StageHelper.writeDwzSignal("300", "地方频道修改失败！", "119", StageHelper.DWZ_FORWARD,
                        "/pms/product/updatesku-nationimage?proId=" + proId, response);
            }
        } else {    //去修改页
            try {
                ProProduct product = ProProductClient.getWholeProductById(proId);
                if (product != null) {
                    model.addAttribute("proId", proId);
                    model.addAttribute("isFalls", product.getIsFalls());
                    model.addAttribute("fallsImageUri", product.getFallsImageUri());
                    model.addAttribute("height", product.getHeight());
                }
            } catch (Exception e) {
                logger.error(e, "去地方频道修改页异常！ :isHandle:" + isHandle + ",proId:" + proId, WebUtils.getIpAddr(request));
            }
        }
        model.addAttribute("proId", proId);
        return "/pms/product/update_nation_image";
    }

    @RequestMapping(value = "/product-detail")
    public String viewProductDetail(HttpServletRequest request, Model model) {
        int proId = ServletRequestUtils.getIntParameter(request, "proId", 0);
        //获取商品相关信息
        ProProduct product = ProProductClient.getProductForAdminShowById(proId);
        //获取库存相关信息
        ProStock stock = ProStockClient.getStockById(proId);

        if (product != null) {
            if (product.getThirdCategoryId() > 0) {
                //获取类目相关信息
                ProCategory thirdCategory = ProCategoryClient.getProCategoryById(product.getThirdCategoryId());
                ProCategory secondCategory = ProCategoryClient.getProCategoryById(thirdCategory.getParentId());
                ProCategory firstCategory = ProCategoryClient.getProCategoryById(secondCategory.getParentId());
                model.addAttribute("thirdCategory", thirdCategory.getCategoryName());
                model.addAttribute("secondCategory", secondCategory.getCategoryName());
                model.addAttribute("firstCategory", firstCategory.getCategoryName());
            }
            if (product.getSpuId() > 0) {
                //获取规格信息
                setSkuGuiGeInfo(product, model);
            }
        }
        int preSale = 0;
        if (product.isPresale()){
            preSale = 1;
        }
        model.addAttribute("preSale",preSale);
        model.addAttribute("product", product);
        model.addAttribute("stock", stock);

        return "/pms/product/detail";
    }

    @RequestMapping("/updateskuPresale")
    public String updateskuPresale(HttpServletRequest request, Model model, HttpServletResponse response){
        int handle = ServletRequestUtils.getIntParameter(request,"handle",0);
        int proId = ServletRequestUtils.getIntParameter(request,"proId",0);
        if (handle>0){
            String presaleEndTime = ServletRequestUtils.getStringParameter(request,"presaleEndTime",null);
            String presaleSendTime = ServletRequestUtils.getStringParameter(request,"presaleSendTime",null);
            if (StringUtils.isEmpty(presaleEndTime)){
                presaleEndTime = "0000-00-00 00:00:00";
            }
            if (StringUtils.isEmpty(presaleSendTime)){
                presaleSendTime = "0000-00-00 00:00:00";
            }

            try {
                int count = ProProductClient.updatePresaleTime(proId,presaleEndTime,presaleSendTime);
                if (count>0){
                    return StageHelper.writeDwzSignal("200", "修改预售相关时间成功！", "10141", StageHelper.DWZ_CLOSE_CURRENT,
                            "/pms/product/product-detail?proId=" + proId, response);
                }else {
                    return StageHelper.writeDwzSignal("300", "修改预售相关时间失败！", "119", StageHelper.DWZ_FORWARD,
                            "/pms/product/updateskuPresale?proId=" + proId, response);
                }
            }catch (IceServiceRuntimeException e) {
                logger.error(e, proId, WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "修改预售相关时间失败！", "119", StageHelper.DWZ_FORWARD,
                        "/pms/product/updateskuPresale?proId=" + proId, response);
            } catch (Exception e) {
                logger.error(e, proId, WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "修改预售相关时间失败！", "119", StageHelper.DWZ_FORWARD,
                        "/pms/product/updateskuPresale?proId=" + proId, response);
            }
        }else {
            SimpleProduct product = ProProductClient.getProductById(proId);
            model.addAttribute("bean",product);
            return "/pms/product/update_presale_time";
        }
    }

    //修改或者增加商品的额外积分
    @RequestMapping(value = "/update-integral")
    public String updateProIntegral(HttpServletRequest request, Model model, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        int proId = ServletRequestUtils.getIntParameter(request, "proId", 0);
        String optType = ServletRequestUtils.getStringParameter(request, "optType", "add");
        try {
            UserProExtraIntegral integral = UserProExtraIntegralServiceClient.getUserProExtraIntegralByProId(proId);
            if (integral != null) {
                optType = "update";
            }
            if (isHandle > 0) {
                int value = ServletRequestUtils.getIntParameter(request, "intralValue", 0);
                UserProExtraIntegral pexi = new UserProExtraIntegral();
                pexi.setProId(proId);
                pexi.setValue(value);
                if (optType.equals("add")) {
                    UserProExtraIntegralServiceClient.saveUserProExtraIntegral(pexi);
                } else {
                    int petId = ServletRequestUtils.getIntParameter(request, "petId", 0);
                    pexi.setId(petId);
                    UserProExtraIntegralServiceClient.updateUserProExtraIntegral(pexi);
                }
                return StageHelper.writeDwzSignal("200", "操作成功", "115", StageHelper.DWZ_CLOSE_CURRENT, "/pms/product/list?proId=" + proId, response);
            }
            model.addAttribute("integral", integral);
            model.addAttribute("optType", optType);
            model.addAttribute("proId", proId);
        } catch (IceServiceRuntimeException e) {
            logger.error(e, "isHandle:" + isHandle + ",proId:" + proId + ",optType:" + optType, WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("300", "操作额外积分失败，请联系管理员", "10141", StageHelper.DWZ_FORWARD, "/pms/product/update-integral", response);
        } catch (Exception e) {
            logger.error(e, "isHandle:" + isHandle + ",proId:" + proId + ",optType:" + optType, WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("300", "操作额外积分失败，请联系管理员", "10141", StageHelper.DWZ_CLOSE_CURRENT, "/pms/product/update-integral", response);
        }
        return "/pms/product/update_integral";
    }

    //seo编辑功能
    @RequestMapping(value = "/update-seo")
    public String updateProSeo(HttpServletRequest request, Model model, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        int proId = ServletRequestUtils.getIntParameter(request, "proId", 0);
        try {
            if (isHandle > 0) {
                String seoTitle = ServletRequestUtils.getStringParameter(request, "seoTitle", null);
                String seoKeyword = ServletRequestUtils.getStringParameter(request, "seoKeyword", null);
                String seoDescp = ServletRequestUtils.getStringParameter(request, "seoDescp", null);
                ProDetail detail = new ProDetail();
                // detail.setProId(proId);
                detail.setSeoTitle(seoTitle);
                detail.setSeoKeyword(seoKeyword);
                detail.setSeoDescp(seoDescp);
                try {
                    boolean suc = ProSpuClient.updateProDetail(detail);
                    if (suc) {
                        return StageHelper.writeDwzSignal("200", "操作成功", "115", StageHelper.DWZ_CLOSE_CURRENT, "/pms/product/list?proId=" + proId, response);
                    } else {
                        return StageHelper.writeDwzSignal("300", "修改SEO失败，请联系管理员", "10141", StageHelper.DWZ_FORWARD, "/pms/product/update-seo", response);
                    }
                } catch (Exception e) {
                    logger.error(e, detail, WebUtils.getIpAddr(request));
                    return StageHelper.writeDwzSignal("300", "修改SEO失败，请联系管理员", "10141", StageHelper.DWZ_FORWARD, "/pms/product/update-seo", response);
                }

            } else {
                ProDetail detail = ProProductClient.getProDetailByProId(proId);
                model.addAttribute("detail", detail);
            }
        } catch (IceServiceRuntimeException e) {
            logger.error(e, "isHandle:" + isHandle + ",proId:" + proId, WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("300", "操作失败请联系管理员", "10141", StageHelper.DWZ_FORWARD, "/pms/product/update-seo", response);
        } catch (Exception e) {
            logger.error(e, "isHandle:" + isHandle + ",proId:" + proId, WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("300", "操作失败请联系管理员", "10141", StageHelper.DWZ_FORWARD, "/pms/product/update-seo", response);
        }
        return "/pms/product/update_seo";
    }

    /**
     * 设置优秀评论
     */
    @RequestMapping(value = "/updateExcellentComment")
    public void updateExcellentComment(HttpServletRequest request, Model model, HttpServletResponse response) {
        int commentId = -1;
        int proId = -1;
        try {
            commentId = ServletRequestUtils.getIntParameter(request, "commentId", -1);
            proId = ServletRequestUtils.getIntParameter(request, "proId", -1);
            boolean success = false;
            //commentId＝0为取消优秀评论
            if (commentId >= 0 && proId > 0) {
                success = ProActionClient.updateCommentIdById(proId, commentId);
            }
            if (success) {
                StageHelper.dwzSuccessForward("设置优秀评论成功", "", "", response);
                return;
            }
            StageHelper.dwzFailForward("设置优秀评论失败", "", "", response);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e, String.format("commentId(%s),proId(%s)", commentId, proId), WebUtils.getIpAddr(request));
            StageHelper.dwzFailForward("设置优秀评论失败", "", "", response);
        }

    }

    /**
     * 导出
     */
    @RequestMapping(value = "/getStockExcel")
    public void getStockExcel(HttpServletRequest request, HttpServletResponse response, Model model) {
        ProductDTO bean = new ProductDTO();
        String excelName = "Stock_export" + DateUtil.getCurrentDateStr();
        boolean success = false;
        try {
            PagerControl<ProductDTO> pc = null;
            String orderField = ServletRequestUtils.getStringParameter(request, "orderField", "");
            String orderDirection = ServletRequestUtils.getStringParameter(request, "orderDirection", "");

            PageInfo pageInfo = WebParamterUtil.getPageInfo(request);
            pageInfo.setOrderField(orderField);
            pageInfo.setOrderDirection(orderDirection);
            pageInfo.setPagesize(100000);
            if (StringUtils.isEmpty(orderField)) {
                pageInfo.setOrderField("update_time");
                pageInfo.setOrderDirection("desc");
            }
            setSkuSearchParam(request, model, bean);
            pc = ProProductClient.pageByObj(bean, pageInfo, pageInfo.getOrderField(), pageInfo.getOrderDirection());
            if (pc != null && pc.getEntityList() != null && pc.getEntityList().size() > 0) {
                String sheet1Name = "商品库存表";
                // 创建一个EXCEL
                HSSFWorkbook wb = new HSSFWorkbook();
                // 创建一个SHEET
                HSSFSheet sheet1 = wb.createSheet(sheet1Name);
                String[] titles = {"商品条形码", "商品名称", "库存"};
                PoiExcelUtil.buildTitles(sheet1, titles);
                int rowNum = 1;//新行号
                List<ProductDTO> list = pc.getEntityList();
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    ProductDTO dto = list.get(i);
                    HSSFRow rowdata = sheet1.createRow(rowNum);
                    // 下面是填充数据
                    rowdata.createCell(0).setCellValue(dto.getBarCode());//商品条形码
                    rowdata.createCell(1).setCellValue(dto.getProName()); //商品名称
                    rowdata.createCell(2).setCellValue(dto.getSellStock() == null ? 0 : dto.getSellStock());//可使用库存
                    rowNum++;
                }
                String writeName = "Stock_export" + new Date().getTime();
                PoiExcelUtil.createFile(exportPath, writeName, wb);  //在服务器端写文件
                PoiExcelUtil.download(request, response, "xls", exportPath + writeName, excelName + ".xls");  //在服务器端下载指定文件，完成后删除该文件
            } else {
                list(request, model, response, bean);
            }
        } catch (Exception e) {
            logger.error(e, model, WebUtils.getIpAddr(request));
            StageHelper.dwzFailForward("导出" + excelName + "失败，请联系技术部门", "115", "/pms/product/list", response);
        }
    }

    //sku管理 搜索条件设置
    private void setSkuSearchParam(HttpServletRequest request, Model model, ProductDTO bean) {
        //商品状态
        int state = ServletRequestUtils.getIntParameter(request, "state", 1);
        bean.setState(state);

        //商品id
        int proId = ServletRequestUtils.getIntParameter(request, "pro_id", 0);
        if (proId > 0) {
            bean.setProId(proId);
        }

        //spu_id
        int spuId = ServletRequestUtils.getIntParameter(request, "spu_id", 0);
        if (spuId > 0) {
            bean.setSpuId(spuId);
        }

        //商品标题
        String proName = ServletRequestUtils.getStringParameter(request, "pro_name", "");
        if (!"".equals(proName.trim())) {
            bean.setProName(proName);
        }

        //条形码
        String barCode = ServletRequestUtils.getStringParameter(request, "bar_code", "");
        if (!"".equals(barCode.trim())) {
            bean.setBarCode(barCode);
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

        // safeStock !＝null则筛选
        int safeStock = ServletRequestUtils.getIntParameter(request, "safeStock", 0);
        if (safeStock != 0) {
            bean.setSafeStock(safeStock);
        }

        model.addAttribute("bean", bean);
    }

    private ProSpu getSpu(HttpServletRequest request) {
        //获取类目ID
        int firstLevel = ServletRequestUtils.getIntParameter(request, "firstLevel", 0);
        int secondLevel = ServletRequestUtils.getIntParameter(request, "secondLevel", 0);
        int thirdLevel = ServletRequestUtils.getIntParameter(request, "thirdLevel", 0);

        //获取标题
        String proName = ServletRequestUtils.getStringParameter(request, "proName", null);
        String shortName = ServletRequestUtils.getStringParameter(request, "shortName", null);
        String advName = ServletRequestUtils.getStringParameter(request, "advName", null);

        //获取品牌、产地、经营方式、供应商相关
        int brandId = ServletRequestUtils.getIntParameter(request, "brand.id", 0);
        int placeId = ServletRequestUtils.getIntParameter(request, "placeId", 0);
        int supplierId = ServletRequestUtils.getIntParameter(request, "supplierId", 0);

        //获取创建者ID
        int adminId = StageHelper.getLoginUser(request).getBksAdmin().getAdminId();
        //获取是否支持货到付款
        int isPay = ServletRequestUtils.getIntParameter(request, "isCod", 1);
        ProSpu product = new ProSpu();

        product.setProName(proName);
        product.setShortName(shortName);
        product.setAdvName(advName);
        product.setFirstCategoryId(firstLevel);
        product.setSecondCategoryId(secondLevel);
        product.setThirdCategoryId(thirdLevel);
        product.setPlaceId(placeId);
        product.setBrandId(brandId);
        product.setAdminId(adminId);
        product.setSupplierId(supplierId);
        product.setIsCod((short) isPay);

        return product;
    }

    private ProProduct getProduct(HttpServletRequest request) {
        //获取价格相关
        ProProduct product = new ProProduct();
        String mlwPrice = ServletRequestUtils.getStringParameter(request, "mlwPrice", "0");
        String marketPrice = ServletRequestUtils.getStringParameter(request, "marketPrice", "0");
        String tradePrice = ServletRequestUtils.getStringParameter(request, "tradePrice", "0");
        int showSellNum = ServletRequestUtils.getIntParameter(request, "showSellNum", 0);
        int state = ServletRequestUtils.getIntParameter(request, "state", 0);
        ProAction action = new ProAction();
        action.setShowSaleNum(showSellNum);
        //获取创建者ID
        int adminId = StageHelper.getLoginUser(request).getBksAdmin().getAdminId();
        //获取是否加入频道功能
        int isFalls = ServletRequestUtils.getIntParameter(request, "isFalls", 0);
        if (isFalls == 1) {
            String fallsImageUri = ServletRequestUtils.getStringParameter(request, "fallsImageUri", null);
            Integer height = ServletRequestUtils.getIntParameter(request, "imgHeight", 0);
            product.setFallsImageUri(fallsImageUri);
            product.setHeight(height);
            product.setNationTime(new Date());
        }
        if (state == Constant.PRODUCT_OFF) {
            product.setOffTime(new Date());
        }
        String presaleEndTime = ServletRequestUtils.getStringParameter(request,"presaleEndTime",null);
        String presaleSendTime = ServletRequestUtils.getStringParameter(request,"presaleSendTime",null);
        if (StringUtils.isNotEmpty(presaleEndTime)){
            product.setPresaleEndTime(DateUtil.parseDateTime(presaleEndTime));
        }
        if (StringUtils.isNotEmpty(presaleSendTime)){
            product.setPresaleSendTime(DateUtil.parseDateTime(presaleSendTime));
        }
        product.setState((short) state);
        product.setIsFalls((short) isFalls);
        product.setMlwPrice(new BigDecimal(mlwPrice));
        product.setMarketPrice(new BigDecimal(marketPrice));
        product.setTradePrice(new BigDecimal(tradePrice));
        product.setAdminId(adminId);
        product.setAction(action);
        return product;
    }

    //获取商品相关的属性，这一部分最复杂，要做按照不同条件的拆分
    private Map<String, Object> getPropertyAction(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        //获取SKU属性对应值
        String[] skuProp = {};
        try {
            skuProp = ServletRequestUtils.getRequiredStringParameters(request, "skuProp");
        } catch (Exception e) {
        }
        //获取异图属性ID
        String skuImage = ServletRequestUtils.getStringParameter(request, "skuImage", "0");

        //获取SKU属性值列表
        String[] skuProv = {};
        try {
            skuProv = ServletRequestUtils.getRequiredStringParameters(request, "skuProv");
        } catch (Exception e) {
        }

        //获取类目的属性
        String[] props = {};
        try {
            props = ServletRequestUtils.getRequiredStringParameters(request, "props");
        } catch (Exception e) {
        }

        //获取输入的属性
        int[] proPropId = {};
        String[] proPropV = {};
        try {
            proPropId = ServletRequestUtils.getRequiredIntParameters(request, "proPropId");
            proPropV = ServletRequestUtils.getRequiredStringParameters(request, "proPropV");
        } catch (Exception e) {
        }

        //用于保存属性值ID组成的字符串
        StringBuffer propertyString = new StringBuffer();
        List<ProProductProperty> pppList = new ArrayList<ProProductProperty>();
        int skuFlag = 0;
        if (skuProv != null && skuProv.length > 0) {
            Map<String, String> map = getPropertyString(skuProv);
            for (Map.Entry<String, String> mp : map.entrySet()) {
                ProProductProperty ppp = new ProProductProperty();
                ppp.setProPropId(Integer.parseInt(mp.getKey()));
                ppp.setValueId(mp.getValue());
                //判断是否是sku属性
                if (skuProp != null && skuProp.length > 0) {
                    //双规格
                    if (skuProp.length > 1) {
                        if (mp.getKey().equals(skuProp[0].split(":")[0])) {
                            ppp.setIsSku(Constant.IS_SKU_YES);
                            if (mp.getKey().equals(skuImage)) {
                                ppp.setIsImage(Constant.IS_IMAGE_YES);
                                result.put("skuPropA", skuProp[0].split(":")[1] + "(异图)");
                            } else {
                                result.put("skuPropA", skuProp[0].split(":")[1]);
                            }
                        } else if (mp.getKey().equals(skuProp[1].split(":")[0])) {
                            ppp.setIsSku(Constant.IS_SKU_YES);
                            if (mp.getKey().equals(skuImage)) {
                                ppp.setIsImage(Constant.IS_IMAGE_YES);
                                result.put("skuPropB", skuProp[1].split(":")[1] + "(异图)");
                            } else {
                                result.put("skuPropB", skuProp[1].split(":")[1]);
                            }
                        }
                        skuFlag = 2;
                    } else { //单规格
                        if (mp.getKey().equals(skuProp[0].split(":")[0])) {
                            ppp.setIsSku(Constant.IS_SKU_YES);
                            if (mp.getKey().equals(skuImage)) {
                                ppp.setIsImage(Constant.IS_IMAGE_YES);
                                result.put("skuPropA", skuProp[0].split(":")[1] + "(异图)");
                            } else {
                                result.put("skuPropA", skuProp[0].split(":")[1]);
                            }
                        }
                        skuFlag = 1;
                    }
                }
                pppList.add(ppp);
            }
        }
        //类目属性计算
        if (props != null || props.length > 0) {
            for (String check : props) {
                String checks[] = check.split(":");
                if (Integer.parseInt(checks[2]) == 1) {
                    if (StringUtils.isNotEmpty(propertyString.toString())) {
                        propertyString.append("," + checks[0] + ":" + checks[1]);
                    } else {
                        propertyString.append(checks[0] + ":" + checks[1]);
                    }
                }
            }
            Map<String, String> propsmap = getPropertyString(props);
            for (Map.Entry<String, String> mp : propsmap.entrySet()) {
                ProProductProperty ppp1 = new ProProductProperty();
                ppp1.setProPropId(Integer.parseInt(mp.getKey()));
                ppp1.setValueId(mp.getValue());
                pppList.add(ppp1);
            }
        }
        //输入属性存储结构
        if (proPropId != null || proPropId.length > 0) {
            for (int i = 0; i < proPropId.length; i++) {
                if (!Strings.isNullOrEmpty(proPropV[i])) {
                    ProProductProperty ppp2 = new ProProductProperty();
                    ppp2.setProPropId(proPropId[i]);
                    ppp2.setValue(proPropV[i]);
                    pppList.add(ppp2);
                }
            }
        }
        result.put("propertyString", propertyString.toString());
        result.put("property", pppList);
        result.put("skuFlag",skuFlag);
        return result;
    }

    //获取SPU自身固有的属性
    private List<ProSelfProperty> getSpuSelfProp(HttpServletRequest request) {
        //获取商品自身属性和属性值
        String[] selfPropName = {};
        String[] selfPropValue = {};
        int[] selfPropId = {};
        try {
            selfPropName = ServletRequestUtils.getRequiredStringParameters(request, "selfPropName");
            selfPropValue = ServletRequestUtils.getRequiredStringParameters(request, "selfPropValue");
            selfPropId = ServletRequestUtils.getRequiredIntParameters(request, "selfPropId");
        } catch (Exception e) {
        }
        List<ProSelfProperty> psps = new ArrayList<ProSelfProperty>();
        if (selfPropName != null && selfPropName.length > 0) {
            for (int i = 0; i < selfPropName.length; i++) {
                ProSelfProperty psp = new ProSelfProperty();
                psp.setId(selfPropId[i]);
                psp.setSelfPropName(selfPropName[i]);
                psp.setSelfPropValue(selfPropValue[i]);
                psps.add(psp);
            }
        }
        return psps;
    }

    //获取sku自身固有的属性
    private List<SkuSelfProperty> getProSelfProp(HttpServletRequest request) {
        //获取商品自身属性和属性值
        String[] selfPropName = {};
        String[] selfPropValue = {};
        int[] selfPropId = {};
        try {
            selfPropName = ServletRequestUtils.getRequiredStringParameters(request, "selfPropName[]");
            selfPropValue = ServletRequestUtils.getRequiredStringParameters(request, "selfPropValue[]");
            selfPropId = ServletRequestUtils.getRequiredIntParameters(request, "selfPropId[]");
        } catch (Exception e) {
        }
        List<SkuSelfProperty> psps = new ArrayList<SkuSelfProperty>();
        if (selfPropName != null && selfPropName.length > 0) {
            for (int i = 0; i < selfPropName.length; i++) {
                SkuSelfProperty psp = new SkuSelfProperty();
                psp.setId(selfPropId[i]);
                psp.setSelfPropName(selfPropName[i]);
                psp.setSelfPropValue(selfPropValue[i]);
                psps.add(psp);
            }
        }
        return psps;
    }

    /**
     * 设置sku商品规格信息
     *
     * @param product
     * @param model
     */
    private void setSkuGuiGeInfo(ProProduct product, Model model) {
        List<ProProperty> propList = new ArrayList();
        List<ProProperty> skuPropList = new ArrayList();
        propList = ProPropertyClient.getSpuWithSkuPropsById(product.getSpuId());
        String skuPropertyStr = product.getSkuPropertyStr();
        if (StringUtils.isNotBlank(skuPropertyStr)) {
            String[] skuStrs = skuPropertyStr.split(";");
            //单规
            if (skuStrs.length == 1 && propList.size() == 1) {
                ProProperty prop = propList.get(0);
                ProProperty skuProp = new ProProperty();
                if (prop.getIsImage() == 1) {
                    skuProp.setName(prop.getName() + "(异图)");
                } else {
                    skuProp.setName(prop.getName());
                }
                if (StringUtils.isNotBlank(skuStrs[0]) && skuStrs[0].split(":").length > 1 &&
                        StringUtils.isNotBlank(skuStrs[0].split(":")[1]) && prop.getProProValue() != null) {
                    String proValueId = skuStrs[0].split(":")[1];
                    for (ProPropertyValue value : prop.getProProValue()) {
                        if (value.getId().toString().equals(proValueId)) {
                            skuProp.setName(value.getName());
                        }
                    }
                    skuPropList.add(skuProp);
                }
            }
            //双规
            if (skuStrs.length == 2 && propList.size() == 2) {
                for (String propIds : skuStrs) {
                    String[] ids = propIds.split(":");
                    if (ids.length > 1 && StringUtils.isNotBlank(ids[0]) && StringUtils.isNotBlank(ids[1])) {
                        String propId = ids[0];
                        String propValueId = ids[1];
                        ProProperty skuProp = new ProProperty();
                        for (ProProperty prop : propList) {
                            if (propId.equals(prop.getId().toString())) {   //属性id相等
                                if (prop.getIsImage() == 1) {
                                    skuProp.setName(prop.getName() + "(异图)");
                                } else {
                                    skuProp.setName(prop.getName());
                                }
                                for (ProPropertyValue value : prop.getProProValue()) {
                                    if (propValueId.equals(value.getId().toString())) {   //属性值id相等
                                        skuProp.setDescp(value.getName());
                                    }
                                }
                            }
                        }
                        skuPropList.add(skuProp);
                    }
                }
            }
            model.addAttribute("skuPropList", skuPropList);
        }
    }

    //获取商品详情
    private ProDetail getDetail(HttpServletRequest request) {

        //获取去商品详情锚点
        String descpMenu = ServletRequestUtils.getStringParameter(request, "descpMenu", null);
        //获取描述
        String descp = ServletRequestUtils.getStringParameter(request, "descp", null);
        //获取商品加工厂
        String factory = ServletRequestUtils.getStringParameter(request, "factory", null);
        //获取小编推荐
        String editorRec = ServletRequestUtils.getStringParameter(request, "editorRec", null);
        ProDetail detail = new ProDetail();
        //获取关键词
        String[] keyword = {};
        try {
            keyword = ServletRequestUtils.getRequiredStringParameters(request, "keyword");
        } catch (Exception e) {
        }
        if (keyword != null && keyword.length > 0) {
            StringBuffer keyStr = new StringBuffer();
            for (String str : keyword) {
                if (!Strings.isNullOrEmpty(str)) {
                    keyStr.append(str + ",");
                }
            }
            detail.setSearchKeyword(keyStr.toString());
        }
        //获取seo相关
        String seoTitle = ServletRequestUtils.getStringParameter(request, "seoTitle", null);
        String seoKeyword = ServletRequestUtils.getStringParameter(request, "seoKeyword", null);
        String seoDescp = ServletRequestUtils.getStringParameter(request, "seoDescp", null);

        detail.setDescpMenu(descpMenu);
        detail.setDescp(descp);
        detail.setFactory(factory);
        detail.setEditorRec(editorRec);
        detail.setSeoTitle(seoTitle);
        detail.setSeoKeyword(seoKeyword);
        detail.setSeoDescp(seoDescp);

        return detail;
    }

    //获取库存
    private ProStock getProStock(HttpServletRequest request) {
        //获取库存
        int stock = ServletRequestUtils.getIntParameter(request, "stock", 0);
        //获取安全库存
        int safeStock = ServletRequestUtils.getIntParameter(request, "safeStock", 0);
        ProStock proStock = new ProStock();
        proStock.setStock(stock);
        proStock.setSafeStock(safeStock);
        return proStock;
    }

    private Map<String, String> getPropertyString(String[] strs) {
        Map<String, String> map = new HashMap<String, String>();
        for (String str : strs) {
            String[] kv = str.split(":");
            String v = map.get(kv[0]);
            if (Strings.isNullOrEmpty(v)) {
                map.put(kv[0], kv[1]);
            } else {
                map.put(kv[0], v + "," + kv[1]);
            }
        }
        return map;
    }

    private String getSuccess(int state) {
        if (state == Constant.PRODUCT_EDITFAIL) {
            return "商品保存成功";
        } else if (state == Constant.PRODUCT_ON) {
            return "商品发布并且上架成功";
        } else {
            return "商品发布并且下架成功";
        }
    }

    /**
     * 获取第一级类目.第一次
     *
     * @param model
     */
    private void setCategory(Model model) {

        ProCategory category = new ProCategory();
        category.setParentId(0);
        List<ProCategory> categoryList = ProCategoryClient.getListByPId(category);
        model.addAttribute("categoryList", categoryList);

    }

    /**
     * 状态列表
     *
     * @param model
     */
    private void setState(Model model) {

        model.addAttribute("stateList", ProductStatus.values());

    }

    /**
     * 销售方式列表
     */
    private void setSellType(Model model) {

        Map<Integer, Object> bean = new LinkedHashMap<Integer, Object>();
        bean.put(0, "请选择");
        bean.put(1, "购销");
        bean.put(2, "代销");
        bean.put(3, "联营");
        model.addAttribute("sellTypeMap", bean);
    }

    /**
     * 店铺列表
     */
    private void setStoreList(Model model) {
        List<ProStore> list = ProStoreClient.getAllStore();
        model.addAttribute("store", list);
    }

    /**
     * 获取供应商列表
     *
     * @param model
     */
    private void setSupplier(Model model) {
        List<ProSupplier> suppliers = ProSupplierClient.getSupplierList(new ProSupplier());
        model.addAttribute("suppliers", suppliers);

    }

    /**
     * 获取品牌列表
     *
     * @param model
     */
    private void setBrandList(Model model) {
        //获取产地
        List<ProPlace> places = ProPlaceClient.getAllProPlaceList(new ProPlace());
        model.addAttribute("places", places);
    }

    /**
     * 获取产地列表
     *
     * @param model
     */
    private void setPlaceList(Model model) {
        //获取产地
        List<ProPlace> places = ProPlaceClient.getAllProPlaceList(new ProPlace());
        model.addAttribute("places", places);
    }

    private void setSpuSearchParam(HttpServletRequest request, Model model, SpuListDTO bean) {
        int state = ServletRequestUtils.getIntParameter(request, "state", 1);
        //类目
        int first_category_id = ServletRequestUtils.getIntParameter(request, "spu_first_id", 0);
        if (first_category_id != 0) {
            bean.setFirstCategoryId(first_category_id);
            ProCategory second_category = new ProCategory();
            second_category.setParentId(first_category_id);
            List<ProCategory> second_List = ProCategoryClient.getListByPId(second_category);
            model.addAttribute("second_List", second_List);
        }

        int second_category_id = ServletRequestUtils.getIntParameter(request, "spu_second_id", 0);
        if (second_category_id != 0) {
            bean.setSecondCategoryId(second_category_id);
            ProCategory third_category = new ProCategory();
            third_category.setParentId(second_category_id);
            List<ProCategory> third_List = ProCategoryClient.getListByPId(third_category);
            model.addAttribute("third_List", third_List);
        }
        int third_category_id = ServletRequestUtils.getIntParameter(request, "spu_third_id", 0);
        if (third_category_id != 0) {
            bean.setThirdCategoryId(third_category_id);
        }
        int spuId = ServletRequestUtils.getIntParameter(request, "spuId", 0);
        if (spuId != 0) {
            bean.setSpuId(spuId);
        }
        String proName = ServletRequestUtils.getStringParameter(request, "proName", null);
        if (StringUtils.isNotEmpty(proName)) {
            bean.setProName(proName);
        }
        int supplierId = ServletRequestUtils.getIntParameter(request, "supplierId", 0);
        if (supplierId != 0) {
            bean.setSupplierId(supplierId);
        }
        int storeId = ServletRequestUtils.getIntParameter(request, "storeId", 0);
        if (storeId != 0) {
            bean.setStoreId(storeId);
        }
        bean.setState(state);
        model.addAttribute("bean", bean);
    }

    /**
     * 没有上传图片的property value list
     *
     * @param spu
     * @param proPropAllList
     * @return
     */
    private List<ProPropertyValue> setUnImageValList(ProSpu spu, List<ProProperty> proPropAllList) {
        List<ProPropertyValue> unImageValueList = new ArrayList<ProPropertyValue>();
        List<ProImages> imageses = spu.getImageses();
        if (imageses == null || imageses.size() == 0) {
            for (ProProperty proProperty : proPropAllList) {
                if (proProperty.getIsSku() == 1 && proProperty.getIsImage() == 1) {
                    for (ProPropertyValue proPropertyValue : proProperty.getProProValue()) {
                        if (proPropertyValue.getFill() == 1) {
                            unImageValueList.add(proPropertyValue);
                        }
                    }
                }
            }
        } else {

            List<String> hasImageList = new ArrayList<String>();

            for (ProImages proImage : imageses) {
                hasImageList.add(proImage.getProPropId() + "," + proImage.getProProvId());
            }

            for (ProProperty proProperty : proPropAllList) {
                if (proProperty.getIsSku() == 1 && proProperty.getIsImage() == 1) {
                    for (ProPropertyValue proPropertyValue : proProperty.getProProValue()) {
                        if (proPropertyValue.getFill() == 1) {
                            if (!hasImageList.contains(proPropertyValue.getProPropId() + "," + proPropertyValue.getId())) {
                                unImageValueList.add(proPropertyValue);
                            }
                        }
                    }
                }
            }
        }
        return unImageValueList;
    }

    private ProSpu setSpu(Model model, int spuId) {

        ProSpu spu = ProSpuClient.getAllSpuById(spuId);
        ProCategory firstPc = ProCategoryClient.getProCategoryById(spu.getFirstCategoryId());
        ProCategory secondPc = ProCategoryClient.getProCategoryById(spu.getSecondCategoryId());
        ProCategory thirdPc = ProCategoryClient.getProCategoryById(spu.getThirdCategoryId());

        model.addAttribute("spu", spu);
        model.addAttribute("firstPc", firstPc.getCategoryName());
        model.addAttribute("secondPc", secondPc.getCategoryName());
        model.addAttribute("thirdPc", thirdPc.getCategoryName());


        List<ProProperty> proPropAllList = ProPropertyClient.getPropByProProp(thirdPc.getId(), spuId);

        List<ProPropertyValue> unImageValueList = setUnImageValList(spu, proPropAllList);


        setSupplier(model);
        setPlaceList(model);
        setBlandList(model);
        model.addAttribute("proPropAllList", proPropAllList);
        model.addAttribute("unImageValueList", unImageValueList);
        return spu;
    }

    private ProductNamesDto setProductNames(HttpServletRequest request) {
        ProductNamesDto dto = new ProductNamesDto();
        dto.setProName(ServletRequestUtils.getStringParameter(request, "proName", ""));
        dto.setShortName(ServletRequestUtils.getStringParameter(request, "shortName", ""));
        dto.setAdvName(ServletRequestUtils.getStringParameter(request, "advName", ""));
        return dto;
    }

    private ProductPublicParasDto setPublicParas(HttpServletRequest request) {
        ProductPublicParasDto dto = new ProductPublicParasDto();
        dto.setBrandId(ServletRequestUtils.getIntParameter(request, "brand.id", 0));
        dto.setIsCod(ServletRequestUtils.getIntParameter(request, "isCod", 0));
        dto.setPlaceId(ServletRequestUtils.getIntParameter(request, "placeId", 0));
        dto.setSupplierId(ServletRequestUtils.getIntParameter(request, "supplierId", 0));
        return dto;
    }

    private ProductDetailDTO setProDetail(HttpServletRequest request) {
        ProductDetailDTO dto = new ProductDetailDTO();
        dto.setDescp(ServletRequestUtils.getStringParameter(request, "descp", ""));
        dto.setDescpMenu(ServletRequestUtils.getStringParameter(request, "descpMenu", ""));
        return dto;
    }


    /**
     * 获取sku属性对应商品，sku属性值作为key，sku作为value
     *
     * @param products sku列表
     * @return
     */
    private Map<String, SimpleProduct> getPropvToSku(List<SimpleProduct> products) {
        Map<String, SimpleProduct> map = new HashMap<String, SimpleProduct>();
        for (SimpleProduct product : products) {
            String[] skuPropStr = product.getSkuPropertyStr().split(";");
            if (skuPropStr.length == 1) {
                map.put("0" + skuPropStr[0].split(":")[1], product);
            } else {
                map.put(skuPropStr[0].split(":")[1] + skuPropStr[1].split(":")[1], product);
            }
        }
        return map;
    }

    @RequestMapping("/viewAndOn")
    public String getViewAnGetOn(HttpServletRequest request,Model model){
        int proId = ServletRequestUtils.getIntParameter(request,"proId",0);
        int navFlag = ServletRequestUtils.getIntParameter(request,"navFlag",0);
        String timeStr = DateUtil.formatDate(new Date(),"HHmmss");
        try {
            ShardJedisTool.getInstance().set(JedisKey.pms$views,timeStr,proId);
        }catch (JedisClientException e){
            logger.error(e,"set商品详情页预览安全key失败，proId:"+proId,WebUtils.getIpAddr(request));
        }
        model.addAttribute("key",timeStr);
        model.addAttribute("proId",proId);
        model.addAttribute("navFlag",navFlag);
        return "/pms/product/view_and_on";
    }
}

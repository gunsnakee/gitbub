package com.meiliwan.emall.bkstage.web.controller.sp.ticket;

import com.meiliwan.emall.base.bean.BaseMsgTemplate;
import com.meiliwan.emall.base.client.BaseMsgTemplateClient;
import com.meiliwan.emall.bkstage.client.RoleServiceClient;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
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
import com.meiliwan.emall.sp2.bean.SpTicketBatch;
import com.meiliwan.emall.sp2.bean.SpTicketBatchProd;
import com.meiliwan.emall.sp2.bean.SpTicketImportDetail;
import com.meiliwan.emall.sp2.bean.SpTicketImportResult;
import com.meiliwan.emall.sp2.bean.view.SimpleActVO;
import com.meiliwan.emall.sp2.client.ActivityInterfaceClient;
import com.meiliwan.emall.sp2.client.SpTicketBatchClient;
import com.meiliwan.emall.sp2.constant.PrivilegeType;
import com.meiliwan.emall.sp2.constant.SpTicketBatchState;
import com.meiliwan.emall.sp2.dto.TicketImportVo;
import com.meiliwan.emall.sp2.dto.TicketParms;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * User: wuzixin
 * Date: 14-5-29
 * Time: 上午11:11
 */
@Controller("ticketBatchController")
@RequestMapping(value = "/ticket/batch")
public class TicketBatchController {

    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

    /**
     * 优惠券批次查询列表
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/list")
    public String list(HttpServletRequest request, Model model) {
        int selType = ServletRequestUtils.getIntParameter(request, "selType", 0);
        PageInfo pageInfo = StageHelper.getPageInfo(request, "create_time", "desc");
        if (selType == 0) {
            SpTicketBatch batch = getModelBatch(request);
            PagerControl<SpTicketBatch> pc = SpTicketBatchClient.getPagerByObj(batch, pageInfo, pageInfo.getOrderField(), pageInfo.getOrderDirection());
            model.addAttribute("batch", batch);
            model.addAttribute("pc", pc);
            model.addAttribute("currentTime", new Date());
            return "/sp/ticket/batch/list";
        } else {
            int batchId = ServletRequestUtils.getIntParameter(request, "batchId", 0);
            SpTicketBatch batch = SpTicketBatchClient.getBatchById(batchId);
            model.addAttribute("batch", batch);
            if (batch != null) {
                List<SpTicketBatchProd> prods = SpTicketBatchClient.getBatchProdsByBatchId(batchId);
                if (prods != null && prods.size() > 0) {
                    int[] ids = getProIds(prods);
                    List<SimpleProduct> products = ProProductClient.getSimpleListByProIds(ids);
                    model.addAttribute("prods", products);
                }
            }
            return "/sp/ticket/batch/view_prod_list";
        }

    }

    @RequestMapping(value = "/addProdList")
    public String addProdList(HttpServletRequest request, Model model) {
        int size = 0;
        int batchId = ServletRequestUtils.getIntParameter(request, "batchId", 0);
        SpTicketBatch batch = SpTicketBatchClient.getBatchById(batchId);
        SpTicketBatchProd prod = new SpTicketBatchProd();
        prod.setBatchId(batchId);
        PageInfo pageInfo = StageHelper.getPageInfo(request, "create_time", "desc");
        PagerControl<SpTicketBatchProd> pc = SpTicketBatchClient.getTicketProdPagerByObj(prod, pageInfo);
        if (pc.getEntityList() != null && pc.getEntityList().size() > 0) {
            size = pc.getPageInfo().getTotalCounts();
            int[] ids = getProIds(pc.getEntityList());
            List<SimpleProduct> products = ProProductClient.getSimpleListByProIds(ids);
            Map<Integer, SimpleProduct> map = new HashMap<Integer, SimpleProduct>();
            if (products != null && products.size() > 0) {
                for (SimpleProduct product : products) {
                    map.put(product.getProId(), product);
                }
            }
            for (SpTicketBatchProd sp : pc.getEntityList()) {
                SimpleProduct pd = map.get(sp.getProId());
                if (pd != null) {
                    sp.setProName(pd.getProName());
                    sp.setMlwPrice(pd.getMlwPrice());
                }
            }
        }
        model.addAttribute("batch", batch);
        model.addAttribute("pc", pc);
        model.addAttribute("size", size);
        return "/sp/ticket/batch/add_prod_list";
    }

    @RequestMapping("/add")
    public String add(HttpServletRequest request, HttpServletResponse response) {
        int handle = ServletRequestUtils.getIntParameter(request, "handle", 0);
        if (handle == 0) {
            return "/sp/ticket/batch/add";
        } else {
            String ticketName = ServletRequestUtils.getStringParameter(request, "ticketName", null);
            String ticketPrice = ServletRequestUtils.getStringParameter(request, "ticketPrice", "0");
            int initNum = ServletRequestUtils.getIntParameter(request, "initNum", 0);
            String startTime = ServletRequestUtils.getStringParameter(request, "startTime", null);
            String endTime = ServletRequestUtils.getStringParameter(request, "endTime", null);
            int ticketType = ServletRequestUtils.getIntParameter(request, "ticketType", 1);
            SpTicketBatch batch = new SpTicketBatch();
            if (ticketType == 1) {
                String descp = ServletRequestUtils.getStringParameter(request, "descp", null);
                batch.setState((short) 0);
                batch.setDescp(descp);
            } else {
                batch.setOnTime(new Date());
                batch.setState((short) 1);
                batch.setDescp("全站通用");
            }
            batch.setTicketName(ticketName);
            batch.setTicketPrice(new BigDecimal(ticketPrice));
            batch.setInitNum(initNum);
            batch.setStartTime(DateUtil.parseDateTime(startTime));
            batch.setEndTime(DateUtil.parseDateTime(endTime));
            batch.setTicketType((short) ticketType);
            batch.setAdminId(StageHelper.getLoginUser(request).getBksAdmin().getAdminId());
            try {
                int batchId = SpTicketBatchClient.insert(batch);
                if (batchId > 0) {
                    if (ticketType == 1) {
                        return StageHelper.writeDwzSignal("200", "创建优惠券成功", "5c7fb3cf498dc1e43f2d84226b653f00", StageHelper.DWZ_CLOSE_CURRENT, "/ticket/batch/addProdList?batchId=" + batchId, response);
                    } else {
                        return StageHelper.writeDwzSignal("200", "创建优惠券成功", "8ffc2abadd88452989065497d68b3461", StageHelper.DWZ_CLOSE_CURRENT, "/ticket/batch/list", response);
                    }
                } else {
                    return StageHelper.writeDwzSignal("200", "创建优惠券失败，请联系管理员", "a50ed4e78631f1456448e871ff566520", StageHelper.DWZ_FORWARD, "/ticket/batch/add", response);
                }
            } catch (Exception e) {
                logger.error(e, "创建优惠券批次失败,batch=" + batch.toString(), WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("200", "创建优惠券失败，请联系管理员", "a50ed4e78631f1456448e871ff566520", StageHelper.DWZ_FORWARD, "/ticket/batch/add", response);
            }
        }
    }

    /**
     * 创建优惠券并上线(主要针对品类券)
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/get-on")
    public String getOn(HttpServletRequest request, HttpServletResponse response) {
        int batchId = ServletRequestUtils.getIntParameter(request, "batchId", 0);
        List<SpTicketBatchProd> list = SpTicketBatchClient.getBatchProdsByBatchId(batchId);
        if (list != null && list.size() > 0) {
            try {
                SpTicketBatchClient.updateStateByOn(batchId);
                return StageHelper.writeDwzSignal("200", "创建优惠券上线成功", "8ffc2abadd88452989065497d68b3461", StageHelper.DWZ_CLOSE_CURRENT, "/ticket/batch/list", response);
            } catch (Exception e) {
                logger.error(e, "该品类券未选择商品，上线失败,batchId=" + batchId, WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "该品类券上线失败", "5c7fb3cf498dc1e43f2d84226b653f00", StageHelper.DWZ_FORWARD, "/ticket/batch/addProdList?batchId=" + batchId, response);
            }
        } else {
            return StageHelper.writeDwzSignal("300", "该品类券未选择商品，上线失败", "5c7fb3cf498dc1e43f2d84226b653f00", StageHelper.DWZ_FORWARD, "/ticket/batch/addProdList?batchId=" + batchId, response);
        }
    }

    @RequestMapping("/get-prod-list")
    public String getProdList(HttpServletRequest request, HttpServletResponse response, Model model) {
        int batchId = ServletRequestUtils.getIntParameter(request, "batchId", 0);
        int handle = ServletRequestUtils.getIntParameter(request, "handle", 0);
        if (handle > 0) {
            SpTicketBatch batch = SpTicketBatchClient.getBatchById(batchId);
            if (batch != null) {
                int[] ids = ServletRequestUtils.getIntParameters(request, "ids");
                if (ids != null && ids.length > 0) {
                    List<SpTicketBatchProd> prods = new ArrayList<SpTicketBatchProd>();
                    SpTicketBatchProd prod = null;
                    for (int id : ids) {
                        prod = new SpTicketBatchProd();
                        prod.setBatchId(batchId);
                        prod.setProId(id);
                        prod.setTicketName(batch.getTicketName());
                        prods.add(prod);
                    }
                    try {
                        SpTicketBatchClient.insertByBatchProd(prods);
                        return StageHelper.writeDwzSignal("200", "添加品类券商品，添加成功", "5c7fb3cf498dc1e43f2d84226b653f00", StageHelper.DWZ_FORWARD, "/ticket/batch/addProdList?batchId=" + batchId, response);
                    } catch (Exception e) {
                        logger.error(e, "添加品类券商品，添加失败,batch=" + batch.toString(), WebUtils.getIpAddr(request));
                        return StageHelper.writeDwzSignal("300", "添加品类券商品，添加失败", "174c3995a38084e785352ccdeb84e42c", StageHelper.DWZ_FORWARD, "/ticket/batch/get-prod-list", response);
                    }
                } else {
                    return StageHelper.writeDwzSignal("300", "添加品类券商品，未选择商品", "174c3995a38084e785352ccdeb84e42c", StageHelper.DWZ_FORWARD, "/ticket/batch/get-prod-list", response);
                }
            }
            return StageHelper.writeDwzSignal("300", "添加品类券商品，未选择商品", "174c3995a38084e785352ccdeb84e42c", StageHelper.DWZ_FORWARD, "/ticket/batch/get-prod-list", response);
        } else {
            PagerControl<ProductDTO> pc = null;
            String orderField = ServletRequestUtils.getStringParameter(request, "orderField", "");
            String orderDirection = ServletRequestUtils.getStringParameter(request, "orderDirection", "");
            ProductDTO bean = null;
            if (bean == null) {
                bean = new ProductDTO();
            }
            PageInfo pageInfo = WebParamterUtil.getPageInfo(request, 20);
            pageInfo.setOrderField(orderField);
            pageInfo.setOrderDirection(orderDirection);
            if ("".equals(orderField)) {
                pageInfo.setOrderField("create_time");
                pageInfo.setOrderDirection("desc");
            }
            setSkuSearchParam(request, model, bean);
            pc = ProProductClient.pageByObj(bean, pageInfo, pageInfo.getOrderField(), pageInfo.getOrderDirection());
            if (pc.getEntityList() != null && pc.getEntityList().size() > 0) {
                int[] ids = getSpProIds(pc.getEntityList());
                getProdsToSp(pc.getEntityList(), ids);
                getProdsToTicket(pc.getEntityList(), ids);
            }
            setSupplier(model);
            setCategory(model);
            model.addAttribute("batchId", batchId);
            model.addAttribute("pc", pc);
            return "/sp/ticket/batch/get_prod_list";
        }
    }

    @RequestMapping("/del-prod")
    public String delProd(HttpServletRequest request, HttpServletResponse response) {
        int proId = ServletRequestUtils.getIntParameter(request, "proId", 0);
        int batchId = ServletRequestUtils.getIntParameter(request, "batchId", 0);
        try {
            SpTicketBatchClient.deleteProdById(proId, batchId);
            return StageHelper.writeDwzSignal("200", "删除品类券商品，删除成功", "8ffc2abadd88452989065497d68b3461", StageHelper.DWZ_FORWARD, "/ticket/batch/list?batchId=" + batchId + "&selType=1", response);
        } catch (Exception e) {
            logger.error(e, "删除品类券商品，删除失败,batch=" + batchId + ",proId=" + proId, WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("300", "删除品类券商品，删除失败", "8ffc2abadd88452989065497d68b3461", StageHelper.DWZ_FORWARD, "/ticket/batch/list?batchId=" + batchId + "&selType=1", response);
        }
    }

    @RequestMapping("/index")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
        int dtType = ServletRequestUtils.getIntParameter(request, "dtType", 0);
        if (dtType == 0) {
            String url = request.getSession().getServletContext().getRealPath("/");
            try {
                int downType = ServletRequestUtils.getIntParameter(request, "downType", 0);
                String fileName = "EP-201406051752";
                if (downType == 1) {
                    fileName = "AC-201406051752";
                }
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
                String path = url + File.separator + "WEB-INF" + File.separator + "views" + File.separator + "sp" + File.separator + "ticket" + File.separator + "batch" + File.separator + fileName + ".xls";
                File file = new File(path);
                if (file.isFile() && file.exists()) {
                    long fileLength = file.length();
                    response.setContentType("xls");
                    response.setHeader("Content-disposition", "attachment; filename="
                            + new String((fileName + ".xls").getBytes("utf-8"), "ISO8859-1"));
                    response.setHeader("Content-Length", String.valueOf(fileLength));
                    bis = new BufferedInputStream(new FileInputStream(path));
                    bos = new BufferedOutputStream(response.getOutputStream());
                    byte[] buff = new byte[2048];
                    int bytesRead;
                    while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                        bos.write(buff, 0, bytesRead);
                    }
                    bis.close();
                    bos.close();
                }
            } catch (Exception e) {
                Map map = new HashMap();
                map.put("reason", " 文件下载出错！");
                logger.error(e, map, WebUtils.getIpAddr(request));
            }
        } else {
            //短信、邮件、站内信相关模板
            int handle = ServletRequestUtils.getIntParameter(request, "handle", 0);
            if (handle == 0) {
                List<BaseMsgTemplate> msgs = BaseMsgTemplateClient.getListByType(2);
                model.addAttribute("msgs", msgs);
                return "/sp/ticket/batch/view_msg_template";
            } else if (handle == 1) {
                int tmpId = ServletRequestUtils.getIntParameter(request, "tmpId", 0);
                BaseMsgTemplate template = BaseMsgTemplateClient.getTmpById(tmpId);
                model.addAttribute("template", template);
                return "/sp/ticket/batch/update_msg_template";
            } else {//修改消息模板
                int tmpId = ServletRequestUtils.getIntParameter(request, "tmpId", 0);
                String content = ServletRequestUtils.getStringParameter(request, "msgContent", null);
                BaseMsgTemplate template = new BaseMsgTemplate();
                template.setTmpId(tmpId);
                template.setContent(content);
                try {
                    BaseMsgTemplateClient.update(template);
                    return StageHelper.writeDwzSignal("200", "修改消息模板成功", "acb94d695f9f5fb5e6ad3b324826b898", StageHelper.DWZ_CLOSE_CURRENT, "/ticket/batch/index?dtType=1", response);
                } catch (Exception e) {
                    logger.error(e, "修改消息模板失败,template = " + template.toString(), WebUtils.getIpAddr(request));
                    return StageHelper.writeDwzSignal("300", "删除品类券商品，删除失败", "acb94d695f9f5fb5e6ad3b324826b898", StageHelper.DWZ_FORWARD, "/ticket/batch/index?dtType=1&handle=1", response);
                }
            }
        }
        return null;
    }

    /**
     * 禁用优惠券批次
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("/disable-batch")
    public String disableBatch(HttpServletRequest request, HttpServletResponse response, Model model) {
        int handle = ServletRequestUtils.getIntParameter(request, "handle", 0);
        if (handle > 0) {
            int batchId = ServletRequestUtils.getIntParameter(request, "batchId", 0);
            String disableDescp = ServletRequestUtils.getStringParameter(request, "disableDescp", "");
            SpTicketBatch batch = new SpTicketBatch();
            batch.setBatchId(batchId);
            batch.setDisable((short) SpTicketBatchState.DISABLED.getValue());
            batch.setDisableDescp(disableDescp);
            try {
                SpTicketBatchClient.updateBatch(batch);
                return StageHelper.writeDwzSignal("200", "停用优惠券批次，操作成功", "8ffc2abadd88452989065497d68b3461", StageHelper.DWZ_CLOSE_CURRENT, "/ticket/batch/list", response);
            } catch (Exception e) {
                logger.error(e, "停用优惠券批次，操作失败,batch=" + batch.toString(), WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("200", "停用优惠券批次，操作失败", "fe53c769a4850d2a4984aca46696fabc", StageHelper.DWZ_FORWARD, "/ticket/batch/disable-batch", response);
            }
        } else {
            int batchId = ServletRequestUtils.getIntParameter(request, "batchId", 0);
            model.addAttribute("batchId", batchId);
            return "/sp/ticket/batch/disable_batch";
        }
    }

    /**
     * 删除优惠券批次
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/del")
    public String del(HttpServletRequest request, HttpServletResponse response) {
        int batchId = ServletRequestUtils.getIntParameter(request, "batchId", 0);
        try {
            int count = SpTicketBatchClient.deleteBatch(batchId);
            if (count == 0) {
                return StageHelper.writeDwzSignal("300", "删除优惠券批次，该批次已上线不能删除", "4506965c35164bd0d297b7260e5293a9", StageHelper.DWZ_FORWARD, "/ticket/batch/del", response);
            } else {
                return StageHelper.writeDwzSignal("200", "删除优惠券批次，操作成功", "8ffc2abadd88452989065497d68b3461", StageHelper.DWZ_FORWARD, "/ticket/batch/list", response);
            }
        } catch (Exception e) {
            logger.error(e, "删除优惠券批次，操作失败,batchId=" + batchId, WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("200", "停用优惠券批次，操作失败", "4506965c35164bd0d297b7260e5293a9", StageHelper.DWZ_FORWARD, "/ticket/batch/del", response);
        }
    }

    /**
     * 关联活动
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("/update-to-sp")
    public String updateToSP(HttpServletRequest request, HttpServletResponse response, Model model) {
        int handle = ServletRequestUtils.getIntParameter(request, "handle", 0);
        if (handle > 0) {
            int batchId = ServletRequestUtils.getIntParameter(request, "batchId", 0);
            String actUrl = ServletRequestUtils.getStringParameter(request, "actUrl", "");
            SpTicketBatch batch = new SpTicketBatch();
            batch.setBatchId(batchId);
            batch.setActUrl(actUrl);
            try {
                int count = SpTicketBatchClient.updateActUrl(batch);
                return StageHelper.writeDwzSignal("200", "关联活动URL，操作成功", "8ffc2abadd88452989065497d68b3461", StageHelper.DWZ_CLOSE_CURRENT, "/ticket/batch/list", response);
            } catch (Exception e) {
                logger.error(e, "关联活动，操作失败,batch=" + batch.toString(), WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "关联活动URL，操作失败", "eb56c2458f5c377269781295f436dbdb", StageHelper.DWZ_FORWARD, "/ticket/batch/disable-batch", response);
            }
        } else {
            int batchId = ServletRequestUtils.getIntParameter(request, "batchId", 0);
            SpTicketBatch batch = SpTicketBatchClient.getBatchById(batchId);
            model.addAttribute("batchId", batchId);
            model.addAttribute("batch", batch);
            return "/sp/ticket/batch/updat_batch_sp";
        }
    }

    /**
     * 单个发送功能
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("/send-ticket")
    public String sendTicket(HttpServletRequest request, HttpServletResponse response, Model model) {
        int optType = ServletRequestUtils.getIntParameter(request, "optType", 0);
        int handle = ServletRequestUtils.getIntParameter(request, "handle", 0);
        int batchId = ServletRequestUtils.getIntParameter(request, "batchId", 0);
        SpTicketBatch batch = SpTicketBatchClient.getBatchById(batchId);
        if (optType == 0) { //单个发送功能
            if (handle > 0) {
                if (DateUtil.compare(batch.getEndTime(), new Date()) < 1) {
                    return StageHelper.writeDwzSignal("300", "单个发送优惠券，该批次已过期，操作失败", "d6b33f2eed97522a480586064d3360af", StageHelper.DWZ_FORWARD, "/ticket/batch/send-ticket", response);
                }
                //判断实际发送数量是否已经大于总数
                if (batch.getRealNum() >= batch.getInitNum()) {
                    return StageHelper.writeDwzSignal("300", "单个发送优惠券，实际发送数量超过总数，操作失败", "d6b33f2eed97522a480586064d3360af", StageHelper.DWZ_FORWARD, "/ticket/batch/send-ticket", response);
                }
                //获取信息
                String sendCt = ServletRequestUtils.getStringParameter(request, "sendCt", null);
                if (StringUtils.isNotBlank(sendCt)) {
                    //获取发送类型
                    int sendType = ServletRequestUtils.getIntParameter(request, "sendType", 0);
                    List<TicketParms> parmses = new ArrayList<TicketParms>();
                    TicketParms parms = new TicketParms();
                    parms.setAcountNum(sendCt);
                    parms.setType(sendType);
                    parmses.add(parms);

                    TicketImportVo vo = new TicketImportVo();
                    vo.setBatchId(batchId);
                    vo.setAdminId(StageHelper.getLoginUser(request).getBksAdmin().getAdminId());
                    vo.setAdminName(StageHelper.getLoginUser(request).getBksAdmin().getAdminName());
                    try {
                        boolean suc = SpTicketBatchClient.getTkSendToUser(parmses, vo);
                        if (suc) {
                            return StageHelper.writeDwzSignal("200", "单个发送优惠券，操作成功", "8ffc2abadd88452989065497d68b3461", StageHelper.DWZ_FORWARD, "/ticket/batch/list", response);
                        } else {
                            return StageHelper.writeDwzSignal("300", "单个发送优惠券，发送失败，请联系管理员", "d6b33f2eed97522a480586064d3360af", StageHelper.DWZ_FORWARD, "/ticket/batch/send-ticket", response);
                        }
                    } catch (Exception e) {
                        logger.error(e, "关联活动，操作失败,batch=" + batch.toString() + ",params=" + parmses.toString() + ",vo=" + vo.toString(), WebUtils.getIpAddr(request));
                        return StageHelper.writeDwzSignal("300", "单个发送优惠券，发送失败，请联系管理员", "d6b33f2eed97522a480586064d3360af", StageHelper.DWZ_FORWARD, "/ticket/batch/send-ticket", response);
                    }
                } else {
                    return StageHelper.writeDwzSignal("300", "单个发送优惠券，填写信息错误，操作失败", "d6b33f2eed97522a480586064d3360af", StageHelper.DWZ_FORWARD, "/ticket/batch/send-ticket", response);
                }
            } else {
                model.addAttribute("batch", batch);
                model.addAttribute("optType", optType);
                return "/sp/ticket/batch/simple_send";
            }
        } else {
            model.addAttribute("batch", batch);
            model.addAttribute("optType", optType);
            return "/sp/ticket/batch/batch_send";
        }
    }

    /**
     * 批量发送功能
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/batch-send")
    public String batchSend(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile file) {
        try {
            CommonsMultipartFile multipartFile = (CommonsMultipartFile) file;
            String fileName = multipartFile.getFileItem().getName().split("\\.")[0];
            List<SpTicketImportResult> results = SpTicketBatchClient.getListImportResultByFileName(fileName);
            if (results != null && results.size() > 0) {
                return StageHelper.writeDwzSignal("300", "该文件已经导入，请仔细检查，避免重复导入哦！", "dce689184b97c2915586793df57ca200", StageHelper.DWZ_FORWARD, "/ticket/batch/batch-send", response);
            } else {

                int sendType = ServletRequestUtils.getIntParameter(request, "sendType", 0);
                List<TicketParms> list = getImportTks(request, file, sendType);
                if (list != null && list.size() > 0) {
                    int batchId = ServletRequestUtils.getIntParameter(request, "batchId", 0);
                    SpTicketBatch batch = SpTicketBatchClient.getBatchById(batchId);
                    //判断实际发送数量是否已经大于总数
                    if (batch.getRealNum() >= batch.getInitNum()) {
                        return StageHelper.writeDwzSignal("300", "批量发送优惠券，实际发送数量超过总数，发送失败", "dce689184b97c2915586793df57ca200", StageHelper.DWZ_FORWARD, "/ticket/batch/batch-send", response);
                    }
                    TicketImportVo vo = new TicketImportVo();
                    vo.setBatchId(batchId);
                    vo.setAdminId(StageHelper.getLoginUser(request).getBksAdmin().getAdminId());
                    vo.setAdminName(StageHelper.getLoginUser(request).getBksAdmin().getAdminName());
                    vo.setFileName(fileName);
                    boolean suc = SpTicketBatchClient.getTkSendToUser(list, vo);
                    if (suc) {
                        return StageHelper.writeDwzSignal("200", "导入数据成功", "dce689184b97c2915586793df57ca200", StageHelper.DWZ_FORWARD, "/ticket/batch/batch-send?batchId=" + batchId, response);
                    } else {
                        return StageHelper.writeDwzSignal("300", "批量发送优惠券,导入失败，请联系管理员", "dce689184b97c2915586793df57ca200", StageHelper.DWZ_FORWARD, "/ticket/batch/batch-send", response);
                    }
                } else {
                    return StageHelper.writeDwzSignal("300", "批量发送优惠券,请检查导入数据，导入失败", "dce689184b97c2915586793df57ca200", StageHelper.DWZ_FORWARD, "/ticket/batch/batch-send", response);
                }
            }
        } catch (IceServiceRuntimeException e) {
            logger.error(e, "导入购买这信息失败", WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("300", "批量发送优惠券,导入失败，请联系管理员", "dce689184b97c2915586793df57ca200", StageHelper.DWZ_FORWARD, "/ticket/batch/batch-send", response);
        } catch (Exception e) {
            logger.error(e, "导入购买这信息失败", WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("300", "批量发送优惠券,导入失败，请联系管理员", "dce689184b97c2915586793df57ca200", StageHelper.DWZ_FORWARD, "/ticket/batch/batch-send", response);
        }
    }

    @RequestMapping("/detail")
    public String detail(HttpServletRequest request, Model model) {
        int handle = ServletRequestUtils.getIntParameter(request, "handle", 0);
        if (handle == 0) {
            int batchId = ServletRequestUtils.getIntParameter(request, "batchId", 0);
            int optType = ServletRequestUtils.getIntParameter(request, "optType", 1);
            SpTicketImportResult result = new SpTicketImportResult();
            result.setBatchId(batchId);
            result.setOptType((short) optType);
            PageInfo pageInfo = StageHelper.getPageInfo(request);
            SpTicketBatch batch = SpTicketBatchClient.getBatchById(batchId);
            PagerControl<SpTicketImportResult> pc = SpTicketBatchClient.getImportResultPageByObj(result, pageInfo);
            if (batch != null) {
                model.addAttribute("admin", RoleServiceClient.getBksAdmin(batch.getAdminId()));
            }
            if (optType == 0) { //查找单个发送的优惠券券号，导入结果表不冗余券号这个字段
                if (pc.getEntityList() != null && pc.getEntityList().size() > 0) {
                    SpTicketImportDetail detail = null;
                    for (SpTicketImportResult rt : pc.getEntityList()) {
                        detail = new SpTicketImportDetail();
                        detail.setFileName(rt.getFileName());
                        detail.setBatchId(rt.getBatchId());
                        List<SpTicketImportDetail> list = SpTicketBatchClient.getImportDetailListByObj(detail);
                        if (list != null && list.size() > 0) {
                            rt.setDetail(list.get(0));
                        }
                    }
                }
            }
            model.addAttribute("batch", batch);
            model.addAttribute("pc", pc);
            model.addAttribute("optType", optType);
            model.addAttribute("currentTime", new Date());
            return "/sp/ticket/batch/import_result_detail";
        } else {
            int importId = ServletRequestUtils.getIntParameter(request, "importId", 0);
            String fileName = ServletRequestUtils.getStringParameter(request, "fileName", null);
            int batchId = ServletRequestUtils.getIntParameter(request, "batchId", 0);
            SpTicketImportDetail detail = new SpTicketImportDetail();
            detail.setFileName(fileName);
            SpTicketImportResult result = SpTicketBatchClient.getImportResultById(importId);
            List<SpTicketImportDetail> list = SpTicketBatchClient.getImportDetailListByObj(detail);
            List<SpTicketImportDetail> sucList = new ArrayList<SpTicketImportDetail>();
            List<SpTicketImportDetail> errorList = new ArrayList<SpTicketImportDetail>();
            if (list != null && list.size() > 0) {
                for (SpTicketImportDetail dt : list) {
                    if (dt.getState() == 0) {
                        errorList.add(dt);
                    } else {
                        sucList.add(dt);
                    }
                }
            }
            SpTicketBatch batch = SpTicketBatchClient.getBatchById(batchId);
            model.addAttribute("batch", batch);
            model.addAttribute("result", result);
            model.addAttribute("sucList", sucList);
            model.addAttribute("errorList", errorList);
            return "/sp/ticket/batch/import_detail";
        }

    }


    private SpTicketBatch getModelBatch(HttpServletRequest request) {
        SpTicketBatch batch = new SpTicketBatch();
        int batchId = ServletRequestUtils.getIntParameter(request, "batchId", 0);
        String ticketName = ServletRequestUtils.getStringParameter(request, "ticketName", null);
        int state = ServletRequestUtils.getIntParameter(request, "state", 99);

        String createTimeMin = ServletRequestUtils.getStringParameter(request, "createTimeMin", null);
        String createTimeMax = ServletRequestUtils.getStringParameter(request, "createTimeMax", null);

        String endTimeMin = ServletRequestUtils.getStringParameter(request, "endTimeMin", null);
        String endTimeMax = ServletRequestUtils.getStringParameter(request, "endTimeMax", null);

        String startTimeMin = ServletRequestUtils.getStringParameter(request, "startTimeMin", null);
        String startTimeMax = ServletRequestUtils.getStringParameter(request, "startTimeMax", null);

        if (batchId > 0) {
            batch.setBatchId(batchId);
        }
        if (StringUtils.isNotEmpty(ticketName)) {
            batch.setTicketName(ticketName);
        }

        if (StringUtils.isNotEmpty(createTimeMin) && StringUtils.isNotEmpty(createTimeMax)) {
            batch.setCreateTimeMin(DateUtil.parseTimestamp(createTimeMin));
            batch.setCreateTimeMax(DateUtil.parseTimestamp(createTimeMax));
        }

        if (StringUtils.isNotEmpty(endTimeMin) && StringUtils.isNotEmpty(endTimeMax)) {
            batch.setEndTimeMin(DateUtil.parseTimestamp(endTimeMin));
            batch.setEndTimeMax(DateUtil.parseTimestamp(endTimeMax));
        }

        if (StringUtils.isNotEmpty(startTimeMin) && StringUtils.isNotEmpty(startTimeMax)) {
            batch.setStartTimeMin(DateUtil.parseTimestamp(startTimeMin));
            batch.setStartTimeMax(DateUtil.parseTimestamp(startTimeMax));
        }
        batch.setState((short) state);

        return batch;
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
        bean.setState(1);
        model.addAttribute("bean", bean);
    }

    private int[] getProIds(List<SpTicketBatchProd> list) {
        int size = list.size();
        int[] ids = new int[size];
        for (int i = 0; i < size; i++) {
            ids[i] = list.get(i).getProId();
        }
        return ids;
    }

    private int[] getSpProIds(List<ProductDTO> list) {
        int size = list.size();
        int[] ids = new int[size];
        for (int i = 0; i < size; i++) {
            ids[i] = list.get(i).getProId();
        }
        return ids;
    }


    private void setSupplier(Model model) {
        List<ProSupplier> suppliers = ProSupplierClient.getSupplierList(new ProSupplier());
        model.addAttribute("suppliers", suppliers);
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
     * 获取参加活动的商品
     *
     * @param list
     * @param ids
     */
    private void getProdsToSp(List<ProductDTO> list, int[] ids) {
        Map<Integer, Map<PrivilegeType, List<SimpleActVO>>> prosMap = ActivityInterfaceClient.getSimpleActivitiesByProIdsForSpTicket(ids);
        Map<Integer, String> sp = getSPNameList(prosMap);
        for (ProductDTO product : list) {
            product.setSeoKeyword(null);
            if (sp.get(product.getProId()) != null) {
                product.setSeoKeyword(sp.get(product.getProId()));
            }
        }
    }

    /**
     * 获取商品参加的优惠券
     *
     * @param list
     * @param ids
     */
    private void getProdsToTicket(List<ProductDTO> list, int[] ids) {
        List<SpTicketBatchProd> prods = SpTicketBatchClient.getTicketProdsByProIds(ids);
        Map<Integer, String> map = new HashMap<Integer, String>();
        if (prods != null && list.size() > 0) {
            for (SpTicketBatchProd prod : prods) {
                String ticketStr = map.get(prod.getProId());
                StringBuffer buffer = new StringBuffer();
                SpTicketBatch batch = SpTicketBatchClient.getBatchById(prod.getBatchId());
                if (batch != null) {
                    //排除未上线的优惠券
                    if (batch.getState() > 0) {
                        Date startTime = DateUtil.parseDateTime(DateUtil.getDateTimeStr(batch.getStartTime()));
                        Date endTime = DateUtil.parseDateTime(DateUtil.getDateTimeStr(batch.getEndTime()));
                        if (DateUtil.compare(new Date(), endTime) < 1) {
                            buffer.append("面值:" + batch.getTicketPrice());
                            if ((DateUtil.compare(new Date(), startTime) > -1) && (DateUtil.compare(new Date(), endTime) < 1)) {
                                buffer.append("," + "状态:进行中");
                            }
                            if (DateUtil.compare(new Date(), startTime) < 0) {
                                buffer.append("," + "状态:未开始");
                            }
                            if (batch.getDisable() == 1) {
                                buffer.append("(已停用)");
                            }
                            buffer.append(",批次:" + batch.getBatchId());
                        }
                    }
                }

                if (StringUtils.isNotEmpty(ticketStr)) {
                    if (StringUtils.isNotEmpty(buffer.toString())) {
                        ticketStr = ticketStr + "/" + buffer.toString();
                    }
                    map.put(prod.getProId(), ticketStr);
                } else {
                    ticketStr = buffer.toString();
                    map.put(prod.getProId(), ticketStr);
                }
            }
        }
        for (ProductDTO dto : list) {
            dto.setSearchKeyword(null);
            if (map.get(dto.getProId()) != null) {
                dto.setSearchKeyword(map.get(dto.getProId()));
            }
        }
    }

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
                                StringBuffer buffer = new StringBuffer();
                                buffer.append("活动类型:" + vo.getActType().getDesc());
                                Date startTime = DateUtil.parseDateTime(DateUtil.getDateTimeStr(vo.getStartTime()));
                                Date endTime = DateUtil.parseDateTime(DateUtil.getDateTimeStr(vo.getEndTime()));
                                if ((DateUtil.compare(new Date(), startTime) > -1) && (DateUtil.compare(new Date(), endTime) < 1)) {
                                    buffer.append("," + "状态:进行中");
                                }
                                if (DateUtil.compare(new Date(), startTime) < 0) {
                                    buffer.append("," + "状态:未开始");
                                }
                                if (DateUtil.compare(new Date(), endTime) > 0) {
                                    buffer.append("," + "状态:已结束");
                                }
                                buffer.append(",活动ID:" + vo.getActId());

                                if (StringUtils.isNotEmpty(actName)) {
                                    actName = actName + buffer.toString();
                                } else {
                                    actName = actName + "/" + buffer.toString();
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

    private List<TicketParms> getImportTks(HttpServletRequest request, MultipartFile file, int type) {
        InputStream is = null;
        List<TicketParms> list = new ArrayList<TicketParms>();
        try {
            is = file.getInputStream();
        } catch (IOException e) {
            logger.error(new BaseException("导入礼品卡购买者信息，获取数据流失败", e), "", WebUtils.getIpAddr(request));
            return list;
        }
        HSSFWorkbook hssfWorkbook = null;
        try {
            hssfWorkbook = new HSSFWorkbook(is);
        } catch (IOException e) {
            logger.error(new BaseException("导入礼品卡购买者信息,Excel转换出错", e), "", WebUtils.getIpAddr(request));
            return list;
        }
        TicketParms xlsDto = null;
        // 循环工作表Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // 循环行Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }
                xlsDto = new TicketParms();
                // 循环列Cell
                // 礼品卡卡号
                HSSFCell accountNum = hssfRow.getCell(0);
                if (accountNum == null || "".equals(getValue(accountNum))) {
                    continue;
                }
                xlsDto.setAcountNum(getValue(accountNum));
                xlsDto.setType(type);
                list.add(xlsDto);
            }
        }
        return list;
    }

    @SuppressWarnings("static-access")
    private static String getValue(HSSFCell hssfCell) {
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值
            return String.valueOf(String.format("%.0f", hssfCell.getNumericCellValue()));
        } else {
            // 返回字符串类型的值
            return String.valueOf(hssfCell.getStringCellValue().trim());
        }
    }
}

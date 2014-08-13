package com.meiliwan.emall.bkstage.web.controller.oms;
 

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meiliwan.emall.oms.bean.*;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.bkstage.web.controller.bkstage.view.LoginUser;
import com.meiliwan.emall.bkstage.web.jreport.factory.DeliverVoucherFactory;
import com.meiliwan.emall.bkstage.web.jreport.vo.BaseReportVO;
import com.meiliwan.emall.bkstage.web.jreport.vo.DeliverGoods;
import com.meiliwan.emall.bkstage.web.jreport.vo.DeliverGoodsItem;
import com.meiliwan.emall.bkstage.web.jreport.vo.RetDeliverGoods;
import com.meiliwan.emall.bkstage.web.jreport.vo.RetDeliverGoodsItem;
import com.meiliwan.emall.bkstage.web.jreport.vo.RetOrderDetail;
import com.meiliwan.emall.bkstage.web.jreport.vo.RetOrderGoodsItem;
import com.meiliwan.emall.bkstage.web.jreport.vo.RetOrderLog;
import com.meiliwan.emall.commons.bean.PayCode;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.NumberUtil;
import com.meiliwan.emall.commons.util.StringUtil;
import com.meiliwan.emall.mms.bean.UserPassport;
import com.meiliwan.emall.mms.client.UserPassportClient;
import com.meiliwan.emall.oms.client.OrdClient;
import com.meiliwan.emall.oms.client.OrdLogisticsClient;
import com.meiliwan.emall.oms.client.ReturnOrderClient;
import com.meiliwan.emall.oms.constant.BuyType;
import com.meiliwan.emall.oms.constant.OrdIRetStatus;
import com.meiliwan.emall.oms.constant.OrderType;
import com.meiliwan.emall.oms.constant.RetPayType;
import com.meiliwan.emall.oms.constant.RetType;
import com.meiliwan.emall.oms.constant.RetordCreateType;
import com.meiliwan.emall.oms.constant.TransportCompany;
import com.meiliwan.emall.oms.dto.OrdDetailDTO;
import com.meiliwan.emall.pms.bean.ProLocation;
import com.meiliwan.emall.pms.bean.SimpleProduct;
import com.meiliwan.emall.pms.client.ProLocationClient;
import com.meiliwan.emall.pms.client.ProProductClient;

/**
 * User: xiong.yu
 * Date: 13-6-6
 * Time: 下午4:56
 * 用户管理
 */
@Controller("omsPrinterController")
@RequestMapping("/oms/printer")
public class PrinterController {
    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());
    private final short COD_YES=1;
    private final short COD_NO=0;

    //打印类型，VOUCHER：配送票据，GOODS：发货清单，RET_STOCK_WAITCHECK：逆向-退换货-仓库等待确认单
    enum PRINT_TYPE{VOUCHER("配送票据"), GOODS("发货清单"), GOODS_USER("给用户的发货清单"), RET_STOCK_WAITCHECK("逆向-退换货-仓库等待确认单"), RET_DELIVER_GOODS("逆向发货单");
        private String desc;

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
		PRINT_TYPE(String desc){
			this.desc = desc;
		}
    }
    //票据的jasper文件目录
    enum JASPER{VOUCHER("jasper/DeliverVoucher.jasper"), GOODS("jasper/DeliverGoods.jasper"), GOODS_USER("jasper/DeliverGoodsForUser.jasper"), RET_STOCK_WAITCHECK("jasper/RetStockWaitCheck.jasper"), RET_DELIVER_GOODS("jasper/RetDeliverGoods.jasper");
		private String file;
		public String getFile() {
			return file;
		}
		public void setFile(String file) {
			this.file = file;
		}
		JASPER(String file){
			this.file = file;
		}
	}
    //jasper文件的一些默认数据：默认发货人信息
    private static Map PARAMS_CONSIGN = new HashMap();


    /**
     * 打印相关参数
     * @param request
     * @return
     */
    private Map getPrinterParameters(HttpServletRequest request){
    	String type = getPrinterType(request);
    	PRINT_TYPE printType = PRINT_TYPE.valueOf(type);
    	Map params = new HashMap();
    	params.put("type", type);
        String webRoot = request.getSession().getServletContext().getRealPath("/");

        PARAMS_CONSIGN.put("SUBREPORT_DIR", webRoot+"jasper/");
        params.put("jasperParams", PARAMS_CONSIGN);
        String orderId = ServletRequestUtils.getStringParameter(request, "order_id", "");

    	switch(printType){
			case VOUCHER:
				params.put("jasperFile", webRoot+JASPER.VOUCHER.getFile());
				params.put("jasperDataSource", DeliverVoucherFactory.getBeanCollection());
				break;
			case GOODS:
				params.put("jasperFile", webRoot+JASPER.GOODS.getFile());
				params.put("jasperDataSource", getListReportHasComment(orderId));
				break;
            case GOODS_USER:
                params.put("jasperFile", webRoot+JASPER.GOODS_USER.getFile());
                params.put("jasperDataSource", getListReportNotHasComment(orderId));
                break;
            case RET_DELIVER_GOODS:
                params.put("jasperFile", webRoot+JASPER.RET_DELIVER_GOODS.getFile());
                params.put("jasperDataSource", getRetListReport(orderId));
                break;
            case RET_STOCK_WAITCHECK:
                params.put("jasperFile", webRoot+JASPER.RET_STOCK_WAITCHECK.getFile());
                params.put("jasperDataSource", getRetOrderDetailReport(orderId));
                break;
			default:
                params.put("jasperFile", webRoot+JASPER.GOODS.getFile());
                params.put("jasperDataSource", getListReportHasComment(orderId));
				break;
		}
    	return params;
    }

    private List<BaseReportVO> getRetOrderDetailReport(String ordId){
        List<BaseReportVO> mainList = new ArrayList<BaseReportVO>();

        RetOrderDetail rod = new RetOrderDetail();
        //拿退换货订单
        Retord ret = ReturnOrderClient.getRetordById(ordId);
        if(ret == null) return null;
        //拿原正向订单详情
        OrdDetailDTO oldOrder = OrdClient.getDetail(ret.getOldOrderId());
        if(oldOrder == null) return null;

        rod.setApplyTime(ret.getRetTime());
        rod.setOrderId(ret.getRetordOrderId());

        rod.setCreateType(ret.getCreateType()==RetordCreateType.CREATE_TYPE_USER.getCode()?
                RetordCreateType.CREATE_TYPE_USER.getDesc():RetordCreateType.CREATE_TYPE_ADMIN.getDesc());
        rod.setOldOrderId(ret.getOldOrderId());
        rod.setPayType("");

        //支付方式
        //这里应该用PayName，而不是PayCode modify by yuxiong 2013.11.25
        if(oldOrder.getOrdPayList()!=null && oldOrder.getOrdPayList().get(0).getPayCode()!=null){
            PayCode payCode = null;
            try{
                payCode = PayCode.valueOf(oldOrder.getOrdPayList().get(0).getPayCode());
            }catch (Exception e){
                logger.error(e,"PrinterController.getRetOrderDetailReport"+oldOrder.getOrdPayList().get(0).toString(),null);
            }
            if(payCode!=null){
                rod.setPayType(payCode.getShow());
            }
        }

        rod.setIsInvoice(oldOrder.getOrd().getIsInvoice());
        rod.setRecvName(ret.getAdRecvName());
        rod.setRecvArea(ret.getAdProvince() + ret.getAdCity() + ret.getAdCounty());
        rod.setRecvAddress(ret.getAdDetailAddr());
        rod.setRecvMobile(ret.getAdMobile());
        rod.setRecvPhone(ret.getAdPhone());

        UserPassport user = UserPassportClient.getPassportByUid(ret.getUid());
        rod.setUserId(Integer.toString(user.getUid()));
        rod.setUserNickName(user.getNickName());
        rod.setUserEmail(user.getEmail());
        rod.setUserEmailVerified(user.getEmailActive()>0?true:false);
        rod.setUserMobile(user.getMobile());
        rod.setUserMobileVerified(user.getMobileActive()>0?true:false);

        rod.setServiceType(ret.getRetType().equals(RetType.RET.getCode())?"退货":"换货");
        rod.setRetType(rod.getServiceType());
        OrdIRetStatus ordIRetStatus = OrdIRetStatus.getDescByCode(ret.getRetStatus());
        if(ordIRetStatus != null){
            rod.setRetStatus(ordIRetStatus.getDesc());
        }
        if(ret.getRetTotalAmount() == null){
        		ret.setRetTotalAmount(0d);
        }
        BigDecimal retTotalAmount = BigDecimal.valueOf(ret.getRetTotalAmount());
        String card = "";
        if (ret.getRetRealCardAmount() != null && ret.getRetRealCardAmount() > 0) {
            card = "、礼品卡账户余额（"+ret.getRetRealCardAmount()+"元）";
            retTotalAmount = BigDecimal.valueOf(NumberUtil.subtract(ret.getRetTotalAmount(), (ret.getRetRealCardAmount().doubleValue()+ret.getRetRealMlwAmount().doubleValue()), 2));
        }
        //lzl需求调整 2014.07.04 6.1.1
//        if(ret.getRetPayType().equals(RetPayType.MLW_WALLET.getCode())){
//            rod.setRefundType("美丽钱包("+retTotalAmount+"元)" + card);
//        }else if(ret.getRetPayType().equals(RetPayType.THIRD_ALIPAY.getCode())){
//            rod.setRefundType("支付宝: ("+retTotalAmount+"元)" + ret.getServiceRetAlipay() + card);
//        }else if(ret.getRetPayType().equals(RetPayType.THIRD_BANK.getCode())){
//            StringBuffer refundTypeSB = new StringBuffer("银行名称: ("+retTotalAmount+"元)");
//            refundTypeSB.append(StringUtil.checkNull(ret.getServiceRetBank())?"":ret.getServiceRetBank()).append(", 银行账户名: ")
//            .append(StringUtil.checkNull(ret.getServiceRetCardName())?"":ret.getServiceRetCardName()).append(", 银行卡号: ")
//                    .append(StringUtil.checkNull(ret.getServiceRetCardNum())?"":ret.getServiceRetCardNum())
//                    .append(", 开户行: ").append(StringUtil.checkNull(ret.getServiceRetOpenBank())?"":ret.getServiceRetOpenBank());
//            rod.setRefundType(refundTypeSB.toString() + card);
//        }
        rod.setRetCardAmount(ret.getRetRealCardAmount());
        rod.setRetMlwAmount(ret.getRetPayType().equals(RetPayType.MLW_WALLET.getCode()) ? retTotalAmount.doubleValue() : ret.getRetRealMlwAmount());
        rod.setRetThirdPayAmount(
        		(ret.getRetPayType().equals(RetPayType.THIRD_ALIPAY.getCode()) || ret.getRetPayType().equals(RetPayType.THIRD_BANK.getCode())) ? retTotalAmount.doubleValue() : 0.00);
        rod.setServiceRetAlipay(ret.getServiceRetAlipay());
        rod.setServiceRetAlipayName(ret.getServiceRetAlipayName());
        rod.setServiceRetBank(ret.getServiceRetBank());
        rod.setServiceRetCardName(ret.getServiceRetCardName());
        rod.setServiceRetCardNum(ret.getServiceRetCardNum());
        rod.setServiceRetOpenBank(ret.getServiceRetOpenBank());
        
        rod.setRefundProAmount(ret.getRetPayAmount());
        rod.setRefundTransFee(ret.getRetPayFare());
        rod.setClaimTransFee(ret.getRetPayCompensate());
        rod.setRefundTotalAmount(ret.getRetTotalAmount());

        List<RetordLogs> logs = ret.getRetLogsList();
        if(logs!=null && !logs.isEmpty())  {
            List<RetOrderLog> logList = new ArrayList(logs.size());
            for(RetordLogs log : logs){
                RetOrderLog orderLog = new RetOrderLog();
                orderLog.setCreateTime(log.getCreateTime());
                orderLog.setOptContent(log.getOptContent());
                orderLog.setOptId(log.getOptId());
                orderLog.setOptMan(log.getOptMan());
                orderLog.setOptResult(log.getOptResult());
                logList.add(orderLog);

                if(log.getOptStatusCode().equals(OrdIRetStatus.RET_APPLY_PASSED.getCode()) || log.getOptStatusCode().equals(OrdIRetStatus.RET_APPLY_REJECTED.getCode())){
                    rod.setCheckDetail(log.getOptContent());
                    rod.setCheckResult(log.getOptResult());
                    rod.setCheckTime(log.getCreateTime());
                    rod.setCheckMan(log.getOptMan());
                }

                if(log.getOptStatusCode().equals(OrdIRetStatus.RET_SELLER_RECEIPTED.getCode()) || log.getOptStatusCode().equals(OrdIRetStatus.RET_SELLER_NO_RECEIPTED.getCode())){
                    rod.setStockCheckDetail(log.getOptContent());
                    rod.setStockCheckResult(log.getOptResult());
                    rod.setStockCheckTime(log.getCreateTime());
                    rod.setStockCheckMan(log.getOptMan());
                }
            }
            rod.setRetOrderLogs(logList);
        }

        List<Ordi> ordis = ret.getOrdiList();
        if(ordis!=null && !ordis.isEmpty()){
            List<RetOrderGoodsItem> goodsItems = new ArrayList();
            for(Ordi ordi : ordis){
                //退换货订单行，state==1 表示用户提交了的退换货 modify by yuxiong 2013-12-4
                if(ordi.getState() == 1){
                    RetOrderGoodsItem goodsItem = new RetOrderGoodsItem();
                    //对于逆向订单来说，ordi的proCateId属性用来存放原订单的商品行销售数
                    goodsItem.setBuyNum(ordi.getProCateId());
                    goodsItem.setProId(ordi.getProId());
                    goodsItem.setProName(ordi.getProName());
                    goodsItem.setProSn(ordi.getProBarCode());
                    goodsItem.setRetNum(ordi.getSaleNum());
                    goodsItem.setProSaleAmount(ordi.getUintPrice());
                    goodsItems.add(goodsItem);
                }
            }
            rod.setRetOrderGoodsItems(goodsItems);
        }
        mainList.add(rod);
        return mainList;

    }

    private List<BaseReportVO> getListReportNotHasComment(String ordId){
    		return getListReport(ordId,"");
    }

    private List<BaseReportVO> getListReportHasComment(String ordId){
    		StringBuilder comment = new StringBuilder();
		List<OrdRemark> remarkList = OrdClient.getRemarksByOrderId(ordId);
		if( remarkList!=null&&remarkList.size()>0){
			for (OrdRemark ordRemark : remarkList) {
				comment.append(ordRemark.getContent()).append("\r\n");
			}
		}
		return getListReport(ordId,comment.toString());
    }
    /**
     * 
     * @param ordId
     * @return
     */
    private List<BaseReportVO> getListReport(String ordId,String comment){
    		List<BaseReportVO> mainList = new ArrayList<BaseReportVO>();
         try {
			OrdDetailDTO dto = OrdClient.getDetail(ordId);
			
			 Ord ord = dto.getOrd();
			 //根据ID查找收货地址
             OrdAddr addr=OrdClient.getOrdAddrById(ord.getOrderId());
             if(addr==null){
                 addr=new OrdAddr();
             }
			 OrdLogistics logist = OrdLogisticsClient.getOrdLogisticsByOrdId(ord.getOrderId());
			 DeliverGoods goods = new DeliverGoods();

			 StringBuilder add = new StringBuilder();
			 add.append(addr.getProvince()).append(addr.getCity())
			 .append(addr.getCounty()).append(addr.getDetailAddr());
			 logger.debug("getListReport:"+add.toString());
			 goods.setAddress(add.toString());
			 goods.setOrderId(ord.getOrderId());
			 goods.setComment(comment);
			 goods.setCreateTime(DateUtil.getDateTimeStr(ord.getCreateTime()));
			 goods.setIsInvoice(ord.getIsInvoice());
			 goods.setMobile(addr.getMobile());
			 goods.setPhone(addr.getPhone());
			 goods.setRecvName(addr.getRecvName());
			 goods.setSaleAmout(ord.getOrderSaleAmount());

             if(logist!=null){
                 goods.setShipperNumber(logist.getLogisticsNumber());
                 if(logist.getLogisticsCompany()!=null){
                     if(logist.getLogisticsCompany().equals(TransportCompany.SF.name())||
                             logist.getLogisticsCompany().equals(TransportCompany.EMS.name())){
                         TransportCompany tc = TransportCompany.valueOf(logist.getLogisticsCompany());
                         goods.setTransCompany(tc.getDesc());
                     }else{
                         goods.setTransCompany(logist.getLogisticsCompany());
                     }
                 }
             }

			 goods.setTransportFee(ord.getTransportFee());
			 goods.setRealPayAmout(ord.getRealPayAmount());
			 OrdInvoice invoidce = dto.getInvoice();
			 //已做null判断
			 goods.setInvoiceHead(invoidce.getInvoiceHead());
			 if(ord.getOrderType().equals(OrderType.REAL_ORDER_COD.getCode())){
				 goods.setIsCOD(COD_YES);
			 }else{
				 goods.setIsCOD(COD_NO);
			 }

             //计算待开发票金额, 待开发票金额=实收金额-礼品卡支付订单金额
             List<OrdPay> ordPays = dto.getOrdPayList();
             if(ordPays!=null && ordPays.size()>0 && ord.getIsInvoice() == 1){
            	 int num = 0 ;
                 for(OrdPay ordPay : ordPays){
                     if(ordPay.getPayCode().equals(PayCode.MLW_C.name())){
                         goods.setInvoiceAmount(ord.getRealPayAmount().doubleValue() - ordPay.getPayAmount());
                         num ++ ;
                     }
                 }
                 //没有礼品卡支付也得计算发票金额
                 if(num <= 0){
                	 goods.setInvoiceAmount(ord.getRealPayAmount().doubleValue());
                 }
                 
             }else{
            	 goods.setInvoiceAmount(0.00);
             }
             

			 List<Ordi> oiList = dto.getOrdiList();
			 List<DeliverGoodsItem> list = new ArrayList<DeliverGoodsItem>();
			 int totalItem=0;

             List<String> barCodeList = new ArrayList<String>();
			 for (Ordi ordi : oiList) {

			 		DeliverGoodsItem item = new DeliverGoodsItem();
			 		SimpleProduct pro = ProProductClient.getProductById(ordi.getProId());
			 		item.setProId(ordi.getProId());
			 		if(ordi.getBuyType().equals(BuyType.PACK.getCode())){
			 			item.setProName("[套餐]"+ordi.getProName());
			 		}else{
			 			item.setProName(ordi.getProName());
			 		}

			 		item.setProSn(pro.getBarCode());
			 		item.setTotalAmount(ordi.getTotalAmount());
			 		item.setSaleNum(ordi.getSaleNum());
			 		item.setStoragepaces("");
			 		item.setUnitPrice(ordi.getUintPrice());
					list.add(item);
					totalItem+=ordi.getSaleNum();

                 if(!StringUtil.checkNull(ordi.getProBarCode())){
                    barCodeList.add(ordi.getProBarCode());
                 }
			}

            if(barCodeList!=null && !barCodeList.isEmpty()){
                List<ProLocation> locationList = ProLocationClient.getListByBarCode(barCodeList);
                //放储藏位置
                for (DeliverGoodsItem item : list) {
                    for (ProLocation pl : locationList) {
                        if (pl != null && pl.getBarCode() != null && pl.getBarCode().equals(item.getProSn())) {
                            item.setStoragepaces(pl.getLocationName());//这里将remark字段用于存储储位。
                        }
                    }
                }
            }

			 goods.setTotalItem(totalItem);
			 // P0904 订单详情页以及拣货单、发货单的商品根据仓库号进行排序 lzl
             Collections.sort(list, new Comparator<DeliverGoodsItem>(){
            	@Override  
            	public int compare(DeliverGoodsItem o1, DeliverGoodsItem o2){
            		if(o1 != null && o2 != null && !StringUtils.isBlank(o1.getStoragepaces()) && !StringUtils.isBlank(o2.getStoragepaces())){
            			return o1.getStoragepaces().compareTo(o2.getStoragepaces());
            		}else{
            			return Integer.MAX_VALUE;
            		}
            	}
             });
			 goods.setDeliverGoodsItems(list);
			 mainList.add(goods);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e, mainList, null);
		}

         return mainList;

    }

    private List<BaseReportVO> getRetListReport(String retId){
        Retord ret = ReturnOrderClient.getCleanRetordById(retId);
        List<Ordi> ordiList = OrdClient.getOrdiListByOrderId(retId);
        List<RetDeliverGoodsItem> listri = new ArrayList<RetDeliverGoodsItem>();
        int itemNum = 0;
        int totalRetNum = 0;
        double saleAmount = 0;
        if (ordiList != null && ordiList.size() > 0) {
            for (Ordi oi : ordiList) {
                if (oi.getState()==1) {
                    itemNum++;
                    saleAmount=NumberUtil.add(saleAmount, new Double(oi.getTotalAmount()));
                    RetDeliverGoodsItem ri = new RetDeliverGoodsItem();
                    ri.setProId(oi.getProId());
                    ri.setProName(oi.getProName());
                    SimpleProduct product = ProProductClient.getProductById(oi.getProId());
                    if (product != null) {
                        ri.setProSn(product.getBarCode());
                    }
                    ri.setRetNum(oi.getSaleNum());
                    ri.setStoragepaces("");
                    listri.add(ri);
                    totalRetNum +=  ri.getRetNum();
                }
            }
        }
        OrdLogistics logistics = OrdLogisticsClient.getOrdLogisticsByOrdId(retId);
        List<BaseReportVO> mainList = new ArrayList<BaseReportVO>();
        RetDeliverGoods goods = new RetDeliverGoods();
        goods.setOldOrderId(ret.getOldOrderId());
        goods.setAddress(ret.getAdDetailAddr());
        goods.setComment(ret.getApplyComments());
        goods.setCreateTime(ret.getCreateTime().toString());
        goods.setMobile(ret.getAdMobile());
        goods.setOrderId(retId);
        goods.setPhone(ret.getAdPhone());
        //goods.setRealPayAmout(ret.getRetTotalAmount());
        //解决getRetTotalAmount()空指针的问题，如果空指针，则显示金额0元 modify by yuxiong 2013.11.28
        if(ret.getRetTotalAmount() == null){
            goods.setRealPayAmout(0d);
        }else{
            goods.setRealPayAmout(ret.getRetTotalAmount());
        }

        goods.setRecvName(ret.getAdRecvName());
        goods.setSaleAmout(saleAmount);
        if (logistics != null) {
            goods.setTransCompany(logistics.getLogisticsCompany());
            goods.setShipperNumber(logistics.getLogisticsNumber());
        }
        goods.setTotalItem(itemNum);
        goods.setTotalRetNum(totalRetNum);
        goods.setDeliverGoodsItems(listri);
        mainList.add(goods);
        return mainList;
    }
    
    private String getPrinterType(HttpServletRequest request){
    	return StringUtil.checkNull(request.getParameter("type"))?
	    		PRINT_TYPE.GOODS.name():request.getParameter("type");
    }

    /**
     * 查询用户list
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/viewer")
    public void viewer(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException{
        Map printParams = getPrinterParameters(request);
        try {
            //添加（发货单|拣货单）打印记录
            addPrintLog(printParams,(LoginUser) request.getSession().getAttribute(StageHelper.loginUserSessionName),ServletRequestUtils.getStringParameter(request, "order_id", ""));
            JasperPrint jasperPrint = getJasperPrint(printParams.get("jasperFile").toString(),  (Map)printParams.get("jasperParams"),
                     (List<BaseReportVO>) printParams.get("jasperDataSource"));

            response.setContentType("application/pdf;charset=UTF-8");
            JRExporter export = new JRPdfExporter();
            export.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            export.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
            export.setParameter(JRPdfExporterParameter.CHARACTER_ENCODING, "UTF-8");
//            Map fontsMap = new HashMap();

//            fontsMap.put(new FontKey("宋体", true, false), new PdfFont("STSong-Light", "UniGB-UCS2-H", true, true, false));
//            export.setParameter(JRExporterParameter.FONT_MAP, fontsMap);
            export.exportReport();

        } catch (JRException e) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<head>");
            out.println("<title>MLW Reports</title>");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"../stylesheet.css\" title=\"Style\">");
            out.println("</head>");
            out.println("<body bgcolor=\"white\">");
            out.println("<span class=\"bnew\">Reports encountered this error :</span>");
            out.println("<pre>");
            e.printStackTrace(out);
            out.println("</pre>");
            out.println("</body>");
            out.println("</html>");
            e.printStackTrace();
        }
    }

    /**
     * 查询用户list
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/html")
    public void html(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException{
        Map printParams = getPrinterParameters(request);
        try {
            //添加（发货单|拣货单）打印记录
            addPrintLog(printParams,(LoginUser) request.getSession().getAttribute(StageHelper.loginUserSessionName),ServletRequestUtils.getStringParameter(request, "order_id", ""));
            JasperPrint jasperPrint = getJasperPrint(printParams.get("jasperFile").toString(),  (Map)printParams.get("jasperParams"),
                    (List<BaseReportVO>) printParams.get("jasperDataSource"));

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            String[] fontList = ge.getAvailableFontFamilyNames();
            for(int i=0;i<fontList.length;i++)
            {
                logger.debug("font = "+fontList[i]);
            }

            response.setContentType("text/html;charset=UTF-8");
            //获得输出流 ,这里不能这样response.getOutputStream()
            PrintWriter printWriter = response.getWriter();
            //创建JRHtmlExporter对象
            JRHtmlExporter htmlExporter = new JRHtmlExporter();
            request.getSession().setAttribute(
                    ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE,
                    jasperPrint);

            htmlExporter.setParameter( JRHtmlExporterParameter.IMAGES_URI, "/servlets/image?image=");
            //把jasperPrint到Session里面(net.sf.jasperreports.j2ee.jasper_print)
            //request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
            //设值jasperPrint
            htmlExporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
            //设置输出
            htmlExporter.setParameter(JRExporterParameter.OUTPUT_WRITER, printWriter);
            //设置图片生成的Servlet(生成图片就用这个ImageServlet,并且要在XML文件里面配置 image?image=这个是Servlet的url-pattern)
            //flush随机数用于重新获取图片（更新图片地址），否则条件改变后图片不会随之发生改变
            //htmlExporter.setParameter(JRHtmlExporterParameter.IMAGES_URI,"image?flush="+Math.random()+"&image=");
            htmlExporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
            //导出
            htmlExporter.exportReport();
            //关闭输出流
            printWriter.close();
            //关闭输入流
           // inputStream.close();





        } catch (JRException e) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<head>");
            out.println("<title>MLW Reports</title>");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"../stylesheet.css\" title=\"Style\">");
            out.println("</head>");
            out.println("<body bgcolor=\"white\">");
            out.println("<span class=\"bnew\">Reports encountered this error :</span>");
            out.println("<pre>");
            e.printStackTrace(out);
            out.println("</pre>");
            out.println("</body>");
            out.println("</html>");
            e.printStackTrace();
        }
    }

    /**
     * 添加（发货单|拣货单）打印记录
     */
    private void addPrintLog(Map printParams,LoginUser loginUser,String orderId) {
        String type = printParams.get("type").toString();

        if (loginUser != null && !StringUtil.checkNull(orderId) && (type.equals(PRINT_TYPE.GOODS.name()) || type.equals(PRINT_TYPE.GOODS_USER.name()))) {
            OrdPrintLogs ordPrintLogs = new OrdPrintLogs();
            ordPrintLogs.setOrderId(orderId);
            ordPrintLogs.setOptUid(loginUser.getBksAdmin().getAdminId());
            ordPrintLogs.setOptUname(loginUser.getBksAdmin().getAdminName());
            ordPrintLogs.setCreateTime(new Date());
            ordPrintLogs.setPrintType(type.equals(PRINT_TYPE.GOODS.name()) ? 0 : 1);

            OrdClient.insertPrintLog(ordPrintLogs);
        }
    }

    private JasperPrint getJasperPrint(String jasperFile, Map parameters, Collection coll) throws JRException{

		File reportFile = new File(jasperFile);
		if (!reportFile.exists())
			throw new JRRuntimeException("File WebappReport.jasper not found. The report design must be compiled first.");

		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(reportFile);

		return
				JasperFillManager.fillReport(
					jasperReport,
					parameters,
					new JRBeanCollectionDataSource(coll));

    }


}

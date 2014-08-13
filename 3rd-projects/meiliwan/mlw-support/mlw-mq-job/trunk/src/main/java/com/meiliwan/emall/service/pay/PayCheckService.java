package com.meiliwan.emall.service.pay;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import cmb.netpayment.Settle;

import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.bean.PayCode;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.exception.BaseRuntimeException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.oms.client.OrdClient;
import com.meiliwan.emall.pay.bean.PayHead;
import com.meiliwan.emall.pay.bean.PayItem;
import com.meiliwan.emall.pay.client.PayClient;
import com.meiliwan.emall.pay.constant.PayStatus;
import com.meiliwan.emall.pay.constant.PayType;
import com.meiliwan.emall.pay.dto.PayParam;
import com.meiliwan.emall.pay.dto.PaymentDTO;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import com.meiliwan.emall.dao.pay.PayItemDao;

@Service
public class PayCheckService extends DefaultBaseServiceImpl{
	@Autowired
	private PayItemDao payItemDao ;
	
	private final MLWLogger LOG = MLWLoggerFactory.getLogger(PayCheckService.class);
	private final String CONFIG_FILE = "pay-web/cmbpay.properties";
	private Settle cmbSettle = Settle() ;
	private String cmbPayCode = getCmbPayCode();
	
	private Settle Settle(){
		cmbSettle = new Settle() ;
		cmbSettle.SetOptions("netpay.cmbchina.com");
		return cmbSettle ;
	}
	
	//所有使用招行接口的银行 如果有增加则需要维护这里
	private String getCmbPayCode(){
		StringBuffer stb = new StringBuffer();
		stb.append("('").append(PayCode.CMB_PAY).append("'");
		stb.append(", '").append(PayCode.CMB_YWT).append("'");
		stb.append(", '").append(PayCode.CMB_ABC).append("'");
		stb.append(", '").append(PayCode.CMB_BOCB2C).append("'");
		stb.append(", '").append(PayCode.CMB_CCB).append("'");
		stb.append(", '").append(PayCode.CMB_CEBB).append("'");
		stb.append(", '").append(PayCode.CMB_CGBC).append("'");
		stb.append(", '").append(PayCode.CMB_CMBC).append("'");
		stb.append(", '").append(PayCode.CMB_ECITIC).append("'");
		stb.append(", '").append(PayCode.CMB_HXB).append("'");
		stb.append(", '").append(PayCode.CMB_ICBCB2C).append("'");
		stb.append(", '").append(PayCode.CMB_JTB).append("'");
		stb.append(", '").append(PayCode.CMB_PAB).append("'");
		stb.append(", '").append(PayCode.CMB_PSBC).append("'");
		stb.append(")");
		return stb.toString() ;
	}
	
	public void setPayItemDao(PayItemDao payItemDao){
		this.payItemDao = payItemDao ;
	}
	
	/**
	 * 取得最近发生的错误信息
	 * @param iNo        错误代码
	 * @return string    错误描述
	 */
	public String GetLastErrCMB(int iNo){
		String backStatus = cmbSettle.GetLastErr(iNo);
		LOG.info("招行接口-取得最近发生的错误信息", "错误代码:"+iNo+"错误描述:"+backStatus, null) ;
		return backStatus ;
	}
	
	/**
	 * 招行 查询单笔定单
	 * 定单状态：0－已结帐，1－已撤销，2－部分结帐，3－退款，4－未结帐，5-无效状态，6－未知状态
	 * @param dateStr        定单的交易日期
	 * @param ordNum         订单号
	 * @param strBuf         存放已结帐定单信息——交易日期\n处理日期\n定单状态\n定单金额\n【结帐金额\n】
	 * @return int 查询结果返回代码
	 */
	public int QuerySingleOrderCMB(String dateStr,String ordNum,StringBuffer strBuf){
		//默认错误代码为17，在招行定义中对应未知错误，用来承接我们内部参数传输错误跳出逻辑使用
		int backState = 17 ;
		if(StringUtils.isBlank(dateStr) || StringUtils.isBlank(ordNum)){
			LOG.info("招行接口-查询单笔定单 交易日期或订单号为空", "dateStr:"+dateStr+",ordNum:"+ordNum, null);
			return backState ;
		}
		
		backState = cmbSettle.QuerySingleOrder (dateStr,ordNum,strBuf);
		LOG.info("招行接口-查询单笔订单返回结果", "backState:"+GetLastErrCMB(backState), null);
		return backState ;
	}
	
	/**
	 * 招行 分页方式按交易日查询已结帐定单信息
	 * 定单状态：0－已结帐，1－已撤销，2－部分结帐，3－退款，4－未结帐，5-无效状态，6－未知状态
	 * @param startDate      查询的开始日期
	 * @param endDate        查询的结束日期
	 * @param count          本次查询的定单数目
	 * @param strBuf         存放已结帐定单信息
	 * 交易日期--------------"yyyymmdd"
	 * 处理日期--------------"yyyymmdd"
	 * 金额--------------------"*****.**"
	 * 定单号.----------------- 6或10个字符
	 * 定单状态---------------1 characters
	 * @return
	 */
	public int QuerySettledOrderByPageCMB(String startDate, String endDate, int count, StringBuffer strBuf){
		//默认错误代码为17，在招行定义中对应未知错误，用来承接我们内部参数传输错误跳出逻辑使用
		int backState = 17 ;
		if(StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)){
			LOG.info("招行接口-分页方式按交易日查询已结帐定单信息 交易日期为空", "startDate:"+startDate+",endDate:"+endDate, null);
			return backState ;
		}
		if(count<=0){
			LOG.info("招行接口-分页方式按交易日查询已结帐定单信息 本次查询数目为0", "count:"+count, null);
			return backState ;
		}
		
		backState = cmbSettle.QuerySettledOrderByPage(startDate, endDate, count, strBuf);
		LOG.info("招行接口-分页方式按交易日查询已结帐定单信息返回结果", "backState:"+GetLastErrCMB(backState), null);
		return backState ;
	}
	
	/**
	 * 招行自动对账
	 * 1.登录招行
	 * 2.执行业务
	 * 3.退出招行
	 */
	public void autoCMBPayDZ(JsonObject jsonObject){
		ConfigOnZk zkConfig = ConfigOnZk.getInstance();
		try{
			//登录招行
			String branchId = zkConfig.getValue(CONFIG_FILE, "BranchID");
			String coNo = zkConfig.getValue(CONFIG_FILE, "CoNo");
			String usNo = zkConfig.getValue(CONFIG_FILE, "UsNo");
			String host = zkConfig.getValue(CONFIG_FILE, "query_host");
			String adminPwd = zkConfig.getValue(CONFIG_FILE, "admin_pwd");
			//参数设置。该函数必须首先被调用  0表示成功，其他值表示错误
			int setOptionsState = cmbSettle.SetOptions(host); 
			if(setOptionsState == 0){
				LOG.info("招行接口-参数设置成功", "setOptionsState:"+GetLastErrCMB(setOptionsState)+"| host:"+host, null);
			}else{
				//失败则不用继续往后执行
				LOG.warn("招行接口-参数设置失败", "setOptionsState:"+GetLastErrCMB(setOptionsState)+"| host:"+host, null);
				throw new BaseException("招行接口-参数设置失败");
			}
			
			//登录。只有成功登录后，查询、结帐等方法才能被调用  0表示成功，其他值表示错误
			int loginCState = cmbSettle.LoginC(branchId, coNo+usNo, "131113");
			if(loginCState == 0 || loginCState == 2){
				LOG.info("招行接口-商户登录成功", "loginCState:"+GetLastErrCMB(loginCState)+"| branchId:"+branchId+",coNo:"+coNo+",adminPwd:"+adminPwd+"****", null);
			}else{
				//失败则不用继续往后执行
				LOG.warn("招行接口-商户登录失败", "loginCState:"+GetLastErrCMB(loginCState)+"| branchId:"+branchId+",coNo:"+coNo+",adminPwd:"+adminPwd+"****", null);
				throw new BaseException("招行接口-商户登录失败");
			}
			
			
			//单笔订单查询
//			String dateStr = "20140114" ;
//			String ordNum = "000006259929" ;
//			StringBuffer sb = new StringBuffer() ;
//			int backState = QuerySingleOrderCMB(dateStr, ordNum, sb) ;
//			LOG.info("招行接口-单笔订单查询 返回结果", "订单串："+sb.toString(), null);
			
			//分页方式按交易日查询已结帐定单信息
//			String startDate = "20131028" ;
//			String endDate = "20131028" ;
//			int count = Integer.MAX_VALUE ;
			String startDate = DateUtil.getCurrentDateStr().replaceAll("-", "");
			String endDate = DateUtil.getCurrentDateStr().replaceAll("-", "");
			int count = 500 ;
			StringBuffer strBuf = new StringBuffer() ;
			QuerySettledOrderByPageCMB(startDate, endDate, count, strBuf) ;
			LOG.info("招行接口-批量订单查询 返回结果", "订单串："+strBuf.toString(), null);
			if(strBuf != null && !StringUtils.isBlank(strBuf.toString())){
				int resultStatus = checkCMBOrderList(strBuf);
				LOG.info("核对处理结果：", resultStatus, null);
				System.out.println("============================核对处理结果:"+resultStatus+"===============================");
			}
			
			//退出招行
			int logoutState = cmbSettle.Logout();
			if(logoutState == 0){
				LOG.info("招行接口-商户登出成功", "logoutState:"+GetLastErrCMB(logoutState)+"| branchId:"+branchId+",coNo:"+coNo+",adminPwd:"+adminPwd, null);
			}else{
				LOG.warn("招行接口-商户登出失败", "logoutState:"+GetLastErrCMB(logoutState)+"| branchId:"+branchId+",coNo:"+coNo+",adminPwd:"+adminPwd, null);
			}
			
		}catch (BaseException e) {
			LOG.error(e, "info:招行对账异常！", null);
		}
	}
	
	/**
	 * 批量 解析招行返回的订单信息，并在商城内进行核对处理
	 * @param strBuf       招行返回的订单串
	 * @return checkState  方法返回处理状态 0(处理成功) 1(处理失败) 2(部分失败)
	 * 交易日期\n处理日期\n金额\n订单号\n订单状态\n
	 * 19980820\n19980920\n1200.00\n100201\n0\n
	 */
	public int checkCMBOrderList(StringBuffer strBuf){
		int checkState = 1 ;
		//如果招行没有对应的订单信息返回则不继续往下执行
		if(strBuf == null || strBuf.length()<=0){
			LOG.info("调用招行接口返回的订单信息为空", "strBuf:"+strBuf.toString(), null);
			return checkState ;
		}
		Map<String, PayItem> payItemMap = new HashMap<String, PayItem>() ;
		String[] orderStr = strBuf.toString().split("\n");
		if(orderStr!=null && orderStr.length>0){
			//两小时内
			Date nowDate = new Date();
			nowDate.setHours(nowDate.getHours()-2);
			String startDate = DateUtil.getDateTimeStr(nowDate) ;
			String endDate = DateUtil.getCurrentDateTimeStr() ;
//			String startDate = "2013-09-26 00:00:00" ;
//			String endDate = "2013-10-28 23:59:59" ;
			String payCode = cmbPayCode;
			try{
				List<PayItem> payItems = payItemDao.getPayItemByDateAndPayCode(startDate, endDate, payCode);
				if(payItems == null || payItems.size() <= 0){
					LOG.info("系统中没有需要和招行对账的订单", "action:getPayItemByDateAndPayCode,startDate:"+startDate+"endDate:"+endDate+"payCode:"+payCode, null);
					return checkState ;
				}
				
				if(payItems != null && payItems.size()>0){
					for(int i=0; i<payItems.size(); i++){
						PayItem payItem = payItems.get(i);
						if(payItem != null){
							payItemMap.put(payItem.getOrderId()+payItem.getPayCode(), payItem);
						}
					}
				}
				
			}catch (Exception e) {
				throw new BaseRuntimeException("payItemDao-getPayItemByDateAndPayCode-error", e);
			}
			
			//如果没有对应的订单则不需要继续往下执行
			if(payItemMap == null || payItemMap.size() <= 0){
				LOG.info("系统中没有需要和招行对账的订单", "action:getPayItemByDateAndPayCode,startDate:"+startDate+"endDate:"+endDate+"payCode:"+payCode, null);
				return checkState ;
			}
			
			for(int i=0; i<orderStr.length; i++){
				//因为招行订单为10位，系统中存为12位，需要补零
				String keyStr = "00" + orderStr[i+3] + PayCode.CMB_PAY.name() ;
				PayItem payItem = payItemMap.get(keyStr);
				if(payItem != null){
					//因为上面已经通过orderid去取订单，所以订单id不需要重复验证
					if(Math.round(Double.valueOf(orderStr[2])*100) != Math.round(payItem.getPayAmount().doubleValue()*100)){
						//订单金额不一致
						LOG.warn("", "orderId:"+payItem.getOrderId()+", cmbPay:"+Double.valueOf(orderStr[2])+", mlwPay:"+payItem.getPayAmount().doubleValue(), null);
						continue ;
					}
					if(!payItem.getPayStatus().equals(PayStatus.PAY_FINISHED.name())){
						PayHead payHead = PayClient.getPayHead(payItem.getOrderId(), true, false);
						//招行已支付成功，系统内订单状态未改变,则需要通知支付和订单修改状态
						System.out.println("=======================订单"+payItem.getOrderId()+"招行已经支付成功！");
						PaymentDTO payment = new PaymentDTO() ;
						PayParam payParam = new PayParam() ;
						PayParam[] payParams =new PayParam[payHead.getPayItems().size()] ;
						payParam.setAmount(payItem.getPayAmount().doubleValue());
						payParam.setPayCode(PayCode.valueOf(payItem.getPayCode()));
						payParam.setPayId(payItem.getPayId());
						payParam.setPayStatus(PayStatus.valueOf(payItem.getPayStatus()));
						payment.setOrderId(payHead.getOrderId());
						payment.setPayType(PayType.valueOf(payHead.getPayType()));
						payment.setSubject(payHead.getSubject());
						payment.setTargetUid(payHead.getTargetUid());
						payment.setUid(payHead.getUid());
						payment.setTotalAmount(payHead.getTotalAmount().doubleValue());
						int num = 0 ;
						for(PayItem payItems:payHead.getPayItems()){
							if(payItems == null){
								continue ;
							}
							PayParam paparam = new PayParam() ;
							paparam.setAmount(payItems.getPayAmount().doubleValue());
							paparam.setPayCode(PayCode.valueOf(payItems.getPayCode()));
							paparam.setPayId(payItem.getPayId());
							paparam.setPayStatus(PayStatus.valueOf(payItem.getPayStatus()));
							payParams[num] = null ;
							num ++ ;
						}
						payment.setPayParams(payParams);
						OrdClient.updateSubStockAndPayStatus(payment, payParam);
					}
				}
				
				i = i + 4 ;
			}
			
		}
		
		return checkState ;
	}
	
	/**
	 * 单条 解析招行返回的订单信息，并在商城内进行核对处理
	 * @param strBuf       招行返回的订单串
	 * @param orderId      查询的订单号
	 * @return checkState  方法返回处理状态 0(处理成功) 1(处理失败) 2(部分失败)
	 * 交易日期\n处理日期\n定单状态\n定单金额\n【结帐金额\n】
	 * 19980820\n19980920\n0\n1200.00\n
	 */
	public int checkCMBOrderSingle(StringBuffer strBuf, String orderId){
		int checkState = 1 ;
		//如果招行没有对应的订单信息返回则不继续往下执行
		if(strBuf == null || strBuf.length()<=0 || StringUtils.isBlank(orderId)){
			LOG.info("调用招行接口返回的订单信息为空", "strBuf:"+strBuf.toString()+",orderId:"+orderId, null);
			return checkState ;
		}
		String[] orderStr = strBuf.toString().split("\n");
		if(orderStr!=null && orderStr.length>0 && !StringUtils.isBlank(orderId)){
			PayItem payItem = null ;
			try{
				payItem = payItemDao.getPayItemByOrderIdAndPayCode(orderId, PayCode.CMB_PAY.name());
				
			}catch (Exception e) {
				throw new BaseRuntimeException("payItemDao-getPayItemByOrderIdAndPayCode-error", e);
			}
			//如果没有对应的订单则不需要继续往下执行
			if(payItem == null){
				LOG.info("系统中没有需要和招行对账的订单", "action:getPayItemByOrderIdAndPayCode,orderId:"+orderId+",payCode:"+PayCode.CMB_PAY.name(), null);
			}
			
			//对比金额
			if(Math.round(Double.valueOf(orderStr[3])*100) != Math.round(payItem.getPayAmount().doubleValue()*100)){
				
			}
			
			//对比支付状态
			if(!payItem.getPayStatus().equals(PayStatus.PAY_FINISHED.name())){
				
			}
			
		}
		
		return checkState ;
	}
	
	
//	public static void main(String[] args) {
//		autoCMBPayDZ() ;
//	}
}

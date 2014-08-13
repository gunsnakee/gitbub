


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.meiliwan.emall.account.client.GiftCardClient;
import com.meiliwan.emall.commons.util.DESedeCoder;
import com.meiliwan.emall.mms.client.UserPassportClient;
import com.meiliwan.emall.pms.bean.Card;
import com.meiliwan.emall.pms.client.CardClient;
import com.meiliwan.emall.pms.dto.CardParmsDTO;
import com.meiliwan.emall.pms.util.CardStatus;

public class AccountTest {
	
	public static void main(String[] args) throws Exception{
//		ShardJedisTool.getInstance().set(JedisKey.account$payerrorcount, uid, count);
		//ShardJedisTool.getInstance().del(JedisKey.account$payerrorcount, 1139);
		
		
		//double isMax = GiftCardClient.isCardAddMax(userId);
		Map<Integer, String> uidCardIdMap = new HashMap<Integer, String>();
		//uidCardIdMap.put(Integer.valueOf(5317), "LPNJS7140107152009");
		
//		uidCardIdMap.put(Integer.valueOf(1057), "EP76BD140515161335");
//		uidCardIdMap.put(Integer.valueOf(1265), "EPC93N140515161422"); //,EP9L8N140228105628, 
//		uidCardIdMap.put(Integer.valueOf(1323), "EP16HM140228171737");
//		uidCardIdMap.put(Integer.valueOf(4554), "EP7FNT140228171738");
//		uidCardIdMap.put(Integer.valueOf(1106), "EP7MQB140228105628");
		
		//uidCardIdMap.put(Integer.valueOf(6041), "EPC93N140515161422");
		for (Integer uidI : uidCardIdMap.keySet()) {
			int uid = uidI.intValue();
			String cardId = uidCardIdMap.get(uid);
			
			Card card = CardClient.getCardById(cardId);
			
			if (card == null) { 
				return;        
			}
			
			String userName = UserPassportClient.getPassportByUid(uid).getUserName();
			if (userName == null) {
				return;
			}
			System.out.println("user " + userName);
			
			String cardPwd = card.getPassword();
			System.out.println(cardPwd);
			
			String cardRPwd = decryCardPwd(cardPwd);
			
			cardActive(uid, cardRPwd, userName);
			
			//第二步：调用礼品卡接口进行卡账户充值
			double ownCoin = GiftCardClient.addMoneyWithIp(uid, card.getCardPrice().doubleValue(), "客服操作-礼品卡激活", cardId, "10.249.9.251");
	//
			if(ownCoin > 0){
				System.out.println("userid " + uid + " ownCoin " + ownCoin);
			}
			
			System.exit(0);
		}
		
		
	}
	
	/**
	 * 卡激活
	 * @param uid
	 * @param cardPwd
	 * @param userName
	 */
	public static void cardActive(int uid, String cardPwd, String userName) {
		CardParmsDTO cardParams = new CardParmsDTO();
		cardParams.setUserId(uid);
		cardParams.setPassword(cardPwd);
		cardParams.setUserName(userName);
		Map<String, Object> resultMap = CardClient.activeCard(cardParams);

		Object sttObj = resultMap.get("status");
		if(!CardStatus.SUCCESS.name().equals(sttObj.toString())){
			//
		} else {
			System.out.println("card " + cardPwd + " 激活成功"); 
		}
	}
	
	
	public static String decryCardPwd(String opwd){
		String KEY_STRING = "MLWCardPasswordkeyabcdf965j5yugd";
		 String data = null;
		try {
			data = new String(DESedeCoder.decrypt(Base64.decodeBase64(opwd.getBytes()),  KEY_STRING.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}  
	     System.out.println("解密后：\t" + data);
	     return data;
	}
	
	  
	
}

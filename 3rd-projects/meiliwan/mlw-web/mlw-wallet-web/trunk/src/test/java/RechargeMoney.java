import com.meiliwan.emall.account.bean.AccountWallet;
import com.meiliwan.emall.account.bean.WalletOptRecord;
import com.meiliwan.emall.account.bo.AccountBizLogOp;
import com.meiliwan.emall.account.client.AccountWalletClient;
import com.meiliwan.emall.base.client.IdGenClient;
import com.meiliwan.emall.commons.bean.PayCode;
import com.meiliwan.emall.mms.bean.UserPassport;
import com.meiliwan.emall.mms.client.UserPassportClient;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 14-1-7
 * Time: 下午1:03
 * To change this template use File | Settings | File Templates.
 */
public class RechargeMoney {

    public static void main(String [] args) throws IOException {
        List<String> notFoundUser = new ArrayList<String>();
        List<String> notPass = new ArrayList<String>();

        Map<String,String> accountMoney = new HashMap<String, String>();

        Map<String,String> map = new HashMap<String,String>();
        Map<String,String> mapName = new HashMap<String, String>();
        Map<String,String> mobileMap = new HashMap<String, String>();
        List<String> list = FileUtils.readLines(new File("/data/recharge.txt"));
        int index = 1;
        if(list != null && list.size() > 0){
            for(String line:list){
                String [] filds = line.split(",");
                map.put(filds[6],filds[2]);
                mapName.put(filds[6],filds[1]);
                mobileMap.put(filds[6], filds[3]);

                System.out.println(index++ + "," +line);
            }
        }


        Set<String> keySet = map.keySet();
        for(String key:keySet){
            //step 1 查找用户id
            UserPassport userPassport = UserPassportClient.getPassportByUserName(key);

            //充值
            if(userPassport == null){
                notFoundUser.add(key);
                continue;
            }

            //校验
            if(!mapName.get(key).equals(userPassport.getEmail()) && !mobileMap.get(key).equals(userPassport.getMobile())){
                notPass.add(key);
                continue;
            }


            //插入记录
            String rechargeId= IdGenClient.getPayId();
            WalletOptRecord walletOptRecord=new WalletOptRecord();
            walletOptRecord.setOutNum(rechargeId);
            walletOptRecord.setInnerNum(rechargeId);
            walletOptRecord.setMoney(new BigDecimal(Double.valueOf(map.get(key))));
            walletOptRecord.setUid(userPassport.getUid());
            walletOptRecord.setOptType(AccountBizLogOp.ADD_MONEY.toString());
            walletOptRecord.setOptTime(new Date());
            AccountWalletClient.addOptRecord(walletOptRecord);

            //查出旧值
            AccountWallet accountWallet = AccountWalletClient.getAccountWalletByUid(userPassport.getUid());
            accountMoney.put(key,String.valueOf(accountWallet.getMlwCoin().doubleValue()));




            AccountWalletClient.addMoney(userPassport.getUid(),Double.valueOf(map.get(key)),rechargeId, PayCode.MLW_W,rechargeId);
        }

        System.out.println("未找到的用户个数为:"+notFoundUser.size());
        for(String uId:notFoundUser){
            System.out.println(uId);
        }

        System.out.println("未通过校验的用户个数为:"+notPass.size());
        for(String uId:notPass){
            System.out.println(uId);
        }

        //查询下插入的数值
        int i = 1;
        for(String key:keySet){
            UserPassport userPassport = UserPassportClient.getPassportByUserName(key);
            AccountWallet accountWallet = AccountWalletClient.getAccountWalletByUid(userPassport.getUid());
            System.out.println("NO:"+i+" userName:"+key+" money: "+accountWallet.getMlwCoin().doubleValue()+ " oldMoney: "+accountMoney.get(key));
            i++;
        }

    }
}

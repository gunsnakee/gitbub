package com.meiliwan.emall.pms.service.helper;

import com.meiliwan.emall.pms.bean.ProSpu;
import com.meiliwan.emall.pms.bean.SimpleProduct;
import com.meiliwan.emall.pms.client.ProSpuClient;
import com.meiliwan.emall.pms.constant.Constant;
import com.meiliwan.emall.stock.client.ProStockClient;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

/**
 * User: wuzixin
 * Date: 14-3-6
 * Time: 下午1:16
 */
public class ProductValidate {
    //校验价格
    public static boolean getPriceValid(SimpleProduct product){
        if (product.getMlwPrice().compareTo(new BigDecimal(0)) <= 0){
            return true;
        }
        if (product.getMarketPrice().compareTo(new BigDecimal(0)) <= 0){
            return true;
        }
        return false;
    }

    //验证主图
    public static boolean getImgValid(SimpleProduct product){
        if (StringUtils.isEmpty(product.getDefaultImageUri())){
            return true;
        }else {
            return false;
        }
    }

    //验证库存
    public static boolean getStockVaild(int proId){
        int stock = ProStockClient.getSellStock(proId);
        if (stock<=0){
            return true;
        }
        return false;
    }
}

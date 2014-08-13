package com.meiliwan.emall.oms.util;

import com.meiliwan.emall.commons.util.NumberUtil;
import com.meiliwan.emall.oms.dto.OrdDTO;
import com.meiliwan.emall.oms.dto.OrdiDTO;

import java.util.List;

/**
 * Created by Sean on 13-11-1.
 */
public class OrdUtil {

    /**
     * 订单行 运费，以及 订单价格 的计算
     *
     * @param ordDTO
     */
    public static void avgProTransExp(OrdDTO ordDTO) {
        List<OrdiDTO> list = ordDTO.getOrdiList();
        if (list != null && list.size() > 0) {
            OrdiDTO lastObj = list.get(list.size() - 1);
            double useTransExp = 0d;
            for (OrdiDTO eachObj : list) {
                //订单行总价
                double curLineTotalPrice = NumberUtil.multiply(eachObj.getUintPrice(), eachObj.getSaleNum());
                //判断是否最后一个
                if (eachObj == lastObj) {
                    //订单运费总额 - 占有的运费金额 得到最后一个 订单行 运费金额
                    eachObj.setAvgProTransExp(NumberUtil.subtract(ordDTO.getTransportFee(), useTransExp, 2));
                } else {
                    //订单行总价 除以  订单商品总额  的比例  然后 乘以  总运费  得到当前 订单行运费
                	//因为除数或被除数为零的情况下算法会报错，所以业务上做了判断 lzl 0627
                    double transExp = (curLineTotalPrice ==0 || ordDTO.getOrderSaleAmount() == 0) ? 0.00 : NumberUtil.multiply(NumberUtil.divide(curLineTotalPrice, ordDTO.getOrderSaleAmount(), 8), ordDTO.getTransportFee(), 2);
                    eachObj.setAvgProTransExp(transExp);
                    useTransExp = NumberUtil.add(useTransExp, transExp, 2);
                }
                //商品价格 乘以 商品数量 +运费费用
                eachObj.setProPriceAndTransExp(NumberUtil.add(curLineTotalPrice, eachObj.getAvgProTransExp(), 2));
            }
        }
    }
}

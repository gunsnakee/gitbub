package com.meiliwan.emall.mms.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.mms.bean.UserCart;
import com.meiliwan.emall.mms.client.UserCartClient;
import com.meiliwan.emall.mms.dao.UserCartDao;
import com.meiliwan.emall.service.BaseService;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * Created by Sean on 13-6-14.
 */
@Service
public class UserCartService extends DefaultBaseServiceImpl implements BaseService {
    @Autowired
    private UserCartDao userCartDao;

    /**
     * 包括购物车操作的几种场景  添加 登陆合并 删除..<br/>
     * 以及是否需要返回 操作影响后的结果
     *
     * @param resultObj
     * @param operator
     * @param productIds
     * @param countArgs
     * @param userId
     */
    public void updateUserCart(JsonObject resultObj, UserCartClient.Operator operator, List<String> productIds, List<String> countArgs, List<Long> addTime, int userId, boolean isReturnResult) {
        UserCart userCart = new UserCart();
        userCart.setUserId(userId);
        //数组验证
        if (productIds.size() > 0) {
            for (int i = 0; productIds.size() > i; i++) {
                userCart.setCartId(productIds.get(i));
                int curCount = Integer.parseInt(countArgs.get(i));

                //如果操作数量为负数 不合法 忽略此操作
                if (curCount < 1) continue;
                UserCart dbCartItem = userCartDao.getEntityByObj(userCart);

                //删除操作
                if (UserCartClient.Operator.Del == operator && dbCartItem != null) {
                    if (curCount - dbCartItem.getCartCount() >= 0) {
                        userCartDao.deleteByEntity(userCart);
                    } else {
                        dbCartItem.setCartCount(dbCartItem.getCartCount() - curCount);
                        userCartDao.update(dbCartItem);
                    }
                }

                //删除操作
                if (UserCartClient.Operator.Set == operator) {
                    //删除操作
                    if (dbCartItem == null) {
                        userCart.setCartCount(curCount);
                        userCart.setCreateTime(new Date());
                        userCartDao.insert(userCart);
                    } else if (dbCartItem.getCartCount() == 0) {
                        userCartDao.deleteByEntity(userCart);
                    } else {
                        dbCartItem.setCartCount(curCount);
                        userCartDao.update(dbCartItem);
                    }
                }


                //更新 以及合并 做相同操作
                if (UserCartClient.Operator.Add == operator) {
                    //更新操作
                    if (dbCartItem != null) {
                        dbCartItem.setCartCount(dbCartItem.getCartCount() + curCount);
                        //最大999的数量
                        dbCartItem.setCartCount(Math.min(dbCartItem.getCartCount(), 999));
                        userCartDao.update(dbCartItem);
                    } else {//添加操作
                        userCart.setCartCount(curCount);
                        userCart.setCreateTime(new Date());
                        userCartDao.insert(userCart);
                    }
                    userCartClean(userId);
                }


                //更新 以及合并 做相同操作
                if (UserCartClient.Operator.Merge == operator) {
                    userCart.setCreateTime(new Date(addTime.get(i)));
                    //更新操作
                    if (dbCartItem != null) {
                        dbCartItem.setCartCount(Math.max(dbCartItem.getCartCount(), curCount));
                        //最大999的数量
                        dbCartItem.setCartCount(Math.min(dbCartItem.getCartCount(), 999));
                        dbCartItem.setCreateTime(new Date(addTime.get(i)));
                        userCartDao.update(dbCartItem);
                    } else {//添加操作
                        userCart.setCartCount(curCount);
                        userCartDao.insert(userCart);
                    }
                    userCartClean(userId);
                }


            }

        }
        if (isReturnResult || UserCartClient.Operator.Get == operator)
            userCart.setCartId(null);
        addToResult(userCartDao.getListByObj(userCart), resultObj);
    }


    public void userCartClean(int uid) {
        UserCart userCart = new UserCart();
        userCart.setUserId(uid);
        List<UserCart> list = userCartDao.getListByObj(userCart);
        for (int i = 50; i < list.size() && list.size() > 50; i++) {
            UserCart uc = list.get(i);
            userCartDao.deleteByEntity(uc);
        }
    }

    public static void main(String[] args) {
        for (int i = 50; i < 80 && 80 > 50; i++) {
            System.out.println(i);
        }
    }
}
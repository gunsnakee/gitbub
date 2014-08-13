package com.meiliwan.emall.bkstage.web.controller.account.template;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-11-27
 * Time: 下午5:06
 * To change this template use File | Settings | File Templates.
 */
public class LimitException extends Exception {
    public LimitException(String msg){
        super(msg);
    }
}

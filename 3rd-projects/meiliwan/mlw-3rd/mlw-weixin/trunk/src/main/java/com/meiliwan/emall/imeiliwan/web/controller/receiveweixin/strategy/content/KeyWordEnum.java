package com.meiliwan.emall.imeiliwan.web.controller.receiveweixin.strategy.content;

/**
 * 微信关键字回复枚举类
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-6-18
 * Time: 下午4:06
 * To change this template use File | Settings | File Templates.
 */
public enum KeyWordEnum {

    kw100("100","如何绑定美丽湾微信公众帐号？","【100】如何绑定美丽湾微信公众帐号？\n" +
            "-------------------------\n" +
            "您好，绑定美丽湾微信公众帐号将会为您带来全新的高效的服务体验，下面小易为您介绍一下绑定的具体步骤：\n" +
            "1、在美丽湾微信公众帐号下进入美丽湾商城，注册/登录成功后即为绑定成功\n" +
            "\n" +
            "就这么简单，快快绑定吧！绑定后您将可以享受微信订单进度、售后问题、人工服务等快捷高效的服务体验。"),

    kw101("101","订单管理","【101】 订单管理\n" +
            "-------------------------\n" +
            "您成功提交订单后，可以到“我的订单”查询您的订单，根据订单状态可以了解订单的处理进度：\n" +
            "1、等待付款：表示您的订单还未付款。 \n" +
            "2、等待发货：表示您的订单还未经过工作人员处理，如长时间出现这样的情况，请与客服联系。 \n" +
            "3、等待确认收货：表示工作人员已发货，请在收到货品后点击“确认收货”。自签收之日起7天内，系统自动确认收货，如您有任何问题请与客服联系。 \n" +
            "4、交易成功：表示美丽湾已送货完成，且顾客已收到订单货物。 \n" +
            "5、已取消：表示该订单已取消。"),
    kw102("102","运费标准","【102】运费标准\n" +
            "-------------------------\n" +
            "1、全场满48元包邮（港澳台地区除外）；\n" +
            "2、不包邮部分收费标准；\n" +
            "1) 单笔订单少于48元收取10元/笔运费（港澳台地区除外）；\n" +
            "2) 港澳台地区不参与包邮活动，每笔订单130元起；\n" +
            "3、2014年3月1日起，美丽湾默认快递为顺丰或EMS快递，若您的寄送地址为县级以下地区快递可能无法送达，请您及时联系客服修改快递方式。\n" +
            "注：运费的收取标准随着美丽湾业务的发展可能进行相应调整。"),
    kw103("103","配送时间","【103】配送时间\n" +
            "-------------------------\n" +
            "1. 配送时间：\n" +
            "在线支付订单，以成功支付订单时间为标准，当天18:00前的订单当天发货，18:00后的次日发货。\n" +
            "货到付款订单，以成功生成订单时间为标准，当天18:00前的订单当天发货，18:00后的次日发货。\n" +
            "\n" +
            "2. 配送延迟：\n" +
            "（1）如果您填写的是单位地址，周六周日和法定节假日的送货时间将相应顺延。\n" +
            "（2）如遇国家法定节假日或者是异常天气状况，订单配送可能会出现一定的延迟。"),
    kw104("104","退换货流程","【104】退换货流程\n" +
            "-------------------------\n" +
            "美丽湾网对所售出的商品均会按照三包要求进行售后服务，如需退换货具体的申请方式如下：\n" +
            "1、在微信公众账号服务栏【自助服务】绑定您的微信帐号\n" +
            "2、通过【自助服务】中选择【维权】\n" +
            "3、选择您要申请售后的商品，同时选择对应的售后服务及申请售后原因等信息，即可完成售后申请。"),
    kw105("105","退款说明","【105】退款说明\n" +
            "-------------------------\n" +
            "您好，发生退货的订单，在湾湾收到所退商品后，将款项原路打回您支付时所使用的支付方式，即支付时使用信用卡，则退款至信用卡，到账速度跟进各银行不同有差异；支付时使用财付通余额，则退款至财付通账号。");

    private String keyWord;// 关键字
    private String title;//标题
    private String returnMsg; //回复消息


    private KeyWordEnum(String keyWord, String title,String returnMsg) {
        this.keyWord = keyWord;
        this.title = title;
        this.returnMsg = returnMsg;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 依据msgType返回ReceiveMsgtpyeEnum对象
     *
     * @param keyWord
     * @return
     */
    public static KeyWordEnum getKeyWordEnum(String keyWord) {
        KeyWordEnum[] receiveMsgtpyeEnums = KeyWordEnum.values();
        for (KeyWordEnum obj : receiveMsgtpyeEnums) {
            if (obj.keyWord.equals(keyWord)) {
                return obj;
            }
        }
        return null;
    }
}

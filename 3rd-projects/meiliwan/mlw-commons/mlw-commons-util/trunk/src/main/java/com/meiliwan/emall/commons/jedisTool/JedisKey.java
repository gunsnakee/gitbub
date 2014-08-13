package com.meiliwan.emall.commons.jedisTool;


import com.meiliwan.emall.commons.constant.GlobalNames;

/**
 *
 * @author liushangfeng
 *
 *键的命名规范:  由命名空间名字、实体名字和主键名字构成，相互之间以 $ 分隔，主键名字如果是联合主键，联合主键之间以 _ 分隔
 */
public enum JedisKey {

    session( GlobalNames.SESSION_EXPIRE_SECONDS, "以简单的key、value结构存储，",0,JedisKey.HASH,false),

    imeiliwan$payCodeSed(0,"保存用户上一次支付方式",0,JedisKey.STRING,true),
    imeiliwan$proMyLove(0,"用户对商品喜爱行为，商品地方馆商品喜爱功能",0,JedisKey.HASH,true),
    imeiliwan$cart(24*7*60*60,"购物车信息",0,JedisKey.STRING,true),
    imeiliwan$cashierCart(24*60*60,"确认订单信息",0,JedisKey.STRING,false),
    imeiliwan$commentUsed(0,"用户对评论的操作，商品评论有用或无用功能使用",0,JedisKey.HASH,true),

    /** ====account web 中 paypassword**/
    account$paypwd(1800,"以hset结构存储，key为sessionId,设置支付密码使用",0,JedisKey.HASH,true),
    //24小时，记录支付密码输入错误次数
    account$payerrorcount(86400,"记录支付密码的错误次数,key为userId",0,JedisKey.STRING,false),



    /** ===========地方馆缓存key 定义 =========== */
    publist$dfg(60*60,"地方馆最新发布类型排序",0,JedisKey.STRING,false),
    welcome$dfg(60*60,"地方馆最受欢迎类型排序",0,JedisKey.STRING,false),
    //地方馆商品总条数
    count$dfg(60*60,"地方馆商品总条数",0,JedisKey.STRING,false),
    //默认不适用Cache
    defaultNotUse(0*0,"",0,JedisKey.STRING,false),

    /** ====pms模块相关基础数据缓存相关key===================*/
    pms$pro(12*60*60,"商品表基础数据",0,JedisKey.STRING,false),
    pms$cat(0,"商品类目表基础数据",0,JedisKey.STRING,false),
    pms$brand(0,"商品品牌基础数据",0,JedisKey.STRING,false),
    pms$place(0,"商品产地基础数据",0,JedisKey.STRING,false),
    pms$action(60,"商品用户行为缓存，比如喜欢数量等",0,JedisKey.STRING,false),
    pms$stock(0,"商品库存缓存",0,JedisKey.STRING,false),
    pms$allbrand(1800,"所有品牌",0,JedisKey.STRING,false),

    /**商品详情页相关缓存 */
    pms$detail(60,"商品详情缓存",0,JedisKey.HASH,true),
    pmsapp$detail(60,"app端商品详情缓存",0,JedisKey.HASH,true),
    pms$consults(60,"商品详情咨询缓存",0,JedisKey.STRING,true),
    pms$comments(60,"商品详情评价缓存",0,JedisKey.STRING,true),
    pms$cmt$score(0,"1到5分对应的伪评论内容，用于给无评论内容的评价随机填充评论内容",8,JedisKey.SET,true),//add by liushangfeng

    /** ====base模块相关基础数据缓存相关key===================*/
    base$sys(1800,"系统基础配置项,以普通key value结构存储",0,JedisKey.STRING,false),
	base$transport(1800,"配送管理,运费,根据ID取得区域",0,JedisKey.STRING,false),
	base$transportAreaCode(1800,"根据AreaCode查找",0,JedisKey.STRING,false),
    base$validcode(1800, "以简单的key、value结构存储，可以为sessionId",0,JedisKey.STRING,true),
    base$ipinfo(24*3600*7, "保存ip和地址的对应关系，以哈希hset方式保存",0,JedisKey.HASH,true),
    
    /** ====mms模块相关基础数据缓存相关key===================*/
    mms$email(86400,"以hset结构存储，key为sessionId，验证邮箱时使用",0,JedisKey.HASH,true),
    mms$emailRSPwd(300,"以hset结构存储，key为sessionId，利用邮箱重置登陆密码使用",0,JedisKey.HASH,true),
    mms$emailVI(1800,"以hset结构存储，key为sessionId，利用邮箱验证身份时使用",0,JedisKey.HASH,true),
    mms$mobile(300,"以hset结构存储，key为sessionId，验证手机号码时使用",0,JedisKey.HASH,true),
    mms$uInfo(0, "以hset结构存储，key为用户id",0,JedisKey.HASH,false),
    mms$passport(1800,"用户登陆表基础数据",0,JedisKey.STRING,false),
    mms$extra(1800,"用户信息表基础数据",0,JedisKey.STRING,false),
    mms$vUser(0,"虚拟用户",0,JedisKey.SET,false),
    part3$notRemind(600,"以hset结构存储，key为uId，第三方登录七天免提醒功能",0,JedisKey.HASH,false),


    /** ====bkstage模块相关基础数据缓存相关key===================*/
    bkstage$menu(0,"后台导航",0,JedisKey.STRING,false),
    bkstage$saveSelectState(3600,"后台选择",0,JedisKey.STRING,false),

    /** ====antispam模块相关基础数据缓存相关key===================*/
    antispam$loadTime(0,"词库load时间",0,JedisKey.STRING,true),

    /** ====monitor系统相关key===================*/
    monitor$statistics(0,"监控地图缓存",0,JedisKey.HASH,true),


    /** ====全局 缓存相关key===================*/

    // 测试用
    vu$test(60,"",0,JedisKey.STRING,true,true),
    vu$testNot(1800,"",0,JedisKey.STRING,false),
    vu$hash(1800,"",0,JedisKey.HASH,true,true),
    vu$hashNot(1800,"",0,JedisKey.HASH,false),
    vu$list(0,"",0,JedisKey.LIST,true,true),
    vu$listNot(1800,"",0,JedisKey.LIST,false),
    vu$set(1800,"",0,JedisKey.SET,true,true),
    vu$setNot(1800,"",0,JedisKey.SET,false),
    vu$sortset(0,"",0,JedisKey.SORTED_SET,true,true),
    vu$sortsetNot(1800,"",0,JedisKey.SORTED_SET,false),

    test$test(0, "测试用的key", 8, JedisKey.STRING,false),

    //临时改做不需要持久化 modify by yuxiong 2013-10-08
    secure$rand(3600, "以key/value结构存储，以登陆sessionId为键，随机码为值存储",0,JedisKey.STRING,true),
    submit$norepeat(1800, "以key/value结构存储，以登陆sessionId为键，javaUUID为值存储，防止表单的重复提交",0,JedisKey.STRING,true),

    //======================
	search$key(20,"搜索缓存时间",0,JedisKey.STRING,false),
    appSearch$key(20,"app搜索缓存时间",0,JedisKey.STRING,false),
	searchInfo(24*60*60,"存储最近修改过的商品信息，在solr索引更新完后便会被删除，以set结构存储",0,JedisKey.SET,false),
    //------------------------
	
    //全站碎片缓存
    global$inc(0,"全站碎片缓存",0,JedisKey.HASH,true),
    global$zixun(0,"资讯内容缓存,缓存时间为12小时",0,JedisKey.HASH,true),

    /** 商品可用库存缓存实现 */
    stock$sell(0,"商品可用库存缓存",0,JedisKey.STRING,false),

    //帮助中心 基础缓存
    hc$cache$key(0,"帮助中心内容缓存",5,JedisKey.STRING,true);


    public static final String STRING="String";// 字符串
    public static final String HASH="Hash";//  哈希表
    public static final String LIST="List"; // 列表
    public static final String SET="Set"; // 集合
    public static final String SORTED_SET="SortedSet"; // 有序集合



	public final static String SUFFIX_STR = "suffix";
	public final static String KEY_SPLITER="`@~";
	private String desc;
	private int expTime = 0;
	private int priority = 0;
	private String keyType;

    private boolean persistNeeded = false;


    private boolean persistQuery = false; // 仅当 persistNeeded＝ true 时 才生效。如果从正常状态的Redis查询不到数据，是否需要从mongo查询。
	
	

	/**
	 *
	 * <Description>this is a constructor</Description>
	 *
	 * @param desc 描述
	 */
	private JedisKey(String desc){
		this.desc=desc;
	}

	private JedisKey(int expTime, String desc){
		this.desc=desc;
		this.expTime=expTime;
	}
	
	private JedisKey(int expTime, String desc, int priority, String keyType, boolean persistNeeded){
		this(expTime, desc);
		this.priority = priority;
		this.keyType = keyType;
        this.persistNeeded = persistNeeded;
	}

    private JedisKey(int expTime, String desc, int priority, String keyType, boolean persistNeeded,boolean persistQuery){
        this(expTime,desc,priority,keyType,persistNeeded);
        this.persistQuery = persistQuery;
    }

	public String buildKey(){
		return this.name();
	}

	public String getDesc() {
		return desc;
	}

	public int getExpTime() {
		return expTime;
	}
	
	public int getPriority() {
		return priority;
	}

	public String getKeyType() {
		return keyType;
	}
    public boolean isPersistNeeded() {
        return persistNeeded;
    }

    public boolean isPersistQuery() {
        return persistQuery;
    }

	/**
	 *
	 * @return 判断是否为持久存储的key
	 */
	public boolean isPersist(){
		return expTime == 0l;
	}

}

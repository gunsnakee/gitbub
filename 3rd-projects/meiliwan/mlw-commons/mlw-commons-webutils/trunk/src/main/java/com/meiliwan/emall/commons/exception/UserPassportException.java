package com.meiliwan.emall.commons.exception;

/**
 *
 * 用户未登陆异常
 *
 * User: jiawuwu
 * Date: 13-10-25
 * Time: 下午4:05
 * To change this template use File | Settings | File Templates.
 */
public class UserPassportException extends AbstractNestedRuntimeException{


    /**
	 * 
	 */
	private static final long serialVersionUID = -3862290992415708677L;
	private String code;					//异常code
    private String friendlyMessage="";		//用于页面显示的友好异常提示信息

    private Object[] messageArgs;			//code对应消息的参数对象数组
    private String defaultFriendlyMessage;  //缺省友好异常提示信息

    /**
     * 用指定的cause异常构造一个新的UserPassportException
     * @param cause the exception cause
     */
    public UserPassportException(Throwable cause) {
        super(cause);
    }

    /**
     * 用指定的异常日志 message构造一个UserPassportException
     * @param logMsg the detail message
     */
    public UserPassportException(String logMsg){
        super(logMsg);
    }

    /**
     * 用指定code和cause异常构造一个UserPassportException
     *
     * @param code the exception code
     * @param cause the exception cause
     */
    public UserPassportException(String code,Throwable cause){
        super(code,cause);
        this.code = code;
    }

    /**
     *
     * 用指定的code和cause构造一个UserPassportException,并指定code对应message的参数值
     *
     * @param code the exception code
     * @param cause the root cause
     * @param messageArgs the argument array of the message corresponding to code
     */
    public UserPassportException(String code,Throwable cause,Object[] messageArgs){
        super(cause);
        this.code = code;
        this.messageArgs = messageArgs;
    }


    /**
     * 用指定code和异常日志 message构造一个UserPassportException
     *
     * @param code the exception code
     * @param logMsg the log message
     *
     */
    public UserPassportException(String code,String logMsg) {
        super(logMsg);
        this.code = code;
    }

    /**
     * 用指定的code和异常日志 message构造一个UserPassportException,并指定code对应message的参数值
     *
     * @param code the exception code
     * @param logMsg the detail message
     * @param messageArgs the argument array of the message corresponding to code
     *
     */
    public UserPassportException(String code,String logMsg,Object[] messageArgs){
        super(logMsg);
        this.code = code;
        this.messageArgs = messageArgs;
    }


    /**
     * 用指定code和异常日志 message以及异常cause构造一个UserPassportException,
     *
     * @param code the exception code
     * @param logMsg the detail message
     * @param cause the root cause
     */
    public UserPassportException(String code,String logMsg,Throwable cause){
        super(logMsg,cause);
        this.code = code;
    }

    /**
     * 用指定的异常code和异常日志 message以及异常cause构造一个UserPassportException,
     * 并传递code对应message的参数值
     *
     * @param code the exception code
     * @param logMsg the detail message
     * @param cause the root cause
     * @param messageArgs the argument array of the message corresponding to code
     */
    public UserPassportException(String code,String logMsg,Throwable cause,Object[] messageArgs){
        super(logMsg,cause);
        this.code = code;
        this.messageArgs = messageArgs;
    }

    /**
     * 用指定的code和异常cause以及缺省的页面友好message构造一个UserPassportException
     *
     * @param code the exception code
     * @param cause the root cause
     * @param defaultFriendlyMessage the default friendly message if the friendly message
     * corresponding to code is not exist.
     */
    public UserPassportException(String code,Throwable cause,String defaultFriendlyMessage){
        super(cause);
        this.code = code;
        this.defaultFriendlyMessage = defaultFriendlyMessage;
    }

    /**
     * 用指定的code和异常cause构造一个UserPassportException,传递code对应message的参数值，
     * 并指定缺省的页面友好message
     *
     * @param code the exception code
     * @param cause the root cause
     * @param messageArgs the argument array of the message corresponding to code
     * @param defaultFriendlyMessage the default friendly message if the friendly message
     * corresponding to code is not exist.
     *
     */
    public UserPassportException(String code,Throwable cause,Object[] messageArgs,String defaultFriendlyMessage){
        super(cause);
        this.code = code;
        this.messageArgs = messageArgs;
        this.defaultFriendlyMessage = defaultFriendlyMessage;
    }


    /**
     * 用指定的code和异常日志message构造一个UserPassportException,并指定缺省的页面友好message
     *
     * @param code the exception code
     * @param logMsg the detail message
     * @param defaultFriendlyMessage the default friendly message if the friendly message
     * corresponding to code is not exist.
     *
     */
    public UserPassportException(String code,String logMsg,String defaultFriendlyMessage)	{
        super(logMsg);
        this.code = code;
        this.defaultFriendlyMessage = defaultFriendlyMessage;
    }

    /**
     * 用指定异常code和异常日志message构造一个UserPassportException,传递code对应message的参数值,
     * 并指定缺省的页面友好message
     *
     * @param code the exception code
     * @param logMsg the detail message
     * @param messageArgs the argument array of the message corresponding to code
     * @param defaultFriendlyMessage the default friendly message if the friendly message
     * corresponding to code is not exist.
     *
     */
    public UserPassportException(String code,String logMsg,Object[] messageArgs,String defaultFriendlyMessage){
        super(logMsg);
        this.code = code;
        this.messageArgs = messageArgs;
        this.defaultFriendlyMessage = defaultFriendlyMessage;
    }

    /**
     * 用指定的code和异常日志message以及异常cause构造一个UserPassportException,
     * 并指定缺省的页面友好message
     *
     * @param code the exception code
     * @param logMsg the detail message
     * @param cause the root cause
     * @param defaultFriendlyMessage the default friendly message if the friendly message
     * corresponding to code is not exist.
     *
     */
    public UserPassportException(String code,String logMsg,Throwable cause,String defaultFriendlyMessage){
        super(logMsg,cause);
        this.code = code;
        this.defaultFriendlyMessage = defaultFriendlyMessage;
    }


    /**
     * 用指定异常code和异常日志message以及异常cause构造一个UserPassportException,传递code对应message的参数值,
     * 并指定缺省的页面友好message
     *
     * @param code the exception code
     * @param logMsg the detail message
     * @param cause the root cause
     * @param messageArgs the argument array of the message corresponding to code
     * @param defaultFriendlyMessage the default friendly message if the friendly message
     * corresponding to code is not exist.
     *
     */
    public UserPassportException(String code,String logMsg,Throwable cause,Object[] messageArgs,String defaultFriendlyMessage){
        super(logMsg,cause);
        this.code = code;
        this.messageArgs = messageArgs;
        this.defaultFriendlyMessage = defaultFriendlyMessage;
    }


    /**
     * Return the detail message, including the message from the nested exception
     * if there is one.
     */
    @Override
    public String getMessage() {
        if(code!=null && code.trim().length()>0){
            StringBuilder sb = new StringBuilder();
            sb.append("Code: ").append(code)
                    .append("\rMessage: ").append(super.getMessage());
            return sb.toString();
        }
        return super.getMessage();
    }

    public String getCode() {
        return code;
    }

    public String getFriendlyMessage() {
        return friendlyMessage;
    }

    public void setFriendlyMessage(String friendlyMessage) {
        this.friendlyMessage = friendlyMessage;
    }

    public Object[] getMessageArgs() {
        return messageArgs;
    }

    public void setMessageArgs(Object[] messageArgs) {
        this.messageArgs = messageArgs;
    }

    public String getDefaultFriendlyMessage() {
        return defaultFriendlyMessage;
    }

    public void setDefaultFriendlyMessage(String defaultFriendlyMessage) {
        this.defaultFriendlyMessage = defaultFriendlyMessage;
    }
}

package com.meiliwan.emall.exception;


import com.meiliwan.emall.commons.exception.BaseRuntimeException;


/**
 * 
 * BaseException 框架定义的异常类,也是引入本框架的应用唯一可以创建和使用的异常类：
 * 	<ul> 1.应用捕获到系统异常后如要再重新抛出，一定要封装为BaseException再重新抛出<br>
 * 	 2.应用要抛出业务异常，只能抛出BaseException类型的异常 </ul>
 * <p>
 * BaseException根据不同的需求和使用场景提供了灵活的创建构造函数，涉及到以下几个参数：
 * cause,logMsg ,code ,messageArgs,defaultFriendlyMessage。
 * <p>
 * 
 * <b>logMsg:</b> 异常日志message,用于记录到日志系统的message
 * <p>
 * <b>code：</b>  异常code，指定一个code编码来标识不同的异常。 
 * 
 * 	 <ul> code除了用于标识异常外，还具有另外一个隐含的功能：一个code可以关联到对应的一个friendlyMessage，这个friendlyMessage是当发生异常时，
 * 		     在页面上显示给用户看的友好异常message，它区别于logMsg，logMsg是用于在封装或创建BaseException异常时附加的用于记日志和调试的日志message。 
 * 		  code和 friendlyMessage之间映射关系在系统消息资源属性文件中配置.</ul>
 * 
 * <p>
 * <b>messageArgs：</b>  code 对应消息的参数数组。当code对应的message包含参数，通过这个参数数组来传递实际参数值
 *<p>
 * <b>defaultFriendlyMessage:</b> 缺省的提供页面调用显示的友好异常message。
 * <ul>   框架在确定页面显示的友好异常message的时候，按照下面的逻辑顺序来选择,一旦找到就不再往下找：<br/>
 *    	      异常有code，根据code查找<br/>
 *    <ul> 	1.在系统message资源文件(messages.properties)中查找code对应的message<br/>	  
 *        	2.使用defaultFriendlyMessage<br/>
 *          3.查找系统message资源文件(messages.properties)中key为"exception.defaultMessage"的message<br/> 
 *    </ul>       
 *        异常没有code，根据根异常类全名查找 <br/>        
 *     <ul> 1.在系统message资源文件(messages.properties)查找根异常(使用类的全名,如java.io.IOException)对应的message<br/>
 *          2.查找系统message资源文件(messages.properties)中key为"exception.defaultMessage"的message<br/>  
 *     </ul>        
 * </ul>  
 *      
 * 
 */
public class IceServiceRuntimeException extends  BaseRuntimeException{
	
	private static final long serialVersionUID = 8458544317507845657L;
	
	public IceServiceRuntimeException(String code, String msg, Throwable cause) {
		super(code, msg, cause);
	}
	
	public IceServiceRuntimeException(String code, String msg) {
		super(code, msg);
	}
}

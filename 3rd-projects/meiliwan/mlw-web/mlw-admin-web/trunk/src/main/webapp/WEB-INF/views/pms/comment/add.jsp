<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>

<script type="text/javascript" src="js/jquery.raty.js"></script>
<style>
.clearfix{clear: both}
</style>
<div class="pageContent">
	<form id="pms_comment_add" method="post" action="/pms/comment/add" class="pageForm required-validate" >		
		<div class="pageFormContent nowrap" layouth="56" style="height: 205px; overflow: auto;">	
			<dl>
				<dt>前台用户：</dt>
				<dd><input name="handler" type="hidden" value="1"/>
				<input name="uid" type="hidden" value="${user.uid}" />
				<input name="nickName"  type="hidden" value="${user.nickName}" />
				<button id="getRandomShamUser">更换用户</button>
				<span id="comment_user">用户ID：${user.uid} 用户昵称：${user.nickName}<span>
				</dd>
			</dl>
			
			<dl>
				<dt>商品编号：</dt>
				<dd><input name="proId"  type="hidden" value="${proId}"/>
				${proId}
				</dd>
			</dl>
			
			<dl>
				<dt>评价分数：</dt>
				<dd><input id="comment_star_hidden" name="score" type="hidden" />
				<div id="comment_star"></div>
				<span id="comment_star_error" generated="true" style="display:none; overflow:hidden; width:165px; height:21px; padding:0 3px; line-height:21px; background:#F00; color:#FFF; top:5px; left:318px;">还没有打分哦</span>
				</dd>
			</dl>
			<dl>
				<dt>评论内容：</dt>
				<dd><textarea id="pms_comment_content" name="content"  rows="5" cols="50" ></textarea><!-- 5-400个汉字  -->
				<span id="content_error" generated="true" style="display:none; overflow:hidden; width:165px; height:21px; padding:0 3px; line-height:21px; background:#F00; color:#FFF; top:5px; left:318px;">必填字段,长度介于10和800之间的字符串</span>
				</dd>
			</dl>
			<dl>
				<dt>评价时间：</dt>
				<dd><input id="pms_comment_time" type="text" name="time" value="" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none" size="30"/>
				<span id="comment_time_error" generated="true" style="display:none; overflow:hidden; width:165px; height:21px; padding:0 3px; line-height:21px; background:#F00; color:#FFF; top:5px; left:318px;">请选择时间</span>

				<input id="pms_system_time" name="systemTime" type="checkbox" value="1"/>使用当前系统时间</dd>
			</dl>
			
		</div>

		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>

	</form>
</div>
<script type="text/javascript">
$(document).ready(function(){
	
	$.ajax({
	        type: "POST",
	        url: "/mms/passport/randomShamUser",
	        data: "",
	        success: function (data) {
	        	$("#pms_comment_add input[name=uid]").val(data.uid);
	        	$("#pms_comment_add input[name=nickName]").val(data.nickName);
	        	var hh="用户ID："+data.uid+"用户昵称："+data.nickName;
	            $("#comment_user").html(hh);
	        }
	    });
	
	$("#getRandomShamUser").click(function(e){
		$.ajax({
	        type: "POST",
	        url: "/mms/passport/randomShamUser",
	        data: "",
	        success: function (data) {
	        	$("#pms_comment_add input[name=uid]").val(data.uid);
	        	$("#pms_comment_add input[name=nickName]").val(data.nickName);
	        	var hh="用户ID："+data.uid+"用户昵称："+data.nickName;
	            $("#comment_user").html(hh);
	        }
	    });
	    e.preventDefault();

	});


	$('#comment_star').raty({ 
		
		click:function(num){
			$("#comment_star_hidden").val(num);
		},
		path:'/images'

	});



	$("#pms_comment_add").validate({
		submitHandler:function(form){

			//打分
			var star=$("#comment_star_hidden").val();
			
			if(!star){
				
				$("#comment_star_error").show();
				return false;
			}
			
			$("#comment_star_error").hide();
			
			//评论=<
			var content = $("#pms_comment_content").val();
			
			if(!content || content.length<10 || content.length>800){
				$("#content_error").show();
				return false;
			}
			
			$("#content_error").hide();

			//时间
			var ckeck = $("#pms_system_time").attr("checked");
			
			var time = $("#pms_comment_time").val();

			
			time = time.replace("-","/");//替换字符，变成标准格式  
			var start=new Date("2013/09/01 00:00:00");//取今天的日期  
			var end=new Date();
			var d1 = new Date(Date.parse(time));  
			
			if(d1>end){  
			  alert("时间不能大于当前时间");  
			  return false;
			}  

			if(d1<start){  
			  alert("时间不能小于2013/09/01 00:00:00");  
			  return false;
			}  

			if(!ckeck&&!time){
				$("#comment_time_error").show();
				return false;
			}
			$("#comment_time_error").hide();
			return validateCallback(form, navTabAjaxDone);
		}    
	}); 

	//是否支持免邮
	$("#pms_system_time").click(function(){

		var chk = $(this).attr("checked")
		if(chk){
			$("#pms_comment_time").val("").attr("disabled","true").addClass("textInput disabled");
		}else{
			$("#pms_comment_time").removeAttr("disabled").removeClass('disabled');
		}
	});

});
</script>
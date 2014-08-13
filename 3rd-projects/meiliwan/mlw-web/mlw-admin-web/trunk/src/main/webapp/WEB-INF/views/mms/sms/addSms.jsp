<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<script>
function submitForm(t, d){
	var sigleM = document.getElementsByName('mobile')[0].value ;
	if(sigleM != ''){
		document.getElementsByName('mobiles')[0].value = sigleM ;
	}
	if(document.getElementsByName('mobiles')[0].value == '' ){
		alertMsg.warn("接收号码不能为空，请检查！"); 
		return false ;
	}
  
  return validateCallback(t, d) ;
}

function importCardSuccess (file, data, response) {
	  var json = eval("("+data+")") ;
    if (json.data.numbers != null){
        document.getElementsByName('mobiles')[0].value = json.data.numbers ;
        document.getElementsByName('fileName')[0].value = json.data.fileN ;
        $("#messageBox").text(json.data.msg) ;

    }else{
        $("#messageBox").text(json.data.msg) ;
    }
}
</script>
<div class="pageContent">
	<form method="post" action="/mms/sms/send" class="pageForm required-validate" onsubmit="return submitForm(this, dialogAjaxDone)" novalidate="novalidate">
		<div class="pageFormContent" layouth="56" style="height: 303px; overflow: auto;">
			<input type="hidden" name="mobiles" value="" />
			<input type="hidden" name="fileName" value="" />
			<p>
        <label>单独发送</label>
        <label><input type="text" name="mobile" class="digits" minlength="11" maxlength="11" value="" /></label>
      </p>
      <p width="550px">
        <label>批量发送</label>
        <label>
          <input id="importStock" type="file" name="file" uploaderOption="{
			        swf:'uploadify/scripts/uploadify.swf',
			        uploader:'/mms/sms/import',
			        formData:{},
			        buttonText:'导入手机号码',
			        fileSizeLimit:'2048KB',
			        fileTypeDesc:'*.txt;',
			        fileTypeExts:'*.txt;',
			        auto:true,
			        multi:true,
			        onUploadSuccess:importCardSuccess
			         }"/>
        </label>
        <br/><span style="BACKGROUND-COLOR: #FFFF00; COLOR: #0000CC">不可超过1000个号码</span> <span id="messageBox" style="color: #ff0000;">&nbsp;&nbsp;&nbsp;</span>
      </p>
      <p style="padding-top:10px;">
        <label>短信内容</label>
        <label><textarea name="content" cols="65" rows="8" minlength="5" maxlength="500" class="required"></textarea></label>
      </p>
			
		</div>
		
		<div class="formBar">
			<ul>
        <li><div class="buttonActive"><div class="buttonContent"><button type="submit">确定发送</button></div></div></li>
        <li><div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div></li>
      </ul>
		</div>
		
	</form>
</div>
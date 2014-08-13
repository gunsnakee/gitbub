<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<script src="/js/ajaxfileupload.js" type="text/javascript"></script>


<div class="pageContent">

    <div id="uploadForm">
    	<p>wms:请务必选择xls属于的类型</p>
    	<p>
            <input type="radio" name="type" value="tmall"/>天猫
    	<input type="radio" name="type" value="taoczwms"/>淘常州
    	<input type="radio" name="type" value="wowotuanwms"/>窝窝团
    	<input type="radio" name="type" value="yhd"/>一号店
        <input type="radio" name="type" value="jd"/>京东
        </p>
        <p>
        <input id="myupload" type="file" name="myupload"/>
        </p>
		<input id="myupload_button" type="button" value="上传"/>
		<div id="result"></div>
    </div>
</div>


<div class="pageContent">

    <div id="uploadForm2">
        <p>oms:请务必选择xls属于的类型</p>
        <p>
        <!--<input type="radio" name="type" value="tmall"/>天猫-->
        <input type="radio" name="type" value="taocz"/>淘常州
        <input type="radio" name="type" value="wowotuan"/>窝窝团
        <!--<input type="radio" name="type" value="yhd"/>一号店
        <input type="radio" name="type" value="jd"/>京东-->
        <input type="radio" name="type" value="nuomi"/>糯米
        </p>
        <p><button class="button" id="addInput" >添加一组</button>
        </p>
        <p id="calculation_model">
            <input id="file0" type="file" name="file"/>
            
        </p>
        <input id="myupload_button2" type="button" value="上传"/>
        <div id="result2"></div>
    </div>
<!-- http://www.myexception.cn/ajax/1308964.html -->
</div>

<script>

$("#addInput").click(function(){
    addInputFile();
});
function addInputFile(){
    var len = $("#calculation_model input").length;
    $("#calculation_model").append("<div><input id='file"+len+"' type='file'  name='file'><span class='file_del'>删除</span><div>");
}
$("#calculation_model").on("click",".file_del",function(){
    $(this).parent().remove();

});

$("#myupload_button2").click(function(){
    var type=$("#uploadForm2 input[name='type']:checked").val();
    var fileElementId=[];
    $("#calculation_model input").each(function(){
        var name = $(this).attr("id");
        fileElementId.push(name)
    });
    $.ajaxFileUpload({  
        url:'/statistics/multiUpload',  
        secureuri:false,                           //是否启用安全提交,默认为false   
        fileElementId:fileElementId,               //文件选择框的id属性  
        dataType:'text',                           //服务器返回的格式,可以是json或xml等  
        data: { "type": type},
        success:function(data){            //服务器响应成功时的处理函数  
            if(data.result){
                 $('#result').html("上传成功");  
            }else{
                $('#result').html("上传解析失败，请检查后重试"); 
            }
        },  
        error:function(data, status, e){ //服务器响应失败时的处理函数  
            $('#result').html('上传解析失败，请检查后重试');  
        }  
    });  
});


$("#myupload_button").click(function(){
    var type=$("#uploadForm input[name='type']:checked").val();
    $.ajaxFileUpload({  
        //处理文件上传操作的服务器端地址(可以传参数,已亲测可用)
        url:'/statistics/upload',  
        secureuri:false,                           //是否启用安全提交,默认为false   
        fileElementId:['myupload','myupload1'],               //文件选择框的id属性  
        dataType:'text',                           //服务器返回的格式,可以是json或xml等  
        data: { "type": type},
        success:function(data){            //服务器响应成功时的处理函数  
        	if(data.result){
        		 $('#result').html("上传成功");  
        	}else{
        		$('#result').html("上传解析失败，请检查后重试"); 
        	}
           

        },  
        error:function(data, status, e){ //服务器响应失败时的处理函数  
            $('#result').html('上传解析失败，请检查后重试');  
        }  
    });  
});


</script>
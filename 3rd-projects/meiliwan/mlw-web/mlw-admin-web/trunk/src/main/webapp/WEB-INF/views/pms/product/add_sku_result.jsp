<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent" id="pageContent">
    <form class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone);" id="sava-sku-Form">
		<div class="pageFormContent" layoutH="56">
			<input id="save-spuId" name="spuId"  type="hidden" value="${spuId}"/>
            <input id="skuName" name="skuName"  type="hidden" value="${skuName}"/>
            <input id="skuProp1" name="skuProp1"  type="hidden" value="${skuProp1}"/>
            <input id="skuProp2" name="skuProp2"  type="hidden" value="${skuProp2}"/>
            <input id="skuProv1" name="skuProv1"  type="hidden" value="${skuProv1}"/>
            <input id="skuProv2" name="skuProv2"  type="hidden" value="${skuProv2}"/>
            <input id="skuDetail" name="skuDetail"  type="hidden" value="${skuDetail}"/>
            <p>
                <label>生成规格为：</label>
                <label style="width: 200px"><c:if test="${empty skuDetail}">无</c:if><c:if test="${!empty skuDetail}">${skuDetail}</c:if></label>
            </p>
            <p>
                <label>SKU条形码：</label>
                <input type="text" class="required digit" name="barCode" id="barCode" minlength="9" maxlength="13" remote="/pms/product/list?selectFlag=1&type=add" class="textInput valid"><span for="barCode" class="error" id="errorId" style="display: none;">已经被使用或数据格式错误</span>
                <span class="info">(长度为9到13之间的字符串)</span>
            </p>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="button" id="sku-submit" onclick="saveSku()">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" id="add-cancle" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
    </form>
</div>
<script type="text/javascript">

    function saveSku(){
        var $form = $("#sava-sku-Form");
        if (!$form.valid()) {
            return false;
        }
        var spuId = $("#save-spuId").val();
        var skuName = $("#skuName").val();
        var skuProp1 = $("#skuProp1").val();
        var skuProp2 = $("#skuProp2").val();
        var skuProv1 = $("#skuProv1").val();
        var skuProv2 = $("#skuProv2").val();
        var barCode = $("#barCode").val();
        if(barCode ==''){
            $("#sku-submit").removeAttr("class");
            return false;
        }else{
            if(barCode.length<9){
                return false;
            }
            $("#errorId").hide();
            var re;
            re = /[^0-9a-zA-Z]/;
            if(re.test(barCode)){
                return false;
            }
        }
        $.post("/pms/product/add",{step:9,spuId:spuId,skuName:skuName,skuProp1:skuProp1,skuProp2:skuProp2,skuProv1:skuProv1,skuProv2:skuProv2,barCode:barCode},function(data){
            if(data == '0'){
                alertMsg.error("生成对应规格失败");
                return false;
            }else{
                var resultId = "sku-add-"+skuProv1+"-"+skuProv2;
                var skuDetail = $("#skuDetail").val();
                $("#"+resultId).attr('href',"/pms/product/update?proId="+data+"&skuDetail="+skuDetail);
                $("#"+resultId).attr('width',"900")
                $("#"+resultId).attr('height',"600")
                var htmlStr = '<span style="color: #1727ff">商品ID:'+data+'</span>';
                $("#"+resultId).html(htmlStr).after('<br><span id="'+data+'">'+'当前状态:未编辑完成</span>');
                $('#add-cancle').click();
            }
        });
    }
</script>

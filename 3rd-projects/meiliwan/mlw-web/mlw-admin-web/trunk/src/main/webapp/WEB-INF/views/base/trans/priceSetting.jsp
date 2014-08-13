<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>

<div id="priceSettingStationFare" style="display:none;">${stationFare.type},${stationFare.fixedPrice},${stationFare.fullFreeLimit},${stationFare.notFullFreePrice}</div>

<div class="pageContent" id="trans_tree_add">
  <form id="trans_add_form" method="post" action="/base/fare/delAddPrice" class="pageForm required-validate" onsubmit="return TransValidateCallback(this, transNavTabAjaxDone);">
	<!--  ------ tree1 --------  -->
	<div  style=" float:left; display:block; margin:10px; overflow:auto; width:200px; height:400px; overflow:auto; border:solid 1px #CCC; line-height:21px; background:#FFF;">
		<ul id="trans_tree1">
		<c:forEach var="e" items="${tree}">
			<li>
			<input type="checkbox" name="area_code" value="${e.areaCode}" onclick="tree1_click(${e.areaCode},render1,this)">
			${e.areaName}
			</li>
		</c:forEach>
		</ul>
	</div>
	<!-- tree1 end -->
	
	<!--  ------ tree2 --------  -->
	<div style=" float:left; display:block; margin:10px; overflow:auto; width:200px; height:400px; overflow:auto; border:solid 1px #CCC; line-height:21px; background:#FFF;">
	
		<ul id="trans_tree2"></ul>
	</div>
	<!-- tree2 end -->
	
	<!--  ------ tree3 --------  -->
	<div style=" float:left; display:block; margin:10px; overflow:auto; width:200px; height:400px; overflow:auto; border:solid 1px #CCC; line-height:21px; background:#FFF;">
	
		<input id="tree3_all_check" type="checkbox" name="all" onclick="trans_all_click(this)">全选
		<ul id="trans_tree3"></ul>
	
	</div>
	<!-- tree3 end -->
			
	<div class="pageFormContent" layoutH="76" id="stationFareView" >
		<p>
	    	<input id="priceSettingUnified1" type="radio" name="unified"  value="1">使用全局运费设置
		</p>
		<p>
	    	<input id="priceSettingUnified0" type="radio" name="unified"  value="0">独立设置运费
		</p>
		<div id="priceSettingUnifiedNo" style="display:none;">
			<p>
				<input id="priceSettingRadioFixed" type="radio" name="type" value="fixed" checked>固定运费 
		    	<input id="priceSettingRadioFull" type="radio" name="type" value="full" >满包邮  
		    	<input id="priceSettingRadioFree" type="radio" name="type" value="free" >免运费
			</p>
			<div  class="clearfix"></div>

			<div id="priceSettingFixed">
				<p>
					<label> 固定运费的价格：</label>
			   		<input type="text" name="fixedPrice" value="0" class="required number gtZero" max="1000000" >元
				</p>
				<div  class="clearfix"></div>
			</div>

			<div id="priceSettingFull" style="display:none;">
				<p>
					<label>满包邮需要的订单价格</label>
			   		<input type="text" name="fullFreeLimit" value="0" class="required number gtZero" max="1000000" >元
				</p>
				<div  class="clearfix"></div>
				<p>
					<label>未达到满包邮运费价格</label>
			   		<input type="text" name="notFullFreePrice" value="0" class="required number gtZero" max="1000000" >元
				</p>
			</div>

		</div>
		<p>
			<label>预计到达时间(最小)：</label>
			<input  name="predictTimeMin" type="text" class="required digits" min="1" max="1000" />小时
		</p>
		<p>
			<label>预计到达时间(最大)：</label>
			<input  name="predictTimeMax" type="text" class="required digits" min="1" max="1000" />小时
		</p>

	</div>

	<div class="formBar">
		<ul>
			<li><div class="buttonActive"><div class="buttonContent"><button type="submit" onkeydown="if(13==event.keyCode){return false;}">保存</button></div></div></li>
			<li>
				<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
			</li>
		</ul>
	</div>

   </form>
</div>
<script type="text/javascript">

jQuery.validator.addMethod("gtZero", function(value, element) {       
     return this.optional(element) || value>0;       
 }, "最小值不能为0");

function TransValidateCallback(_this, transNavTabAjaxDone){
	var len = $("#trans_tree3 input:checked[name='area_code'] ").length;
	
	if(!len){
		alert("必须选择第三级区域");
		return false;
	}
	
	var max = $("#stationFareView input[name='predictTimeMax']").val();
	var min = $("#stationFareView input[name='predictTimeMin']").val();
	if(min*1>max*1){
		alert("预计到达时间最小值不能大于最大值");
		return false;
	}
	return validateCallback(_this, transNavTabAjaxDone);
}

 

//是否支持免邮
$("#trans_fieldset_free_support").click(function(){

	var chk = $(this).attr("checked")
	if(!chk){
		$("#full_free_support").val(0).attr("disabled","true").addClass("textInput disabled");
	}else{
		$("#full_free_support").removeAttr("disabled").removeClass('disabled');
	}
});


function tree1_click(pcode,fn,_thiz){
	
	var d={
		parentCode:pcode
	};
	$.ajax({
		url:"/base/fare/getByParentCode",
		data:d,
		success: function(data) {
			fn(data);
		},
		dataType:"json"
	});
	var $this=$(_thiz);
	var $ul = $this.parent().parent();
	var cked = $this.attr("checked");
	
	var len = $ul.find("input").each(function(){
		$(this).attr("checked",false);
	});
	$this.attr("checked",!!cked);
	$("#tree3_all_check").attr("checked",false);
}

/*
 * 渲染树形2
*/
function render1(list){
	var len=list.length;
	
	var temp="";
	for(var i=0;i<len;i++){
		
		temp+='<li><input type="checkbox" name="area_code" value="'+list[i].areaCode+'" onclick="tree1_click('+list[i].areaCode+',render2,this)">'+list[i].areaName+'</li>';
	}
	$("#trans_tree2").html(temp);
	$("#trans_tree3").html("");
	$("#tree3_all_check").attr("checked",false);
}

function getStationFare(){
	
	var stationFare = {};
	var arr = [];
	arr = $("#priceSettingStationFare").text().split(",");
	stationFare["type"]=arr[0];
	stationFare.fixedPrice=arr[1];
	stationFare.fullFreeLimit=arr[2];
	stationFare.notFullFreePrice=arr[3];
	return stationFare;
}



function getIndependent(data){
	var after="";
	if(data["type"]=="fixed"){
		if(data.fixedPrice||data.fixedPrice==0){
			after+=" 固";
			after+=data.fixedPrice;
		}
	}
	if(data["type"]=="full"){
		if(data.fullFreeLimit||data.fullFreeLimit==0){
			after+=" 满";
			after+=data.fullFreeLimit;
		}
		if(data.notFullFreePrice||data.notFullFreePrice==0){
			after+=" 未";
			after+=data.notFullFreePrice;
		}
		
	}
	if(data["type"]=="free"){
		after+="免";
	}
	if((data.predictTimeMin||data.predictTimeMin==0)&&(data.predictTimeMax||data.predictTimeMax==0)){
		after+=" "+data.predictTimeMin+"-"+data.predictTimeMax+"小时";
	}
	return after;
}
		


/*
 * 渲染树形3
*/
function render2(list){
	var len=list.length;
	
	var temp="";
	for(var i=0;i<len;i++){
		var after = "";
		if(list[i].unified){
			var data =getStationFare();
			after="全局";
			after+=getIndependent(data);	
		}
		if(list[i].unified==0){
			after="独立";
			after+=getIndependent(list[i]);
		}

		
		temp+='<li><input type="checkbox" name="area_code" value="'+list[i].areaCode+'">'+list[i].areaName+' '+after+'</li>';
	}
	$("#trans_tree3").html(temp);
	
}


function trans_all_click(thiz){

	
	if(thiz.checked==true){
		
		$("#trans_tree3 input").each(function(){
			$(this).attr("checked",true);
		});
	}else{
		$("#trans_tree3 input").each(function(){
			$(this).attr("checked",false);
		});
	}
}

/*
取消
*/
function trans_price_cancel(){

	var data={};
	var ids="";  

	$("#trans_tree3 input:checked[name='area_code'] ").each(function(){  
		ids+=$(this).val()+",";  
	});
	data.areaCode=ids.substring(0,ids.length-1);
	
	data.companyId=999;
	$.ajax({
		url:"/base/transRef/cancelCOD",
		data:data,
		success: function(data) {
			refreshTree3();
		},
		dataType:"json"
	}); 
}

//完成后的回调
function transNavTabAjaxDone(json){
	
	//navTabAjaxDone(json);
	refreshTree3();
}

function refreshTree3(){
	var pcode = $("#trans_tree2 input:checked").val();
	var d={
		parentCode:pcode
	};
	$.ajax({
		url:"/base/fare/getByParentCode",
		data:d,
		success: function(data) {
			render2(data);
		},
		dataType:"json"
	});
	$("#tree3_all_check").attr("checked",false);
}

//---------------右侧设置
var unifiedNoLimit= $("#priceSettingUnifiedNo input[name='fullFreeLimit']");
unifiedNoLimit.val()==0?unifiedNoLimit.val(10):0;

var unifiedNoNotFull = $("#priceSettingUnifiedNo input[name='notFullFreePrice']");
unifiedNoNotFull.val()==0?unifiedNoNotFull.val(10):0;
var unifiedNoFixed = $("#priceSettingUnifiedNo input[name='fixedPrice']");
unifiedNoFixed.val()==0?unifiedNoFixed.val(10):0;

$("#priceSettingUnified1").click(function(){
	$("#priceSettingUnifiedNo").hide();
});
$("#priceSettingUnified0").click(function(){
	$("#priceSettingUnifiedNo").show();
});

$("#priceSettingRadioFixed").click(function(){

	$("#priceSettingFixed").show();
	$("#priceSettingFull").hide();
	unifiedNoLimit.val()==0?unifiedNoLimit.val(10):0;
	unifiedNoNotFull.val()==0?unifiedNoNotFull.val(10):0;
});

$("#priceSettingRadioFull").click(function(){
	
	$("#priceSettingFixed").hide();
	$("#priceSettingFull").show();
	unifiedNoFixed.val()==0?unifiedNoFixed.val(10):0;

});

$("#priceSettingRadioFree").click(function(){
	
	$("#priceSettingFixed").hide();
	$("#priceSettingFull").hide();
	
	unifiedNoLimit.val()==0?unifiedNoLimit.val(10):0;
	unifiedNoNotFull.val()==0?unifiedNoNotFull.val(10):0;
	unifiedNoFixed.val()==0?unifiedNoFixed.val(10):0;

});
</script>
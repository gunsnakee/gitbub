<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">设置货到付款区域-${map['company'].name}</h2>

<input  id="trans_company_id" type="hidden" name="company_id" value="${map['company'].id}"/>

<div class="pageContent" id="transCompany_tree_add">
       
			<!--  ------ tree1 --------  -->
			<div  style=" float:left; display:block; margin:10px; overflow:auto; width:200px; height:400px; overflow:auto; border:solid 1px #CCC; line-height:21px; background:#FFF;">
			<ul id="transCompany_tree1">
			<c:forEach var="e" items="${map['tree']}">
				<li>
				<input type="checkbox" name="area_code" value="${e.areaCode}" onclick="transComptree1_click(${e.areaCode},render1,this)">
				<a tname="area_code" tvalue="${e.areaCode}">${e.areaName}</a>
				</li>
			</c:forEach>
			<button type="button" onclick="cancel('#transCompany_tree1')">取消设置</button>
			</ul>
			</div>
			<!-- tree end -->
			
			<!--  ------ tree2 --------  -->
			<div style=" float:left; display:block; margin:10px; overflow:auto; width:200px; height:400px; overflow:auto; border:solid 1px #CCC; line-height:21px; background:#FFF;">
			
			<ul id="transCompany_tree2">
			</ul>
			<button type="button" onclick="cancel('#transCompany_tree2')">取消设置</button>
			</div>
			<!-- tree end -->
			
			<!--  ------ tree3 --------  -->
			<div style=" float:left; display:block; margin:10px; overflow:auto; width:200px; height:400px; overflow:auto; border:solid 1px #CCC; line-height:21px; background:#FFF;">
			
			<!--<input type="checkbox" name="all" onclick="transCompany_all_click(this,'#transCompany_tree3')">全选-->
			<ul id="transCompany_tree3"></ul>
			<button type="button" onclick="cancel('#transCompany_tree3')">取消设置</button>
			</div>
			<!-- tree end -->
			
			<!--  ------ tree4 --------  -->
			<div style=" float:left; display:block; margin:10px; overflow:auto; width:200px; height:400px; overflow:auto; border:solid 1px #CCC; line-height:21px; background:#FFF;">
			
			<input id="tree4_all_check" type="checkbox" name="all" onclick="transCompany_all_click(this,'#transCompany_tree4')">全选
			<ul id="transCompany_tree4"></ul>
			<button type="button" onclick="cancel('#transCompany_tree4')">取消设置</button>
			</div>
			<!-- tree end -->
		
        <div class="formBar" layoutH="58">
            <ul>
                <li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="transCompany_submit()">保存</button></div></div></li>
				<!--<li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="transCompany_cancel()">取消货到付款设置</button></div></div></li>-->
                <li><div class="button"><div class="buttonContent"><button class="close" type="button">关闭</button></div></div></li>
            </ul>
        </div>
   
</div>
<script type="text/javascript">
function cancel(input){

	var data={};
	var ids="";  

	$(input).find("input:checked[name='area_code'] ").each(function(){  
		ids+=$(this).val()+",";  
	});
	data.areaCode=ids.substring(0,ids.length-1);
	
	
	var comId = $("#trans_company_id").val();
	data.companyId=comId;
	
	$.ajax({
		url:"/base/transRef/cancelCOD",
		data:data,
		success: function(data) {
			if(data.result){
				refreshTree34();
			}else{
				alert("请先取消下一级的货到付款区域");
			}
		},
		dataType:"json"
	}); 

}


function transComptree1_click(pcode,fn,_thiz){
	
	var data={};
	var comId = $("#trans_company_id").val();
	data.transCompanyId=comId;
	data.parentCode=pcode;
	$.ajax({
		url:"/base/transRef/getDeliveryAreaByPid",
		data:data,
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

	$("#transCompany_tree_add input[name=all]").each(function(){
		$(this).attr("checked",false);
	});
	
}
//$("#tree3_all_check").attr("checked",false);
/*
 * 渲染树形2
*/
function render1(list){
	var len=list.length;
	
	var temp="";
	for(var i=0;i<len;i++){
		var cod = "";
		if(list[i].cashOnDelivery){
			cod="*";
		}
		
		temp+='<li><input type="checkbox" name="area_code" value="'+list[i].areaCode+'" onclick="transComptree1_click('+list[i].areaCode+',render2,this)"><a>'+list[i].areaName+'</a>'+cod+'</li>';
	}
	$("#transCompany_tree2").html(temp);
	$("#transCompany_tree3").html("");
	$("#transCompany_tree4").html("");
}

/*
 * 渲染树形3
*/
function render2(list,refresh_){
	var len=list.length;
	
	var temp="";
	for(var i=0;i<len;i++){
		var cod = "";

		if(list[i].cashOnDelivery){
			cod="*";

		}

		temp+='<li><input type="checkbox" name="area_code" value="'+list[i].areaCode+'" onclick="transComptree1_click('+list[i].areaCode+',render3,this)"><a>'+list[i].areaName+'</a>'+cod+'</li>';
	}
	$("#transCompany_tree3").html(temp);
	if(!refresh_){
		$("#transCompany_tree4").html("");
	}
	
	
}

/*
 * 渲染树形4
*/
function render3(list){
	var len=list.length;
	
	var temp="";
	for(var i=0;i<len;i++){
		var cod = "";
		if(list[i].cashOnDelivery){
			cod="*";
		}
		temp+='<li><input type="checkbox" name="area_code" value="'+list[i].areaCode+'"><a>'+list[i].areaName+'</a>'+cod+'</li>';
	}
	$("#transCompany_tree4").html(temp);
	
}

/*
提交
*/
function transCompany_submit(){

	var data={};
	var ids="";  

	$("#transCompany_tree_add input:checked[name='area_code'] ").each(function(){  
		ids+=$(this).val()+",";  
	});
	data.area_code=ids.substring(0,ids.length-1);
	
	
	var comId = $("#trans_company_id").val();
	data.company_id=comId;
	
	$.ajax({
		url:"/base/transRef/addCOD",
		data:data,
		success: function(data) {
			refreshTree34();
		},
		dataType:"json"
	}); 

}


function refreshTree34(){
	var pcode2 = $("#transCompany_tree2 input:checked").val();
	var pcode3 = $("#transCompany_tree3 input:checked").val();
	var comId = $("#trans_company_id").val();
	var d2={
		parentCode:pcode2,
		transCompanyId:comId
	};
	var d3={
		parentCode:pcode3,
		transCompanyId:comId
	};
	$.ajax({
		url:"/base/transRef/getDeliveryAreaByPid",
		data:d2,
		success: function(data) {
			render2(data,true);
		},
		dataType:"json"
	});
	$.ajax({
		url:"/base/transRef/getDeliveryAreaByPid",
		data:d3,
		success: function(data) {
			render3(data);
		},
		dataType:"json"
	});
	$("#tree4_all_check").attr("checked",false);
}

/*
取消
*/
function transCompany_cancel(){

	var data={};
	var ids="";  

	$("#transCompany_tree3 input:checked[name='area_code'] ").each(function(){  
		ids+=$(this).val()+",";  
	});
	data.areaCode=ids.substring(0,ids.length-1);
	
	var comId = $("#trans_company_id").val();
	data.companyId=comId;
	
	$.ajax({
		url:"/base/transRef/cancelCOD",
		data:data,
		success: function(data) {
			//返回{}
		},
		dataType:"json"
	}); 
}


function transCompany_all_click(thiz,tree){

	if(thiz.checked==true){
		
		$(tree).find("input").each(function(){
			$(this).attr("checked",true);
		});
	}else{
		$(tree).find("input").each(function(){
			$(this).attr("checked",false);
		});
	}
}
</script>
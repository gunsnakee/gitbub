<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<style>
.clearfix{clear: both}
</style>
<div class="pageContent">
	<form id="pms_brand_add" method="post" action="/pms/brand/add" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone);">
		<div class="pageFormContent" layoutH="76">
			<%--<p>--%>
			<%--<label for="first">一级类目：:</label>--%>
				<%----%>
					<%--<select id="brand_first" name="first_category_id">--%>
						<%--<option value="0">全部</option>--%>
						<%--<c:forEach var="e" items="${categoryList}">--%>
							<%--<option value="${e.categoryId}" >${e.categoryName}</option>--%>
						<%--</c:forEach>--%>
					<%--</select>--%>
				<%--</p>--%>
				<%--<div  class="clearfix"></div>--%>
				<%--<p>--%>
					<%--<label for="second">二级类目:</label>--%>
				<%----%>
					<%--<select id="brand_second" name="second_category_id">--%>
						<%--<option value="0">全部</option>--%>
					<%--</select>--%>
					<%--</p>--%>
					<%--<div  class="clearfix"></div>--%>
				<%--<p>--%>
					<%--<label for="third">三级类目:</label>--%>
				<%----%>
					<%----%>
						<%--<select name="third_category_id" id="brand_third">--%>
						<%--<option value="">全部</option>--%>
					<%--</select>--%>
				<%--</p>--%>
			<div  class="clearfix"></div>
			<p>
				<label>品牌名称：</label>
				<input name="handle" type="hidden" value="1"/>
                <input type="text" class="required" name="name" size="30" maxlength="32" remote="/pms/brand/list?type=2" class="textInput valid"><span for="name" class="error" style="display: none;">该名称已经使用</span>
			</p>
			<div  class="clearfix"></div>
			<p>
				<label>品牌英文名：</label>
				<input name="en_name" type="text" size="30" alt="请输入品牌英文名"  maxlength="32"/>
			</p>
			<div  class="clearfix"></div>
			<p>
				<label>其他名称：</label>
				<input name="other_name" type="text" size="30"  alt="请输入其他名称" maxlength="64"/>
			</p>
			<div  class="clearfix"></div>
			<p>
				<label>品牌首字母：</label>
				<input name="first_char" class="required" type="text" size="30"  maxlength="1"/>
			</p>
			<div  class="clearfix"></div>
			<p>
				<label>描述：</label>
				<input name="descp" type="text" size="30"  alt="请输入描述" maxlength="500"/>
			</p>
			<div  class="clearfix"></div>
			<p>
				<label>品牌链接：</label>
				<input name="brand_uri" type="text" size="30" maxlength="256"/>
			</p>
			<div  class="clearfix"></div>
			<p>
				<label>图片链接：</label>
				<input name="logo_uri" type="text" size="30" maxlength="256"/>
			</p>
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
//$(document).ready(function(){
//onkeydown="if(13==event.keyCode){return false;}"
//	$("#brand_first").change(function(){
//		var level = $("#brand_first").val();
//		$.post("/common/pms/child-category",{level:level},function(data){
//			$("#brand_second").html('<option value="0">全部</option>'+data);
//		});
//	  //  $("#third").html('<option value="-1">全部</option>');
//	});
//
//	$("#brand_second").change(function(){
//		var level = $("#brand_second").val();
//		$.post("/common/pms/child-category",{level:level},function(data){
//			$("#brand_third").html('<option value=0>全部</option>'+data);
//		});
//	});
//
//	$("#pms_brand_add").validate({
//	submitHandler:function(form){
//
//		var pmin = $("#brand_third").val();
//
//		if(!pmin||pmin=="0"){
//			this.showErrors({
//			  "third_category_id": "三级类目必须选择"
//			});
//			return false;
//		}
//
//		return validateCallback(form, navTabAjaxDone);
//	}
//});
//
//});
</script>
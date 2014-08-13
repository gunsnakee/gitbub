<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form method="post" action="/pms/product/add" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone);">
    <input type="hidden" name="step" value="6">
    <input type="hidden" name="spuId" value="${spu.spuId}">
    <div id="J-imageUris">
    </div>
    <div class="pageFormContent nowrap" layoutH="70">
        <fieldset>
            <legend><span style="color: #ff0000">公共信息</span></legend>
            <dl>
                <dt>SPU ID:</dt>
                <dd>${spu.spuId}</dd>
            </dl>
            <dl>
                <dt>类目:</dt>
                <dd><span>${firstPc.categoryName} &lt;  ${secondPc.categoryName}  &lt;  ${thirdPc.categoryName}</span></dd>
            </dl>
            <dl>
                <dt>商品标题:</dt>
                <dd>${spu.proName}</dd>
            </dl>
        </fieldset>
        <div class="divider"></div>
        <div class="divider"></div>
        <fieldset class="goodsAttr">
            <legend>规格</legend>
            <c:if test="${empty properties}">
                <tr><td style="text-align: center;" ><font color="#dc143c">暂无规格属性数据</font></td></tr>
            </c:if>
            <c:if test="${!empty properties}">
                <c:forEach var="e" varStatus="i" items="${properties}">
                    <dl>
                        <dt>${e.name}<c:if test="${e.isImage == 1}">(异图)</c:if></dt>
                        <dd>
                            <c:if test="${!empty e.proProValue}">
                                <c:forEach var="spv" varStatus="i" items="${e.proProValue}">
                                   ${spv.name};
                                </c:forEach>
                            </c:if>
                        </dd>
                    </dl>
                </c:forEach>
            </c:if>
        </fieldset>
        <fieldset id="uploadImage">
            <legend>上传图片信息</legend>
            <c:if test="${imageFlag == 0}">
                  <dl>
                      <dd class="uploadItem"><a class="button" href="/pms/product/add?step=5&provId=0" target="dialog" height="350" width="1000" id="spv-image-0"><span>上传图片</span></a>&nbsp;&nbsp;&nbsp;&nbsp;</dd>
                  </dl>
            </c:if>
            <c:if test="${imageFlag == 1}">
                <c:forEach var="e" varStatus="i" items="${properties}">
                    <c:if test="${e.isImage == 1}">
                        <input type="hidden" name="proPropId" value="${e.proPropId}">
                        <table class="table" width="1000" layoutH="205" >
                            <thead>
                                <tr>
                                    <c:if test="${!empty e.proProValue}">
                                        <c:forEach var="spa" varStatus="i" items="${e.proProValue}">
                                            <th align="center">${spa.name}</th>
                                        </c:forEach>
                                    </c:if>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <c:if test="${!empty e.proProValue}">
                                        <c:forEach var="spv" varStatus="i" items="${e.proProValue}">
                                            <td align="center"  class="uploadItem"><a href="/pms/product/add?step=5&provId=${spv.id}" target="dialog" height="350" width="1000" id="spv-image-${spv.id}"><span style="color: #1727ff">上传${spv.name}图片</span></a></td>
                                        </c:forEach>
                                    </c:if>
                                </tr>
                            </tbody>
                        </table>
                    </c:if>
                </c:forEach>
            </c:if>
        </fieldset>
    </div>
    <div class="formBar">
        <ul>
            <li><div class="buttonActive"><div class="buttonContent"><button type="submit" onclick="return skuCreate()">创  建</button></div></div></li>
        </ul>
    </div>
</form>
<script type="text/javascript">
  function skuCreate(){
      var validate = true;
      var items = $("#uploadImage .uploadItem");
      for (var i = 0; i < items.size(); i++) {
          var curItemIsValSuc = true;
          var item = items.eq(i);
          var spanItem = item.find("span[class=isComplete]");
          if (spanItem.length<=0) {
              curItemIsValSuc = false;
          }
      }
      if(curItemIsValSuc ==false){
          alertMsg.error("图片未上传完整！");
          validate =false;
      }
      return validate;
  }
</script>

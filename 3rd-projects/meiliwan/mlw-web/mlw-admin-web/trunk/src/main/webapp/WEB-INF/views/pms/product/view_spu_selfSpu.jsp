<%@ page language="java" pageEncoding="UTF-8" %>
<fieldset>
    <legend>自定义SPU参数-前台显示用</legend>
    <dl>
        <dt>商品小编推荐：</dt>
        <dd>
                    <span name="selfSpu" class="view">
                        ${spu.detail.editorRec}
                    </span>
                    <span name="selfSpu" class="input">
                        <textarea name="editorRec" class="required textInput" cols="80" rows="4" minlength="15" maxlength="500" >${spu.detail.editorRec}</textarea>
                    </span>

        </dd>
        <div class="divider"></div>
                <span name="selfSpu" class="view">
                    <c:if test="${!empty spu.psplist}">
                        <dl>
                            <dt>属性名称</dt>
                            <dd>属性值</dd>
                        </dl>
                        <c:forEach var="psp" items="${spu.psplist}">
                            <dl>
                                <dt>${psp.selfPropName}:</dt>
                                <dd>${psp.selfPropValue}</dd>
                            </dl>
                        </c:forEach>
                    </c:if>
                </span>
                <span name="selfSpu" class="input" id="selfSpuDls">
                     <dl>
                         <dt>属性名称</dt>
                         <dd>属性值</dd>
                     </dl>
                    <c:if test="${!empty spu.psplist}">
                        <c:forEach var="psp" items="${spu.psplist}">
                            <dl>
                                <input type="hidden" name="pspid" value="${psp.id}"/>
                                <dt><input name="selfPropName"  type="text" style="width: 110px" value="${psp.selfPropName}"></dt>
                                <dd><input name="selfPropValue" type="text" style="width: 150px" value="${psp.selfPropValue}"></dd>
                            </dl>
                        </c:forEach>
                    </c:if>
                </span>


    </dl>
    <c:if test='${sessionScope.bkstage_user.menus["f6f03aa4654220af64642d5aa03965ed"]!=null}'>
        <span name="selfSpu" class="view"><input  type="button"  name="modify" value="修改"  ref="selfSpu"/></span>
    </c:if>
            <span name="selfSpu" class="input">
                <div  id="update-chideren"><a class="button" href="javascript:;"><span>添加自定义属性</span></a></div><br/>
                <input type="button" name="saveSubmit"  value="保存" submitType="selfSpu"/>
                <input type="button"  name="cancel" value="取消" ref="selfSpu"/>
            </span>
</fieldset>
<script>
    $(function(){
        var spuSelfOne = '<dl><dt><input type="hidden" name="pspid" value="-1"/><input name="selfPropName" type="text" style="width: 110px" /></dt><dd><input name="selfPropValue" type="text" style="width: 150px" /></dd></dl>';

        $("#update-chideren").click(function(e){
            e.preventDefault();
            $(spuSelfOne).appendTo($('#selfSpuDls'));

        });
    });
</script>
<%@ page language="java" pageEncoding="UTF-8" %>
<fieldset id="proTitleField">
    <legend>商品标题</legend>
    <dl>
        <dt>公共标题：</dt>
        <dd>
            <span name="proTitleField" class="view">${spu.proName}</span>
            <span name="proTitleField" class="input"> <input type="text" size="25" name="proName" class="required" value="${spu.proName}" style="width: 300px" minlength="10" maxlength="100"/></span>
        </dd>
    </dl>
    <dl>
        <dt>短标题：</dt>
        <dd>
            <span name="proTitleField" class="view">${spu.shortName}</span>
            <span name="proTitleField" class="input"><input type="text" size="25" name="shortName" class="required" value="${spu.shortName}" style="width: 300px" minlength="5" maxlength="40"/></span>

        </dd>
    </dl>
    <dl>
        <dt>宣传标题：</dt>
        <dd>
            <span name="proTitleField" class="view">${spu.advName}</span>
            <span name="proTitleField" class="input"><input type="text" size="25" name="advName" value="${spu.advName}" style="width: 300px" minlength="5" maxlength="40"/></span>

        </dd>
    </dl>
    <c:if test='${sessionScope.bkstage_user.menus["f6f03aa4654220af64642d5aa03965ed"]!=null}'>
        <span name="proTitleField" class="view"><input  type="button" name="modify"  value="修改" ref="proTitleField" /></span>
    </c:if>
            <span name="proTitleField" class="input">
                <input type="button"  name="saveSubmit" value="保存" submitType="proTitleField"/>
                <input type="button"  name="cancel" value="取消" ref="proTitleField" />
            </span>
</fieldset>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">菜单编辑</h2>

<div class="pageContent">
    <form action="/bkstage/menu/edit" method="post" class="pageForm required-validate"
          onsubmit="return validateCallback(this, navTabAjaxDone)">
        <input type="hidden" value="1" name="handle">
        <input name="parentId" value="${parentMenu.menuId}" type="hidden"/>
        <input name="menuId" value="${obj.menuId}" type="hidden"/>
        <div class="pageFormContent" layoutH="97">
            <fieldset>
                <legend>菜单编辑</legend>
                <dl>
                    <dt>父节点名称：</dt>
                    <dd><input readonly="true" value="${parentMenu.name}" type="text"/></dd>
                </dl>
                <dl>
                    <dt>菜单名称：</dt>
                    <dd><input name="name" type="text" value="${obj.name}" class="required"/></dd>
                </dl>
                <dl>
                    <dt>权限字符串：</dt>
                    <dd><input name="role_key" style="width: 230px;"  value="${obj.roleKey}"  type="text"/></dd>
                </dl>
                <dl>
                    <dt>数据授权字符串：</dt>
                    <dd><input name="authorization" style="width: 230px;"  value="${obj.authorization}"  type="text"/></dd>
                </dl>
                <dl>
                    <dt>目录连接URL：</dt>
                    <dd><input style="width:230px;" name="url"  value="${obj.url}"   type="text"/></dd>
                </dl>
                <dl>
                    <dt>目标(target)：</dt>
                    <dd><input style="width:230px;" name="target"  value="${obj.target}" type="text"/></dd>
                </dl>
                <dl>
                    <dt>菜单类别：</dt>
                    <dd><select name="menu_type">
                        <option value="1" <c:if test="${obj.menuType ==1 }" > selected="selected" </c:if>>菜单</option>
                        <option value="0" <c:if test="${obj.menuType ==0 }" > selected="selected" </c:if>>功能</option>
                    </select></dd>
                </dl>
                <dl>
                    <dt>排序：</dt>
                    <dd><select name="sequence">
                        <option value="10" <c:if test='${obj.sequence==10}' >selected="selected" </c:if> >10</option>
                        <option value="9"  <c:if test='${obj.sequence==9}' >selected="selected" </c:if> >9</option>
                        <option value="8"  <c:if test='${obj.sequence==8}' >selected="selected" </c:if> >6</option>
                        <option value="7" <c:if test='${obj.sequence==7}' >selected="selected" </c:if> >7</option>
                        <option value="6" <c:if test='${obj.sequence==6}' >selected="selected" </c:if> >6</option>
                        <option value="5" <c:if test='${obj.sequence==5}' >selected="selected" </c:if> >5</option>
                        <option value="4" <c:if test='${obj.sequence==4}' >selected="selected" </c:if> >4</option>
                        <option value="3" <c:if test='${obj.sequence==3}' >selected="selected" </c:if> >3</option>
                        <option value="2" <c:if test='${obj.sequence==2}' >selected="selected" </c:if> >2</option>
                        <option value="1" <c:if test='${obj.sequence==1}' >selected="selected" </c:if> >1</option>
                        <option value="0" <c:if test='${obj.sequence==0}' >selected="selected" </c:if> >0</option>
                        <option value="-1" <c:if test='${obj.sequence==-1}' >selected="selected" </c:if>>-1</option>
                        <option value="-2" <c:if test='${obj.sequence==-2}' >selected="selected" </c:if>>-2</option>
                        <option value="-3" <c:if test='${obj.sequence==-3}' >selected="selected" </c:if>>-3</option>
                        <option value="-4" <c:if test='${obj.sequence==-4}' >selected="selected" </c:if>>-4</option>
                        <option value="-5" <c:if test='${obj.sequence==-5}' >selected="selected" </c:if>>-5</option>
                        <option value="-6" <c:if test='${obj.sequence==-6}' >selected="selected" </c:if>>-6</option>
                        <option value="-7" <c:if test='${obj.sequence==-7}' >selected="selected" </c:if>>-7</option>
                        <option value="-8" <c:if test='${obj.sequence==-8}' >selected="selected" </c:if>>-8</option>
                        <option value="-9" <c:if test='${obj.sequence==-9}' >selected="selected" </c:if>>-9</option>
                        <option value="-10" <c:if test='${obj.sequence==-10}' >selected="selected" </c:if>>-10</option>
                    </select></dd>
                </dl>
                <dl>
                    <dt>使用状态：</dt>
                    <dd><select name="state">
                        <option value="0"  <c:if test="${obj.state ==0 }" > selected="selected" </c:if> >启用</option>
                        <option value="-1" <c:if test="${obj.state ==-1 }" > selected="selected" </c:if> >停用</option>
                    </select></dd>
                </dl>
                <dl>
                    <dt>模块：</dt>
                    <dd><select name="model">
                        <option value="bkstage_menu" <c:if test='${obj.model =="bkstage_menu" }' > selected="selected" </c:if> >美丽湾后台管理</option>
                        <option value="cms"  <c:if test='${obj.model =="cms" }' > selected="selected" </c:if> >CMS</option>
                    </select></dd>
                </dl>
                <dl>
                    <dt>描述：</dt>
                    <dd><input name="description" style="width: 230px;"  value="${obj.description}"  type="text"/></dd>
                </dl>
            </fieldset>
        </div>
        <div class="formBar">
            <ul>
                <li>
                    <div class="buttonActive">
                        <div class="buttonContent">
                            <button type="submit">保存</button>
                        </div>
                    </div>
                </li>
                <li>
                    <div class="button">
                        <div class="buttonContent">
                            <button class="close" type="button">关闭</button>
                        </div>
                    </div>
                </li>
            </ul>
        </div>
    </form>
</div>
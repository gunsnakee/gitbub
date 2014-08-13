<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<h2 class="contentTitle">菜单添加</h2>

<div class="pageContent">
    <form action="/bkstage/menu/add" method="post" class="pageForm required-validate"
          onsubmit="return validateCallback(this, navTabAjaxDone)">
        <input type="hidden" value="1" name="handle">
        <input name="parentId" value="${parentMenu.menuId}" type="hidden"/>

        <div class="pageFormContent" layoutH="97">
            <fieldset>
                <legend>菜单添加</legend>
                <dl>
                    <dt>父节点名称：</dt>
                    <dd><input readonly="true" value="${parentMenu.name}" type="text"/></dd>
                </dl>
                <dl>
                    <dt>菜单名称：</dt>
                    <dd><input name="name" type="text" class="required"/></dd>
                </dl>
                <dl>
                    <dt>权限Str(随机唯一)：</dt>
                    <dd><input name="role_key" style="width: 230px;"  value="${uuid}"  type="text"/></dd>
                </dl>
                <dl>
                    <dt>数据授权Str：</dt>
                    <dd><input name="authorization" style="width: 230px;" type="text"/></dd>
                </dl>
                <dl>
                    <dt>目录连接URL：</dt>
                    <dd><input style="width:230px;" name="url" type="text"/></dd>
                </dl>
                <dl>
                    <dt>目标(target)：</dt>
                    <dd><input style="width:230px;" name="target" type="text"/></dd>
                </dl>
                <dl>
                    <dt>菜单类别：</dt>
                    <dd><select name="menu_type">
                        <option value="1">菜单</option>
                        <option value="0">功能</option>
                    </select></dd>
                </dl>
                <dl>
                    <dt>排序：</dt>
                    <dd><select name="sequence">
                        <option value="10">10</option>
                        <option value="9">9</option>
                        <option value="8">6</option>
                        <option value="7">7</option>
                        <option value="6">6</option>
                        <option value="5">5</option>
                        <option value="4">4</option>
                        <option value="3">3</option>
                        <option value="2">2</option>
                        <option value="1">1</option>
                        <option selected="selected" value="0">0</option>
                        <option value="-1">-1</option>
                        <option value="-2">-2</option>
                        <option value="-3">-3</option>
                        <option value="-4">-4</option>
                        <option value="-5">-5</option>
                        <option value="-6">-6</option>
                        <option value="-7">-7</option>
                        <option value="-8">-8</option>
                        <option value="-9">-9</option>
                        <option value="-10">-10</option>
                    </select></dd>
                </dl>
                <dl>
                    <dt>使用状态：</dt>
                    <dd><select name="state">
                        <option value="0">启用</option>
                        <option value="-1">停用</option>
                    </select></dd>
                </dl>
                <dl>
                    <dt>模块：</dt>
                    <dd><select name="model">
                        <option value="bkstage_menu">美丽湾后台管理</option>
                        <option value="cms">CMS</option>
                    </select></dd>
                </dl>
                <dl>
                    <dt>描述：</dt>
                    <dd><input name="description" style="width: 230px;" type="text"/></dd>
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
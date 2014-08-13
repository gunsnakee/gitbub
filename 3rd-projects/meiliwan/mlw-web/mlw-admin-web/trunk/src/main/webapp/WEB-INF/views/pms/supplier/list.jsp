<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/pms/supplier/list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
    <input type="hidden" name="name" value="${name}"/>
    <input type="hidden" name="linkman" value="${linkman}"/>
    <input type="hidden" name="phone" value="${phone}"/>
    <input type="hidden" name="otherPhone" value="${otherPhone}"/>
</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/pms/supplier/list" method="post" id="queryForm">
        <div class="searchBar">
            <table class="searchContent pageFormContent">
                <tr>
                    <td>
                        <label>供应商名:</label>
                        <input name="name" id="name" <c:if test="${!empty name}">value="${name}"</c:if> />
                    </td>
                    <td>
                        <label>联系人名:</label>
                        <input name="linkman" id="linkman" <c:if test="${!empty linkman}">value="${linkman}"</c:if> />
                    </td>
                </tr>

                <tr>
                    <td>
                        <label>联系电话:</label>
                        <input name="phone" id="phone" <c:if test="${!empty phone}">value="${phone}"</c:if> />
                    </td>
                    <td>
                        <label>其他电话:</label>
                        <input name="otherPhone" id="otherPhone" <c:if test="${!empty otherPhone}">value="${otherPhone}"</c:if> />
                    </td>
                </tr>
                <tr>
                    <td><label>经营方式:</label>
                        <select name="operateType" id="operateType" class="required">
                            <option value="" >全部</option>
                            <option value="1" <c:if test="${operateType==1}">selected="selected"</c:if>>购销</option>
                            <option value="2" <c:if test="${operateType==2}">selected="selected"</c:if>>代销</option>
                            <option value="3" <c:if test="${operateType==3}">selected="selected"</c:if>>联营</option>
                        </select>
                    </td><td></td>
                    <td>
                        <div class="subBar">
                            <div class="buttonActive"><div class="buttonContent"><button type="submit">筛选</button></div> </div>
                            <div class="buttonActive"><div class="buttonContent" id="revertSelectSupplier"><button type="button">重置</button></div></div>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </form>
</div>

<div class="pageContent">
    <form action="/pms/supplier/del" id="supplierForm" method="post" class="pageForm" onsubmit="return validateCallback(this, navTabAjaxDone)">
        <input type="hidden" id="supplierStatus" name="state" value="">
        <div class="panelBar">
            <ul class="toolBar">
                <li><a title="添加供应商！" href="/pms/supplier/add?handle=1" height="320" rel="64"  target="dialog" class="add"><span>添加供应商</span></a></li>
                <li><a title="批量删除供应商！" href="javascript:void(0);"  rel="ids" val="-1" class="delete"><span>删除供应商</span></a></li>
                <li><a title="批量恢复删除的供应商！" href="javascript:void(0);"  rel="ids" val="0" class="delete"><span>恢复供应商</span></a></li>
            </ul>
        </div>
        <table class="table" width="100%" layoutH="180" id="J-table">
            <thead>
            <tr>
                <th><input type="checkbox" group="ids" class="checkboxCtrl"></th>
                <th align="center" width="8%">序号</th>
                <th align="center" width="24%">供应商名称</th>
                <th align="center" width="5%">经营方式</th>
                <th align="center" width="8%">联系人</th>
                <th align="center" width="10%">联系电话</th>
                <th align="center" width="14%">其他电话</th>
                <th align="center" width="13%">创建时间</th>
                <th align="center" width="8%">是否删除</th>
                <th align="center" width="10%">操作</th>
            </tr>
            </thead>
            <tbody>
            <c:if test="${empty pc.entityList}">
                <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
            </c:if>
            <c:if test="${!empty pc.entityList}">
                <c:forEach var="e" items="${pc.entityList}">
                    <tr  target="adminId" rel="${e.supplierId}">
                        <td><input name="ids" value="${e.supplierId}" type="checkbox" /></td>
                        <td>${e.supplierId}</td>
                        <td align="center" style=" overflow:hidden;word-break: break-all; word-wrap: break-word;">${e.supplierName}</td>
                        <td>
                            <c:if test="${e.operateType==1}">购销</c:if>
                            <c:if test="${e.operateType==2}">代销</c:if>
                            <c:if test="${e.operateType==3}">联营</c:if>
                        </td>
                        <td>${e.supplierLinkman}</td>
                        <td>${e.supplierPhone}</td>
                        <td>${e.supplierOtherPhone}</td>
                        <td><fmt:formatDate value="${e.createTime}" pattern="yyyy-MM-dd HH:mm:ss"  /></td>
                        <td>
                            <c:if test="${e.state==-1}"><a target="ajaxTodo" title="标记为未删除" href="/pms/supplier/del?ids=${e.supplierId}&state=0"><span style="color: #ff0000">已删除</span></a></c:if>
                            <c:if test="${e.state==0}"><a target="ajaxTodo" title="标记为已删除" href="/pms/supplier/del?ids=${e.supplierId}&state=-1"><span style="color: #0000ff">未删除</span></a></c:if>
                        </td>
                        <td>
                            <a title="查看供应商卖出的商品" target="navTab" href="/pms/supplier/sellList?supplierId=${e.supplierId}" class="btnView" >查看供应商卖出的商品</a>
                            <a title="修改详情" target="dialog" href="/pms/supplier/update?id=${e.supplierId}" class="btnEdit" val="${e.supplierId}">修改详情</a>
                            <c:if test="${e.state==0}"><a title="删除供应商" target="ajaxTodo" href="/pms/supplier/del?ids=${e.supplierId}&state=-1" class="btnDel" val="${entity.id}">删除供应商</a></c:if>
                            <c:if test="${e.state==-1}"><a title="恢复删除的供应商" target="ajaxTodo" href="/pms/supplier/del?ids=${e.supplierId}&state=0" class="btnSelect" val="${entity.id}">恢复供应商</a></c:if>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
            </tbody>
        </table>
    </form>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>
<script>
    $(document).ready(function(){
        $(".delete").click(function(){
            $("#supplierStatus").val($(this).attr("val"));
            alertMsg.confirm('确实要执行?' , {
                okCall: function(){
                    $("#supplierForm").submit();
                }
            });
        });

        $("#revertSelectSupplier").click(function(){
            $("#name").val("");
            $("#linkman").val("");
            $("#phone").val("");
            $("#otherPhone").val("");
            $("#operateType").val('<option value="">全部</option><option value="1">购销</option><option value="2" >代销</option><option value="3" >联营</option>');
        });
    });
</script>
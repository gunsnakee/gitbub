<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/pms/place/list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
    <input type="hidden" name="id" value="${id}"/>
</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/pms/place/list" method="post" id="queryForm">
        <div class="searchBar">
            <table class="searchContent pageFormContent">
                <tr>
                    <td>
                        序号:
                        <input name="id" id="adminName" class="digits" <c:if test="${!empty id}">value="${id}"</c:if> />
                    </td>
                    <td>
                        <div class="subBar">
                            <div class="buttonActive">
                                <div class="buttonContent">
                                    <button type="submit">筛选</button>
                                </div>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </form>
</div>

<div class="pageContent">
    <form action="/pms/place/del" id="placeForm" method="post" class="pageForm" onsubmit="return validateCallback(this, navTabAjaxDone)">
        <input type="hidden" id="placeStatus" name="state" value="">
        <div class="panelBar">
            <ul class="toolBar">
                <li><a title="添加产地！" href="/pms/place/add?handle=1" rel="64"  target="dialog" class="add"><span>添加产地</span></a></li>
                <li><a title="批量删除产地！" href="javascript:void(0);"  rel="ids" val="-1" class="delete"><span>删除产地</span></a></li>
                <li><a title="批量恢复删除的产地！" href="javascript:void(0);"  rel="ids" val="0" class="delete"><span>恢复产地</span></a></li>
            </ul>
        </div>
        <table class="table" width="100%" layoutH="130" id="J-table">
            <thead>
            <tr>
                <th align="center" width="2%"><input type="checkbox" group="ids" class="checkboxCtrl"></th>
                <th align="center" width="10%">序号</th>
                <th align="center" width="18%">产地名称</th>
                <th align="center" width="18%">产地英文名</th>
                <th align="center" width="18%">扩展名称</th>
                <th align="center" width="14%">添加时间</th>
                <th align="center" width="8%">是否删除</th>
                <th align="center" width="8%">操作</th>
            </tr>
            </thead>
            <tbody>
            <c:if test="${empty pc.entityList}">
                <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
            </c:if>
            <c:if test="${!empty pc.entityList}">
                <c:forEach var="e" items="${pc.entityList}">
                    <tr  target="adminId" rel="${e.placeId}">
                        <td><input name="ids" value="${e.placeId}" type="checkbox" /></td>
                        <td>${e.placeId}</td>
                        <td>${e.placeName}</td>
                        <td>${e.enName}</td>
                        <td>${e.otherName}</td>
                        <td><fmt:formatDate value="${e.createTime}" pattern="yyyy-MM-dd HH:mm:ss"  /></td>
                        <td>
                            <c:if test="${e.isDel==-1}"><a target="ajaxTodo" title="标记为未删除" href="/pms/place/del?ids=${e.placeId}&state=0"><span style="color: #ff0000">已删除</span></a></c:if>
                            <c:if test="${e.isDel==0}"><a target="ajaxTodo" title="标记为已删除" href="/pms/place/del?ids=${e.placeId}&state=-1"><span style="color: #0000ff">未删除</span></a></c:if>
                        </td>
                        <td>
                            <a title="修改详情" target="dialog" href="/pms/place/update?id=${e.placeId}" class="btnEdit" val="${e.placeId}">修改详情</a>
                            <c:if test="${e.isDel==0}"><a title="删除产地" target="ajaxTodo" href="/pms/place/del?ids=${e.placeId}&state=-1" class="btnDel" val="${entity.id}">删除产地</a></c:if>
                            <c:if test="${e.isDel==-1}"><a title="恢复删除的产地" target="ajaxTodo" href="/pms/place/del?ids=${e.placeId}&state=0" class="btnSelect" val="${entity.id}">恢复产地</a></c:if>
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
            $("#placeStatus").val($(this).attr("val"));
            alertMsg.confirm('确实要执行?' , {
                okCall: function(){
                    $("#placeForm").submit();
                }
            });
        });
    });
</script>
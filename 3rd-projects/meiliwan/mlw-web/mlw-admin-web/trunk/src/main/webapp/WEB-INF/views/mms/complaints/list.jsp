<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/mms/complaints/list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}">
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}">
    <input type="hidden" name="state" value="${state}">
    <input type="hidden" name="complaintsId" value="${compId}">
    <input type="hidden" name="complaintsType" value="${compType}">
    <input type="hidden" name="orderId" value="${orderId}">
    <input type="hidden" name="nickName" value="${nickName}">
    <input type="hidden" name="orderField" value="${pc.pageInfo.orderField}" />
    <input type="hidden" name="orderDirection" value="${pc.pageInfo.orderDirection}" />
</form>
<div class="pageHeader">
    <form ref="pagerForm" class="required-validate" onsubmit="return navTabSearch(this);" action="/mms/complaints/list" method="post">
        <div class="searchBar">
            <table class="searchContent">
                <tr>
                    <td>
                        <label>投诉编号</label><input type="text" size="15"class="digits"  name="complaintsId" id="complaintsId" <c:if test="${!empty compId}">value="${compId}"</c:if>>
                    </td>
                    <td>
                        <label>订单编号</label><input type="text" size="15"class="digits"  name="orderId" id="orderIdComplaints" <c:if test="${!empty orderId}">value="${orderId}"</c:if>>
                    </td>
                    <td>
                        <label>投诉发起人</label><input type="text" size="15" name="nickName" id="nickNameComplaints" <c:if test="${!empty nickName}">value="${nickName}"</c:if>>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label>投诉类型</label>
                        <select id="complaintsType" name="complaintsType">
                            <option value="-1">全部</option>
                            <option value="1" <c:if test="${compType==1}">selected="selected"</c:if>>订单问题</option>
                            <option value="2" <c:if test="${compType==2}">selected="selected"</c:if>>物流问题</option>
                            <option value="3" <c:if test="${compType==3}">selected="selected"</c:if>>售后问题</option>
                            <option value="4" <c:if test="${compType==4}">selected="selected"</c:if>>其他与建议</option>
                        </select>
                    </td>
                    <td>
                        <label>投诉状态</label>
                        <select id="stateComplaints" name="state">
                            <option value="-1">全部</option>
                            <option value="0" <c:if test="${state==0}">selected="selected"</c:if>>已取消</option>
                            <option value="1" <c:if test="${state==1}">selected="selected"</c:if>>未回复</option>
                            <option value="2" <c:if test="${state==2}">selected="selected"</c:if>>已回复</option>
                        </select>
                    </td>
                    <td>
                        <div class="subBar">
                            <ul>
                                <li>
                                    <div class="buttonActive"><div class="buttonContent"><button type="submit">筛选</button></div></div>
                                    <div class="buttonActive"><div class="buttonContent" id="revertSelectComplaints"><button type="button">重置</button></div></div>
                                </li>
                            </ul>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </form>
</div>
<div class="pageContent">
    <form action="/mms/complaints/update" id="complaintsForm" method="post" class="pageForm" onsubmit="return validateCallback(this, navTabAjaxDone)">
        <input type="hidden" name="handle" id="complaintsState" value=""/>
        <div class="panelBar">
            <ul class="toolBar">
                <li><a title="标记一条或多条为买家取消"  rel="ids" val="1" class="edit updateComplaints"><span>取消投诉</span></a></li>
                <li><a title="标记一条或多条为买家不取消"  rel="ids" val="2" class="edit updateComplaints"><span>恢复取消</span></a></li>
                <li><a title="标记一条或多条为买家已删除"  rel="ids" val="5" class="delete updateComplaints"><span>删除投诉</span></a></li>
                <li><a title="恢复一条或多条买家已删除的投诉"  rel="ids" val="6" class="delete updateComplaints"><span>恢复删除的投诉</span></a></li>
            </ul>
        </div>
        <table class="table" width="100%"  layoutH="135">
            <thead>
            <tr align="center">
                <th width="2%"><input type="checkbox" group="ids" class="checkboxCtrl"></th>
                <th width="8%">投诉编号</th>
                <th width="6%">投诉类型</th>
                <th width="15%">投诉内容</th>
                <th width="10%">投诉人</th>
                <th width="8%">投诉订单</th>
                <th width="12%"  orderField="create_time" class="<c:if test='${pc.pageInfo.orderField=="create_time"}'>${pc.pageInfo.orderDirection}</c:if>">投诉时间</th>
                <th width="8%">联系方式</th>
                <th width="14%">回复内容</th>
                <th width="5%">是否取消</th>
                <th width="6%">是否删除</th>
            </tr>
            </thead>
            <tbody>
            <c:if test="${empty pc.entityList}">
                <tr>
                    <td style="text-align: center;"><font color="#dc153c">暂无数据</font></td>
                </tr>
            </c:if>
            <c:if test="${!empty pc.entityList}">
                <c:forEach var="entity" items="${pc.entityList}">
                    <tr align="center">
                        <td><input name="ids" value="${entity.id}" type="checkbox"></td>
                        <td title="${entity.id}">${entity.id}</td>
                        <td>
                            <c:choose>
                                <c:when test="${entity.complaintsType == 1}">
                                    订单问题
                                </c:when>
                                <c:when test="${entity.complaintsType == 2}">
                                    物流问题
                                </c:when>
                                <c:when test="${entity.complaintsType == 3}">
                                    售后问题
                                </c:when>
                                <c:when test="${entity.complaintsType == 4}">
                                    其他与建议
                                </c:when>
                            </c:choose>
                        </td>
                        <td style=" overflow:hidden;word-break: break-all; word-wrap: break-word;"><a target="dialog" height="630" title="查看详情" href="/mms/complaints/detail?id=${entity.id}"><span style="color: #ff0000">${entity.content}</span> </a></td>
                        <td title="${entity.nickName}">${entity.nickName}</td>
                        <td title="${entity.orderId}">${entity.orderId}</td>
                        <td title="<fmt:formatDate value="${entity.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>"><fmt:formatDate value="${entity.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                        <td title="${entity.contactInfo}">${entity.contactInfo}</td>
                        <td style=" overflow:hidden;word-break: break-all; word-wrap: break-word;">
                            <c:if test="${empty entity.replyContent && entity.state!=-1}"><a target="dialog" width="600" height="340" title="回复投诉？" href="/mms/complaints/reply?id=${entity.id}&handle=0"><span style="color: #ff0000">未回复</span> </a></c:if>
                            <c:if test="${!empty entity.replyContent}">${entity.replyContent}</c:if>
                        </td>
                        <td>
                            <c:if test="${entity.state==-1}"><a target="ajaxTodo" title="改为未取消？" href="/mms/complaints/update?ids=${entity.id}&handle=2"><span style="color: #ff0000">已取消</span> </a></c:if>
                            <c:if test="${entity.state==0}"><a target="ajaxTodo" title="改为已取消？" href="/mms/complaints/update?ids=${entity.id}&handle=1"><span style="color: #0000ff">未取消</span> </a></c:if>
                            <c:if test="${entity.state==1}">已处理</c:if>
                        </td>
                        <td>
                            <c:if test="${entity.isUserDel==0}"><a target="ajaxTodo" title="改为用户已删除？" href="/mms/complaints/update?ids=${entity.id}&handle=5"><span style="color: #0000ff">未删除</span></a></c:if>
                            <c:if test="${entity.isUserDel==-1}"><a target="ajaxTodo" title="改为用户未删除？" href="/mms/complaints/update?ids=${entity.id}&handle=6"><span style="color: #ff0000">已删除</span></a></c:if>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
            </tbody>
        </table>
    </form>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $(".updateComplaints").click(function () {
            $("#complaintsState").val($(this).attr("val"));
            alertMsg.confirm('确实要执行?', {
                okCall: function () {
                    $("#complaintsForm").submit();
                }
            });
        });

        $("#revertSelectComplaints").click(function(){
            $("#complaintsId").val("");
            $("#orderIdComplaints").val("");
            $("#nickNameComplaints").val("");
            $("#complaintsType").html("<option value='-1'>全部</option><option value='0'>订单问题</option><option value='1'>物流问题</option><option value='2'>售后问题</option><option value='3'>其他与建议</option>");
            $("#stateComplaints").html("<option value='-1'>全部</option><option value='0'>已取消</option><option value='1'>未回复</option><option value='2'>已回复</option>");
        });
    });
</script>
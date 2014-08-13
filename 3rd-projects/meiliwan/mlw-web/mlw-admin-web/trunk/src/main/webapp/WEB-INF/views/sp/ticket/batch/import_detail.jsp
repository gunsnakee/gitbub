<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
    <div class="pageFormContent" width="900" layouth="60">
        <fieldset>
            <legend>导入明细</legend>
            <table class="table" width="900" id="J-table">
                <thead>
                    <tr>
                        <th style="text-align: center">券批次号</th>
                        <th style="text-align: center">券名称</th>
                        <th style="text-align: center">券面值</th>
                        <th style="text-align: center">导入时间</th>
                        <th style="text-align: center">导入数量</th>
                        <th style="text-align: center">操作人</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td style="text-align: center">${batch.batchId}</td>
                        <td style="text-align: center">${batch.ticketName}</td>
                        <td style="text-align: center">${batch.ticketPrice}</td>
                        <td style="text-align: center"><fmt:formatDate value="${result.createTime}" pattern="yyyy-MM-dd HH:mm:ss"  /></td>
                        <td style="text-align: center">${result.totalNum}</td>
                        <td style="text-align: center">${result.adminName}</td>
                    </tr>
                </tbody>
            </table>
        </fieldset>
        <br>
        <fieldset>
            <legend>导入结果明细：</legend>
            <dl class="nowrap">
                <dt>成功数量:</dt>
                <dd>${result.sucNum}    <a href="javascript:void(0);" id="ticketSucSend"><span style="color: #0000ff">查看</span></a></dd>
            </dl>
            <dl class="nowrap">
                <dt>美丽湾账号不匹配数:</dt>
                <dd>${result.dismatchNun}    <a href="javascript:void(0);" id="ticketErrorSend"><span style="color: #0000ff"> 查看</span></a></dd>
            </dl>
        </fieldset>
        <br>
        <br>
        <div id="ticketErrorNum">
            <fieldset>
                <legend>发送的美丽湾账户不存在，不可发送，数量(${result.dismatchNun})</legend>
                <br>
                <table class="table" width="900">
                    <thead>
                    <tr>
                        <th style="text-align: center">序号</th>
                        <th style="text-align: center">发送登录账户</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${empty errorList}">
                        <tr><td style="text-align: center" ><font color="#dc143c">暂无数据</font></td></tr>
                    </c:if>
                    <c:if test="${!empty errorList}">
                        <c:forEach var="e" varStatus="i" items="${errorList}">
                            <tr>
                                <td style="text-align: center">${i.index+1}</td>
                                <td style="text-align: center"><c:if test="${empty e.userName}">--</c:if><c:if test="${!empty e.userName}">${e.userName}</c:if></td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    </tbody>
                </table>
            </fieldset>
        </div>
        <br>
        <br>
        <div id="ticketSucNum">
            <fieldset>
                <legend>发送成功数：成功数量(${result.sucNum})</legend>
                <br>
                <table class="table" width="900">
                    <thead>
                    <tr>
                        <th style="text-align: center">序号</th>
                        <th style="text-align: center">发送手机</th>
                        <th style="text-align: center">发送邮箱</th>
                        <th style="text-align: center">发送登录账号</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${empty sucList}">
                        <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
                    </c:if>
                    <c:if test="${!empty sucList}">
                        <c:forEach var="e" varStatus="i" items="${sucList}">
                            <tr>
                                <td style="text-align: center">${i.index+1}</td>
                                <td style="text-align: center"><c:if test="${empty e.buyerPhone}">--</c:if><c:if test="${!empty e.buyerPhone}">${e.buyerPhone}</c:if></td>
                                <td style="text-align: center"><c:if test="${empty e.buyerEmail}">--</c:if><c:if test="${!empty e.buyerEmail}">${e.buyerEmail}</c:if></td>
                                <td style="text-align: center"><c:if test="${empty e.userName}">--</c:if><c:if test="${!empty e.userName}">${e.userName}</c:if></td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    </tbody>
                </table>
            </fieldset>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $("#ticketSucSend").click(function () {
            $("#ticketSucNum").show();
            $("#ticketErrorNum").hide();
        });
        $("#ticketErrorSend").click(function () {
            $("#ticketSucNum").hide();
            $("#ticketErrorNum").show();
        });
    });
</script>

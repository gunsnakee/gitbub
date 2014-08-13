<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
    <div class="pageFormContent" width="900" layouth="60">
        <fieldset>
            <legend>导入明细</legend>
            <table class="table" width="900" id="J-table">
                <thead>
                    <tr>
                        <th style="text-align: center">导入批次号</th>
                        <th style="text-align: center">文件名称</th>
                        <th style="text-align: center">导入时间</th>
                        <th style="text-align: center">操作人</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td style="text-align: center">${result[0].batchId}</td>
                        <td style="text-align: center">${result[0].fileName}</td>
                        <td style="text-align: center"><fmt:formatDate value="${result[0].createTime}" pattern="yyyy-MM-dd HH:mm:ss"  /></td>
                        <td style="text-align: center">${result[0].adminName}</td>
                    </tr>
                </tbody>
            </table>
        </fieldset>
        <br>
        <fieldset>
            <legend>导入结果：</legend>
            <c:if test="${empty result}">
                <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
            </c:if>
            <c:if test="${!empty result}">
                <c:forEach var="rt" items="${result}">
                    <c:if test="${rt.cardType == 0}">
                        <br><span>实体卡共导入${rt.totalNum}条数据，成功导入${rt.totalNum - rt.errorNum - rt.dismatchNum}条，不匹配数<a href="javascript:void(0);" id="LP-mismatch"><span style="color: red"> ${rt.dismatchNum} (查看)</span></a></span>
                    </c:if>
                    <c:if test="${rt.cardType == 1}">
                        <br><span>电子卡共导入${rt.totalNum}条数据，成功导入${rt.totalNum - rt.errorNum - rt.dismatchNum}条，不匹配数<a href="javascript:void(0);" id="EP-mismatch"><span style="color: red"> ${rt.dismatchNum} (查看)</span></a>，发送成功${rt.sendSucNum}条，发送失败 <a href="javascript:void(0);" id="EP-sendError"><span style="color: red"> ${rt.sendErrorNum} (查看)</span></a></span>
                    </c:if>
                </c:forEach>
            </c:if>
        </fieldset>
        <br>
        <br>
        <div id="LP-mismatch-table">
            <fieldset>
                <legend>实体卡不匹配列表：</legend>
                <br>
                <table class="table" width="900">
                    <thead>
                    <tr>
                        <th style="text-align: center">礼品卡卡号</th>
                        <th style="text-align: center">礼品卡类型</th>
                        <th style="text-align: center">购买者账户</th>
                        <th style="text-align: center">销售人姓名</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${empty logs}">
                        <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
                    </c:if>
                    <c:if test="${!empty logs}">
                        <c:forEach var="e" items="${logs}">
                            <c:if test="${e.cardType == 0 && e.state == 2 }">
                                <tr>
                                    <td style="text-align: center">${e.cardId}</td>
                                    <td style="text-align: center">实体卡</td>
                                    <td style="text-align: center">${e.buyerName}</td>
                                    <td style="text-align: center">${e.sellerName}</td>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </c:if>
                    </tbody>
                </table>
            </fieldset>
        </div>
        <br>
        <br>
        <div id="EP-mismatch-table">
            <fieldset>
                <legend>电子卡不匹配列表：</legend>
                <br>
                <table class="table" width="900">
                    <thead>
                    <tr>
                        <th style="text-align: center">礼品卡卡号</th>
                        <th style="text-align: center">礼品卡类型</th>
                        <th style="text-align: center">购买者账户</th>
                        <th style="text-align: center">销售人姓名</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${empty logs}">
                        <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
                    </c:if>
                    <c:if test="${!empty logs}">
                        <c:forEach var="e" items="${logs}">
                            <c:if test="${e.cardType == 1 && e.state == 2 }">
                                <tr>
                                    <td style="text-align: center">${e.cardId}</td>
                                    <td style="text-align: center">电子卡</td>
                                    <td style="text-align: center">${e.buyerName}</td>
                                    <td style="text-align: center">${e.sellerName}</td>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </c:if>
                    </tbody>
                </table>
            </fieldset>
        </div>
        <br>
        <br>
        <div id="EP-sendError-table">
            <fieldset>
                <legend>电子卡发送失败列表</legend>
                <br>
                <table class="table" width="900">
                    <thead>
                    <tr>
                        <th style="text-align: center">礼品卡批次号</th>
                        <th style="text-align: center">礼品卡类型</th>
                        <th style="text-align: center">礼品卡卡号</th>
                        <th style="text-align: center">礼品卡名称</th>
                        <th style="text-align: center">面额</th>
                        <th style="text-align: center">购买者账户</th>
                        <th style="text-align: center">销售人姓名</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${empty logs}">
                        <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
                    </c:if>
                    <c:if test="${!empty logs}">
                        <c:forEach var="e" items="${logs}">
                            <c:if test="${e.state == 1 && e.isSend == 0}">
                                <tr>
                                    <td style="text-align: center">${e.cardBatchId}</td>
                                    <td style="text-align: center">电子卡</td>
                                    <td style="text-align: center">${e.cardId}</td>
                                    <td style="text-align: center">${e.cardName}</td>
                                    <td style="text-align: center">${e.cardPrice}</td>
                                    <td style="text-align: center">${e.buyerName}</td>
                                    <td style="text-align: center">${e.sellerName}</td>
                                </tr>
                            </c:if>

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
        $("#LP-mismatch").click(function () {
            $("#LP-mismatch-table").show();
            $("#EP-mismatch-table").hide();
            $("#EP-sendError-table").hide();
        });
        $("#EP-mismatch").click(function () {
            $("#LP-mismatch-table").hide();
            $("#EP-mismatch-table").show();
            $("#EP-sendError-table").hide();
        });

        $("#EP-sendError").click(function () {
            $("#LP-mismatch-table").hide();
            $("#EP-mismatch-table").hide();
            $("#EP-sendError-table").show();
        });
    });
</script>

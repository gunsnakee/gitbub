<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
    <div class="pageFormContent" width="900" layouth="60">
        <fieldset>
            <legend>导入库存明细</legend>
            <table class="table" width="900" id="J-table">
                <thead>
                <tr>
                    <th style="text-align: center">导入批次号</th>
                    <th style="text-align: center">文件名称</th>
                    <th style="text-align: center">导入库存时间</th>
                    <th style="text-align: center">操作人</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td style="text-align: center">${result.batchNo}</td>
                    <td style="text-align: center">${result.fileName}</td>
                    <td style="text-align: center"><fmt:formatDate value="${result.importTime}" pattern="yyyy-MM-dd HH:mm:ss"  /></td>
                    <td style="text-align: center">${result.adminName}</td>
                </tr>
                </tbody>
            </table>
            <fieldset>
                <br>
                 <span>导入结果：导入商品 ${result.totalNum} 条，成功条数 ${result.totalNum-result.mismatchNum-result.errorNum} 条，未找到匹配商品<a href="javascript:void(0);" id="mismatch"><span style="color: red"> ${result.mismatchNum}条 (点击查看)</span></a>，库存不足商品<a href="javascript:void(0);" id="shortstock"><span style="color: red"> ${result.stockShortNum}条 (点击查看)</span></a></span>
            </fieldset>
        </fieldset>
        <br>
        <div id="mismatch-table">
            <fieldset>
                <legend>未匹配商品${result.mismatchNum}条如下：</legend>
                <br>
                <table class="table" width="900">
                    <thead>
                    <tr>
                        <th style="text-align: center">商品条形码</th>
                        <th style="text-align: center">库存变化数</th>
                        <th style="text-align: center">问题类型</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${empty list}">
                        <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
                    </c:if>
                    <c:if test="${!empty list}">
                        <c:forEach var="e" items="${list}">
                            <c:if test="${e.state == -1}">
                                <tr>
                                    <td style="text-align: center">${e.barCode}</td>
                                    <td style="text-align: center">${e.changeStock}</td>
                                    <td style="text-align: center">${e.descp}</td>
                                </tr>
                            </c:if>

                        </c:forEach>
                    </c:if>
                    </tbody>
                </table>
            </fieldset>
        </div>

        <br>
        <div id="short-table">
            <fieldset>
                <legend>库存不足商品${result.stockShortNum}条如下</legend>
                <br>
                <table class="table" width="900">
                    <thead>
                    <tr>
                        <th style="text-align: center">商品条形码</th>
                        <th style="text-align: center">原库存数</th>
                        <th style="text-align: center">库存变化数</th>
                        <th style="text-align: center">现库存数</th>
                        <th style="text-align: center">问题类型</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${empty list}">
                        <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
                    </c:if>
                    <c:if test="${!empty list}">
                        <c:forEach var="e" items="${list}">
                            <c:if test="${e.state == 0}">
                                <tr>
                                    <td style="text-align: center">${e.barCode}</td>
                                    <td style="text-align: center">${e.originalStock}</td>
                                    <td style="text-align: center">${e.changeStock}</td>
                                    <td style="text-align: center">${e.nowStock}</td>
                                    <td style="text-align: center">${e.descp}</td>
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
        $("#mismatch").click(function () {
            $("#mismatch-table").show();
            $("#short-table").hide();
        });
        $("#shortstock").click(function () {
            $("#short-table").show();
            $("#mismatch-table").hide();
        });
    });
</script>

<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/card/card/list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
    <input type="hidden" name="orderField" value="${pc.pageInfo.orderField}"/>
    <input type="hidden" name="orderDirection" value="${pc.pageInfo.orderDirection}"/>
</form>
<form id="getCountCardList" action="/card/card/get-count-export" style="display: none;" target="_blank">
</form>
<form id="getCardExport" action="/card/card/export" style="display: none;" target="_blank">
</form>
<div class="pageHeader">
    <form rel="pagerForm" onsubmit="return navTabSearch(this);" action="/card/card/list" method="post"id="card_list_queryForm">
        <input type="hidden" name="isDel" value="${card.isDel}" id="isDel">
        <table class="searchContent pageFormContent" style="padding:0px;">
            <tr>
                <td><label>批次号:</label></td>
                <td><input name="batchId" value="${card.batchId}"/>

                <td><label>绑定账户:</label></td>
                <td><input name="userName" value="${card.userName}"/>

                <td><label>有效期开始时间：</label></td>
                <td>
                    <input type="text" name="createTimeMin" value="${card.createTimeMin}" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
                    至
                    <input type="text" name="createTimeMax" value="${card.createTimeMax}" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
                </td>
            </tr>
            <tr>
                <td><label>礼品卡卡号:</label></td>
                <td><input name="cardId" value="${card.cardId}"/></td>
                <td><label>销售领取人:</label></td>
                <td><input name="sellerName" value="${card.sellerName}"/></td>
                <td>
                    <label>有效期结束时间：</label>
                </td>
                <td>
                    <input type="text" name="endTimeMin" value="${card.endTimeMin}" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
                    至
                    <input type="text" name="endTimeMax" value="${card.endTimeMax}" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
                </td>
            </tr>
            <tr>
                <td><label>冻结状态:</label></td>
                <td>
                    <select name="isFreeze">
                        <option value="99" <c:if test="${card.isFreeze == 99}"> selected="selected"</c:if>>全部</option>
                        <option value="0" <c:if test="${card.isFreeze == 0}"> selected="selected"</c:if>>正常</option>
                        <option value="1" <c:if test="${card.isFreeze == 1}"> selected="selected"</c:if>>已冻结</option>
                    </select>
                </td>

                <td><label>销售状态:</label></td>
                <td>
                    <select name="isSell">
                        <option value="99" <c:if test="${card.isSell == 99}"> selected="selected"</c:if>>全部</option>
                        <option value="0" <c:if test="${card.isSell == 0}"> selected="selected"</c:if>>未销售</option>
                        <option value="1" <c:if test="${card.isSell == 1}"> selected="selected"</c:if>>已销售</option>
                    </select>
                </td>

                <td><label>用户激活时间：</label></td>
                <td>
                    <input type="text" name="activeTimeMin" value="${card.activeTimeMin}" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
                    至
                    <input type="text" name="activeTimeMax" value="${card.activeTimeMax}" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" readonly="true" style="float:none"/>
                </td>
            </tr>
            <tr>
                <td><label>激活状态:</label></td>
                <td>
                    <select name="state">
                        <option value="99" <c:if test="${card.state == 99}"> selected="selected"</c:if>>全部</option>
                        <option value="0" <c:if test="${card.state == 0}"> selected="selected"</c:if>>未激活</option>
                        <option value="1" <c:if test="${card.state == 1}"> selected="selected"</c:if>>已激活</option>
                    </select>
                </td>
                <td>
                    <label>卡类型:</label>
                </td>
                <td>
                    <select name="cardType">
                        <option value="99" <c:if test="${card.cardType == 99}"> selected="selected"</c:if>>全部</option>
                        <option value="1" <c:if test="${card.cardType == 1}"> selected="selected"</c:if>>电子卡</option>
                        <option value="0" <c:if test="${card.cardType == 0}"> selected="selected"</c:if>>实体卡</option>
                    </select>
                </td>

            </tr>
            <tr>
                <td><label>购买人手机:</label></td>
                <td><input name="buyerPhone" value="${card.buyerPhone}" class="phone"/>

                <td><label>购买人邮箱:</label></td>
                <td><input name="buyerEmail" value="${card.buyerEmail}" class="email"/>
                <td><label>面  额:</label></td>
                <td><input name="cardPrice" value="${card.cardPrice}"/>
                <td><input type="checkbox" id="checkDel" <c:if test="${card.isDel == 0}"> checked="checked" </c:if>>排除作废礼品卡</td>
                <td>
                    <div class="subBar">
                        <div class="buttonActive">
                            <div class="buttonContent">
                                <button type="submit">筛   选</button>
                            </div>
                        </div>
                    </div>
                </td>
                <td><a class="button" href="javascript:void(0);"><span id="cardReset">重  置</span></a></td>
            </tr>
        </table>
        <br/>
    </form>
</div>

<!-- ============================================== -->
<div class="pageContent">
    <form method="post" id="cardListExportForm" class="pageForm" target="_blank">
        <div class="panelBar" style="background: #eeff62">
            <ul>
                <li>
                    <span> 统计：总数量 ${countMap['total']}张，未过期 <span style="color: red">${countMap['unexpired']}张</span>，已过期 <span style="color: red">${countMap['expired']}张</span>，已销售 <a href="javascript:void(0); " id="cardSell"><span style="color: red">${countMap['sell']}张（查看）</span></a>，作废 <a href="javascript:void(0); " id="cardDel"><span style="color: red">${countMap['del']}张（查看）</span></a>，已激活 <a href="javascript:void(0); " id="cardActive"><span style="color: red">${countMap['active']}张（查看）</span></a>， 已冻结 <a href="javascript:void(0); " id="cardFreeze"><span style="color: red">${countMap['freeze']}张（查看）</span></a></span>
                </li>
            </ul>
        </div>
        <div class="panelBar">
            <ul class="toolBar">
                <c:if test='${sessionScope.bkstage_user.menus["f29a109a7e8ed1b91660139e0d7ad1f1"]!=null}'>
                    <li> <a title="礼品卡作废" target="dialog" rel="ids"  class="delete" href="/card/card/del" id="batchDel"><span>批量作废</span></a></li>
                </c:if>
                <c:if test='${sessionScope.bkstage_user.menus["acb77d78d8b7f2b1854fb77640a4ce65"]!=null}'>
                    <li><a title="导出礼品卡统计表"  href="javascript:void(0);" class="icon" id="countExport"><span>导出礼品卡统计表</span></a></li>
                </c:if>
                <c:if test='${sessionScope.bkstage_user.menus["eec65e341cb81e0470721b33c8db7cf7"]!=null}'>
                    <li><a title="导出礼品卡" href="javascript:void(0); " class="icon" id="cardExport"><span>导出礼品卡</span></a></li>
                </c:if>
                <c:if test='${sessionScope.bkstage_user.menus["edbe81f7292b53cbc97d4c7afeb2893b"]!=null}'>
                    <li><a title="导入购买信息" target="dialog" rel="ids" href="/card/card/index" class="add" height="220" width="450"><span>导入购买信息</span></a></li>
                </c:if>
                <li style="color: #0000ff"><span >选中数量</span> <span class="SelectSaveCount" id="selectCount"><c:if test="${empty SelectSaveCount}">0</c:if><c:if test="${!empty SelectSaveCount}">${SelectSaveCount}</c:if></span> <span >/    200</span> </li>
            </ul>
            <ul style="float: right">
                <c:if test='${sessionScope.bkstage_user.menus["bee1420ef18b0f62780bee3005712849"]!=null}'>
                    <li><a title="查看导入购买记录" target="navTab" rel="ids" href="/card/card/import-list"><span style="color: #0000ff">查看导入购买记录</span></a></li>
                </c:if>
                <c:if test='${sessionScope.bkstage_user.menus["edbe81f7292b53cbc97d4c7afeb2893b"]!=null}'>
                    <li><a title="下载导入购买信息模板" href="/card/card/index?handle=2"><span style="color: #0000ff">下载导入购买信息模板</span></a></li>
                </c:if>
                <li></li><li></li>
            </ul>
        </div>
        <table class="table" width="100%" layoutH="250">
            <thead>
            <tr>
                <th align="center"><input type="checkbox" group="ids" ctrId="cardExport" class="checkboxCtrl saveAllStateChecked"></th>
                <th align="center">礼品卡卡号</th>
                <th align="center">卡类型</th>
                <th align="center">卡名称</th>
                <th align="center">有效期开始时间</th>
                <th align="center">有效期结束时间</th>
                <th align="center">面额(元)</th>
                <th align="center">礼品卡状态</th>
                <th align="center">销售状态</th>
                <th align="center">冻结状态</th>
                <th align="center">激活状态</th>
                <th align="center">用户激活时间</th>
                <th align="center">绑定账户</th>
                <th align="center">批次号</th>
                <th align="center">销售人</th>
                <th align="center">操作</th>
            </tr>
            </thead>
            <tbody id="zuofeiTable">
            <c:if test="${empty pc.entityList}">
                <tr>
                    <td style="text-align: center;"><font color="#dc143c">暂无数据</font></td>
                </tr>
            </c:if>
            <c:if test="${!empty pc.entityList}">
                <c:forEach var="e" varStatus="i" items="${pc.entityList}">
                    <tr rel="${e.cardId}" <c:if test="${e.isFreeze == 1}">style="color: #0e10ff" </c:if><c:if test="${e.endTime < currentTime}">style="color: red" </c:if>>
                        <td><input name="ids" value="${e.cardId}" type="checkbox"></td>
                        <td>${e.cardId}</td>
                        <td><c:if test="${e.cardType == 0}">实体卡</c:if><c:if test="${e.cardType == 1}">电子卡</c:if></td>
                        <td>${e.cardName}</td>
                        <td><fmt:formatDate value="${e.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                        <td><fmt:formatDate value="${e.endTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                        <td>￥${e.cardPrice}</td>
                        <td><c:choose><c:when test="${e.endTime >= currentTime }">未过期</c:when><c:otherwise>已过期</c:otherwise></c:choose></td>
                        <td><c:if test="${e.isSell == 0}">未销售</c:if><c:if test="${e.isSell == 1}">已销售</c:if></td>
                        <td><c:if test="${e.isFreeze == 0}">正常</c:if><c:if test="${e.isFreeze == 1}">已冻结</c:if></td>
                        <td><c:if test="${e.state == 0}">未激活</c:if><c:if test="${e.state == 1}">已激活</c:if></td>
                        <td><c:choose><c:when test="${e.state == 1 }"><fmt:formatDate value="${e.activeTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></c:when><c:otherwise>-</c:otherwise></c:choose></td>
                        <td><c:choose><c:when test="${e.state == 1 }">${e.userName}</c:when><c:otherwise>-</c:otherwise></c:choose></td>
                        <td>${e.batchId}</td>
                        <td><c:choose><c:when test="${e.isSell == 1 }">${e.sellerName}</c:when><c:otherwise>-</c:otherwise></c:choose></td>
                        <td align="center">
                            <c:if test='${sessionScope.bkstage_user.menus["05ac152285728f2a53215d3d20a15dbe"]!=null}'>
                                <span><a title="查看礼品卡详情" href="/card/card/detail?cardId=${e.cardId}" target="navTab" style="color: #0000ff">查看详情</a></span>
                            </c:if>
                            <c:choose>
                                <c:when test="${e.isDel == 1}">
                                    礼品卡已作废
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${e.state != 1 && e.isFreeze == 0&& e.endTime >= currentTime}">
                                        <c:if test='${sessionScope.bkstage_user.menus["e80ff5f258b9306f5271fc799ca76373"]!=null}'>
                                            <span><a title="冻结礼品卡" href="/card/card/freeze?cardId=${e.cardId}&state=1" height="200" width="400" target="dialog" style="color: #0000ff">冻结</a></span>
                                        </c:if>
                                        <c:if test="${e.cardType == 1}">
                                            <c:if test='${sessionScope.bkstage_user.menus["0bc4b6e611de0ab139818bef418cb2e4"]!=null}'>
                                                <span><a title="发送信息" href="/card/card/send?cardId=${e.cardId}" height="200" width="550"  target="dialog" style="color: #0000ff">发送</a></span>
                                            </c:if>
                                        </c:if>
                                    </c:if>
                                    <c:if test="${e.isFreeze == 1 && e.endTime >= currentTime}">
                                        <c:if test='${sessionScope.bkstage_user.menus["e80ff5f258b9306f5271fc799ca76373"]!=null}'>
                                            <span><a title="解除冻结" href="/card/card/freeze?cardId=${e.cardId}&state=0" height="200" width="400" target="dialog" style="color: red">解除冻结</a></span>
                                        </c:if>
                                    </c:if>
                                    <c:if test="${e.state != 1}">
                                        <c:if test='${sessionScope.bkstage_user.menus["f29a109a7e8ed1b91660139e0d7ad1f1"]!=null}'>
                                            <span><a title="作废礼品卡" href="/card/card/del?cardId=${e.cardId}&type=single" target="dialog" style="color: #0000ff">作废</a></span>
                                        </c:if>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
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

        $("#cardReset").click(function () {
            $("input[name=batchId]").val("");
            $("input[name=userName]").val("");
            $("input[name=createTimeMin]").val("");
            $("input[name=createTimeMax]").val("");
            $("input[name=endTimeMin]").val("");
            $("input[name=endTimeMax]").val("");
            $("input[name=cardId]").val("");
            $("input[name=sellerName]").val("");
            $("input[name=activeTimeMin]").val("");
            $("input[name=activeTimeMax]").val("");
            $("select[name=cardType]").val("");
            $("select[name=state]").val("");
            $("select[name=isFreeze]").val("");
            $("select[name=isSell]").val("");
            $("input[name=cardPrice]").val("");
            $("input[name=buyerPhone]").val("");
            $("input[name=buyerEmail]").val("");
        });

        $("#cardSell").on('click',function(e){
            e.preventDefault();
            $("select[name=isSell]").val(1);
            $("select[name=state]").val("");
            $("select[name=isFreeze]").val("");
            $("#isDel").val(99);
            $("#card_list_queryForm").attr("action","card/card/list?clearCtrId=1").submit();
            return false;
        });
        $("#cardActive").on('click',function(e){
            e.preventDefault();
            $("select[name=state]").val(1);
            $("select[name=isSell]").val("");
            $("select[name=isFreeze]").val("");
            $("#isDel").val(99);
            $("#card_list_queryForm").attr("action","card/card/list?clearCtrId=1").submit();
            return false;
        });
        $("#cardFreeze").on('click',function(e){
            e.preventDefault();
            $("select[name=isFreeze]").val(1);
            $("select[name=isSell]").val("");
            $("select[name=state]").val("");
            $("#isDel").val(99);
            $("#card_list_queryForm").attr("action","card/card/list?clearCtrId=1").submit();
            return false;
        });
        $("#cardDel").on('click',function(e){
            e.preventDefault();
            $("#isDel").val(1);
            $("select[name=isFreeze]").val("");
            $("select[name=isSell]").val("");
            $("select[name=state]").val("");
            $("#card_list_queryForm").attr("action","card/card/list?clearCtrId=1").submit();
            return false;
        });

        $("#checkDel").on('click',function(){
            $(this).attr("checked") == 'checked'? $("#isDel").val(0):$("#isDel").val(99);
        });

        $("#countExport").click(function(){
            $("#getCountCardList").html($("#card_list_queryForm").html()).submit();
            return false;
        });
        $("#cardExport").click(function(){
            $("#getCardExport").html($("#card_list_queryForm").html()).submit();
            return false;
        });
    });
</script>


<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/pms/consult/list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}">
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}">
    <input type="hidden" name="content" value="${consult.content}">
    <input type="hidden" name="nickName" value="${consult.nickName}">
    <input type="hidden" name="proId" value="${consult.proId}">
    <input type="hidden" name="proName" value="${consult.proName}">
    <input type="hidden" name="consultType" value="${consult.consultType}">
    <input type="hidden" name="startTime" value="${consult.startTime}">
    <input type="hidden" name="endTime" value="${consult.endTime}">
    <input type="hidden" name="isWebVisible" value="${consult.isWebVisible}">
    <input type="hidden" name="isReply" value="${consult.isReply}">
</form>
<div class="pageHeader">
    <form ref="pagerForm" class="required-validate" onsubmit="return navTabSearch(this);" action="/pms/consult/list" method="post">
        <input type="hidden" name="isReply" value="${consult.isReply}">
        <div class="searchBar">
            <table class="searchContent">
                <tr>
                    <td>
                        <label>咨询内容</label><input type="text" size="15" name="content" id="consultContent" <c:if test="${!empty consult.content}">value="${consult.content}"</c:if>>
                    </td>
                    <td>
                        <label>咨询用户</label><input type="text" size="15" name="nickName" id="nickNameConsult" <c:if test="${!empty consult.nickName}">value="${consult.nickName}"</c:if>>
                    </td>
                    <td>
                        <label>商品ID</label><input type="text" size="15" class="digits" name="proId" id="proIdConsult" <c:if test="${!empty consult.proId}">value="${consult.proId}"</c:if>>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label>商品标题</label><input type="text" size="15" name="proName" id="proNameConsult" <c:if test="${!empty consult.proName}">value="${consult.proName}"</c:if>>
                    </td>
                    <td>
                        <label>咨询类型</label>
                        <select id="consultType" name="consultType">
                            <option value="">全部</option>
                            <option value="1" <c:if test="${consult.consultType==1}">selected="selected"</c:if>>商品咨询</option>
                            <option value="2" <c:if test="${consult.consultType==2}">selected="selected"</c:if>>库存及配送</option>
                            <option value="3" <c:if test="${consult.consultType==3}">selected="selected"</c:if>>支付问题</option>
                            <option value="4" <c:if test="${consult.consultType==4}">selected="selected"</c:if>>促销及赠品</option>
                            <option value="5" <c:if test="${consult.consultType==5}">selected="selected"</c:if>>其他</option>

                        </select>
                    </td>
                    <td>
                        <label>咨询时间</label><input type="text" size="15" class="date" dateFmt="yyyy-MM-dd HH:mm:ss"  name="startTime" id="startTimeConsult" <c:if test="${!empty consult.startTime}">value="${consult.startTime}"</c:if>>
                        至
                        <input type="text" size="15" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" name="endTime" id="endTimeConsult" <c:if test="${!empty consult.endTime}">value="${consult.endTime}"</c:if>>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label>前台显示的咨询<input type="checkbox" name="isWebVisible" id="isWebVisible" <c:if test="${!empty consult.isWebVisible}">value="0" checked</c:if>></label>
                    </td>
                    <td></td>
                    <td>
                        <div class="subBar">
                            <ul>
                                <li>
                                    <div class="buttonActive"><div class="buttonContent"><button type="submit">筛选</button></div></div>
                                    <div class="buttonActive"><div class="buttonContent" id="revertSelectConsult"><button type="button">重置</button></div></div>
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
    <form action="/pms/consult/update" id="consultForm" method="post" class="pageForm" onsubmit="return validateCallback(this, navTabAjaxDone)">
        <input type="hidden" name="handle" id="consultState" value=""/>
        <div class="panelBar">
            <ul class="toolBar">
                <c:if test="${consult.isReply==1}">
                    <li><a title="标记一条或多条为前台显示"  rel="ids" val="2" class="edit updateConsult"><span>前台显示</span></a></li>
                    <li><a title="标记一条或多条为前台不显示"  rel="ids" val="1" class="edit updateConsult"><span>前台不显示</span></a></li>
                </c:if>
                <li><a title="标记一条或多条为已删除"  rel="ids" val="3" class="delete updateConsult"><span>删除咨询</span></a></li>
                <li><a title="恢复一条或多条已删除的咨询"  rel="ids" val="4" class="delete updateConsult"><span>恢复删除</span></a></li>
            </ul>
        </div>
        <div class="tabs" currentIndex="${consult.isReply-1}" eventtype="click">
            <div class="tabsHeader">
                <div class="tabsHeaderContent">
                    <ul>
                        <li><a title="咨询管理(已回复)" href="/pms/consult/list?isReply=1" target="navTab" rel="139"><span>已回复</span></a></li>
                        <li><a title="咨询管理(未回复)" href="/pms/consult/list?isReply=2" target="navTab" rel="139"><span>未回复</span></a></li>
                    </ul>
                </div>
            </div>
            <div class="tabsContent" style="">
                <div>
                    <c:if test="${consult.isReply==1}">
                        <table class="table" width="100%" layoutH="195">
                            <thead>
                            <tr align="center">
                                <th width="2%"><input type="checkbox" group="ids" class="checkboxCtrl"></th>
                                <th width="6%">咨询类型</th>
                                <th width="20%">咨询内容</th>
                                <th width="8%">咨询用户</th>
                                <th width="13%">咨询时间</th>
                                <th width="8%">商品编号</th>
                                <th width="18%">商品信息</th>
                                <th width="15%">回复内容</th>
                                <th width="5%">前台显示</th>
                                <th width="5%">是否删除</th>
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
                                        <td>
                                            <c:choose>
                                                <c:when test="${entity.consultType == 1}">
                                                    商品咨询
                                                </c:when>
                                                <c:when test="${entity.consultType == 2}">
                                                    库存及配送
                                                </c:when>
                                                <c:when test="${entity.consultType == 3}">
                                                    支付问题
                                                </c:when>
                                                <c:when test="${entity.consultType == 4}">
                                                    促销及赠品
                                                </c:when>
                                                <c:when test="${entity.consultType == 5}">
                                                    其他
                                                </c:when>
                                            </c:choose>
                                        </td>
                                        <td style=" overflow:hidden;word-break: break-all; word-wrap: break-word;"><a target="dialog" height="400" title="查看详情" href="/pms/consult/detail?id=${entity.id}"><span style="color: #0000ff">${entity.content}</span> </a></td>
                                        <td>${entity.nickName}</td>
                                        <td><fmt:formatDate value="${entity.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                                        <td>${entity.proId}</td>
                                        <td style=" overflow:hidden;word-break: break-all; word-wrap: break-word;">${entity.proName}</td>
                                        <td style=" overflow:hidden;word-break: break-all; word-wrap: break-word;">
                                            <c:if test="${empty entity.replyContent}"><a target="dialog" width="600" height="340" title="回复咨询？" href="/pms/consult/reply?id=${entity.id}&handle=0"><span style="color: #ff0000">未回复</span> </a></c:if>
                                            <c:if test="${!empty entity.replyContent}">${entity.replyContent}</c:if>
                                        </td>
                                        <td>
                                            <c:if test="${entity.isWebVisible==-1}"><a target="ajaxTodo" title="改为前台显示？" href="/pms/consult/update?ids=${entity.id}&handle=2"><span style="color: #ff0000">不显示</span> </a></c:if>
                                            <c:if test="${entity.isWebVisible==0}"><a target="ajaxTodo" title="改为前台隐藏？" href="/pms/consult/update?ids=${entity.id}&handle=1"><span style="color: #0000ff">已显示</span> </a></c:if>
                                        </td>
                                        <td>
                                            <c:if test="${entity.isAdminDel==0}"><a target="ajaxTodo" title="改为已删除？" href="/pms/consult/update?ids=${entity.id}&handle=3"><span style="color: #0000ff">未删除</span></a></c:if>
                                            <c:if test="${entity.isAdminDel==-1}"><a target="ajaxTodo" title="改为未删除？" href="/pms/consult/update?ids=${entity.id}&handle=4"><span style="color: #ff0000">已删除</span></a></c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            </tbody>
                        </table>
                    </c:if>
                </div>
                <div>
                    <c:if test="${consult.isReply==2}">
                        <table class="table" width="100%" layoutH="195">
                            <thead>
                            <tr align="center">
                                <th width="2%"><input type="checkbox" group="ids" class="checkboxCtrl"></th>
                                <th width="6%">咨询类型</th>
                                <th width="20%">咨询内容</th>
                                <th width="8%">咨询用户</th>
                                <th width="13%">咨询时间</th>
                                <th width="8%">商品编号</th>
                                <th width="18%">商品信息</th>
                                <th width="15%">回复内容</th>
                                <th width="5%">前台显示</th>
                                <th width="5%">是否删除</th>
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
                                        <td>
                                            <c:choose>
                                                <c:when test="${entity.consultType == 1}">
                                                    商品咨询
                                                </c:when>
                                                <c:when test="${entity.consultType == 2}">
                                                    库存及配送
                                                </c:when>
                                                <c:when test="${entity.consultType == 3}">
                                                    支付问题
                                                </c:when>
                                                <c:when test="${entity.consultType == 4}">
                                                    促销及赠品
                                                </c:when>
                                                <c:when test="${entity.consultType == 5}">
                                                    其他
                                                </c:when>
                                            </c:choose>
                                        </td>
                                        <td style=" overflow:hidden;word-break: break-all; word-wrap: break-word;"><a target="dialog" height="450" title="查看详情" href="/pms/consult/detail?id=${entity.id}"><span style="color: #0000ff">${entity.content}</span> </a></td>
                                        <td>${entity.nickName}</td>
                                        <td><fmt:formatDate value="${entity.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                                        <td>${entity.proId}</td>
                                        <td style=" overflow:hidden;word-break: break-all; word-wrap: break-word;">${entity.proName}</td>
                                        <td style=" overflow:hidden;word-break: break-all; word-wrap: break-word;">
                                            <c:if test="${empty entity.replyContent}"><a target="dialog" width="600" height="340" title="回复咨询？" href="/pms/consult/reply?id=${entity.id}&handle=0"><span style="color: #ff0000">未回复</span> </a></c:if>
                                            <c:if test="${!empty entity.replyContent}">${entity.replyContent}</c:if>
                                        </td>
                                        <td>
                                            <span style="color: #ff0000">不显示</span>
                                        </td>
                                        <td>
                                            <c:if test="${entity.isAdminDel==0}"><a target="ajaxTodo" title="改为已删除？" href="/pms/consult/update?ids=${entity.id}&handle=3"><span style="color: #0000ff">未删除</span></a></c:if>
                                            <c:if test="${entity.isAdminDel==-1}"><a target="ajaxTodo" title="改为未删除？" href="/pms/consult/update?ids=${entity.id}&handle=4"><span style="color: #ff0000">已删除</span></a></c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            </tbody>
                        </table>
                    </c:if>
                </div>
            </div>
        </div>
    </form>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $(".updateConsult").click(function () {
            $("#consultState").val($(this).attr("val"));
            alertMsg.confirm('确实要执行?', {
                okCall: function () {
                    $("#consultForm").submit();
                }
            });
        });

        $("#isWebVisible").click(function(){
            $(this).val(0);
        });

        $("#revertSelectConsult").click(function(){
            $("#consultContent").val("");
            $("#nickNameConsult").val("");
            $("#proIdConsult").val("");
            $("#proNameConsult").val("");
            $("#startTimeConsult").val("");
            $("#endTimeConsult").val("");
            $("#isWebVisible").replaceWith("<input type='checkbox' name='isWebVisible' id='isWebVisible' >");
            $("#consultType").html("<option value=''>全部</option><option value='0'>商品咨询</option><option value='1'>物流及库存</option><option value='2'>支付问题</option><option value='3'>促销及赠品</option><option value='4'>其他</option>");
        });
    });
</script>
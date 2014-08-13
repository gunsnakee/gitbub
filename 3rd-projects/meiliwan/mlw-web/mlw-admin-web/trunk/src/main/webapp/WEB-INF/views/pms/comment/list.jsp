<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/pms/comment/list">
    <input type="hidden" name="pageNum" value="${pc.pageInfo.page}">
    <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}">
    <input type="hidden" name="orderId" value="${commentView.orderId}">
    <input type="hidden" name="nickName" value="${commentView.nickName}">
    <input type="hidden" name="proId" value="${commentView.proId}">
    <input type="hidden" name="proName" value="${commentView.proName}">
    <input type="hidden" name="startUseful" value="${commentView.startUseful}">
    <input type="hidden" name="endUseful" value="${commentView.endUseful}">
    <input type="hidden" name="startUseless" value="${commentView.startUseless}">
    <input type="hidden" name="endUseless" value="${commentView.endUseless}">
    <input type="hidden" name="startTime" value="${commentView.startTime}">
    <input type="hidden" name="endTime" value="${commentView.endTime}">
    <input type="hidden" name="startScore" value="${commentView.startScore}">
    <input type="hidden" name="endScore" value="${commentView.endScore}">
    <input type="hidden" name="isTop" value="${commentView.isTop}">
    <input type="hidden" name="content" value="${commentView.content}">
    <input type="hidden" name="isReply" value="${commentView.isReply}">
    <input type="hidden" name="adminDel" value="${commentView.adminDel}">
    <input type="hidden" name="check" value="${commentView.check}">
    <input type="hidden" name="orderField" value="${pc.pageInfo.orderField}" />
    <input type="hidden" name="orderDirection" value="${pc.pageInfo.orderDirection}" />
    <input type="hidden" name="divIndex" value="${divIndex}" />
</form>
<div class="pageHeader">
    <form ref="pagerForm"  class="required-validate"  onsubmit="return navTabSearch(this);" action="/pms/comment/list" method="post">
        <input type="hidden" name="isReply" value="${commentView.isReply}">
        <input type="hidden" name="divIndex" value="${divIndex}" />
        <input type="hidden" name="adminDel" value="${commentView.adminDel}">
        <input type="hidden" name="check" value="${commentView.check}">
        <input type="hidden" name="state" value="${commentView.state}">

        <div class="searchBar">
            <table class="searchContent">
                <tr>
                    <td>
                        <label>订单编号</label><input type="text" size="15" class="digits" name="orderId" id="orderIdComment" <c:if test="${!empty commentView.orderId}">value="${commentView.orderId}"</c:if>>
                    </td>
                    <td>
                        <label>评价用户</label><input type="text" size="15" name="nickName" id="nickNameComment" <c:if test="${!empty commentView.nickName}">value="${commentView.nickName}"</c:if>>
                    </td>
                    <td>
                        <label>商品ID</label><input type="text" size="15" class="digits" name="proId" id="proIdComment" <c:if test="${!empty commentView.proId}">value="${commentView.proId}"</c:if>>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label>商品标题</label><input type="text" size="15" name="proName" id="proNameComment" <c:if test="${!empty commentView.proName}">value="${commentView.proName}"</c:if>>
                    </td>
                    <td>
                        <label>有用次数</label><input type="text" class="digits" size="15" name="startUseful" id="startUsefulComment" <c:if test="${!empty commentView.startUseful}">value="${commentView.startUseful}"</c:if>>
                        至
                        <input type="text" size="15" name="endUseful" class="digits" id="endUsefulComment" <c:if test="${!empty commentView.endUseful}">value="${commentView.endUseful}"</c:if>>
                    </td>
                    <td>
                        <label>无用次数</label><input type="text" class="digits" size="15" name="startUseless" id="startUselessComment" <c:if test="${!empty commentView.startUseless}">value="${commentView.startUseless}"</c:if>>
                        至
                        <input type="text" size="15" class="digits" name="endUseless" id="endUselessComment" <c:if test="${!empty commentView.endUseless}">value="${commentView.endUseless}"</c:if>>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label>评价时间</label><input type="text" size="15" class="date" dateFmt="yyyy-MM-dd HH:mm:ss"  name="startTime" id="startTimeComment" <c:if test="${!empty commentView.startTime}">value="${commentView.startTime}"</c:if>>
                        至
                        <input type="text" size="15" class="date" dateFmt="yyyy-MM-dd HH:mm:ss" name="endTime" id="endTimeComment" <c:if test="${!empty commentView.endTime}">value="${commentView.endTime}"</c:if>>
                    </td>
                    <td>
                        <label>评分</label><input type="text" min="1" max="4" class="digits" size="15" name="startScore" id="startScoreComment" <c:if test="${!empty commentView.startScore}">value="${commentView.startScore}"</c:if>>
                        至
                        <input type="text" min="2" max="5" size="15" class="digits" name="endScore" id="endScoreComment" <c:if test="${!empty commentView.endScore}">value="${commentView.endScore}"</c:if>>
                    </td>
                    <td>
                        <label>是否置顶</label>
                        <select name="isTop" id="isTopComment">
                            <option value="">全部</option>
                            <option value="0" <c:if test="${commentView.isTop==0}">selected="selected"</c:if>>已置顶</option>
                            <option value="-1" <c:if test="${commentView.isTop==-1}">selected="selected"</c:if>>非置顶</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label>评价字数15个字以上<input type="checkbox" name="content" id="commentSize" <c:if test="${!empty commentView.content}">checked</c:if>></label>
                    </td>
                    <td></td>
                    <td>
                        <div class="subBar">
                            <ul>
                                <li>
                                    <div class="buttonActive"><div class="buttonContent"><button type="submit">筛选</button></div></div>
                                    <div class="buttonActive"><div class="buttonContent" id="revertSelectComment"><button type="button">重置</button></div></div>
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
    <form action="/pms/comment/update" id="commentForm" method="post" class="pageForm" onsubmit="return validateCallback(this, navTabAjaxDone)">
        <input type="hidden" name="handle" id="commentState" value=""/>
        <div class="panelBar">
            <ul class="toolBar">
                <li><a title="标记一条或多条评价为审核不通过"  rel="ids" val="1" class="edit"><span>审核不通过</span></a></li>
                <li><a title="标记一条或多条评价为审核通过"  rel="ids" val="2" class="edit"><span>审核通过</span></a></li>
                <li><a title="标记一条或多条评价为置顶"  rel="ids" val="8" class="edit"><span>设为置顶</span></a></li>
                <li><a title="标记一条或多条评价为不置顶"  rel="ids" val="7" class="edit"><span>取消置顶</span></a></li>
                <li><a title="标记一条或多条为已删除"  rel="ids" val="3" class="delete"><span>删除评论</span></a></li>
                <li><a title="恢复一条或多条已删除的评价"  rel="ids" val="4" class="delete"><span>恢复删除</span></a></li>
            </ul>
        </div>
        <div class="tabs" currentIndex="${divIndex}" eventType="click">
            <div class="tabsHeader">
                <div class="tabsHeaderContent">
                    <ul>
                        <%--<c:if test="${!empty commentView.orderId}">&orderId=${commentView.orderId}</c:if><c:if test="${!empty commentView.nickName}">&nickName=${commentView.nickName}</c:if><c:if test="${!empty commentView.proId}">&proId=${commentView.proId}</c:if><c:if test="${!empty commentView.proName}">&proName=${commentView.proName}</c:if><c:if test="${!empty commentView.startUseful}">&startUseful=${commentView.startUseful}</c:if><c:if test="${!empty commentView.endUseful}">&endUseful=${commentView.endUseful}</c:if><c:if test="${!empty commentView.startUseless}">&startUseless=${commentView.startUseless}</c:if><c:if test="${!empty commentView.endUseless}">&endUseless=${commentView.endUseless}</c:if><c:if test="${!empty commentView.startTime}">&startTime=${commentView.startTime}</c:if><c:if test="${!empty commentView.endTime}">&endTime=${commentView.endTime}</c:if><c:if test="${!empty commentView.startScore}">&startScore=${commentView.startScore}</c:if><c:if test="${!empty commentView.endScore}">&endScore=${commentView.endScore}</c:if><c:if test="${!empty commentView.isTop}">&isTop=${commentView.isTop}</c:if><c:if test="${!empty commentView.content}">&content=${commentView.content}</c:if>
--%>
                        <li><a title="评价管理(已回复)" href="/pms/comment/list?isReply=1&divIndex=0<c:if test="${!empty commentView.orderId}">&orderId=${commentView.orderId}</c:if><c:if test="${!empty commentView.nickName}">&nickName=${commentView.nickName}</c:if><c:if test="${!empty commentView.proId}">&proId=${commentView.proId}</c:if><c:if test="${!empty commentView.proName}">&proName=${commentView.proName}</c:if><c:if test="${!empty commentView.startUseful}">&startUseful=${commentView.startUseful}</c:if><c:if test="${!empty commentView.endUseful}">&endUseful=${commentView.endUseful}</c:if><c:if test="${!empty commentView.startUseless}">&startUseless=${commentView.startUseless}</c:if><c:if test="${!empty commentView.endUseless}">&endUseless=${commentView.endUseless}</c:if><c:if test="${!empty commentView.startTime}">&startTime=${commentView.startTime}</c:if><c:if test="${!empty commentView.endTime}">&endTime=${commentView.endTime}</c:if><c:if test="${!empty commentView.startScore}">&startScore=${commentView.startScore}</c:if><c:if test="${!empty commentView.endScore}">&endScore=${commentView.endScore}</c:if><c:if test="${!empty commentView.isTop}">&isTop=${commentView.isTop}</c:if><c:if test="${!empty commentView.content}">&content=${commentView.content}</c:if>" target="navTab" rel="135"><span>已回复</span></a></li>
                        <li><a title="评价管理(未回复)" href="/pms/comment/list?isReply=2&state=0&divIndex=1<c:if test="${!empty commentView.orderId}">&orderId=${commentView.orderId}</c:if><c:if test="${!empty commentView.nickName}">&nickName=${commentView.nickName}</c:if><c:if test="${!empty commentView.proId}">&proId=${commentView.proId}</c:if><c:if test="${!empty commentView.proName}">&proName=${commentView.proName}</c:if><c:if test="${!empty commentView.startUseful}">&startUseful=${commentView.startUseful}</c:if><c:if test="${!empty commentView.endUseful}">&endUseful=${commentView.endUseful}</c:if><c:if test="${!empty commentView.startUseless}">&startUseless=${commentView.startUseless}</c:if><c:if test="${!empty commentView.endUseless}">&endUseless=${commentView.endUseless}</c:if><c:if test="${!empty commentView.startTime}">&startTime=${commentView.startTime}</c:if><c:if test="${!empty commentView.endTime}">&endTime=${commentView.endTime}</c:if><c:if test="${!empty commentView.startScore}">&startScore=${commentView.startScore}</c:if><c:if test="${!empty commentView.endScore}">&endScore=${commentView.endScore}</c:if><c:if test="${!empty commentView.isTop}">&isTop=${commentView.isTop}</c:if><c:if test="${!empty commentView.content}">&content=${commentView.content}</c:if>" target="navTab" rel="135"><span>未回复</span></a></li>
                        <li><a title="评价管理(已删除)" href="/pms/comment/list?adminDel=-1&divIndex=2<c:if test="${!empty commentView.orderId}">&orderId=${commentView.orderId}</c:if><c:if test="${!empty commentView.nickName}">&nickName=${commentView.nickName}</c:if><c:if test="${!empty commentView.proId}">&proId=${commentView.proId}</c:if><c:if test="${!empty commentView.proName}">&proName=${commentView.proName}</c:if><c:if test="${!empty commentView.startUseful}">&startUseful=${commentView.startUseful}</c:if><c:if test="${!empty commentView.endUseful}">&endUseful=${commentView.endUseful}</c:if><c:if test="${!empty commentView.startUseless}">&startUseless=${commentView.startUseless}</c:if><c:if test="${!empty commentView.endUseless}">&endUseless=${commentView.endUseless}</c:if><c:if test="${!empty commentView.startTime}">&startTime=${commentView.startTime}</c:if><c:if test="${!empty commentView.endTime}">&endTime=${commentView.endTime}</c:if><c:if test="${!empty commentView.startScore}">&startScore=${commentView.startScore}</c:if><c:if test="${!empty commentView.endScore}">&endScore=${commentView.endScore}</c:if><c:if test="${!empty commentView.isTop}">&isTop=${commentView.isTop}</c:if><c:if test="${!empty commentView.content}">&content=${commentView.content}</c:if>" target="navTab" rel="135"><span>已删除</span></a></li>
                        <li><a title="评价管理(未审核)" href="/pms/comment/list?check=0&divIndex=3<c:if test="${!empty commentView.orderId}">&orderId=${commentView.orderId}</c:if><c:if test="${!empty commentView.nickName}">&nickName=${commentView.nickName}</c:if><c:if test="${!empty commentView.proId}">&proId=${commentView.proId}</c:if><c:if test="${!empty commentView.proName}">&proName=${commentView.proName}</c:if><c:if test="${!empty commentView.startUseful}">&startUseful=${commentView.startUseful}</c:if><c:if test="${!empty commentView.endUseful}">&endUseful=${commentView.endUseful}</c:if><c:if test="${!empty commentView.startUseless}">&startUseless=${commentView.startUseless}</c:if><c:if test="${!empty commentView.endUseless}">&endUseless=${commentView.endUseless}</c:if><c:if test="${!empty commentView.startTime}">&startTime=${commentView.startTime}</c:if><c:if test="${!empty commentView.endTime}">&endTime=${commentView.endTime}</c:if><c:if test="${!empty commentView.startScore}">&startScore=${commentView.startScore}</c:if><c:if test="${!empty commentView.endScore}">&endScore=${commentView.endScore}</c:if><c:if test="${!empty commentView.isTop}">&isTop=${commentView.isTop}</c:if><c:if test="${!empty commentView.content}">&content=${commentView.content}</c:if>" target="navTab" rel="135"><span>未审核</span></a></li>
                        <li><a title="评价管理(已审核)" href="/pms/comment/list?check=1&divIndex=4<c:if test="${!empty commentView.orderId}">&orderId=${commentView.orderId}</c:if><c:if test="${!empty commentView.nickName}">&nickName=${commentView.nickName}</c:if><c:if test="${!empty commentView.proId}">&proId=${commentView.proId}</c:if><c:if test="${!empty commentView.proName}">&proName=${commentView.proName}</c:if><c:if test="${!empty commentView.startUseful}">&startUseful=${commentView.startUseful}</c:if><c:if test="${!empty commentView.endUseful}">&endUseful=${commentView.endUseful}</c:if><c:if test="${!empty commentView.startUseless}">&startUseless=${commentView.startUseless}</c:if><c:if test="${!empty commentView.endUseless}">&endUseless=${commentView.endUseless}</c:if><c:if test="${!empty commentView.startTime}">&startTime=${commentView.startTime}</c:if><c:if test="${!empty commentView.endTime}">&endTime=${commentView.endTime}</c:if><c:if test="${!empty commentView.startScore}">&startScore=${commentView.startScore}</c:if><c:if test="${!empty commentView.endScore}">&endScore=${commentView.endScore}</c:if><c:if test="${!empty commentView.isTop}">&isTop=${commentView.isTop}</c:if><c:if test="${!empty commentView.content}">&content=${commentView.content}</c:if>" target="navTab" rel="135"><span>已审核</span></a></li>
                    </ul>
                </div>
            </div>
            <div class="tabsContent" style="">
                <!-- 已回复 start-->

                <div>
                    <c:if test="${divIndex==0}">
                    <table class="table" width="100%" layoutH="220">
                        <thead>
                        <tr align="center">
                            <th width="2%"><input type="checkbox" group="ids" class="checkboxCtrl"></th>
                            <th width="4%">评分</th>
                            <th width="18%">评论内容</th>
                            <th width="5%">有用无用次数</th>
                            <th width="6%">订单编号</th>
                            <th width="8%">评价用户</th>
                            <th width="13%" align="center"  orderField="comment_time" class="<c:if test='${pc.pageInfo.orderField=="comment_time"}' >
                        ${pc.pageInfo.orderDirection}</c:if>">评价时间</th>
                            <%--<th width="13%">评价时间</th>--%>
                            <th width="6%">商品编号</th>
                            <th width="13%">商品标题</th>
                            <th width="10%">回复内容</th>
                            <th width="4%">审核通过</th>
                            <th width="5%">是否置顶</th>
                            <th width="3%">置优</th>
                            <th width="4%">是否删除</th>
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
                                    <td>${entity.score}</td>
                                    <td title="${entity.content}" style=" overflow:hidden;word-break: break-all; word-wrap: break-word;"><a height="500" title="查看评价详情" target="dialog" href="/pms/comment/detail?id=${entity.id}"><span style="color: #0000ff">${entity.content}</span></a></td>
                                    <td>${entity.usefulCount}/${entity.uselessCount}</td>
                                    <td>${entity.orderId}</td>
                                    <td>${entity.nickName}</td>
                                    <td><fmt:formatDate value="${entity.commentTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                                    <td>${entity.proId}</td>
                                    <td style=" overflow:hidden;word-break: break-all; word-wrap: break-word;">${entity.proName}</td>
                                    <td title="${entity.replyContent}" style=" overflow:hidden;word-break: break-all; word-wrap: break-word;">
                                        <c:if test="${empty entity.replyContent}"><a target="dialog" width="600" height="400" title="去回复" href="/pms/comment/reply?id=${entity.id}&handle=0"><span style="color: #ff0000">未回复</span></a></c:if>
                                        <c:if test="${!empty entity.replyContent}">${entity.replyContent}</c:if>
                                    </td>
                                    <td>
                                        <c:if test="${entity.isWebVisible==0}"><a target="ajaxTodo" title="改为未通过审核？" href="/pms/comment/update?ids=${entity.id}&handle=1"><span style="color: #0000ff">通过</span></a></c:if>
                                        <c:if test="${entity.isWebVisible==-1}"><a target="ajaxTodo" title="改为通过审核？" href="/pms/comment/update?ids=${entity.id}&handle=2"><span style="color: #ff0000">未通过</span></a></c:if>
                                        <c:if test="${entity.isWebVisible==-2}">
                                            <a target="ajaxTodo" title="改为通过审核？" href="/pms/comment/update?ids=${entity.id}&handle=2"><span style="color: #0000ff">通过</span></a>
                                            <a target="ajaxTodo" title="改为未通过审核？" href="/pms/comment/update?ids=${entity.id}&handle=1"><span style="color: #ff0000">不通过</span></a>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${entity.sequence==0}"><a target="ajaxTodo" title="改为非置顶？" href="/pms/comment/update?ids=${entity.id}&handle=7"><span style="color: #ff0000">已置顶</span></a></c:if>
                                        <c:if test="${entity.sequence==-1}"><a target="ajaxTodo" title="改为已置顶？" href="/pms/comment/update?ids=${entity.id}&handle=8"><span style="color: #0000ff">非置顶</span></a></c:if>
                                    </td>
                                    <td>
                                        <c:if test="${entity.excellentCommentId==null||entity.excellentCommentId!=entity.id}"><a target="ajaxTodo" title="改为优秀评论？" href="/pms/product/updateExcellentComment?commentId=${entity.id}&proId=${entity.proId}"><span style="color: #ff0000">否</span></a></c:if>
                                        <c:if test="${entity.excellentCommentId!=null&&entity.excellentCommentId==entity.id}"><a target="ajaxTodo" title="取消优秀评论？" href="/pms/product/updateExcellentComment?commentId=0&proId=${entity.proId}"><span style="color: #0000ff">优</span></a></c:if>
                                    </td>
                                    <td>
                                        <c:if test="${entity.isAdminDel==0}"><a target="ajaxTodo" title="改为已删除？" href="/pms/comment/update?ids=${entity.id}&handle=3"><span style="color: #0000ff">未删除</span></a></c:if>
                                        <c:if test="${entity.isAdminDel==-1}"><a target="ajaxTodo" title="改为未删除？" href="/pms/comment/update?ids=${entity.id}&handle=4"><span style="color: #ff0000">已删除</span></a></c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:if>
                        </tbody>
                    </table>
                    </c:if>
                </div>

                <!-- 已回复 end-->
                <!-- 未回复 start-->

                <div>
                    <c:if test="${divIndex==1}">
                    <table class="table" width="100%" layoutH="220">
                        <thead>
                        <tr align="center">
                            <th width="2%"><input type="checkbox" group="ids" class="checkboxCtrl"></th>
                            <th width="4%">评分</th>
                            <th width="18%">评论内容</th>
                            <th width="5%">有用无用次数</th>
                            <th width="6%">订单编号</th>
                            <th width="8%">评价用户</th>
                            <th width="13%" align="center"  orderField="comment_time" class="<c:if test='${pc.pageInfo.orderField=="comment_time"}' >
                        ${pc.pageInfo.orderDirection}</c:if>">评价时间</th>
                            <th width="6%">商品编号</th>
                            <th width="13%">商品标题</th>
                            <th width="10%">回复内容</th>
                            <th width="4%">审核通过</th>
                            <th width="5%">是否置顶</th>
                            <th width="3%">置优</th>
                            <th width="4%">是否删除</th>
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
                                    <td>${entity.score}</td>
                                    <td title="${entity.content}" style=" overflow:hidden;word-break: break-all; word-wrap: break-word;"><a height="500" title="查看评价详情" target="dialog" href="/pms/comment/detail?id=${entity.id}"><span style="color: #0000ff">${entity.content}</span></a></td>
                                    <td>${entity.usefulCount}/${entity.uselessCount}</td>
                                    <td>${entity.orderId}</td>
                                    <td>${entity.nickName}</td>
                                    <td><fmt:formatDate value="${entity.commentTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                                    <td>${entity.proId}</td>
                                    <td style=" overflow:hidden;word-break: break-all; word-wrap: break-word;">${entity.proName}</td>
                                    <td title="${entity.replyContent}" style=" overflow:hidden;word-break: break-all; word-wrap: break-word;">
                                        <c:if test="${empty entity.replyContent}"><a target="dialog" width="600" height="400" title="去回复" href="/pms/comment/reply?id=${entity.id}&handle=0"><span style="color: #ff0000">未回复</span></a></c:if>
                                        <c:if test="${!empty entity.replyContent}">${entity.replyContent}</c:if>
                                    </td>
                                    <td>
                                        <c:if test="${entity.isWebVisible==0}"><a target="ajaxTodo" title="改为未通过审核？" href="/pms/comment/update?ids=${entity.id}&handle=1"><span style="color: #0000ff">通过</span></a></c:if>
                                        <c:if test="${entity.isWebVisible==-1}"><a target="ajaxTodo" title="改为通过审核？" href="/pms/comment/update?ids=${entity.id}&handle=2"><span style="color: #ff0000">未通过</span></a></c:if>
                                        <c:if test="${entity.isWebVisible==-2}">
                                            <a target="ajaxTodo" title="改为通过审核？"  href="/pms/comment/update?ids=${entity.id}&handle=2"><span style="color: #0000ff">通过</span></a>
                                            <a target="ajaxTodo" title="改为未通过审核？"  href="/pms/comment/update?ids=${entity.id}&handle=1"><span style="color: #ff0000">不通过</span></a>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${entity.sequence==0}"><a target="ajaxTodo" title="改为非置顶？" href="/pms/comment/update?ids=${entity.id}&handle=7"><span style="color: #ff0000">已置顶</span></a></c:if>
                                        <c:if test="${entity.sequence==-1}"><a target="ajaxTodo" title="改为已置顶？" href="/pms/comment/update?ids=${entity.id}&handle=8"><span style="color: #0000ff">非置顶</span></a></c:if>
                                    </td>
                                    <td>
                                        <c:if test="${entity.excellentCommentId==null||entity.excellentCommentId!=entity.id}"><a target="ajaxTodo" title="改为优秀评论？" href="/pms/product/updateExcellentComment?commentId=${entity.id}&proId=${entity.proId}"><span style="color: #ff0000">否</span></a></c:if>
                                        <c:if test="${entity.excellentCommentId!=null&&entity.excellentCommentId==entity.id}"><a target="ajaxTodo" title="取消优秀评论？" href="/pms/product/updateExcellentComment?commentId=0&proId=${entity.proId}"><span style="color: #0000ff">优</span></a></c:if>
                                    </td>

                                    <td>
                                        <c:if test="${entity.isAdminDel==0}"><a target="ajaxTodo" title="改为已删除？" href="/pms/comment/update?ids=${entity.id}&handle=3"><span style="color: #0000ff">未删除</span></a></c:if>
                                        <c:if test="${entity.isAdminDel==-1}"><a target="ajaxTodo" title="改为未删除？" href="/pms/comment/update?ids=${entity.id}&handle=4"><span style="color: #ff0000">已删除</span></a></c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:if>
                        </tbody>
                    </table>
                    </c:if>
                </div>

                <!-- 未回复 end-->

                <!-- 已删除 start-->

                <div>
                    <c:if test="${divIndex==2}">
                    <table class="table" width="100%" layoutH="220">
                        <thead>
                        <tr align="center">
                            <th width="2%"><input type="checkbox" group="ids" class="checkboxCtrl"></th>
                            <th width="4%">评分</th>
                            <th width="18%">评论内容</th>
                            <th width="5%">有用无用次数</th>
                            <th width="6%">订单编号</th>
                            <th width="8%">评价用户</th>
                            <th width="13%" align="center"  orderField="comment_time" class="<c:if test='${pc.pageInfo.orderField=="comment_time"}' >
                        ${pc.pageInfo.orderDirection}</c:if>">评价时间</th>
                            <%--<th width="13%">评价时间</th>--%>
                            <th width="6%">商品编号</th>
                            <th width="13%">商品标题</th>
                            <th width="10%">回复内容</th>
                            <th width="4%">审核通过</th>
                            <th width="5%">是否置顶</th>
                            <th width="3%">置优</th>
                            <th width="4%">是否删除</th>
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
                                    <td>${entity.score}</td>
                                    <td title="${entity.content}" style=" overflow:hidden;word-break: break-all; word-wrap: break-word;"><a height="500" title="查看评价详情" target="dialog" href="/pms/comment/detail?id=${entity.id}"><span style="color: #0000ff">${entity.content}</span></a></td>
                                    <td>${entity.usefulCount}/${entity.uselessCount}</td>
                                    <td>${entity.orderId}</td>
                                    <td>${entity.nickName}</td>
                                    <td><fmt:formatDate value="${entity.commentTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                                    <td>${entity.proId}</td>
                                    <td style=" overflow:hidden;word-break: break-all; word-wrap: break-word;">${entity.proName}</td>
                                    <td title="${entity.replyContent}" style=" overflow:hidden;word-break: break-all; word-wrap: break-word;">
                                        <c:if test="${empty entity.replyContent}"><a target="dialog" width="600" height="400" title="去回复" href="/pms/comment/reply?id=${entity.id}&handle=0"><span style="color: #ff0000">未回复</span></a></c:if>
                                        <c:if test="${!empty entity.replyContent}">${entity.replyContent}</c:if>
                                    </td>
                                    <td>
                                        <c:if test="${entity.isWebVisible==0}"><a target="ajaxTodo" title="改为未通过审核？" href="/pms/comment/update?ids=${entity.id}&handle=1"><span style="color: #0000ff">通过</span></a></c:if>
                                        <c:if test="${entity.isWebVisible==-1}"><a target="ajaxTodo" title="改为通过审核？" href="/pms/comment/update?ids=${entity.id}&handle=2"><span style="color: #ff0000">未通过</span></a></c:if>
                                        <c:if test="${entity.isWebVisible==-2}">
                                            <a target="ajaxTodo" title="改为通过审核？"  href="/pms/comment/update?ids=${entity.id}&handle=2"><span style="color: #0000ff">通过</span></a>
                                            <a target="ajaxTodo" title="改为未通过审核？"  href="/pms/comment/update?ids=${entity.id}&handle=1"><span style="color: #ff0000">不通过</span></a>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${entity.sequence==0}"><a target="ajaxTodo" title="改为非置顶？" href="/pms/comment/update?ids=${entity.id}&handle=7"><span style="color: #ff0000">已置顶</span></a></c:if>
                                        <c:if test="${entity.sequence==-1}"><a target="ajaxTodo" title="改为已置顶？" href="/pms/comment/update?ids=${entity.id}&handle=8"><span style="color: #0000ff">非置顶</span></a></c:if>
                                    </td>
                                    <td>
                                        <c:if test="${entity.excellentCommentId==null||entity.excellentCommentId!=entity.id}"><a target="ajaxTodo" title="改为优秀评论？" href="/pms/product/updateExcellentComment?commentId=${entity.id}&proId=${entity.proId}"><span style="color: #ff0000">否</span></a></c:if>
                                        <c:if test="${entity.excellentCommentId!=null&&entity.excellentCommentId==entity.id}"><a target="ajaxTodo" title="取消优秀评论？" href="/pms/product/updateExcellentComment?commentId=0&proId=${entity.proId}"><span style="color: #0000ff">优</span></a></c:if>
                                    </td>
                                    <td>
                                        <c:if test="${entity.isAdminDel==0}"><a target="ajaxTodo" title="改为已删除？" href="/pms/comment/update?ids=${entity.id}&handle=3"><span style="color: #0000ff">未删除</span></a></c:if>
                                        <c:if test="${entity.isAdminDel==-1}"><a target="ajaxTodo" title="改为未删除？" href="/pms/comment/update?ids=${entity.id}&handle=4"><span style="color: #ff0000">已删除</span></a></c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:if>
                        </tbody>
                    </table>
                    </c:if>
                </div>

                <!-- 已删除 end-->

                <!-- 未审核 start-->

                <div>
                    <c:if test="${divIndex==3}">
                    <table class="table" width="100%" layoutH="220">
                        <thead>
                        <tr align="center">
                            <th width="2%"><input type="checkbox" group="ids" class="checkboxCtrl"></th>
                            <th width="4%">评分</th>
                            <th width="18%">评论内容</th>
                            <th width="5%">有用无用次数</th>
                            <th width="6%">订单编号</th>
                            <th width="8%">评价用户</th>
                            <th width="13%" align="center"  orderField="comment_time" class="<c:if test='${pc.pageInfo.orderField=="comment_time"}' >
                        ${pc.pageInfo.orderDirection}</c:if>">评价时间</th>
                            <%--<th width="13%">评价时间</th>--%>
                            <th width="6%">商品编号</th>
                            <th width="13%">商品标题</th>
                            <th width="4%">回复内容</th>
                            <th width="10%">审核通过</th>
                            <th width="5%">是否置顶</th>
                            <th width="3%">置优</th>
                            <th width="4%">是否删除</th>
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
                                    <td>${entity.score}</td>
                                    <td title="${entity.content}" style=" overflow:hidden;word-break: break-all; word-wrap: break-word;"><a height="500" title="查看评价详情" target="dialog" href="/pms/comment/detail?id=${entity.id}"><span style="color: #0000ff">${entity.content}</span></a></td>
                                    <td>${entity.usefulCount}/${entity.uselessCount}</td>
                                    <td>${entity.orderId}</td>
                                    <td>${entity.nickName}</td>
                                    <td><fmt:formatDate value="${entity.commentTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                                    <td>${entity.proId}</td>
                                    <td style=" overflow:hidden;word-break: break-all; word-wrap: break-word;">${entity.proName}</td>
                                    <td title="${entity.replyContent}" style=" overflow:hidden;word-break: break-all; word-wrap: break-word;">
                                        <c:if test="${empty entity.replyContent}"><a target="dialog" width="600" height="400" title="去回复" href="/pms/comment/reply?id=${entity.id}&handle=0"><span style="color: #ff0000">未回复</span></a></c:if>
                                        <c:if test="${!empty entity.replyContent}">${entity.replyContent}</c:if>
                                    </td>
                                    <td>
                                        <c:if test="${entity.isWebVisible==0}"><a target="ajaxTodo" title="改为未通过审核？" href="/pms/comment/update?ids=${entity.id}&handle=1"><span style="color: #0000ff">通过</span></a></c:if>
                                        <c:if test="${entity.isWebVisible==-1}"><a target="ajaxTodo" title="改为通过审核？" href="/pms/comment/update?ids=${entity.id}&handle=2"><span style="color: #ff0000">未通过</span></a></c:if>
                                        <c:if test="${entity.isWebVisible==-2}">
                                            <a target="ajaxTodo" title="改为通过审核？"  href="/pms/comment/update?ids=${entity.id}&handle=2"><span style="color: #0000ff">通过</span></a>
                                            <a target="ajaxTodo" title="改为未通过审核？"  href="/pms/comment/update?ids=${entity.id}&handle=1"><span style="color: #ff0000">不通过</span></a>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${entity.sequence==0}"><a target="ajaxTodo" title="改为非置顶？" href="/pms/comment/update?ids=${entity.id}&handle=7"><span style="color: #ff0000">已置顶</span></a></c:if>
                                        <c:if test="${entity.sequence==-1}"><a target="ajaxTodo" title="改为已置顶？" href="/pms/comment/update?ids=${entity.id}&handle=8"><span style="color: #0000ff">非置顶</span></a></c:if>
                                    </td>
                                    <td>
                                        <c:if test="${entity.excellentCommentId==null||entity.excellentCommentId!=entity.id}"><a target="ajaxTodo" title="改为优秀评论？" href="/pms/product/updateExcellentComment?commentId=${entity.id}&proId=${entity.proId}"><span style="color: #ff0000">否</span></a></c:if>
                                        <c:if test="${entity.excellentCommentId!=null&&entity.excellentCommentId==entity.id}"><a target="ajaxTodo" title="取消优秀评论？" href="/pms/product/updateExcellentComment?commentId=0&proId=${entity.proId}"><span style="color: #0000ff">优</span></a></c:if>
                                    </td>
                                    <td>
                                        <c:if test="${entity.isAdminDel==0}"><a target="ajaxTodo" title="改为已删除？" href="/pms/comment/update?ids=${entity.id}&handle=3"><span style="color: #0000ff">未删除</span></a></c:if>
                                        <c:if test="${entity.isAdminDel==-1}"><a target="ajaxTodo" title="改为未删除？" href="/pms/comment/update?ids=${entity.id}&handle=4"><span style="color: #ff0000">已删除</span></a></c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:if>
                        </tbody>
                    </table>
                    </c:if>
                </div>

                <!-- 未审核 end-->

                <!-- 已审核 start-->

                <div>
                    <c:if test="${divIndex==4}">
                    <table class="table" width="100%" layoutH="220">
                        <thead>
                        <tr align="center">
                            <th width="2%"><input type="checkbox" group="ids" class="checkboxCtrl"></th>
                            <th width="4%">评分</th>
                            <th width="18%">评论内容</th>
                            <th width="5%">有用无用次数</th>
                            <th width="6%">订单编号</th>
                            <th width="8%">评价用户</th>
                            <th width="13%" align="center"  orderField="comment_time" class="<c:if test='${pc.pageInfo.orderField=="comment_time"}' >
                        ${pc.pageInfo.orderDirection}</c:if>">评价时间</th>
                            <%--<th width="13%">评价时间</th>--%>
                            <th width="6%">商品编号</th>
                            <th width="13%">商品标题</th>
                            <th width="10%">回复内容</th>
                            <th width="4%">审核通过</th>
                            <th width="5%">是否置顶</th>
                            <th width="3%">置优</th>
                            <th width="4%">是否删除</th>
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
                                    <td>${entity.score}</td>
                                    <td title="${entity.content}" style=" overflow:hidden;word-break: break-all; word-wrap: break-word;"><a height="500" title="查看评价详情" target="dialog" href="/pms/comment/detail?id=${entity.id}"><span style="color: #0000ff">${entity.content}</span></a></td>
                                    <td>${entity.usefulCount}/${entity.uselessCount}</td>
                                    <td>${entity.orderId}</td>
                                    <td>${entity.nickName}</td>
                                    <td><fmt:formatDate value="${entity.commentTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                                    <td>${entity.proId}</td>
                                    <td style=" overflow:hidden;word-break: break-all; word-wrap: break-word;">${entity.proName}</td>
                                    <td title="${entity.replyContent}" style=" overflow:hidden;word-break: break-all; word-wrap: break-word;">
                                        <c:if test="${empty entity.replyContent}"><a target="dialog" width="600" height="400" title="去回复" href="/pms/comment/reply?id=${entity.id}&handle=0"><span style="color: #ff0000">未回复</span></a></c:if>
                                        <c:if test="${!empty entity.replyContent}">${entity.replyContent}</c:if>
                                    </td>
                                    <td>
                                        <c:if test="${entity.isWebVisible==0}"><a target="ajaxTodo" title="改为未通过审核？" href="/pms/comment/update?ids=${entity.id}&handle=1"><span style="color: #0000ff">通过</span></a></c:if>
                                        <c:if test="${entity.isWebVisible==-1}"><a target="ajaxTodo" title="改为通过审核？" href="/pms/comment/update?ids=${entity.id}&handle=2"><span style="color: #ff0000">未通过</span></a></c:if>
                                        <c:if test="${entity.isWebVisible==-2}">
                                            <a target="ajaxTodo" title="改为通过审核？"  href="/pms/comment/update?ids=${entity.id}&handle=2"><span style="color: #0000ff">通过</span></a>
                                            <a target="ajaxTodo" title="改为未通过审核？"  href="/pms/comment/update?ids=${entity.id}&handle=1"><span style="color: #ff0000">不通过</span></a>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${entity.sequence==0}"><a target="ajaxTodo" title="改为非置顶？" href="/pms/comment/update?ids=${entity.id}&handle=7"><span style="color: #ff0000">已置顶</span></a></c:if>
                                        <c:if test="${entity.sequence==-1}"><a target="ajaxTodo" title="改为已置顶？" href="/pms/comment/update?ids=${entity.id}&handle=8"><span style="color: #0000ff">非置顶</span></a></c:if>
                                    </td>
                                    <td>
                                        <c:if test="${entity.excellentCommentId==null||entity.excellentCommentId!=entity.id}"><a target="ajaxTodo" title="改为优秀评论？" href="/pms/product/updateExcellentComment?commentId=${entity.id}&proId=${entity.proId}"><span style="color: #ff0000">否</span></a></c:if>
                                        <c:if test="${entity.excellentCommentId!=null&&entity.excellentCommentId==entity.id}"><a target="ajaxTodo" title="取消优秀评论？" href="/pms/product/updateExcellentComment?commentId=0&proId=${entity.proId}"><span style="color: #0000ff">优</span></a></c:if>
                                    </td>
                                    <td>
                                        <c:if test="${entity.isAdminDel==0}"><a target="ajaxTodo" title="改为已删除？" href="/pms/comment/update?ids=${entity.id}&handle=3"><span style="color: #0000ff">未删除</span></a></c:if>
                                        <c:if test="${entity.isAdminDel==-1}"><a target="ajaxTodo" title="改为未删除？" href="/pms/comment/update?ids=${entity.id}&handle=4"><span style="color: #ff0000">已删除</span></a></c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:if>
                        </tbody>
                    </table>
                    </c:if>
                </div>

                <!-- 已审核 end-->

            </div>
        </div>
    </form>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $(".delete").click(function () {
            $("#commentState").val($(this).attr("val"));
            alertMsg.confirm('确实要执行?', {
                okCall: function () {
                    $("#commentForm").submit();
                }
            });
        });

        $("#commentSize").click(function(){
            $(this).val(1);
        });

        $(".edit").click(function () {
            $("#commentState").val($(this).attr("val"));
            alertMsg.confirm('确实要执行?', {
                okCall: function () {
                    $("#commentForm").submit();
                }
            });
        });

        $("#revertSelectComment").click(function(){
            $(".searchContent input").each(function(){
                $(this).val("");
            });
            $("#commentSize").replaceWith("<input type='checkbox' name='commentSize' id='commentSize' >");
            $("#isTopComment").html("<option value=''>全部</option><option value='1'>已置顶</option><option value='0'>非置顶</option>");
        });
    });
</script>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<form id="pagerForm" method="post" action="/mms/passport/blacklist">
     <input type="hidden" name="pageNum" value="${pc.pageInfo.page}"/>
     <input type="hidden" name="numPerPage" value="${pc.pageInfo.pagesize}"/>
     <input type="hidden" name="userNameSearch" value="${userNameSearch}"/>
     <input type="hidden" name="nickNameSearch" value="${nickNameSearch}"/>
     <input type="hidden" name="emailSearch" value="${emailSearch}"/>
     <input type="hidden" name="mobileSearch" value="${mobileSearch}"/>
     <input type="hidden" name="birthdayBegin" value="${birthdayBegin}"/>
     <input type="hidden" name="birthdayEnd" value="${birthdayEnd}"/>
     <input type="hidden" name="createTimeBegin" value="${createTimeBegin}"/>
     <input type="hidden" name="createTimeEnd" value="${createTimeEnd}"/>
     <input type="hidden" name="mlwCoinBegin" value="${mlwCoinBegin}"/>
     <input type="hidden" name="mlwCoinEnd" value="${mlwCoinEnd}"/>
</form>
<div class="pageHeader">
    <form rel="pagerForm" action="/mms/passport/blacklist" method="post" id="queryFormBlacklidst" class="pageForm required-validate" onsubmit="return navTabSearch(this);">

        <div class="searchBar">
            <table class="searchContent" width="60%">
                <tr>
                    <td>
                        <label>用户ID：</label><input size="15"name="userNameSearch" id="userNameSearch" <c:if test="${!empty userNameSearch}">value="${userNameSearch}"</c:if> />
                    </td>
                    <td>
                        <label>昵称：</label> <input size="15" name="nickNameSearch" id="nickNameSearch" <c:if test="${!empty nickNameSearch}">value="${nickNameSearch}"</c:if> />
                    </td>
                 </tr>
                 <tr>   
                    <td>
                        <label>邮箱：</label><input size="15" name="emailSearch" id="emailSearch" <c:if test="${!empty emailSearch}">value="${emailSearch}"</c:if> />
                    </td>
                    <td>
                        <label>手机号：</label><input size="15" name="mobileSearch" id="mobileSearch" <c:if test="${!empty mobileSearch}">value="${mobileSearch}"</c:if> />
                    </td>
                 </tr>

                <!--
                <tr>
                    <td>
                        <label>订单数：</label>
                    </td>
                    <td>
                        <label>总消费金额：</label>
                    </td>
                 </tr>
                 -->
                 <tr>      
                    <td >
                        <label>生日：</label>
                        <p style="float: left;width: 105px;">
                         	<input type="text" class="date" pattern="yyyy-MM-dd" size="10"  name="birthdayBegin" id="birthdayBegin-queryFormBlacklidst" readonly="true" <c:if test="${!empty birthdayBegin}">value="${birthdayBegin}"</c:if> />
                        	<a class="inputDateButton" href="#" style="float: right;">选择</a>
                        </p>
                        <p style="float: left;width: 20px;">
                        	 至
                        </p>
                         <p style="float: left;width: 105px;">
                         	<input type="text" class="date" pattern="yyyy-MM-dd" size="10" name="birthdayEnd" id="birthdayEnd-queryFormBlacklidst" readonly="true" <c:if test="${!empty birthdayEnd}">value="${birthdayEnd}"</c:if> />
                         	<a class="inputDateButton" href="#" style="float: right;">选择</a>
                         	
                         </p>
                    </td>
                    <td>
                        <label>注册日期：</label>
                        <p style="float: left;width: 105px;">
                        	<input type="text" class="date" pattern="yyyy-MM-dd" size="10" name="createTimeBegin" id="createTimeBegin-queryFormBlacklidst" readonly="true" <c:if test="${!empty createTimeBegin}">value="${createTimeBegin}"</c:if> />
                        	<a class="inputDateButton" href="#" style="float: right;">选择</a>
                        </p>
                        <p style="float: left;width: 20px;">
                        	 至
                        </p>
                        <p style="float: left;width: 105px;">
                        	<input type="text" class="date" pattern="yyyy-MM-dd" size="10" name="createTimeEnd" id="createTimeEnd-queryFormBlacklidst" readonly="true" <c:if test="${!empty createTimeEnd}">value="${createTimeEnd}"</c:if> />
                        	<a class="inputDateButton" href="#" style="float: right;">选择</a>
                        </p>
                          
                    </td>
                 </tr>
                 <tr>
                    <%--<td>--%>
                        <%--<label>帐号余额：</label>--%>
                        <%--<input class="number" size="10" name="mlwCoinBegin" id="mlwCoinBegin" <c:if test="${!empty mlwCoinBegin}">value="${mlwCoinBegin}"</c:if> /> 至--%>
                        <%--<input class="number" size="10" name="mlwCoinEnd" id="mlwCoinEnd" <c:if test="${!empty mlwCoinEnd}">value="${mlwCoinEnd}"</c:if> />--%>
                    <%--</td>--%>
                    <td colspan="2">
                       
                        <div class="subBar">
                            <div class="buttonActive">
                                <div class="buttonContent">
                                     <button id="btnSubmitPassportBlackList" type="submit" >筛选</button>
                                </div>
                            </div>
                            <div class="button">
                                <div class="buttonContent">
                                    <button id="passport_blacklist_reset" >重置</button>
                                </div>
                            </div>
                        </div>
                    </td>
                    
                 </tr>
            </table>
        </div>
    </form>
</div>
<form  action="/mms/passport/undoDelBatch" method="post" id="undoDelBachForm" class="pageForm required-validate" onsubmit="return validateCallback(this,function(){
   navTabSearch($('#queryFormBlacklidst'));
},'是否要移出黑名单?');">
<div class="pageContent">
    <div class="panelBar">
        <div class="button">
            <div class="buttonContent">
                <button type="submit" >移出黑名单</button>
            </div>
            <div class="buttonContent">
                <a class="button" target="dialog" ref="dlg_black8888" href="/mms/passport/editBlackUser"><span>设置黑名单用户</span></a>
            </div>
        </div>
    </div>
        <table class="table" width="100%" layoutH="225" id="J-table">
            <thead>
            <tr>
             <th align="center"><input type="checkbox" group="ids" class="checkboxCtrl" /></th>
            <th align="center">用户ID</th>
            <th align="center">昵称</th>
            <th align="center">邮箱</th>
            <th align="center">手机号</th>
                <!--
            <th align="center">订单数</th>
            <th align="center">总消费金额</th>
            -->
            <th align="center">账户余额</th>
            <th align="center">生日</th>
            <th align="center">注册日期</th>
            <th align="center">黑名单</th>
             <th align="center">站内信</th>
             <th align="center">用户详情</th>
            </tr>
            </thead>
            <tbody>
            <c:if test="${empty pc.entityList}">
                <tr><td style="text-align: center;" ><font color="#dc143c">暂无数据</font></td></tr>
            </c:if>
            <c:if test="${!empty pc.entityList}">
                <c:forEach var="e" items="${pc.entityList}">
                     <tr  target="uid" rel="${e.uid}">
                    <td><input type="checkbox"  class="checkboxCtrl" name="ids" value="${e.uid}" /></td>
                    <td>${e.userName}</td>
                    <td>${e.nickName}</td>
                    <td>${e.email}</td>
                    <td>${e.mobile}</td>
                         <!--
                    <td>${e.orderCount}</td>
                    <td>${e.paySum}</td>
                    -->
                    <td>${e.mlwCoin}</td>
                    <td><fmt:formatDate value="${e.birthday}" pattern="yyyy-MM-dd"  /></td>
                    <td><fmt:formatDate value="${e.createTime}" pattern="yyyy-MM-dd HH:mm:ss"  /></td>
                    <td>
                        <a title="确定要移出黑名单?" target="ajaxTodo" href="/mms/passport/undoDel?uid=${e.uid}"><font color="red">移出黑名单</font></a>
                    </td>
                     <td>
                         <a target="dialog" href="/mms/passport/message?uid=${e.uid}" rel="dlg_page_message"><font color="red">发送消息</font></a>
                    </td>
                    <td>
                        <a href="/mms/passport/detail?uid=${e.uid}" target="dialog" rel="dlg_page8" title="用户详情页" width="800" height="480"><font color="red">查看详情</font></a>
                    </td>
                </tr>
                </c:forEach>
            </c:if>
            </tbody>
        </table>
    <%@include file="/WEB-INF/inc/per_page.jsp" %>
</div>
 </form>
<script>
    $(function(){

        $("#passport_blacklist_reset").click(function(e){
            e.preventDefault();
            $("#queryFormBlacklidst input").each(function(){
                $(this).val("");
            });
        });

        $('#btnSubmitPassportBlackList').click(function(e){
            e.preventDefault();

            var ba = $('#birthdayBegin-queryFormBlacklidst').val();
            var bb= $('#birthdayEnd-queryFormBlacklidst').val();
            var ca = $('#createTimeBegin-queryFormBlacklidst').val();
            var cb = $('#createTimeEnd-queryFormBlacklidst').val();

            var birthdayBegin = new Date(ba.replace(/-/g,"/"));
            var birthdayEnd = new Date(bb.replace(/-/g,"/"));
            var createTimeBegin = new Date(ca.replace(/-/g,"/"));
            var createTimeEnd = new Date(cb.replace(/-/g,"/"));

            var now = new Date();


            if(ba!='' && bb!=''){
                if(birthdayBegin>birthdayEnd){
                    alert("筛选条件中：生日的开始时间不能大于结束时间");
                    return false;
                }

                if(birthdayEnd>now){
                    alert("筛选条件中：生日的结束时间不能大于当前日期");
                    return false;
                }
            }else{

               if(birthdayBegin>now){
                   alert("筛选条件中：生日的开始时间不能大于当前日期");
                   return false;
               }

                if(birthdayEnd>now){
                    alert("筛选条件中：生日的结束时间不能大于当前日期");
                    return false;
                }

            }

            if(ca!='' && cb!=''){
                if(createTimeBegin>createTimeEnd){
                    alert("筛选条件中：注册日期的开始时间不能大于结束时间");
                    return false;
                }

                if(createTimeEnd>now){
                    alert("筛选条件中：注册日期的结束时间不能大于当前日期");
                    return false;
                }
            }else{

                if(createTimeBegin>now){
                    alert("筛选条件中：注册日期的开始时间不能大于当前日期");
                    return false;
                }

                if(createTimeEnd>now){
                    alert("筛选条件中：注册日期的结束时间不能大于当前日期");
                    return false;
                }

            }

            navTabSearch($('#queryFormBlacklidst'));

        });

    });
</script>

<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div class="pageContent">
    <form method="post" action="/mms/passport/createCode" class="pageForm required-validate"
          onsubmit="return validateCallback(this)">
        <input type="hidden" name="uid" value="${passport.uid}"/>
        <input type="hidden" name="currDiv" value="${currDiv}"/>

        <div class="formBar">
            <ul>
                <li>
                    <a class="button" href="/mms/passport/createCode?uid=${passport.uid}" width="600" height="450"  target="dialog" rel="dlg_page8888"><span>生成随机密码</span></a>
                </li>
            </ul>
        </div>
    </form>
</div>

<div id="tabsdiv" class="tabs" currentIndex="0" >

    <div class="tabsHeader">
        <div class="tabsHeaderContent">
            <ul>
                <li><a href="javascript:;"><span>注册信息</span></a></li>
                <li><a href="javascript:;"><span>个人资料</span></a></li>
                <li><a href="/mms/passport/myOrderList?uid=${passport.uid}" target="dialog" width="800" height="600" rel="dialog_myOrderList"><span>订单列表</span></a></li>
                <li><a href="/account/info?uid=${passport.uid}" target="dialog" width="800" height="600" rel="dialog_account_info"><span>账户余额</span></a></li>
                <li><a href="/account/giftCardInfo?uid=${passport.uid}" target="dialog" width="800" height="600" rel="dialog_account_giftCardInfo"><span>礼品卡余额</span></a></li>
            </ul>
        </div>
    </div>
    <div class="tabsContent">

        <div class="pageFormContent" layoutH="60">


            <table class="table" width="100%">

                <tr>
                    <td>用户ID:</td>
                    <td>${passport.userName}</td>
                    <td>当前昵称:</td>
                    <td>${passport.nickName}</td>
                </tr>

                <tr>
                    <td>当前邮箱:</td>
                    <td>${passport.email}</td>
                    <td>是否已验证:</td>
                    <td>
                        <c:choose>
                            <c:when test="${passport.emailActive ==1}">
                                已验证
                            </c:when>
                            <c:otherwise>
                                未验证
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>

                <tr>
                    <td>当前手机:</td>
                    <td>${passport.mobile}</td>
                    <td>是否已验证:</td>
                    <td>
                        <c:choose>
                            <c:when test="${passport.mobileActive ==1}">
                                已验证
                            </c:when>
                            <c:otherwise>
                                未验证
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>



            </table>

        </div>
        <div>
            <div class="pageFormContent" layoutH="60">

                <table class="table" width="100%">

                    <tr>

                        <td>
                            性别：
                        </td>
                        <td>
                            <c:if test="${extra.sex!=null}">

                                <c:choose>
                                    <c:when test="${extra.sex ==1}">
                                        男
                                    </c:when>
                                    <c:otherwise>
                                        女
                                    </c:otherwise>
                                </c:choose>

                            </c:if>


                        </td>

                        <td colspan="2" rowspan="4">

                            <img alt="" src="${passport.headUri}150x150.jpg" onerror="this.src='http://www.meiliwan.com/images/user_t.jpg150x150.jpg'"/>

                        </td>

                    </tr>

                    <tr>
                       <td>
                           家乡:
                       </td>
                        <td>
                          <!--   ${extra.countryName}&nbsp;&nbsp; -->
                            ${extra.provinceName}&nbsp;&nbsp;
                            ${extra.cityName}&nbsp;&nbsp;
                            ${extra.areaName}
                        </td>
                    </tr>
                    <tr>
                        <td>
                            生日:
                        </td>
                        <td>
                            <fmt:formatDate value="${extra.birthday}" pattern="yyyy-MM-dd"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            当前手机:
                        </td>
                        <td>
                            ${passport.mobile}
                        </td>
                    </tr>

                </table>

            </div>
        </div>

       <%-- <div >
        </div>
        <div>
        </div>--%>

    </div>
    <div class="tabsFooter">
        <div class="tabsFooterContent">
            <ul>
                <li>
                    <a class="button" href="/mms/passport/createCode?uid=${passport.uid}" target="dialog" rel="dlg_page8888"><span>订单列表</span></a>
                </li>
            </ul>
        </div>
    </div>
</div>




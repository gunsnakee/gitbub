<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<!DOCTYPE HTML>
<div>
    请在zkeeper上修改
</div>

<div class="pageContent">
    <table class="widefat post fixed" style="border:1px;border-color:red;">
       <thead>
        <tr>
            <th style="text-align:center;"  width="10%">配置</th>  
        </tr>
       </thead>
       <tbody id="log_content">
            <c:if test="${empty list}">
            <tr><td style="text-align: center;" COLSPAN="4"><font color="#dc143c">暂无数据</font></td></tr>
            </c:if>
            
            <c:if test="${!empty list}">
                

                <c:forEach var="e" varStatus="i" items="${list}">
                <tr>
                    <td>${e}</td>
                    
                </tr>
                </c:forEach>

            </c:if>

       </tbody>
      </table>


</div>
<script>            


</script>			



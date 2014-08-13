<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/inc/taglib.jsp" %>
<div>
    <form onsubmit="return dialogAjaxDone(this);" action="/allocation/requestExcludeSettingAdd" method="post">
        类型:
        <select name="type">
            <option value="dao">dao(数据库层)</option>
             <option value="iceS">iceS(服务层)</option>
             <option value="iceC">iceC(接口调用)</option>
             <option value="intr">intr(用户请求)</option>
        </select>
        <p></p>
        名称:<input type="text" name="name" value=""/>
        <p></p>
        大于:<input type="text" name="resumeTime" value=""/>毫秒
        <button type="submit" onkeydown="if(13==event.keyCode){return false;}">保存</button>
    </form>
    
</div>	
			



<%@ page language="java" pageEncoding="UTF-8" %>
<html>
<body>
	<p>设置密码</p>
	<form action="/mlw-web/connector/setpasswd" method="post">
	  <input type="hidden" name="refer" value="${url}" />
	  <input type="hidden" name="uid" value="${passport.uid}" />
	  <input type="text" name="nickName" value="${passport.nickName}" />
	  <input type="password" name="passwd" value="" />
	  <input type="submit" value="提 交" />
	</form>
</body>
</html>

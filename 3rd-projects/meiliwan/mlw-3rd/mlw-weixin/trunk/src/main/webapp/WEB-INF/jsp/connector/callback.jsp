<%@ page language="java" pageEncoding="UTF-8" %>
<html>
<body>
	<h1>Message : ${userInfo.nickname} Login Success</h1>	
	<p><img src="${userInfo.avatar}" /></p>
	<p>gender: ${userInfo.gender}</p>
	<p>msg: ${userInfo.msg}</p>
	<p>level: ${userInfo.level}</p>
	<p>ret: ${userInfo.ret}</p> 
	<p>tokenExpireIn: ${tokenExpireIn}</p>
</body>
</html>

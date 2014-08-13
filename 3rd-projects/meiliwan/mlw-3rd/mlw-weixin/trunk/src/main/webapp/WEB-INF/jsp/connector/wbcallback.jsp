<%@ page language="java" pageEncoding="UTF-8" %>
<html>
<body>
	<h1>Message : ${userInfo.screenName} Login Success</h1>	
	<p><img src="${userInfo.avatarLarge}" /></p>
	<p>gender: ${userInfo.gender}</p> 
	<p>msg: ${userInfo.profileImageUrl}</p>
</body>
</html>

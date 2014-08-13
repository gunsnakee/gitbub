<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>sf login page</title>
</head>
<body>

<form action="/channel/login" method="post">
    <div align="center">
      <span>美丽湾-APP合作业务查询</span> <br /> <br />
	  用户名：<input type="text" name="username" /> <br /> <br />
	  密&nbsp;&nbsp;码：<input type="password" name="password" /> <br/>  <br />
	  <input type="submit" value="登 录"> <br/><br/>
	  <span style="font-color:red;">${errorMsg }</span>
	</div>
</form>
</body>
</html>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>add page</title>
</head>

<%
    String path = request.getContextPath();//
    String basePath = request.getScheme()+"://"+request.getServerName()+":"
    +request.getServerPort()+path;
%>

<body>
<form method="post" action="<%=basePath%>/user/register"></br>
        userName:<input type="text" name="userName"/></br>
        password:<input type="password" name="password"/></br>
        age:<input type="number" name="age"/></br>
        isAdmin:<input type="text" name="isAdmin"/></br>
        <button type="submit">submit</button>
    </form>
</body>
</html>
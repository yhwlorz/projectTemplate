<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<body>
<h2 id="h2">wm</h2>
<script>
    function startTest(){
        //调接口
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.open("GET", "1/11", true);
        xmlhttp.send(null);
        //简单弹个窗吧
        window.alert("ing...");
    }
</script>
<input type="button" value="start" onclick="startTest()"/>
</body>
</html>
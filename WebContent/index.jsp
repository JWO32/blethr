<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    
<jsp:include page="inc/header.jsp"/>

<div id="mainContent">

<div id="headerBox">

<h1>Blethr</h1>

<h3>Are you ready to Blether?</h3>

<p>Blether: Verb (Chiefly Scottish): Talk long windedly without making much sense</p>

<form id="loginform" action="login" method="post" enctype="application/x-www-form-urlencoded" >
<table>
<tr>
<td><label>User Name:</label></td> <td><input type="text" name="UserID"/></td>
</tr>
<tr>
<td><label>Password: </label> </td> <td><input type="password" name="Password"/></td>
</tr>
</table>
<input id="loginbutton" type="submit"/>
</form>

</div>
</div>
<jsp:include page="inc/footer.jsp" />
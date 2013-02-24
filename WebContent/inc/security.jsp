<%
boolean loggedin = (Boolean) session.getAttribute("loggedin");

if(loggedin == false)
{
	response.sendRedirect("index.jsp");	
}
%>
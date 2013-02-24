<%
Boolean loggedin = (Boolean) session.getAttribute("loggedin");

if(loggedin == null)
{
	response.sendRedirect("../index.jsp");
}
%>
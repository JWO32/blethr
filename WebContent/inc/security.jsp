<%

// Doesn't work... 

Boolean loggedin = (Boolean) session.getAttribute("loggedin");
RequestDispatcher rd = null;

if(loggedin == null)
{
	pageContext.forward("../index.jsp");
}
%>
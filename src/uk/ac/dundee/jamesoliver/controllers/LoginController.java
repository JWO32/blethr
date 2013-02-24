package uk.ac.dundee.jamesoliver.controllers;

import uk.ac.dundee.jamesoliver.model.LoginAuthenticator;
import uk.ac.dundee.jamesoliver.model.UserDetails;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.sql.DataSource;

/**
 * Servlet implementation class LoginController
 */

public class LoginController extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	
	@Resource(name = "jdbc/blethrdb")
	private DataSource ds;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController()
    {
        super();     
        
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * 
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		String path = req.getRequestURI();
		String[] pathComponents = path.split("/");
		
		// Remove logged in condition from the session and redirect to the login page.
		if(pathComponents[3].equals("logout"))
		{
			req.getSession().setAttribute("loggedin", new Boolean(false));
			resp.sendRedirect("../index.jsp");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * 
	 * Login details are posted to login
	 * 
	 * Must make sure that mime type is set to application/x-www-form-urlencoded in view or parameters won't be picked up!
	 * 
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		String userid = null;
		String password = null;
		RequestDispatcher rd = null;
		
		if(req.getParameterMap().containsKey("UserID"))
			userid = (String) req.getParameter("UserID");
		
		if(req.getParameterMap().containsKey("Password"))
			password = (String) req.getParameter("Password");
		
		LoginAuthenticator la = new LoginAuthenticator(ds);
		
		UserDetails login = la.doLogin(userid, password);
		
		// if User details are found, add them to the session
		//

		if(login != null)
		{	
			req.setAttribute("Login",  login);
			req.getSession().setAttribute("loggedin",  new Boolean(true));
			rd = req.getRequestDispatcher("main.jsp");
			rd.forward(req, resp);
		}else
		{
			resp.sendRedirect("../index.jsp");
		}
	}
}

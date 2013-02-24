package uk.ac.dundee.jamesoliver.controllers;

import uk.ac.dundee.jamesoliver.model.UserManager;
import uk.ac.dundee.jamesoliver.model.UserDetails;
import uk.ac.dundee.jamesoliver.controllers.ListTransformer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.sql.DataSource;

import java.util.ArrayList;


/**
 * Servlet implementation class UserController
 */
public class UserController extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	
	@Resource(name = "jdbc/blethrdb")
	private DataSource ds;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserController() 
    {
        super();
       
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		UserManager UM = new UserManager(ds);
		String path = req.getRequestURI();
		String[] pathComponents = path.split("/");
		ArrayList<UserDetails> userList = new ArrayList<UserDetails>();
		boolean querySuccess = false;
		
		//
		// If the user doesn't supply enough arguments then set bad request and return
		if(pathComponents.length < 3)
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		//
		// If there is a bad path, set bad request and return
		if(!pathComponents[2].equals("user"))
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.setContentLength(0);
			resp.setContentType("text/plain");
			return;
		}
		
		if(pathComponents[3].equals("getallusers"))
		{
			
			userList = UM.fetchAllUsers();
			
			if(userList.size() > 0)
			{
				String json = ListTransformer.returnUsersFromArrayList(userList);
				
				
				PrintWriter output = resp.getWriter();
				
				resp.setContentType("application/json");
				output.write(json);
				querySuccess = true;
			}
		}
		
		//Set a user and a friend
		//
		if(pathComponents[3].equals("addfriend"))
		{
			int currentUser = Integer.parseInt(pathComponents[4]);
			int newFriend = Integer.parseInt(pathComponents[5]);
			
			querySuccess = UM.setFriend(currentUser, newFriend);
		}
		
		/*
		 * This must return the friends of the person in JSON format
		 * If no friend is found, return 0.
		 */
		if(pathComponents[3].equals("findfriends"))
		{
			int currentUser = Integer.parseInt(pathComponents[4]);
			
			userList = UM.fetchAllFriendsById(currentUser);
			
			if(userList.size() !=0)
			{
				String json = ListTransformer.returnUsersFromArrayList(userList);
				
				PrintWriter output = resp.getWriter();
				
				resp.setContentType("application/json");
				output.write(json);
				querySuccess = true;
			}else 
			{
				PrintWriter output = resp.getWriter();
				resp.setContentType("application/json");
				output.write("0");
			}
		}
			
		// If we register a problem, it will relate to stuff not being found so send back a 404 resource not found
		//
		if(querySuccess == false)
		{
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			resp.setContentLength(0);
			resp.setContentType("text/plain");
		}	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		UserManager UM = new UserManager(ds);
		String path = req.getRequestURI();
		String[] pathComponents = path.split("/");
		boolean querySuccess = false;
		
		//Add a new user
		// Not sure if this is a good idea -- TODO: check and remove if necessary
		//
		if(pathComponents[3].equals("register"))
		{
			String userName = req.getParameter("UserName");
			String userDescription = req.getParameter("Description");
			String userPassword = req.getParameter("Password");
			String userEmail = req.getParameter("Email");
			String isAdministrator = req.getParameter("isAdministrator");
			
			querySuccess = UM.addUser(userName, userDescription, userPassword, userEmail, isAdministrator);
		}
		
		if(querySuccess == false)
		{
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			resp.setContentLength(0);
			resp.setContentType("text/plain");
			resp.sendRedirect("main.jsp");
		}	
	}
	

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		UserManager UM = new UserManager(ds);
		String path = req.getRequestURI();
		String[] pathComponents = path.split("/");
		boolean querySuccess = false;
		
		if(pathComponents.length < 3)
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		if(!pathComponents[2].equals("user"))
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		
		//Delete user
		
		if(pathComponents[3].equals("delete"))
		{
			int userid = Integer.parseInt(pathComponents[4]);
			querySuccess = UM.deleteUser(userid);
		}
		
		if(querySuccess == true)
		{
			resp.setStatus(HttpServletResponse.SC_OK);
		}else
		{
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

}

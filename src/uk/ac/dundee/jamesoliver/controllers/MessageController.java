package uk.ac.dundee.jamesoliver.controllers;

import uk.ac.dundee.jamesoliver.model.MessageManager;
import uk.ac.dundee.jamesoliver.model.MessageDetails;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.util.ArrayList;

/**
 * Servlet implementation class MessageController
 */

public class MessageController extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
	@Resource(name = "jdbc/blethrdb")
	private DataSource ds;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MessageController() 
    {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		MessageManager mm = new MessageManager(ds);
		ArrayList<MessageDetails> messages;
		
		String path = req.getRequestURI();
		String[] pathComponents = path.split("/");
		StringBuffer jsonBuffer = new StringBuffer();
		String finalJson = new String();
		
		if(pathComponents.length < 3)
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		if(!pathComponents[2].equals("message"))
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		/* Convert list to json and then return to the view so it can be rendered.
		 * 
		 */
		if(pathComponents[3].equals("getAllMessages"))
		{
			//
			// Iterate through the ArrayList and produce a series of JSON objects.
			//
			
			messages = mm.fetchAllMessages();
			
			if(messages.size() != 0)
			{	
				jsonBuffer.append(ListTransformer.returnMessagesFromArrayList(messages));
				
				finalJson = jsonBuffer.toString();
				
				PrintWriter output = resp.getWriter();
				
				resp.setContentType("application/json");
				resp.setStatus(HttpServletResponse.SC_OK);
				output.write(finalJson);
				return;
				
			}else
			{
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.setContentType("text/plain");
				resp.setContentLength(0);
				return;
			}			
		}
		
		if(pathComponents[3].equals("findMessageByID"))
		{
			int messageid = Integer.valueOf(pathComponents[4]);
			
			messages = mm.fetchMessageById(messageid);
			
			if(messages.size() !=0)
			{
				jsonBuffer.append(ListTransformer.returnMessagesFromArrayList(messages));
				
				finalJson = jsonBuffer.toString();
				
				PrintWriter output = resp.getWriter();
				
				resp.setContentType("application/json");
				resp.setStatus(HttpServletResponse.SC_OK);
				output.write(finalJson);
				return;
			}else
			{
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.setContentType("text/plain");
				resp.setContentLength(0);
				return;
			}
		}
		
		if(pathComponents[3].equals("findMessagesByUsername"))
		{
			String userName = pathComponents[4];
			
			messages = mm.fetchMessagesByUserName(userName);
			
			if(messages.size() !=0)
			{
				jsonBuffer.append(ListTransformer.returnMessagesFromArrayList(messages));
				
				finalJson = jsonBuffer.toString();
				
				PrintWriter output = resp.getWriter();
				
				resp.setContentType("application/json");
				resp.setStatus(HttpServletResponse.SC_OK);
				output.write(finalJson);
				return;
			}else
			{
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.setContentType("test/plain");
				resp.setContentLength(0);
				return;
			}
		}
	}

	/**
	 * Add a message to the database
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		MessageManager mm = new MessageManager(ds);
		String path = req.getRequestURI();
		String[] pathComponents = path.split("/");
		boolean success = false;
		
		if(pathComponents.length < 3)
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.setContentLength(0);
			resp.setContentType("text/plain");
			return;
		}
		
		if(pathComponents[3].equals("addMessage"))
		{
			int userid = Integer.valueOf((String) pathComponents[4]);
			String message = (String) req.getParameter("comment");
			
			success = mm.insertMessage(userid,  message);
			if(success == true)
			{
				resp.setStatus(HttpServletResponse.SC_OK);
				resp.setContentType("text/plain");
				resp.setContentLength(0);
			}else
			{
				resp.setStatus(HttpServletResponse.SC_OK);
				resp.setContentLength(0);
				resp.setContentType("text/plain");
			}
		}	
	}
	
	
	/**
	 * 
	 * Delete a message from the database
	 * parameters should include the message id.
	 * 
	 */
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		MessageManager mm = new MessageManager(ds);
		boolean success = false;
		
		String path = req.getRequestURI();
		String[]  pathComponents = path.split("/");
		
		
		if(pathComponents.length < 3)
		{
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.setContentLength(0);
			resp.setContentType("text/plain");
			return;
		}
		

		if(pathComponents[3].equals("delete"))
		{
			int messageid = Integer.valueOf(pathComponents[4]);
			
			success = mm.deleteMessage(messageid);
			if(success == true)
			{
				resp.setStatus(HttpServletResponse.SC_OK);
				resp.setContentType("text/plain");
				resp.setContentLength(0);
			}else
			{
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				resp.setContentType("text/plain");
				resp.setContentLength(0);
			}
		}
	}
}
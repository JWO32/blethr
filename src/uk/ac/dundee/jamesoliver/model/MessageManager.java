package uk.ac.dundee.jamesoliver.model;

import uk.ac.dundee.jamesoliver.model.MessageDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

public class MessageManager
{
	private DataSource ds;
	
	private final String InsertMessage = "INSERT INTO blethrs (userid, comment) VALUES (?, ?);";
	private final String GetAllMessages = "SELECT blethrid, blethrs.userid, comment, users.username FROM blethrs, users WHERE blethrs.userid = users.userid ORDER BY blethrid DESC;";
	private final String GetAllMessagesByID = "SELECT blethrid, blethrs.userid, comment, users.username FROM blethrs, users WHERE blethrs.userid =? AND blethrs.userid = users.userid ORDER BY blethrid DESC;";
	private final String GetAllMessagesByUserName = "SELECT  blethrid, blethrs.userid, comment, users.username FROM blethrs, users WHERE users.username =? AND blethrs.userid = users.userid ORDER BY blethrid DESC;";
	private final String DeleteMessageByID = "DELETE FROM blethrs WHERE blethrid=?";

	public MessageManager(DataSource dataSource)
	{
		ds = dataSource;
		
	}
	
	public ArrayList<MessageDetails> fetchAllMessages()
	{
		String query = GetAllMessages;
		ArrayList<MessageDetails> messageList = new ArrayList<MessageDetails>();
		
		try
		{
			messageList = doFetchQuery(query, null);
		}catch(SQLException ex)
		{
			ex.printStackTrace();
		}
		
		// if we have messages, return the list.  Otherwise return null so we know there is nothing.
		if(messageList.size() > 0)
			return messageList;
		else
			return null;
	}
	
	public ArrayList<MessageDetails> fetchMessageById(int messageid)
	{
		String query = GetAllMessagesByID;
		String[] parameters = new String[2];
		ArrayList<MessageDetails> messageList = new ArrayList<MessageDetails>();
		
		parameters[0] = String.valueOf(messageid);
		
		try
		{
			messageList = doFetchQuery(query, parameters);
		}catch(SQLException ex)
		{
			ex.printStackTrace();
			messageList= null;
		}
		
		return messageList;
	}
	
	public ArrayList<MessageDetails> fetchMessagesByUserName(String username)
	{
		ArrayList<MessageDetails> messageList = new ArrayList<MessageDetails>();
		String query = GetAllMessagesByUserName;
		String[] parameters = new String[1];
		
		parameters[0] = username;
		
		try
		{
			messageList = doFetchQuery(query, parameters);
		}catch(SQLException ex)
		{
			ex.printStackTrace();
			messageList= null;
		}
		
		return messageList;
	}
	
	public ArrayList<MessageDetails> fetchFriendMessages(int userId, int friendId)
	{
		String query = GetAllMessagesByID;
		String[] parameters = new String[2];
		ArrayList<MessageDetails> messageList = new ArrayList<MessageDetails>();
		
		parameters[0] = String.valueOf(userId);
		parameters[1] = String.valueOf(friendId);
		
		try
		{
			messageList = doFetchQuery(query, parameters);
		}catch(SQLException ex)
		{
			ex.printStackTrace();
			messageList = null;
		}
		
		return messageList;
		
	}
	
	public boolean insertMessage(int userId, String message)
	{
		String query = InsertMessage;
		String[] parameters = new String[2];
		boolean success = false;
		
		parameters[0] = String.valueOf(userId);
		parameters[1] = message;
		
		try
		{
			success = doUpdateQuery(query, parameters);
		}catch (SQLException ex)
		{
			ex.printStackTrace();
		}
		
		return success;
	}
	
	public boolean deleteMessage(int messageid)
	{
		String query = DeleteMessageByID;
		String[] parameters = new String[1];
		boolean success = false;
		
		parameters[0] = String.valueOf(messageid);
		
		try
		{
			success = doUpdateQuery(query, parameters);
		}catch(SQLException ex)
		{
			ex.printStackTrace();
		}
		
		return success;
	}
	
	
	private ArrayList<MessageDetails> doFetchQuery(String query, String[] parameters) throws SQLException
	{
		ArrayList<MessageDetails> messageList = new ArrayList<MessageDetails>();
		MessageDetails tempMessage = new MessageDetails();
		
		Connection con = ds.getConnection();
		ResultSet rs = null;
		
		PreparedStatement prepSt = con.prepareStatement(query);
		
		if(parameters != null)
		{
			prepSt.setString(1, parameters[0]);
		}
		
		rs = prepSt.executeQuery();
		
		while(rs.next())
		{
			tempMessage.setUserID(rs.getString("userid"));
			tempMessage.setUserName(rs.getString("users.username"));
			tempMessage.setComment(rs.getString("comment"));
			tempMessage.setMessageID(rs.getString("blethrid"));
			messageList.add(tempMessage);
			tempMessage = new MessageDetails();
		}
		
		return messageList;
	}
	
	private boolean doUpdateQuery(String query, String[] parameters) throws SQLException
	{
		Connection con = ds.getConnection();
		int rowsUpdated;
		
		PreparedStatement prepSt = con.prepareStatement(query);
		
		if(parameters!= null)
		{
			for(int i = 0; i < parameters.length; i++)
			{
				prepSt.setString(i+1, parameters[i]);
			}
		}
		
		rowsUpdated = prepSt.executeUpdate();
		
		con.close();
		
		if (rowsUpdated == 0)
			return false;
		else
			return true;
	}

}

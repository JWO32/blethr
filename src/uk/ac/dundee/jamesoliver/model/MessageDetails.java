package uk.ac.dundee.jamesoliver.model;

public class MessageDetails 
{
	private String MessageID = null;
	private String UserID = null;
	private String UserName = null;
	private String Comment = null;
	private String Date = null;
	
	public MessageDetails()
	{
		
	}
	
	public void setMessageID(String messid)
	{
		MessageID = messid;
	}
	
	public void setUserID(String userid)
	{
		UserID = userid;
	}
	
	public void setUserName(String newUserName)
	{
		UserName = newUserName;
	}
	
	public void setComment(String comment)
	{
		Comment = comment;
	}
	
	public void setDate(String date)
	{
		Date = date;
	}
	
	public String getUserName()
	{
		return UserName;
	}
	
	public String getMessageID()
	{
		return MessageID;
	}
	
	public String getUserID()
	{
		return UserID;
	}
	
	public String getComment()
	{
		return Comment;
	}
	
	public String getDate()
	{
		return Date;
	}
	
}

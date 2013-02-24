package uk.ac.dundee.jamesoliver.model;

public class UserDetails
{
	private String UserID = null;
	private String UserName = null;
	private String Description = null;
	private String Password = null;
	private boolean isAdmin = false;
	private String Email = null;
	
	public UserDetails()
	{
		
	}
	
	public void setUserID(String newUserID)
	{
		UserID = newUserID;
	}
	
	public void setPassword(String newPassword)
	{
		Password = newPassword;
		
	}
	
	public void setUserName (String newUserName)
	{
		UserName = newUserName;
	}
	
	public void setEmail(String newEmail)
	{
		Email = newEmail;
	}
	
	public void setDescription (String newDescription)
	{
		Description = newDescription;
	}
	
	public void setAdmin(boolean newAdmin)
	{
		isAdmin = newAdmin;
	}
	
	public String getUserID()
	{
		return UserID;
	}
	
	public String getEmail()
	{
		return Email;
	}
	
	public String getPassword()
	{
		return Password;
	}
	
	public String getUserName()
	{
		return UserName;
	}
	
	public String getDescription()
	{
		return Description;
	}
	
	public boolean getisAdmin()
	{
		return isAdmin;
	}
	
	
}

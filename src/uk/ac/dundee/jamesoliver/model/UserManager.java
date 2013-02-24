package uk.ac.dundee.jamesoliver.model;

import java.util.ArrayList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import javax.sql.DataSource;

public class UserManager 
{
	DataSource ds;
	
	private final String AddUserQuery = "INSERT INTO users (username, description, password, email, isAdministrator) VALUES (?, ?, ?, ?, ?);";
	private final String DeleteUserQuery = "DELETE FROM users WHERE userid=?;";
	private final String FetchAllUserQuery = "SELECT * FROM users;";
	private final String FetchSingleUserQuery = "SELECT * FROM users WHERE userid=?;";
	private final String SetNewFriendQuery = "INSERT INTO pals (userid, palid) VALUES (?, ?);";
	private final String FetchFriendsByUserID = "SELECT * FROM users, pals WHERE pals.userid = ? AND pals.palid = users.userid;";
	
	public UserManager(DataSource dataSource)
	{
		ds = dataSource;
		
	}
	
	public UserManager()
	{
		
	}
	
	/**
	 * 
	 * Collects parameters for user delete and passes to doUpdateQuery()
	 * 
	 * @param userID
	 * @return
	 */
	
	public boolean deleteUser(int userID)
	{
		String dbQuery = DeleteUserQuery;
		String [] dbParameters = new String[1];
		boolean success = false;
		
		dbParameters[0] = Integer.toString(userID);
		
		try
		{
			success = doUpdateQuery(dbQuery, dbParameters);
		}catch (SQLException ex)
		{
			ex.printStackTrace();
		}	
		
		return success;
	}
	
	/**
	 * 
	 * 
	 * TODO: Make sure that the password is added in due course.
	 * @param userName
	 * @param userDescription
	 * @param isAdministrator
	 * @return
	 */
	public boolean addUser(String userName, String userDescription, String userPassword, String email, String isAdministrator)
	{
		String dbQuery = AddUserQuery;
		String[] dbParameters = new String[5];
		boolean success = false;
		
		dbParameters[0] = userName;
		dbParameters[1] = userDescription;
		dbParameters[2] = userPassword;
		dbParameters[3] = email;
		dbParameters[4] = isAdministrator;
		
		try
		{
			success = doUpdateQuery(dbQuery, dbParameters);
		}catch(SQLException ex)
		{
			ex.printStackTrace();
		}
		
		return success;
	}
	
	/**
	 * Takes the id of the currently logged in user and the member they want to be friends with. Passes parameters to doUpdateQuery.
	 * @param currentUser
	 * @param newFriend
	 * @return
	 */
	public boolean setFriend(int currentUser, int newFriend)
	{
		boolean success = false;
		String dbQuery = SetNewFriendQuery;
		String[] dbParameters = new String[2];
		
		dbParameters[0] = Integer.toString(currentUser);
		dbParameters[1] = Integer.toString(newFriend);
		
		try
		{
			success = doUpdateQuery(dbQuery, dbParameters);
		}catch(SQLException ex)
		{
			ex.printStackTrace();
		}
		
		return success;
	}
	
	
	/**
	 * Fetches a list of all the users registered on the system and returns them as an array list
	 * 
	 * @return
	 */
	public ArrayList<UserDetails> fetchAllUsers()
	{
		String dbQuery = FetchAllUserQuery;
		ArrayList <UserDetails> userList = null;
		
		try
		{
			userList = doFetchQuery(dbQuery, null);
		}catch(SQLException ex)
		{
			ex.printStackTrace();
		}
		
		return userList;
	}
	
	/**
	 * Fetch and return all of the friends belonging to a specific user
	 * @param userID
	 * @return
	 */
	
	public ArrayList<UserDetails> fetchAllFriendsById(int userid)
	{
		ArrayList<UserDetails> friendList = new ArrayList<UserDetails>();
		String query = FetchFriendsByUserID;
		
		String[] params = new String[1];
		
		params[0] = Integer.toString(userid);
		
		try
		{
			friendList = doFetchQuery(query, params);
		}catch(SQLException ex)
		{
			ex.printStackTrace();
		}
		
		return friendList;
	}
	
	/**
	 * Fetches the details of a single user and returns as an array list for conistency
	 * 
	 * @param userid
	 * @return
	 */
	public ArrayList<UserDetails> fetchSingleUser(int userid)
	{
		String dbQuery = FetchSingleUserQuery;
		String[] params = new String[1];
		
		ArrayList<UserDetails> userList = null;
		params[0] = Integer.toString(userid);
		
		try
		{
			userList = doFetchQuery(dbQuery, params);
		}catch(SQLException ex)
		{
			ex.printStackTrace();
			userList = null;
		}
		
		return userList;
		
	}
	
	/**
	 * 
	 * Send a select query to the database, fetch user details
	 * @param query
	 * @param parameters
	 * @return
	 * @throws SQLException
	 */
	private ArrayList<UserDetails> doFetchQuery(String query, String[] parameters) throws SQLException
	{
		ArrayList<UserDetails> userList = new ArrayList<UserDetails>();
		UserDetails tempUser = new UserDetails();
		
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
			tempUser.setUserID(rs.getString("userid"));
			tempUser.setUserName(rs.getString("username"));
			tempUser.setDescription(rs.getString("description"));
			tempUser.setPassword(rs.getString("password"));
			tempUser.setAdmin(Boolean.valueOf(rs.getString("isAdministrator")));
			userList.add(tempUser);
			tempUser = new UserDetails();
		}
	
		return userList;
	}
	
	/**
	 * Send an update query to the database to insert and delete user details
	 * @param query
	 * @param parameters
	 * @return
	 * @throws SQLException
	 */
	private boolean doUpdateQuery(String query, String[] parameters) throws SQLException
	{
		Connection con = ds.getConnection();
		int rowsUpdated;
		
		PreparedStatement prepSt = con.prepareStatement(query);
		
		for(int i = 0; i < parameters.length; i++)
		{
			prepSt.setString(i+1, parameters[i]);
		}
		
		rowsUpdated = prepSt.executeUpdate();
		
		con.close();
		
		if (rowsUpdated == 0)
			return false;
		else
			return true;
	}
}

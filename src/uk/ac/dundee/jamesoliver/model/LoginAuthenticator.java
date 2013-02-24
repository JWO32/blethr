package uk.ac.dundee.jamesoliver.model;



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

public class LoginAuthenticator
{
	private DataSource ds;
	
	
	private String LoginQuery = "SELECT * FROM users WHERE username=? AND password=?";
	private UserDetails Login = new UserDetails();
	
	public LoginAuthenticator(DataSource datasource)
	{
		ds = datasource;
	}
	
	public UserDetails doLogin(String user, String password)
	{
		try
		{
			Login = this.findUser(user, password);
			
			if(Login == null)
			{
				//Error
				
			}

		}catch (SQLException ex)
		{
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
		
		return Login;
	}
	
	private UserDetails findUser(String user, String password) throws SQLException
	{
		Connection con = ds.getConnection();
		ResultSet rs = null;
		UserDetails ld = new UserDetails();
		
		PreparedStatement prepSt = con.prepareStatement(LoginQuery);
		prepSt.setString(1, user);
		prepSt.setString(2, password);
		
		rs = prepSt.executeQuery();
		
		//Silly little hack to get the number of rows in the resultset, there should be 1 only.
		
		if(!rs.next())
		{
			con.close();
			return null;
		}else
		{
			ld.setUserName(rs.getString("username"));
			ld.setUserID(rs.getString("userid"));
			ld.setDescription(rs.getString("description"));
			ld.setAdmin(rs.getBoolean("isAdministrator"));
		}

		con.close();
		
		return ld;
	}
}

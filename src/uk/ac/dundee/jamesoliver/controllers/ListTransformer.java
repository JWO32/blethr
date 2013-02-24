package uk.ac.dundee.jamesoliver.controllers;

import uk.ac.dundee.jamesoliver.model.*;
import java.util.ArrayList;

import com.google.gson.*;

/**
 * 
 * @author JWO
 *
 *	Takes an ArrayList and uses google gson class to create a JSON string for the client.
 *
 */

public class ListTransformer 
{	
	private static Gson gsn = new Gson();
	
	public static String returnUsersFromArrayList(ArrayList<UserDetails> userList)
	{
		return gsn.toJson(userList);
	}
	
	public static String returnMessagesFromArrayList(ArrayList<MessageDetails> messageList)
	{	
		return gsn.toJson(messageList);
	}
}

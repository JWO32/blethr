<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="uk.ac.dundee.jamesoliver.model.UserDetails" %>    
    
<jsp:include page="inc/security.jsp"/>
    
<jsp:include page="inc/header.jsp"/>

<%
UserDetails User = (UserDetails) request.getAttribute("Login");
%>


<!-- 

Javascript Ajax functions.
 -->

<script type="text/javascript">

$('#admin').hide();

$('#admin').click(function()
{
	$('#admin').toggle();

});

$('#refreshbutton').click(function()
	{
		refreshMessages();
	});

//Refresh all messages every 5 seconds
self.setInterval(refreshMessages, 5000);

//makes an ajax rest call to add a new message
var addMessage = function()
{
	var userid = <%=User.getUserID()%>;
	if($('#comment').val().length == 0)
	{
		alert('Please enter a message!');
	 	return;
	}
	var message = $('#addNewMessageForm').serialize();
	
	$.ajax({
		url: 'message/addMessage/'+userid,
		type: "post",
		dataType: "text",
		data: message,
		
		error: function()
		{
			alert( "Error: Server not responding" );
			
		},

		success: function(data)
		{
			alert('Blethr Added!');
		}});
	//
	//Pause for 3/4 of a  second before refreshing the message list
	//
	setTimeout(refreshMessages, 750);
};

// makes an ajax rest call to add a new friend to the friend list
var addFriend = function()
{
	var friendId = $('#allUsers').val();
	$.ajax({
	url: 'user/addfriend/'+<%=User.getUserID()%>+'/'+friendId,
	type: "GET",
	dataType: "text",
	
	error: function()
	{
		alert( "Error: Server not responding" );
		
	},

	success: function(data)
	{
		alert('Friend Added!');
		findFriends();
	}});	
};

//Makes an ajax rest call to refresh (update) the list of messages
var refreshMessages = function()
{
	$('#messages').empty();
	$.ajax({
		url:  "message/getAllMessages",
		type: "GET",
		dataType: "json",
		
		error: function()
		{
			alert( "Error: Server not responding" );
			
		},

		success: function( strData )
		{
			jQuery.each(strData, function(i, val)
			{
				var messageDetails;		
				messageDetails = '<div class="message">';
				messageDetails += '<span class="username">User: '+val.UserName+'</span>';
				messageDetails += '<span class="comment">Comment: '+val.Comment+'</span>';
				if(<%=User.getUserID()%> == val.UserID)
				{
					messageDetails+='<a href="#"onclick="deleterefresh('+val.MessageID+');">Delete Blethr</a>';
				}
				
				messageDetails += '</div>';
				$('#messages').append(messageDetails);
			});
		
		}
	});	
};

//makes an ajax rest call to delete the specified message
var deleterefresh = function(messageID)
{
	$.ajax({
		url: 'message/delete/'+messageID,
		type: "DELETE",
		dataType: "text",
		
		error: function()
		{
			alert( "Error: Server not responding" );
			
		},

		success: function(data)
		{
			alert('Message Deleted!');
		}});
	//
	//Pause for 3/4 of a  second before refreshing the message list
	//
	setTimeout(refreshMessages, 750);
};

//Download friends into option box
$("#friendList").on("change", function()
{
	findFriends();
});

//Makes an ajax rest call to find friends of the current user
var findFriends = function()
{
	var url = "user/findfriends/<%=User.getUserID()%>";
	$.ajax({
		url:  url,
		type: "GET",
		dataType: "json",
		
		error: function()
		{
			alert( "Error: Server not responding" );
			
		},

		success: function(data)
		{
			jQuery.each(data, function(i, val)
			{
				var friend;		
				friend = '<option value="'+val.UserID+'">';
				friend += val.UserName;
				friend += '</option>';
				$('#friendList').append(friend);
			});	
		}
	});
};
</script>

<script type="text/javascript">

//makes an ajax rest call to insert a user into the database
var addNewUser = function()
{
	var userDetails = $('#addUserForm').serialize();
	
	$.ajax({
		url:  "user/register",
		type: "POST",
		data: userDetails,		
		error: function()
		{
			alert( "Error: Server not responding" );
			
		},
		success: function(data)
		{
			alert(data);
			alert('New User added!');
		}
	});
};	

//Makes an ajax rest call to fetch all users
var getUserNames = function()
{
	$.ajax({
		url:  "user/getallusers",
		type: "GET",
		dataType: "json",
		
		error: function()
		{
			alert( "Error: Server not responding" );
			
		},
		
		success: function(data)
		{
			$('#deleteUser').empty();
			$('#allUsers').empty();
			
			jQuery.each(data, function(i, val)
			{
				var user;		
				user = '<option value="'+val.UserID+'">';
				user += val.UserName;
				user += '</option>';
				$('#deleteUser').append(user);
				$('#allUsers').append(user);
			});	
		}
	});
};

//Makes an ajax rest call to delete a specified user
var deleteuser = function()
{
	var userid = $('#deleteUser').val();
	
	$.ajax({
		url:  "user/delete/"+userid,
		type: "DELETE",
		dataType: "text",
		
		error: function()
		{
			alert( "Error: Server not responding" );
			
		},
		
		success: function(data)
		{
			alert('User Deleted!');
			
			setTimeout(getUserNames, 750);
		}
	});
	
};


</script>

<div id="mainContent">

<div id="headerBox">
<h1>Blethr!</h1>
<p>Twitter for Scottish people!</p>
<p>Welcome <%=User.getUserName() %></p>
<p> <a href="login/logout">Log Out of Blethr!</a></p>
<form id="displayFriendMessages">
<label>Your Friends</label>
<select id="friendList">
</select><br/>
<label>Available Friends</label>
<select id="allUsers">

</select>

<input type="button" value="Add Friend" onclick="addFriend();" id="displayFriendMessagesButton">
</form>

<form id="addNewMessageForm">
<h4>Blethr Box</h4>
<label>What do you want to blether about?</label><input type="text" name="comment" id="comment">

<input type="button" value="Send Blethr!" id="sendBlethrButton" onclick="addMessage()"/>

</form>

</div>

<!-- All messages are appended to this div -->
<div id="messages">


</div>

<%
if(User.getisAdmin() == true)
{%>
	<jsp:include page="admin.jsp"/>
<%	
}
%>

</div>

<jsp:include page="inc/footer.jsp" />
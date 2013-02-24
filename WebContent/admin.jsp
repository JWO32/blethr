<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    

<h1 id="headeradmin">Administration</h1>
   
<div id="admin">
<p>You currently have admin rights. You can create a new user by entering the details below.</p>
<p>To delete a user, select from the drop down list and click delete!</p>

<form id="addUserForm">
<label>User Name:</label>
<input type="text" name="UserName"/>
<label>Description:</label>
<input type="text" name="Description"/>
<label>E-mail:</label>
<input type="text" name="Email"/>
<label>Password:</label>
<input type="text" name="Password"/>
<label>Administrator</label>
<select name="isAdministrator">
	<option value="1">Administrator</option>
	<option value="0">Standard User</option>
</select>
<input type="button" value="Add User" onclick="addNewUser();"/>
</form>



<form id="deleteUserList">

<label>Select a User to Delete:</label>

<select id="deleteUser">
</select>

<input type="button" value="Delete User" onclick="deleteuser();"/>
</form>

</div>
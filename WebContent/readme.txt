Blethr: A twitter clone for Scottish people.


Requires MySQL with database installed first.

Restful Interface

The rest interface is group into 3 separate sets of functions:

/login/

/login/logout (GET)

/user

/user/register (form data) (POST)
/user/addfriend/userid/friendid (GET)
/user/findfriends/userid (GET)
/user/delete/userid (DELETE)


/message/

/message/addMessage (POST)
/message/getAllMessages (GET)
/message/getMessageById/messageid (GET)
/message/findMessagesByUsername/username (GET)
/message/delete/messageid (DELETE)


Not all functionality has been built into the client due to time constraints.
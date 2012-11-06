# Description

This project is for send MYQL request from an Android device (1.5+)

## Getting Started

- Download https://github.com/MichaelMinelli/Android-MYSQL-Request/archive/master.zip
- Upload the php file on your server
- Add the java file to your project

NO MODIFICATION OF THE CODE WILL BE NECESSARY

## Exemple of use

### Request with a result (SELECT)

	//Initialize the class with the information for the identification
	MYSQL_Request request = new MYSQL_Request(MYSQL_Page, MYSQL_Database, MYSQL_Server, MYSQL_Username, MYSQL_Password);
	
	//Set the request
	request.setRequest("SELECT * FROM tblTest");
	
	//Execute the requeste
	request.getServerData();
	
	//While a result will be available
	while (request.getNextEntry())
	{
		//Get the JSON value
		JSONObject data = request.getJsonValue();
		try
		{
			debug.d("result", data.getString("testID"));
		} 
		catch (Exception e)
		{}
	}
	
### Request without result (INSERT INTO, DELETE, ALTER, ...)

	//Initialize the class with the information for the identification
	MYSQL_Request request = new MYSQL_Request(MYSQL_Page, MYSQL_Database, MYSQL_Server, MYSQL_Username, MYSQL_Password);

	//Set the request
	request.setRequest("INSERT INTO tblTest (testID, testName) VALUES (NULL, 'GitHub');");

	//Execute the request
	request.executeRequest();
	
## Debug (Devlopement) mode

For activate the Debug(Devlopment) mode you have to modify the first variable of the JAVA class (named "DEBUG")
and set her to "true". It's show you the address of the called page, the send request and all error the class can do.

This mode is for debug your eventually error in SQL request.

## Reporting bugs & contributing & support

- For send support and contributing send a mail to : contact@application.famille-minelli.ch
- For issue, please send to : https://github.com/MichaelMinelli/Android-MYSQL-Request/issues

## Authors

Michaël Minelli

- https://twitter.com/Michael_Minelli
- http://github.com/MichaelMinelli
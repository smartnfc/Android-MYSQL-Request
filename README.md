# Description

This project is for send MYSQL request from an Android device (1.5+)

## Changelog

10.11.2012 : Add Encryption of identifiers and response (will come : the encryption of the request)

## Getting Started

- Download https://github.com/MichaelMinelli/Android-MYSQL-Request/archive/master.zip
- Modify initialization vector and encryption key (16bits)
- Upload the php file on your server
- Add the two java files to your project
- Modify initialization vector and encryption key (16bits) in "MYSQL_Request.java" by the same value at the PHP file

NO OTHER MODIFICATION WILL BE NECESSARY

## Exemple of use

### Request with a result (SELECT)

	//Initialize the class with the information for the identification
	MYSQL_Request request = new MYSQL_Request(MYSQL_Page, MYSQL_Database, MYSQL_Server, MYSQL_Username, MYSQL_Password);
	
	//Set the request
	request.setRequest("SELECT * FROM tblTest");
	
	//Execute the request
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
	
### Request with inserted id (INSERT INTO)

	//Initialize the class with the information for the identification
	MYSQL_Request request = new MYSQL_Request(MYSQL_Page, MYSQL_Database, MYSQL_Server, MYSQL_Username, MYSQL_Password);

	//Set the request
	request.setRequest("INSERT INTO tblTest (testID, testName) VALUES (NULL, 'GitHub');");

	//Execute the request with the recuperation of the ID
	request.executeRequestWithID();
	
	//Stock the ID
	int id = Integer.parseInt(requete.getResultID());
	
## Debug (Development) mode

For activate the Debug(Devlopment) mode you have to modify the first variable of the JAVA class (named "DEBUG")
and set her to "true". It's show you the address of the called page, the send request and all error the class can do.

This mode is for debug your eventually error in SQL request.

## Reporting bugs & contributing & support

- For send support and contributing send a mail to : contact@application.famille-minelli.ch
- For issue, please send to : https://github.com/MichaelMinelli/Android-MYSQL-Request/issues

## License

Project under Creative Commons License (CC-BY-SA) (Paternity, Modification allowed (Published allowed under the same condition), sharing allowed, commercial use (free) allowed)

## Authors / Contributors

### Authors

Michaël Minelli (Swiss)

- https://twitter.com/Michael_Minelli
- http://github.com/MichaelMinelli

### Contributors

Android Snippets (for the encryption part)
- http://www.androidsnippets.com/encrypt-decrypt-between-android-and-php
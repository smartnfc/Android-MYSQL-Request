<?php
/*
 * PHP file for do request to a MYSQL Database and show the result in JSON
 * 
 * @Author Micha�l Minelli
 * @Version : 1.1.0
 *
 * More info at https://github.com/MichaelMinelli/Android-MYSQL-Request
 *
 * Project under Creative Commons License (CC-BY-SA) (Paternity, Modification allowed (Published under the same condition), sharing allowed, commercial use (free) allowed)
 */

// Get the connexion parameter
$dbname = $_GET['dbname'];
$host = $_GET['host'];
$username = $_GET['username'];
$password = $_GET['password'];

//Open the connexion to the database
$connect = mysql_connect($host, $username, $password);
mysql_select_db($dbname, $connect);

//Execute the request
$q=mysql_query(str_replace("\\'", "'", $_GET['request']));
while($e=mysql_fetch_assoc($q))
		$output[]=$e;
 
//Show the JSON value
print(json_encode($output));
 
//Close the MYSQL connexion
mysql_close();
?>
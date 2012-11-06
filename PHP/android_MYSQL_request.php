<?php
$dbname = $_GET['dbname'];
$host = $_GET['host'];
$username = $_GET['username'];
$password = $_GET['password'];

$connect = mysql_connect($host, $username, $password);
mysql_select_db($dbname, $connect);

$q=mysql_query(str_replace("\\'", "'", $_GET['request']));
while($e=mysql_fetch_assoc($q))
		$output[]=$e;
 
print(json_encode($output));
 
mysql_close();

?>
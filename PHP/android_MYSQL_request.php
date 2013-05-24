<?php

/*
* PHP file for do request to a MYSQL Database and show the result in JSON
*
* @Author Michaël Minelli
* @Version : 2.0.0
*
* More info at https://github.com/MichaelMinelli/Android-MYSQL-Request
*
* Project under Creative Commons License (CC-BY-SA) (Paternity, Modification allowed (Published under the same condition), sharing allowed, commercial use (free) allowed)
*/

/**
* Thanks to androidsnippets for this ancryption/decryption class
*
* @author http://www.androidsnippets.com/encrypt-decrypt-between-android-and-php
*/
class MCrypt
{
    // Initialization vector and secret key for encryption (CHANGE IT! AND CHANGE IN THE JAVA FILE BY THE SAME VALUE)
    private $iv = 'fedcba9876543210'; // Must be 16bits
    private $key = '0123456789abcdef'; //Must be 16bits

    function __construct()
    {
        
    }

    function encrypt($str)
    {
        //$key = $this->hex2bin($key);
        $iv = $this->iv;

        $td = mcrypt_module_open('rijndael-128', '', 'cbc', $iv);

        mcrypt_generic_init($td, $this->key, $iv);
        $encrypted = mcrypt_generic($td, $str);

        mcrypt_generic_deinit($td);
        mcrypt_module_close($td);

        return bin2hex($encrypted);
    }

    function decrypt($code)
    {
        //$key = $this->hex2bin($key);
        $code = $this->hex2bin($code);
        $iv = $this->iv;

        $td = mcrypt_module_open('rijndael-128', '', 'cbc', $iv);

        mcrypt_generic_init($td, $this->key, $iv);
        $decrypted = mdecrypt_generic($td, $code);

        mcrypt_generic_deinit($td);
        mcrypt_module_close($td);

        return utf8_encode(trim($decrypted));
    }

    protected function hex2bin($hexdata)
    {
        $bindata = '';

        for ($i = 0; $i < strlen($hexdata); $i += 2)
        {
            $bindata .= chr(hexdec(substr($hexdata, $i, 2)));
        }

        return $bindata;
    }

}

$mcrypt = new MCrypt();

// Get the connexion parameter
$dbname = $mcrypt->decrypt($_REQUEST['dbname']);
$host = $mcrypt->decrypt($_REQUEST['host']);
$username = $mcrypt->decrypt($_REQUEST['username']);
$password = $mcrypt->decrypt($_REQUEST['password']);

//Open the connexion to the database
$connect = new PDO('mysql:host=' . $host . ';dbname=' . $dbname, $username, $password);
$connect->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$connect->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
$connect->exec("SET CHARACTER SET utf8");

//Execute the request
$result = $connect->prepare(str_replace("\\'", "'", $_REQUEST['request']));
$result->execute();

//If we need to get de last insert id
if (isset($_REQUEST['isNeedToGetId']))
	echo $mcrypt->encrypt($connect->lastInsertId());
else
	print($mcrypt->encrypt(json_encode($result->fetchAll()))); //Show the JSON value

//Close the MYSQL connexion
$result->closeCursor();
?>
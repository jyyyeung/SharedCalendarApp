<?php

class DbConnect
{

	//Class constructor
	function __construct()
	{
	}

	//This method will connect to the database
	function connect()
	{
		//Including the constants.php file to get the database constants
		include_once dirname(__FILE__) . '/Constants.php';

		//connecting to mysql database
		$mysqli = @new mysqli(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME);

		//Checking if any error occurred while connecting
		if ($mysqli->connect_error) {
			echo 'Errno: ' . $mysqli->connect_errno;
			echo '<br>';
			echo 'Error: ' . $mysqli->connect_error;
			return null;
		}

		echo 'Success: A proper connection to MySQL was made.';
		echo '<br>';
		echo 'Host information: ' . $mysqli->host_info;
		echo '<br>';
		echo 'Protocol version: ' . $mysqli->protocol_version;

		//finally returning the connection link
		return $mysqli;
	}
}

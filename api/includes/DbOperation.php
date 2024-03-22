<?php

class DbOperation
{
	private $con;

	function __construct()
	{
		require_once dirname(__FILE__) . '/DbConnect.php';
		$db = new DbConnect();
		$this->con = $db->connect();
	}

	//adding a account to database
	public function createAccount($username, $email, $password_hash, $settings)
	{
		$stmt = $this->con->prepare("INSERT INTO `account` (`username`, `email`, `password_hash`, `created_date`, `updated_date`, `settings`) VALUES (?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ?)");
		// $stmt = $this->con->prepare("INSERT INTO artists (name, genre) VALUES (?, ?)");
		$stmt->bind_param("ss", $username, $email, $password_hash, $settings);
		if ($stmt->execute())
			return true;
		return false;
	}

	//fetching all accounts from the database
	public function getAccounts()
	{
		$stmt = $this->con->prepare("SELECT id, username, email FROM account");
		$stmt->execute();
		$stmt->bind_result($id, $username, $email);
		$accounts = array();

		while ($stmt->fetch()) {
			$temp = array();
			$temp['id'] = $id;
			$temp['username'] = $username;
			$temp['email'] = $email;
			array_push($accounts, $temp);
		}
		return $accounts;
	}
}

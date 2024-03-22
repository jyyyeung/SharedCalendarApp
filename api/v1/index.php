<?php

//adding DbOperation file 
require_once '../includes/DbOperation.php';

//response array
$response = array();

//if a get parameter named op is set we will consider it as an api call 
if (isset($_GET['op'])) {

	//switching the get op value 
	switch ($_GET['op']) {

			//if it is add account 
			//that means we will add an account 
		case 'createaccount':
			if (isset($_POST['username']) && isset($_POST['email']) && isset($_POST['password_hash']) && isset($_POST['settings'])) {
				$db = new DbOperation();
				if ($db->createAccount($_POST['username'], $_POST['email'], $_POST['password_hash'], $_POST['settings'])) {
					$response['error'] = false;
					$response['message'] = 'Account added successfully';
				} else {
					$response['error'] = true;
					$response['message'] = 'Could not add account';
				}
			} else {
				$response['error'] = true;
				$response['message'] = 'Required Parameters are missing';
			}
			break;

			//if it is getartist that means we are fetching the records
		case 'getaccounts':
			$db = new DbOperation();
			$accounts = $db->getAccounts();
			if (count($accounts) <= 0) {
				$response['error'] = true;
				$response['message'] = 'Nothing found in the database';
			} else {
				$response['error'] = false;
				$response['accounts'] = $accounts;
			}
			break;

		default:
			$response['error'] = true;
			$response['message'] = 'No operation to perform';
	}
} else {
	$response['error'] = false;
	$response['message'] = 'Invalid Request';
}

//displaying the data in json 
echo json_encode($response);

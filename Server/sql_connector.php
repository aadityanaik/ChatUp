<?php
ini_set('display_startup_errors', 1);
ini_set('display_errors', 1);
error_reporting(-1);

DEFINE('DB_USER','helpuser');
DEFINE('DB_PASSWORD','helppass');
DEFINE('DB_HOST','localhost');
DEFINE('DB_NAME','helpline');

//Depending on style, index.php must also be changed
//Default = OOP
//Procedural
/*
$dbc = @mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME)
	OR die('Could not connect to MySQL <br />' . mysqli_connect_error());

*/
//OOP
$dbc = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);

if($dbc->connect_errno) {
	die('Could not connect to MySQL <br />' . $dbc->connect_error);
}
?>

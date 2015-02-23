<?php

$servername = "mysql.stud.ntnu.no";
$username = "perod_hillsize";
$password = "7xFsMPvM";
$dbname = "perod_hsm";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
	die('Connection failed: '.$conn->connect_error);
} 

?>
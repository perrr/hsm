<?php

$servername = "mysql.stud.ntnu.no";
$username = "perod_hillsize";
$password = "LKFa7JuW";
$dbname = "perod_hsm";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
	die('Connection failed: '.$conn->connect_error);
} 

$id = mysqli_real_escape_string($conn, $_GET['id']);
if (is_numeric($id)) {
	$sqlN = 'SELECT * FROM nation WHERE id = '.$id;
	$nation = $conn->query($sqlN);

	if ($nation->num_rows > 0) {
		$rowN = $nation->fetch_assoc();
		echo '<h1>'.$rowN['name'].'</h1>';
		echo '<table class="stats-table"><tr><td class="l">ID</td><td class="r">'.$rowN['id'].'</td></tr>'
		.'<tr><td class="l">Abbrevation</td><td class="r">'.$rowN['abbrevation'].'</td></tr>'
		.'<tr><td class="l">National record</td><td class="r">'.$rowN['national_record'].' m</td></tr></table>';
	}
	else {
		echo 'Nation does not exist';
	}
}
else {
	echo '<h1>Search result</h1>';
	$sqlH = 'SELECT id, name FROM nation WHERE name LIKE \'%'.$id.'%\' OR abbrevation = \''.$id.'\' ORDER BY name ASC';
	$result = $conn->query($sqlH);

	if ($result->num_rows > 0) {
		while($row = $result->fetch_assoc()) {
			echo '<a href="index.php?element=nation.php&id='.$row['id'].'">'.$row['name'].'</a><br />';
		}
	}
	else {
		echo 'No nations found.';
	}
}
?>
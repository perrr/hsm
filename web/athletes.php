<html>
<head>
<title>Hillsize Manager Statistics</title>
<link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>

<h1>Athletes</h1>

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

$sql = 'SELECT id, first_name, last_name FROM athlete ORDER BY last_name ASC, first_name ASC';
$result = $conn->query($sql);

while($row = $result->fetch_assoc()) {
	echo '<a href="index.php?element=athlete.php&id='.$row['id'].'">'.$row['first_name'].' '.$row['last_name'].'</a><br />';
}
?>

</body>
</html>
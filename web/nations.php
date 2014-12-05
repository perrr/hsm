<html>
<head>
<title>Hillsize Manager Statistics</title>
<link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>

<h1>Nations</h1>

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

$sql = 'SELECT id, name FROM nation ORDER BY name ASC';
$result = $conn->query($sql);

while($row = $result->fetch_assoc()) {
	echo '<a href="index.php?element=nation.php&id='.$row['id'].'">'.$row['name'].'</a><br />';
}
?>

</body>
</html>
</html>
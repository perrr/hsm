<?php

include('connection.php');

$sql = 'SELECT id, first_name, last_name FROM athlete ORDER BY last_name ASC, first_name ASC';
$result = $conn->query($sql);

while($row = $result->fetch_assoc()) {
	echo '<a href="index.php?element=athlete&id='.$row['id'].'">'.$row['first_name'].' '.$row['last_name'].'</a><br />';
}
?>
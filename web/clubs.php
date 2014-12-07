<?php

include('connection.php');

$sql = 'SELECT id, name FROM club ORDER BY name ASC';
$result = $conn->query($sql);

while($row = $result->fetch_assoc()) {
	echo '<a href="index.php?element=club&id='.$row['id'].'">'.$row['name'].'</a><br />';
}
?>
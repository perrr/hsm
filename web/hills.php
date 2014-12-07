<?php

include('connection.php');

$sql = 'SELECT id, name, hs FROM hill ORDER BY name ASC, hs ASC';
$result = $conn->query($sql);

while($row = $result->fetch_assoc()) {
	echo '<a href="index.php?element=hill&id='.$row['id'].'">'.$row['name'].' (HS '.$row['hs'].')</a><br />';
}
?>
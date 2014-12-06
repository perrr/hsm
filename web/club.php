<?php

include('connection.php');

$id = mysqli_real_escape_string($conn, $_GET['id']);
if (is_numeric($id)) {
	$sqlC = 'SELECT * FROM club WHERE id = '.$id;
	$club = $conn->query($sqlC);
	$sqlA = 'SELECT id, first_name, last_name FROM athlete WHERE club = '.$id;
	$athletes = $conn->query($sqlA);

	if ($club->num_rows > 0) {
		$rowC = $club->fetch_assoc();
		$sqlN = 'SELECT name FROM nation WHERE id = '.$rowC['nation'];
		$nation = $conn->query($sqlN);
		$rowN = $nation->fetch_assoc();
		echo '<h1>'.$rowC['name'].'</h1>';
		echo '<table class="stats-table"><tr><td class="l">ID</td><td class="r">'.$rowC['id'].'</td></tr>'
		.'<tr><td class="l">Nation</td><td class="r"><a href="index.php?element=nation.php&id='.$rowC['nation'].'">'.$rowN['name'].'</a></td></tr>'
		.'<tr><td class="l">Manager</td><td class="r">'.$rowC['manager'].'</td></tr><tr><td></td><td class="r">';
		echo ($rowC['active'] == 1 ? 'Active' : 'Inactive').'</td></tr></table>';
		if ($athletes->num_rows > 0) {
			echo '<h2>Athletes</h1>';
			while ($row = $athletes->fetch_assoc()) {
				echo '<a href="index.php?element=athlete.php&id='.$row['id'].'">'.$row['first_name'].' '.$row['last_name'].'</a><br />';
			}
		}
	}
	else {
		echo 'Club does not exist';
	}
}
else {
	echo '<h1>Search result</h1>';
	$sqlH = 'SELECT id, name FROM club WHERE name LIKE \'%'.$id.'%\' ORDER BY name ASC';
	$result = $conn->query($sqlH);

	if ($result->num_rows > 0) {
		while ($row = $result->fetch_assoc()) {
			echo '<a href="index.php?element=club.php&id='.$row['id'].'">'.$row['name'].'</a><br />';
		}
	}
	else {
		echo 'No clubs found.';
	}
}
?>
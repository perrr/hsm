<?php

include('connection.php');

$id = mysqli_real_escape_string($conn, $_GET['id']);
if (is_numeric($id)) {
	$sqlN = 'SELECT * FROM nation WHERE id = '.$id;
	$nation = $conn->query($sqlN);
	$sqlA = 'SELECT id, first_name, last_name FROM athlete WHERE nation = '.$id;
	$athletes = $conn->query($sqlA);
	$sqlC = 'SELECT id, name FROM club WHERE nation = '.$id;
	$clubs = $conn->query($sqlC);
	$sqlH = 'SELECT id, name, hs FROM hill WHERE nation = '.$id;
	$hills = $conn->query($sqlH);

	if ($nation->num_rows > 0) {
		$rowN = $nation->fetch_assoc();
		echo '<h1>'.$rowN['name'].'</h1>';
		echo '<table class="stats-table"><tr><td class="l">ID</td><td class="r">'.$rowN['id'].'</td></tr>'
		.'<tr><td class="l">Abbrevation</td><td class="r">'.$rowN['abbrevation'].'</td></tr>'
		.'<tr><td class="l">National record</td><td class="r">'.$rowN['national_record'].' m</td></tr></table>';
		if ($athletes->num_rows > 0) {
			echo '<h2>Athletes</h1>';
			while ($row = $athletes->fetch_assoc()) {
				echo '<a href="index.php?element=athlete.php&id='.$row['id'].'">'.$row['first_name'].' '.$row['last_name'].'</a><br />';
			}
		}
		if ($clubs->num_rows > 0) {
			echo '<h2>Clubs</h1>';
			while ($row = $clubs->fetch_assoc()) {
				echo '<a href="index.php?element=club.php&id='.$row['id'].'">'.$row['name'].'</a><br />';
			}
		}
		if ($hills->num_rows > 0) {
			echo '<h2>Hills</h1>';
			while ($row = $hills->fetch_assoc()) {
				echo '<a href="index.php?element=hill.php&id='.$row['id'].'">'.$row['name'].' (HS '.$row['hs'].')</a><br />';
			}
		}
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
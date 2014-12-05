<?php

include('connection.php');

$id = mysqli_real_escape_string($conn, $_GET['id']);
if (is_numeric($id)) {
	$sqlA = 'SELECT * FROM athlete WHERE id = '.$id;
	$athlete = $conn->query($sqlA);

	if ($athlete->num_rows > 0) {
		$rowA = $athlete->fetch_assoc();
		$sqlN = 'SELECT name FROM nation WHERE id = '.$rowA['nation'];
		$nation = $conn->query($sqlN);
		$rowN = $nation->fetch_assoc();
		$sqlC = 'SELECT name FROM club WHERE id = '.$rowA['club'];
		$club = $conn->query($sqlC);
		$rowC = $club->fetch_assoc();
		echo '<h1>'.$rowA['first_name'].' '.$rowA['last_name'].'</h1>';
		echo '<table class="stats-table"><tr><td class="l">ID</td><td class="r">'.$rowA['id'].'</td></tr>'
		.'<tr><td class="l">Age</td><td class="r">'.$rowA['age'].'</td></tr>'
		.'<tr><td class="l">Club</td><td class="r"><a href="index.php?element=club.php&id='.$rowA['club'].'">'.$rowC['name'].'</a></td></tr>'
		.'<tr><td class="l">Nation</td><td class="r"><a href="index.php?element=nation.php&id='.$rowA['nation'].'">'.$rowN['name'].'</a></td></tr>'
		.'<tr><td class="l">Personal best</td><td class="r">'.$rowA['personal_best'].' m</td></tr><tr><td></td><td class="r">';
		echo ($rowA['active'] == 1 ? 'Active' : 'Inactive').'</td></tr></table>';
	}
	
	else {
		echo 'Athlete does not exist';
	}
}
else {
	echo '<h1>Search result</h1>';
	$sqlH = 'SELECT id, first_name, last_name FROM athlete WHERE CONCAT(first_name, \' \', last_name) LIKE \'%'.$id.'%\' ORDER BY last_name ASC, first_name ASC';
	$result = $conn->query($sqlH);

	if ($result->num_rows > 0) {
		while($row = $result->fetch_assoc()) {
			echo '<a href="index.php?element=athlete.php&id='.$row['id'].'">'.$row['first_name'].' '.$row['last_name'].'</a><br />';
		}
	}
	else {
		echo 'No athletes found.';
	}
}
?>
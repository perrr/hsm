<?php

include('connection.php');

$id = mysqli_real_escape_string($conn, $_GET['id']);
if (is_numeric($id)) {
	$sqlH = 'SELECT * FROM hill WHERE id = '.$id;
	$hill = $conn->query($sqlH);

	if ($hill->num_rows > 0) {
		$rowH = $hill->fetch_assoc();
		$sqlN = 'SELECT name FROM nation WHERE id = '.$rowH['nation'];
		$nation = $conn->query($sqlN);
		$rowN = $nation->fetch_assoc();
		$sqlA = 'SELECT first_name, last_name FROM athlete WHERE id = '.$rowH['hill_record_athlete'];
		$athlete = $conn->query($sqlA);
		$rowA = $athlete->fetch_assoc();
		echo '<h1>'.$rowH['name'].'</h1>';
		echo '<table class="stats-table"><tr><td class="l">ID</td><td class="r">'.$rowH['id'].'</td></tr>'
		.'<tr><td class="l">Nation</td><td class="r"><a href="index.php?element=nation.php&id='.$rowH['nation'].'">'.$rowN['name'].'</a></td></tr>'
		.'<tr><td class="l">K-point</td><td class="r">'.$rowH['k-point'].' m</td></tr>'
		.'<tr><td class="l">Hillsize</td><td class="r">'.$rowH['hs'].' m</td></tr>'
		.'<tr><td class="l">Hill record</td><td class="r">'.$rowH['hill_record'].' m</td></tr>'
		.'<tr><td></td><td class="r"><a href="index.php?element=athlete.php&id='.$rowH['hill_record_athlete'].'">'.$rowA['first_name'].' '.$rowA['last_name'].'</a></td></tr>'
		.'<tr><td></td><td class="r">'.$rowH['hill_record_date'].'</td></tr></table>';
	}
	else {
		echo 'Hill does not exist';
	}
}
else {
	echo '<h1>Search result</h1>';
	$sqlH = 'SELECT id, name, hs FROM hill WHERE name LIKE \'%'.$id.'%\' ORDER BY name ASC, hs ASC';
	$result = $conn->query($sqlH);

	if ($result->num_rows > 0) {
		while($row = $result->fetch_assoc()) {
			echo '<a href="index.php?element=hill.php&id='.$row['id'].'">'.$row['name'].' (HS '.$row['hs'].')</a><br />';
		}
	}
	else {
		echo 'No hills found.';
	}
}
?>
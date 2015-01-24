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
		.'<tr><td class="l">Club</td><td class="r"><a href="index.php?element=club&id='.$rowA['club'].'">'.$rowC['name'].'</a></td></tr>'
		.'<tr><td class="l">Nation</td><td class="r"><a href="index.php?element=nation&id='.$rowA['nation'].'">'.$rowN['name'].'</a></td></tr>'
		.'<tr><td class="l">Personal best</td><td class="r">'.$rowA['personal_best'].' m</td></tr><tr><td></td><td class="r">';
		echo ($rowA['active'] == 1 ? 'Active' : 'Inactive').'</td></tr></table>';
	}
	
	else {
		echo 'Athlete does not exist';
	}
	$sqlWC = 'SELECT competition, points, placement FROM competition_result WHERE athlete = '.$id;
	$WC = $conn->query($sqlWC);
	if ($WC->num_rows > 0) {
	echo '<script type="text/javascript"
	src="https://www.google.com/jsapi?autoload={
	\'modules\':[{
	\'name\':\'visualization\',
	\'version\':\'1\',
	\'packages\':[\'corechart\']
	}]
	}"></script>
	<script type="text/javascript">
	google.setOnLoadCallback(drawChart);
	function drawChart() {
	var data = google.visualization.arrayToDataTable([';

	echo '[\'Season\', \'Result\'],';
	$first = 1;
	while($rowWC = $WC->fetch_assoc()) {
		$sqlSeason = 'SELECT season, world_cup FROM competition WHERE id = '.$rowWC['competition'];
		$season = $conn->query($sqlSeason);
		$rowS = $season->fetch_assoc();
		if ($rowS['world_cup'] == 1) {
			if ($first == 1) {
				$first = 0;
			}
			else {
				echo ',';
			}
			echo '[\'Season '.$rowS['season'].'\', '.$rowWC['placement'].']';
		}
	}
	echo ']);
	';

	echo 'var options = {
	title: \'World Cup Results\',
	vAxis: { direction: -1 },
	legend: { position: \'bottom\' }
	};
	var chart = new google.visualization.LineChart(document.getElementById(\'curve_chart\'));
	chart.draw(data, options);
	}
	</script>';

	echo '<div class="graph" id="curve_chart" style="width: 900px; height: 500px"></div>';
	}
}
else {
	echo '<h1>Search result</h1>';
	$sqlH = 'SELECT id, first_name, last_name FROM athlete WHERE CONCAT(first_name, \' \', last_name) LIKE \'%'.$id.'%\' ORDER BY last_name ASC, first_name ASC';
	$result = $conn->query($sqlH);

	if ($result->num_rows > 0) {
		while($row = $result->fetch_assoc()) {
			echo '<a href="index.php?element=athlete&id='.$row['id'].'">'.$row['first_name'].' '.$row['last_name'].'</a><br />';
		}
	}
	else {
		echo 'No athletes found.';
	}
}
?>

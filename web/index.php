<!DOCTYPE html>

<html>
<head>
<title>Hillsize Manager Statistics</title>
<link rel="stylesheet" type="text/css" href="style.css" />
<link rel="icon" href="favicon.ico" type="image/x-icon" />
</head>
<body>

<?php

echo '<div class="center">
<div class="first-bar"><a href="index.php?element=start.php">Hillsize Manager Statistics</a></div>
<form action="index.php" method="get" class="search"><input type="hidden" name="element" value="athlete">
<div class="bar"><a href="index.php?element=athletes">Athletes</a> <input type="text" name="id" size=15 placeholder=" ID or name"></div>
<input type="submit" value="" class="invisible"></form>
<form action="index.php" method="get" class="search"><input type="hidden" name="element" value="club">
<div class="bar"><a href="index.php?element=clubs">Clubs</a> <input type="text" name="id" size=15 placeholder=" ID or name"></div>
<input type="submit" value="" class="invisible"></form>
<form action="index.php" method="get" class="search"><input type="hidden" name="element" value="nation">
<div class="bar"><a href="index.php?element=nations">Nations</a> <input type="text" name="id" size=15 placeholder=" ID or name"></div>
<input type="submit" value="" class="invisible"></form>
<form action="index.php" method="get" class="search"><input type="hidden" name="element" value="hill">
<div class="bar"><a href="index.php?element=hills">Hills</a> <input type="text" name="id" size=15 placeholder=" ID or name"></div>
<input type="submit" value="" class="invisible"></form></div>';

echo '<div class="container">';
include($_GET['element'].'.php');
echo '</div>';
?>

</body>
</html>

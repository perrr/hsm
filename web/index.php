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
<div class="first-bar"><a href="index.php?element=start.html">Hillsize Manager Statistics</a></div>
<form action="index.php" method="get" class="search"><input type="hidden" name="element" value="athlete.php">
<div class="bar"><a href="index.php?element=athletes.php">Athletes</a> <input type="text" name="id" size=15 placeholder=" ID or name"></div>
<input type="submit" value="" class="invisible"></form>
<form action="index.php" method="get" class="search"><input type="hidden" name="element" value="club.php">
<div class="bar"><a href="index.php?element=clubs.php">Clubs</a> <input type="text" name="id" size=15 placeholder=" ID or name"></div>
<input type="submit" value="" class="invisible"></form>
<form action="index.php" method="get" class="search"><input type="hidden" name="element" value="nation.php">
<div class="bar"><a href="index.php?element=nations.php">Nations</a> <input type="text" name="id" size=15 placeholder=" ID or name"></div>
<input type="submit" value="" class="invisible"></form>
<form action="index.php" method="get" class="search"><input type="hidden" name="element" value="hill.php">
<div class="bar"><a href="index.php?element=hills.php">Hills</a> <input type="text" name="id" size=15 placeholder=" ID or name"></div>
<input type="submit" value="" class="invisible"></form></div>';

echo '<div class="container">';
include($_GET['element']);
echo '</div>';
?>

</body>
</html>

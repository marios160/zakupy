<?php
	$email = $_POST['email'];
	if(!filter_var($email, FILTER_VALIDATE_EMAIL)){
		echo "Niepoprawny email";
		return;
	}
	$user = 'octicos';
	$password = 'KiLuGiLu1';
	$database = 'octicos';
	
	mysql_connect('mysql.cba.pl', $user, $password);
	@mysql_select_db($database) or die("Bł±d bazy danych");
	
	$query = "SELECT * FROM uzytkownicy WHERE user LIKE ".$email;
	$result = mysql_query($query);
	if (mysql_num_rows($result) < 1){
		$query = "INSERT INTO uzytkownicy (user,listaZakupow) VALUES (".$email.",);"
		if(!mysql_query($query)){
			echo "Nie zarejestrowano";
		} else {
			echo "Zarejestrowano";
		}
	} else {
		echo "Uzytkownik juz istnieje";
	}

?>
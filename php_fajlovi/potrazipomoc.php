<?php

require_once 'connection.php'; 
 if($_SERVER['REQUEST_METHOD'] == 'POST')
 {
 
	 $ime = $_POST['ime'];
	 $naslov =$_POST['naslov']; 
	 $opis= $_POST['opis']; 
	 $slika=$_POST['slika']; 
	 $telefon=$_POST['telefon']; 
	 $adresa=$_POST['adresa']; 
	 $latitude= $_POST['latitude']; 
	 $longitude=$_POST['longitude']; 
	 $idkorisnika=$_POST['idkorisnika']; 
	 $idvolontera=$_POST['idvolontera']; 
	 $status=$_POST['status']; 

		$sql="INSERT INTO products (ime,naslov,opis,slika,telefon,adresa,latitude,longitude,idkorisnika,idvolontera,status) values ('$ime','$naslov', '$opis', '$slika','$telefon','$adresa', '$latitude', '$longitude','$idkorisnika', '$idvolontera', '$status')";
		if(mysqli_query($conn,$sql))
		{
			$response['error'] = false;   
			$response['message'] = 'uspesno postavljeno';   
			mysqli_close($conn);
		}
	
 
 
 }
 
?>


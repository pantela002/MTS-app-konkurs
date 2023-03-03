<?php

	require_once 'connection.php';  
	$stmt = $conn->prepare("SELECT * FROM products");
  
	$stmt->execute();
 
 $stmt->bind_result($idpomoci,$ime,$naslov,$opis,$slika,$telefon,$adresa,$latitude,$longitude,$idkorisnika,$idvolontera,$status);
 
 $products = array(); 
 

 while($stmt->fetch()){
 $temp = array();
 $temp['idpomoci']=$idpomoci;
 $temp['ime'] = $ime; 
 $temp['naslov'] = $naslov; 
 $temp['slika'] = $slika; 
 $temp['opis'] = $opis; 
 $temp['telefon'] = $telefon; 
 $temp['adresa'] = $adresa; 
 $temp['latitude'] = $latitude; 
 $temp['longitude'] = $longitude; 
 $temp['idkorisnika'] = $idkorisnika; 
 $temp['idvolontera']=$idvolontera; 
 $temp['status'] = $status; 
 array_push($products, $temp);
 }
 echo json_encode($products);

?>
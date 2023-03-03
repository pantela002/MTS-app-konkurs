<?php

	require_once 'connection.php';  
	$stmt = $conn->prepare("SELECT username,bodovi FROM users WHERE kor='Volonter' ORDER BY cast(bodovi as unsigned)");
  
	$stmt->execute();
 
 $stmt->bind_result($username,$bodovi);
 
 $products = array(); 
 

 while($stmt->fetch()){
 $temp = array();
 $temp['username'] = $username; 
 $temp['bodovi'] = $bodovi; 
 array_push($products, $temp);
 }
 echo json_encode($products);

?>
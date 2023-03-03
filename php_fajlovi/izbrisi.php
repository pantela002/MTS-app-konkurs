<?php

require_once 'connection.php'; 
 if($_SERVER['REQUEST_METHOD'] == 'POST')
 {
	
	$idpomoci = $_POST['idpomoci'];

		$sql = "DELETE FROM products WHERE idpomoci=$idpomoci";

		if ($conn->query($sql) === TRUE) {
			$response['error'] = false;   
			$response['message'] = 'Uspesno';   
		} else {
		  echo "Problem 5: " . $conn->error;
		}
}
mysqli_close($conn);
		 
		 
	 
 

 
?>

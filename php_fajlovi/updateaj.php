<?php

require_once 'connection.php'; 
 if($_SERVER['REQUEST_METHOD'] == 'POST')
 {
	$idpomoci = $_POST['idpomoci'];
	$status=$_POST['status'];
	$idvolontera = $_POST['idvolontera'];

		$sql = "UPDATE products SET status='$status', idvolontera='$idvolontera' WHERE idpomoci=$idpomoci";

		if ($conn->query($sql) === TRUE) {
		  	$response['error'] = false;   
			$response['message'] = 'Uspesno postavljeno';   
			
		} else {
		  echo "Problem 3 : " . $conn->error;
		}
}
mysqli_close($conn);
		 
		 
	 
 

 
?>

<?php

require_once 'connection.php'; 
 if($_SERVER['REQUEST_METHOD'] == 'POST')
 {
	
	$id = $_POST['id'];

		$sql = "UPDATE users SET bodovi=bodovi+1 WHERE id=$id";
		if ($conn->query($sql) === TRUE) {
		 	$response['error'] = false;   
			$response['message'] = 'Uspesno';   
		} else {
		  echo "Problem 4: " . $conn->error;
		}
}
mysqli_close($conn);
		 
		 
	 
 

 
?>

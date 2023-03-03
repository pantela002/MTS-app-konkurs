<?php  
$servername = "localhost";  
$username = "**********88";  
$password = "*********";  
$database = "*********";   
$conn = new mysqli($servername, $username, $password, $database);  
if ($conn->connect_error) {  
    die("Problem sa konekcijom: " . $conn->connect_error);  
}  
?>  

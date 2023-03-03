<?php   

  require_once 'connection.php';  
  $response = array();  
  if(isset($_GET['apicall'])){  
  switch($_GET['apicall']){  
  case 'signup':  
    if(isTheseParametersAvailable(array('username','email','password','kor','bodovi'))){  
    $username = $_POST['username'];   
    $email = $_POST['email'];   
    $password = md5($_POST['password']);  
    $kor = $_POST['kor'];   
	$bodovi = $_POST['bodovi']; 
   
    $stmt = $conn->prepare("SELECT id FROM users WHERE username = ? OR email = ?");  
    $stmt->bind_param("ss", $username, $email);  
    $stmt->execute();  
    $stmt->store_result();  
   
    if($stmt->num_rows > 0){  
        $response['error'] = true;  
        $response['message'] = 'Vec ste registrovani';  
        $stmt->close();  
    }  
    else{  
        $stmt = $conn->prepare("INSERT INTO users (username, email, password, kor,bodovi) VALUES (?, ?, ?, ?,?)");  
        $stmt->bind_param("sssss", $username, $email, $password, $kor, $bodovi);  
   
        if($stmt->execute()){  
            $stmt = $conn->prepare("SELECT id, username, email, kor,bodovi FROM users WHERE username = ?");   
            $stmt->bind_param("s",$username);  
            $stmt->execute();  
            $stmt->bind_result($id, $username, $email, $kor,$bodovi);  
            $stmt->fetch();  
   
            $user = array(  
            'id'=>$id,   
            'username'=>$username,   
            'email'=>$email,  
            'kor'=>$kor,
			'bodovi'=>$bodovi
            );  
   
            $stmt->close();  
   
            $response['error'] = false;   
            $response['message'] = 'Uspesno ste se registrovali';   
            $response['user'] = $user;   
        }  
    }  
   
}  
else{  
    $response['error'] = true;   
    $response['message'] = 'parametri nedostupni';   
}  
break;   





case 'login':  
  if(isTheseParametersAvailable(array('username', 'password'))){  
    $username = $_POST['username'];  
    $password = md5($_POST['password']);   
   
    $stmt = $conn->prepare("SELECT id, username, email, kor,bodovi FROM users WHERE username = ? AND password = ?");  
    $stmt->bind_param("ss",$username, $password);  
    $stmt->execute();  
    $stmt->store_result();  
    if($stmt->num_rows > 0){  
    $stmt->bind_result($id, $username, $email, $kor,$bodovi);  
    $stmt->fetch();  
    $user = array(  
    'id'=>$id,   
    'username'=>$username,   
    'email'=>$email,  
    'kor'=>$kor,
	'bodovi'=>$bodovi
    );  
   
    $response['error'] = false;   
    $response['message'] = 'Uspesno ste se prijavili';   
    $response['user'] = $user;   
 }  
 else{  
    $response['error'] = false;   
    $response['message'] = 'Korisnicko ime ili lozinka nisu tacni';  
 }  
}  
break;   
default:   
 $response['error'] = true;   
 $response['message'] = 'Problem 1';  
}  
}  
else{  
 $response['error'] = true;   
 $response['message'] = 'Problem 2';  
}  
echo json_encode($response);  
function isTheseParametersAvailable($params){  
foreach($params as $param){  
 if(!isset($_POST[$param])){  
     return false;   
  }  
}  
return true;   
}  
?>  
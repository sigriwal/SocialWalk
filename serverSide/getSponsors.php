<?php
include_once 'config.php';

$conn = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
if (mysqli_connect_errno($this->connect)){
echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$json_array = array();
 
$query = "select * from sponsors";
 
$result = mysqli_query($conn, $query);

if(mysqli_num_rows($result) > 0){
 
while ($row = mysqli_fetch_assoc($result)) {
 
$json_array["sponsors"][] = $row;
}
}

?>
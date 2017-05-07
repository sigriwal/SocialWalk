<?php
 
include_once 'db_connect.php';
 
class Contacts{
 
private $db;
 
private $db_table = "my_contcts";
 
public function __construct(){
 
$this->db = new DbConnect();
 
}
 
public function getAllContacts(){
 
$json_array = array();
 
$query = "select * from my_contacts";

$com = $this->db->getDb();
 
$result = mysqli_query($com, $query);

if(mysqli_num_rows($result) > 0){
 
while ($row = mysqli_fetch_assoc($result)) {
 
$json_array["contacts"][] = $row;
 
}
 
}
 
return $json_array;
 
}
 
}
 
?>
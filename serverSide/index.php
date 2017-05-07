<?php
require_once 'my_contacts.php';
$contactObject = new Contacts();
$json_contact_objects = $contactObject->getAllContacts();
echo json_encode($json_contact_objects);
?>
<?php
ini_set('display_startup_errors', 1);
ini_set('display_errors', 1);
error_reporting(-1);

//Dummy data
/*
$from = 101;
$to = 2;
$msg = 'i have the high ground';
*/

//SQL connector
$sqlconnector_script = 'sql_connector.php';

$param = array();
foreach ($_GET as $name => $value) {
  array_push($param, $value);
}

/**
 Log Messages
*/ //----------------------------------------------------------------------


if ($param[0] > 100){
$sid = (string)$param[0];
$ufile = fopen("./chats/{$sid}.txt", "a") or die("Unable to open file!");

fwrite($ufile, "\n");
fwrite($ufile, $param[2]);
fclose($ufile);
}
/**
 JSONify data
*/ //----------------------------------------------------------------------

class Template{
  public $from;
  public $to;
  public $msg;
  function __construct($gets){
    $this->from = $gets['from'];
    $this->to = $gets['to'];
    $this->msg = $gets['msg'];
  }
}
$customobj = new Template($_GET);

$inbox_id = $param[1];
//Display the custom response
$jsonobj = json_encode($customobj);

echo file_put_contents("./chats/inbox_{$inbox_id}.txt",$param[2]);

sleep(4.5);
unlink("./chats/inbox_".$inbox_id.".txt");

//----------------------------------------------------------------------

?>

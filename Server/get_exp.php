<?php
//Its servicing in JSON format
header('Content-Type: application/json');

ini_set('display_startup_errors', 1);
ini_set('display_errors', 1);
error_reporting(-1);

$sqlconnector_script = 'sql_connector.php';

//Include the database handler
require_once($sqlconnector_script);

$selall = "SELECT COUNT(*) AS total FROM users WHERE id<100;";
$result = $dbc->query($selall);
$info = $result->fetch_assoc();
$total = $info['total'];
$total = (int)$total;
//$rand_id = rand(1,$total);
$rand_id = 5;

$tabres = $dbc->query("SELECT * FROM users WHERE 
id='{$rand_id}';");


$arrayres = array();
while($crow = $tabres->fetch_assoc()){
    array_push($arrayres, $crow);
}

class uhhh{
    public $exp_id;
    public $fname;
    public $lname;
    public $ratings;
    public $category;
    public $consistency;
    function __construct($rand_id, $arrayres){
        $this->exp_id = $rand_id;
        $this->fname = $arrayres['fname'];
        $this->lname = $arrayres['lname'];
        $this->ratings = (float)$arrayres['ratings'];
        $this->category = $arrayres['category'];
        if($_GET['uid'] < 100){
            $this->category = "NAN";
        }
        $this->consistency = (float)$arrayres['consistency'];
    }
}
$jsonvar = new uhhh($rand_id, $arrayres[0]);

echo json_encode($jsonvar);

$dbc->close();

?>
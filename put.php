<?php
/**
 * Web service, puts vocabularies in the server by either insert or update.
 */
require_once("{$_SERVER['DOCUMENT_ROOT']}/Vocabulary/model/VocabularyFetcher.php");

$headers = apache_request_headers();

$authorization = isset($headers['Authorization']) ? $headers['Authorization'] : $headers['authorization'];

if(isset($authorization))
{
    $isAuthorized = $authorization === md5("aaron");
    if($isAuthorized)
    {
        $jsonData = json_decode(file_get_contents('php://input'), true);

        $fetcher = new VocabularyFetcher();

        $response = $fetcher->putVocabularies($jsonData);

        if(strcmp($response, "Success") == 0)
        {
            http_response_code(200); // OK
        }
        else
        {
            http_response_code(500); // Internal Server Error
        }

        echo $response;
    }
    else
    {
        http_response_code(401); // Unauthorized
        $error = array("Error" => "Unauthorized access.");
        echo json_encode($error);
    }
}
else
{
    http_response_code(400); // Bad Request
    $error = array("Error" => "Please provide authorize key.");
    echo json_encode($error);
}
?>
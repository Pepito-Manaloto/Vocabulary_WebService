<?php
/**
 * Web service, puts vocabularies in the server by either insert or update.
 */
require_once("{$_SERVER['DOCUMENT_ROOT']}/Vocabulary/model/VocabularyFetcher.php");

$headers = apache_request_headers();

if(isset($headers['Authorization']))
{
    if($headers['Authorization'] === md5("aaron"))
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
        echo "Unauthorized access.";
    }
}
else
{
    http_response_code(400); // Bad Request
    echo "Please provide authorize key.";
}
?>
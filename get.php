<?php
/**
 * Web service, retrieves all vocabularies and returns them as JSON object.
 */
require_once("{$_SERVER['DOCUMENT_ROOT']}/Vocabulary/model/VocabularyFetcher.php");

$headers = apache_request_headers();

if(isset($headers['Authorization']))
{
    if($headers['Authorization'] === md5("aaron"))
    {
        if(!isset($_GET['last_updated']) || empty($_GET['last_updated'])) // If last_updated is not given, then set earliest date
        {
            $lastUpdated = "1950-01-01";
        }
        else
        {
            $lastUpdated = $_GET['last_updated'];
        }
        
        $fetcher = new VocabularyFetcher();

        $data = $fetcher->getVocabularies($lastUpdated);

        http_response_code(200); // OK

        header('Content-Type: application/json');
        echo $data;
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
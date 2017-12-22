<?php
/**
 * Web service, retrieves all vocabularies and returns them as JSON object.
 */
require_once("{$_SERVER['DOCUMENT_ROOT']}/Vocabulary/model/VocabularyFetcher.php");
require_once("{$_SERVER['DOCUMENT_ROOT']}/Vocabulary/model/Logger.php");

$headers = apache_request_headers();
global $logger;

$authorization = isset($headers['Authorization']) ? $headers['Authorization'] : $headers['authorization'];

if(isset($authorization))
{
    $isAuthorized = $authorization === md5("aaron");
    if($isAuthorized)
    {
        $hasLastUpdatedHeader = isset($_GET['last_updated']) && !empty($_GET['last_updated']);

        if($hasLastUpdatedHeader)
        {
            $lastUpdated = $_GET['last_updated'];
        }
        else
        {
            $lastUpdated = "1950-01-01"; // If last_updated is not given, then set earliest date
        }
    
        $logger->logMessage(basename(__FILE__), __LINE__, "GET", "Authenticated. Get by last_updated={$lastUpdated}");

        $fetcher = new VocabularyFetcher();

        $data = $fetcher->getVocabularies($lastUpdated);

        http_response_code(200); // OK

        header('Content-Type: application/json');
        echo strip_tags($data);
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
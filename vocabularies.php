<?php
/**
 * Web service, retrieves all vocabularies and returns them as JSON object.
 */
require_once("{$_SERVER['DOCUMENT_ROOT']}/Vocabulary/model/VocabularyFetcher.php");
require_once("{$_SERVER['DOCUMENT_ROOT']}/Vocabulary/model/Logger.php");

$headers = apache_request_headers();
global $logger;

$authorizationHeaderProvided = array_key_exists('Authorization', $headers) || array_key_exists('authorization', $headers);
if($authorizationHeaderProvided)
{
    $authorization = isset($headers['Authorization']) ? $headers['Authorization'] : $headers['authorization'];

    if(isset($authorization))
    {
        $isAuthorized = $authorization === md5("aaron");
        if($isAuthorized)
        {
            $lastUpdated = parseLastUpdated();

            $logger->logMessage(basename(__FILE__), __LINE__, "GET", "Authenticated. Get by last_updated={$lastUpdated}");

            $fetcher = new VocabularyFetcher();

            $data = $fetcher->getVocabularies($lastUpdated);

            http_response_code(200); // OK

            header('Content-Type: application/json');
            echo strip_tags($data);
        }
        else
        {
            returnErrorResponseDataAndCode(401, "Unauthorized access.");
        }
    }
    else
    {
        returnErrorResponseDataAndCode(400, "Please provide authorize key.");
    }
}
else
{
    returnErrorResponseDataAndCode(400, "Please provide authorize key.");
}

function parseLastUpdated()
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

    return $lastUpdated;
}

function returnErrorResponseDataAndCode($code, $errorMessage)
{
    http_response_code($code);
    $error = array("Error" => $errorMessage);
    echo json_encode($error);
}
?>
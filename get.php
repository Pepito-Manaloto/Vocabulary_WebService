<?php
/**
 * Web service, retrieves all vocabularies and returns them as JSON object.
 */
require_once("{$_SERVER['DOCUMENT_ROOT']}/Vocabulary/model/VocabularyFetcher.php");

$fetcher = new VocabularyFetcher();

$fetcher->getVocabularies();
?>
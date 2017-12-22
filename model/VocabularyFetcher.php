<?php
require_once("{$_SERVER['DOCUMENT_ROOT']}/Vocabulary/model/Database.php");
require_once("{$_SERVER['DOCUMENT_ROOT']}/Vocabulary/model/Logger.php");

/**
 * Class that fetches vocabularies from the database.
 */
class VocabularyFetcher
{
    private $mysqli;
    private $foreignLanguages = array("Hokkien", "Japanese", "Mandarin");

    /**
     * Default constructor. Initializes the mysqli variable.
     */ 
    public function __construct()
    {
        global $dbConnection;

        $this->mysqli = $dbConnection->getMySQLiConnection();
    }

    /**
     * Retrieves all vocabularies from the database + the number of recently added depending on the given date, and returns them as JSON object.
     * @param lastUpdated the client's last updated vocabulary
     * @return jsonObject
     */ 
    public function getVocabularies($lastUpdated)
    {
        global $logger;
        $query = "CALL Get_Vocabularies(?, @recently_added_count);";

        if($stmt = $this->mysqli->prepare($query))
        {
            $data = array();
            $stmt->bind_param("s", $lastUpdated);
            $stmt->execute();
            $index = 0;

            $logger->logMessage(basename(__FILE__), __LINE__, "getVocabularies", "CALL Get_Vocabularies({$lastUpdated}, @recently_added_count)");

            do
            {   
                if($result = $stmt->get_result())
                {
                    // ex: $data['Hokkien'][english_word], $data['Hokkien'][foreign_word]
                    $data[$this->foreignLanguages[$index]] = mysqli_fetch_all($result, MYSQLI_ASSOC);
                    $result->free_result();
                    $index++;
                }
            } while($stmt->more_results() && $stmt->next_result());
            
            $select = $this->mysqli->query('SELECT @recently_added_count;');
            $result = $select->fetch_assoc();
            $data['recently_added_count'] = $result['@recently_added_count'];

            global $dbConnection;
            $dbConnection->closeConnection();

            return json_encode($data);
        }

        $logger->logMessage(basename(__FILE__), __LINE__, "getVocabularies", "Error getting vocabularies. error={$this->mysqli->error}");
        $error = array("Error" => $this->mysqli->error);
        return json_encode($error);
    }
    
    /**
     * Inserts or updates the database with the given vocabularies.
     * @param jsonData the vocabularies to insert or update
     * @return response or result of the operation
     */ 
    public function putVocabularies($jsonData)
    {
        $this->mysqli->autocommit(false);
        $count = count($jsonData);
        $result = true;

        for($i = 0; $i < $count; $i++)
        {
            if($result == false)
            {
                return "Operation failed, reverting.";
            }

            $englishWord = $jsonData[$i]["english_word"];
            $foreignWord = $jsonData[$i]["foreign_word"];
            $foreignLanguage = $jsonData[$i]["foreign_language"];
            $action = $jsonData[$i]["action"];

            if(strcmp($action, "Insert") == 0)
            {
                $result = insert($englishWord, $foreignWord, $foreignLanguage);
            }
            else if(strcmp($action, "Update") == 0)
            {
                $result = update($englishWord, $foreignWord, $foreignLanguage, $jsonData[$i]["word_update"]);
            }
            else
            {
                return "Unknown action \"" . $action . "\".";
            }
        }

        $this->mysqli->commit();
        global $dbConnection;
        $dbConnection->closeConnection();

        return "Success";
    }

    /**
     * Inserts a vocabulary to the database.
     * @param englishWord
     * @param foreignWord
     * @param foreignLanguage in number format
     * @return true on success, else false
     */
    private function insert($englishWord, $foreignWord, $foreignLanguage)
    {
        global $logger;
        $query = "CALL Add_Vocabulary(?, ?, ?)";

        if($stmt = $this->mysqli->prepare($query))
        {
            $stmt->bind_param("ssi", $englishWord, $foreignWord, $foreignLanguage);
            $executeResult = $stmt->execute();

            $logger->logMessage(basename(__FILE__), __LINE__, "insert", "CALL Add_Vocabulary({$englishWord}, {$foreignWord}, {$foreignLanguage})");

            if($executeResult == false)
            {
                return false;
            }

            if($result = $stmt->get_result())
            {
                $result->free_result();
                $this->mysqli->next_result();
            }
            
            return true;
        }

        $logger->logMessage(basename(__FILE__), __LINE__, "insert", "Error adding new vocabulary. error={$this->mysqli->error}");
        return false;
    }

    /**
     * Updates a vocabulary in the database.
     * @param englishWord
     * @param foreignWord
     * @param foreignLanguage
     * @param wordUpdate 0 if englishWord will be updated, 1 if foreignWord will be updated
     * @return true on success, else false
     */
    private function update($englishWord, $foreignWord, $foreignLanguage, $wordUpdate)
    {
        global $logger;
        $query = "CALL Update_Vocabulary(?, ?, ?, ?)";
        if($stmt = $this->mysqli->prepare($query))
        {
            $stmt->bind_param("sssi", $englishWord, $foreignWord, $foreignLanguage, $wordUpdate);
            $stmt->execute();

            $logger->logMessage(basename(__FILE__), __LINE__, "update", "CALL Update_Vocabulary({$englishWord}, {$foreignWord}, {$foreignLanguage}, {$wordUpdate})");

            if($result = $stmt->get_result())
            {
                $result->free_result();
                $this->mysqli->next_result();
            }
            
            return true;
        }

        $logger->logMessage(basename(__FILE__), __LINE__, "update", "Error updating vocabulary. error={$this->mysqli->error}");
        return false;
    }
}
?>
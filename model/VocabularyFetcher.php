<?php
require_once("{$_SERVER['DOCUMENT_ROOT']}/Vocabulary/model/Database.php");
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
        global $db;

        $this->mysqli = $db->getMySQLiConnection();
    }

    /**
     * Retrieves all vocabularies from the database + the number of recently added depending on the given date, and returns them as JSON object.
     * @param lastUpdated the client's last updated vocabulary
     * @return jsonObject
     */ 
    public function getVocabularies($lastUpdated)
    {
        $query = "CALL Get_Vocabularies(?, @recently_added_count);";

        if($stmt = $this->mysqli->prepare($query))
        {
            $data = array();
            $stmt->bind_param("s", $lastUpdated);
            $stmt->execute();
            $index = 0;

            do
            {   
                if($result = $stmt->get_result())
                {
                    // ex: $data['Hokkien'][english_word], $data['Hokkien'][foreign_word]
                    $data[$this->foreignLanguages[$index]] = mysqli_fetch_all($result, MYSQLI_ASSOC);
                    mysqli_free_result($result);
                    $index++;
                }
            } while($stmt->more_results() && $stmt->next_result());
        }

        $select = $mysqli->query('SELECT @recently_added_count;');
        $result = $select->fetch_assoc();
        $data['recently_added_count'] = $result['@recently_added_count'];
        
        global $db;
        $db->closeConnection();

        return json_encode($data);
    }
}
?>
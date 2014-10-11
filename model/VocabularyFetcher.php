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
	 * Retrieves all vocabularies from the database and returns them as JSON object.
	 */	
	public function getVocabularies()
	{
		$query = "CALL Get_Vocabularies();";
		
		if($stmt = $this->mysqli->prepare($query))
		{
			$data = array();

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

		global $db;
		$db->closeConnection();

		echo json_encode($data);
	}
}
?>
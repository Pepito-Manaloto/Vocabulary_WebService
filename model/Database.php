<?php
/**
 * Database connector for MySQL.
 */
class Database
{
    private $host = "localhost";
    private $username = "root";
    private $password = "root";
    private $schema = "my_vocabulary";
    
    private $mysqli;
    private $pdo;

    /**
     * Returns mysqli database connection.
     */
    public function getMySQLiConnection()
    {
        $this->mysqli = new mysqli($this->host, $this->username, $this->password, $this->schema);
        
        if($this->mysqli->connect_errno)
        {
            http_response_code(500); // Internal Server Error
            die("{$this->mysqli->connect_error}");
        }
        else
        {
            return $this->mysqli;
        }
    }

    /**
     * Returns pdo database connection.
     */
    public function getPDOConnection()
    {
        try
        {
            $this->pdo = new PDO("mysql:host={$this->host};dbname={$this->schema}", $this->username, $this->password);
        }
        catch(PDOException $e)
        {
            die("Could not connect. {$e}");
        }
        
        return $this->pdo;
    }

    /**
     * Closes all database connections opened by this class.
     */
    public function closeConnection()
    {
        if(isset($this->mysqli))
        {
            $this->mysqli->close();
        }

        $this->pdo = null;
    }
}

// Instantiate global variable of the class
$db = new Database();

?>
<?php
/**
 * Database connector for MySQL.
 */
class Database
{
    const CHARSET = "utf8mb4";
    const HOST = "localhost";
    const USERNAME = "root";
    const PASSWORD = "root";
    const SCHEMA = "my_vocabulary";

    private $mysqli;
    private $pdo;

    /**
     * Returns mysqli database connection.
     */
    public function getMySQLiConnection()
    {
        $this->mysqli = new mysqli(self::HOST, self::USERNAME, self::PASSWORD, self::SCHEMA);
        
        if($this->mysqli->connect_errno)
        {
            http_response_code(500); // Internal Server Error
            throw new Exception("Could not connect. {$this->mysqli->connect_error}");
        }

        $this->mysqli->set_charset(self::CHARSET);
        return $this->mysqli;
    }

    /**
     * Returns pdo database connection.
     */
    public function getPDOConnection()
    {
        try
        {
            $this->pdo = new PDO("mysql:host=" . self::HOST . ";dbname=" . self::SCHEMA . ";charset=" . self::CHARSET, self::USERNAME, self::PASSWORD);
        }
        catch(PDOException $e)
        {
            http_response_code(500); // Internal Server Error
            throw new Exception("Could not connect. {$e}");
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
$dbConnection = new Database();

?>
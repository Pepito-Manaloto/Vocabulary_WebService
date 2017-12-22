<?php
define("LOG_DIR", "{$_SERVER['DOCUMENT_ROOT']}/Vocabulary/log");
define("LOG_FILE", "/vocabulary.log." .  date("Y-m-d"));
    
class Logger
{
    public function __construct()
    {
        if(!is_dir(LOG_DIR))
        {
            mkdir(LOG_DIR);
        }
    }

    /**
     * Logs a message to the log file
     * @param message the message to log
     */
    public function logMessage($filename, $codeline, $methodName, $message)
    {
        $logLine = "[" . date("Y-m-d H:i:s") . "] {$filename}:{$codeline} - {$methodName}. {$message}";
        $logFile = fopen(LOG_DIR . LOG_FILE, 'a');
        fwrite($logFile, $logLine . "\n");
        fclose($logFile);
    }
}

$logger = new Logger();
?>
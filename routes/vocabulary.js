const AUTH_KEY = "aaron";
const DB_HOST = "localhost";
const DB_USER = "root";
const DB_PASS = "root";
const DB_SCHEMA = "my_vocabulary";

//(req.params) Checks route params, ex: /user/:id 
//(req.query) Checks query string params, ex: ?id=12 Checks urlencoded body params 
exports.getFilterDate = function(request, response)
{
    var crypto = require("crypto");
    var md5sum = crypto.createHash("md5");
    try
    {
        var auth  = request.headers['authorization'];
        
        if(auth)
        {
            if(auth == md5sum.update(AUTH_KEY).digest("hex"))
            {
                var lastUpdated = request.query["last_updated"];
                if(!lastUpdated || lastUpdated.length == 0) // If last_updated is not given, then set earliest date
                {
                    lastUpdated = "1950-01-01";
                }

                response.setHeader("Content-Type", "application/json");
                response.status(200);

                getVocabularies(lastUpdated, response);
            }
            else
            {
                response.status(401);
                response.send("Unauthorized access.");
            }
        }
        else
        {
            response.status(400);
            response.send("Please provide authorize key.");
        }
    }
    catch(err)
    {
        console.log(err + "\n" + err.stack);
        response.status(500);
        response.send("Internal Server Error.");
    }
};

function getVocabularies(lastUpdated, response)
{
    var mysql = require("mysql");
    var pool =  mysql.createPool(
        {
            host:     DB_HOST,
            user:     DB_USER,
            password: DB_PASS,
            database: DB_SCHEMA,
            multipleStatements: true,
        });
    
    pool.getConnection(function(err, conn)
        {
            if(err)
            {
                console.log("Error in getting connection to database.\n%s.\n", err);
            }
            else
            {
                conn.query("CALL Get_Vocabularies(" + lastUpdated + ", @recently_added_count); SELECT @recently_added_count AS recently_added_count;",
                           function(err, results, fields)
                           {
                               console.log(err);
                               console.log(results);

                               response.send(results);
                           });
            }
            
            conn.release();
        });
}

function printObject(obj)
{
    var out = '';
    for (var key in obj)
    {
        out += key + ': ' + obj[key] + '\n';
    }
    return out;
}
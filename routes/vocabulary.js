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
        response.setHeader("Content-Type", "application/json");

        if(auth)
        {
            if(auth == md5sum.update(AUTH_KEY).digest("hex"))
            {
                var lastUpdated = request.query["last_updated"];
                if(!lastUpdated || lastUpdated.length == 0) // If last_updated is not given, then set earliest date
                {
                    lastUpdated = "1950-01-01";
                }

                getVocabularies(lastUpdated, response);
            }
            else
            {
                response.status(401);
                response.send({"http_response_text": "Unauthorized access."});
            }
        }
        else
        {
            response.status(400);
            response.send({"http_response_text": "Please provide authorize key."});
        }
    }
    catch(err)
    {
        console.log(err + "\n" + err.stack);
        response.status(500);
        response.send({"http_response_text": "Internal Server Error. " + err});
    }
};

var mysql = require("mysql");
var pool =  mysql.createPool(
    {
        host:     DB_HOST,
        user:     DB_USER,
        password: DB_PASS,
        database: DB_SCHEMA,
        multipleStatements: true,
    });

function getVocabularies(lastUpdated, response)
{
    pool.getConnection(function(err, conn)
        {
            if(err)
            {
                console.log("Error in getting connection to database.\n%s.", err);
                response.status(500);
                response.send({"http_response_text": "Internal Server Error. " + err});
            }
            else
            {
                conn.query("CALL Get_Vocabularies(" + lastUpdated + ", @recently_added_count); SELECT @recently_added_count AS recently_added_count;",
                           function(err, result, fields)
                           {
                               if(err)
                               {
                                   console.log("Error in getting vocabularies from database. %s.\n%s", err, err.stack);
                                   response.status(500);
                                   response.send({"http_response_text": "Internal Server Error. " + err});
                               }
                               else
                               {
                                   var len = result.length;
                                   var recently_added_count = result[len - 1][0].recently_added_count;
                                   var newResult = {"Hokkien": result[0],
                                                    "Japanese": result[1],
                                                    "Mandarin": result[2],
                                                    "recently_added_count": recently_added_count,
                                                    "http_response_text": "Success"};

                                   
                                   response.status(200);
                                   response.send(newResult);
                               }
                           });
            }

            if(conn)
            {   
                conn.release();
            }
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
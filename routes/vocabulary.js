const AUTH_KEY = "aaron";

//(req.params) Checks route params, ex: /user/:id 
//(req.query) Checks query string params, ex: ?id=12 Checks urlencoded body params 
function getFilterDate(request, response)
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
                var lastUpdated = request.query("last_updated");
                if(!lastUpdated || lastUpdated.length == 0) // If last_updated is not given, then set earliest date
                {
                    lastUpdated = "1950-01-01";
                }

                response.setHeader("Content-Type", "application/json");
                response.status(200);
                
                var result = {"response_text": "success"};
                response.send(result);
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
        console.log(err);
        response.status(500);
        response.send("Internal Server Error.");
    }
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
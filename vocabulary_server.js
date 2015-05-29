const PORT = 9090;
var express = require("express");
var app = express();
var vocabulary = require("./routes/vocabulary");

app.get("/Vocabulary", vocabulary.getFilterDate);
app.get("/Vocabulary/get", vocabulary.getFilterDate);
app.listen(PORT);

console.log("Server started... localhost:%s", PORT);
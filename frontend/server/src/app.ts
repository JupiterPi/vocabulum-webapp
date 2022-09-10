const fs = require("fs");
const express = require("express");
const app = express();
const port = 63108;

app.use(express.json());

/*const config = JSON.parse(fs.readFileSync("config.json"));
app.get("/backendHost", (req, res) => {
    res.status(200).send(config["backendHost"]);
});*/

// --------
const _app_folder = "dist/vocabulum-webapp-pwa";
// ---- SERVE STATIC FILES ---- //
//app.get('*.*', express.static(_app_folder));
// ->
app.use(express.static("dist/vocabulum-webapp-pwa"));
// ---- SERVE APPLICATION PATHS ---- //
["/dictionary", "/dictionary/:vocabulary", "/search", "/trainer", "/trainer/chat"].forEach(route => {
    app.get(route, function (req, res) {
        res.status(200).sendFile("/index.html", {root: _app_folder});
    });
});
// --------  (source: https://itnext.io/express-server-for-an-angular-application-part-1-getting-started-2cd27de691bd)

app.listen(port, () => {
    console.log(`Vocabulum Webapp PWA server listening on port ${port}`);
});
var express = require('express');
var router = express.Router();

router.post('/:resortID/seasons/:seasonID/days/:dayID/skiers/:skierID', function (req, res, next) {
    console.log(req.params);
    res.status(201);
    setTimeout(() => {
        res.end();
    }, req.params.resortID);
});

router.get('/:resortID/seasons/:seasonID/days/:dayID/skiers/:skierID', function (req, res, next) {
    console.log(req.params);
    res.status(200);
    res.contentType("application/json");
    res.end(req.params.skierID);
});

router.get('/:skierID/vertical', function (req, res, next) {
    console.log(req.params);
    let response = {
        "resorts": [
            {
                "seasonID": `${req.params.skierID}`,
                "totalVert": 0
            }
        ]
    }
    res.status(200);
    res.contentType("application/json");
    res.end(JSON.stringify(response));
});

module.exports = router;

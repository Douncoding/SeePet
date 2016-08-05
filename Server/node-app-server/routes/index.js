var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});


router.get('/live', function (req, res, next) {
    console.log('스트리밍 준비 상태확인');

    res.sendStatus(200);
});

module.exports = router;

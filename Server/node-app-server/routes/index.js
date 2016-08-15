var express = require('express');
var router = express.Router();
var exec = require('child_process').exec;

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});


router.get('/live', function (req, res, next) {
  exec('ps aux | grep \'[p]icam\' | wc -l', function (error, stdout, stderr) {
    var result = stdout * 1;
    if (result == 0) {
      res.sendStatus(400);
      console.log('스트리밍 서비스를 수행할 수 없는 상태: PICAM 데몬 확인 필요');
    } else {
        res.sendStatus(200);
    }
  });
});

router.get('/media/play/:target', function (req, res) {
  var cli = "omxplayer -b ../media/" + req.params.target ;

/**
 * 영상 재생중인 경우 응답을 줄수 없는 상태에 빠지기 때문에 적절한 응답을 클라이언트로 전송할 수 없는
 * 문제가 있다. 따라서 영상이 재생이 정상적인지 확인하기 위해서는 다른 API를 정의해서 사용해야 한다.
 *
 * 본 문제를 tmux 통해 세션을 분할해서 처리하고자 하였으나.. 명확한 사용법을 이해하지 못해 exec()
 * 를 이용해 세션을 생성할 수 없는 문제해서 해결할 수가 없었다. 기능과는 거의 무관함에 따라 이는 이슈
 * 사항으로만 기록한다.
 *
 * 위의 문제 사항으로 인해 클라이언트의 타임아웃 시간을 3초로 매우짧게 설정했다.
 */
  exec(cli, function (err, stdout, stderr) {
    if (err) {
      console.log(stderr);
      res.sendStatus(400);
    } else {
      res.sendStatus(200);
    }
  });
});

router.get('/media/stop', function (req, res) {
  var cli = "ps -ef|grep omxplayer | grep -v grep|awk '{print \"kill -9 \"$2}' |sh"

  exec(cli, function (err, stdout, stderr) {
    if (err) {
      res.sendStatus(400);
    } else {
      res.sendStatus(200);
    }
  });
});

router.get('/media', function (req, res, next) {
  exec('ls ../media', function (err, stdout, stderr) {
    var mediaList = new Array();

    var items = stdout.split('\n');
    for (var i=0; i < items.length-1; i++) {
        mediaList.push(items[i]);
        console.log(items[i]);
    }

    res.status(200).json(mediaList);
  });
});


router.get('/motor/:id', function (req, res, next) {
  console.log("[미사용] 제어모터 번호:" + req.params.id);

  exec('../gpio-app/servo_one', function (err, stdout, stderr) {
    if (err) {
      res.sendStatus(400);
    } else {
      res.sendStatus(200);
    }
  });
});


module.exports = router;

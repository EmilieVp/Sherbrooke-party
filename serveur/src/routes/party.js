const express = require('express');
const router = express.Router();
const Party = require('../modeles/party');

const gen = require('../generateur');

var bodyParser = require('body-parser');

// parse application/x-www-form-urlencoded
router.use(bodyParser.urlencoded({ extended: false }));

// parse application/json
router.use(bodyParser.json());

router.get('/', function (req, res, next) {
  res.send(gen.liste_parties);
});

router.get('/:id', function (req, res, next) {
  res.send(gen.liste_parties[req.params.id]);
});

router.post('/', function (request, res) {
	gen.liste_parties.push(  new Party(request.body.idHost, request.body.type, request.body.title, request.body.description, request.body.price,
      request.body.max_guest, request.body.schedule,request.body.latitude, request.body.longitude, request.body.idUsersGuest))
    var d = {'value' : 'success add'};
    res.send(d);
});

module.exports = router;
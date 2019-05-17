const express = require('express');
const router = express.Router();
const User = require('../modeles/user');

const gen = require('../generateur');

var bodyParser = require('body-parser');

// parse application/x-www-form-urlencoded
router.use(bodyParser.urlencoded({ extended: false }));

// parse application/json
router.use(bodyParser.json());

router.get('/', function (req, res, next) {
  res.send(gen.liste_users);
});

router.get('/:id', function (req, res, next) {
  res.send(gen.liste_users[req.params.id]);
});

// POST /login gets urlencoded bodies
router.post('/', function (request, res) {
	console.log("done");
	gen.liste_users.push(new User(gen.liste_users.length + 1, request.body.adr_mail, request.body.mdp));
	var d = {'val' : "success"};
	res.send(d);
})


module.exports = router;

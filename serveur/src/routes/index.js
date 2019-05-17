const express = require('express');
const router = express.Router();

const Party = require('../modeles/party.js');

router.get('/', function (req, res, next) {
	const p = new Party('my_id','anniversaire','Anniversaire de Imilie', 10, true, 15, '18h30', 0, 0);
	//res.send('Bienvenu dans le serveur d evenements.');
	res.send(p);
});

module.exports = router;

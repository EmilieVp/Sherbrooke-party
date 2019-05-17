const Party = require('./modeles/party');
const User = require('./modeles/user');

const listeParties = [];
const listeUsers = [];

listeParties.push(new Party('emilie94190@hotmail.fr','anniversaire','Anniversaire de Imilie','description de ouf', 10, 15, '18h30', 45.3879447, -71.9176287));
listeParties.push(new Party('coco@gmail.com','fete','Anniversaire de Corentin','description pas ouf', 10, 15, '18h30', -33.87365, 151.20689));

listeUsers.push(new User(1,'emilie94190@hotmail.fr', 'coucou'));


module.exports = {};
module.exports.liste_parties = listeParties;
module.exports.liste_users = listeUsers;

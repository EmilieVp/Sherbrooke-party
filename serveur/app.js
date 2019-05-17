const express = require('express');
const cookieParser = require('cookie-parser');
const logger = require('morgan');

const indexRouter = require('./src/routes/index');
const partiesRouter = require('./src/routes/party');
const usersRouter = require('./src/routes/user');

const app = express();

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());

app.use('/', indexRouter);
app.use('/parties', partiesRouter);
app.use('/users', usersRouter);

module.exports = app;

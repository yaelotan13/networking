const express = require('express');
const loaders = require('./loaders/loaders');
const userService = require('./services/user- service');
const tokenService = require('./services/token-service');

let app;

function startServer() {
  app = express();
  loaders.init(app);

  app.listen(process.env.PORT, () => console.log(`Server is listening on port ${process.env.PORT}!`));
}

startServer();

app.post('/users', async (req, res) => {
  const { userID, clientError } = await userService.signUp(req.body);
  const { token } = await tokenService.setNewToken(userID);

  if (clientError) {
    res.status(400).send(clientError);
  } else {
    res.cookie('token', token, { maxAge: 90000000, httpOnly: true });
    res.send(`userID ${userID}: new user ${req.body.firstName} ${req.body.lastName} has been created`);
  }
});

app.use('/', async (req, res, next) => {
  const { token, clientError, userID } = await userService.validateUser(req.body);
  if (clientError) {
    res.status(400).send(clientError);
  } else {
    res.token = token;
    res.userID = userID;
    next();
  }
});

app.post('/login', async (req, res) => {
  const { token } = await tokenService.setNewToken(res.userID);
  res.cookie('token', token, { maxAge: 90000000, httpOnly: true });
  res.send(`${userName} is logged in!`);
});

app.post('/logout', async (req, res) => {
  await tokenService.deleteToken(res.token);
  res.clearCookie('token');
  res.status(202).send('you are logged out');
});

//http://127.0.0.1:3000/users?criteria=age&filter=gt&value=18 -> how to send the query
app.get('/users', async (req, res) => {
  const criteria = req.query.criteria;
  const filter = req.query.filter;
  const value = req.query.value;

  if (!criteria || !filter || !value) {
    res.status(400).send('please specify the criteria, filter and value');
  }

  if (criteria == 'age' && filter == 'gt' && value == 18) {
    const results = await userService.getUsersOver18();
    res.send(results);
  }
});

app.get('/users/:id', async (req, res) => {
  const { user, clientError }  = await userService.getUserByID(req.params.id);
  if (clientError) {
    res.status(401).send(clientError);
  }
  res.send(user);
});

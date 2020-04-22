function init() {
  const { Client } = require('pg');
  const client = new Client({
    user: 'yael',
    database: 'authentication_server',
    password: 'ct,h kvmkhj',
  });

  client.connect().then(console.log('DB is connected!'));
  return client;
};

module.exports = init;

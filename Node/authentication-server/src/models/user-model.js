const db = require('./init-DB')();

async function insertUser(user) {
  try {
    await db.query({
      text: 'INSERT INTO users (firstname, lastname, yearofbirth, email, password) VALUES ($1, $2, $3, $4, $5)',
      values: Object.values(user),
    });

    const result = await db.query('SELECT MAX(userId) FROM users');
    return result.rows[0].max;
  } catch (e) {
    console.log('DB error occurred'); // TODO have proper error handling
  }
}

async function getUserByEmail(email) {
  try {
    const result = await db.query(`SELECT userid, firstname, lastname, password FROM users WHERE email='${email}'`);
    return result.rows[0];
  } catch (e) {
    console.log('DB error occurred');
  }
}

async function getUsersOver18() {
  try {
    const results = await db.query(`SELECT (userID, firstName, LastName, yearOfBirth, email) 
    FROM users WHERE (date_part('year', CURRENT_DATE) - yearOfBirth  > 18);`);
    return results.rows;
  } catch (e) {
    console.log('DB error occurred');
  }
}

async function getUserByID(userID) {
  try {
    const result = await db.query(`SELECT  firstName, lastName, yearOfBirth, email
    FROM users WHERE userid=${userID}`);
    return result.rows[0];
  } catch (e) {
    console.log('DB error occurred');
  }
}

module.exports = {
  insertUser,
  getUserByEmail,
  getUsersOver18,
  getUserByID,
};

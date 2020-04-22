const db = require('./init-DB')();

async function insertToken(userID, token) {
  try {
    await db.query(`INSERT INTO sessions (token, userID) VALUES ('${token}', ${userID});`);
  } catch (e) {
    console.log('DB error occurred');
  }
}

async function tokenIsValid(token) {
  try {
    const result = await db.query(`SELECT EXISTS(SELECT FROM sessions WHERE token='${token}')`);
    return result.rows[0].exists;
  } catch (e) {
    console.log('DB error occurred');
  }
}

async function deleteToken(token) {
  try {
    await db.query(`DELETE FROM sessions WHERE token='${token}'`);
  } catch (e) {
    console.log('DB error occurred');
  }
}

module.exports = {
  insertToken,
  tokenIsValid,
  deleteToken,
};

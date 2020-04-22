function init(expressApp) {
  const express = require('express');
  const cookieParser = require('cookie-parser');

  if (!expressApp) {
    throw new Error('expressApp must be provided');
  }

  expressApp.use(express.json());
  expressApp.use(cookieParser());
}

module.exports = {
  init,
};

DROP TABLE IF EXISTS user_account;

CREATE TABLE user_account (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  account_number VARCHAR(5) NOT NULL,
  -- usually the pin (password) should hashed, but for simplicity it's plain text
  pin VARCHAR(4) NOT NULL,
  balance DOUBLE(15) NOT NULL
);
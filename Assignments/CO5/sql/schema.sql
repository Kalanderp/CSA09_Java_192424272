CREATE DATABASE IF NOT EXISTS java_co5 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE java_co5;

CREATE TABLE IF NOT EXISTS bus_pass (
  pass_id VARCHAR(20) PRIMARY KEY,
  reg_no VARCHAR(20) NOT NULL,
  name VARCHAR(100) NOT NULL,
  route_no INT NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_bus_pass_route(route_no),
  INDEX idx_bus_pass_reg(reg_no)
);

CREATE TABLE IF NOT EXISTS users (
  id VARCHAR(40) PRIMARY KEY,
  pwd VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS hostel_wallet (
  wallet_id VARCHAR(20) PRIMARY KEY,
  balance DECIMAL(12,2) NOT NULL CHECK (balance >= 0)
);
CREATE TABLE IF NOT EXISTS canteen_wallet (
  wallet_id VARCHAR(20) PRIMARY KEY,
  balance DECIMAL(12,2) NOT NULL CHECK (balance >= 0)
);

INSERT IGNORE INTO users(id,pwd) VALUES ('student1','java123');
INSERT IGNORE INTO hostel_wallet(wallet_id,balance) VALUES ('HOSTEL',10000.00);
INSERT IGNORE INTO canteen_wallet(wallet_id,balance) VALUES ('CANTEEN',2500.00);

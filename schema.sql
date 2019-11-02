use db;

DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS credit_card;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS account;

CREATE TABLE account (
    account_id INT(10) UNSIGNED  NOT NULL AUTO_INCREMENT,
    customer_id INT(10) UNSIGNED  default 0,
    accountNumber varchar(255) default null,
    createdAt INT UNSIGNED,
    lastModified INT UNSIGNED,
    PRIMARY KEY(account_id)
);

CREATE TABLE address (
    address_id INT(10) UNSIGNED  NOT NULL AUTO_INCREMENT,
    account_id INT(10) UNSIGNED default 0,
    street1 varchar(255) default NULL,
    street2 varchar(255) default NULL,
    state varchar(255) default NULL,
    city varchar(255) default NULL,
    country varchar(255) default NULL,
    zipCode varchar(255) default NULL,
    createdAt INT UNSIGNED,
    lastModified INT UNSIGNED,
    PRIMARY KEY(address_id),
    CONSTRAINT address_account_id FOREIGN KEY (account_id) REFERENCES account (account_id)
);

CREATE TABLE credit_card (
    credit_id INT(10) UNSIGNED  NOT NULL AUTO_INCREMENT,
    account_id INT(10) UNSIGNED,
    number varchar(255),
    createdAt INT UNSIGNED,
    lastModified INT UNSIGNED,
    PRIMARY KEY(credit_id),
    CONSTRAINT credit_account_id FOREIGN KEY (account_id) REFERENCES account (account_id)
);

CREATE TABLE customer (
  customer_id INT(10) UNSIGNED  NOT NULL AUTO_INCREMENT,
  account_id INT(10) UNSIGNED  NOT NULL,
  firstName varchar(255) default NULL,
  lastName varchar(255) default NULL,
  email varchar(255) default NULL,
  createdAt INT UNSIGNED,
  lastModified INT UNSIGNED,
  PRIMARY KEY(customer_id),
  CONSTRAINT customer_account_id FOREIGN KEY (account_id) REFERENCES account (account_id)
);
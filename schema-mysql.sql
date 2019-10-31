
-- COSTUMER

DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS creditcard;
DROP TABLE IF EXISTS customer;

CREATE TABLE creditcard (
    id SERIAL PRIMARY KEY,
    number varchar(255) not null,
    createdAt UNSIGNED BIGINT,
    lastModified UNSIGNED BIGINT
);

CREATE TABLE address (
    id SERIAL PRIMARY KEY,
    street1 varchar(255) default NULL,
    street2 varchar(255) default NULL,
    state varchar(255) default NULL,
    city varchar(255) default NULL,
    country varchar(255) default NULL,
    zipCode varchar(255) default NULL,
    createdAt UNSIGNED BIGINT,
    lastModified UNSIGNED BIGINT
);

CREATE TABLE account (
    id SERIAL PRIMARY KEY,
    accountNumber varchar(255) not null,
    createdAt UNSIGNED BIGINT,
    lastModified UNSIGNED BIGINT
);

-- CUSTOMERS

CREATE TABLE customer (
  id SERIAL PRIMARY KEY,
  firstName varchar(255) default NULL,
  lastName varchar(255) default NULL,
  email varchar(255) defaul NULL,
  createdAt UNSIGNED BIGINT,
  lastModified UNSIGNED BIGINT
);
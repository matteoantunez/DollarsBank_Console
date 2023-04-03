CREATE DATABASE	dbank;

USE dbank;

CREATE TABLE Customers (
	customer_id int UNIQUE NOT NULL AUTO_INCREMENT,
	first_name varchar(50),
    last_name varchar(50),
    username varchar(50),
    psword varchar(50),
    state varchar(50),
    area_code int,
    local_code int,
    personal_digits int,
    PRIMARY KEY (customer_id)
);


CREATE TABLE Checking_Account (
	customer_id int NOT NULL,
    account_id int UNIQUE NOT NULL,
    balance float,
    PRIMARY KEY (account_id),
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id)
);

CREATE TABLE Transactions_Checking(

);

CREATE TABLE Transactions_Savings(

);



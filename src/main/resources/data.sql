-- Test data for application

DROP TABLE IF EXISTS User;

-- Create user table
CREATE TABLE User (userId LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
                   userName VARCHAR(35) NOT NULL,
                   userPhone VARCHAR(15) NOT NULL,
                   userEmail VARCHAR(25));

CREATE UNIQUE INDEX idx_user on User(userName, userEmail);

-- Insert some data into the user table
INSERT INTO User (userName, userPhone, userEmail) VALUES ('John Stan', '1234-564-35', 'john_stan@gmail.com');
INSERT INTO User (userName, userPhone, userEmail) VALUES ('Den Cook', '4564-345-89', 'den_cook@gmail.com');


DROP TABLE IF EXISTS Account;

-- Create account table
CREATE TABLE Account (accountId LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
                      accountNumber VARCHAR(30),
                      accountBalance DECIMAL(19,3));

CREATE UNIQUE INDEX idx_acc on Account(accountNumber);

-- Insert some data into the account table
INSERT INTO Account (accountNumber, accountBalance) VALUES ('34557685', 150.005);
INSERT INTO Account (accountNumber, accountBalance) VALUES ('56756787', 560.010);
INSERT INTO Account (accountNumber, accountBalance) VALUES ('67865785', 800.567);
INSERT INTO Account (accountNumber, accountBalance) VALUES ('90007856', 900.023);
DROP DATABASE thesisProject;

CREATE DATABASE thesisProject;
USE thesisProject;

CREATE TABLE users (
  email      VARCHAR(100) PRIMARY KEY NOT NULL,
  password   VARCHAR(100)             NOT NULL,
  name       VARCHAR(50)              NOT NULL,
  surname    VARCHAR(50)              NOT NULL
);

create table authorities (

  email     VARCHAR(100) NOT NULL,
  authority VARCHAR(50)  NOT NULL,
  CONSTRAINT fk_authorities_users FOREIGN KEY (email) REFERENCES users (email)
    ON DELETE CASCADE
);

CREATE TABLE resetToken (
  email      VARCHAR(100) PRIMARY KEY,
  resetToken CHAR(36),
  date       DATETIME,
  CONSTRAINT fk_resetToken_users FOREIGN KEY (email) REFERENCES users (email)
    ON DELETE CASCADE
);

-- Sample user's password '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq' is a hashed version of password 'password'
INSERT INTO users (email, password, name, surname) VALUES ('sdoe@gmail.com', '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq', 'Steve', 'Doe');
INSERT INTO users (email, password, name, surname) VALUES ('test@gmail.com', '$2a$10$9d5AC2CrUGa', 'Steve', 'Doe');

INSERT INTO authorities (email, authority) VALUES ('sdoe@gmail.com', 'ROLE_USER');
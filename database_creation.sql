DROP DATABASE thesisProject;

CREATE DATABASE thesisProject;

USE thesisProject;

CREATE TABLE users (
  email      VARCHAR(100) PRIMARY KEY NOT NULL,
  password   VARCHAR(100)             NOT NULL,
  username       VARCHAR(50)              NOT NULL
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

CREATE TABLE employees (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR (255) NOT NULL,
  timeFactor DECIMAL(3,2) NOT NULL,
  localization varchar(255) NOT NULL
);

CREATE TABLE wages (
	id INT PRIMARY KEY AUTO_INCREMENT,
	hourlyWage DECIMAL(7,2) NOT NULL,
	startDate DATETIME NOT NULL,
	endDate DATETIME
);

create table shops (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR (255) NOT NULL,
	localization VARCHAR (255) NOT NULL,
	openingTime TIME NOT NULL,
	closingTime TIME NOT NULL,
	requiredStaff int not null
);

create table shop_work_days (
	id INT PRIMARY KEY AUTO_INCREMENT,
	date DATE NOT NULL,
	shop_id int not null,
	constraint fk_shopWorkDays_shop FOREIGN key (shop_id) references shops(id)
);

-- Sample user's password '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq' is a hashed version of password 'password'
INSERT INTO users (email, password, username) VALUES ('sdoe@gmail.com', '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq', 'Steve');
INSERT INTO users (email, password, username) VALUES ('test@gmail.com', '$2a$10$9d5AC2CrUGa', 'Steve');

INSERT INTO authorities (email, authority) VALUES ('sdoe@gmail.com', 'ROLE_USER');

INSERT INTO employees (name, timeFactor, localization) VALUES ('Pracownik Jeden', 1, 'KRK');
INSERT INTO employees (name, timeFactor, localization) VALUES ('Pracownik Dwa', 0.75, 'KRK');
INSERT INTO employees (name, timeFactor, localization) VALUES ('Pracownik Trzy', 1, 'DFW');
INSERT INTO employees (name, timeFactor, localization) VALUES ('Pracownik Cztery', 1, 'KRK');
INSERT INTO employees (name, timeFactor, localization) VALUES ('Pracownik Piec', 1, 'KRK');

# INSERT INTO wages (employee_id, hourlyWage, startDate) values (1, 11.50, '2018-06-10 11:00:00');
# INSERT INTO wages (employee_id, hourlyWage, startDate) values (2, 11.50, '2018-06-10 11:00:00');
# INSERT INTO wages (employee_id, hourlyWage, startDate, endDate) values (3, 11.50, '2018-06-10 11:00:00', '2018-06-11 11:00:00');
# INSERT INTO wages (employee_id, hourlyWage, startDate) values (3, 20.50, '2018-06-12 11:00:00');
# INSERT INTO wages (employee_id, hourlyWage, startDate) values (4, 11.50, '2018-06-12 11:00:00');
# INSERT INTO wages (employee_id, hourlyWage, startDate) values (5, 11.50, '2018-06-12 11:00:00');

insert into shops (name, localization, openingTime, closingTime, requiredStaff) values ('Shop one', 'KRK', '09:00', '17:00', 2);
insert into shops (name, localization, openingTime, closingTime, requiredStaff) values ('Shop two', 'KRK', '10:00', '12:00', 1);
insert into shops (name, localization, openingTime, closingTime, requiredStaff) values ('Shop three', 'DFW', '10:00', '12:00', 1);
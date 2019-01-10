DROP DATABASE thesisProject;

CREATE DATABASE thesisProject;

USE thesisProject;

CREATE TABLE users
(
  email    VARCHAR(100) PRIMARY KEY NOT NULL,
  password VARCHAR(100)             NOT NULL,
  username VARCHAR(50)              NOT NULL
);

create table authorities
(
  email     VARCHAR(100) NOT NULL,
  authority VARCHAR(50)  NOT NULL,
  CONSTRAINT fk_authorities_users FOREIGN KEY (email) REFERENCES users (email) ON DELETE CASCADE
);

CREATE TABLE resetToken
(
  email      VARCHAR(100) PRIMARY KEY,
  resetToken VARCHAR(255),
  date       DATETIME,
  CONSTRAINT fk_resetToken_users FOREIGN KEY (email) REFERENCES users (email) ON DELETE CASCADE
);

CREATE TABLE employees
(
  id         INT PRIMARY KEY AUTO_INCREMENT,
  name       VARCHAR(255) NOT NULL,
  timeFactor float        NOT NULL,
  location   varchar(255) NOT NULL
);

CREATE TABLE wages
(
  id         INT PRIMARY KEY AUTO_INCREMENT,
  hourlyWage DECIMAL(7, 2) NOT NULL,
  startDate  DATETIME      NOT NULL,
  endDate    DATETIME
);

CREATE TABLE employees_wages
(
  employee_id INT,
  wages_id    int unique,
  CONSTRAINT pk_employees_wages PRIMARY KEY (employee_id, wages_id),
  CONSTRAINT fk_employees_wages_employees FOREIGN KEY (employee_id) REFERENCES employees (id) ON DELETE CASCADE,
  CONSTRAINT fk_employees_wages_wages FOREIGN KEY (wages_id) REFERENCES wages (id) ON DELETE CASCADE
);

CREATE TABLE shops
(
  id            INT PRIMARY KEY AUTO_INCREMENT,
  name          VARCHAR(255) NOT NULL,
  location      VARCHAR(255) NOT NULL,
  openingTime   TIME         NOT NULL,
  closingTime   TIME         NOT NULL,
  requiredStaff int          not null
);

CREATE TABLE schedules
(
  id      INT PRIMARY KEY AUTO_INCREMENT,
  date    DATE NOT NULL,
  shop_id INT,
  CONSTRAINT fk_schedules_shop FOREIGN KEY (shop_id) REFERENCES shops (id) ON DELETE CASCADE
);

CREATE TABLE schedules_employees
(
  schedule_id  INT,
  employees_id INT NOT NULL,
  CONSTRAINT pk_schedules_employees PRIMARY KEY (schedule_id, employees_id),
  CONSTRAINT fk_schedules_employees_schedules FOREIGN KEY (schedule_id) REFERENCES schedules (id) ON DELETE CASCADE,
  CONSTRAINT fk_schedules_employees_employees FOREIGN KEY (employees_id) REFERENCES employees (id) ON DELETE CASCADE
);

CREATE TABLE work_days
(
  id          INT PRIMARY KEY AUTO_INCREMENT,
  hoursWorked BIGINT NOT NULL,
  shop_id     INT,
  date        DATE   NOT NULL,
  CONSTRAINT work_days_shops FOREIGN KEY (shop_id) REFERENCES shops (id) ON DELETE CASCADE
);

CREATE TABLE employees_work_days
(
  employee_id INT,
  workDays_id INT UNIQUE,
  CONSTRAINT pk_employees_work_days PRIMARY KEY (employee_id, workDays_id),
  CONSTRAINT fk_employees_work_days_employees FOREIGN KEY (employee_id) REFERENCES employees (id) ON DELETE CASCADE,
  CONSTRAINT fk_employees_work_days_workDays FOREIGN KEY (workDays_id) REFERENCES work_days (id) ON DELETE CASCADE
);

CREATE TABLE wares
(
  id          INT PRIMARY KEY AUTO_INCREMENT,
  name        VARCHAR(255)  NOT NULL,
  amount      DECIMAL(7, 2) NOT NULL,
  price       DECIMAL(7, 2) NOT NULL,
  description VARCHAR(255)  NOT NULL
);

CREATE TABLE sold_wares
(
  id          INT PRIMARY KEY AUTO_INCREMENT,
  employee_id INT           NOT NULL,
  amount      DECIMAL(7, 2) NOT NULL,
  price       DECIMAL(7, 2) NOT NULL,
  dateSold    DATETIME      NOT NULL,
  CONSTRAINT fk_sold_wares_employee FOREIGN KEY (employee_id) REFERENCES employees (id) ON DELETE CASCADE
);

CREATE TABLE wares_sold_wares
(
  ware_id      INT        NOT NULL AUTO_INCREMENT,
  soldParts_id INT UNIQUE NOT NULL,
  CONSTRAINT pk_wares_sold_wares PRIMARY KEY (ware_id, soldParts_id),
  CONSTRAINT fk_wares_sold_wares_sold_wares FOREIGN KEY (soldParts_id) REFERENCES sold_wares (id) ON DELETE CASCADE,
  CONSTRAINT fk_wares_sold_wares_wares FOREIGN KEY (ware_id) REFERENCES wares (id) ON DELETE CASCADE
);

-- Sample user's password '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq' is a hashed version of password 'password'
INSERT INTO users (email, password, username)
VALUES ('sdoe@gmail.com', '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq', 'Piotr');
INSERT INTO users (email, password, username)
VALUES ('test@gmail.com', '$2a$10$9d5AC2CrUGa', 'Piotr 2');

INSERT INTO authorities (email, authority)
VALUES ('sdoe@gmail.com', 'ROLE_USER');

INSERT INTO employees (name, timeFactor, location)
VALUES ('Pracownik Jeden', 1, 'KRK');
INSERT INTO employees (name, timeFactor, location)
VALUES ('Pracownik Dwa', 0.75, 'KRK');
INSERT INTO employees (name, timeFactor, location)
VALUES ('Pracownik Trzy', 1, 'DFW');
INSERT INTO employees (name, timeFactor, location)
VALUES ('Pracownik Cztery', 1, 'KRK');
INSERT INTO employees (name, timeFactor, location)
VALUES ('Pracownik Piec', 1, 'KRK');

INSERT INTO wages (hourlyWage, startDate)
VALUES (15, '2018-12-12');
INSERT INTO wages (hourlyWage, startDate)
VALUES (15, '2018-12-12');
INSERT INTO wages (hourlyWage, startDate)
VALUES (15, '2018-12-12');
INSERT INTO wages (hourlyWage, startDate)
VALUES (15, '2018-12-12');
INSERT INTO wages (hourlyWage, startDate)
VALUES (15, '2018-12-12');

INSERT INTO employees_wages (employee_id, wages_id)
VALUES (1, 1);
INSERT INTO employees_wages (employee_id, wages_id)
VALUES (2, 2);
INSERT INTO employees_wages (employee_id, wages_id)
VALUES (3, 3);
INSERT INTO employees_wages (employee_id, wages_id)
VALUES (4, 4);
INSERT INTO employees_wages (employee_id, wages_id)
VALUES (5, 5);

INSERT INTO shops (name, location, openingTime, closingTime, requiredStaff)
VALUES ('Shop one', 'KRK', '09:00', '17:00', 2);
INSERT INTO shops (name, location, openingTime, closingTime, requiredStaff)
VALUES ('Shop two', 'KRK', '10:00', '12:00', 1);
INSERT INTO shops (name, location, openingTime, closingTime, requiredStaff)
VALUES ('Shop three', 'DFW', '10:00', '12:00', 1);

INSERT INTO wares (name, amount, price, description)
VALUES ('ring', '1', 150, 'An old ring');
INSERT INTO wares (name, amount, price, description)
VALUES ('bracelet', '2', 200, 'Original bracelet');
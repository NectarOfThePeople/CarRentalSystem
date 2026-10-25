-- car_rental.sql
-- Run this file once in MySQL to create the database, the tables and some sample data.
--   Command line :  mysql -u root -p < car_rental.sql
--   Workbench    :  File > Open SQL Script, then click the lightning button.

CREATE DATABASE IF NOT EXISTS car_rental;
USE car_rental;

-- Admin login. The admin must type this password. Customers do not need a password.
CREATE TABLE IF NOT EXISTS admins (
    admin_id   VARCHAR(20) PRIMARY KEY,
    password   VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS cars (
    car_id        VARCHAR(10) PRIMARY KEY,
    name          VARCHAR(50) NOT NULL,
    type          VARCHAR(30) NOT NULL,
    rent_per_day  INT NOT NULL,
    available     BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS customers (
    customer_id  VARCHAR(10) PRIMARY KEY,
    name         VARCHAR(50) NOT NULL,
    phone        VARCHAR(15) NOT NULL,
    license_no   VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS bookings (
    booking_id   VARCHAR(10) PRIMARY KEY,
    car_id       VARCHAR(10) NOT NULL,
    customer_id  VARCHAR(10) NOT NULL,
    days         INT NOT NULL,
    status       VARCHAR(10) NOT NULL
);

-- Sample data (INSERT IGNORE means running this file again will not give errors)
INSERT IGNORE INTO admins VALUES ('admin', 'admin123');

INSERT IGNORE INTO cars VALUES ('C101', 'Swift',  'Hatchback', 1500, TRUE);
INSERT IGNORE INTO cars VALUES ('C102', 'Innova', 'SUV',       3000, FALSE);
INSERT IGNORE INTO cars VALUES ('C103', 'Baleno', 'Hatchback', 1800, TRUE);
INSERT IGNORE INTO cars VALUES ('C104', 'Verna',  'Sedan',     2500, TRUE);

INSERT IGNORE INTO customers VALUES ('CU101', 'Arun',  '9800000001', 'DL1001');
INSERT IGNORE INTO customers VALUES ('CU102', 'Rahul', '9800000002', 'DL1002');

INSERT IGNORE INTO bookings VALUES ('B1', 'C102', 'CU101', 3, 'Active');

-- To change the admin password later:
--   UPDATE admins SET password = 'new_password' WHERE admin_id = 'admin';

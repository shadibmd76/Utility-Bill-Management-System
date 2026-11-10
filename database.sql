-- MySQL Workbench / phpMyAdmin e ei puro file ta run koro
CREATE DATABASE IF NOT EXISTS utility_db;
USE utility_db;

DROP VIEW IF EXISTS bill_view;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS bills;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL
);

CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE bills (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bill_name VARCHAR(100) NOT NULL,
    category_id INT NOT NULL,
    bill_month VARCHAR(7) NOT NULL,          -- format: 2026-11
    amount DOUBLE NOT NULL,
    bill_date DATE NOT NULL,
    due_date DATE NOT NULL,
    description VARCHAR(200),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bill_id INT NOT NULL,
    amount DOUBLE NOT NULL,
    pay_date DATE NOT NULL,
    method VARCHAR(20) NOT NULL,
    FOREIGN KEY (bill_id) REFERENCES bills(id) ON DELETE CASCADE
);

-- Paid / Due / Status auto calculate hoy ei view theke
CREATE VIEW bill_view AS
SELECT b.id, b.bill_name, c.name AS category, b.bill_month, b.amount,
       IFNULL(SUM(p.amount), 0) AS paid,
       b.amount - IFNULL(SUM(p.amount), 0) AS due,
       b.due_date,
       CASE
         WHEN IFNULL(SUM(p.amount), 0) >= b.amount THEN 'Paid'
         WHEN b.due_date < CURDATE() THEN 'Overdue'
         WHEN IFNULL(SUM(p.amount), 0) > 0 THEN 'Partial'
         ELSE 'Unpaid'
       END AS status
FROM bills b
JOIN categories c ON b.category_id = c.id
LEFT JOIN payments p ON p.bill_id = b.id
GROUP BY b.id, c.name;

INSERT INTO users (username, password) VALUES ('admin', '1234');
INSERT INTO categories (name) VALUES ('Electricity'), ('Gas'), ('Water'), ('Internet'), ('Other Bills');

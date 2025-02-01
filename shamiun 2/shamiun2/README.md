# CSEDU Supermarket

## Project Overview
**CSEDU Supermarket** is a web-based supermarket management system designed to enhance the shopping experience for customers while streamlining operations for administrators and employees. The platform includes three main dashboards:

- **Admin Dashboard**: Manages inventory, orders, user accounts, and customer reviews.
- **Employee Dashboard**: Handles orders, payments, and customer queries.
- **Customer Dashboard**: Enables product browsing, review submissions, and communication with employees.

The goal of the system is to provide an efficient, seamless shopping experience while optimizing store management.


## **Tools Used**
- **Programming Language**: Java
- **Framework**: JavaFX
- **Database**: MySQL
- **IDE**: IntelliJ IDEA
- **Testing**: Manual testing for system functionality

## Steps to Install and Use This Program

1. Unzip the folder.
2. Open the project using IntelliJ IDEA.
3. Navigate to **Project Structure** and add the **MySQL Connector JAR** file to the dependencies.
4. Set up **MySQL Workbench**.
5. Run the following SQL script to create and configure the database:

```sql
-- Create the database
CREATE DATABASE admin_db;

-- Use the created database
USE admin_db;

-- Create the admin table
CREATE TABLE admin (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Insert into the admin table
INSERT INTO admin (username, password)
VALUES ('csedu', '123456');

-- Create the employee table
CREATE TABLE employee (
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    fullname VARCHAR(100) NOT NULL,
    gender VARCHAR(100) NOT NULL,
    date DATE NOT NULL,
    PRIMARY KEY (username)
);

-- Insert into the employee table
INSERT INTO employee (username, password, fullname, gender, date)
VALUES
('aurnob', 'aurnob46', 'Ahil Islam Aurnob', 'Male', '2025-01-01'),
('anika', 'anika02', 'Anika Sanzida', 'Female', '2025-01-01'),
('tabassum', 'tabassum56', 'Tabassum Kabir', 'Female', '2025-01-01'),
('shamiun', 'shamiun42', 'S M Shamiun Ferdous', 'Male', '2025-01-01');

-- Create the customer table
CREATE TABLE customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    gender VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    confirm_password VARCHAR(255) NOT NULL
);

-- Create the product table
CREATE TABLE product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    brand_name VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    status VARCHAR(100) NOT NULL,
    image VARCHAR(100) NOT NULL
);

-- Create the review table
CREATE TABLE review (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    review_text TEXT NOT NULL
);
```
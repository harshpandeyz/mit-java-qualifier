CREATE TABLE department (
    department_id INT PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL
);

CREATE TABLE employee (
    emp_id INT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    dob DATE NOT NULL,
    gender VARCHAR(20) NOT NULL,
    department INT NOT NULL,
    CONSTRAINT fk_employee_department
        FOREIGN KEY (department) REFERENCES department(department_id)
);

CREATE TABLE payments (
    payment_id INT PRIMARY KEY,
    emp_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_time TIMESTAMP NOT NULL,
    CONSTRAINT fk_payments_employee
        FOREIGN KEY (emp_id) REFERENCES employee(emp_id)
);

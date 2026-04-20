# MIT Java Qualifier - Salary Query Project

This repository is a complete, git-ready solution for the qualifier problem using:

- MySQL scripts for schema, data, and final query
- A plain Java 17 console program that reproduces the answer

## Problem

Find the highest salary credited to an employee, excluding transactions made on the `1st` day of any month, and return:

- `SALARY`
- `NAME`
- `AGE`
- `DEPARTMENT_NAME`

## Final Answer

| SALARY | NAME | AGE | DEPARTMENT_NAME |
| --- | --- | --- | --- |
| 74998.00 | Emily Brown | 32 | Sales |

## Assumption

`AGE` is calculated at the time of the payment transaction.

## Project Structure

- [schema.sql](./schema.sql): creates the three tables
- [data.sql](./data.sql): inserts the provided sample data
- [solution.sql](./solution.sql): final SQL query
- [setup_mysql.sql](./setup_mysql.sql): one-shot MySQL setup script
- [src/Main.java](./src/Main.java): Java solution
- [run.ps1](./run.ps1): compile and run helper

## Run In MySQL

Open MySQL and run:

```sql
SOURCE setup_mysql.sql;
```

Then execute:

```sql
USE mit_java_qualifier;
SOURCE solution.sql;
```

## Run The Java Program

```powershell
.\run.ps1
```

## Final SQL Query

```sql
SELECT
    p.amount AS SALARY,
    CONCAT(e.first_name, ' ', e.last_name) AS NAME,
    TIMESTAMPDIFF(YEAR, e.dob, p.payment_time) AS AGE,
    d.department_name AS DEPARTMENT_NAME
FROM payments p
JOIN employee e
    ON p.emp_id = e.emp_id
JOIN department d
    ON e.department = d.department_id
WHERE DAY(p.payment_time) <> 1
  AND p.amount = (
      SELECT MAX(amount)
      FROM payments
      WHERE DAY(payment_time) <> 1
  );
```

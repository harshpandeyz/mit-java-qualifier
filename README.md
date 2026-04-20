# Bajaj Finserv Health Qualifier 1 - JAVA

This repository now contains a Spring Boot submission project for the qualifier workflow.

On startup, the app can:

- send the `generateWebhook` POST request
- choose the SQL answer based on the last two digits of the registration number
- submit the final SQL query to the returned webhook URL with the returned JWT token
- write a local audit file at `target/submission-result.json`

## Current Status

What is already complete:

- Spring Boot startup flow with `RestTemplate`
- no controller-based trigger
- SQL assets for the provided salary question
- Maven wrapper so the project can build without a local Maven install
- Git-ready repo structure

What you still need to provide before the live submission run:

- your real `name`
- your real `regNo`
- your real `email`
- the missing SQL query if your registration number maps to the question that is not yet filled in this repo

## Question Mapping

The app determines the question from the last two digits of `regNo`.

- odd last two digits: uses `qualifier.odd-final-query`
- even last two digits: uses `qualifier.even-final-query`

The salary query from the screenshots is prefilled as the default value for `qualifier.even-final-query`.

## Project Files

- [pom.xml](./pom.xml): Spring Boot build file
- [artifacts/mit-java-qualifier-0.0.1-SNAPSHOT.jar](./artifacts/mit-java-qualifier-0.0.1-SNAPSHOT.jar): built executable jar for submission
- [src/main/java/com/harshpandeyz/qualifier/MitJavaQualifierApplication.java](./src/main/java/com/harshpandeyz/qualifier/MitJavaQualifierApplication.java): application entry point
- [src/main/java/com/harshpandeyz/qualifier/service/QualifierFlowRunner.java](./src/main/java/com/harshpandeyz/qualifier/service/QualifierFlowRunner.java): startup runner that performs the full API flow
- [src/main/java/com/harshpandeyz/qualifier/service/FinalQueryProvider.java](./src/main/java/com/harshpandeyz/qualifier/service/FinalQueryProvider.java): odd/even query selection
- [src/main/resources/application.properties](./src/main/resources/application.properties): env-driven configuration
- [solution.sql](./solution.sql): verified SQL for the provided salary question
- [schema.sql](./schema.sql): schema setup
- [data.sql](./data.sql): sample data
- [setup_mysql.sql](./setup_mysql.sql): sample MySQL setup
- [run.ps1](./run.ps1): Spring Boot run helper

## Configure The Submission

Set these environment variables before running the app:

```powershell
$env:QUALIFIER_NAME="Your Name"
$env:QUALIFIER_REG_NO="YourRegNo"
$env:QUALIFIER_EMAIL="your@email.com"
$env:QUALIFIER_SUBMIT_ON_STARTUP="true"
```

If your `regNo` maps to the odd question and you have that SQL query, set:

```powershell
$env:QUALIFIER_ODD_FINAL_QUERY="SELECT ..."
```

If the API expects `Bearer <token>` instead of the raw token, set:

```powershell
$env:QUALIFIER_AUTH_PREFIX="Bearer"
```

## Run The App

```powershell
.\run.ps1
```

Or package the jar:

```powershell
.\mvnw.cmd clean package
```

The jar will be created under `target/`.

This repo also includes a copied submission jar in `artifacts/` so you can push it directly.

Once pushed to GitHub, the raw downloadable jar link will be:

`https://raw.githubusercontent.com/harshpandeyz/mit-java-qualifier/main/artifacts/mit-java-qualifier-0.0.1-SNAPSHOT.jar`

## Verify The SQL Locally

In MySQL:

```sql
USE mit_java_qualifier;
SOURCE C:/Users/91820/Downloads/Bajajfinserv-Q1/schema.sql;
SOURCE C:/Users/91820/Downloads/Bajajfinserv-Q1/data.sql;
SOURCE C:/Users/91820/Downloads/Bajajfinserv-Q1/solution.sql;
```

Expected result:

| SALARY | NAME | AGE | DEPARTMENT_NAME |
| --- | --- | --- | --- |
| 74998.00 | Emily Brown | 32 | Sales |

## Prefilled Final SQL

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

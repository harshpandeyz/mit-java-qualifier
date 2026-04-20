import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        List<Department> departments = List.of(
            new Department(1, "HR"),
            new Department(2, "Finance"),
            new Department(3, "Engineering"),
            new Department(4, "Sales"),
            new Department(5, "Marketing"),
            new Department(6, "IT")
        );

        List<Employee> employees = List.of(
            new Employee(1, "John", "Williams", LocalDate.parse("1980-05-15"), "Male", 3),
            new Employee(2, "Sarah", "Johnson", LocalDate.parse("1990-07-20"), "Female", 2),
            new Employee(3, "Michael", "Smith", LocalDate.parse("1985-02-10"), "Male", 3),
            new Employee(4, "Emily", "Brown", LocalDate.parse("1992-11-30"), "Female", 4),
            new Employee(5, "David", "Jones", LocalDate.parse("1988-09-05"), "Male", 5),
            new Employee(6, "Olivia", "Davis", LocalDate.parse("1995-04-12"), "Female", 1),
            new Employee(7, "James", "Wilson", LocalDate.parse("1983-03-25"), "Male", 6),
            new Employee(8, "Sophia", "Anderson", LocalDate.parse("1991-08-17"), "Female", 4),
            new Employee(9, "Liam", "Miller", LocalDate.parse("1979-12-01"), "Male", 1),
            new Employee(10, "Emma", "Taylor", LocalDate.parse("1993-06-28"), "Female", 5)
        );

        List<Payment> payments = List.of(
            new Payment(1, 2, new BigDecimal("65784.00"), LocalDateTime.parse("2025-01-01T13:44:12.824")),
            new Payment(2, 4, new BigDecimal("62736.00"), LocalDateTime.parse("2025-01-06T18:36:37.892")),
            new Payment(3, 1, new BigDecimal("69437.00"), LocalDateTime.parse("2025-01-01T10:19:21.563")),
            new Payment(4, 3, new BigDecimal("67183.00"), LocalDateTime.parse("2025-01-02T17:21:57.341")),
            new Payment(5, 2, new BigDecimal("66273.00"), LocalDateTime.parse("2025-02-01T11:49:15.764")),
            new Payment(6, 5, new BigDecimal("71475.00"), LocalDateTime.parse("2025-02-01T07:24:14.453")),
            new Payment(7, 1, new BigDecimal("70837.00"), LocalDateTime.parse("2025-02-03T19:11:31.553")),
            new Payment(8, 6, new BigDecimal("69628.00"), LocalDateTime.parse("2025-01-02T10:41:15.113")),
            new Payment(9, 4, new BigDecimal("71876.00"), LocalDateTime.parse("2025-02-01T12:16:47.807")),
            new Payment(10, 3, new BigDecimal("70098.00"), LocalDateTime.parse("2025-02-03T10:11:17.341")),
            new Payment(11, 6, new BigDecimal("67827.00"), LocalDateTime.parse("2025-02-02T19:21:27.753")),
            new Payment(12, 5, new BigDecimal("69871.00"), LocalDateTime.parse("2025-02-05T17:54:17.453")),
            new Payment(13, 2, new BigDecimal("72984.00"), LocalDateTime.parse("2025-03-05T09:37:35.974")),
            new Payment(14, 1, new BigDecimal("67982.00"), LocalDateTime.parse("2025-03-01T06:09:51.983")),
            new Payment(15, 6, new BigDecimal("70198.00"), LocalDateTime.parse("2025-03-02T10:34:35.753")),
            new Payment(16, 4, new BigDecimal("74998.00"), LocalDateTime.parse("2025-03-02T09:27:26.162"))
        );

        Map<Integer, Employee> employeeById = new HashMap<>();
        for (Employee employee : employees) {
            employeeById.put(employee.empId, employee);
        }

        Map<Integer, Department> departmentById = new HashMap<>();
        for (Department department : departments) {
            departmentById.put(department.departmentId, department);
        }

        Payment highestEligiblePayment = payments.stream()
            .filter(payment -> payment.paymentTime.getDayOfMonth() != 1)
            .max((left, right) -> left.amount.compareTo(right.amount))
            .orElseThrow();

        Employee employee = employeeById.get(highestEligiblePayment.empId);
        Department department = departmentById.get(employee.departmentId);
        int ageAtPayment = Period.between(employee.dob, highestEligiblePayment.paymentTime.toLocalDate()).getYears();

        System.out.printf("%-10s %-20s %-5s %-18s%n", "SALARY", "NAME", "AGE", "DEPARTMENT_NAME");
        System.out.printf(
            "%-10s %-20s %-5d %-18s%n",
            highestEligiblePayment.amount.setScale(2),
            employee.firstName + " " + employee.lastName,
            ageAtPayment,
            department.departmentName
        );
    }

    private record Department(int departmentId, String departmentName) {
    }

    private record Employee(
        int empId,
        String firstName,
        String lastName,
        LocalDate dob,
        String gender,
        int departmentId
    ) {
    }

    private record Payment(
        int paymentId,
        int empId,
        BigDecimal amount,
        LocalDateTime paymentTime
    ) {
    }
}

import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/employeeData";
    private static final String USER = "root";
    private static final String PASS = "k9530213";

    public DatabaseManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Database connected!");
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading MySQL driver: " + e.getMessage());
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public ResultSet getEmployeeById(int empid) throws SQLException {
        String query = "SELECT empid, Fname, Lname, email, HireDate, Salary, SSN FROM employees WHERE empid = ?";
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, empid);
        return stmt.executeQuery();
    }

    public ArrayList<Employee> searchEmployees(String fname, String lname, String ssn, Integer empid) throws SQLException {
        ArrayList<Employee> results = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT empid, Fname, Lname, email, HireDate, Salary, SSN FROM employees WHERE 1=1");
        ArrayList<Object> params = new ArrayList<>();

        if (fname != null && !fname.isEmpty()) {
            query.append(" AND Fname LIKE ?");
            params.add("%" + fname + "%");
        }
        if (lname != null && !lname.isEmpty()) {
            query.append(" AND Lname LIKE ?");
            params.add("%" + lname + "%");
        }
        if (ssn != null && !ssn.isEmpty()) {
            query.append(" AND SSN = ?");
            params.add(ssn);
        }
        if (empid != null) {
            query.append(" AND empid = ?");
            params.add(empid);
        }

        System.out.println("Executing query: " + query + " with params: " + params);

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Employee emp = new Employee(
                    rs.getInt("empid"),
                    rs.getString("Fname"),
                    rs.getString("Lname"),
                    rs.getString("email"),
                    rs.getString("HireDate"),
                    rs.getDouble("Salary"),
                    rs.getString("SSN")
                );
                results.add(emp);
            }
        }
        return results;
    }

    public void updateEmployee(int empid, String fname, String lname, String email) throws SQLException {
        String query = "UPDATE employees SET Fname = ?, Lname = ?, email = ? WHERE empid = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, fname);
            stmt.setString(2, lname);
            stmt.setString(3, email);
            stmt.setInt(4, empid);
            stmt.executeUpdate();
        }
    }

    public void updateSalariesInRange(double minSalary, double maxSalary, double percentage) throws SQLException {
        String query = "UPDATE employees SET Salary = Salary * (1 + ?/100) WHERE Salary BETWEEN ? AND ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, percentage);
            stmt.setDouble(2, minSalary);
            stmt.setDouble(3, maxSalary);
            stmt.executeUpdate();
        }
    }

    public String getPayHistoryReport(int empid) throws SQLException {
        StringBuilder report = new StringBuilder("Pay History Report\n\n");
        String query = "SELECT pay_date, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care " +
                      "FROM payroll WHERE empid = ? ORDER BY pay_date DESC";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, empid);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                report.append(String.format("Pay Date: %s\nEarnings: $%.2f\nFederal Tax: $%.2f\nMedicare: $%.2f\n" +
                                            "Social Security: $%.2f\nState Tax: $%.2f\n401k: $%.2f\nHealth Care: $%.2f\n\n",
                    rs.getString("pay_date"), rs.getDouble("earnings"), rs.getDouble("fed_tax"),
                    rs.getDouble("fed_med"), rs.getDouble("fed_SS"), rs.getDouble("state_tax"),
                    rs.getDouble("retire_401k"), rs.getDouble("health_care")));
            }
        }
        return report.toString();
    }

    public String getPayByJobReport(String date) throws SQLException {
        StringBuilder report = new StringBuilder("Pay by Job Title Report\n\n");
        String query = "SELECT jt.job_title, COUNT(*) as count, AVG(e.Salary) as avg_salary, MIN(e.Salary) as min_salary, MAX(e.Salary) as max_salary " +
                      "FROM employees e JOIN employee_job_titles ejt ON e.empid = ejt.empid " +
                      "JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id " +
                      "GROUP BY jt.job_title ORDER BY avg_salary DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                report.append(String.format("Job Title: %s\nCount: %d\nAverage Salary: $%.2f\nMin Salary: $%.2f\nMax Salary: $%.2f\n\n",
                    rs.getString("job_title"), rs.getInt("count"), rs.getDouble("avg_salary"),
                    rs.getDouble("min_salary"), rs.getDouble("max_salary")));
            }
        }
        return report.toString();
    }

    public String getPayByDivisionReport(String date) throws SQLException {
        StringBuilder report = new StringBuilder("Pay by Division Report\n\n");
        String query = "SELECT d.Name, COUNT(*) as count, AVG(e.Salary) as avg_salary, MIN(e.Salary) as min_salary, MAX(e.Salary) as max_salary " +
                      "FROM employees e JOIN employee_division ed ON e.empid = ed.empid " +
                      "JOIN division d ON ed.div_ID = d.ID " +
                      "GROUP BY d.Name ORDER BY avg_salary DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                report.append(String.format("Division: %s\nCount: %d\nAverage Salary: $%.2f\nMin Salary: $%.2f\nMax Salary: $%.2f\n\n",
                    rs.getString("Name"), rs.getInt("count"), rs.getDouble("avg_salary"),
                    rs.getDouble("min_salary"), rs.getDouble("max_salary")));
            }
        }
        return report.toString();
    }

    // Method to add a new employee
    public void addEmployee(int empid, String fname, String lname, String email, String hireDate, double salary, String ssn) throws SQLException {
        String query = "INSERT INTO employees (empid, Fname, Lname, email, HireDate, Salary, SSN) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, empid);
            stmt.setString(2, fname);
            stmt.setString(3, lname);
            stmt.setString(4, email);
            stmt.setString(5, hireDate);
            stmt.setDouble(6, salary);
            stmt.setString(7, ssn);
            stmt.executeUpdate();
        }
    }

    // Method to delete an employee
    public void deleteEmployee(int empid) throws SQLException {
        String query = "DELETE FROM employees WHERE empid = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, empid);
            stmt.executeUpdate();
        }
    }
}
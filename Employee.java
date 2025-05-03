public class Employee {
    private int empid;
    private String fname;
    private String lname;
    private String email;
    private String hireDate;
    private double salary;
    private String ssn;

    public Employee(int empid, String fname, String lname, String email, String hireDate, double salary, String ssn) {
        this.empid = empid;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.hireDate = hireDate;
        this.salary = salary;
        this.ssn = ssn;
    }

    public int getEmpid() { return empid; }
    public String getFname() { return fname; }
    public String getLname() { return lname; }
    public String getEmail() { return email; }
    public String getHireDate() { return hireDate; }
    public double getSalary() { return salary; }
    public String getSsn() { return ssn; }

    @Override
    public String toString() {
    return "ID: " + empid + "\nName: " + fname + " " + lname + "\nEmail: " + email +
               "\nHire Date: " + hireDate + "\nSalary: " + salary + "\nSSN: " + ssn;    }
}
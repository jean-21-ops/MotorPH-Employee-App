package Term2;
public class Payroll {
    private int payrollID;
    private int employeeID;
    private double salary;
    private double netSalary;

    public int getPayrollID() {
        return payrollID;
    }

    public double getSalary() {
        return salary;
    }

    public double getNetSalary() {
        return netSalary;
    }

    public void setPayrollID(int payrollID) {
        this.payrollID = payrollID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double calculateNetSalary() {
        netSalary = salary * 0.85;
        return netSalary;
    }

    public void generatePayslip() {
    }
}

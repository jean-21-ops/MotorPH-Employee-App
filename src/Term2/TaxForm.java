package Term2;

public class TaxForm {
    private int employeeID;
    private int formID;
    private int taxYear;
    private double amount;

    public int getFormID() {
        return formID;
    }

    public int getTaxYear() {
        return taxYear;
    }

    public double getAmount() {
        return amount;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public void setFormID(int formID) {
        this.formID = formID;
    }

    public void setTaxYear(int taxYear) {
        this.taxYear = taxYear;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void downloadForm() {
    }
}

package Term2;

public class Employee {
    private int employeeID;
    private String lastName;
    private String firstName;
    private String email;
    private String phoneNumber;
    private String department;
    private String sssNumber;
    private String philHealthNumber;
    private String tinNumber;
    private String pagIbigNumber;
    private String position;
    private String birthday;
    private double basicSalary;
    
    // Default constructor
    public Employee() {
        this.employeeID = 0;
        this.lastName = "";
        this.firstName = "";
        this.email = "";
        this.phoneNumber = "";
        this.department = "";
        this.sssNumber = "";
        this.philHealthNumber = "";
        this.tinNumber = "";
        this.pagIbigNumber = "";
        this.position = "";
        this.birthday = "";
        this.basicSalary = 0.0;
    }
    
    // Constructor with all fields
    public Employee(int employeeID, String lastName, String firstName, String email, 
                   String phoneNumber, String department, String sssNumber, 
                   String philHealthNumber, String tinNumber, String pagIbigNumber,
                   String position, String birthday, double basicSalary) {
        this.employeeID = employeeID;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.department = department;
        this.sssNumber = sssNumber;
        this.philHealthNumber = philHealthNumber;
        this.tinNumber = tinNumber;
        this.pagIbigNumber = pagIbigNumber;
        this.position = position;
        this.birthday = birthday;
        this.basicSalary = basicSalary;
    }
    
    // Getters
    public int getEmployeeID() { return employeeID; }
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getName() { return firstName + " " + lastName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getDepartment() { return department; }
    public String getSssNumber() { return sssNumber; }
    public String getPhilHealthNumber() { return philHealthNumber; }
    public String getTinNumber() { return tinNumber; }
    public String getPagIbigNumber() { return pagIbigNumber; }
    public String getPosition() { return position; }
    public String getBirthday() { return birthday; }
    public double getBasicSalary() { return basicSalary; }
    
    // Setters
    public void setEmployeeID(int employeeID) { this.employeeID = employeeID; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setName(String fullName) {
        String[] parts = fullName.split(" ", 2);
        this.firstName = parts[0];
        this.lastName = parts.length > 1 ? parts[1] : "";
    }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setDepartment(String department) { this.department = department; }
    public void setSssNumber(String sssNumber) { this.sssNumber = sssNumber; }
    public void setPhilHealthNumber(String philHealthNumber) { this.philHealthNumber = philHealthNumber; }
    public void setTinNumber(String tinNumber) { this.tinNumber = tinNumber; }
    public void setPagIbigNumber(String pagIbigNumber) { this.pagIbigNumber = pagIbigNumber; }
    public void setPosition(String position) { this.position = position; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }
    
    // CSV methods
    public String toCSV() {
        return String.join(",",
            String.valueOf(employeeID),
            lastName != null ? lastName : "",
            firstName != null ? firstName : "",
            email != null ? email : "",
            phoneNumber != null ? phoneNumber : "",
            department != null ? department : "",
            sssNumber != null ? sssNumber : "",
            philHealthNumber != null ? philHealthNumber : "",
            tinNumber != null ? tinNumber : "",
            pagIbigNumber != null ? pagIbigNumber : "",
            position != null ? position : "",
            birthday != null ? birthday : "",
            String.valueOf(basicSalary)
        );
    }
    
    public static Employee fromCSV(String csvLine) {
        try {
            String[] parts = csvLine.split(",");
            if (parts.length >= 13) {
                return new Employee(
                    Integer.parseInt(parts[0].trim()),
                    parts[1].trim(),
                    parts[2].trim(),
                    parts[3].trim(),
                    parts[4].trim(),
                    parts[5].trim(),
                    parts[6].trim(),
                    parts[7].trim(),
                    parts[8].trim(),
                    parts[9].trim(),
                    parts[10].trim(),
                    parts[11].trim(),
                    Double.parseDouble(parts[12].trim())
                );
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Error parsing CSV line: " + e.getMessage());
        }
        return null;
    }
}
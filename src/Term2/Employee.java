package Term2;

public class Employee {
    private int employeeID;
    private String lastName;
    private String firstName;
    private String birthday;
    private String address;
    private String phoneNumber;
    private String sssNumber;
    private String philHealthNumber;
    private String tinNumber;
    private String pagIbigNumber;
    private String status;
    private String position;
    private String immediateSupervisor;
    private double basicSalary;
    private double riceSubsidy;
    private double phoneAllowance;
    private double clothingAllowance;
    private double grossSemiMonthlyRate;
    private double hourlyRate;
    
    // Legacy fields for compatibility
    private String email;
    private String department;
    
    // Default constructor
    public Employee() {
        this.employeeID = 0;
        this.lastName = "";
        this.firstName = "";
        this.birthday = "";
        this.address = "";
        this.phoneNumber = "";
        this.sssNumber = "";
        this.philHealthNumber = "";
        this.tinNumber = "";
        this.pagIbigNumber = "";
        this.status = "";
        this.position = "";
        this.immediateSupervisor = "";
        this.basicSalary = 0.0;
        this.riceSubsidy = 0.0;
        this.phoneAllowance = 0.0;
        this.clothingAllowance = 0.0;
        this.grossSemiMonthlyRate = 0.0;
        this.hourlyRate = 0.0;
        this.email = "";
        this.department = "";
    }
    
    // Constructor with all fields for CSV compatibility
    public Employee(int employeeID, String lastName, String firstName, String birthday,
                   String address, String phoneNumber, String sssNumber, 
                   String philHealthNumber, String tinNumber, String pagIbigNumber,
                   String status, String position, String immediateSupervisor, 
                   double basicSalary, double riceSubsidy, double phoneAllowance,
                   double clothingAllowance, double grossSemiMonthlyRate, double hourlyRate) {
        this.employeeID = employeeID;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthday = birthday;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.sssNumber = sssNumber;
        this.philHealthNumber = philHealthNumber;
        this.tinNumber = tinNumber;
        this.pagIbigNumber = pagIbigNumber;
        this.status = status;
        this.position = position;
        this.immediateSupervisor = immediateSupervisor;
        this.basicSalary = basicSalary;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.grossSemiMonthlyRate = grossSemiMonthlyRate;
        this.hourlyRate = hourlyRate;
        
        // Set legacy fields for compatibility
        this.email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@motorph.com";
        this.department = determineDepartmentFromPosition(position);
    }
    
    // Legacy constructor for backward compatibility
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
        
        // Set new fields to defaults
        this.address = "";
        this.status = "Regular";
        this.immediateSupervisor = "";
        this.riceSubsidy = 1500.0;
        this.phoneAllowance = 1000.0;
        this.clothingAllowance = 1000.0;
        this.grossSemiMonthlyRate = basicSalary / 2;
        this.hourlyRate = basicSalary / 168; // Approximate monthly hours
    }
    
    private String determineDepartmentFromPosition(String position) {
        if (position == null) return "General";
        
        String pos = position.toLowerCase();
        if (pos.contains("ceo") || pos.contains("chief executive")) return "Executive";
        if (pos.contains("cfo") || pos.contains("chief finance")) return "Finance";
        if (pos.contains("coo") || pos.contains("chief operating")) return "Operations";
        if (pos.contains("cmo") || pos.contains("chief marketing")) return "Marketing";
        if (pos.contains("hr") || pos.contains("human resource")) return "HR";
        if (pos.contains("it") || pos.contains("information technology")) return "IT";
        if (pos.contains("payroll")) return "Finance";
        if (pos.contains("account")) return "Accounting";
        if (pos.contains("sales") || pos.contains("marketing")) return "Sales";
        if (pos.contains("supply chain") || pos.contains("logistics")) return "Operations";
        if (pos.contains("customer service")) return "Customer Service";
        
        return "General";
    }
    
    // Getters
    public int getEmployeeID() { return employeeID; }
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getName() { return firstName + " " + lastName; }
    public String getBirthday() { return birthday; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getSssNumber() { return sssNumber; }
    public String getPhilHealthNumber() { return philHealthNumber; }
    public String getTinNumber() { return tinNumber; }
    public String getPagIbigNumber() { return pagIbigNumber; }
    public String getStatus() { return status; }
    public String getPosition() { return position; }
    public String getImmediateSupervisor() { return immediateSupervisor; }
    public double getBasicSalary() { return basicSalary; }
    public double getRiceSubsidy() { return riceSubsidy; }
    public double getPhoneAllowance() { return phoneAllowance; }
    public double getClothingAllowance() { return clothingAllowance; }
    public double getGrossSemiMonthlyRate() { return grossSemiMonthlyRate; }
    public double getHourlyRate() { return hourlyRate; }
    
    // Legacy getters for compatibility
    public String getEmail() { return email; }
    public String getDepartment() { return department; }
    
    // Setters
    public void setEmployeeID(int employeeID) { this.employeeID = employeeID; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setName(String fullName) {
        String[] parts = fullName.split(" ", 2);
        this.firstName = parts[0];
        this.lastName = parts.length > 1 ? parts[1] : "";
    }
    public void setBirthday(String birthday) { this.birthday = birthday; }
    public void setAddress(String address) { this.address = address; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setSssNumber(String sssNumber) { this.sssNumber = sssNumber; }
    public void setPhilHealthNumber(String philHealthNumber) { this.philHealthNumber = philHealthNumber; }
    public void setTinNumber(String tinNumber) { this.tinNumber = tinNumber; }
    public void setPagIbigNumber(String pagIbigNumber) { this.pagIbigNumber = pagIbigNumber; }
    public void setStatus(String status) { this.status = status; }
    public void setPosition(String position) { 
        this.position = position; 
        this.department = determineDepartmentFromPosition(position);
    }
    public void setImmediateSupervisor(String immediateSupervisor) { this.immediateSupervisor = immediateSupervisor; }
    public void setBasicSalary(double basicSalary) { 
        this.basicSalary = basicSalary;
        this.grossSemiMonthlyRate = basicSalary / 2;
        this.hourlyRate = basicSalary / 168;
    }
    public void setRiceSubsidy(double riceSubsidy) { this.riceSubsidy = riceSubsidy; }
    public void setPhoneAllowance(double phoneAllowance) { this.phoneAllowance = phoneAllowance; }
    public void setClothingAllowance(double clothingAllowance) { this.clothingAllowance = clothingAllowance; }
    public void setGrossSemiMonthlyRate(double grossSemiMonthlyRate) { this.grossSemiMonthlyRate = grossSemiMonthlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }
    
    // Legacy setters for compatibility
    public void setEmail(String email) { this.email = email; }
    public void setDepartment(String department) { this.department = department; }
    
    // Helper method to clean numeric values from CSV
    private static double parseNumericValue(String value) {
        if (value == null || value.trim().isEmpty()) return 0.0;
        
        // Remove quotes, commas, and whitespace
        String cleaned = value.replaceAll("[\"',\\s]", "");
        
        try {
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    // CSV methods
    public String toCSV() {
        return String.join(",",
            String.valueOf(employeeID),
            lastName != null ? lastName : "",
            firstName != null ? firstName : "",
            birthday != null ? birthday : "",
            address != null ? "\"" + address + "\"" : "",
            phoneNumber != null ? phoneNumber : "",
            sssNumber != null ? sssNumber : "",
            philHealthNumber != null ? philHealthNumber : "",
            tinNumber != null ? tinNumber : "",
            pagIbigNumber != null ? pagIbigNumber : "",
            status != null ? status : "",
            position != null ? position : "",
            immediateSupervisor != null ? "\"" + immediateSupervisor + "\"" : "",
            "\"" + String.format("%.0f", basicSalary) + "\"",
            "\"" + String.format("%.0f", riceSubsidy) + "\"",
            "\"" + String.format("%.0f", phoneAllowance) + "\"",
            "\"" + String.format("%.0f", clothingAllowance) + "\"",
            "\"" + String.format("%.0f", grossSemiMonthlyRate) + "\"",
            String.format("%.2f", hourlyRate)
        );
    }
    
    public static Employee fromCSV(String csvLine) {
        try {
            // Handle CSV parsing with proper quote handling
            String[] parts = parseCSVLine(csvLine);
            
            if (parts.length >= 19) {
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
                    parts[12].trim(),
                    parseNumericValue(parts[13]),
                    parseNumericValue(parts[14]),
                    parseNumericValue(parts[15]),
                    parseNumericValue(parts[16]),
                    parseNumericValue(parts[17]),
                    parseNumericValue(parts[18])
                );
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Error parsing CSV line: " + e.getMessage());
            System.err.println("Line: " + csvLine);
        }
        return null;
    }
    
    // Helper method to properly parse CSV lines with quotes and commas
    @SuppressWarnings("CollectionsToArray")
    private static String[] parseCSVLine(String line) {
        java.util.List<String> result = new java.util.ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        
        result.add(current.toString());
        return result.toArray(new String[0]);
    }
}
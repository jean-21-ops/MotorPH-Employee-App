// package Term2;
package Term2;
import java.io.*;
import java.util.*;

public class CSVManager {
    private static final String CSV_FILE_PATH = "employees.csv";
    private static final String CSV_HEADER = "Employee #,Last Name,First Name,Birthday,Address,Phone Number,SSS #,Philhealth #,TIN #,Pag-ibig #,Status,Position,Immediate Supervisor,Basic Salary,Rice Subsidy,Phone Allowance,Clothing Allowance,Gross Semi-monthly Rate,Hourly Rate";
    
    public static void initializeCSVFile() {
        File file = new File(CSV_FILE_PATH);
        // Don't overwrite existing CSV file - it contains the school data
        if (!file.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println(CSV_HEADER);
                // Add minimal sample data if no file exists
                writer.println("1001,Doe,John,01/15/1990,\"123 Main St, City\",123-456-7890,12-3456789-0,12-345678901-2,123-456-789-012,1234567890123,Regular,Software Developer,\"Manager Name\",\"50000\",\"1500\",\"1000\",\"1000\",\"25000\",297.62");
            } catch (IOException e) {
                System.err.println("Error creating CSV file: " + e.getMessage());
            }
        }
    }
    
    public static ArrayList<Employee> loadEmployeesFromCSV() {
        ArrayList<Employee> employees = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            reader.readLine(); // Skip header
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Employee emp = Employee.fromCSV(line);
                    if (emp != null) {
                        employees.add(emp);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            initializeCSVFile(); // Create file if it doesn't exist
        }
        
        return employees;
    }
    
    public static void saveEmployeesToCSV(ArrayList<Employee> employees) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE_PATH))) {
            writer.println(CSV_HEADER);
            
            for (Employee emp : employees) {
                writer.println(emp.toCSV());
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }
    
    public static void appendEmployeeToCSV(Employee employee) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE_PATH, true))) {
            writer.println(employee.toCSV());
        } catch (IOException e) {
            System.err.println("Error appending to CSV file: " + e.getMessage());
        }
    }
}
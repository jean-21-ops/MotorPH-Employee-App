// package Term2;
package Term2;
import java.io.*;
import java.util.*;

public class CSVManager {
    private static final String CSV_FILE_PATH = "employees.csv";
    private static final String CSV_HEADER = "Employee_ID,Last_Name,First_Name,Email,Phone,Department,SSS,PhilHealth,TIN,PagIBIG,Position,Birthday,Basic_Salary";
    
    public static void initializeCSVFile() {
        File file = new File(CSV_FILE_PATH);
        if (!file.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println(CSV_HEADER);
                // Add sample data
                writer.println("1001,Doe,John,john.doe@motorph.com,123-456-7890,IT,12-3456789-0,12-345678901-2,123-456-789-012,1234567890123,Software Developer,01/15/1990,50000");
                writer.println("1002,Smith,Jane,jane.smith@motorph.com,098-765-4321,HR,98-7654321-0,98-765432109-8,987-654-321-098,9876543210987,HR Specialist,03/22/1985,45000");
                writer.println("1003,Johnson,Mike,mike.johnson@motorph.com,555-123-4567,Finance,55-5123456-7,55-512345678-9,555-123-456-789,5551234567890,Financial Analyst,07/10/1992,48000");
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
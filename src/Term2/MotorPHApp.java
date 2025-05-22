package Term2;

import java.awt.*;
import javax.swing.*;

public class MotorPHApp extends JFrame {
    @SuppressWarnings("resource")

    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    
    // Objects for different modules
    private final Employee employee;
    private final Payroll payroll;
    private final LeaveRequest leaveRequest;
    private final TaxForm taxForm;
    private final Login login;
    private final HRAdmin hrAdmin;
    
    // Panels for different screens
    private JPanel loginPanel;
    private JPanel dashboardPanel;
    private JPanel employeeInfoPanel;
    private JPanel payrollPanel;
    private JPanel leaveRequestPanel;
    private JPanel taxFormPanel;
    private JPanel hrAdminPanel;
    
    public MotorPHApp() {
        // Initialize objects
        employee = new Employee();
        payroll = new Payroll();
        leaveRequest = new LeaveRequest();
        taxForm = new TaxForm();
        login = new Login();
        hrAdmin = new HRAdmin();
        
        // Setup demo account
        login.setEmail("admin@motorphapp.com");
        login.setPassword("password123");
        
        // Configure main frame
        setTitle("MotorPH Employee App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create card layout for switching between panels
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Initialize panels
        createLoginPanel();
        createDashboardPanel();
        createEmployeeInfoPanel();
        createPayrollPanel();
        createLeaveRequestPanel();
        createTaxFormPanel();
        createHRAdminPanel();
        
        // Add panels to main panel
        mainPanel.add(loginPanel, "login");
        mainPanel.add(dashboardPanel, "dashboard");
        mainPanel.add(employeeInfoPanel, "employeeInfo");
        mainPanel.add(payrollPanel, "payroll");
        mainPanel.add(leaveRequestPanel, "leaveRequest");
        mainPanel.add(taxFormPanel, "taxForm");
        mainPanel.add(hrAdminPanel, "hrAdmin");
        
        // Show login panel first
        cardLayout.show(mainPanel, "login");
        
        // Add main panel to frame
        add(mainPanel);
    }
    
    private void createLoginPanel() {
        loginPanel = new JPanel();
        loginPanel.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Title
        JLabel titleLabel = new JLabel("MotorPH Employee Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        loginPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Email field
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);
        
        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        
        // Login button
        JButton loginButton = new JButton("Login");
        
        // Add components to form
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(emailField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(passwordField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginButton, gbc);
        
        // Add form panel to login panel
        loginPanel.add(formPanel, BorderLayout.CENTER);
        
        // Login button action
        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            
            if (login.login(email, password)) {
                // Set demo employee data
                employee.setEmployeeID(1001);
                employee.setName("John Doe");
                employee.setEmail(email);
                employee.setDepartment("IT");
                employee.setPhoneNumber("123-456-7890");
                
                payroll.setEmployeeID(1001);
                payroll.setSalary(50000.0);
                
                // Show dashboard
                cardLayout.show(mainPanel, "dashboard");
            } else {
                JOptionPane.showMessageDialog(loginPanel, 
                    "Invalid email or password", 
                    "Login Failed", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    private void createDashboardPanel() {
        dashboardPanel = new JPanel(new BorderLayout());
        
        // Title
        JLabel titleLabel = new JLabel("MotorPH Employee Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        dashboardPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Menu buttons
        JPanel menuPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JButton employeeInfoButton = new JButton("Employee Information");
        JButton payrollButton = new JButton("Payroll");
        JButton leaveRequestButton = new JButton("Leave Requests");
        JButton taxFormButton = new JButton("Tax Forms");
        JButton hrAdminButton = new JButton("HR Admin");
        JButton logoutButton = new JButton("Logout");
        
        menuPanel.add(employeeInfoButton);
        menuPanel.add(payrollButton);
        menuPanel.add(leaveRequestButton);
        menuPanel.add(taxFormButton);
        menuPanel.add(hrAdminButton);
        
        // Add button actions
        employeeInfoButton.addActionListener(e -> cardLayout.show(mainPanel, "employeeInfo"));
        payrollButton.addActionListener(e -> cardLayout.show(mainPanel, "payroll"));
        leaveRequestButton.addActionListener(e -> cardLayout.show(mainPanel, "leaveRequest"));
        taxFormButton.addActionListener(e -> cardLayout.show(mainPanel, "taxForm"));
        hrAdminButton.addActionListener(e -> cardLayout.show(mainPanel, "hrAdmin"));
        logoutButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        
        // Main content
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel welcomeLabel = new JLabel("Welcome, " + employee.getName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        contentPanel.add(welcomeLabel, gbc);
        
        gbc.gridy = 1;
        contentPanel.add(new JLabel("Employee ID: " + employee.getEmployeeID()), gbc);
        
        gbc.gridy = 2;
        contentPanel.add(new JLabel("Department: " + employee.getDepartment()), gbc);
        
        gbc.gridy = 3;
        gbc.insets = new Insets(30, 10, 10, 10);
        contentPanel.add(logoutButton, gbc);
        
        // Add panels to dashboard
        dashboardPanel.add(menuPanel, BorderLayout.WEST);
        dashboardPanel.add(contentPanel, BorderLayout.CENTER);
    }
    
    private void createEmployeeInfoPanel() {
        employeeInfoPanel = new JPanel(new BorderLayout());
        
        // Back button
        JButton backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "dashboard"));
        
        // Title
        JLabel titleLabel = new JLabel("Employee Information", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        formPanel.add(new JLabel("Employee ID:"));
        JLabel idLabel = new JLabel(String.valueOf(employee.getEmployeeID()));
        formPanel.add(idLabel);
        
        formPanel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField(employee.getName());
        formPanel.add(nameField);
        
        formPanel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField(employee.getEmail());
        formPanel.add(emailField);
        
        formPanel.add(new JLabel("Department:"));
        JTextField deptField = new JTextField(employee.getDepartment());
        formPanel.add(deptField);
        
        formPanel.add(new JLabel("Phone:"));
        JTextField phoneField = new JTextField(employee.getPhoneNumber());
        formPanel.add(phoneField);
        
        // Update button
        JButton updateButton = new JButton("Update Information");
        updateButton.addActionListener(e -> {
            employee.setName(nameField.getText());
            employee.setEmail(emailField.getText());
            employee.setDepartment(deptField.getText());
            employee.setPhoneNumber(phoneField.getText());
            
            JOptionPane.showMessageDialog(employeeInfoPanel, 
                "Employee information updated successfully", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(updateButton);
        buttonPanel.add(backButton);
        
        // Add components to panel
        employeeInfoPanel.add(titleLabel, BorderLayout.NORTH);
        employeeInfoPanel.add(formPanel, BorderLayout.CENTER);
        employeeInfoPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void createPayrollPanel() {
        payrollPanel = new JPanel(new BorderLayout());
        
        // Back button
        JButton backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "dashboard"));
        
        // Title
        JLabel titleLabel = new JLabel("Payroll Information", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        // Payroll info
        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        infoPanel.add(new JLabel("Employee ID:"));
        infoPanel.add(new JLabel(String.valueOf(employee.getEmployeeID())));
        
        infoPanel.add(new JLabel("Employee Name:"));
        infoPanel.add(new JLabel(employee.getName()));
        
        infoPanel.add(new JLabel("Gross Salary:"));
        infoPanel.add(new JLabel("$" + String.format("%.2f", payroll.getSalary())));
        
        double netSalary = payroll.calculateNetSalary();
        infoPanel.add(new JLabel("Net Salary:"));
        infoPanel.add(new JLabel("$" + String.format("%.2f", netSalary)));
        
        infoPanel.add(new JLabel("Tax Deduction:"));
        infoPanel.add(new JLabel("$" + String.format("%.2f", payroll.getSalary() - netSalary)));
        
        // Buttons
        JButton generatePayslipButton = new JButton("Generate Payslip");
        generatePayslipButton.addActionListener(e -> {
            payroll.generatePayslip();
            JOptionPane.showMessageDialog(payrollPanel, 
                "Payslip generated successfully", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(generatePayslipButton);
        buttonPanel.add(backButton);
        
        // Add components to panel
        payrollPanel.add(titleLabel, BorderLayout.NORTH);
        payrollPanel.add(infoPanel, BorderLayout.CENTER);
        payrollPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void createLeaveRequestPanel() {
        leaveRequestPanel = new JPanel(new BorderLayout());
        
        // Back button
        JButton backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "dashboard"));
        
        // Title
        JLabel titleLabel = new JLabel("Leave Request", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        // Leave request form
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        formPanel.add(new JLabel("Employee ID:"));
        formPanel.add(new JLabel(String.valueOf(employee.getEmployeeID())));
        
        formPanel.add(new JLabel("Leave Type:"));
        String[] leaveTypes = {"Vacation", "Sick", "Personal"};
        JComboBox<String> leaveTypeCombo = new JComboBox<>(leaveTypes);
        formPanel.add(leaveTypeCombo);
        
        formPanel.add(new JLabel("Start Date (MM/DD/YYYY):"));
        JTextField startDateField = new JTextField();
        formPanel.add(startDateField);
        
        formPanel.add(new JLabel("End Date (MM/DD/YYYY):"));
        JTextField endDateField = new JTextField();
        formPanel.add(endDateField);
        
        formPanel.add(new JLabel("Status:"));
        JLabel statusLabel = new JLabel("New Request");
        formPanel.add(statusLabel);
        
        // Submit button
        JButton submitButton = new JButton("Submit Request");
        submitButton.addActionListener(e -> {
            leaveRequest.setEmployeeID(employee.getEmployeeID());
            leaveRequest.setLeaveType((String) leaveTypeCombo.getSelectedItem());
            leaveRequest.setStartDate(startDateField.getText());
            leaveRequest.setEndDate(endDateField.getText());
            leaveRequest.setStatus("Pending");
            leaveRequest.submitRequest();
            
            statusLabel.setText("Pending");
            
            JOptionPane.showMessageDialog(leaveRequestPanel, 
                "Leave request submitted successfully", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(submitButton);
        buttonPanel.add(backButton);
        
        // Add components to panel
        leaveRequestPanel.add(titleLabel, BorderLayout.NORTH);
        leaveRequestPanel.add(formPanel, BorderLayout.CENTER);
        leaveRequestPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void createTaxFormPanel() {
        taxFormPanel = new JPanel(new BorderLayout());
        
        // Back button
        JButton backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "dashboard"));
        
        // Title
        JLabel titleLabel = new JLabel("Tax Forms", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        // Tax form info
        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Set tax form data
        taxForm.setEmployeeID(employee.getEmployeeID());
        taxForm.setFormID(1001);
        taxForm.setTaxYear(2024);
        taxForm.setAmount(payroll.getSalary() * 0.15); // 15% tax
        
        infoPanel.add(new JLabel("Employee ID:"));
        infoPanel.add(new JLabel(String.valueOf(taxForm.getEmployeeID())));
        
        infoPanel.add(new JLabel("Form ID:"));
        infoPanel.add(new JLabel(String.valueOf(taxForm.getFormID())));
        
        infoPanel.add(new JLabel("Tax Year:"));
        infoPanel.add(new JLabel(String.valueOf(taxForm.getTaxYear())));
        
        infoPanel.add(new JLabel("Tax Amount:"));
        infoPanel.add(new JLabel("$" + String.format("%.2f", taxForm.getAmount())));
        
        // Download button
        JButton downloadButton = new JButton("Download Tax Form");
        downloadButton.addActionListener(e -> {
            taxForm.downloadForm();
            JOptionPane.showMessageDialog(taxFormPanel, 
                "Tax form downloaded successfully", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(downloadButton);
        buttonPanel.add(backButton);
        
        // Add components to panel
        taxFormPanel.add(titleLabel, BorderLayout.NORTH);
        taxFormPanel.add(infoPanel, BorderLayout.CENTER);
        taxFormPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void createHRAdminPanel() {
        hrAdminPanel = new JPanel(new BorderLayout());
        
        // Back button
        JButton backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "dashboard"));
        
        // Title
        JLabel titleLabel = new JLabel("HR Administration", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        // HR Admin form
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        formPanel.add(new JLabel("Admin ID:"));
        JTextField adminIdField = new JTextField("1000");
        formPanel.add(adminIdField);
        
        formPanel.add(new JLabel("Admin Name:"));
        JTextField adminNameField = new JTextField("Admin User");
        formPanel.add(adminNameField);
        
        formPanel.add(new JLabel("Employee ID to Manage:"));
        JTextField employeeIdField = new JTextField();
        formPanel.add(employeeIdField);
        
        // Manage button
        JButton manageButton = new JButton("Manage Employee Record");
        manageButton.addActionListener(e -> {
            try {
                hrAdmin.setAdminID(Integer.parseInt(adminIdField.getText()));
                hrAdmin.setName(adminNameField.getText());
                
                int empId = Integer.parseInt(employeeIdField.getText());
                hrAdmin.manageEmployeeRecord(empId);
                
                JOptionPane.showMessageDialog(hrAdminPanel, 
                    "Employee record management initiated for ID: " + empId, 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(hrAdminPanel, 
                    "Please enter valid numeric IDs", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(manageButton);
        buttonPanel.add(backButton);
        
        // Add components to panel
        hrAdminPanel.add(titleLabel, BorderLayout.NORTH);
        hrAdminPanel.add(formPanel, BorderLayout.CENTER);
        hrAdminPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public static void main(String[] args) {
        // Use the Event Dispatch Thread for Swing applications
        SwingUtilities.invokeLater(() -> {
            MotorPHApp app = new MotorPHApp();
            app.setVisible(true);
        });
    }
}
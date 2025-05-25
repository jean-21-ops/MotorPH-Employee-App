package Term2;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MotorPHApp extends JFrame {
    private static final long serialVersionUID = 1L;

    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    
    private final Employee employee;
    private final Payroll payroll;
    private final LeaveRequest leaveRequest;
    private final TaxForm taxForm;
    private final Login login;
    
    private final ArrayList<AttendanceRecord> attendanceRecords;
    
    private JPanel loginPanel;
    private JPanel dashboardPanel;
    private JPanel employeeInfoPanel;
    private JPanel payrollPanel;
    private JPanel leaveRequestPanel;
    private JPanel taxFormPanel;
    private JPanel attendancePanel;
    private JPanel employeeManagementPanel;
    private JPanel employeeListPanel;
    private ArrayList<Employee> employeeList; // To store employees
        
    public MotorPHApp() {
        // Try to set system look and feel for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.put("Button.background", new Color(0, 102, 204));
            UIManager.put("Button.foreground", Color.WHITE);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // Log or handle the exception appropriately in production
        }
        
        employee = new Employee();
        payroll = new Payroll();
        leaveRequest = new LeaveRequest();
        taxForm = new TaxForm();
        login = new Login();
        attendanceRecords = new ArrayList<>();
        
        createSampleAttendanceData();
        
        // Setup demo account
        login.setEmail("123");
        login.setPassword("123");
        
        // Configure main frame
        setTitle("MotorPH Employee App");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the main content panel with BorderLayout
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        
        // Create permanent sidebar panel
        JPanel sidebarPanel = createSidebarPanel();
        mainContentPanel.add(sidebarPanel, BorderLayout.WEST);
        
        // Create card layout for switching between panels
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainContentPanel.add(mainPanel, BorderLayout.CENTER);
        
        // Initialize panels
        createLoginPanel();
        createDashboardPanel();
        createEmployeeInfoPanel();
        createPayrollPanel();
        createLeaveRequestPanel();
        createTaxFormPanel();
        createAttendancePanel();
        createEmployeeManagementPanel();
        createEmployeeListPanel();
        
        mainPanel.add(loginPanel, "login");
        mainPanel.add(dashboardPanel, "dashboard");
        mainPanel.add(employeeInfoPanel, "employeeInfo");
        mainPanel.add(payrollPanel, "payroll");
        mainPanel.add(leaveRequestPanel, "leaveRequest");
        mainPanel.add(taxFormPanel, "taxForm");
        mainPanel.add(attendancePanel, "attendance");
        mainPanel.add(employeeManagementPanel, "employeeManagement");
        mainPanel.add(employeeListPanel, "employeeList");
        
        cardLayout.show(mainPanel, "login");
        
        add(mainContentPanel);
    }

    // Method to create the persistent sidebar
    private JPanel createSidebarPanel() {
        // Create menu panel (sidebar) with blue background
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(0, 102, 204));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        menuPanel.setPreferredSize(new Dimension(220, 600));
        
        // Add company logo/name to the top of sidebar
        JLabel logoLabel = new JLabel("MotorPH");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 20));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        menuPanel.add(logoLabel);
        
        // Create menu buttons
        JButton employeeManagementButton = new JButton("Employee Management");
        JButton employeeInfoButton = new JButton("Employee Information");
        JButton payrollButton = new JButton("Payroll & Calculations");
        JButton leaveRequestButton = new JButton("Leave Requests");
        JButton taxFormButton = new JButton("Tax Forms");
        JButton attendanceButton = new JButton("Attendance Records");
        JButton logoutButton = new JButton("Logout");
        
        // Style all menu buttons
        styleMenuButton(employeeManagementButton);
        styleMenuButton(employeeInfoButton);
        styleMenuButton(payrollButton);
        styleMenuButton(leaveRequestButton);
        styleMenuButton(taxFormButton);
        styleMenuButton(attendanceButton);
        styleMenuButton(logoutButton);
        
        // Add spacing between buttons
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(employeeInfoButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(payrollButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(leaveRequestButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(taxFormButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(attendanceButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(employeeManagementButton);
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(logoutButton);
        
        // Add button actions
        employeeManagementButton.addActionListener(e -> cardLayout.show(mainPanel, "employeeManagement"));
        employeeInfoButton.addActionListener(e -> cardLayout.show(mainPanel, "employeeInfo"));
        payrollButton.addActionListener(e -> cardLayout.show(mainPanel, "payroll"));
        leaveRequestButton.addActionListener(e -> cardLayout.show(mainPanel, "leaveRequest"));
        taxFormButton.addActionListener(e -> cardLayout.show(mainPanel, "taxForm"));
        attendanceButton.addActionListener(e -> cardLayout.show(mainPanel, "attendance"));
        logoutButton.addActionListener(e -> {
            // Record logout time for today's attendance
            for (AttendanceRecord record : attendanceRecords) {
                if (record.getDate().equals(new Date())) {
                    record.setLogoutTime(new Date());
                    record.setHoursWorked(
                        (record.getLogoutTime().getTime() - record.getLoginTime().getTime()) / 3600000.0
                    );
                    break;
                }
            }
            cardLayout.show(mainPanel, "login");
            menuPanel.setVisible(false); // Hide sidebar when logged out
        });
        
        // Initially hide the sidebar until login
        menuPanel.setVisible(false);
        
        // After successful login, we'll make the sidebar visible
        return menuPanel;
    }

    private void createEmployeeManagementPanel() {
        employeeManagementPanel = new JPanel(new BorderLayout());
        employeeManagementPanel.setBackground(Color.WHITE);
        
        // Header with blue background
        JPanel headerPanel = new JPanel(new BorderLayout());
        styleHeaderPanel(headerPanel);
        
        JLabel titleLabel = new JLabel("EMPLOYEE MANAGEMENT", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(new Color(0, 102, 204));
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        forceButtonStyle(searchButton);
        searchPanel.add(new JLabel("Search: "));
        searchPanel.getComponent(0).setForeground(Color.WHITE);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(searchPanel, BorderLayout.EAST);
        
        // Form Panel with white background
        JPanel formPanel = new JPanel(new GridBagLayout());
        stylePanel(formPanel);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Add New Employee");
        subtitleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        subtitleLabel.setForeground(new Color(0, 102, 204));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(subtitleLabel, gbc);
        
        // Employee Number field with styled border
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel empNumberLabel = new JLabel("Employee Number:");
        formPanel.add(empNumberLabel, gbc);
        
        gbc.gridx = 1;
        JTextField empNumberField = new JTextField(15);
        empNumberField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formPanel.add(empNumberField, gbc);
        
        // Name field with styled border
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Name:"), gbc);
        
        gbc.gridx = 1;
        JTextField nameField = new JTextField(15);
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formPanel.add(nameField, gbc);
        
        // Birthday field with styled border
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Birthday (MM/DD/YYYY):"), gbc);
        
        gbc.gridx = 1;
        JTextField birthdayField = new JTextField(15);
        birthdayField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formPanel.add(birthdayField, gbc);
        
        // Contact Information field with styled border
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Contact Information:"), gbc);
        
        gbc.gridx = 1;
        JTextField contactField = new JTextField(15);
        contactField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formPanel.add(contactField, gbc);
        
        // Position field with styled border
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Position:"), gbc);
        
        gbc.gridx = 1;
        JTextField positionField = new JTextField(15);
        positionField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formPanel.add(positionField, gbc);
        
        // Department dropdown
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Department:"), gbc);
        
        gbc.gridx = 1;
        String[] departments = {"IT", "HR", "Finance", "Operations", "Marketing", "Sales"};
        JComboBox<String> departmentCombo = new JComboBox<>(departments);
        departmentCombo.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204)));
        formPanel.add(departmentCombo, gbc);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton addButton = new JButton("ADD EMPLOYEE");
        forceButtonStyle(addButton);
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        JButton listButton = new JButton("Employee List →");
        forceButtonStyle(listButton);
        
        buttonPanel.add(addButton);
        buttonPanel.add(listButton);
        
        // Add button actions (keeping the existing logic)
        addButton.addActionListener(e -> {
            try {
                int empNumber = Integer.parseInt(empNumberField.getText());
                String name = nameField.getText();
                String birthday = birthdayField.getText();
                String contact = contactField.getText();
                String position = positionField.getText();
                String department = (String) departmentCombo.getSelectedItem();
                
                // Validate inputs
                if (name.isEmpty() || birthday.isEmpty() || contact.isEmpty() || position.isEmpty()) {
                    JOptionPane.showMessageDialog(employeeManagementPanel,
                        "Please fill all fields",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Create and add employee
                Employee newEmployee = new Employee();
                newEmployee.setEmployeeID(empNumber);
                newEmployee.setName(name);
                newEmployee.setPhoneNumber(contact);
                newEmployee.setDepartment(department);
                
                if (employeeList == null) {
                    employeeList = new ArrayList<>();
                }
                employeeList.add(newEmployee);
                
                // Clear fields
                empNumberField.setText("");
                nameField.setText("");
                birthdayField.setText("");
                contactField.setText("");
                positionField.setText("");
                
                JOptionPane.showMessageDialog(employeeManagementPanel,
                    "Employee added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(employeeManagementPanel,
                    "Please enter a valid employee number",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // List button action
        listButton.addActionListener(e -> {
            updateEmployeeListTable();
            cardLayout.show(mainPanel, "employeeList");
        });
        
        // Assemble all panels
        employeeManagementPanel.add(headerPanel, BorderLayout.NORTH);
        employeeManagementPanel.add(formPanel, BorderLayout.CENTER);
        employeeManagementPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void createEmployeeListPanel() {
        employeeListPanel = new JPanel(new BorderLayout());
        employeeListPanel.setBackground(Color.WHITE);
        
        // Header with blue background
        JPanel headerPanel = new JPanel(new BorderLayout());
        styleHeaderPanel(headerPanel);
        
        JLabel titleLabel = new JLabel("EMPLOYEE LIST", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(new Color(0, 102, 204));
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        forceButtonStyle(searchButton);
        searchPanel.add(new JLabel("Search: "));
        searchPanel.getComponent(0).setForeground(Color.WHITE);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(searchPanel, BorderLayout.EAST);
        
        // Table with styled header
        String[] columnNames = {"ID", "Name", "Birthday", "Contact", "Position", "Department"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable employeeTable = new JTable(model);
        employeeTable.getTableHeader().setBackground(new Color(0, 102, 204));
        employeeTable.getTableHeader().setForeground(Color.WHITE);
        employeeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        employeeTable.setRowHeight(25);
        employeeTable.setGridColor(new Color(220, 220, 220));
        employeeTable.setShowHorizontalLines(true);
        employeeTable.setShowVerticalLines(true);
        
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204)));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton addNewButton = new JButton("Add New Employee");
        forceButtonStyle(addNewButton);
        addNewButton.addActionListener(e -> cardLayout.show(mainPanel, "employeeManagement"));
        
        buttonPanel.add(addNewButton);
        
        // Search action
        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().toLowerCase();
            filterEmployeeList(searchTerm, model);
        });
        
        // Assemble the panel
        employeeListPanel.add(headerPanel, BorderLayout.NORTH);
        employeeListPanel.add(scrollPane, BorderLayout.CENTER);
        employeeListPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    // Method to update the employee list table
    private void updateEmployeeListTable() {
        if (employeeListPanel != null) {
            JScrollPane scrollPane = (JScrollPane) employeeListPanel.getComponent(1);
            JTable table = (JTable) scrollPane.getViewport().getView();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            
            // Clear existing data
            model.setRowCount(0);
            
            // Add all employees to table
            if (employeeList != null) {
                for (Employee emp : employeeList) {
                    model.addRow(new Object[]{
                        emp.getEmployeeID(),
                        emp.getName(),
                        "N/A", // Birthday (add getter method)
                        emp.getPhoneNumber(),
                        "N/A", // Position (add getter method)
                        emp.getDepartment()
                    });
                }
            }
        }
    }

    // Method to filter employees based on search term
    private void filterEmployeeList(String searchTerm, DefaultTableModel model) {
        // Clear existing data
        model.setRowCount(0);
        
        // Add matching employees to table
        if (employeeList != null) {
            for (Employee emp : employeeList) {
                if (String.valueOf(emp.getEmployeeID()).contains(searchTerm) ||
                    emp.getName().toLowerCase().contains(searchTerm) ||
                    emp.getDepartment().toLowerCase().contains(searchTerm) ||
                    (emp.getPhoneNumber() != null && emp.getPhoneNumber().toLowerCase().contains(searchTerm))) {
                    
                    model.addRow(new Object[]{
                        emp.getEmployeeID(),
                        emp.getName(),
                        "N/A", // Birthday
                        emp.getPhoneNumber(),
                        "N/A", // Position
                        emp.getDepartment()
                    });
                }
            }
        }
    }
    
    private void createSampleAttendanceData() {
        Calendar cal = Calendar.getInstance();
        
        // Today's record
        attendanceRecords.add(new AttendanceRecord(
            cal.getTime(), 
            new Date(cal.getTimeInMillis() - 3600000), // 1 hour ago
            new Date(), 
            8.0));
        
        // Yesterday's record
        cal.add(Calendar.DAY_OF_MONTH, -1);
        attendanceRecords.add(new AttendanceRecord(
            cal.getTime(), 
            new Date(cal.getTimeInMillis() + 32400000), // 9:00 AM
            new Date(cal.getTimeInMillis() + 61200000), // 5:00 PM
            8.0));
        
        // Two days ago
        cal.add(Calendar.DAY_OF_MONTH, -1);
        attendanceRecords.add(new AttendanceRecord(
            cal.getTime(), 
            new Date(cal.getTimeInMillis() + 32400000), // 9:00 AM
            new Date(cal.getTimeInMillis() + 61200000), // 5:00 PM
            8.0));
        
        // Three days ago
        cal.add(Calendar.DAY_OF_MONTH, -1);
        attendanceRecords.add(new AttendanceRecord(
            cal.getTime(), 
            new Date(cal.getTimeInMillis() + 30600000), // 8:30 AM
            new Date(cal.getTimeInMillis() + 59400000), // 4:30 PM
            8.0));
    }
    
    private void createLoginPanel() {
        loginPanel = new JPanel();
        loginPanel.setLayout(new BorderLayout());
        loginPanel.setBackground(Color.WHITE);
        
        // Blue header
        JPanel headerPanel = new JPanel();
        styleHeaderPanel(headerPanel);
        headerPanel.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("MotorPH Employee Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Email field
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        // Login button
        JButton loginButton = new JButton("Login");
        forceButtonStyle(loginButton);
        loginButton.setPreferredSize(new Dimension(100, 40));
        
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
        gbc.insets = new Insets(20, 10, 10, 10);
        formPanel.add(loginButton, gbc);
        
        // Add company logo or image
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(Color.WHITE);
        JLabel logoLabel = new JLabel("MotorPH");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 36));
        logoLabel.setForeground(new Color(0, 102, 204));
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        logoPanel.add(logoLabel);
        
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
                
                // Record login attendance
                attendanceRecords.add(new AttendanceRecord(
                    new Date(),
                    new Date(),
                    null,
                    0.0
                ));
                
                // Show dashboard and sidebar
                cardLayout.show(mainPanel, "dashboard");
                
                // Make the sidebar visible after login
                Container parent = mainPanel.getParent();
                Component[] components = parent.getComponents();
                for (Component component : components) {
                    if (component instanceof JPanel && component != mainPanel) {
                        component.setVisible(true);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(loginPanel, 
                    "Invalid email or password", 
                    "Login Failed", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        loginPanel.add(headerPanel, BorderLayout.NORTH);
        loginPanel.add(formPanel, BorderLayout.CENTER);
        loginPanel.add(logoPanel, BorderLayout.SOUTH);
    }
    
    private void createDashboardPanel() {
        dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setBackground(Color.WHITE);
        
        // Header panel with blue background
        JPanel headerPanel = new JPanel(new BorderLayout());
        styleHeaderPanel(headerPanel);
        
        JLabel titleLabel = new JLabel("MotorPH Employee Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Main content panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
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
        
        // Current time
        gbc.gridy = 3;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        contentPanel.add(new JLabel("Current Login: " + sdf.format(new Date())), gbc);
        
        // Add panels to dashboard
        dashboardPanel.add(headerPanel, BorderLayout.NORTH);
        dashboardPanel.add(contentPanel, BorderLayout.CENTER);
    }
    
    private void createEmployeeInfoPanel() {
        employeeInfoPanel = new JPanel(new BorderLayout());
        employeeInfoPanel.setBackground(Color.WHITE);
        
        // Header with blue background
        JPanel headerPanel = new JPanel(new BorderLayout());
        styleHeaderPanel(headerPanel);
        
        JLabel titleLabel = new JLabel("Employee Information", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        stylePanel(formPanel);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        formPanel.add(new JLabel("Employee ID:"));
        JLabel idLabel = new JLabel(String.valueOf(employee.getEmployeeID()));
        formPanel.add(idLabel);
        
        formPanel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField(employee.getName());
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formPanel.add(nameField);
        
        formPanel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField(employee.getEmail());
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formPanel.add(emailField);
        
        formPanel.add(new JLabel("Department:"));
        JTextField deptField = new JTextField(employee.getDepartment());
        deptField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formPanel.add(deptField);
        
        formPanel.add(new JLabel("Phone:"));
        JTextField phoneField = new JTextField(employee.getPhoneNumber());
        phoneField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formPanel.add(phoneField);
        
        // Update button
        JButton updateButton = new JButton("Update Information");
        forceButtonStyle(updateButton);
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
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(updateButton);
        
        // Assemble the panel
        employeeInfoPanel.add(headerPanel, BorderLayout.NORTH);
        employeeInfoPanel.add(formPanel, BorderLayout.CENTER);
        employeeInfoPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void createPayrollPanel() {
        payrollPanel = new JPanel(new BorderLayout());
        payrollPanel.setBackground(Color.WHITE);
        
        // Header with blue background
        JPanel headerPanel = new JPanel(new BorderLayout());
        styleHeaderPanel(headerPanel);
        
        JLabel titleLabel = new JLabel("Payroll & Salary Calculation", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Create tabbed pane for different payroll functions
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Tab 1: Basic Payroll Info
        JPanel basicInfoPanel = createBasicPayrollPanel();
        tabbedPane.addTab("Basic Information", basicInfoPanel);
        
        // Tab 2: Salary Calculator
        JPanel calculatorPanel = createSalaryCalculatorPanel();
        tabbedPane.addTab("Salary Calculator", calculatorPanel);
        
        // Tab 3: Deductions
        JPanel deductionsPanel = createDeductionsPanel();
        tabbedPane.addTab("Deductions", deductionsPanel);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton generatePayslipButton = new JButton("Generate Payslip");
        forceButtonStyle(generatePayslipButton);
        generatePayslipButton.addActionListener(e -> {
            payroll.generatePayslip();
            JOptionPane.showMessageDialog(payrollPanel, 
                "Payslip generated successfully", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        buttonPanel.add(generatePayslipButton);
        
        // Add components to panel
        payrollPanel.add(headerPanel, BorderLayout.NORTH);
        payrollPanel.add(tabbedPane, BorderLayout.CENTER);
        payrollPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createBasicPayrollPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        panel.add(new JLabel("Employee ID:"));
        panel.add(new JLabel(String.valueOf(employee.getEmployeeID())));
        
        panel.add(new JLabel("Employee Name:"));
        panel.add(new JLabel(employee.getName()));
        
        panel.add(new JLabel("Gross Salary:"));
        panel.add(new JLabel("$" + String.format("%.2f", payroll.getSalary())));
        
        double netSalary = payroll.calculateNetSalary();
        panel.add(new JLabel("Net Salary:"));
        panel.add(new JLabel("$" + String.format("%.2f", netSalary)));
        
        panel.add(new JLabel("Tax Deduction:"));
        panel.add(new JLabel("$" + String.format("%.2f", payroll.getSalary() - netSalary)));
        
        return panel;
    }
    
    private JPanel createSalaryCalculatorPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        // Input fields
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBackground(Color.WHITE);
        
        inputPanel.add(new JLabel("Base Salary:"));
        JTextField baseSalaryField = new JTextField(String.valueOf(payroll.getSalary()));
        baseSalaryField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        inputPanel.add(baseSalaryField);
        
        inputPanel.add(new JLabel("Hours Worked:"));
        JTextField hoursWorkedField = new JTextField("160"); // Default monthly hours
        hoursWorkedField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        inputPanel.add(hoursWorkedField);
        
        inputPanel.add(new JLabel("Overtime Hours:"));
        JTextField overtimeHoursField = new JTextField("0");
        overtimeHoursField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        inputPanel.add(overtimeHoursField);
        
        inputPanel.add(new JLabel("Bonus:"));
        JTextField bonusField = new JTextField("0");
        bonusField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        inputPanel.add(bonusField);
        
        // Result fields (read-only)
        inputPanel.add(new JLabel("Calculated Gross:"));
        JTextField grossSalaryResult = new JTextField("0");
        grossSalaryResult.setEditable(false);
        grossSalaryResult.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        inputPanel.add(grossSalaryResult);
        
        inputPanel.add(new JLabel("Calculated Net:"));
        JTextField netSalaryResult = new JTextField("0");
        netSalaryResult.setEditable(false);
        netSalaryResult.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        inputPanel.add(netSalaryResult);
        
        panel.add(inputPanel, BorderLayout.CENTER);
        
        // Calculate button
        JButton calculateButton = new JButton("Calculate Salary");
        forceButtonStyle(calculateButton);
        calculateButton.addActionListener(e -> {
            try {
                double baseSalary = Double.parseDouble(baseSalaryField.getText());
                double hoursWorked = Double.parseDouble(hoursWorkedField.getText());
                double overtimeHours = Double.parseDouble(overtimeHoursField.getText());
                double bonus = Double.parseDouble(bonusField.getText());
                
                double hourlyRate = baseSalary / 160; // Assuming 160 hours per month
                double overtimePay = overtimeHours * hourlyRate * 1.5; // 1.5x for overtime
                
                double grossSalary = (hourlyRate * hoursWorked) + overtimePay + bonus;
                
                // Simplified tax calculation (15% tax)
                double taxRate = 0.15;
                double netSalary = grossSalary * (1 - taxRate);
                
                // Update fields
                grossSalaryResult.setText(String.format("%.2f", grossSalary));
                netSalaryResult.setText(String.format("%.2f", netSalary));
                
                // Update payroll object
                payroll.setSalary(grossSalary);
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, 
                    "Please enter valid numbers for all fields", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(calculateButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createDeductionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        // Deductions input
        JPanel deductionsPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        deductionsPanel.setBackground(Color.WHITE);
        
        deductionsPanel.add(new JLabel("Gross Salary:"));
        JTextField grossSalaryField = new JTextField(String.format("%.2f", payroll.getSalary()));
        grossSalaryField.setEditable(false);
        grossSalaryField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        deductionsPanel.add(grossSalaryField);
        
        deductionsPanel.add(new JLabel("Income Tax (15%):"));
        JTextField incomeTaxField = new JTextField(String.format("%.2f", payroll.getSalary() * 0.15));
        incomeTaxField.setEditable(false);
        incomeTaxField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        deductionsPanel.add(incomeTaxField);
        
        deductionsPanel.add(new JLabel("Social Security (2%):"));
        JTextField ssField = new JTextField(String.format("%.2f", payroll.getSalary() * 0.02));
        ssField.setEditable(false);
        ssField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        deductionsPanel.add(ssField);
        
        deductionsPanel.add(new JLabel("Health Insurance (3%):"));
        JTextField healthField = new JTextField(String.format("%.2f", payroll.getSalary() * 0.03));
        healthField.setEditable(false);
        healthField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        deductionsPanel.add(healthField);
        
        deductionsPanel.add(new JLabel("Retirement Fund (5%):"));
        JTextField retirementField = new JTextField(String.format("%.2f", payroll.getSalary() * 0.05));
        retirementField.setEditable(false);
        retirementField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        deductionsPanel.add(retirementField);
        
        deductionsPanel.add(new JLabel("Other Deductions:"));
        JTextField otherDeductionsField = new JTextField("0.00");
        otherDeductionsField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        deductionsPanel.add(otherDeductionsField);
        
        double totalDeductions = payroll.getSalary() * 0.25; // 15% + 2% + 3% + 5%
        deductionsPanel.add(new JLabel("Total Deductions:"));
        JTextField totalDeductionsField = new JTextField(String.format("%.2f", totalDeductions));
        totalDeductionsField.setEditable(false);
        totalDeductionsField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        deductionsPanel.add(totalDeductionsField);
        
        panel.add(deductionsPanel, BorderLayout.CENTER);
        
        // Calculate button
        JButton calculateButton = new JButton("Recalculate Deductions");
        forceButtonStyle(calculateButton);
        calculateButton.addActionListener(e -> {
            try {
                double otherDeductions = Double.parseDouble(otherDeductionsField.getText());
                double grossSalary = payroll.getSalary();
                
                // Calculate deductions
                double incomeTax = grossSalary * 0.15;
                double socialSecurity = grossSalary * 0.02;
                double healthInsurance = grossSalary * 0.03;
                double retirement = grossSalary * 0.05;
                double total = incomeTax + socialSecurity + healthInsurance + retirement + otherDeductions;
                
                // Update fields
                incomeTaxField.setText(String.format("%.2f", incomeTax));
                ssField.setText(String.format("%.2f", socialSecurity));
                healthField.setText(String.format("%.2f", healthInsurance));
                retirementField.setText(String.format("%.2f", retirement));
                totalDeductionsField.setText(String.format("%.2f", total));
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, 
                    "Please enter valid numbers for all fields", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(calculateButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void createLeaveRequestPanel() {
        leaveRequestPanel = new JPanel(new BorderLayout());
        leaveRequestPanel.setBackground(Color.WHITE);
        
        // Header with blue background
        JPanel headerPanel = new JPanel(new BorderLayout());
        styleHeaderPanel(headerPanel);
        
        JLabel titleLabel = new JLabel("Leave Request", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Leave request form
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);
        
        formPanel.add(new JLabel("Employee ID:"));
        formPanel.add(new JLabel(String.valueOf(employee.getEmployeeID())));
        
        formPanel.add(new JLabel("Leave Type:"));
        String[] leaveTypes = {"Vacation", "Sick", "Personal"};
        JComboBox<String> leaveTypeCombo = new JComboBox<>(leaveTypes);
        formPanel.add(leaveTypeCombo);
        
        formPanel.add(new JLabel("Start Date (MM/DD/YYYY):"));
        JTextField startDateField = new JTextField();
        startDateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formPanel.add(startDateField);
        
        formPanel.add(new JLabel("End Date (MM/DD/YYYY):"));
        JTextField endDateField = new JTextField();
        endDateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formPanel.add(endDateField);
        
        formPanel.add(new JLabel("Status:"));
        JLabel statusLabel = new JLabel("New Request");
        formPanel.add(statusLabel);
        
        // Submit button
        JButton submitButton = new JButton("Submit Request");
        forceButtonStyle(submitButton);
        submitButton.addActionListener(e -> {
            leaveRequest.setEmployeeID(employee.getEmployeeID());
            leaveRequest.setLeaveType((String) leaveTypeCombo.getSelectedItem());
            leaveRequest.setStartDate(startDateField.getText());
            leaveRequest.setEndDate(endDateField.getText());
            leaveRequest.setStatus("Pending");
            
            statusLabel.setText("Pending");
            
            JOptionPane.showMessageDialog(leaveRequestPanel, 
                "Leave request submitted successfully", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(submitButton);
        
        // Add components to panel
        leaveRequestPanel.add(headerPanel, BorderLayout.NORTH);
        leaveRequestPanel.add(formPanel, BorderLayout.CENTER);
        leaveRequestPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void createTaxFormPanel() {
        taxFormPanel = new JPanel(new BorderLayout());
        taxFormPanel.setBackground(Color.WHITE);
        
        // Header with blue background
        JPanel headerPanel = new JPanel(new BorderLayout());
        styleHeaderPanel(headerPanel);
        
        JLabel titleLabel = new JLabel("Tax Forms", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Tax form info
        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        infoPanel.setBackground(Color.WHITE);
        
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
        forceButtonStyle(downloadButton);
        downloadButton.addActionListener(e -> {
            taxForm.downloadForm();
            JOptionPane.showMessageDialog(taxFormPanel, 
                "Tax form downloaded successfully", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(downloadButton);
        
        // Add components to panel
        taxFormPanel.add(headerPanel, BorderLayout.NORTH);
        taxFormPanel.add(infoPanel, BorderLayout.CENTER);
        taxFormPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void createAttendancePanel() {
        attendancePanel = new JPanel(new BorderLayout());
        attendancePanel.setBackground(Color.WHITE);
        
        // Header with blue background
        JPanel headerPanel = new JPanel(new BorderLayout());
        styleHeaderPanel(headerPanel);
        
        JLabel titleLabel = new JLabel("Attendance Management", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Create tabbed pane for different attendance functions
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Tab 1: Clock In/Out
        JPanel clockPanel = createClockInOutPanel();
        tabbedPane.addTab("Clock In/Out", clockPanel);
        
        // Tab 2: Attendance Records
        JPanel recordsPanel = createAttendanceRecordsPanel();
        tabbedPane.addTab("Attendance Records", recordsPanel);
        
        // Tab 3: Attendance Summary
        JPanel summaryPanel = createAttendanceSummaryPanel();
        tabbedPane.addTab("Summary", summaryPanel);
        
        // Add components to panel
        attendancePanel.add(headerPanel, BorderLayout.NORTH);
        attendancePanel.add(tabbedPane, BorderLayout.CENTER);
    }

    // Tab 1: Clock In/Out Panel
    private JPanel createClockInOutPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Current status panel
        JPanel statusPanel = new JPanel(new GridBagLayout());
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setBorder(BorderFactory.createTitledBorder("Current Status"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Check current status
        boolean isClockedIn = false;
        AttendanceRecord todayRecord = null;
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        
        for (AttendanceRecord record : attendanceRecords) {
            Calendar recordDate = Calendar.getInstance();
            recordDate.setTime(record.getDate());
            recordDate.set(Calendar.HOUR_OF_DAY, 0);
            recordDate.set(Calendar.MINUTE, 0);
            recordDate.set(Calendar.SECOND, 0);
            recordDate.set(Calendar.MILLISECOND, 0);
            
            if (recordDate.getTimeInMillis() == today.getTimeInMillis()) {
                todayRecord = record;
                if (record.getLoginTime() != null && record.getLogoutTime() == null) {
                    isClockedIn = true;
                }
                break;
            }
        }
        
        // Current time display
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        
        gbc.gridx = 0; gbc.gridy = 0;
        statusPanel.add(new JLabel("Current Date:"), gbc);
        gbc.gridx = 1;
        statusPanel.add(new JLabel(dateFormat.format(new Date())), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        statusPanel.add(new JLabel("Current Time:"), gbc);
        gbc.gridx = 1;
        JLabel currentTimeLabel = new JLabel(timeFormat.format(new Date()));
        statusPanel.add(currentTimeLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        statusPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        JLabel statusLabel = new JLabel(isClockedIn ? "CLOCKED IN" : "CLOCKED OUT");
        statusLabel.setForeground(isClockedIn ? new Color(0, 150, 0) : new Color(200, 0, 0));
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusPanel.add(statusLabel, gbc);
        
        if (todayRecord != null && todayRecord.getLoginTime() != null) {
            gbc.gridx = 0; gbc.gridy = 3;
            statusPanel.add(new JLabel("Login Time:"), gbc);
            gbc.gridx = 1;
            statusPanel.add(new JLabel(timeFormat.format(todayRecord.getLoginTime())), gbc);
        }
        
        // Update time every second
        javax.swing.Timer timer = new javax.swing.Timer(1000, e -> {
            currentTimeLabel.setText(timeFormat.format(new Date()));
        });
        timer.start();
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton clockInButton = new JButton("CLOCK IN");
        forceButtonStyle(clockInButton);
        clockInButton.setPreferredSize(new Dimension(120, 40));
        clockInButton.setEnabled(!isClockedIn);
        
        JButton clockOutButton = new JButton("CLOCK OUT");
        forceButtonStyle(clockOutButton);
        clockOutButton.setPreferredSize(new Dimension(120, 40));
        clockOutButton.setEnabled(isClockedIn);
        
        clockInButton.addActionListener(e -> {
            // Add new attendance record
            AttendanceRecord newRecord = new AttendanceRecord(
                new Date(),
                new Date(),
                null,
                0.0
            );
            attendanceRecords.add(newRecord);
            
            statusLabel.setText("CLOCKED IN");
            statusLabel.setForeground(new Color(0, 150, 0));
            clockInButton.setEnabled(false);
            clockOutButton.setEnabled(true);
            
            JOptionPane.showMessageDialog(panel, 
                "Clock in successful at " + timeFormat.format(new Date()), 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        clockOutButton.addActionListener(e -> {
            // Find and update today's record
            for (AttendanceRecord record : attendanceRecords) {
                if (record.getLoginTime() != null && record.getLogoutTime() == null) {
                    record.setLogoutTime(new Date());
                    record.setHoursWorked(
                        (record.getLogoutTime().getTime() - record.getLoginTime().getTime()) / 3600000.0
                    );
                    break;
                }
            }
            
            statusLabel.setText("CLOCKED OUT");
            statusLabel.setForeground(new Color(200, 0, 0));
            clockInButton.setEnabled(true);
            clockOutButton.setEnabled(false);
            
            JOptionPane.showMessageDialog(panel, 
                "Clock out successful at " + timeFormat.format(new Date()), 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        buttonPanel.add(clockInButton);
        buttonPanel.add(clockOutButton);
        
        panel.add(statusPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    // Tab 2: Attendance Records Panel
    private JPanel createAttendanceRecordsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Create table model
        String[] columnNames = {"Date", "Login Time", "Logout Time", "Hours Worked", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        
        // Add attendance records to table
        SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeSdf = new SimpleDateFormat("HH:mm:ss");
        
        for (AttendanceRecord record : attendanceRecords) {
            String date = dateSdf.format(record.getDate());
            String loginTime = record.getLoginTime() != null ? timeSdf.format(record.getLoginTime()) : "N/A";
            String logoutTime = record.getLogoutTime() != null ? timeSdf.format(record.getLogoutTime()) : "Active";
            String hoursWorked = record.getLogoutTime() != null ? 
                String.format("%.2f", record.getHoursWorked()) : "In Progress";
            String status = record.getLogoutTime() != null ? "Complete" : "Active";
                
            model.addRow(new Object[]{date, loginTime, logoutTime, hoursWorked, status});
        }
        
        // Create styled table
        JTable attendanceTable = new JTable(model);
        attendanceTable.getTableHeader().setBackground(new Color(0, 102, 204));
        attendanceTable.getTableHeader().setForeground(Color.WHITE);
        attendanceTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        attendanceTable.setRowHeight(25);
        attendanceTable.setGridColor(new Color(220, 220, 220));
        attendanceTable.setShowHorizontalLines(true);
        attendanceTable.setShowVerticalLines(true);
        
        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204)));
        
        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter Records"));
        
        filterPanel.add(new JLabel("Show:"));
        String[] filterOptions = {"All Records", "This Week", "This Month", "Active Sessions"};
        JComboBox<String> filterCombo = new JComboBox<>(filterOptions);
        filterPanel.add(filterCombo);
        
        JButton refreshButton = new JButton("Refresh");
        forceButtonStyle(refreshButton);
        filterPanel.add(refreshButton);
        
        refreshButton.addActionListener(e -> {
            // Refresh the table data
            model.setRowCount(0);
            for (AttendanceRecord record : attendanceRecords) {
                String date = dateSdf.format(record.getDate());
                String loginTime = record.getLoginTime() != null ? timeSdf.format(record.getLoginTime()) : "N/A";
                String logoutTime = record.getLogoutTime() != null ? timeSdf.format(record.getLogoutTime()) : "Active";
                String hoursWorked = record.getLogoutTime() != null ? 
                    String.format("%.2f", record.getHoursWorked()) : "In Progress";
                String status = record.getLogoutTime() != null ? "Complete" : "Active";
                    
                model.addRow(new Object[]{date, loginTime, logoutTime, hoursWorked, status});
            }
        });
        
        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    // Tab 3: Attendance Summary Panel
    private JPanel createAttendanceSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Summary statistics
        JPanel statsPanel = new JPanel(new GridLayout(3, 2, 20, 10));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Attendance Statistics"));
        statsPanel.setBackground(Color.WHITE);
        
        // Calculate statistics
        int totalDays = attendanceRecords.size();
        double totalHours = 0;
        int completeDays = 0;
        
        for (AttendanceRecord record : attendanceRecords) {
            if (record.getLogoutTime() != null) {
                totalHours += record.getHoursWorked();
                completeDays++;
            }
        }
        
        double averageHours = completeDays > 0 ? totalHours / completeDays : 0;
        
        statsPanel.add(new JLabel("Total Attendance Days:"));
        statsPanel.add(new JLabel(String.valueOf(totalDays)));
        
        statsPanel.add(new JLabel("Complete Days:"));
        statsPanel.add(new JLabel(String.valueOf(completeDays)));
        
        statsPanel.add(new JLabel("Total Hours Worked:"));
        statsPanel.add(new JLabel(String.format("%.2f hours", totalHours)));
        
        statsPanel.add(new JLabel("Average Hours/Day:"));
        statsPanel.add(new JLabel(String.format("%.2f hours", averageHours)));
        
        statsPanel.add(new JLabel("This Month Hours:"));
        statsPanel.add(new JLabel(String.format("%.2f hours", totalHours))); // Simplified
        
        statsPanel.add(new JLabel("Attendance Rate:"));
        double attendanceRate = totalDays > 0 ? (completeDays * 100.0 / totalDays) : 0;
        statsPanel.add(new JLabel(String.format("%.1f%%", attendanceRate)));
        
        // Chart placeholder (you could add a simple bar chart here)
        JPanel chartPanel = new JPanel();
        chartPanel.setBorder(BorderFactory.createTitledBorder("Hours Chart"));
        chartPanel.setBackground(Color.WHITE);
        chartPanel.add(new JLabel("Weekly hours chart would go here"));
        
        // Export button
        JButton exportButton = new JButton("Export Attendance Report");
        forceButtonStyle(exportButton);
        exportButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(panel, 
                "Attendance report exported successfully", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(exportButton);
        
        panel.add(statsPanel, BorderLayout.NORTH);
        panel.add(chartPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Helper class for attendance records
    private static class AttendanceRecord {
        private final Date date;
        private final Date loginTime;
        private Date logoutTime;
        private double hoursWorked;
        
        public AttendanceRecord(Date date, Date loginTime, Date logoutTime, double hoursWorked) {
            this.date = date;
            this.loginTime = loginTime;
            this.logoutTime = logoutTime;
            this.hoursWorked = hoursWorked;
        }
        
        public Date getDate() { return date; }
        public Date getLoginTime() { return loginTime; }
        public Date getLogoutTime() { return logoutTime; }
        public double getHoursWorked() { return hoursWorked; }
        
        public void setLogoutTime(Date logoutTime) { this.logoutTime = logoutTime; }
        public void setHoursWorked(double hoursWorked) { this.hoursWorked = hoursWorked; }
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        button.setOpaque(true);  // This is crucial - makes sure background is painted
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);  // Ensure content area is filled with background color
        
        // More visible border
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 80, 170), 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect with custom listener
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 120, 230));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 102, 204));
            }
        });
    }

    private void forceButtonStyle(JButton button) {
        // This will override the Look and Feel
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        styleButton(button);
    }

    private void styleMenuButton(JButton button) {
        styleButton(button);
        button.setPreferredSize(new Dimension(200, 40));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
    }

    private void stylePanel(JPanel panel) {
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void styleHeaderPanel(JPanel panel) {
        panel.setBackground(new Color(0, 102, 204));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
}
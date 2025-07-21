package Term2;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
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
    private JButton themeToggleButton;
    private ArrayList<Employee> employeeList; // To store employees
    private JLabel statusLabel; // Status bar for user feedback
        
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

        // Initialize employeeList BEFORE creating panels
        employeeList = new ArrayList<>(); // Initialize as empty list first
        
        createSampleAttendanceData();
        
        // Setup demo account
        login.setEmail("admin@motorph.com");
        login.setPassword("MotorPH2025");
        
        // Configure main frame
        setTitle("MotorPH Employee App");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Add keyboard shortcuts
        setupKeyboardShortcuts();

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

        // Initialize CSV file and load employees AFTER creating panels
        try {
            CSVManager.initializeCSVFile();
            employeeList = CSVManager.loadEmployeesFromCSV();
            if (employeeList == null) {
                employeeList = new ArrayList<>(); // Fallback if CSV loading fails
            }
        } catch (Exception e) {
            System.err.println("Error loading employees: " + e.getMessage());
            employeeList = new ArrayList<>(); // Fallback to empty list
        }
        
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
        
        // Create and add status bar
        statusLabel = new JLabel("Ready - MotorPH Employee Management System");
        statusLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLoweredBevelBorder(),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        mainContentPanel.add(statusLabel, BorderLayout.SOUTH);
        
        add(mainContentPanel);
    }

    // Setup keyboard shortcuts for improved accessibility
    private void setupKeyboardShortcuts() {
        // Create input and action maps for the main frame
        JRootPane rootPane = getRootPane();
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = rootPane.getActionMap();
        
        // Ctrl+T for theme toggle
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK), "toggleTheme");
        actionMap.put("toggleTheme", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (themeToggleButton != null) {
                    themeToggleButton.doClick();
                }
            }
        });
        
        // Ctrl+N for new employee
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK), "newEmployee");
        actionMap.put("newEmployee", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "employeeManagement");
                // Simulate clicking the new employee button
                SwingUtilities.invokeLater(() -> openNewEmployeeFrame());
            }
        });
        
        // Ctrl+E for employee management
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK), "employeeManagement");
        actionMap.put("employeeManagement", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "employeeManagement");
            }
        });
        
        // Ctrl+P for payroll
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK), "payroll");
        actionMap.put("payroll", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "payroll");
            }
        });
        
        // Ctrl+A for attendance
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK), "attendance");
        actionMap.put("attendance", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "attendance");
            }
        });
    }

    // Method to create the enhanced sidebar with modern design
    private JPanel createSidebarPanel() {
        // Create menu panel (sidebar) with enhanced styling
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(ThemeManager.getAccentColor());
        menuPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 3, new Color(255, 255, 255, 50)),
            BorderFactory.createEmptyBorder(25, 15, 25, 15)
        ));
        menuPanel.setPreferredSize(new Dimension(250, 600));
        
        // Enhanced company logo section
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBackground(ThemeManager.getAccentColor());
        logoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        
        JLabel logoLabel = new JLabel("🏢 MotorPH");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subLabel = new JLabel("Employee Management System");
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subLabel.setForeground(new Color(255, 255, 255, 180));
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        logoPanel.add(logoLabel);
        logoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        logoPanel.add(subLabel);
        menuPanel.add(logoPanel);
        
        // Enhanced theme toggle button
        themeToggleButton = new JButton(ThemeManager.isDarkMode() ? "☀️ Light Mode" : "🌙 Dark Mode");
        themeToggleButton.setToolTipText("Toggle between light and dark themes (Ctrl+T)");
        themeToggleButton.getAccessibleContext().setAccessibleName("Theme Toggle");
        themeToggleButton.getAccessibleContext().setAccessibleDescription("Switch between light and dark mode themes");
        enhanceMenuButton(themeToggleButton);
        themeToggleButton.setBackground(new Color(255, 255, 255, 20));
        themeToggleButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        themeToggleButton.addActionListener(e -> {
            ThemeManager.toggleTheme(this);
            updateThemeToggleButton();
            updateAllButtonStyles();
            ThemeManager.forceTextColorUpdate(this);
            updateStatus("Theme switched to " + (ThemeManager.isDarkMode() ? "Dark Mode" : "Light Mode"));
        });
        menuPanel.add(themeToggleButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Create enhanced menu buttons with icons
        JButton employeeManagementButton = createEnhancedMenuButton("👥 Employee Management", "Manage employee records and information (Ctrl+E)");
        JButton employeeInfoButton = createEnhancedMenuButton("👤 Employee Information", "View and edit personal information");
        JButton payrollButton = createEnhancedMenuButton("💰 Payroll & Calculations", "Calculate salaries and deductions (Ctrl+P)");
        JButton leaveRequestButton = createEnhancedMenuButton("📅 Leave Requests", "Submit and manage leave requests");
        JButton taxFormButton = createEnhancedMenuButton("📄 Tax Forms", "Generate and download tax documents");
        JButton attendanceButton = createEnhancedMenuButton("⏰ Attendance Records", "Track time and attendance (Ctrl+A)");
        
        // Add buttons with enhanced spacing
        addMenuButton(menuPanel, employeeInfoButton);
        addMenuButton(menuPanel, payrollButton);
        addMenuButton(menuPanel, leaveRequestButton);
        addMenuButton(menuPanel, taxFormButton);
        addMenuButton(menuPanel, attendanceButton);
        addMenuButton(menuPanel, employeeManagementButton);
        
        // Add flexible space
        menuPanel.add(Box.createVerticalGlue());
        
        // Enhanced logout section
        JPanel logoutPanel = new JPanel(new BorderLayout());
        logoutPanel.setBackground(ThemeManager.getAccentColor());
        logoutPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JButton logoutButton = createEnhancedMenuButton("🚪 Logout", "Sign out of the application");
        logoutButton.setBackground(new Color(220, 53, 69, 180));
        logoutPanel.add(logoutButton, BorderLayout.CENTER);
        menuPanel.add(logoutPanel);
        
        // Add button actions (existing code)
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
        
        return menuPanel;
    }

    // Helper method to create enhanced menu buttons with tooltips and accessibility
    private JButton createEnhancedMenuButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setToolTipText(tooltip);
        
        // Add accessibility name for screen readers
        button.getAccessibleContext().setAccessibleName(text.replaceAll("[^a-zA-Z\\s]", ""));
        button.getAccessibleContext().setAccessibleDescription(tooltip);
        
        enhanceMenuButton(button);
        return button;
    }
    
    // Enhanced menu button styling
    private void enhanceMenuButton(JButton button) {
        button.setBackground(new Color(255, 255, 255, 0));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(220, 45));
        button.setPreferredSize(new Dimension(220, 45));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 0), 1, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Enhanced hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 255, 255, 30));
                button.setOpaque(true);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 1, true),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 255, 255, 0));
                button.setOpaque(false);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 255, 255, 0), 1, true),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        });
    }
    
    // Helper method to add menu buttons with consistent spacing
    private void addMenuButton(JPanel menuPanel, JButton button) {
        menuPanel.add(button);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 8)));
    }
    
    private void updateThemeToggleButton() {
        if (themeToggleButton != null) {
            themeToggleButton.setText(ThemeManager.isDarkMode() ? "☀️ Light Mode" : "🌙 Dark Mode");
        }
    }

    private void updateAllButtonStyles() {
        // Update all buttons in the application
        updateButtonStylesRecursively(this);
        // Force repaint to ensure changes are visible
        repaint();
    }

    private void updateButtonStylesRecursively(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (!button.equals(themeToggleButton)) { // Don't update the theme toggle button
                    // Reapply button styling to match current theme
                    styleButton(button);
                }
            }
            if (component instanceof Container) {
                updateButtonStylesRecursively((Container) component);
            }
        }
    }

    private void createEmployeeManagementPanel() {
        employeeManagementPanel = new JPanel(new BorderLayout());
        employeeManagementPanel.setBackground(ThemeManager.getBackgroundColor());
        
        // Header with blue background
        JPanel headerPanel = new JPanel(new BorderLayout());
        styleHeaderPanel(headerPanel);
        
        JLabel titleLabel = new JLabel("EMPLOYEE MANAGEMENT", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Main content with split layout
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(ThemeManager.getBackgroundColor());
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // LEFT SIDE: Employee table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(ThemeManager.getBackgroundColor());
        
        // Employee table
        String[] columnNames = {"Employee #", "Last Name", "First Name", "SSS Number", "PhilHealth #", "TIN", "Pag-IBIG #"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        JTable employeeTable = new JTable(tableModel);
        employeeTable.getTableHeader().setBackground(new Color(0, 102, 204));
        employeeTable.getTableHeader().setForeground(Color.WHITE);
        employeeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        employeeTable.setRowHeight(25);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.setGridColor(new Color(220, 220, 220));
        
        // Load employee data into table
        refreshEmployeeTable(tableModel);
        
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("All Employees"));
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        // Search panel for table
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(ThemeManager.getBackgroundColor());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));
        
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        forceButtonStyle(searchButton);
        
        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().toLowerCase().trim();
            filterEmployeeTable(tableModel, searchTerm);
        });
        
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // RIGHT SIDE: Employee Details Form
        JPanel detailsPanel = createEmployeeDetailsPanel();
        
        // Split pane to divide table and details
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tablePanel, detailsPanel);
        splitPane.setDividerLocation(520);
        splitPane.setResizeWeight(0.6);
        
        mainContentPanel.add(splitPane, BorderLayout.CENTER);
        
        // Bottom button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(ThemeManager.getBackgroundColor());
        
        JButton viewEmployeeButton = new JButton("View Details");
        JButton newEmployeeButton = new JButton("New Employee");
        JButton refreshButton = new JButton("Refresh Table");
        
        forceButtonStyle(viewEmployeeButton);
        forceButtonStyle(newEmployeeButton);
        forceButtonStyle(refreshButton);
        
        viewEmployeeButton.addActionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow >= 0) {
                Employee selectedEmployee = employeeList.get(selectedRow);
                openEmployeeDetailFrame(selectedEmployee);
            } else {
                JOptionPane.showMessageDialog(employeeManagementPanel,
                    "Please select an employee from the table",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        newEmployeeButton.addActionListener(e -> openNewEmployeeFrame());
        
        refreshButton.addActionListener(e -> {
            employeeList = CSVManager.loadEmployeesFromCSV();
            refreshEmployeeTable(tableModel);
            clearEmployeeDetailsForm();
        });
        
        buttonPanel.add(viewEmployeeButton);
        buttonPanel.add(newEmployeeButton);
        buttonPanel.add(refreshButton);
        
        // Add table selection listener to populate form
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = employeeTable.getSelectedRow();
                if (selectedRow >= 0 && selectedRow < employeeList.size()) {
                    populateEmployeeDetailsForm(employeeList.get(selectedRow));
                    enableEditDeleteButtons(true);
                } else {
                    clearEmployeeDetailsForm();
                    enableEditDeleteButtons(false);
                }
            }
        });
        
        // Assemble the panel
        employeeManagementPanel.add(headerPanel, BorderLayout.NORTH);
        employeeManagementPanel.add(mainContentPanel, BorderLayout.CENTER);
        employeeManagementPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    // Create the employee details form panel
    private JPanel detailsPanel;
    private JTextField detailEmpIdField, detailLastNameField, detailFirstNameField, 
                    detailBirthdayField, detailAddressField, detailPhoneField, detailSssField, 
                    detailPhilHealthField, detailTinField, detailPagIbigField, 
                    detailPositionField, detailSalaryField, detailSupervisorField;
    private JComboBox<String> detailStatusCombo;
    // Legacy fields for compatibility
    private JTextField detailEmailField;
    private JComboBox<String> detailDeptCombo;
    private JButton updateButton, deleteButton, clearButton;
    private Employee currentSelectedEmployee = null;

    private JPanel createEmployeeDetailsPanel() {
        detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(ThemeManager.getBackgroundColor());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Employee Details"));
        detailsPanel.setPreferredSize(new Dimension(350, 400));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(16, 2, 5, 8));
        formPanel.setBackground(ThemeManager.getBackgroundColor());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Initialize form fields
        detailEmpIdField = new JTextField();
        detailLastNameField = new JTextField();
        detailFirstNameField = new JTextField();
        detailBirthdayField = new JTextField();
        detailAddressField = new JTextField();
        detailPhoneField = new JTextField();
        detailSssField = new JTextField();
        detailPhilHealthField = new JTextField();
        detailTinField = new JTextField();
        detailPagIbigField = new JTextField();
        detailStatusCombo = new JComboBox<>(new String[]{"Regular", "Probationary", "Contractual"});
        detailPositionField = new JTextField();
        detailSupervisorField = new JTextField();
        detailSalaryField = new JTextField();
        
        // Legacy fields for compatibility
        detailEmailField = new JTextField();
        detailDeptCombo = new JComboBox<>(new String[]{"Executive", "IT", "HR", "Finance", "Operations", "Marketing", "Sales", "Accounting", "Customer Service", "General"});
        
        // Make Employee ID read-only for editing
        detailEmpIdField.setEditable(false);
        detailEmpIdField.setBackground(new Color(240, 240, 240));
        
        // Style all fields
        styleTextField(detailLastNameField);
        styleTextField(detailFirstNameField);
        styleTextField(detailBirthdayField);
        styleTextField(detailAddressField);
        styleTextField(detailPhoneField);
        styleTextField(detailSssField);
        styleTextField(detailPhilHealthField);
        styleTextField(detailTinField);
        styleTextField(detailPagIbigField);
        styleTextField(detailPositionField);
        styleTextField(detailSupervisorField);
        styleTextField(detailSalaryField);
        styleTextField(detailEmailField);
        
        // Add fields to form
        formPanel.add(new JLabel("Employee ID:"));
        formPanel.add(detailEmpIdField);
        formPanel.add(new JLabel("Last Name:"));
        formPanel.add(detailLastNameField);
        formPanel.add(new JLabel("First Name:"));
        formPanel.add(detailFirstNameField);
        formPanel.add(new JLabel("Birthday:"));
        formPanel.add(detailBirthdayField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(detailAddressField);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(detailPhoneField);
        formPanel.add(new JLabel("SSS Number:"));
        formPanel.add(detailSssField);
        formPanel.add(new JLabel("PhilHealth:"));
        formPanel.add(detailPhilHealthField);
        formPanel.add(new JLabel("TIN:"));
        formPanel.add(detailTinField);
        formPanel.add(new JLabel("Pag-IBIG:"));
        formPanel.add(detailPagIbigField);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(detailStatusCombo);
        formPanel.add(new JLabel("Position:"));
        formPanel.add(detailPositionField);
        formPanel.add(new JLabel("Supervisor:"));
        formPanel.add(detailSupervisorField);
        formPanel.add(new JLabel("Basic Salary:"));
        formPanel.add(detailSalaryField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(detailEmailField);
        formPanel.add(new JLabel("Department:"));
        formPanel.add(detailDeptCombo);
        
        // Button panel for form
        JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        formButtonPanel.setBackground(ThemeManager.getBackgroundColor());
        
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");
        
        forceButtonStyle(updateButton);
        forceButtonStyle(deleteButton);
        forceButtonStyle(clearButton);
        
        // Initially disable update and delete buttons
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        
        // Make delete button red
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                deleteButton.setBackground(new Color(200, 35, 51));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                deleteButton.setBackground(new Color(220, 53, 69));
            }
        });
        
        // Add button actions
        updateButton.addActionListener(e -> updateSelectedEmployee());
        deleteButton.addActionListener(e -> deleteSelectedEmployee());
        clearButton.addActionListener(e -> clearEmployeeDetailsForm());
        
        formButtonPanel.add(updateButton);
        formButtonPanel.add(deleteButton);
        formButtonPanel.add(clearButton);
        
        detailsPanel.add(formPanel, BorderLayout.CENTER);
        detailsPanel.add(formButtonPanel, BorderLayout.SOUTH);
        
        return detailsPanel;
    }

    // Method to populate the form with selected employee data
    private void populateEmployeeDetailsForm(Employee employee) {
        currentSelectedEmployee = employee;
        
        detailEmpIdField.setText(String.valueOf(employee.getEmployeeID()));
        detailLastNameField.setText(employee.getLastName());
        detailFirstNameField.setText(employee.getFirstName());
        detailBirthdayField.setText(employee.getBirthday());
        detailAddressField.setText(employee.getAddress());
        detailPhoneField.setText(employee.getPhoneNumber());
        detailSssField.setText(employee.getSssNumber());
        detailPhilHealthField.setText(employee.getPhilHealthNumber());
        detailTinField.setText(employee.getTinNumber());
        detailPagIbigField.setText(employee.getPagIbigNumber());
        detailStatusCombo.setSelectedItem(employee.getStatus());
        detailPositionField.setText(employee.getPosition());
        detailSupervisorField.setText(employee.getImmediateSupervisor());
        detailSalaryField.setText(String.valueOf(employee.getBasicSalary()));
        
        // Legacy fields for compatibility
        detailEmailField.setText(employee.getEmail());
        detailDeptCombo.setSelectedItem(employee.getDepartment());
    }

    // Method to clear the form
    private void clearEmployeeDetailsForm() {
        currentSelectedEmployee = null;
        
        detailEmpIdField.setText("");
        detailLastNameField.setText("");
        detailFirstNameField.setText("");
        detailBirthdayField.setText("");
        detailAddressField.setText("");
        detailPhoneField.setText("");
        detailSssField.setText("");
        detailPhilHealthField.setText("");
        detailTinField.setText("");
        detailPagIbigField.setText("");
        detailStatusCombo.setSelectedIndex(0);
        detailPositionField.setText("");
        detailSupervisorField.setText("");
        detailSalaryField.setText("");
        
        // Legacy fields
        detailEmailField.setText("");
        detailDeptCombo.setSelectedIndex(0);
    }

    // Method to enable/disable edit and delete buttons
    private void enableEditDeleteButtons(boolean enabled) {
        updateButton.setEnabled(enabled);
        deleteButton.setEnabled(enabled);
    }

    // Method to update selected employee
    private void updateSelectedEmployee() {
        if (currentSelectedEmployee == null) {
            JOptionPane.showMessageDialog(employeeManagementPanel,
                "No employee selected for update",
                "Update Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Validate required fields
            if (detailLastNameField.getText().trim().isEmpty() ||
                detailFirstNameField.getText().trim().isEmpty() ||
                detailEmailField.getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(employeeManagementPanel,
                    "Please fill in all required fields (Names, Email)",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validate salary
            double salary;
            try {
                salary = Double.parseDouble(detailSalaryField.getText().trim());
                if (salary < 0) {
                    throw new NumberFormatException("Salary cannot be negative");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(employeeManagementPanel,
                    "Please enter a valid salary amount",
                    "Invalid Salary",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Confirm update
            int confirm = JOptionPane.showConfirmDialog(employeeManagementPanel,
                "Are you sure you want to update employee: " + currentSelectedEmployee.getName() + "?",
                "Confirm Update",
                JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                // Update employee object with new CSV fields
                currentSelectedEmployee.setLastName(detailLastNameField.getText().trim());
                currentSelectedEmployee.setFirstName(detailFirstNameField.getText().trim());
                currentSelectedEmployee.setBirthday(detailBirthdayField.getText().trim());
                currentSelectedEmployee.setAddress(detailAddressField.getText().trim());
                currentSelectedEmployee.setPhoneNumber(detailPhoneField.getText().trim());
                currentSelectedEmployee.setSssNumber(detailSssField.getText().trim());
                currentSelectedEmployee.setPhilHealthNumber(detailPhilHealthField.getText().trim());
                currentSelectedEmployee.setTinNumber(detailTinField.getText().trim());
                currentSelectedEmployee.setPagIbigNumber(detailPagIbigField.getText().trim());
                currentSelectedEmployee.setStatus((String) detailStatusCombo.getSelectedItem());
                currentSelectedEmployee.setPosition(detailPositionField.getText().trim());
                currentSelectedEmployee.setImmediateSupervisor(detailSupervisorField.getText().trim());
                currentSelectedEmployee.setBasicSalary(salary);
                
                // Update legacy fields for compatibility
                currentSelectedEmployee.setEmail(detailEmailField.getText().trim());
                currentSelectedEmployee.setDepartment((String) detailDeptCombo.getSelectedItem());
                
                // Save to CSV
                CSVManager.saveEmployeesToCSV(employeeList);
                
                // Refresh table
                refreshEmployeeManagementTable();
                
                JOptionPane.showMessageDialog(employeeManagementPanel,
                    "Employee updated successfully!",
                    "Update Success",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(employeeManagementPanel,
                "Error updating employee: " + e.getMessage(),
                "Update Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to delete selected employee
    private void deleteSelectedEmployee() {
        if (currentSelectedEmployee == null) {
            JOptionPane.showMessageDialog(employeeManagementPanel,
                "No employee selected for deletion",
                "Delete Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Confirm deletion with warning
        int confirm = JOptionPane.showConfirmDialog(employeeManagementPanel,
            "⚠️ WARNING: This will permanently delete employee:\n\n" +
            "ID: " + currentSelectedEmployee.getEmployeeID() + "\n" +
            "Name: " + currentSelectedEmployee.getName() + "\n" +
            "Department: " + currentSelectedEmployee.getDepartment() + "\n\n" +
            "This action cannot be undone. Are you sure?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Remove from list
                employeeList.remove(currentSelectedEmployee);
                
                // Save updated list to CSV
                CSVManager.saveEmployeesToCSV(employeeList);
                
                // Clear form and refresh table
                clearEmployeeDetailsForm();
                enableEditDeleteButtons(false);
                refreshEmployeeManagementTable();
                
                JOptionPane.showMessageDialog(employeeManagementPanel,
                    "Employee deleted successfully!",
                    "Delete Success",
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception e) {
                JOptionPane.showMessageDialog(employeeManagementPanel,
                    "Error deleting employee: " + e.getMessage(),
                    "Delete Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Add helper methods
    private void refreshEmployeeTable(DefaultTableModel model) {
        model.setRowCount(0);
        
        // Add null check for employeeList
        if (employeeList != null) {
            for (Employee emp : employeeList) {
                model.addRow(new Object[]{
                    emp.getEmployeeID(),
                    emp.getLastName(),
                    emp.getFirstName(),
                    emp.getSssNumber(),
                    emp.getPhilHealthNumber(),
                    emp.getTinNumber(),
                    emp.getPagIbigNumber()
                });
            }
        }
    }

    private void filterEmployeeTable(DefaultTableModel model, String searchTerm) {
        model.setRowCount(0);
        
        // Add null check for employeeList
        if (employeeList != null) {
            for (Employee emp : employeeList) {
                if (searchTerm.isEmpty() ||
                    String.valueOf(emp.getEmployeeID()).contains(searchTerm) ||
                    emp.getLastName().toLowerCase().contains(searchTerm) ||
                    emp.getFirstName().toLowerCase().contains(searchTerm) ||
                    emp.getDepartment().toLowerCase().contains(searchTerm)) {
                    
                    model.addRow(new Object[]{
                        emp.getEmployeeID(),
                        emp.getLastName(),
                        emp.getFirstName(),
                        emp.getSssNumber(),
                        emp.getPhilHealthNumber(),
                        emp.getTinNumber(),
                        emp.getPagIbigNumber()
                    });
                }
            }
        }
    }

    // Employee Detail Frame
    private void openEmployeeDetailFrame(Employee employee) {
        JFrame detailFrame = new JFrame("Employee Details - " + employee.getName());
        detailFrame.setSize(800, 600);
        detailFrame.setLocationRelativeTo(this);
        detailFrame.setLayout(new BorderLayout());
        
        // Apply theme to the frame
        detailFrame.getContentPane().setBackground(ThemeManager.getBackgroundColor());
        
        // Header
        JPanel headerPanel = new JPanel();
        styleHeaderPanel(headerPanel);
        JLabel titleLabel = new JLabel("Employee Details & Salary Computation", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(ThemeManager.getBackgroundColor());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Employee details panel
        JPanel detailsPanel = new JPanel(new GridLayout(7, 4, 10, 10));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Employee Information"));
        detailsPanel.setBackground(ThemeManager.getBackgroundColor());
        
        // Create and style all labels with theme colors
        JLabel[] fieldLabels = {
            new JLabel("Employee ID:"), new JLabel("Department:"),
            new JLabel("Last Name:"), new JLabel("First Name:"),
            new JLabel("Email:"), new JLabel("Phone:"),
            new JLabel("Position:"), new JLabel("Birthday:"),
            new JLabel("SSS Number:"), new JLabel("PhilHealth:"),
            new JLabel("TIN:"), new JLabel("Pag-IBIG:"),
            new JLabel("Basic Salary:"), new JLabel("")
        };
        
        JLabel[] valueLabels = {
            new JLabel(String.valueOf(employee.getEmployeeID())),
            new JLabel(employee.getDepartment()),
            new JLabel(employee.getLastName()),
            new JLabel(employee.getFirstName()),
            new JLabel(employee.getEmail()),
            new JLabel(employee.getPhoneNumber()),
            new JLabel(employee.getPosition()),
            new JLabel(employee.getBirthday()),
            new JLabel(employee.getSssNumber()),
            new JLabel(employee.getPhilHealthNumber()),
            new JLabel(employee.getTinNumber()),
            new JLabel(employee.getPagIbigNumber()),
            new JLabel("$" + String.format("%.2f", employee.getBasicSalary())),
            new JLabel("")
        };
        
        // Apply theme to all labels
        for (JLabel label : fieldLabels) {
            label.setForeground(ThemeManager.getForegroundColor());
        }
        for (JLabel label : valueLabels) {
            label.setForeground(ThemeManager.getForegroundColor());
        }
        
        // Add labels to panel in pairs
        for (int i = 0; i < fieldLabels.length; i++) {
            detailsPanel.add(fieldLabels[i]);
            detailsPanel.add(valueLabels[i]);
        }
        detailsPanel.add(new JLabel(""));
        detailsPanel.add(new JLabel(""));
        
        // Salary computation panel
        JPanel salaryPanel = createSalaryComputationPanel(employee);
        
        contentPanel.add(detailsPanel, BorderLayout.NORTH);
        contentPanel.add(salaryPanel, BorderLayout.CENTER);
        
        detailFrame.add(headerPanel, BorderLayout.NORTH);
        detailFrame.add(contentPanel, BorderLayout.CENTER);
        
        // Apply theme to the entire frame
        ThemeManager.applyTheme(detailFrame);
        
        detailFrame.setVisible(true);
    }

    // Salary computation panel for employee detail frame
    private JPanel createSalaryComputationPanel(Employee employee) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Salary Computation"));
        panel.setBackground(ThemeManager.getBackgroundColor());
        
        // Month selection
        JPanel monthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        monthPanel.setBackground(ThemeManager.getBackgroundColor());
        
        JLabel monthLabel = new JLabel("Select Month:");
        monthLabel.setForeground(ThemeManager.getForegroundColor());
        monthPanel.add(monthLabel);
        String[] months = {"January", "February", "March", "April", "May", "June",
                        "July", "August", "September", "October", "November", "December"};
        JComboBox<String> monthCombo = new JComboBox<>(months);
        monthCombo.setBackground(ThemeManager.getBackgroundColor());
        monthCombo.setForeground(ThemeManager.getForegroundColor());
        monthPanel.add(monthCombo);
        
        JButton computeButton = new JButton("Compute Salary");
        forceButtonStyle(computeButton);
        monthPanel.add(computeButton);
        
        // Results panel
        JPanel resultsPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        resultsPanel.setBackground(ThemeManager.getBackgroundColor());
        resultsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Result labels with proper theming
        JLabel baseSalaryLabel = new JLabel("$0.00");
        JLabel allowanceLabel = new JLabel("$0.00");
        JLabel overtimeLabel = new JLabel("$0.00");
        JLabel grossLabel = new JLabel("$0.00");
        JLabel taxLabel = new JLabel("$0.00");
        JLabel sssLabel = new JLabel("$0.00");
        JLabel philHealthLabel = new JLabel("$0.00");
        JLabel netLabel = new JLabel("$0.00");
        
        // Style result labels
        JLabel[] resultLabels = {baseSalaryLabel, allowanceLabel, overtimeLabel, grossLabel,
                               taxLabel, sssLabel, philHealthLabel, netLabel};
        for (JLabel label : resultLabels) {
            label.setForeground(ThemeManager.getForegroundColor());
        }
        
        // Create and style field labels
        JLabel[] fieldLabels = {
            new JLabel("Base Salary:"), new JLabel("Allowance:"), new JLabel("Overtime Pay:"),
            new JLabel("Gross Salary:"), new JLabel("Income Tax (15%):"), new JLabel("SSS (2%):"),
            new JLabel("PhilHealth (3%):"), new JLabel("Net Salary:")
        };
        
        for (JLabel label : fieldLabels) {
            label.setForeground(ThemeManager.getForegroundColor());
        }
        
        // Add labels to results panel
        for (int i = 0; i < fieldLabels.length; i++) {
            resultsPanel.add(fieldLabels[i]);
            resultsPanel.add(resultLabels[i]);
        }
        
        // Compute button action
        computeButton.addActionListener(e -> {
            String selectedMonth = (String) monthCombo.getSelectedItem();
            
            // Sample computation logic
            double baseSalary = employee.getBasicSalary();
            double allowance = baseSalary * 0.10; // 10% allowance
            double overtime = baseSalary * 0.05; // 5% overtime (sample)
            double grossSalary = baseSalary + allowance + overtime;
            
            double tax = grossSalary * 0.15;
            double sss = grossSalary * 0.02;
            double philHealth = grossSalary * 0.03;
            double netSalary = grossSalary - (tax + sss + philHealth);
            
            // Update labels
            baseSalaryLabel.setText("$" + String.format("%.2f", baseSalary));
            allowanceLabel.setText("$" + String.format("%.2f", allowance));
            overtimeLabel.setText("$" + String.format("%.2f", overtime));
            grossLabel.setText("$" + String.format("%.2f", grossSalary));
            taxLabel.setText("$" + String.format("%.2f", tax));
            sssLabel.setText("$" + String.format("%.2f", sss));
            philHealthLabel.setText("$" + String.format("%.2f", philHealth));
            netLabel.setText("$" + String.format("%.2f", netSalary));
            
            JOptionPane.showMessageDialog(panel,
                "Salary computed for " + selectedMonth + "\nNet Salary: $" + String.format("%.2f", netSalary),
                "Computation Complete",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        panel.add(monthPanel, BorderLayout.NORTH);
        panel.add(resultsPanel, BorderLayout.CENTER);
        
        return panel;
    }

    // New Employee Frame
    private void openNewEmployeeFrame() {
        JFrame newEmpFrame = new JFrame("Add New Employee");
        newEmpFrame.setSize(700, 700);
        newEmpFrame.setLocationRelativeTo(this);
        newEmpFrame.setLayout(new BorderLayout());
        
        // Apply theme to the frame
        newEmpFrame.getContentPane().setBackground(ThemeManager.getBackgroundColor());
        
        // Header
        JPanel headerPanel = new JPanel();
        styleHeaderPanel(headerPanel);
        JLabel titleLabel = new JLabel("New Employee Registration", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(16, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(ThemeManager.getBackgroundColor());
        
        // Form fields - Updated to match CSV structure
        JTextField empIdField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField firstNameField = new JTextField();
        JTextField birthdayField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField sssField = new JTextField();
        JTextField philHealthField = new JTextField();
        JTextField tinField = new JTextField();
        JTextField pagIbigField = new JTextField();
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Regular", "Probationary", "Contractual"});
        JTextField positionField = new JTextField();
        JTextField supervisorField = new JTextField();
        JTextField salaryField = new JTextField();
        
        // Legacy fields for compatibility (auto-filled)
        JTextField emailField = new JTextField();
        JComboBox<String> deptCombo = new JComboBox<>(new String[]{"Executive", "IT", "HR", "Finance", "Operations", "Marketing", "Sales", "Accounting", "Customer Service", "General"});
        
        // Style fields
        styleTextField(empIdField);
        styleTextField(lastNameField);
        styleTextField(firstNameField);
        styleTextField(birthdayField);
        styleTextField(addressField);
        styleTextField(phoneField);
        styleTextField(sssField);
        styleTextField(philHealthField);
        styleTextField(tinField);
        styleTextField(pagIbigField);
        styleTextField(positionField);
        styleTextField(supervisorField);
        styleTextField(salaryField);
        styleTextField(emailField);
        
        // Style combo boxes
        statusCombo.setBackground(ThemeManager.getBackgroundColor());
        statusCombo.setForeground(ThemeManager.getForegroundColor());
        deptCombo.setBackground(ThemeManager.getBackgroundColor());
        deptCombo.setForeground(ThemeManager.getForegroundColor());
        
        // Create and style labels
        JLabel[] labels = {
            new JLabel("Employee ID:"), new JLabel("Last Name:"), new JLabel("First Name:"),
            new JLabel("Birthday (MM/DD/YYYY):"), new JLabel("Address:"), new JLabel("Phone:"),
            new JLabel("SSS Number:"), new JLabel("PhilHealth Number:"), new JLabel("TIN:"),
            new JLabel("Pag-IBIG Number:"), new JLabel("Status:"), new JLabel("Position:"),
            new JLabel("Supervisor:"), new JLabel("Basic Salary:"), new JLabel("Email (Auto-filled):"),
            new JLabel("Department (Auto-filled):")
        };
        
        // Apply theme to all labels
        for (JLabel label : labels) {
            label.setForeground(ThemeManager.getForegroundColor());
        }
        
        // Add fields to form
        formPanel.add(labels[0]); // Employee ID:
        formPanel.add(empIdField);
        formPanel.add(labels[1]); // Last Name:
        formPanel.add(lastNameField);
        formPanel.add(labels[2]); // First Name:
        formPanel.add(firstNameField);
        formPanel.add(labels[3]); // Birthday (MM/DD/YYYY):
        formPanel.add(birthdayField);
        formPanel.add(labels[4]); // Address:
        formPanel.add(addressField);
        formPanel.add(labels[5]); // Phone:
        formPanel.add(phoneField);
        formPanel.add(labels[6]); // SSS Number:
        formPanel.add(sssField);
        formPanel.add(labels[7]); // PhilHealth Number:
        formPanel.add(philHealthField);
        formPanel.add(labels[8]); // TIN:
        formPanel.add(tinField);
        formPanel.add(labels[9]); // Pag-IBIG Number:
        formPanel.add(pagIbigField);
        formPanel.add(labels[10]); // Status:
        formPanel.add(statusCombo);
        formPanel.add(labels[11]); // Position:
        formPanel.add(positionField);
        formPanel.add(labels[12]); // Supervisor:
        formPanel.add(supervisorField);
        formPanel.add(labels[13]); // Basic Salary:
        formPanel.add(salaryField);
        formPanel.add(labels[14]); // Email (Auto-filled):
        formPanel.add(emailField);
        formPanel.add(labels[15]); // Department (Auto-filled):
        formPanel.add(deptCombo);
        
        // Auto-fill email when names change
        Runnable updateAutoFields = () -> {
            String firstName = firstNameField.getText().trim().toLowerCase();
            String lastName = lastNameField.getText().trim().toLowerCase();
            if (!firstName.isEmpty() && !lastName.isEmpty()) {
                emailField.setText(firstName + "." + lastName + "@motorph.com");
            }
        };
        
        firstNameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                updateAutoFields.run();
            }
        });
        
        lastNameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                updateAutoFields.run();
            }
        });
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(ThemeManager.getBackgroundColor());
        
        JButton saveButton = new JButton("Save Employee");
        JButton cancelButton = new JButton("Cancel");
        
        forceButtonStyle(saveButton);
        forceButtonStyle(cancelButton);
        
        saveButton.addActionListener(e -> {
            try {
                // Validate and create new employee
                if (validateEmployeeForm(empIdField, lastNameField, firstNameField, emailField)) {
                    // Parse salary and default values for new fields
                    double basicSalary = Double.parseDouble(salaryField.getText().trim());
                    
                    // Create new employee using the full CSV constructor
                    Employee newEmployee = new Employee(
                        Integer.parseInt(empIdField.getText().trim()),
                        lastNameField.getText().trim(),
                        firstNameField.getText().trim(),
                        birthdayField.getText().trim(),
                        addressField.getText().trim(),
                        phoneField.getText().trim(),
                        sssField.getText().trim(),
                        philHealthField.getText().trim(),
                        tinField.getText().trim(),
                        pagIbigField.getText().trim(),
                        (String) statusCombo.getSelectedItem(),
                        positionField.getText().trim(),
                        supervisorField.getText().trim(),
                        basicSalary,
                        1500.0, // Rice subsidy default
                        1000.0, // Phone allowance default
                        1000.0, // Clothing allowance default
                        basicSalary / 2, // Gross semi-monthly rate
                        basicSalary / 168 // Hourly rate (approximate)
                    );
                    
                    // Set legacy fields manually for compatibility
                    if (!emailField.getText().trim().isEmpty()) {
                        newEmployee.setEmail(emailField.getText().trim());
                    }
                    newEmployee.setDepartment((String) deptCombo.getSelectedItem());
                    
                    // Add to list and save to CSV
                    employeeList.add(newEmployee);
                    CSVManager.saveEmployeesToCSV(employeeList);
                    
                    JOptionPane.showMessageDialog(newEmpFrame,
                        "Employee added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    updateStatus("Employee " + newEmployee.getName() + " added successfully!");
                    
                    newEmpFrame.dispose();
                    
                    // Refresh the main table if it exists
                    if (employeeManagementPanel != null) {
                        cardLayout.show(mainPanel, "employeeManagement");
                    }
                    
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(newEmpFrame,
                    "Please enter valid numbers for Employee ID and Salary",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(newEmpFrame,
                    "Error saving employee: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> newEmpFrame.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        newEmpFrame.add(headerPanel, BorderLayout.NORTH);
        newEmpFrame.add(formPanel, BorderLayout.CENTER);
        newEmpFrame.add(buttonPanel, BorderLayout.SOUTH);
        
        // Apply theme to the entire frame
        ThemeManager.applyTheme(newEmpFrame);
        
        newEmpFrame.setVisible(true);
    }

    // Validation method
    private boolean validateEmployeeForm(JTextField empIdField, JTextField lastNameField, 
                                    JTextField firstNameField, JTextField emailField) {
        if (empIdField.getText().trim().isEmpty() || 
            lastNameField.getText().trim().isEmpty() ||
            firstNameField.getText().trim().isEmpty() ||
            emailField.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(null,
                "Please fill in all required fields (Employee ID, Names, Email)",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            int empId = Integer.parseInt(empIdField.getText().trim());
            // Check for duplicate employee ID
            for (Employee emp : employeeList) {
                if (emp.getEmployeeID() == empId) {
                    JOptionPane.showMessageDialog(null,
                        "Employee ID " + empId + " already exists",
                        "Duplicate ID",
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                "Employee ID must be a valid number",
                "Invalid ID",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }

    // Helper method to style text fields
    private void styleTextField(JTextField field) {
        field.setBackground(ThemeManager.getBackgroundColor());
        field.setForeground(ThemeManager.getForegroundColor());
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getAccentColor()),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }

    private void createEmployeeListPanel() {
        employeeListPanel = new JPanel(new BorderLayout());
        employeeListPanel.setBackground(ThemeManager.getBackgroundColor());
        
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
        buttonPanel.setBackground(ThemeManager.getBackgroundColor());
        
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
        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(ThemeManager.getBackgroundColor());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Simple title
        JLabel titleLabel = new JLabel("MotorPH Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(ThemeManager.getForegroundColor());
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);
        
        // Email field
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(ThemeManager.getForegroundColor());
        loginPanel.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JTextField emailField = new JTextField(15);
        emailField.setBackground(ThemeManager.getBackgroundColor());
        emailField.setForeground(ThemeManager.getForegroundColor());
        loginPanel.add(emailField, gbc);
        
        // Password field
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(ThemeManager.getForegroundColor());
        loginPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setBackground(ThemeManager.getBackgroundColor());
        passwordField.setForeground(ThemeManager.getForegroundColor());
        loginPanel.add(passwordField, gbc);
        
        // Login button
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Login");
        forceButtonStyle(loginButton);
        loginPanel.add(loginButton, gbc);
        
        // Demo info
        // gbc.gridy = 4;
        // // JLabel demoLabel = new JLabel("Demo: Email=123, Password=123", JLabel.CENTER);
        // demoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        // demoLabel.setForeground(ThemeManager.getForegroundColor());
        // loginPanel.add(demoLabel, gbc);
        
        // Login action
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
                
                // Show Employee Management panel
                cardLayout.show(mainPanel, "employeeManagement");
                
                // Make the sidebar visible after login
                Container parent = mainPanel.getParent();
                Component[] components = parent.getComponents();
                for (Component component : components) {
                    if (component instanceof JPanel && component != mainPanel) {
                        component.setVisible(true);
                        break;
                    }
                }
                
                // Apply current theme to all components
                ThemeManager.applyTheme(this);
                
                // Update status
                updateStatus("Login successful - Welcome " + employee.getName() + "!");
                
                // Refresh the employee table with the latest data
                if (employeeManagementPanel != null) {
                    refreshEmployeeTableAfterLogin();
                }
                
            } else {
                JOptionPane.showMessageDialog(loginPanel, 
                    "Invalid email or password", 
                    "Login Failed", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void refreshEmployeeTableAfterLogin() {
        // This method will be called after login to ensure the employee table is populated
        try {
            // Reload employee data from CSV
            employeeList = CSVManager.loadEmployeesFromCSV();
            if (employeeList == null) {
                employeeList = new ArrayList<>();
            }
            
            // Find the table model in the employee management panel and refresh it
            refreshEmployeeManagementTable();
            
            // Force a repaint of the employee management panel
            if (employeeManagementPanel != null) {
                employeeManagementPanel.revalidate();
                employeeManagementPanel.repaint();
            }
        } catch (Exception e) {
            System.err.println("Error refreshing employee table after login: " + e.getMessage());
        }
    }

    private void refreshEmployeeManagementTable() {
        if (employeeManagementPanel == null) return;
        
        // Recursively find the JTable in the employee management panel
        JTable employeeTable = findEmployeeTable(employeeManagementPanel);
        if (employeeTable != null && employeeTable.getModel() instanceof DefaultTableModel) {
            DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();
            refreshEmployeeTable(model);
        }
    }

    private JTable findEmployeeTable(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JTable) {
                return (JTable) component;
            } else if (component instanceof Container) {
                JTable found = findEmployeeTable((Container) component);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
    
    private void createDashboardPanel() {
        dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setBackground(ThemeManager.getBackgroundColor());
        
        // Header panel with blue background
        JPanel headerPanel = new JPanel(new BorderLayout());
        styleHeaderPanel(headerPanel);
        
        JLabel titleLabel = new JLabel("MotorPH Employee Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Main content panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(ThemeManager.getBackgroundColor());
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
        employeeInfoPanel.setBackground(ThemeManager.getBackgroundColor());
        
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
        buttonPanel.setBackground(ThemeManager.getBackgroundColor());
        buttonPanel.add(updateButton);
        
        // Assemble the panel
        employeeInfoPanel.add(headerPanel, BorderLayout.NORTH);
        employeeInfoPanel.add(formPanel, BorderLayout.CENTER);
        employeeInfoPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void createPayrollPanel() {
        payrollPanel = new JPanel(new BorderLayout());
        payrollPanel.setBackground(ThemeManager.getBackgroundColor());
        
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
        buttonPanel.setBackground(ThemeManager.getBackgroundColor());
        
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
        panel.setBackground(ThemeManager.getBackgroundColor());
        
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
        panel.setBackground(ThemeManager.getBackgroundColor());
        
        // Input fields
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBackground(ThemeManager.getBackgroundColor());
        
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
        buttonPanel.setBackground(ThemeManager.getBackgroundColor());
        buttonPanel.add(calculateButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createDeductionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(ThemeManager.getBackgroundColor());
        
        // Deductions input
        JPanel deductionsPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        deductionsPanel.setBackground(ThemeManager.getBackgroundColor());
        
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
        buttonPanel.setBackground(ThemeManager.getBackgroundColor());
        buttonPanel.add(calculateButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void createLeaveRequestPanel() {
        leaveRequestPanel = new JPanel(new BorderLayout());
        leaveRequestPanel.setBackground(ThemeManager.getBackgroundColor());
        
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
        formPanel.setBackground(ThemeManager.getBackgroundColor());
        
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
        buttonPanel.setBackground(ThemeManager.getBackgroundColor());
        buttonPanel.add(submitButton);
        
        // Add components to panel
        leaveRequestPanel.add(headerPanel, BorderLayout.NORTH);
        leaveRequestPanel.add(formPanel, BorderLayout.CENTER);
        leaveRequestPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void createTaxFormPanel() {
        taxFormPanel = new JPanel(new BorderLayout());
        taxFormPanel.setBackground(ThemeManager.getBackgroundColor());
        
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
        infoPanel.setBackground(ThemeManager.getBackgroundColor());
        
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
        buttonPanel.setBackground(ThemeManager.getBackgroundColor());
        buttonPanel.add(downloadButton);
        
        // Add components to panel
        taxFormPanel.add(headerPanel, BorderLayout.NORTH);
        taxFormPanel.add(infoPanel, BorderLayout.CENTER);
        taxFormPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void createAttendancePanel() {
        attendancePanel = new JPanel(new BorderLayout());
        attendancePanel.setBackground(ThemeManager.getBackgroundColor());
        
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
        panel.setBackground(ThemeManager.getBackgroundColor());
        
        // Current status panel
        JPanel statusPanel = new JPanel(new GridBagLayout());
        statusPanel.setBackground(ThemeManager.getBackgroundColor());
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
        buttonPanel.setBackground(ThemeManager.getBackgroundColor());
        
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
        panel.setBackground(ThemeManager.getBackgroundColor());
        
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
        filterPanel.setBackground(ThemeManager.getBackgroundColor());
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
        panel.setBackground(ThemeManager.getBackgroundColor());
        
        // Summary statistics
        JPanel statsPanel = new JPanel(new GridLayout(3, 2, 20, 10));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Attendance Statistics"));
        statsPanel.setBackground(ThemeManager.getBackgroundColor());
        
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
        chartPanel.setBackground(ThemeManager.getBackgroundColor());
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
        buttonPanel.setBackground(ThemeManager.getBackgroundColor());
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
        button.setBackground(ThemeManager.getButtonBackgroundColor());
        button.setForeground(ThemeManager.getButtonForegroundColor());
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        
        // More visible border
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getAccentColor(), 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Remove all existing mouse listeners to avoid duplicates
        for (java.awt.event.MouseListener listener : button.getMouseListeners()) {
            if (listener.getClass().getName().contains("MotorPHApp")) {
                button.removeMouseListener(listener);
            }
        }
        
        // Add hover effect with custom listener
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Color hoverColor = ThemeManager.isDarkMode() ? 
                    new Color(0, 140, 255) : new Color(0, 120, 230);
                button.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ThemeManager.getButtonBackgroundColor());
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
        panel.setBackground(ThemeManager.getBackgroundColor());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void styleHeaderPanel(JPanel panel) {
        panel.setBackground(ThemeManager.getAccentColor());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    
    // Helper method to update status bar
    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
            // Auto-clear status after 5 seconds for non-error messages
            if (!message.toLowerCase().contains("error") && !message.toLowerCase().contains("failed")) {
                javax.swing.Timer statusTimer = new javax.swing.Timer(5000, e -> statusLabel.setText("Ready - MotorPH Employee Management System"));
                statusTimer.setRepeats(false);
                statusTimer.start();
            }
        }
    }
}
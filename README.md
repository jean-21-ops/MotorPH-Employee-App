# MotorPH Employee Management App

A comprehensive employee management application built with Java Swing featuring employee records management, salary computation, attendance tracking, and theme customization.

## Features

- Employee Management (Create, Read, Update, Delete)
- CSV-based data persistence
- Salary computation by month
- Attendance tracking
- Dark/Light theme toggle with smooth animations
- Search and filter functionality

## How to Run

### Prerequisites
- Java 8 or higher installed on your system

### Running the Application

#### Option 1: Using the provided scripts
- **Windows**: Double-click `run.bat`
- **macOS/Linux**: Run `./run.sh` in terminal

#### Option 2: Using command line
```bash
# Navigate to the project directory
cd /path/to/MotorPH-Employee-App

# Compile and run
java -cp . Term2.Main
```

#### Option 3: Using Java IDE
1. Open the project in your preferred Java IDE (Eclipse, IntelliJ IDEA, VS Code, etc.)
2. Run the `Main.java` file located in `src/Term2/Main.java`

## Login Credentials
- **Username**: admin@motorph.com
- **Password**: MotorPH2024

## Data Storage
Employee data is automatically saved to `employees.csv` and persists between application sessions.

## Project Structure
```
src/Term2/
├── Main.java          # Application entry point
├── MotorPHApp.java    # Main application GUI
├── Employee.java      # Employee data model
├── CSVManager.java    # CSV file operations
└── ThemeManager.java  # Theme management
```

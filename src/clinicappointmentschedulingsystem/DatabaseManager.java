package clinicappointmentschedulingsystem;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:clinic.db";
    private static DatabaseManager instance;
    
    private DatabaseManager() {
        initializeDatabase();
    }
    
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            createTables(conn);
            insertDefaultData(conn);
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }
    
    private void createTables(Connection conn) throws SQLException {
        // Users table
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                role TEXT NOT NULL DEFAULT 'staff',
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        // Doctors table
        String createDoctorsTable = """
            CREATE TABLE IF NOT EXISTS doctors (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                specialization TEXT,
                phone TEXT,
                email TEXT,
                active BOOLEAN DEFAULT 1
            )
        """;
        
        // Patients table
        String createPatientsTable = """
            CREATE TABLE IF NOT EXISTS patients (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                phone TEXT,
                email TEXT,
                address TEXT,
                date_of_birth DATE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        // Appointments table
        String createAppointmentsTable = """
            CREATE TABLE IF NOT EXISTS appointments (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                patient_name TEXT NOT NULL,
                doctor_name TEXT NOT NULL,
                appointment_date DATE NOT NULL,
                appointment_time TEXT NOT NULL,
                status TEXT DEFAULT 'scheduled',
                notes TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        conn.createStatement().execute(createUsersTable);
        conn.createStatement().execute(createDoctorsTable);
        conn.createStatement().execute(createPatientsTable);
        conn.createStatement().execute(createAppointmentsTable);
    }
    
    private void insertDefaultData(Connection conn) throws SQLException {
        // Insert default admin user if not exists
        String checkUser = "SELECT COUNT(*) FROM users WHERE username = 'admin'";
        try (PreparedStatement stmt = conn.prepareStatement(checkUser)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                String insertAdmin = "INSERT INTO users (username, password, role) VALUES ('admin', 'clinic123', 'admin')";
                conn.createStatement().execute(insertAdmin);
            }
        }
        
        // Insert default doctors if not exists
        String checkDoctors = "SELECT COUNT(*) FROM doctors";
        try (PreparedStatement stmt = conn.prepareStatement(checkDoctors)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                String[] doctors = {
                    "INSERT INTO doctors (name, specialization) VALUES ('Dr. Smith', 'General Medicine')",
                    "INSERT INTO doctors (name, specialization) VALUES ('Dr. Johnson', 'Cardiology')",
                    "INSERT INTO doctors (name, specialization) VALUES ('Dr. Williams', 'Pediatrics')",
                    "INSERT INTO doctors (name, specialization) VALUES ('Dr. Brown', 'Orthopedics')"
                };
                
                for (String doctor : doctors) {
                    conn.createStatement().execute(doctor);
                }
            }
        }
    }
    
    // User authentication
    public boolean authenticateUser(String username, String password) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
            return false;
        }
    }
    
    // Get all doctors
    public List<String> getAllDoctors() {
        List<String> doctors = new ArrayList<>();
        String query = "SELECT name FROM doctors WHERE active = 1 ORDER BY name";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                doctors.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching doctors: " + e.getMessage());
        }
        
        return doctors;
    }
    
    // Save appointment
    public boolean saveAppointment(String patientName, String doctorName, LocalDate date, String time) {
        String query = "INSERT INTO appointments (patient_name, doctor_name, appointment_date, appointment_time) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, patientName);
            stmt.setString(2, doctorName);
            stmt.setString(3, date.toString());
            stmt.setString(4, time);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saving appointment: " + e.getMessage());
            return false;
        }
    }
    
    // Get all appointments
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT * FROM appointments ORDER BY appointment_date DESC, appointment_time";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment(
                    rs.getInt("id"),
                    rs.getString("patient_name"),
                    rs.getString("doctor_name"),
                    LocalDate.parse(rs.getString("appointment_date")),
                    rs.getString("appointment_time"),
                    rs.getString("status")
                );
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching appointments: " + e.getMessage());
        }
        
        return appointments;
    }
    
    // Check for appointment conflicts
    public boolean hasConflict(String doctorName, LocalDate date, String time) {
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_name = ? AND appointment_date = ? AND appointment_time = ? AND status != 'cancelled'";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, doctorName);
            stmt.setString(2, date.toString());
            stmt.setString(3, time);
            
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error checking conflicts: " + e.getMessage());
            return false;
        }
    }
}
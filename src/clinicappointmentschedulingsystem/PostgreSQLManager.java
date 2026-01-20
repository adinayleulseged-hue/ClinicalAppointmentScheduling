package clinicappointmentschedulingsystem;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PostgreSQLManager {
    // Database connection parameters
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "5432";
    private static final String DB_NAME = "clinic_db";
    private static final String DB_USER = "clinic_user";
    private static final String DB_PASSWORD = "clinic_password";
    
    private static final String DB_URL = String.format("jdbc:postgresql://%s:%s/%s", DB_HOST, DB_PORT, DB_NAME);
    private static PostgreSQLManager instance;
    
    private PostgreSQLManager() {
        initializeDatabase();
    }
    
    public static PostgreSQLManager getInstance() {
        if (instance == null) {
            instance = new PostgreSQLManager();
        }
        return instance;
    }
    
    private Connection getConnection() throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", DB_USER);
        props.setProperty("password", DB_PASSWORD);
        props.setProperty("ssl", "false");
        return DriverManager.getConnection(DB_URL, props);
    }
    
    private void initializeDatabase() {
        try (Connection conn = getConnection()) {
            createTables(conn);
            insertDefaultData(conn);
            System.out.println("PostgreSQL database initialized successfully");
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
            System.err.println("Falling back to file-based storage...");
        }
    }
    
    private void createTables(Connection conn) throws SQLException {
        // Users table
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                username VARCHAR(50) UNIQUE NOT NULL,
                password VARCHAR(100) NOT NULL,
                role VARCHAR(20) NOT NULL DEFAULT 'staff',
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        // Doctors table
        String createDoctorsTable = """
            CREATE TABLE IF NOT EXISTS doctors (
                id SERIAL PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                specialization VARCHAR(100),
                phone VARCHAR(20),
                email VARCHAR(100),
                active BOOLEAN DEFAULT true,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        // Patients table
        String createPatientsTable = """
            CREATE TABLE IF NOT EXISTS patients (
                id SERIAL PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                phone VARCHAR(20),
                email VARCHAR(100),
                address TEXT,
                date_of_birth DATE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        // Appointments table
        String createAppointmentsTable = """
            CREATE TABLE IF NOT EXISTS appointments (
                id SERIAL PRIMARY KEY,
                patient_name VARCHAR(100) NOT NULL,
                doctor_name VARCHAR(100) NOT NULL,
                appointment_date DATE NOT NULL,
                appointment_time TIME NOT NULL,
                status VARCHAR(20) DEFAULT 'scheduled',
                notes TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        // Create indexes for better performance
        String createIndexes = """
            CREATE INDEX IF NOT EXISTS idx_appointments_date ON appointments(appointment_date);
            CREATE INDEX IF NOT EXISTS idx_appointments_doctor ON appointments(doctor_name);
            CREATE INDEX IF NOT EXISTS idx_appointments_status ON appointments(status);
        """;
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createDoctorsTable);
            stmt.execute(createPatientsTable);
            stmt.execute(createAppointmentsTable);
            stmt.execute(createIndexes);
        }
    }
    
    private void insertDefaultData(Connection conn) throws SQLException {
        // Insert default admin user if not exists
        String checkUser = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(checkUser)) {
            stmt.setString(1, "admin");
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                String insertAdmin = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertAdmin)) {
                    insertStmt.setString(1, "admin");
                    insertStmt.setString(2, "clinic123");
                    insertStmt.setString(3, "admin");
                    insertStmt.executeUpdate();
                }
                
                // Insert staff user
                try (PreparedStatement insertStmt = conn.prepareStatement(insertAdmin)) {
                    insertStmt.setString(1, "staff");
                    insertStmt.setString(2, "staff123");
                    insertStmt.setString(3, "staff");
                    insertStmt.executeUpdate();
                }
            }
        }
        
        // Insert default doctors if not exists
        String checkDoctors = "SELECT COUNT(*) FROM doctors";
        try (PreparedStatement stmt = conn.prepareStatement(checkDoctors)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                String insertDoctor = "INSERT INTO doctors (name, specialization) VALUES (?, ?)";
                String[][] doctors = {
                    {"Dr. Smith", "General Medicine"},
                    {"Dr. Johnson", "Cardiology"},
                    {"Dr. Williams", "Pediatrics"},
                    {"Dr. Brown", "Orthopedics"},
                    {"Dr. Davis", "Dermatology"}
                };
                
                try (PreparedStatement insertStmt = conn.prepareStatement(insertDoctor)) {
                    for (String[] doctor : doctors) {
                        insertStmt.setString(1, doctor[0]);
                        insertStmt.setString(2, doctor[1]);
                        insertStmt.executeUpdate();
                    }
                }
            }
        }
    }
    
    // User authentication
    public boolean authenticateUser(String username, String password) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
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
    
    // Get user role
    public String getUserRole(String username) {
        String query = "SELECT role FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user role: " + e.getMessage());
        }
        return "staff"; // default role
    }
    
    // Get all doctors
    public List<String> getAllDoctors() {
        List<String> doctors = new ArrayList<>();
        String query = "SELECT name FROM doctors WHERE active = true ORDER BY name";
        
        try (Connection conn = getConnection();
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
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, patientName);
            stmt.setString(2, doctorName);
            stmt.setDate(3, Date.valueOf(date));
            stmt.setTime(4, Time.valueOf(time + ":00")); // Convert HH:MM to HH:MM:SS
            
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
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment(
                    rs.getInt("id"),
                    rs.getString("patient_name"),
                    rs.getString("doctor_name"),
                    rs.getDate("appointment_date").toLocalDate(),
                    rs.getTime("appointment_time").toString().substring(0, 5), // HH:MM format
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
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, doctorName);
            stmt.setDate(2, Date.valueOf(date));
            stmt.setTime(3, Time.valueOf(time + ":00"));
            
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error checking conflicts: " + e.getMessage());
            return false;
        }
    }
    
    // Update appointment status
    public boolean updateAppointmentStatus(int appointmentId, String status) {
        String query = "UPDATE appointments SET status = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, appointmentId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating appointment status: " + e.getMessage());
            return false;
        }
    }
    
    // Get appointments by date range
    public List<Appointment> getAppointmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT * FROM appointments WHERE appointment_date BETWEEN ? AND ? ORDER BY appointment_date, appointment_time";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment(
                    rs.getInt("id"),
                    rs.getString("patient_name"),
                    rs.getString("doctor_name"),
                    rs.getDate("appointment_date").toLocalDate(),
                    rs.getTime("appointment_time").toString().substring(0, 5),
                    rs.getString("status")
                );
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching appointments by date range: " + e.getMessage());
        }
        
        return appointments;
    }
    
    // Test database connection
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
}
package cllinicappointmentschedulingsystem;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Simple file-based data storage for the clinic system
 * This is a temporary solution until SQLite JDBC driver is properly configured
 */
public class SimpleDataStore {
    private static final String DATA_DIR = "clinic_data";
    private static final String USERS_FILE = DATA_DIR + "/users.properties";
    private static final String APPOINTMENTS_FILE = DATA_DIR + "/appointments.txt";
    private static final String DOCTORS_FILE = DATA_DIR + "/doctors.txt";
    
    private static SimpleDataStore instance;
    
    private SimpleDataStore() {
        initializeDataStore();
    }
    
    public static SimpleDataStore getInstance() {
        if (instance == null) {
            instance = new SimpleDataStore();
        }
        return instance;
    }
    
    private void initializeDataStore() {
        // Create data directory
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        
        // Initialize users file
        File usersFile = new File(USERS_FILE);
        if (!usersFile.exists()) {
            try {
                Properties users = new Properties();
                users.setProperty("admin", "clinic123");
                users.setProperty("staff", "staff123");
                users.store(new FileOutputStream(usersFile), "Clinic Users");
            } catch (IOException e) {
                System.err.println("Error creating users file: " + e.getMessage());
            }
        }
        
        // Initialize doctors file
        File doctorsFile = new File(DOCTORS_FILE);
        if (!doctorsFile.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(doctorsFile))) {
                writer.println("Dr. Smith - General Medicine");
                writer.println("Dr. Johnson - Cardiology");
                writer.println("Dr. Williams - Pediatrics");
                writer.println("Dr. Brown - Orthopedics");
                writer.println("Dr. Davis - Dermatology");
            } catch (IOException e) {
                System.err.println("Error creating doctors file: " + e.getMessage());
            }
        }
        
        // Create appointments file if it doesn't exist
        File appointmentsFile = new File(APPOINTMENTS_FILE);
        if (!appointmentsFile.exists()) {
            try {
                appointmentsFile.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating appointments file: " + e.getMessage());
            }
        }
    }
    
    public boolean authenticateUser(String username, String password) {
        try {
            Properties users = new Properties();
            users.load(new FileInputStream(USERS_FILE));
            String storedPassword = users.getProperty(username);
            return storedPassword != null && storedPassword.equals(password);
        } catch (IOException e) {
            System.err.println("Error reading users file: " + e.getMessage());
            return false;
        }
    }
    
    public List<String> getAllDoctors() {
        List<String> doctors = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DOCTORS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Extract just the doctor name (before the dash)
                String doctorName = line.split(" - ")[0].trim();
                doctors.add(doctorName);
            }
        } catch (IOException e) {
            System.err.println("Error reading doctors file: " + e.getMessage());
        }
        return doctors;
    }
    
    public boolean saveAppointment(String patientName, String doctorName, LocalDate date, String time) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(APPOINTMENTS_FILE, true))) {
            String appointmentRecord = String.format("%s|%s|%s|%s|scheduled|%s",
                    patientName, doctorName, date.toString(), time, 
                    java.time.LocalDateTime.now().toString());
            writer.println(appointmentRecord);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving appointment: " + e.getMessage());
            return false;
        }
    }
    
    public List<SimpleAppointment> getAllAppointments() {
        List<SimpleAppointment> appointments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(APPOINTMENTS_FILE))) {
            String line;
            int id = 1;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 5) {
                    SimpleAppointment appointment = new SimpleAppointment(
                            id++,
                            parts[0], // patient name
                            parts[1], // doctor name
                            LocalDate.parse(parts[2]), // date
                            parts[3], // time
                            parts[4]  // status
                    );
                    appointments.add(appointment);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
        }
        return appointments;
    }
    
    public boolean hasConflict(String doctorName, LocalDate date, String time) {
        List<SimpleAppointment> appointments = getAllAppointments();
        return appointments.stream()
                .anyMatch(apt -> apt.getDoctorName().equals(doctorName) 
                        && apt.getAppointmentDate().equals(date)
                        && apt.getAppointmentTime().equals(time)
                        && !"cancelled".equals(apt.getStatus()));
    }
}
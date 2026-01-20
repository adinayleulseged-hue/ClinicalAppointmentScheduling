# 🔗 CLASS CONNECTIONS & RELATIONSHIPS GUIDE

## 🎯 **HOW EACH CLASS CONNECTS TO THE SYSTEM**

---

## 📱 **MAIN APPLICATION CLASSES**

### **1. FinalDatabaseApp.java** 
**🎯 Role**: Main Application Entry Point with Database Integration

#### **How it Connects:**
```java
public class FinalDatabaseApp extends Application {
    // 🔗 CONNECTS TO:
    // - PostgreSQL Database (direct JDBC connection)
    // - JavaFX UI Components (VBox, TextField, Button, etc.)
    // - Java SQL classes (Connection, PreparedStatement, ResultSet)
    
    // 📊 Database Connection
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    // 🖥️ UI Connection
    @Override
    public void start(Stage primaryStage) {
        showLoginScreen(primaryStage);  // Creates JavaFX Scene
    }
    
    // 💾 Data Connection
    private boolean saveAppointmentToDatabase(...) {
        // Connects directly to PostgreSQL via JDBC
    }
}
```

#### **Connection Flow:**
```
FinalDatabaseApp
    ↓ (extends)
JavaFX Application
    ↓ (uses)
JDBC Driver → PostgreSQL Database
    ↓ (creates)
UI Scenes → User Interaction
```

---

### **2. DatabaseTestApp.java**
**🎯 Role**: Database Connection Testing & Diagnostics

#### **How it Connects:**
```java
public class DatabaseTestApp extends Application {
    // 🔗 CONNECTS TO:
    // - Same PostgreSQL database as FinalDatabaseApp
    // - JavaFX UI for test results display
    // - JDBC metadata for database inspection
    
    // 🧪 Test Connection
    private boolean testDatabaseConnection() {
        Class.forName("org.postgresql.Driver");  // JDBC Driver
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        return conn != null;
    }
    
    // 📋 Test Tables
    private void checkTables() {
        DatabaseMetaData metaData = conn.getMetaData();  // Database inspection
        ResultSet tables = metaData.getTables(...);      // Table verification
    }
}
```

#### **Connection Flow:**
```
DatabaseTestApp
    ↓ (tests)
PostgreSQL Connection
    ↓ (verifies)
Database Tables & Structure
    ↓ (reports to)
User Interface (Test Results)
```

---

### **3. WorkingClinicApp.java**
**🎯 Role**: Standalone Application (No Database Required)

#### **How it Connects:**
```java
public class WorkingClinicApp extends Application {
    // 🔗 CONNECTS TO:
    // - In-memory data structures (ArrayList<AppointmentRecord>)
    // - JavaFX UI components only
    // - No external database connections
    
    // 💾 Internal Data Storage
    private List<AppointmentRecord> appointments = new ArrayList<>();
    
    // 🏠 Internal Data Class
    private static class AppointmentRecord {
        String patient, phone, doctor, date, time, notes, status;
        LocalDateTime created;
    }
    
    // 🖥️ Pure UI Connection
    private void showAppointmentScreen(Stage stage, String loggedInUser) {
        // Creates JavaFX components directly
        // No database calls - all data in memory
    }
}
```

#### **Connection Flow:**
```
WorkingClinicApp
    ↓ (uses)
In-Memory ArrayList
    ↓ (displays in)
JavaFX UI Components
    ↓ (no external connections)
Self-Contained System
```

---

## 🗄️ **DATABASE MANAGEMENT CLASSES**

### **4. PostgreSQLManager.java**
**🎯 Role**: Enterprise Database Operations Manager

#### **How it Connects:**
```java
public class PostgreSQLManager {
    // 🔗 CONNECTS TO:
    // - PostgreSQL database via JDBC
    // - Other classes that need database operations
    // - Connection pooling for performance
    
    // 🔌 Singleton Pattern Connection
    private static PostgreSQLManager instance;
    public static PostgreSQLManager getInstance() {
        if (instance == null) {
            instance = new PostgreSQLManager();
        }
        return instance;
    }
    
    // 🗄️ Database Operations
    public boolean authenticateUser(String username, String password) {
        // Connects to users table in PostgreSQL
    }
    
    public List<String> getAllDoctors() {
        // Connects to doctors table in PostgreSQL
    }
}
```

#### **How Other Classes Use It:**
```java
// In any other class:
PostgreSQLManager dbManager = PostgreSQLManager.getInstance();
boolean isValid = dbManager.authenticateUser(username, password);
List<String> doctors = dbManager.getAllDoctors();
```

#### **Connection Flow:**
```
Any Application Class
    ↓ (calls)
PostgreSQLManager.getInstance()
    ↓ (connects to)
PostgreSQL Database
    ↓ (returns data to)
Calling Class
```

---

### **5. SimpleDataStore.java**
**🎯 Role**: File-Based Storage Alternative

#### **How it Connects:**
```java
public class SimpleDataStore {
    // 🔗 CONNECTS TO:
    // - Local file system (clinic_data folder)
    // - Properties files for user data
    // - Text files for appointments
    
    // 📁 File System Connection
    private static final String DATA_DIR = "clinic_data";
    private static final String USERS_FILE = DATA_DIR + "/users.properties";
    private static final String APPOINTMENTS_FILE = DATA_DIR + "/appointments.txt";
    
    // 💾 File Operations
    public boolean authenticateUser(String username, String password) {
        Properties users = new Properties();
        users.load(new FileInputStream(USERS_FILE));  // File connection
        return users.getProperty(username).equals(password);
    }
}
```

#### **Connection Flow:**
```
SimpleDataStore
    ↓ (reads/writes)
Local Files (clinic_data/)
    ↓ (stores)
User & Appointment Data
    ↓ (persists between)
Application Sessions
```

---

## 🎮 **CONTROLLER CLASSES**

### **6. LoginController.java**
**🎯 Role**: Handles Login Screen Logic

#### **How it Connects:**
```java
public class LoginController {
    // 🔗 CONNECTS TO:
    // - FXML UI elements (@FXML annotations)
    // - Data storage classes (SimpleDataStore or PostgreSQLManager)
    // - Next screen (FXMLDocument.fxml)
    
    @FXML private TextField txtUsername;     // UI Connection
    @FXML private PasswordField txtPassword; // UI Connection
    @FXML private Label lblMessage;          // UI Connection
    
    @FXML
    private void handleLogin() {
        // 🔗 Connects to data storage
        SimpleDataStore dataStore = SimpleDataStore.getInstance();
        if (dataStore.authenticateUser(username, password)) {
            // 🔗 Connects to next screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
            Parent root = loader.load();
            // Switch to appointment screen
        }
    }
}
```

#### **Connection Flow:**
```
Login.fxml (UI)
    ↓ (controlled by)
LoginController
    ↓ (validates with)
SimpleDataStore/PostgreSQLManager
    ↓ (navigates to)
FXMLDocument.fxml (Appointment Screen)
```

---

### **7. ClinicAppointmentController.java**
**🎯 Role**: Handles Appointment Screen Logic

#### **How it Connects:**
```java
public class ClinicAppointmentController {
    // 🔗 CONNECTS TO:
    // - FXML UI elements for appointment form
    // - Data storage for saving/loading appointments
    // - Date/Time utilities for validation
    
    @FXML private TextField txtPatient;      // UI Connection
    @FXML private ComboBox<String> cmbDoctor; // UI Connection
    @FXML private DatePicker datePicker;     // UI Connection
    @FXML private TextArea txtOutput;        // UI Connection
    
    private SimpleDataStore dataStore;       // Data Connection
    
    @FXML
    private void initialize() {
        dataStore = SimpleDataStore.getInstance();  // Connect to data
        List<String> doctors = dataStore.getAllDoctors(); // Load data
        cmbDoctor.getItems().addAll(doctors);       // Populate UI
    }
    
    @FXML
    private void handleScheduleAppointment() {
        // Get data from UI → Validate → Save to storage → Update UI
    }
}
```

#### **Connection Flow:**
```
FXMLDocument.fxml (UI)
    ↓ (controlled by)
ClinicAppointmentController
    ↓ (saves/loads data with)
SimpleDataStore/PostgreSQLManager
    ↓ (updates)
UI Display (TextArea)
```

---

## 📄 **FXML FILES (UI LAYOUTS)**

### **8. Login.fxml**
**🎯 Role**: Login Screen Layout

#### **How it Connects:**
```xml
<AnchorPane xmlns:fx="http://javafx.com/fxml"
            fx:controller="cllinicappointmentschedulingsystem.LoginController">
    <!-- 🔗 CONNECTS TO LoginController class -->
    
    <TextField fx:id="txtUsername"/>  <!-- Connected to @FXML field -->
    <Button onAction="#handleLogin"/> <!-- Connected to @FXML method -->
</AnchorPane>
```

#### **Connection Flow:**
```
Login.fxml
    ↓ (fx:controller attribute)
LoginController.java
    ↓ (fx:id attributes)
@FXML annotated fields/methods
```

---

### **9. FXMLDocument.fxml**
**🎯 Role**: Main Appointment Screen Layout

#### **How it Connects:**
```xml
<AnchorPane fx:controller="cllinicappointmentschedulingsystem.ClinicAppointmentController">
    <TextField fx:id="txtPatient"/>           <!-- → @FXML private TextField txtPatient -->
    <ComboBox fx:id="cmbDoctor"/>            <!-- → @FXML private ComboBox cmbDoctor -->
    <Button onAction="#handleScheduleAppointment"/> <!-- → @FXML method -->
</AnchorPane>
```

---

## 🔄 **INTER-CLASS COMMUNICATION PATTERNS**

### **Pattern 1: Main App → Database**
```java
FinalDatabaseApp
    ↓ (direct JDBC calls)
PostgreSQL Database
    ↓ (returns data)
FinalDatabaseApp UI
```

### **Pattern 2: Controller → Data Store**
```java
LoginController
    ↓ (getInstance())
SimpleDataStore
    ↓ (file operations)
Local Files
```

### **Pattern 3: FXML → Controller**
```java
Login.fxml
    ↓ (fx:controller)
LoginController
    ↓ (@FXML annotations)
UI Elements
```

### **Pattern 4: Screen Navigation**
```java
LoginController
    ↓ (FXMLLoader.load())
FXMLDocument.fxml
    ↓ (fx:controller)
ClinicAppointmentController
```

---

## 🎯 **COMPLETE CONNECTION MAP**

```
🖥️ USER INTERFACE LAYER
├── Login.fxml ←→ LoginController.java
├── FXMLDocument.fxml ←→ ClinicAppointmentController.java
└── Pure JavaFX (FinalDatabaseApp, WorkingClinicApp)

🧠 BUSINESS LOGIC LAYER
├── FinalDatabaseApp.java (Main Application)
├── LoginController.java (Authentication Logic)
├── ClinicAppointmentController.java (Appointment Logic)
└── DatabaseTestApp.java (Testing Logic)

💾 DATA ACCESS LAYER
├── PostgreSQLManager.java ←→ PostgreSQL Database
├── SimpleDataStore.java ←→ Local Files
└── Direct JDBC ←→ Database (in FinalDatabaseApp)

🗄️ DATA STORAGE LAYER
├── PostgreSQL Database (appointments, doctors, users tables)
├── Local Files (clinic_data folder)
└── In-Memory (ArrayList in WorkingClinicApp)
```

---

## 🚀 **HOW TO USE EACH CLASS**

### **To Run Database Version:**
```java
// Set main class in project.properties:
javafx.main.class=cllinicappointmentschedulingsystem.FinalDatabaseApp
```

### **To Test Database:**
```java
// Set main class to:
javafx.main.class=cllinicappointmentschedulingsystem.DatabaseTestApp
```

### **To Run Without Database:**
```java
// Set main class to:
javafx.main.class=cllinicappointmentschedulingsystem.WorkingClinicApp
```

### **To Use PostgreSQL Manager in Your Code:**
```java
PostgreSQLManager db = PostgreSQLManager.getInstance();
boolean success = db.saveAppointment(patient, doctor, date, time);
```

### **To Use Simple Data Store:**
```java
SimpleDataStore store = SimpleDataStore.getInstance();
List<String> doctors = store.getAllDoctors();
```

---

## 🔧 **DEPENDENCY REQUIREMENTS**

### **For Database Classes:**
- PostgreSQL JDBC Driver (`postgresql-42.7.1.jar`)
- Running PostgreSQL server
- Database `clinic_db` with proper tables

### **For File-Based Classes:**
- Write permissions in application directory
- No external dependencies

### **For UI Classes:**
- JavaFX runtime
- FXML files in correct package location

This guide shows exactly how each class connects and communicates with others in your clinic appointment system! 🏥✨
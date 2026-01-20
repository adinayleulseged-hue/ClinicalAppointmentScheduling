package cllinicappointmentschedulingsystem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FinalDatabaseApp extends Application {
    
    // Database connection with your credentials
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/clinic_db";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "1234";
    
    @Override
    public void start(Stage primaryStage) {
        // Initialize database tables first
        initializeDatabase();
        showLoginScreen(primaryStage);
    }

    private void initializeDatabase() {
        try (Connection conn = getConnection()) {
            // Create appointments table if it doesn't exist
            String createTable = """
                CREATE TABLE IF NOT EXISTS appointments (
                    id SERIAL PRIMARY KEY,
                    patient_name VARCHAR(100) NOT NULL,
                    patient_phone VARCHAR(20),
                    doctor_name VARCHAR(100) NOT NULL,
                    appointment_date DATE NOT NULL,
                    appointment_time TIME NOT NULL,
                    status VARCHAR(20) DEFAULT 'SCHEDULED',
                    notes TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """;
            
            Statement stmt = conn.createStatement();
            stmt.execute(createTable);
            System.out.println("✅ Database initialized successfully");
            
        } catch (SQLException e) {
            System.err.println("❌ Database initialization failed: " + e.getMessage());
        }
    }

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found", e);
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private void showLoginScreen(Stage stage) {
        VBox loginBox = new VBox(20);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(30));
        loginBox.setStyle("-fx-background-color: linear-gradient(to bottom, #e3f2fd, #bbdefb);");

        Label title = new Label("🏥 CLINIC DATABASE SYSTEM");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1565c0;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setPrefWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setPrefWidth(250);

        Button loginBtn = new Button("🔐 LOGIN");
        loginBtn.setPrefWidth(120);
        loginBtn.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white; -fx-font-weight: bold;");

        Button clearBtn = new Button("🗑️ CLEAR");
        clearBtn.setPrefWidth(120);
        clearBtn.setStyle("-fx-background-color: #757575; -fx-text-fill: white;");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

        Label credentialsLabel = new Label("📋 Login Credentials:\nAdmin: admin / clinic123\nStaff: staff / staff123");
        credentialsLabel.setStyle("-fx-text-alignment: center; -fx-font-size: 12px;");

        // Test database connection on startup
        try (Connection conn = getConnection()) {
            messageLabel.setText("✅ Database connected successfully!");
            messageLabel.setStyle("-fx-text-fill: green;");
        } catch (SQLException e) {
            messageLabel.setText("❌ Database connection failed: " + e.getMessage());
            messageLabel.setStyle("-fx-text-fill: red;");
        }

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loginBtn, clearBtn);

        loginBox.getChildren().addAll(
            title,
            new Label("Username:"),
            usernameField,
            new Label("Password:"),
            passwordField,
            buttonBox,
            messageLabel,
            credentialsLabel
        );

        // Login button action
        loginBtn.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter both username and password");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            boolean isValid = (username.equals("admin") && password.equals("clinic123")) ||
                             (username.equals("staff") && password.equals("staff123"));

            if (isValid) {
                messageLabel.setText("Login successful! Loading appointment system...");
                messageLabel.setStyle("-fx-text-fill: green;");
                showAppointmentScreen(stage, username);
            } else {
                messageLabel.setText("Invalid username or password");
                messageLabel.setStyle("-fx-text-fill: red;");
                passwordField.clear();
            }
        });

        // Clear button action
        clearBtn.setOnAction(e -> {
            usernameField.clear();
            passwordField.clear();
            messageLabel.setText("");
        });

        Scene loginScene = new Scene(loginBox, 450, 400);
        stage.setTitle("Clinic Database System - Login");
        stage.setScene(loginScene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    private void showAppointmentScreen(Stage stage, String loggedInUser) {
        VBox mainBox = new VBox(20);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setPadding(new Insets(20));
        mainBox.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa, #e9ecef);");

        Label title = new Label("🏥 CLINIC APPOINTMENT SYSTEM - DATABASE");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1565c0;");

        Label userLabel = new Label("👤 Logged in as: " + loggedInUser + " | 🗄️ Connected to PostgreSQL");
        userLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

        // Form fields
        TextField patientField = new TextField();
        patientField.setPromptText("Enter patient name");
        patientField.setPrefWidth(300);

        TextField phoneField = new TextField();
        phoneField.setPromptText("Enter patient phone (optional)");
        phoneField.setPrefWidth(300);

        ComboBox<String> doctorCombo = new ComboBox<>();
        doctorCombo.getItems().addAll(
            "Dr. Sarah Smith - General Practitioner",
            "Dr. John Johnson - Cardiologist",
            "Dr. Emily Williams - Pediatrician",
            "Dr. Michael Brown - Dermatologist",
            "Dr. Lisa Davis - Orthopedist",
            "Dr. David Wilson - Neurologist",
            "Dr. Jennifer Garcia - Gynecologist"
        );
        doctorCombo.setPromptText("Select a doctor");
        doctorCombo.setPrefWidth(300);

        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        datePicker.setPrefWidth(300);

        TextField timeField = new TextField();
        timeField.setPromptText("HH:MM (e.g., 14:30)");
        timeField.setPrefWidth(300);

        TextArea notesArea = new TextArea();
        notesArea.setPromptText("Additional notes (optional)");
        notesArea.setPrefWidth(300);
        notesArea.setPrefHeight(60);

        Button scheduleBtn = new Button("💾 SAVE TO DATABASE");
        scheduleBtn.setPrefWidth(200);
        scheduleBtn.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-weight: bold;");

        Button clearBtn = new Button("🗑️ CLEAR FORM");
        clearBtn.setPrefWidth(200);
        clearBtn.setStyle("-fx-background-color: #ff9800; -fx-text-fill: white; -fx-font-weight: bold;");

        Button refreshBtn = new Button("🔄 LOAD FROM DATABASE");
        refreshBtn.setPrefWidth(200);
        refreshBtn.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white; -fx-font-weight: bold;");

        Button logoutBtn = new Button("🚪 LOGOUT");
        logoutBtn.setPrefWidth(100);
        logoutBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");

        TextArea outputArea = new TextArea();
        outputArea.setPrefWidth(650);
        outputArea.setPrefHeight(250);
        outputArea.setEditable(false);
        outputArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px;");

        // Form layout
        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(15);
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setStyle("-fx-background-color: white; -fx-padding: 20px; -fx-background-radius: 10px;");

        formGrid.add(new Label("👤 Patient Name:"), 0, 0);
        formGrid.add(patientField, 1, 0);
        formGrid.add(new Label("📞 Phone:"), 0, 1);
        formGrid.add(phoneField, 1, 1);
        formGrid.add(new Label("👨‍⚕️ Doctor:"), 0, 2);
        formGrid.add(doctorCombo, 1, 2);
        formGrid.add(new Label("📅 Date:"), 0, 3);
        formGrid.add(datePicker, 1, 3);
        formGrid.add(new Label("🕐 Time:"), 0, 4);
        formGrid.add(timeField, 1, 4);
        formGrid.add(new Label("📝 Notes:"), 0, 5);
        formGrid.add(notesArea, 1, 5);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(scheduleBtn, clearBtn, refreshBtn, logoutBtn);

        // Load appointments on startup
        loadAppointmentsFromDatabase(outputArea);

        // Schedule button action - SAVES TO DATABASE
        scheduleBtn.setOnAction(e -> {
            String patient = patientField.getText().trim();
            String phone = phoneField.getText().trim();
            String doctor = doctorCombo.getValue();
            LocalDate date = datePicker.getValue();
            String time = timeField.getText().trim();
            String notes = notesArea.getText().trim();

            if (patient.isEmpty() || doctor == null || date == null || time.isEmpty()) {
                outputArea.setText("❌ Please fill in required fields: Patient Name, Doctor, Date, and Time!");
                return;
            }

            if (!time.matches("\\d{1,2}:\\d{2}")) {
                outputArea.setText("❌ Please enter time in HH:MM format!");
                return;
            }

            // Save to PostgreSQL database
            if (saveAppointmentToDatabase(patient, phone, doctor, date, time, notes)) {
                outputArea.setText("✅ APPOINTMENT SAVED TO DATABASE SUCCESSFULLY!\n\n" +
                                 "💾 Saved to PostgreSQL:\n" +
                                 "👤 Patient: " + patient + "\n" +
                                 "📞 Phone: " + (phone.isEmpty() ? "Not provided" : phone) + "\n" +
                                 "👨‍⚕️ Doctor: " + doctor + "\n" +
                                 "📅 Date: " + date + "\n" +
                                 "🕐 Time: " + time + "\n" +
                                 "📝 Notes: " + (notes.isEmpty() ? "None" : notes) + "\n\n" +
                                 "🎉 Data is now permanently stored in your database!\n" +
                                 "Click 'LOAD FROM DATABASE' to see all appointments.");

                // Clear form
                patientField.clear();
                phoneField.clear();
                doctorCombo.setValue(null);
                datePicker.setValue(LocalDate.now());
                timeField.clear();
                notesArea.clear();
            } else {
                outputArea.setText("❌ ERROR: Failed to save appointment to database!\n" +
                                 "Check console for error details.");
            }
        });

        // Clear button action
        clearBtn.setOnAction(e -> {
            patientField.clear();
            phoneField.clear();
            doctorCombo.setValue(null);
            datePicker.setValue(LocalDate.now());
            timeField.clear();
            notesArea.clear();
            outputArea.setText("🗑️ Form cleared. Ready for new appointment.\n\n");
        });

        // Refresh button action - LOADS FROM DATABASE
        refreshBtn.setOnAction(e -> loadAppointmentsFromDatabase(outputArea));

        // Logout button action
        logoutBtn.setOnAction(e -> showLoginScreen(stage));

        mainBox.getChildren().addAll(
            title,
            userLabel,
            formGrid,
            buttonBox,
            outputArea
        );

        Scene appointmentScene = new Scene(mainBox, 750, 650);
        stage.setTitle("Clinic System - Database Connected (PostgreSQL)");
        stage.setScene(appointmentScene);
        stage.centerOnScreen();
    }

    private boolean saveAppointmentToDatabase(String patient, String phone, String doctor, LocalDate date, String time, String notes) {
        System.out.println("💾 Saving appointment to PostgreSQL database...");
        
        try (Connection conn = getConnection()) {
            String query = "INSERT INTO appointments (patient_name, patient_phone, doctor_name, appointment_date, appointment_time, notes) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, patient);
            stmt.setString(2, phone.isEmpty() ? null : phone);
            stmt.setString(3, doctor);
            stmt.setDate(4, Date.valueOf(date));
            stmt.setTime(5, Time.valueOf(LocalTime.parse(time)));
            stmt.setString(6, notes.isEmpty() ? null : notes);

            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✅ Appointment saved successfully to database!");
                return true;
            } else {
                System.out.println("❌ No rows were inserted");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("❌ Database error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void loadAppointmentsFromDatabase(TextArea outputArea) {
        System.out.println("📋 Loading appointments from PostgreSQL database...");
        
        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM appointments ORDER BY created_at DESC LIMIT 10";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            StringBuilder output = new StringBuilder();
            output.append("=== APPOINTMENTS FROM POSTGRESQL DATABASE ===\n\n");

            int count = 0;
            while (rs.next()) {
                count++;
                output.append(String.format("💾 Database Record #%d\n", rs.getInt("id")));
                output.append(String.format("👤 Patient: %s\n", rs.getString("patient_name")));
                output.append(String.format("📞 Phone: %s\n", rs.getString("patient_phone") != null ? rs.getString("patient_phone") : "Not provided"));
                output.append(String.format("👨‍⚕️ Doctor: %s\n", rs.getString("doctor_name")));
                output.append(String.format("📅 Date: %s\n", rs.getDate("appointment_date")));
                output.append(String.format("🕐 Time: %s\n", rs.getTime("appointment_time").toString().substring(0, 5)));
                output.append(String.format("📝 Status: %s\n", rs.getString("status")));
                String notes = rs.getString("notes");
                if (notes != null && !notes.trim().isEmpty()) {
                    output.append(String.format("💬 Notes: %s\n", notes));
                }
                Timestamp created = rs.getTimestamp("created_at");
                if (created != null) {
                    output.append(String.format("⏰ Created: %s\n", created.toString()));
                }
                output.append("─".repeat(50) + "\n\n");
            }

            if (count == 0) {
                output.append("No appointments found in database.\n");
                output.append("Schedule your first appointment using the form above!\n\n");
                output.append("🗄️ Database connection is working - ready to save appointments!");
            } else {
                output.append(String.format("📊 Showing %d most recent appointments from database.\n", count));
                output.append("🎉 All data is permanently stored in PostgreSQL!");
            }

            outputArea.setText(output.toString());
            System.out.println("✅ Loaded " + count + " appointments from database");

        } catch (SQLException e) {
            outputArea.setText("❌ Error loading appointments from database:\n" + e.getMessage() + 
                             "\n\nPlease check your database connection.");
            System.err.println("❌ Database error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
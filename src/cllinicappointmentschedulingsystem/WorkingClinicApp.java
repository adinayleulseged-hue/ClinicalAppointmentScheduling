package cllinicappointmentschedulingsystem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WorkingClinicApp extends Application {
    
    // In-memory storage for appointments (will persist during app session)
    private List<AppointmentRecord> appointments = new ArrayList<>();
    
    // Simple appointment record class
    private static class AppointmentRecord {
        String patient, phone, doctor, date, time, notes, status;
        LocalDateTime created;
        
        AppointmentRecord(String patient, String phone, String doctor, String date, String time, String notes) {
            this.patient = patient;
            this.phone = phone;
            this.doctor = doctor;
            this.date = date;
            this.time = time;
            this.notes = notes;
            this.status = "SCHEDULED";
            this.created = LocalDateTime.now();
        }
        
        @Override
        public String toString() {
            return String.format("📋 Appointment\n👤 Patient: %s\n📞 Phone: %s\n👨‍⚕️ Doctor: %s\n📅 Date: %s\n🕐 Time: %s\n📝 Status: %s\n💬 Notes: %s\n⏰ Created: %s\n%s\n",
                patient, 
                phone.isEmpty() ? "Not provided" : phone,
                doctor, 
                date, 
                time, 
                status,
                notes.isEmpty() ? "None" : notes,
                created.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                "─".repeat(50)
            );
        }
    }
    
    @Override
    public void start(Stage primaryStage) {
        showLoginScreen(primaryStage);
    }

    private void showLoginScreen(Stage stage) {
        VBox loginBox = new VBox(20);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(30));
        loginBox.setStyle("-fx-background-color: linear-gradient(to bottom, #e3f2fd, #bbdefb);");

        Label title = new Label("🏥 CLINIC APPOINTMENT SYSTEM");
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

        Label messageLabel = new Label("✅ System Ready - No Database Required");
        messageLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");

        Label credentialsLabel = new Label("📋 Login Credentials:\nAdmin: admin / clinic123\nStaff: staff / staff123");
        credentialsLabel.setStyle("-fx-text-alignment: center; -fx-font-size: 12px;");

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
            messageLabel.setText("✅ System Ready - No Database Required");
            messageLabel.setStyle("-fx-text-fill: green;");
        });

        Scene loginScene = new Scene(loginBox, 450, 400);
        stage.setTitle("Clinic System - Login");
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

        Label title = new Label("🏥 CLINIC APPOINTMENT SYSTEM");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1565c0;");

        Label userLabel = new Label("👤 Logged in as: " + loggedInUser);
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

        Button scheduleBtn = new Button("📋 SCHEDULE APPOINTMENT");
        scheduleBtn.setPrefWidth(200);
        scheduleBtn.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-weight: bold;");

        Button clearBtn = new Button("🗑️ CLEAR FORM");
        clearBtn.setPrefWidth(200);
        clearBtn.setStyle("-fx-background-color: #ff9800; -fx-text-fill: white; -fx-font-weight: bold;");

        Button refreshBtn = new Button("🔄 REFRESH APPOINTMENTS");
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
        loadAppointments(outputArea);

        // Schedule button action
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

            // Save appointment to memory
            AppointmentRecord appointment = new AppointmentRecord(patient, phone, doctor, date.toString(), time, notes);
            appointments.add(appointment);

            outputArea.setText("✅ APPOINTMENT SCHEDULED SUCCESSFULLY!\n\n" +
                             "📋 Details saved to system:\n" +
                             "👤 Patient: " + patient + "\n" +
                             "📞 Phone: " + (phone.isEmpty() ? "Not provided" : phone) + "\n" +
                             "👨‍⚕️ Doctor: " + doctor + "\n" +
                             "📅 Date: " + date + "\n" +
                             "🕐 Time: " + time + "\n" +
                             "📝 Notes: " + (notes.isEmpty() ? "None" : notes) + "\n" +
                             "📊 Total Appointments: " + appointments.size() + "\n\n" +
                             "Click 'REFRESH APPOINTMENTS' to see all appointments.");

            // Clear form
            patientField.clear();
            phoneField.clear();
            doctorCombo.setValue(null);
            datePicker.setValue(LocalDate.now());
            timeField.clear();
            notesArea.clear();
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

        // Refresh button action
        refreshBtn.setOnAction(e -> loadAppointments(outputArea));

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
        stage.setTitle("Clinic Appointment System - Working Version");
        stage.setScene(appointmentScene);
        stage.centerOnScreen();
    }

    private void loadAppointments(TextArea outputArea) {
        if (appointments.isEmpty()) {
            outputArea.setText("=== CLINIC APPOINTMENT SYSTEM ===\n\n" +
                             "No appointments scheduled yet.\n" +
                             "Use the form above to schedule your first appointment!\n\n" +
                             "✅ System is working perfectly!\n" +
                             "📊 Total Appointments: 0");
            return;
        }

        StringBuilder output = new StringBuilder();
        output.append("=== SCHEDULED APPOINTMENTS ===\n\n");
        output.append(String.format("📊 Total Appointments: %d\n\n", appointments.size()));

        for (int i = appointments.size() - 1; i >= Math.max(0, appointments.size() - 5); i--) {
            output.append(appointments.get(i).toString());
        }

        if (appointments.size() > 5) {
            output.append(String.format("... and %d more appointments\n", appointments.size() - 5));
        }

        outputArea.setText(output.toString());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
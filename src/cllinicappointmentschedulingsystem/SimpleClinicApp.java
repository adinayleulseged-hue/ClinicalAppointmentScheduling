package cllinicappointmentschedulingsystem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;

public class SimpleClinicApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        showLoginScreen(primaryStage);
    }

    private void showLoginScreen(Stage stage) {
        VBox loginBox = new VBox(20);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(30));
        loginBox.setStyle("-fx-background-color: #f0f8ff;");

        Label title = new Label("🏥 CLINIC LOGIN SYSTEM");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setPrefWidth(200);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setPrefWidth(200);

        Button loginBtn = new Button("LOGIN");
        loginBtn.setPrefWidth(100);

        Button clearBtn = new Button("CLEAR");
        clearBtn.setPrefWidth(100);

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        Label credentialsLabel = new Label("Credentials:\nadmin / clinic123\nstaff / staff123");
        credentialsLabel.setStyle("-fx-text-alignment: center;");

        HBox buttonBox = new HBox(10);
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
                return;
            }

            boolean isValid = (username.equals("admin") && password.equals("clinic123")) ||
                             (username.equals("staff") && password.equals("staff123"));

            if (isValid) {
                messageLabel.setText("Login successful!");
                messageLabel.setStyle("-fx-text-fill: green;");
                showAppointmentScreen(stage);
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

        Scene loginScene = new Scene(loginBox, 400, 350);
        stage.setTitle("Clinic Login System");
        stage.setScene(loginScene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    private void showAppointmentScreen(Stage stage) {
        VBox mainBox = new VBox(15);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setPadding(new Insets(20));
        mainBox.setStyle("-fx-background-color: #f5f5f5;");

        Label title = new Label("🏥 APPOINTMENT SCHEDULING SYSTEM");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Form fields
        TextField patientField = new TextField();
        patientField.setPromptText("Enter patient name");
        patientField.setPrefWidth(250);

        ComboBox<String> doctorCombo = new ComboBox<>();
        doctorCombo.getItems().addAll(
            "Dr. Smith - General Medicine",
            "Dr. Johnson - Cardiology",
            "Dr. Williams - Pediatrics",
            "Dr. Brown - Orthopedics",
            "Dr. Davis - Dermatology"
        );
        doctorCombo.setPromptText("Select a doctor");
        doctorCombo.setPrefWidth(250);

        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        datePicker.setPrefWidth(250);

        TextField timeField = new TextField();
        timeField.setPromptText("HH:MM (e.g., 14:30)");
        timeField.setPrefWidth(250);

        Button scheduleBtn = new Button("SCHEDULE APPOINTMENT");
        scheduleBtn.setPrefWidth(200);

        Button clearBtn = new Button("CLEAR FORM");
        clearBtn.setPrefWidth(200);

        TextArea outputArea = new TextArea();
        outputArea.setPrefWidth(500);
        outputArea.setPrefHeight(200);
        outputArea.setEditable(false);
        outputArea.setText("=== CLINIC APPOINTMENT SYSTEM ===\n\n" +
                          "Welcome! Fill in the form above to schedule appointments.\n\n" +
                          "Successfully logged in!\n\n");

        // Form layout
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setAlignment(Pos.CENTER);

        formGrid.add(new Label("Patient Name:"), 0, 0);
        formGrid.add(patientField, 1, 0);
        formGrid.add(new Label("Doctor:"), 0, 1);
        formGrid.add(doctorCombo, 1, 1);
        formGrid.add(new Label("Date:"), 0, 2);
        formGrid.add(datePicker, 1, 2);
        formGrid.add(new Label("Time:"), 0, 3);
        formGrid.add(timeField, 1, 3);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(scheduleBtn, clearBtn);

        // Schedule button action
        scheduleBtn.setOnAction(e -> {
            String patient = patientField.getText().trim();
            String doctor = doctorCombo.getValue();
            LocalDate date = datePicker.getValue();
            String time = timeField.getText().trim();

            if (patient.isEmpty() || doctor == null || date == null || time.isEmpty()) {
                outputArea.setText("❌ Please fill in all fields!");
                return;
            }

            if (!time.matches("\\d{1,2}:\\d{2}")) {
                outputArea.setText("❌ Please enter time in HH:MM format!");
                return;
            }

            String appointment = String.format(
                "✅ APPOINTMENT SCHEDULED SUCCESSFULLY!\n\n" +
                "📋 Details:\n" +
                "👤 Patient: %s\n" +
                "👨‍⚕️ Doctor: %s\n" +
                "📅 Date: %s\n" +
                "🕐 Time: %s\n" +
                "📝 Status: Confirmed\n\n" +
                "Thank you for using our clinic system!",
                patient, doctor, date, time
            );

            outputArea.setText(appointment);

            // Clear form
            patientField.clear();
            doctorCombo.setValue(null);
            datePicker.setValue(LocalDate.now());
            timeField.clear();
        });

        // Clear button action
        clearBtn.setOnAction(e -> {
            patientField.clear();
            doctorCombo.setValue(null);
            datePicker.setValue(LocalDate.now());
            timeField.clear();
            outputArea.setText("=== CLINIC APPOINTMENT SYSTEM ===\n\n" +
                              "Form cleared. Ready for new appointment.\n\n");
        });

        mainBox.getChildren().addAll(
            title,
            formGrid,
            buttonBox,
            outputArea
        );

        Scene appointmentScene = new Scene(mainBox, 600, 500);
        stage.setTitle("Clinic Appointment Scheduling System");
        stage.setScene(appointmentScene);
        stage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
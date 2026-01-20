package cllinicappointmentschedulingsystem;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.time.LocalDate;

public class ClinicAppointmentController {

    @FXML
    private TextField txtPatient;

    @FXML
    private TextField txtTime;

    @FXML
    private ComboBox<String> cmbDoctor;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextArea txtOutput;

    // Runs automatically when FXML loads
    @FXML
    private void initialize() {
        // Load doctors into dropdown
        cmbDoctor.getItems().addAll(
            "Dr. Smith - General Medicine",
            "Dr. Johnson - Cardiology", 
            "Dr. Williams - Pediatrics",
            "Dr. Brown - Orthopedics",
            "Dr. Davis - Dermatology"
        );
        
        datePicker.setValue(LocalDate.now());
        
        // Welcome message
        txtOutput.setText("=== CLINIC APPOINTMENT SYSTEM ===\n\n" +
                         "Welcome! You can schedule appointments here.\n" +
                         "Fill in the patient details and click 'Schedule Appointment'.\n\n" +
                         "Login Credentials Used:\n" +
                         "- admin / clinic123\n" +
                         "- staff / staff123\n\n");
    }

    // Schedule appointment button
    @FXML
    private void handleScheduleAppointment() {

        String patient = txtPatient.getText().trim();
        String time = txtTime.getText().trim();
        String doctor = cmbDoctor.getValue();
        LocalDate date = datePicker.getValue();

        if (patient.isEmpty() || time.isEmpty() || doctor == null || date == null) {
            txtOutput.setText("❌ Please fill in all fields!");
            return;
        }

        // Validate time format (basic check)
        if (!time.matches("\\d{1,2}:\\d{2}")) {
            txtOutput.setText("❌ Please enter time in HH:MM format (e.g., 14:30)!");
            return;
        }

        // Create appointment record
        String appointment = String.format(
            "✅ APPOINTMENT SCHEDULED SUCCESSFULLY!\n\n" +
            "📋 Appointment Details:\n" +
            "👤 Patient: %s\n" +
            "👨‍⚕️ Doctor: %s\n" +
            "📅 Date: %s\n" +
            "🕐 Time: %s\n" +
            "📝 Status: Confirmed\n\n" +
            "=== PREVIOUS APPOINTMENTS ===\n",
            patient, doctor, date, time
        );

        // Add to existing appointments
        String existingText = txtOutput.getText();
        if (existingText.contains("PREVIOUS APPOINTMENTS")) {
            // Replace the header and add new appointment
            String[] parts = existingText.split("=== PREVIOUS APPOINTMENTS ===\n");
            txtOutput.setText(appointment + parts[1] + 
                String.format("• %s - %s - %s at %s\n", patient, doctor, date, time));
        } else {
            txtOutput.setText(appointment + 
                String.format("• %s - %s - %s at %s\n", patient, doctor, date, time));
        }

        // Clear input fields
        txtPatient.clear();
        txtTime.clear();
        cmbDoctor.setValue(null);
        datePicker.setValue(LocalDate.now());
    }

    // Clear button
    @FXML
    private void handleClear() {
        txtPatient.clear();
        txtTime.clear();
        cmbDoctor.setValue(null);
        datePicker.setValue(LocalDate.now());
        
        // Reset to welcome message
        txtOutput.setText("=== CLINIC APPOINTMENT SYSTEM ===\n\n" +
                         "Welcome! You can schedule appointments here.\n" +
                         "Fill in the patient details and click 'Schedule Appointment'.\n\n");
    }
}

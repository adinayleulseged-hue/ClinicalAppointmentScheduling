package clinicappointmentschedulingsystem;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.time.LocalDate;
import java.util.List;

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

    private SimpleDataStore dataStore;

    // Runs automatically when FXML loads
    @FXML
    private void initialize() {
        dataStore = SimpleDataStore.getInstance();
        
        // Load doctors from data store
        List<String> doctors = dataStore.getAllDoctors();
        cmbDoctor.getItems().addAll(doctors);
        
        datePicker.setValue(LocalDate.now());
        
        // Load existing appointments
        loadAppointments();
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
            txtOutput.setText("❌ Please enter time in HH:MM format!");
            return;
        }

        // Check for appointment conflicts
        if (dataStore.hasConflict(doctor, date, time)) {
            txtOutput.setText("❌ Appointment conflict! " + doctor + " is already booked at " + time + " on " + date);
            return;
        }

        // Save appointment to data store
        if (dataStore.saveAppointment(patient, doctor, date, time)) {
            txtOutput.setText("✅ Appointment scheduled successfully!\n");
            loadAppointments(); // Refresh the appointments list
            
            // Clear input fields
            txtPatient.clear();
            txtTime.clear();
            cmbDoctor.setValue(null);
            datePicker.setValue(LocalDate.now());
        } else {
            txtOutput.setText("❌ Error saving appointment. Please try again.");
        }
    }

    // Clear button
    @FXML
    private void handleClear() {
        txtPatient.clear();
        txtTime.clear();
        cmbDoctor.setValue(null);
        datePicker.setValue(LocalDate.now());
        txtOutput.clear();
    }
    
    // Load all appointments from data store
    private void loadAppointments() {
        List<SimpleAppointment> appointments = dataStore.getAllAppointments();
        StringBuilder output = new StringBuilder();
        
        if (appointments.isEmpty()) {
            output.append("No appointments scheduled yet.\n");
        } else {
            output.append("=== SCHEDULED APPOINTMENTS ===\n\n");
            for (SimpleAppointment appointment : appointments) {
                output.append(appointment.toString()).append("\n");
            }
        }
        
        txtOutput.setText(output.toString());
    }
}
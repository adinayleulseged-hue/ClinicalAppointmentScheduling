package clinicappointmentschedulingsystem;

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
        cmbDoctor.getItems().addAll(
            "Dr. Smith",
            "Dr. Johnson",
            "Dr. Williams",
            "Dr. Brown"
        );
        datePicker.setValue(LocalDate.now());
    }

    // Schedule appointment button
    @FXML
    private void handleScheduleAppointment() {

        String patient = txtPatient.getText();
        String time = txtTime.getText();
        String doctor = cmbDoctor.getValue();
        LocalDate date = datePicker.getValue();

        if (patient.isEmpty() || time.isEmpty() || doctor == null || date == null) {
            txtOutput.setText("❌ Please fill in all fields!");
            return;
        }

        String appointment =
            "✅ Appointment Scheduled\n" +
            "Patient : " + patient + "\n" +
            "Doctor  : " + doctor + "\n" +
            "Date    : " + date + "\n" +
            "Time    : " + time + "\n\n";

        txtOutput.appendText(appointment);

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
        txtOutput.clear();
    }
}

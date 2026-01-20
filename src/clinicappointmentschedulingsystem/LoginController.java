package clinicappointmentschedulingsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblMessage;

    @FXML
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Please enter both username and password");
            return;
        }

        // Authenticate using simple data store
        SimpleDataStore dataStore = SimpleDataStore.getInstance();
        if (dataStore.authenticateUser(username, password)) {
            lblMessage.setText("Login successful! Loading main application...");
            lblMessage.setStyle("-fx-text-fill: green;");
            
            // Load main application
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
                Parent root = loader.load();
                
                Stage stage = (Stage) txtUsername.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Clinic Appointment Scheduling System");
                stage.centerOnScreen();
                
            } catch (Exception e) {
                lblMessage.setText("Error loading main application: " + e.getMessage());
                lblMessage.setStyle("-fx-text-fill: red;");
            }
        } else {
            lblMessage.setText("Invalid username or password");
            lblMessage.setStyle("-fx-text-fill: red;");
            txtPassword.clear();
        }
    }

    @FXML
    private void handleClear() {
        txtUsername.clear();
        txtPassword.clear();
        lblMessage.setText("");
    }
}
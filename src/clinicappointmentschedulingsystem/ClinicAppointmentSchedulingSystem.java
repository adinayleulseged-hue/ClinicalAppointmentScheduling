package clinicappointmentschedulingsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClinicAppointmentSchedulingSystem extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(
                getClass().getResource("FXMLDocument.fxml")
        );

        Scene scene = new Scene(root);

        stage.setTitle("Clinic Appointment Scheduling System");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


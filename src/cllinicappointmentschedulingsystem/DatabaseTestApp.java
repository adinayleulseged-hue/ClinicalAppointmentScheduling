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

public class DatabaseTestApp extends Application {
    
    // ⚠️ UPDATE THESE DATABASE SETTINGS FOR YOUR SYSTEM ⚠️
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "5432";
    private static final String DB_NAME = "clinic_db";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "1234";
    
    private static final String DB_URL = "jdbc:postgresql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
    
    @Override
    public void start(Stage primaryStage) {
        showDatabaseTestScreen(primaryStage);
    }

    private void showDatabaseTestScreen(Stage stage) {
        VBox mainBox = new VBox(20);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setPadding(new Insets(30));
        mainBox.setStyle("-fx-background-color: #f0f8ff;");

        Label title = new Label("🔧 DATABASE CONNECTION TEST");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1565c0;");

        // Database settings display
        VBox settingsBox = new VBox(10);
        settingsBox.setStyle("-fx-background-color: white; -fx-padding: 15px; -fx-border-color: #ccc; -fx-border-radius: 5px;");
        settingsBox.getChildren().addAll(
            new Label("📊 Current Database Settings:"),
            new Label("🖥️ Host: " + DB_HOST),
            new Label("🔌 Port: " + DB_PORT),
            new Label("🗄️ Database: " + DB_NAME),
            new Label("👤 Username: " + DB_USER),
            new Label("🔑 Password: " + (DB_PASSWORD.isEmpty() ? "EMPTY" : "*".repeat(DB_PASSWORD.length())))
        );

        Button testConnectionBtn = new Button("🔍 TEST DATABASE CONNECTION");
        testConnectionBtn.setPrefWidth(250);
        testConnectionBtn.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white; -fx-font-weight: bold;");

        Button testTablesBtn = new Button("📋 CHECK TABLES");
        testTablesBtn.setPrefWidth(250);
        testTablesBtn.setStyle("-fx-background-color: #ff9800; -fx-text-fill: white; -fx-font-weight: bold;");

        Button insertTestBtn = new Button("💾 INSERT TEST APPOINTMENT");
        insertTestBtn.setPrefWidth(250);
        insertTestBtn.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-weight: bold;");

        Button viewDataBtn = new Button("👁️ VIEW APPOINTMENTS");
        viewDataBtn.setPrefWidth(250);
        viewDataBtn.setStyle("-fx-background-color: #9c27b0; -fx-text-fill: white; -fx-font-weight: bold;");

        TextArea resultArea = new TextArea();
        resultArea.setPrefWidth(600);
        resultArea.setPrefHeight(300);
        resultArea.setEditable(false);
        resultArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px;");
        resultArea.setText("Click buttons above to test database connection and operations...\n\n" +
                          "⚠️ IMPORTANT: Update database settings in DatabaseTestApp.java first!\n" +
                          "   - DB_USER: Your PostgreSQL username\n" +
                          "   - DB_PASSWORD: Your PostgreSQL password\n" +
                          "   - DB_HOST: Usually 'localhost'\n" +
                          "   - DB_PORT: Usually '5432'\n" +
                          "   - DB_NAME: Your database name\n\n");

        // Test connection button
        testConnectionBtn.setOnAction(e -> {
            resultArea.setText("🔍 Testing database connection...\n\n");
            
            try {
                // Load PostgreSQL driver
                Class.forName("org.postgresql.Driver");
                resultArea.appendText("✅ PostgreSQL JDBC Driver loaded successfully\n");
                
                // Test connection
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                resultArea.appendText("✅ Database connection successful!\n");
                resultArea.appendText("📊 Connected to: " + DB_URL + "\n");
                resultArea.appendText("👤 User: " + DB_USER + "\n\n");
                
                // Get database info
                DatabaseMetaData metaData = conn.getMetaData();
                resultArea.appendText("🗄️ Database Info:\n");
                resultArea.appendText("   Product: " + metaData.getDatabaseProductName() + "\n");
                resultArea.appendText("   Version: " + metaData.getDatabaseProductVersion() + "\n\n");
                
                conn.close();
                resultArea.appendText("✅ Connection test completed successfully!\n");
                
            } catch (ClassNotFoundException ex) {
                resultArea.appendText("❌ PostgreSQL JDBC Driver not found!\n");
                resultArea.appendText("   Solution: Add postgresql-xx.x.x.jar to your project libraries\n");
                resultArea.appendText("   Error: " + ex.getMessage() + "\n");
            } catch (SQLException ex) {
                resultArea.appendText("❌ Database connection failed!\n");
                resultArea.appendText("   SQL State: " + ex.getSQLState() + "\n");
                resultArea.appendText("   Error Code: " + ex.getErrorCode() + "\n");
                resultArea.appendText("   Message: " + ex.getMessage() + "\n\n");
                resultArea.appendText("💡 Common solutions:\n");
                resultArea.appendText("   1. Check if PostgreSQL server is running\n");
                resultArea.appendText("   2. Verify database name exists\n");
                resultArea.appendText("   3. Check username and password\n");
                resultArea.appendText("   4. Ensure database accepts connections\n");
            }
        });

        // Check tables button
        testTablesBtn.setOnAction(e -> {
            resultArea.setText("📋 Checking database tables...\n\n");
            
            try {
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                
                // Check if appointments table exists
                DatabaseMetaData metaData = conn.getMetaData();
                ResultSet tables = metaData.getTables(null, null, "appointments", null);
                
                if (tables.next()) {
                    resultArea.appendText("✅ 'appointments' table found!\n\n");
                    
                    // Get table structure
                    ResultSet columns = metaData.getColumns(null, null, "appointments", null);
                    resultArea.appendText("📊 Table structure:\n");
                    while (columns.next()) {
                        String columnName = columns.getString("COLUMN_NAME");
                        String dataType = columns.getString("TYPE_NAME");
                        String nullable = columns.getString("IS_NULLABLE");
                        resultArea.appendText(String.format("   %s (%s) %s\n", 
                            columnName, dataType, nullable.equals("YES") ? "NULL" : "NOT NULL"));
                    }
                    
                    // Count existing records
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM appointments");
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        resultArea.appendText("\n📈 Current appointments in database: " + count + "\n");
                    }
                    
                } else {
                    resultArea.appendText("❌ 'appointments' table not found!\n\n");
                    resultArea.appendText("💡 Create the table with this SQL:\n");
                    resultArea.appendText("CREATE TABLE appointments (\n");
                    resultArea.appendText("    id SERIAL PRIMARY KEY,\n");
                    resultArea.appendText("    patient_name VARCHAR(100) NOT NULL,\n");
                    resultArea.appendText("    patient_phone VARCHAR(20),\n");
                    resultArea.appendText("    doctor_name VARCHAR(100) NOT NULL,\n");
                    resultArea.appendText("    appointment_date DATE NOT NULL,\n");
                    resultArea.appendText("    appointment_time TIME NOT NULL,\n");
                    resultArea.appendText("    status VARCHAR(20) DEFAULT 'SCHEDULED',\n");
                    resultArea.appendText("    notes TEXT\n");
                    resultArea.appendText(");\n");
                }
                
                conn.close();
                
            } catch (SQLException ex) {
                resultArea.appendText("❌ Error checking tables: " + ex.getMessage() + "\n");
            }
        });

        // Insert test appointment button
        insertTestBtn.setOnAction(e -> {
            resultArea.setText("💾 Inserting test appointment...\n\n");
            
            try {
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                
                String query = "INSERT INTO appointments (patient_name, patient_phone, doctor_name, appointment_date, appointment_time, notes) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                
                stmt.setString(1, "Test Patient");
                stmt.setString(2, "555-0123");
                stmt.setString(3, "Dr. Test Doctor");
                stmt.setDate(4, Date.valueOf(LocalDate.now()));
                stmt.setTime(5, Time.valueOf(LocalTime.of(14, 30)));
                stmt.setString(6, "Test appointment from Java application");
                
                int rowsAffected = stmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    resultArea.appendText("✅ Test appointment inserted successfully!\n");
                    resultArea.appendText("📋 Details:\n");
                    resultArea.appendText("   Patient: Test Patient\n");
                    resultArea.appendText("   Phone: 555-0123\n");
                    resultArea.appendText("   Doctor: Dr. Test Doctor\n");
                    resultArea.appendText("   Date: " + LocalDate.now() + "\n");
                    resultArea.appendText("   Time: 14:30\n");
                    resultArea.appendText("   Notes: Test appointment from Java application\n\n");
                    resultArea.appendText("🎉 Database INSERT is working!\n");
                } else {
                    resultArea.appendText("❌ No rows were inserted\n");
                }
                
                conn.close();
                
            } catch (SQLException ex) {
                resultArea.appendText("❌ Error inserting test appointment:\n");
                resultArea.appendText("   SQL State: " + ex.getSQLState() + "\n");
                resultArea.appendText("   Error Code: " + ex.getErrorCode() + "\n");
                resultArea.appendText("   Message: " + ex.getMessage() + "\n");
            }
        });

        // View appointments button
        viewDataBtn.setOnAction(e -> {
            resultArea.setText("👁️ Loading appointments from database...\n\n");
            
            try {
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM appointments ORDER BY id DESC LIMIT 10");
                
                resultArea.appendText("📋 Recent Appointments:\n");
                resultArea.appendText("=" .repeat(60) + "\n");
                
                int count = 0;
                while (rs.next()) {
                    count++;
                    resultArea.appendText(String.format("ID: %d\n", rs.getInt("id")));
                    resultArea.appendText(String.format("Patient: %s\n", rs.getString("patient_name")));
                    resultArea.appendText(String.format("Phone: %s\n", rs.getString("patient_phone")));
                    resultArea.appendText(String.format("Doctor: %s\n", rs.getString("doctor_name")));
                    resultArea.appendText(String.format("Date: %s\n", rs.getDate("appointment_date")));
                    resultArea.appendText(String.format("Time: %s\n", rs.getTime("appointment_time")));
                    resultArea.appendText(String.format("Status: %s\n", rs.getString("status")));
                    resultArea.appendText(String.format("Notes: %s\n", rs.getString("notes")));
                    resultArea.appendText("-".repeat(40) + "\n");
                }
                
                if (count == 0) {
                    resultArea.appendText("No appointments found in database.\n");
                } else {
                    resultArea.appendText(String.format("\n✅ Found %d appointments in database\n", count));
                }
                
                conn.close();
                
            } catch (SQLException ex) {
                resultArea.appendText("❌ Error loading appointments: " + ex.getMessage() + "\n");
            }
        });

        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(testConnectionBtn, testTablesBtn, insertTestBtn, viewDataBtn);

        mainBox.getChildren().addAll(title, settingsBox, buttonBox, resultArea);

        Scene scene = new Scene(mainBox, 700, 600);
        stage.setTitle("Database Connection Test Tool");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
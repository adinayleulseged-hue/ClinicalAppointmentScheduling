package clinicappointmentschedulingsystem;

import java.time.LocalDate;

public class SimpleAppointment {
    private int id;
    private String patientName;
    private String doctorName;
    private LocalDate appointmentDate;
    private String appointmentTime;
    private String status;
    
    public SimpleAppointment(int id, String patientName, String doctorName, 
                           LocalDate appointmentDate, String appointmentTime, String status) {
        this.id = id;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }
    
    // Getters
    public int getId() { return id; }
    public String getPatientName() { return patientName; }
    public String getDoctorName() { return doctorName; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public String getAppointmentTime() { return appointmentTime; }
    public String getStatus() { return status; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }
    public void setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; }
    public void setStatus(String status) { this.status = status; }
    
    @Override
    public String toString() {
        return String.format("✅ Appointment #%d\nPatient: %s\nDoctor: %s\nDate: %s\nTime: %s\nStatus: %s\n",
                id, patientName, doctorName, appointmentDate, appointmentTime, status);
    }
}
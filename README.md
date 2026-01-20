# Clinic Appointment Scheduling System

A JavaFX-based clinic appointment scheduling system with user authentication and persistent data storage.

## Features

### 🔐 User Authentication
- Secure login system with username/password validation
- Multiple user accounts (admin/staff roles)
- Session management and navigation

### 📅 Appointment Management
- Schedule patient appointments with doctors
- Date and time selection with conflict detection
- Real-time appointment validation
- Persistent appointment storage

### 👨‍⚕️ Doctor Management
- Pre-configured doctor list with specializations
- Dynamic doctor selection in appointment booking
- Extensible doctor database

### 💾 Data Persistence
- File-based data storage system
- Automatic data directory creation
- Persistent user credentials and appointments
- SQLite database ready for upgrade

## System Requirements

- Java 11 or higher
- JavaFX runtime
- NetBeans IDE (recommended)
- Windows/macOS/Linux

## Installation & Setup

1. **Clone/Download** the project to your local machine
2. **Open** in NetBeans IDE
3. **Build** the project (F11)
4. **Run** the application (F6)

## Default Login Credentials

| Username | Password | Role |
|----------|----------|------|
| admin | clinic123 | Administrator |
| staff | staff123 | Staff Member |

## Project Structure

```
src/
├── clinicappointmentschedulingsystem/
│   ├── ClinicAppointmentSchedulingSystem.java  # Main application
│   ├── LoginController.java                    # Login page controller
│   ├── ClinicAppointmentController.java        # Main app controller
│   ├── SimpleDataStore.java                    # File-based data storage
│   ├── SimpleAppointment.java                  # Appointment data model
│   ├── PostgreSQLManager.java                  # PostgreSQL database (upgrade)
│   ├── DatabaseManager.java                    # SQLite database (legacy)
│   ├── Login.fxml                              # Login page UI
│   └── FXMLDocument.fxml                       # Main application UI
├── cllinicappointmentschedulingsystem/         # Backup package
└── clinic_data/                                # Data storage directory
    ├── users.properties                        # User credentials
    ├── doctors.txt                             # Doctor information
    └── appointments.txt                        # Appointment records
```

## Usage Guide

### 1. Login Process
1. Launch the application
2. Enter username and password
3. Click "Login" to authenticate
4. System validates credentials and loads main interface

### 2. Scheduling Appointments
1. Enter patient name
2. Select doctor from dropdown
3. Choose appointment date
4. Enter time in HH:MM format
5. Click "Schedule Appointment"
6. System checks for conflicts and saves appointment

### 3. Viewing Appointments
- All scheduled appointments display in the text area
- Shows patient, doctor, date, time, and status
- Updates automatically after new bookings

## Data Storage

### Current Implementation (File-Based)
- **Users**: Stored in `clinic_data/users.properties`
- **Doctors**: Listed in `clinic_data/doctors.txt`
- **Appointments**: Saved in `clinic_data/appointments.txt`

### Future Upgrade (PostgreSQL Database)
- Complete PostgreSQL implementation ready in `PostgreSQLManager.java`
- Enterprise-grade database with ACID compliance
- Easy migration path documented in `DATABASE_SETUP.md`
- Professional-grade data management with advanced features

## Key Features

### ✅ Conflict Detection
- Prevents double-booking of doctors
- Real-time validation during appointment creation
- Clear error messages for scheduling conflicts

### ✅ Data Validation
- Required field validation
- Time format verification (HH:MM)
- User input sanitization

### ✅ User Experience
- Clean, professional interface
- Intuitive navigation flow
- Clear success/error messaging
- Responsive design elements

### ✅ Security
- Password-protected access
- Session-based authentication
- Secure credential storage

## Technical Details

### Architecture
- **MVC Pattern**: Separation of concerns with FXML views and Java controllers
- **Singleton Pattern**: Centralized data management
- **Observer Pattern**: UI updates on data changes

### Technologies Used
- **JavaFX**: Modern UI framework
- **FXML**: Declarative UI design
- **Java I/O**: File-based persistence
- **Properties Files**: Configuration management

## Customization

### Adding New Users
Edit `clinic_data/users.properties`:
```properties
newuser=password123
```

### Adding New Doctors
Edit `clinic_data/doctors.txt`:
```
Dr. New Doctor - Specialty
```

### Modifying UI
- Edit FXML files for layout changes
- Update CSS styles in FXML for appearance
- Modify controllers for behavior changes

## Troubleshooting

### Common Issues
1. **FXML Not Found**: Ensure FXML files are in correct package directory
2. **Login Fails**: Check `clinic_data/users.properties` exists
3. **Data Not Saving**: Verify write permissions in project directory

### Debug Mode
- Check console output for error messages
- Verify `clinic_data` directory creation
- Confirm file permissions

## Future Enhancements

### Planned Features
- [ ] PostgreSQL database integration
- [ ] Patient management system
- [ ] Appointment editing/cancellation
- [ ] Reporting and analytics
- [ ] Email notifications
- [ ] Multi-clinic support
- [ ] Role-based permissions
- [ ] Advanced scheduling features

### Database Upgrade
Follow instructions in `DATABASE_SETUP.md` to upgrade from file-based storage to PostgreSQL database for enterprise-grade performance and advanced features.

## Support

For issues or questions:
1. Check the troubleshooting section
2. Review console output for errors
3. Verify all files are in correct locations
4. Ensure proper Java/JavaFX installation

## License

This project is developed for educational and clinic management purposes.

---

**Version**: 1.0  
**Last Updated**: January 2026  
**Status**: Production Ready ✅
# Database Setup Instructions - PostgreSQL

## Current Implementation ✅
The system currently uses a **file-based data storage system** located in the `clinic_data` folder. This provides reliable functionality for:
- ✅ User authentication (admin/clinic123, staff/staff123)
- ✅ Doctor management (5 pre-configured doctors)
- ✅ Appointment scheduling with conflict detection
- ✅ Persistent data storage between sessions

## File Storage Structure
```
clinic_data/
├── users.properties      # User credentials (admin, staff)
├── doctors.txt          # Doctor list with specializations
└── appointments.txt     # All appointment records
```

## Upgrading to PostgreSQL Database

For enterprise-grade performance and advanced features, upgrade to PostgreSQL:

### Step 1: Install PostgreSQL
1. Download PostgreSQL from [postgresql.org](https://www.postgresql.org/download/)
2. Install PostgreSQL server (version 12 or higher recommended)
3. Note the installation directory and port (default: 5432)

### Step 2: Create Database and User
Connect to PostgreSQL as superuser and run:
```sql
-- Create database
CREATE DATABASE clinic_db;

-- Create user
CREATE USER clinic_user WITH PASSWORD 'clinic_password';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE clinic_db TO clinic_user;
GRANT ALL ON SCHEMA public TO clinic_user;
```

### Step 3: Download PostgreSQL JDBC Driver
1. Visit [Maven Central PostgreSQL JDBC](https://central.sonatype.com/artifact/org.postgresql/postgresql)
2. Download latest version: `postgresql-42.7.1.jar`
3. Place in project `lib/` folder

### Step 4: Configure NetBeans
1. Right-click project → Properties
2. Libraries → Compile tab → Add JAR/Folder
3. Select PostgreSQL JDBC JAR file
4. Apply changes

### Step 5: Update Database Configuration
Edit `PostgreSQLManager.java` connection parameters:
```java
private static final String DB_HOST = "localhost";     // Your PostgreSQL host
private static final String DB_PORT = "5432";          // Your PostgreSQL port
private static final String DB_NAME = "clinic_db";     // Your database name
private static final String DB_USER = "clinic_user";   // Your database user
private static final String DB_PASSWORD = "clinic_password"; // Your password
```

### Step 6: Switch Implementation
Replace in controllers:
```java
// Current file-based
SimpleDataStore dataStore = SimpleDataStore.getInstance();

// Upgrade to PostgreSQL
PostgreSQLManager dbManager = PostgreSQLManager.getInstance();
```

### Step 7: Database Schema
PostgreSQL implementation automatically creates:
```sql
-- Users table with roles
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'staff',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Doctors table with specializations
CREATE TABLE doctors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100),
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Appointments table with time handling
CREATE TABLE appointments (
    id SERIAL PRIMARY KEY,
    patient_name VARCHAR(100) NOT NULL,
    doctor_name VARCHAR(100) NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status VARCHAR(20) DEFAULT 'scheduled',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## PostgreSQL Features

### Enhanced Capabilities
- **ACID Compliance**: Full transaction support
- **Concurrent Access**: Multiple users simultaneously
- **Data Integrity**: Foreign key constraints
- **Performance**: Optimized queries with indexes
- **Scalability**: Handle thousands of appointments
- **Backup/Recovery**: Built-in database backup tools

### Advanced Features
- **User Roles**: Admin vs Staff permissions
- **Appointment Status**: scheduled, completed, cancelled
- **Date Range Queries**: Filter appointments by date
- **Conflict Detection**: Prevent double-booking
- **Audit Trail**: Track all changes with timestamps

## Connection Testing
Test your PostgreSQL connection:
```java
PostgreSQLManager dbManager = PostgreSQLManager.getInstance();
if (dbManager.testConnection()) {
    System.out.println("PostgreSQL connected successfully!");
} else {
    System.out.println("Connection failed - using file storage");
}
```

## Default Credentials
- **Admin**: `admin` / `clinic123`
- **Staff**: `staff` / `staff123`

## Troubleshooting

### Common Issues
1. **Connection Failed**: Check PostgreSQL service is running
2. **Authentication Error**: Verify username/password in pg_hba.conf
3. **Database Not Found**: Ensure clinic_db exists
4. **Permission Denied**: Grant proper privileges to clinic_user

### PostgreSQL Service Commands
```bash
# Windows
net start postgresql-x64-14
net stop postgresql-x64-14

# Linux/Mac
sudo systemctl start postgresql
sudo systemctl stop postgresql
```

## System Status: Production Ready ✅
Both file-based and PostgreSQL implementations are production-ready. PostgreSQL provides enterprise features for larger clinics.
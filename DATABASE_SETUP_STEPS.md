# 🔧 DATABASE SETUP - STEP BY STEP GUIDE

## 🎯 **GOAL: Get appointments saving to PostgreSQL database**

### **STEP 1: Update Database Settings**

Edit `DatabaseTestApp.java` (lines 15-19) with YOUR database information:

```java
private static final String DB_HOST = "localhost";        // Your PostgreSQL host
private static final String DB_PORT = "5432";            // Your PostgreSQL port  
private static final String DB_NAME = "clinic_db";       // Your database name
private static final String DB_USER = "postgres";        // YOUR PostgreSQL username
private static final String DB_PASSWORD = "your_password"; // YOUR PostgreSQL password
```

### **STEP 2: Download PostgreSQL JDBC Driver**

1. Go to: https://central.sonatype.com/artifact/org.postgresql/postgresql
2. Download: `postgresql-42.7.1.jar` (or latest version)
3. In NetBeans:
   - Right-click your project
   - Properties → Libraries → Compile
   - Add JAR/Folder → Select the downloaded JAR file
   - Click OK

### **STEP 3: Test Database Connection**

1. **Run the DatabaseTestApp** (it's now set as main class)
2. **Click "🔍 TEST DATABASE CONNECTION"**
3. **Check results:**
   - ✅ Success = Database connection works
   - ❌ Error = Fix the error shown

### **STEP 4: Create Database Tables**

If tables don't exist, run this SQL in your PostgreSQL:

```sql
CREATE TABLE appointments (
    id SERIAL PRIMARY KEY,
    patient_name VARCHAR(100) NOT NULL,
    patient_phone VARCHAR(20),
    doctor_name VARCHAR(100) NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status VARCHAR(20) DEFAULT 'SCHEDULED',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE doctors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100),
    phone VARCHAR(20),
    active BOOLEAN DEFAULT true
);

INSERT INTO doctors (name, specialization, phone) VALUES 
('Dr. Sarah Smith', 'General Practitioner', '555-0101'),
('Dr. John Johnson', 'Cardiologist', '555-0102'),
('Dr. Emily Williams', 'Pediatrician', '555-0103'),
('Dr. Michael Brown', 'Dermatologist', '555-0104'),
('Dr. Lisa Davis', 'Orthopedist', '555-0105');
```

### **STEP 5: Test Database Operations**

In the DatabaseTestApp:
1. **Click "📋 CHECK TABLES"** - Should show appointments table
2. **Click "💾 INSERT TEST APPOINTMENT"** - Should add test data
3. **Click "👁️ VIEW APPOINTMENTS"** - Should show the test appointment

### **STEP 6: Switch to Full Application**

Once database tests pass, update `nbproject/project.properties`:

```properties
javafx.main.class=cllinicappointmentschedulingsystem.DatabaseClinicApp
```

And update the database settings in `DatabaseClinicApp.java` with the same credentials.

---

## 🚨 **COMMON ISSUES & SOLUTIONS**

### **Issue: "PostgreSQL JDBC Driver not found"**
**Solution:** Download and add `postgresql-42.7.1.jar` to project libraries

### **Issue: "Connection refused"**
**Solutions:**
- Check if PostgreSQL server is running
- Verify host and port (usually localhost:5432)
- Check if database accepts connections

### **Issue: "Authentication failed"**
**Solutions:**
- Verify username and password
- Check PostgreSQL user permissions
- Try connecting with psql command line first

### **Issue: "Database does not exist"**
**Solutions:**
- Create database: `CREATE DATABASE clinic_db;`
- Or change DB_NAME to existing database

### **Issue: "Table doesn't exist"**
**Solution:** Run the CREATE TABLE SQL commands above

---

## 🎯 **TESTING CHECKLIST**

- [ ] PostgreSQL server is running
- [ ] Database `clinic_db` exists
- [ ] JDBC driver JAR added to project
- [ ] Database credentials updated in code
- [ ] Connection test passes
- [ ] Tables exist and have correct structure
- [ ] Test appointment inserts successfully
- [ ] Can view appointments from database

---

## 📞 **QUICK VERIFICATION**

Test your PostgreSQL connection manually:

```bash
# Command line test
psql -h localhost -p 5432 -U postgres -d clinic_db

# In psql, run:
\dt                          # List tables
SELECT * FROM appointments;  # View appointments
```

Once all tests pass, your clinic application will save appointments to the database! 🎉
-- PostgreSQL Database Setup Script for Clinic Appointment Scheduling System
-- Run this script as PostgreSQL superuser (postgres)

-- Create database
CREATE DATABASE clinic_db
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'English_United States.1252'
    LC_CTYPE = 'English_United States.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Create user for the application
CREATE USER clinic_user WITH
    LOGIN
    NOSUPERUSER
    NOCREATEDB
    NOCREATEROLE
    INHERIT
    NOREPLICATION
    CONNECTION LIMIT -1
    PASSWORD 'clinic_password';

-- Grant privileges to the user
GRANT ALL PRIVILEGES ON DATABASE clinic_db TO clinic_user;

-- Connect to the clinic_db database
\c clinic_db

-- Grant schema privileges
GRANT ALL ON SCHEMA public TO clinic_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO clinic_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO clinic_user;

-- Create tables (these will be created automatically by the application)
-- But you can run them manually if needed:

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'staff',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS doctors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100),
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS patients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    address TEXT,
    date_of_birth DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS appointments (
    id SERIAL PRIMARY KEY,
    patient_name VARCHAR(100) NOT NULL,
    doctor_name VARCHAR(100) NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status VARCHAR(20) DEFAULT 'scheduled',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_appointments_date ON appointments(appointment_date);
CREATE INDEX IF NOT EXISTS idx_appointments_doctor ON appointments(doctor_name);
CREATE INDEX IF NOT EXISTS idx_appointments_status ON appointments(status);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_doctors_active ON doctors(active);

-- Insert default data
INSERT INTO users (username, password, role) VALUES 
    ('admin', 'clinic123', 'admin'),
    ('staff', 'staff123', 'staff')
ON CONFLICT (username) DO NOTHING;

INSERT INTO doctors (name, specialization) VALUES 
    ('Dr. Smith', 'General Medicine'),
    ('Dr. Johnson', 'Cardiology'),
    ('Dr. Williams', 'Pediatrics'),
    ('Dr. Brown', 'Orthopedics'),
    ('Dr. Davis', 'Dermatology')
ON CONFLICT DO NOTHING;

-- Grant final privileges to ensure everything works
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO clinic_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO clinic_user;

-- Display success message
SELECT 'PostgreSQL database setup completed successfully!' as status;
-- UniversityErpManagement completion migration. Existing country/state/location tables are untouched.

START TRANSACTION;

CREATE TABLE IF NOT EXISTS erp_staff (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(100) NOT NULL UNIQUE,
  employeeCode VARCHAR(255),
  fullName VARCHAR(255),
  department VARCHAR(255),
  email VARCHAR(255),
  phone VARCHAR(255),
  designation VARCHAR(255),
  status VARCHAR(255),
  description TEXT,
  created_by VARCHAR(100),
  updated_by VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_staff_status (status)
);

CREATE TABLE IF NOT EXISTS erp_hod (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(100) NOT NULL UNIQUE,
  employeeCode VARCHAR(255),
  fullName VARCHAR(255),
  department VARCHAR(255),
  email VARCHAR(255),
  phone VARCHAR(255),
  designation VARCHAR(255),
  status VARCHAR(255),
  description TEXT,
  created_by VARCHAR(100),
  updated_by VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_hod_status (status)
);

CREATE TABLE IF NOT EXISTS erp_curriculum (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(100) NOT NULL UNIQUE,
  academicYear VARCHAR(255),
  course VARCHAR(255),
  semester VARCHAR(255),
  subject VARCHAR(255),
  credits VARCHAR(255),
  contactHours VARCHAR(255),
  status VARCHAR(255),
  description TEXT,
  created_by VARCHAR(100),
  updated_by VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_curriculum_status (status)
);

CREATE TABLE IF NOT EXISTS erp_timetable (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(100) NOT NULL UNIQUE,
  academicYear VARCHAR(255),
  semester VARCHAR(255),
  course VARCHAR(255),
  section VARCHAR(255),
  subject VARCHAR(255),
  faculty VARCHAR(255),
  room VARCHAR(255),
  dayOfWeek VARCHAR(255),
  startTime VARCHAR(255),
  endTime VARCHAR(255),
  period VARCHAR(255),
  status VARCHAR(255),
  description TEXT,
  created_by VARCHAR(100),
  updated_by VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_timetable_status (status)
);

CREATE TABLE IF NOT EXISTS erp_marks (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(100) NOT NULL UNIQUE,
  academicYear VARCHAR(255),
  semester VARCHAR(255),
  exam VARCHAR(255),
  course VARCHAR(255),
  subject VARCHAR(255),
  student VARCHAR(255),
  faculty VARCHAR(255),
  maxMarks INT,
  obtainedMarks INT,
  status VARCHAR(255),
  description TEXT,
  created_by VARCHAR(100),
  updated_by VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_marks_status (status)
);

CREATE TABLE IF NOT EXISTS erp_installment (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(100) NOT NULL UNIQUE,
  student VARCHAR(255),
  feeHead VARCHAR(255),
  installmentNo INT,
  dueDate DATE,
  amount DECIMAL(14,2),
  paidAmount DECIMAL(14,2),
  status VARCHAR(255),
  description TEXT,
  created_by VARCHAR(100),
  updated_by VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_installment_status (status)
);

CREATE TABLE IF NOT EXISTS erp_scholarship (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(100) NOT NULL UNIQUE,
  student VARCHAR(255),
  scholarshipName VARCHAR(255),
  provider VARCHAR(255),
  amount DECIMAL(14,2),
  academicYear VARCHAR(255),
  status VARCHAR(255),
  description TEXT,
  created_by VARCHAR(100),
  updated_by VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_scholarship_status (status)
);

CREATE TABLE IF NOT EXISTS erp_library (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(100) NOT NULL UNIQUE,
  bookCode VARCHAR(255),
  title VARCHAR(255),
  author VARCHAR(255),
  isbn VARCHAR(255),
  category VARCHAR(255),
  totalCopies INT,
  availableCopies INT,
  status VARCHAR(255),
  description TEXT,
  created_by VARCHAR(100),
  updated_by VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_library_status (status)
);

CREATE TABLE IF NOT EXISTS erp_hostel (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(100) NOT NULL UNIQUE,
  hostelName VARCHAR(255),
  roomNo VARCHAR(255),
  capacity INT,
  occupied INT,
  bedsFee DECIMAL(14,2),
  status VARCHAR(255),
  description TEXT,
  created_by VARCHAR(100),
  updated_by VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_hostel_status (status)
);

CREATE TABLE IF NOT EXISTS erp_transport (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(100) NOT NULL UNIQUE,
  vehicleNo VARCHAR(255),
  route VARCHAR(255),
  driver VARCHAR(255),
  capacity INT,
  fee VARCHAR(255),
  status VARCHAR(255),
  description TEXT,
  created_by VARCHAR(100),
  updated_by VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_transport_status (status)
);

CREATE TABLE IF NOT EXISTS erp_placement (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(100) NOT NULL UNIQUE,
  companyName VARCHAR(255),
  driveDate DATE,
  jobTitle VARCHAR(255),
  packageAmount DECIMAL(14,2),
  eligibility VARCHAR(255),
  selectedStudents INT,
  status VARCHAR(255),
  description TEXT,
  created_by VARCHAR(100),
  updated_by VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_placement_status (status)
);

CREATE TABLE IF NOT EXISTS erp_certificate (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(100) NOT NULL UNIQUE,
  student VARCHAR(255),
  certificateType VARCHAR(255),
  requestDate DATE,
  issuedDate DATE,
  certificateNo VARCHAR(255),
  status VARCHAR(255),
  description TEXT,
  created_by VARCHAR(100),
  updated_by VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_certificate_status (status)
);

CREATE TABLE IF NOT EXISTS erp_notification (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(100) NOT NULL UNIQUE,
  title VARCHAR(255),
  message VARCHAR(255),
  targetRole VARCHAR(255),
  channel VARCHAR(255),
  publishedAt TIMESTAMP,
  status VARCHAR(255),
  description TEXT,
  created_by VARCHAR(100),
  updated_by VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_notification_status (status)
);

CREATE TABLE IF NOT EXISTS erp_research (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(100) NOT NULL UNIQUE,
  title VARCHAR(255),
  principalInvestigator VARCHAR(255),
  department VARCHAR(255),
  grantAmount DECIMAL(14,2),
  startDate DATE,
  endDate DATE,
  status VARCHAR(255),
  description TEXT,
  created_by VARCHAR(100),
  updated_by VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_research_status (status)
);

CREATE TABLE IF NOT EXISTS erp_grievance (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(100) NOT NULL UNIQUE,
  ticketNo VARCHAR(255),
  raisedBy VARCHAR(255),
  category VARCHAR(255),
  subject VARCHAR(255),
  description TEXT,
  assignedTo VARCHAR(255),
  resolution VARCHAR(255),
  status VARCHAR(255),
  created_by VARCHAR(100),
  updated_by VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_grievance_status (status)
);

COMMIT;
-- Role catalogue used by the Angular guards and backend Role enum.
CREATE TABLE IF NOT EXISTS erp_role_catalog (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 role_code VARCHAR(60) NOT NULL UNIQUE,
 display_name VARCHAR(120) NOT NULL,
 active BOOLEAN NOT NULL DEFAULT TRUE
);
INSERT IGNORE INTO erp_role_catalog(role_code,display_name) VALUES
('SUPER_ADMIN','Super Admin'),('ADMIN','University Admin'),('VC','VC / Chancellor'),('REGISTRAR','Registrar'),('DEAN','Dean'),('HOD','Head of Department'),('FACULTY','Faculty / Teacher'),('ACCOUNTANT','Accountant'),('ADMISSION_OFFICER','Admission Officer'),('EXAM_CONTROLLER','Exam Controller'),('LIBRARIAN','Librarian'),('HOSTEL_WARDEN','Hostel Warden'),('TRANSPORT_MANAGER','Transport Manager'),('PLACEMENT_OFFICER','Placement Officer'),('HR','Human Resources'),('STAFF','Staff'),('STUDENT','Student'),('PARENT','Parent / Guardian'),('ALUMNI','Alumni'),('PRINCIPAL','Principal');

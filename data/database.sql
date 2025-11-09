CREATE DATABASE rhxmanagerspring;
use rhxmanagerspring;

DROP TABLE IF EXISTS Employe_Role;
DROP TABLE IF EXISTS Employe_Project;
DROP TABLE IF EXISTS Payslip;
DROP TABLE IF EXISTS Project;
DROP TABLE IF EXISTS Department;
DROP TABLE IF EXISTS Employe;
DROP TABLE IF EXISTS Role;

CREATE TABLE Role (
    id_role INT AUTO_INCREMENT,
    roleName VARCHAR(127) NOT NULL UNIQUE,
    PRIMARY KEY (id_role)
) ENGINE=InnoDB;

CREATE TABLE Department (
    id_department INT AUTO_INCREMENT,
    departmentName VARCHAR(127) NOT NULL UNIQUE,
    masterChief_id INT,
    PRIMARY KEY (id_department)
) ENGINE=InnoDB;

CREATE TABLE Project (
    id_project INT AUTO_INCREMENT,
    projectName VARCHAR(127) NOT NULL,
    state ENUM('WORKED_ON', 'FINISHED', 'CANCELED') NOT NULL,
    masterChief_id INT,
    PRIMARY KEY (id_project)
) ENGINE=InnoDB;

CREATE TABLE Employe (
    id_employe INT AUTO_INCREMENT,
    lastName VARCHAR(127) NOT NULL,
    firstName VARCHAR(127) NOT NULL,
    grade VARCHAR(127),
    jobName VARCHAR(127),
    salary DOUBLE NOT NULL,
    username VARCHAR(127) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    department_id INT,
    PRIMARY KEY (id_employe),
    FOREIGN KEY (department_id) REFERENCES Department(id_department) ON DELETE SET NULL
) ENGINE=InnoDB;

ALTER TABLE Department ADD CONSTRAINT fk_department_chief
FOREIGN KEY (masterChief_id) REFERENCES Employe(id_employe) ON DELETE SET NULL;

ALTER TABLE Project ADD CONSTRAINT fk_project_chief
FOREIGN KEY (masterChief_id) REFERENCES Employe(id_employe) ON DELETE SET NULL;

CREATE TABLE Payslip (
    id_payslip INT AUTO_INCREMENT,
    month INT NOT NULL,
    year INT NOT NULL,
    bonus DOUBLE DEFAULT 0.0,
    deductions DOUBLE DEFAULT 0.0,
    net DOUBLE NOT NULL,
    employe_id INT NOT NULL,
    PRIMARY KEY (id_payslip),
    FOREIGN KEY (employe_id) REFERENCES Employe(id_employe) ON DELETE CASCADE, -- deleting an emloye deletes the payslip --
    CONSTRAINT uc_payslip_employee_period UNIQUE (employe_id, month, year) -- avoid creating 2x same payslips for an employe --
) ENGINE=InnoDB;

-- junctions tables --

CREATE TABLE Employe_Project (
    employe_id INT,
    project_id INT,
    PRIMARY KEY (employe_id, project_id),
    FOREIGN KEY (employe_id) REFERENCES Employe(id_employe) ON DELETE CASCADE,
    FOREIGN KEY (project_id) REFERENCES Project(id_project) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE Employe_Role (
    employe_id INT,
    role_id INT,
    PRIMARY KEY (employe_id, role_id),
    FOREIGN KEY (employe_id) REFERENCES Employe(id_employe) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES Role(id_role) ON DELETE CASCADE
) ENGINE=InnoDB;

-- username : "aze" | pwd : "aze" --
INSERT INTO Employe (lastName, firstName, grade, jobName, salary, username, password, department_id) VALUES ('lastName', 'firstName', 'N/A', 'Test Account', 1000.00, 'aze', '$2a$10$lflri/EYxKahRbiTHwUZZ.JlW8Z7iJsqLKyea27kw4ewK6OP3Rkxu', NULL);

INSERT INTO Role (roleName) VALUES ('ADMIN');
INSERT INTO Role (roleName) VALUES ('EMPLOYE');
INSERT INTO Employe_Role (employe_id, role_id)
SELECT
    (SELECT id_employe FROM Employe WHERE username = 'aze' LIMIT 1),
    (SELECT id_role FROM Role WHERE roleName = 'ADMIN' LIMIT 1);



create database companyManagent;
use companyManagent;

-- 1. Bảng candidate (Ứng viên)
CREATE TABLE candidate (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           email VARCHAR(100) NOT NULL UNIQUE,
                           password VARCHAR(255) NOT NULL,
                           phone VARCHAR(20),
                           experience INT DEFAULT 0,
                           gender VARCHAR(10),
                           status VARCHAR(20) DEFAULT 'active',
                           description TEXT,
                           dob DATE,
                           CHECK (gender IN ('Nam', 'Nữ'))
);

-- 2. Bảng technology (Công nghệ)
CREATE TABLE technology (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(100) NOT NULL UNIQUE
);

-- 3. Bảng candidate_technology (ứng viên - công nghệ)
CREATE TABLE candidate_technology (
                                      candidateId INT NOT NULL,
                                      technologyId INT NOT NULL,
                                      PRIMARY KEY (candidateId, technologyId),
                                      FOREIGN KEY (candidateId) REFERENCES candidate(id),
                                      FOREIGN KEY (technologyId) REFERENCES technology(id)
);

-- 4. Bảng recruitment_position (Vị trí tuyển dụng)
CREATE TABLE recruitment_position (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      name VARCHAR(100) NOT NULL,
                                      description TEXT,
                                      minSalary DECIMAL(12,2) CHECK (minSalary >= 0),
                                      maxSalary DECIMAL(12,2) CHECK (maxSalary >= 0),
                                      minExperience INT DEFAULT 0,
                                      createdDate DATE DEFAULT (CURRENT_DATE),
                                      expiredDate DATE NOT NULL
);

-- 5. Bảng recruitment_position_technology (vị trí - công nghệ)
CREATE TABLE recruitment_position_technology (
                                                 recruitmentPositionId INT NOT NULL,
                                                 technologyId INT NOT NULL,
                                                 PRIMARY KEY (recruitmentPositionId, technologyId),
                                                 FOREIGN KEY (recruitmentPositionId) REFERENCES recruitment_position(id),
                                                 FOREIGN KEY (technologyId) REFERENCES technology(id)
);

-- 6. Bảng application (Đơn ứng tuyển)
CREATE TABLE application (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             candidateId INT NOT NULL,
                             recruitmentPositionId INT NOT NULL,
                             cvUrl VARCHAR(255) NOT NULL,
                             progress ENUM('pending', 'handling', 'interviewing', 'done') DEFAULT 'pending' NOT NULL,
                             interviewRequestDate DATETIME,
                             interviewRequestResult VARCHAR(100),
                             interviewLink VARCHAR(255),
                             interviewTime DATETIME,
                             interviewResult VARCHAR(50),
                             interviewResultNote TEXT,
                             destroyAt DATETIME,
                             createAt DATETIME DEFAULT CURRENT_TIMESTAMP,
                             updateAt DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             destroyReason TEXT,
                             FOREIGN KEY (candidateId) REFERENCES candidate(id),
                             FOREIGN KEY (recruitmentPositionId) REFERENCES recruitment_position(id)
);

create table admin
(
    id        int primary key,
    adminName varchar(100) not null,
    password  varchar(255) not null
)

-- Các Proceduce

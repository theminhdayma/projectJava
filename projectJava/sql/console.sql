drop database applicationManagement;
create database applicationManagement;
use applicationManagement;

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
                            name VARCHAR(100) NOT NULL UNIQUE,
                            status ENUM('active', 'inactive') DEFAULT 'active'
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

create table admin (
    id        int auto_increment primary key,
    adminName varchar(100) not null,
    password  varchar(255) not null
);

-- Các Proceduce
-- Admin
DELIMITER //

drop procedure if exists check_account_admin;
CREATE PROCEDURE check_account_admin()
BEGIN
    DECLARE total INT;

    SELECT COUNT(*) INTO total FROM admin;

    IF total = 0 THEN
        INSERT INTO admin(adminName, password)
        VALUES ('admin', 'admin');
    END IF;
END //

drop procedure if exists login_admin;
CREATE PROCEDURE login_admin (
    IN name_in VARCHAR(100),
    IN password_in VARCHAR(255)
)
BEGIN
    SELECT id, adminName
    FROM admin
    WHERE adminName = name_in
      AND password = password_in;
END //

DELIMITER ;

-- technology
DELIMITER //

DROP PROCEDURE IF EXISTS get_technology_page;
CREATE PROCEDURE get_technology_page (
    IN in_limit INT
)
BEGIN
    DECLARE total_technology INT;

    SELECT COUNT(*) INTO total_technology FROM technology;
    SELECT CEIL(total_technology / in_limit) AS totalPage;
END //

DROP PROCEDURE IF EXISTS get_technology;
CREATE PROCEDURE get_technology (
    IN page_in INT,
    IN limit_in INT
)
BEGIN
    DECLARE offset INT;
    SET offset = (page_in - 1) * limit_in;
    SELECT id, name FROM technology
    WHERE status = 'active'
    LIMIT limit_in OFFSET offset;
END //

DROP PROCEDURE IF EXISTS add_technology;
CREATE PROCEDURE add_technology (
    IN name_in VARCHAR(100)
)
BEGIN
    DECLARE technology_name_count INT;

    SELECT COUNT(*) INTO technology_name_count
    FROM technology
    WHERE name = name_in;

    IF technology_name_count = 0 THEN
        INSERT INTO technology (name)
        VALUES (name_in);
    ELSE
        UPDATE technology
        SET status = 'active'
        WHERE name = name_in;
    END IF;
END //

DROP PROCEDURE IF EXISTS delete_technology;
CREATE PROCEDURE delete_technology (
    IN technology_id_in INT
)
BEGIN
    DECLARE technology_count INT;

    SELECT COUNT(*) INTO technology_count FROM candidate_technology
    WHERE technologyId = technology_id_in;

    IF technology_count > 0 THEN
        UPDATE technology
        SET
            status = 'inactive',
            name = CONCAT(name, '_deleted')
        WHERE id = technology_id_in;
    ELSE
        DELETE FROM technology
        WHERE id = technology_id_in;
    END IF;
END //

DROP PROCEDURE IF EXISTS update_technology;
CREATE PROCEDURE update_technology (
    IN technology_id_in INT,
    IN name_in VARCHAR(100)
)
BEGIN
    DECLARE technology_name_count INT;

    SELECT COUNT(*) INTO technology_name_count FROM technology
    WHERE name = name_in;

    IF technology_name_count = 0 THEN
        UPDATE technology
        SET name = name_in
        WHERE id = technology_id_in;
    ELSE
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'đã tồn tại công nghệ này';
    END IF;
END //

DROP PROCEDURE IF EXISTS get_technology_by_id;
CREATE PROCEDURE get_technology_by_id (
    IN technology_id_in INT
)
BEGIN
    SELECT id, name FROM technology
    WHERE id = technology_id_in;
END //

DELIMITER ;
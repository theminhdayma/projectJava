drop database if exists applicationManagement;
create database applicationManagement;
use applicationManagement;


-- 1. Bảng candidate (Ứng viên)
CREATE TABLE candidate (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           email VARCHAR(100) NOT NULL UNIQUE,
                           phone VARCHAR(20) NOT NULL,
                           experience INT DEFAULT 0,
                           gender enum ('MALE', 'FEMALE', 'OTHER'),
                           description TEXT,
                           dob DATE,
                           active ENUM('LOCKED', 'UNLOCKED') DEFAULT 'UNLOCKED'
);

create table account (
                         account_id int auto_increment primary key,
                         candidate_id int default null,
                         username varchar(100) not null,
                         password  varchar(255) not null,
                         role      enum ('ADMIN', 'CANDIDATE') not null,
                         status    enum ('ACTIVE', 'INACTIVE') default 'ACTIVE',
                         FOREIGN KEY (candidate_id) REFERENCES candidate(id)
);

-- 2. Bảng technology (Công nghệ)
CREATE TABLE technology (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(100) NOT NULL UNIQUE,
                            status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE'
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

-- Các Proceduce
-- Admin
DELIMITER //

drop procedure if exists check_account_admin;
CREATE PROCEDURE check_account_admin()
BEGIN
    DECLARE total INT;

    SELECT COUNT(*) INTO total FROM account
    WHERE role = 'ADMIN';

    IF total = 0 THEN
        INSERT INTO account(username, password, role)
        VALUES ('admin', 'admin', 'ADMIN');
    END IF;
END //

drop procedure if exists login_admin;
CREATE PROCEDURE login_admin (
    IN name_in VARCHAR(100),
    IN password_in VARCHAR(255)
)
BEGIN
    SELECT account_id, username FROM account
    WHERE username = name_in
      AND password = password_in
        AND role = 'ADMIN';
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

DROP PROCEDURE IF EXISTS check_name_technology;
CREATE PROCEDURE check_name_technology (
    IN name_in VARCHAR(100)
)
BEGIN
    DECLARE technology_name_count INT;

    SELECT COUNT(*) INTO technology_name_count
    FROM technology
    WHERE name = name_in;

    IF technology_name_count > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Tên công nghệ đã tồn tại';
    END IF;
END //

DROP PROCEDURE IF EXISTS add_technology;
CREATE PROCEDURE add_technology (
    IN name_in VARCHAR(100)
)
BEGIN
    INSERT INTO technology (name)
    VALUES (name_in);
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
    UPDATE technology
    SET name = name_in
    WHERE id = technology_id_in;
END //

DROP PROCEDURE IF EXISTS get_technology_by_id;
CREATE PROCEDURE get_technology_by_id (
    IN technology_id_in INT
)
BEGIN
    SELECT id, name FROM technology
    WHERE id = technology_id_in
      AND status = 'active';
END //

DELIMITER ;


-- candidate

DELIMITER //

drop procedure if exists login_candidate;
CREATE PROCEDURE login_candidate (
    IN name_in VARCHAR(100),
    IN password_in VARCHAR(255)
)
BEGIN
    SELECT account_id, username FROM account
    WHERE username = name_in
      AND password = password_in
      AND role = 'CANDIDATE';
END //

DROP PROCEDURE IF EXISTS get_candidate_page;
CREATE PROCEDURE get_candidate_page (
    IN in_limit INT
)
BEGIN
    DECLARE total_candidate INT;
    SELECT COUNT(*) INTO total_candidate FROM candidate;
    SELECT CEIL(total_candidate / in_limit) AS totalPage;
END //

DROP PROCEDURE IF EXISTS get_candidate;
CREATE PROCEDURE get_candidate (
    IN page_in INT,
    IN limit_in INT
)
BEGIN
    DECLARE offset INT;
    SET offset = (page_in - 1) * limit_in;

    SELECT c.id, c.name, c.email
    FROM candidate c
             LEFT JOIN account a ON c.id = a.candidate_id
    WHERE a.status = 'ACTIVE'
    LIMIT limit_in OFFSET offset;
END //

DROP PROCEDURE IF EXISTS check_email_candidate;
CREATE PROCEDURE check_email_candidate (
    IN email_in VARCHAR(100)
)
BEGIN
    DECLARE email_count INT;

    SELECT COUNT(*) INTO email_count FROM candidate
    WHERE email = email_in;

    IF email_count > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Email đã tồn tại';
    END IF;
END //

DROP PROCEDURE IF EXISTS add_candidate;
CREATE PROCEDURE add_candidate (
    IN p_name VARCHAR(100),
    IN p_email VARCHAR(100),
    IN p_phone VARCHAR(20),
    IN p_experience INT,
    IN p_gender ENUM('MALE', 'FEMALE', 'OTHER'),
    IN p_description TEXT,
    IN p_dob DATE,
    IN a_password VARCHAR(255)
)
BEGIN
    DECLARE new_candidate_id INT;

    INSERT INTO candidate (name, email, phone, experience, gender, description, dob)
    VALUES (p_name, p_email, p_phone, p_experience, p_gender, p_description, p_dob);

    SET new_candidate_id = LAST_INSERT_ID();

    INSERT INTO account (candidate_id, username, password, role, status)
    VALUES (new_candidate_id, p_email, a_password, 'CANDIDATE', 'ACTIVE');
END //

DROP PROCEDURE IF EXISTS delete_candidate;
CREATE PROCEDURE delete_candidate (
    IN candidate_id_in INT
)
BEGIN
    DECLARE candidate_count INT;
    DECLARE acc_id INT;

    SELECT account_id INTO acc_id
    FROM account
    WHERE candidate_id = candidate_id_in;

    IF acc_id IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ứng viên không tồn tại';
    END IF;

    SELECT COUNT(*) INTO candidate_count
    FROM application
    WHERE candidateId = candidate_id_in;

    IF candidate_count > 0 THEN
        UPDATE account
        SET status = 'INACTIVE'
        WHERE account_id = acc_id;
        UPDATE candidate
        SET name = CONCAT(name, '_deleted')
        WHERE id = candidate_id_in;
    ELSE
        DELETE FROM account WHERE account_id = acc_id;
        DELETE FROM candidate WHERE id = candidate_id_in;
    END IF;
END //

DROP PROCEDURE IF EXISTS update_candidate;
CREATE PROCEDURE update_candidate (
    IN p_candidate_id INT,
    IN p_name VARCHAR(100),
    IN p_phone VARCHAR(20),
    IN p_experience INT,
    IN p_gender ENUM('MALE', 'FEMALE', 'OTHER'),
    IN p_description TEXT,
    IN p_dob DATE
)
BEGIN
    UPDATE candidate
    SET
        name = p_name,
        phone = p_phone,
        experience = p_experience,
        gender = p_gender,
        description = p_description,
        dob = p_dob
    WHERE id = p_candidate_id;
END //

DROP PROCEDURE IF EXISTS get_candidate_by_id;
CREATE PROCEDURE get_candidate_by_id (
    IN candidate_id_in INT
)
BEGIN
    SELECT
        c.id,
        c.name,
        c.email,
        c.phone,
        c.experience,
        c.gender,
        c.description,
        c.dob,
        c.active,
        a.status,
        a.username
    FROM candidate c
             LEFT JOIN account a ON c.id = a.candidate_id
    WHERE c.id = candidate_id_in;
END //

DROP PROCEDURE IF EXISTS lock_candidate_account;
CREATE PROCEDURE lock_candidate_account (
    IN p_candidate_id INT
)
BEGIN
    DECLARE acc_id INT;

    SELECT account_id INTO acc_id
    FROM account
    WHERE candidate_id = p_candidate_id;

    IF acc_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Không tìm thấy tài khoản ứng viên để khóa';
    END IF;

    UPDATE account
    SET status = 'INACTIVE'
    WHERE account_id = acc_id;
END //

DROP PROCEDURE IF EXISTS unlock_candidate_account;
CREATE PROCEDURE unlock_candidate_account (
    IN p_candidate_id INT
)
BEGIN
    DECLARE acc_id INT;

    SELECT account_id INTO acc_id
    FROM account
    WHERE candidate_id = p_candidate_id;

    IF acc_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Không tìm thấy tài khoản ứng viên để mở khóa';
    END IF;

    UPDATE account
    SET status = 'ACTIVE'
    WHERE account_id = acc_id;
END //

DROP PROCEDURE IF EXISTS reset_candidate_password;
CREATE PROCEDURE reset_candidate_password (
    IN p_candidate_id INT,
    IN p_new_password VARCHAR(255)
)
BEGIN
    DECLARE acc_id INT;

    SELECT account_id INTO acc_id
    FROM account
    WHERE candidate_id = p_candidate_id;

    IF acc_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Không tìm thấy tài khoản ứng viên để reset mật khẩu';
    END IF;

    UPDATE account
    SET password = p_new_password
    WHERE account_id = acc_id;
END //

DROP PROCEDURE IF EXISTS search_candidate_by_name;
CREATE PROCEDURE search_candidate_by_name (
    IN p_keyword VARCHAR(100)
)
BEGIN
    SELECT
        c.id,
        c.name,
        c.email,
        c.phone,
        c.experience,
        c.gender,
        c.description,
        c.dob,
        c.active,
        a.status,
        a.username
    FROM candidate c
             LEFT JOIN account a ON c.id = a.candidate_id
    WHERE LOWER(c.name) LIKE CONCAT('%', LOWER(p_keyword), '%');
END //



DELIMITER ;
CREATE TABLE
    IF NOT EXISTS `account` (
        `id` INT PRIMARY KEY AUTO_INCREMENT,
        `username` VARCHAR(100) NOT NULL,
        `email` VARCHAR(100) UNIQUE NOT NULL,
        `passwordHash` VARCHAR(255) NOT NULL,
        `created_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        `updated_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        `settings` JSON
    );

CREATE TABLE
    IF NOT EXISTS `event` (
        `id` INT PRIMARY KEY AUTO_INCREMENT,
        `title` VARCHAR(100),
        `description` VARCHAR(255),
        `created_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        `updated_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        `startTime` DATETIME NOT NULL,
        `endTime` DATETIME NOT NULL,
        `location` VARCHAR(255),
        `color` VARCHAR(20),
        `isAllDay` TINYINT (1) DEFAULT 0,
        `isPrivate` TINYINT (1) DEFAULT 0,
        `participants` JSON,
        `calendarId` INT NOT NULL
    );

CREATE TABLE
    IF NOT EXISTS `calendar` (
        `id` INT PRIMARY KEY AUTO_INCREMENT,
        `color` VARCHAR(20),
        `name` VARCHAR(100),
        `timezone` INT,
        `ownerId` INT NOT NULL
    );

ALTER TABLE `event` ADD FOREIGN KEY (`calendarId`) REFERENCES `calendar` (`id`);

ALTER TABLE `calendar` ADD FOREIGN KEY (`ownerId`) REFERENCES `account` (`id`);

CREATE TABLE
    IF NOT EXISTS share (
        calendarId INT,
        accountId INT,
        scope ENUM ('AVAILABILITY', 'READ', 'WRITE', 'SHARE') DEFAULT 'READ',
        PRIMARY KEY (`calendarId`, `accountId`)
    );

ALTER TABLE `share` ADD FOREIGN KEY (`calendarId`) REFERENCES `calendar` (`id`);

ALTER TABLE `share` ADD FOREIGN KEY (`accountId`) REFERENCES `account` (`id`);
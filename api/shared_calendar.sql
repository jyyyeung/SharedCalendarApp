CREATE TABLE
  IF NOT EXISTS `account` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `username` VARCHAR(100) NOT NULL,
    `email` VARCHAR(100) UNIQUE NOT NULL,
    `password_hash` VARCHAR(255) NOT NULL,
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
    `start_time` DATETIME NOT NULL,
    `end_time` DATETIME NOT NULL,
    `location` VARCHAR(255),
    `color` VARCHAR(20),
    `is_all_day` TINYINT (1) DEFAULT 0,
    `is_private` TINYINT (1) DEFAULT 0,
    `participants` JSON,
    `calendar_id` INT NOT NULL
  );

CREATE TABLE
  IF NOT EXISTS `calendar` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `color` VARCHAR(20),
    `name` VARCHAR(100),
    `timezone` INT,
    `owner_id` INT NOT NULL
  );

ALTER TABLE `event` ADD FOREIGN KEY (`calendar_id`) REFERENCES `calendar` (`id`);

ALTER TABLE `calendar` ADD FOREIGN KEY (`owner_id`) REFERENCES `account` (`id`);

CREATE TABLE
  IF NOT EXISTS share (
    calendar_id INT,
    account_id INT,
    scope ENUM ('availability', 'read', 'write') DEFAULT 'read',
    PRIMARY KEY (`calendar_id`, `account_id`)
  );

ALTER TABLE `share` ADD FOREIGN KEY (`calendar_id`) REFERENCES `calendar` (`id`);

ALTER TABLE `share` ADD FOREIGN KEY (`account_id`) REFERENCES `account` (`id`);
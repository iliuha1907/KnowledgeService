
DROP SCHEMA IF EXISTS `hoteladmin`;
CREATE SCHEMA `hoteladmin` DEFAULT CHARACTER SET utf8;
USE `hoteladmin`;

CREATE TABLE IF NOT EXISTS `Clients` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);

CREATE TABLE IF NOT EXISTS `Hotel_services` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `price` DECIMAL(10,0) NOT NULL,
  `type` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);

CREATE TABLE IF NOT EXISTS `Rooms` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `status` VARCHAR(45) NOT NULL,
  `price` DECIMAL(10,0) NOT NULL,
  `capacity` INT NOT NULL,
  `stars` INT NOT NULL,
  `isFree` TINYINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);

CREATE TABLE IF NOT EXISTS `Reservations` (
  `room_id` INT NOT NULL,
  `resident_id` INT NOT NULL,
  `arrival_date` DATE NOT NULL,
  `departure_date` DATE NOT NULL,
  `isActive` TINYINT NOT NULL,
  INDEX `room_id_idx` (`room_id` ASC) VISIBLE,
  INDEX `resident_id_idx` (`resident_id` ASC) VISIBLE,
  CONSTRAINT `resident_id`
    FOREIGN KEY (`resident_id`)
    REFERENCES `Clients` (`id`),
  CONSTRAINT `room_id`
    FOREIGN KEY (`room_id`)
    REFERENCES `Rooms` (`id`));

CREATE TABLE IF NOT EXISTS `Visits` (
  `client_id` INT NOT NULL,
  `hotel_service_id` INT NOT NULL,
  `date` DATE NOT NULL,
  `isActive` TINYINT NOT NULL,
  INDEX `client_id_idx` (`client_id` ASC) VISIBLE,
  INDEX `hotel_service_id_idx` (`hotel_service_id` ASC) VISIBLE,
  CONSTRAINT `client_id`
    FOREIGN KEY (`client_id`)
    REFERENCES `Clients` (`id`),
  CONSTRAINT `hotel_service_id`
    FOREIGN KEY (`hotel_service_id`)
    REFERENCES `Hotel_services` (`id`));


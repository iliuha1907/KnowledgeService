START TRANSACTION;

DROP SCHEMA IF EXISTS `hoteladmin_huzei`;
CREATE SCHEMA `hoteladmin_huzei` DEFAULT CHARACTER SET utf8;
USE `hoteladmin_huzei`;

 CREATE TABLE IF NOT EXISTS `roles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `role_type` VARCHAR(45) NOT NULL,
  CONSTRAINT roles_pk PRIMARY KEY (`id`));
  
CREATE TABLE IF NOT EXISTS `users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(45) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `roles_id` INT NOT NULL,
  CONSTRAINT users_pk PRIMARY KEY (`id`),
    CONSTRAINT `roles_id_fk`
    FOREIGN KEY (`roles_id`)
    REFERENCES `roles` (`id`));
  
CREATE TABLE IF NOT EXISTS `clients` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  CONSTRAINT clients_pk PRIMARY KEY (`id`));

CREATE TABLE IF NOT EXISTS `hotel_services` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `price` DECIMAL(10,0) NOT NULL,
  `type` VARCHAR(20) NOT NULL,
  CONSTRAINT hotel_services_pk PRIMARY KEY (`id`));

CREATE TABLE IF NOT EXISTS `rooms` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `status` VARCHAR(45) NOT NULL,
  `price` DECIMAL(10,0) NOT NULL,
  `capacity` INT NOT NULL,
  `stars` INT NOT NULL,
  `is_free` TINYINT NOT NULL,
  CONSTRAINT rooms_pk PRIMARY KEY (`id`));

CREATE TABLE IF NOT EXISTS `reservations` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `room_id` INT NOT NULL,
  `resident_id` INT NOT NULL,
  `arrival_date` DATE NOT NULL,
  `departure_date` DATE NOT NULL,
  `is_active` TINYINT NOT NULL,
  CONSTRAINT reservations_pk PRIMARY KEY (`id`),
  CONSTRAINT `resident_id_fk`
    FOREIGN KEY (`resident_id`)
    REFERENCES `Clients` (`id`),
  CONSTRAINT `room_id_fk`
    FOREIGN KEY (`room_id`)
    REFERENCES `Rooms` (`id`));

CREATE TABLE IF NOT EXISTS `visits` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `client_id` INT NOT NULL,
  `hotel_service_id` INT NOT NULL,
  `date` DATE NOT NULL,
  `is_active` TINYINT NOT NULL,
  CONSTRAINT visits_pk PRIMARY KEY (`id`),
  CONSTRAINT `client_id_fk`
    FOREIGN KEY (`client_id`)
    REFERENCES `Clients` (`id`),
  CONSTRAINT `hotel_service_id_fk`
    FOREIGN KEY (`hotel_service_id`)
    REFERENCES `Hotel_services` (`id`));
    
CREATE INDEX id ON Rooms(id);
CREATE INDEX id ON Hotel_services(id);
CREATE INDEX id ON Clients(id);

    COMMIT;

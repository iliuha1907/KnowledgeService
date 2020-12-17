START TRANSACTION;

DROP SCHEMA IF EXISTS `knowledge_huzei`;
CREATE SCHEMA IF NOT EXISTS `knowledge_huzei`;
USE `knowledge_huzei` ;

-- -----------------------------------------------------
-- Table `user_profiles`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `user_profiles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `birth_date` DATE NOT NULL,
  PRIMARY KEY (`id`));
  
-- -----------------------------------------------------
-- Table `users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(45) NOT NULL,
  `password` VARCHAR(255) NULL,
  `role` VARCHAR(45) NOT NULL,
  `user_profile_id` INT NOT NULL,
  PRIMARY KEY (`id`));
  
ALTER TABLE `users` ADD CONSTRAINT `fk_users_user_profile` FOREIGN KEY (`user_profile_id`)
REFERENCES `user_profiles` (`id`) ON DELETE CASCADE;

ALTER TABLE `users` ADD CONSTRAINT `user_profile_unique` UNIQUE (`user_profile_id`);

ALTER TABLE `users` ADD CONSTRAINT `users_login_unique` UNIQUE (`login`);

-- -----------------------------------------------------
-- Table `topic`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `topics` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NOT NULL,
  `description` VARCHAR(255) NOT NULL,
   PRIMARY KEY (`id`));
   
-- -----------------------------------------------------
-- Table `section`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sections` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  `topic_id` INT NOT NULL,
   PRIMARY KEY (`id`));
    
ALTER TABLE `sections` ADD CONSTRAINT `fk_sections_topic` FOREIGN KEY (`topic_id`) REFERENCES `topics` (`id`)
ON DELETE CASCADE;

CREATE INDEX `fk_section_topic_idx` ON `sections` ( `topic_id` );

-- -----------------------------------------------------
-- Table `course`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `courses` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  `duration` INT NOT NULL,
  `price` DECIMAL NOT NULL,
  `section_id` INT NOT NULL,
   PRIMARY KEY (`id`));

ALTER TABLE `courses` ADD  CONSTRAINT `fk_courses_section` FOREIGN KEY (`section_id`)
REFERENCES `sections` (`id`) ON DELETE CASCADE;

CREATE INDEX `fk_courses_section_idx` ON `courses` ( `section_id` );

ALTER TABLE `courses` ADD CONSTRAINT `courses_price_check` CHECK ( `price` > 0 );

ALTER TABLE `courses` ADD CONSTRAINT `course_duration_check` CHECK ( `duration` > 0 );

-- -----------------------------------------------------
-- Table `course_subscriptions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course_subscriptions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `start_date` DATE NOT NULL,
  `end_date` DATE NOT NULL,
  `user_id` INT NOT NULL,
  `course_id` INT NOT NULL,
   PRIMARY KEY (`id`));
    
ALTER TABLE `course_subscriptions` ADD CONSTRAINT `fk_course_subscriptions_user`  FOREIGN KEY (`user_id`)
REFERENCES `users` (`id`) ON DELETE CASCADE;

ALTER TABLE `course_subscriptions` ADD CONSTRAINT `fk_course_subscriptions_course`  FOREIGN KEY (`course_id`)
REFERENCES `courses` (`id`) ON DELETE CASCADE;

CREATE INDEX `fk_course_subscriptions_user_idx` ON `course_subscriptions` ( `user_id` );

CREATE INDEX `fk_course_subscriptions_course_idx` ON `course_subscriptions` ( `course_id` );

ALTER TABLE `course_subscriptions` ADD CONSTRAINT `course_subscriptions_dates_check`
CHECK ( `start_date` <=  `end_date` );

ALTER TABLE `course_subscriptions` ADD CONSTRAINT `course_subscriptions_user_course_unique`
UNIQUE ( `user_id`, `course_id` );

ALTER TABLE `course_subscriptions` ADD CONSTRAINT `course_subscriptions_date_relation`
CHECK ( `start_date` <= `end_date` );

-- -----------------------------------------------------
-- Table `lesson`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lessons` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  `course_id` INT NOT NULL,
  `type` VARCHAR(45) NOT NULL,
   PRIMARY KEY (`id`));
    
ALTER TABLE `lessons` ADD  CONSTRAINT `fk_lessons_course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`)
ON DELETE CASCADE;

CREATE INDEX `fk_lessons_course_idx` ON `lessons` ( `course_id` );

-- -----------------------------------------------------
-- Table `teachers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `teachers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `reward` DECIMAL NOT NULL,
  `reward_type` VARCHAR(45) NOT NULL,
  `is_active` TINYINT NOT NULL,
   PRIMARY KEY (`id`));
   
ALTER TABLE `teachers` ADD CONSTRAINT `teachers_reward_check` CHECK ( `reward` >= 0 );

-- -----------------------------------------------------
-- Table `lesson_subscriptions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lesson_subscriptions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `lesson_date` DATE NOT NULL,
  `took_place` TINYINT NOT NULL,
  `lesson_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `teacher_id` INT NOT NULL,
   PRIMARY KEY (`id`));
    
ALTER TABLE `lesson_subscriptions` ADD CONSTRAINT `fk_lesson_subscriptions_lesson`
FOREIGN KEY (`lesson_id`) REFERENCES `lessons` (`id`) ON DELETE CASCADE;

ALTER TABLE `lesson_subscriptions` ADD CONSTRAINT `fk_lesson_subscriptions_user`
FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

ALTER TABLE `lesson_subscriptions` ADD CONSTRAINT `fk_lesson_subscriptions_teacher`
FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`id`) ON DELETE CASCADE;

CREATE INDEX `fk_lesson_subscriptions_lesson_idx` ON `lesson_subscriptions` ( `lesson_id` );

CREATE INDEX `fk_lesson_subscriptions_user_idx` ON `lesson_subscriptions` ( `user_id` );

CREATE INDEX `fk_lesson_subscriptions_teacher_idx` ON `lesson_subscriptions` ( `teacher_id` );

ALTER TABLE `lesson_subscriptions` ADD CONSTRAINT `lesson_subscriptions_user_lesson_unique`
UNIQUE ( `user_id`, `lesson_id` );

-- -----------------------------------------------------
-- Table `course_reviews`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course_reviews` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `message` VARCHAR(255) NOT NULL,
  `user_id` INT NOT NULL,
  `course_id` INT NOT NULL,
  `review_date` DATE NOT NULL,
   PRIMARY KEY (`id`));
    
ALTER TABLE `course_reviews` ADD CONSTRAINT `fk_course_reviews_user` FOREIGN KEY (`user_id`)
REFERENCES `users` (`id`) ON DELETE CASCADE;

ALTER TABLE `course_reviews` ADD CONSTRAINT `fk_course_reviews_course` FOREIGN KEY (`course_id`)
REFERENCES `courses` (`id`) ON DELETE CASCADE;

CREATE INDEX `fk_course_reviews_user_idx` ON `course_reviews` ( `user_id` );

CREATE INDEX `fk_course_reviews_course_idx` ON `course_reviews` ( `course_id` );

-- -----------------------------------------------------
-- Table `lesson_reviews`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lesson_reviews` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `message` VARCHAR(255) NOT NULL,
  `user_id` INT NOT NULL,
  `lesson_id` INT NOT NULL,
   `review_date` DATE NOT NULL,
   PRIMARY KEY (`id`));
    
ALTER TABLE `lesson_reviews` ADD  CONSTRAINT `fk_lesson_reviews_user` FOREIGN KEY (`user_id`)
REFERENCES `users` (`id`) ON DELETE CASCADE;

ALTER TABLE `lesson_reviews` ADD CONSTRAINT `fk_lesson_reviews_lesson` FOREIGN KEY (`lesson_id`)
REFERENCES `lessons` (`id`) ON DELETE CASCADE;

CREATE INDEX `fk_lesson_reviews_users_idx` ON `lesson_reviews` ( `user_id` );

CREATE INDEX `fk_lesson_reviews_lesson_idx` ON `lesson_reviews` ( `lesson_id` );

COMMIT;

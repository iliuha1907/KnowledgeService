USE `knowledge_huzei` ;

START TRANSACTION;

INSERT INTO `user_profiles` VALUES(1,'Ilya','Huzei','2001-7-19'), (2,'Alex','Mason','1978-7-12');

INSERT INTO `users` VALUES(1,'iliuha1907','$2y$12$7CO7/Vf34ybyih08OOu28.n5wS3Vw77UIyPmBf3LAKVfY1bPOlzn6',
'ROLE_ADMIN',1), (2,'alex','$2y$12$z0d/nH18Cpn9/dg65P6Sveg.tz7Q6hqyO6kwbi7mt1ws39ROTyScW','ROLE_USER',2);

INSERT INTO `topics` VALUES (1, 'Geography', 'Topic about geography'),(2, 'Math', 'Topic about math');

INSERT INTO `sections` VALUES(1,'Geodesie', 'Section about geodesie', 1),(2,'Geometry','Section about geometry',2),
(3,'Algebra','Section about algebra',2);

INSERT INTO `courses` VALUES(1,'Facts', 'Course about facts about geodesie', 4,100, 1),
(2,'Facts', 'Course about facts about geometry', 5,120, 2);

INSERT INTO `lessons` VALUES(1,'Cook', 'Lesson about Cook', 1,'INDIVIDUAL'),
(2,'Galua', 'Lesson about Galua', 2,'GROUP'),(3,'Albert', 'Lesson about Gauss', 2,'GROUP');

INSERT INTO `teachers` VALUES(1,'Frank','Woods', 100,'FIXED', 1),(2,'Jason','Hudson', 0, 'FLEXIBLE', 1);

INSERT INTO `lesson_subscriptions` VALUES(1,'2020-1-1',0,1,1,1), (2,'2021-1-1',0,2,2,2),(3,'2021-1-1',0,3,2,2);

INSERT INTO `course_subscriptions` VALUES(1,'2019-1-1','2020-2-2',1,1), (2,'2020-1-1','2021-2-2',2,2);

INSERT INTO `course_reviews` VALUES(1,'Great course',1,1,'2020-3-3'), (2,'Great course',2,2,'2021-3-3');

INSERT INTO `lesson_reviews` VALUES(1,'Great lesson',1,1,'2020-3-3'), (2,'Great lesson',2,2,'2021-3-3'),
(3,'Wonderful lesson',2,3,'2021-3-3');

COMMIT;

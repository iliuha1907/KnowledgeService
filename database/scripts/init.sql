USE `hoteladmin_huzei` ;

START TRANSACTION;

INSERT INTO rooms(status, price, capacity, stars, is_free) VALUES('SERVED',100,3,5,0),('REPAIRED',400,4,4,1),
('SERVED',200,3,5,0); 

INSERT INTO clients(first_name, last_name) VALUES('Ivan', 'Petrov'),('Serge', 'Sidorov'); 

INSERT INTO hotel_services(price, type) VALUES(15, 'SPA'),(10, 'SAUNA');

INSERT INTO visits(client_id, hotel_service_id, date, is_active) VALUES(1,1,'2020-4-11',1),(2,2,'2020-5-11',1);

INSERT INTO reservations(room_id, resident_id, arrival_date, departure_date, is_active)
 VALUES(1,1,'2020-4-10','2020-4-20',1),(3,2,'2020-5-10','2020-5-20',1);
 
INSERT INTO roles(role_type) VALUES('ROLE_ADMIN'),('ROLE_USER');

INSERT INTO users(login,password, roles_id) VALUES('admin','$2y$12$B1oRkco5MUk.5yYy3TCZl.aW7/5HPjp.yuwx/WayWUXB6QT4b1T3q',1),('user','$2y$12$F8C7wGVzn5SykRR7tCPTmuUZ5ziQ5rpRtosX3uhbP6AIOvNyL2Dku',2);

 COMMIT;
 
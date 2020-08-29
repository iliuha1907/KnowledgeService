USE `hoteladmin` ;

START TRANSACTION;

CREATE INDEX id ON Rooms(id);
CREATE INDEX id ON Hotel_services(id);
CREATE INDEX id ON Clients(id);

INSERT INTO Rooms(status, price, capacity, stars, is_free) VALUES('SERVED',100,3,5,0); 
INSERT INTO Rooms(status, price, capacity, stars, is_free) VALUES('REPAIRED',400,4,4,1); 
INSERT INTO Rooms(status, price, capacity, stars, is_free) VALUES('SERVED',200,3,5,0); 

INSERT INTO Clients(first_name, last_name) VALUES('Ivan', 'Petrov'); 
INSERT INTO Clients(first_name, last_name) VALUES('Serge', 'Sidorov'); 

INSERT INTO Hotel_services(price, type) VALUES(15, 'SPA');
INSERT INTO Hotel_services(price, type) VALUES(10, 'SAUNA');

INSERT INTO Visits(client_id, hotel_service_id, date, is_active) VALUES(1,1,'2020-4-11',1);
INSERT INTO Visits(client_id, hotel_service_id, date, is_active) VALUES(2,2,'2020-5-11',1);

INSERT INTO Reservations(room_id, resident_id, arrival_date, departure_date, is_active)
 VALUES(1,1,'2020-4-10','2020-4-20',1);
INSERT INTO Reservations(room_id, resident_id, arrival_date, departure_date, is_active)
 VALUES(3,2,'2020-5-10','2020-5-20',1);
 
 COMMIT;
 
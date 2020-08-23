USE `hoteladmin` ;

START TRANSACTION;

insert into Rooms(status, price, capacity, stars, is_free) values('SERVED',100,3,5,0); 
insert into Rooms(status, price, capacity, stars, is_free) values('REPAIRED',400,4,4,1); 
insert into Rooms(status, price, capacity, stars, is_free) values('SERVED',200,3,5,0); 

insert into Clients(first_name, last_name) values('Ivan', 'Petrov'); 
insert into Clients(first_name, last_name) values('Serge', 'Sidorov'); 

insert into Hotel_services(price, type) values(15, 'SPA');
insert into Hotel_services(price, type) values(20, 'MASSAGE');
insert into Hotel_services(price, type) values(10, 'SAUNA');

insert into Visits(client_id, hotel_service_id, date, is_active) values(1,1,'2020-4-11',1);
insert into Visits(client_id, hotel_service_id, date, is_active) values(2,3,'2020-5-11',1);

insert into Reservations(room_id, resident_id, arrival_date, departure_date, is_active)
 values(1,1,'2020-4-10','2020-4-20',1);
insert into Reservations(room_id, resident_id, arrival_date, departure_date, is_active)
 values(3,2,'2020-5-10','2020-5-20',1);
 
 COMMIT;
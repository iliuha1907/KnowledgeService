USE `huzei_task10`;
/* Задание 1 */
select pc_model, speed, hd from pc where price < 500; 

/* Задание 2 */
select maker from product where type = 'Printer'; 

/* Задание 3 */
select laptop_model, hd, screen from laptop where price > 1000; 

/* Задание 4 */
select * from printer where color = 'y'; 

/* Задание 5 */
select pc_model, speed, hd from pc where (cd = '24x' or cd = '12x') and price < 600; 

/* Задание 6 */
select laptop.speed, product.maker from laptop, product where laptop.laptop_model = product.model and laptop.hd >= 100;

/* Задание 7 */
with devices as(select laptop_model as device_model, price from laptop
union
select pc_model as device_model, price from pc
union
select printer_model as device_model, price from printer) 
select device_model, price from devices, product where device_model = model and maker = 'B'; 
  
/* Задание 8 */
select maker from product where type = 'PC' and type != 'Laptop' group by maker;
 
  /* Задание 9 */
select maker from product inner join pc on product.model = pc.pc_model where pc.speed >= 450 group by maker;
  
  /* Задание 10 */
select printer_model, price from printer where price = (select max(price) from printer);
  
   /* Задание 11 */
select avg(speed) from pc;

   /* Задание 12 */
select avg(speed) from laptop where price > 1000;
 
   /* Задание 13 */
select avg(pc.speed) from pc inner join product on product.model = pc.pc_model where product.maker = 'A';
 
   /* Задание 14 */
select speed, avg(price) from pc group by speed;

   /* Задание 15 */
select hd from pc group by hd having count(*) > 1;
   
   /* Задание 16 */
select distinct t1.pc_model, t2.pc_model from pc t1, pc t2 where t1.speed = t2.speed and
  t1.ram = t2.ram and t1.pc_model > t2.pc_model; 
  
   /* Задание 17 */
select laptop_model from laptop where speed < (select min(speed) from pc);

   /* Задание 18 */
select product.maker, printer.price from product inner join printer on product.model = printer.printer_model
  where printer.price = (select min(price) from printer) and printer.color = 'y'; 

   /* Задание 19 */
select product.maker, avg(laptop.screen) from product inner join laptop on product.model = laptop.laptop_model group by product.maker;
 
   /* Задание 20 */
select  maker, count(type) as 'number' from product where type = 'PC' group by maker having number > 2;

   /* Задание 21 */
select product.maker, max(pc.price) from product inner join pc on product.model = pc.pc_model group by product.maker;
 
   /* Задание 22 */
select speed, avg(price) from pc where speed > 600 group by speed;
  
   /* Задание 23 */
with models as(select pc_model as device_model from pc where speed >= 750
union
select laptop_model as device_model from laptop where speed >= 750) 
select maker from product inner join models on product.model = models.device_model group by maker;
  
    /* Задание 24 */
with prices as(select max(price) as price, pc_model as model from pc group by model
union
select max(price) as price, laptop_model as model from laptop group by model
union
select max(price) as price, printer_model as model from printer group by model) 
select price, model from prices where price = (select max(price) from prices);
  
    /* Задание 25 */
select maker from product inner join printer on printer_model = model where maker in (
select maker from product inner join pc on model = pc_model where ram = (select min(ram) from pc) and
speed = (select max(speed) from pc where ram = (select min(ram) from pc)) group by maker);
 
<<<<<<< HEAD
=======
  
>>>>>>> c881a84d628c48b1c31b729c84726697be7dd8c9

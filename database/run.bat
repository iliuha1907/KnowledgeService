@echo off
mysql -h 127.0.0.1 -u root -p < scripts/HotelAdminModel.sql < scripts/init.sql
PAUSE
@echo off
mysql -u root -p < scripts/HotelAdminModel.sql
mysql -u root -p < scripts/init.sql
PAUSE
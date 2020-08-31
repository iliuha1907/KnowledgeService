@echo off
mysql -u root -p < scripts/HotelAdminModelCreation.sql
mysql -u root -p < scripts/init.sql
PAUSE
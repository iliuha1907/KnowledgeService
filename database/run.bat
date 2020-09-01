@echo off
mysql -u root -p < scripts/hotelAdminModelCreation.sql
mysql -u root -p < scripts/init.sql
PAUSE
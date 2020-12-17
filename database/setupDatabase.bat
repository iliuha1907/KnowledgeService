@echo off
mysql -u root -p < scripts/CreateScript.sql
mysql -u root -p < scripts/InitScript.sql
PAUSE
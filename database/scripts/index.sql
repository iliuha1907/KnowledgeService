USE `hoteladmin` ;

START TRANSACTION;

CREATE INDEX id ON Rooms(id);
CREATE INDEX id ON Hotel_services(id);
CREATE INDEX id ON Clients(id);

COMMIT;


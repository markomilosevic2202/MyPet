INSERT specialty (id, name_specialty)  VALUES ('1' ,'Oftamolog');
INSERT specialty (id, name_specialty)  VALUES (UUID() ,'Kardiolog');
INSERT specialty (id, name_specialty)  VALUES (UUID() ,'Opste prakse');
INSERT specialty (id, name_specialty)  VALUES (UUID() ,'Gastrolog');
INSERT specialty (id, name_specialty)  VALUES (UUID() ,'Ortoped');
INSERT  users (id, first_name, last_name, email, admin, created_at, updated_at) VALUES (UUID(), 'Marko', 'Milosevic', 'kraljevicmarko820@gmail.com', false, NOW(), NOW());
INSERT  users (id, first_name, last_name, email, admin, created_at, updated_at) VALUES (UUID(), 'Uros', 'Jovanovic', 'uros@lumenspei.com', false, NOW(), NOW());
UPDATE users SET admin = true WHERE first_name = 'Marko';
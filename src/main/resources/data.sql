DELETE RESTAURANT;
DELETE RESTAURANT_TABLE;
DELETE CUSTOMER;
DELETE RESERVATION;

INSERT INTO RESTAURANT(RESTAURANT_ID, NAME, LOCATION) VALUES (1, 'Tacos', 'London');
INSERT INTO RESTAURANT(RESTAURANT_ID, NAME, LOCATION) VALUES (2, 'CrazyPizza', 'Paris');

INSERT INTO RESTAURANT_TABLE(TABLE_ID, CAPACITY, RESTAURANT_ID) VALUES (1, 2, 1);
INSERT INTO RESTAURANT_TABLE(TABLE_ID, CAPACITY, RESTAURANT_ID) VALUES (2, 6, 1);
INSERT INTO RESTAURANT_TABLE(TABLE_ID, CAPACITY, RESTAURANT_ID) VALUES (3, 3, 1);
INSERT INTO RESTAURANT_TABLE(TABLE_ID, CAPACITY, RESTAURANT_ID) VALUES (4, 3, 1);
INSERT INTO RESTAURANT_TABLE(TABLE_ID, CAPACITY, RESTAURANT_ID) VALUES (5, 3, 1);
INSERT INTO RESTAURANT_TABLE(TABLE_ID, CAPACITY, RESTAURANT_ID) VALUES (6, 3, 2);

INSERT INTO CUSTOMER(CUSTOMER_ID, NAME, PHONE_NUMBER) VALUES (1, 'James Green', '0756985234');
INSERT INTO CUSTOMER(CUSTOMER_ID, NAME, PHONE_NUMBER) VALUES (2, 'Jack Ryan', '074568923');

INSERT INTO RESERVATION (RESTAURANT_ID, CUSTOMER_ID, RESTAURANT_TABLE_ID, RES_DATE, PARTY_SIZE)
VALUES (1, 1, 1, '2020-12-25', 2);

INSERT INTO RESERVATION (RESTAURANT_ID, CUSTOMER_ID, RESTAURANT_TABLE_ID, RES_DATE, PARTY_SIZE)
VALUES (1, 2, 2, '2020-12-25', 2);

INSERT INTO RESERVATION (RESTAURANT_ID, CUSTOMER_ID, RESTAURANT_TABLE_ID, RES_DATE, PARTY_SIZE)
VALUES (1, 2, 3, '2020-12-24', 2);

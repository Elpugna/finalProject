
create table address (
   id  bigserial not null,
    address varchar(255),
    customer_id int8,
    primary key (id)
);

create table checkout (
   id  bigserial not null,
    email varchar(255),
    user_name varchar(255),
    address_id int8,
    payment_method_id int8,
    primary key (id)
);

create table checkout_item (
   quantity int8 not null check (quantity>=1),
    product_id int8 not null,
    checkout_id int8 not null,
    primary key (checkout_id, product_id)
);

create table customer (
   id  bigserial not null,
    auth_server_id varchar(255),
    email varchar(255),
    first_name varchar(255),
    last_login timestamp,
    last_name varchar(255),
    user_name varchar(255),
    primary key (id)
);

create table order_item (
   price float8 not null,
    quantity int8 not null check (quantity>=1),
    product_id int8 not null,
    order_id int8 not null,
    primary key (order_id, product_id)
);

create table orders (
   id  bigserial not null,
    created_at timestamp,
    address varchar(255),
    status int4,
    email varchar(255),
    payment_method varchar(255),
    total_price float8 not null,
    user_name varchar(255),
    auth_server_id varchar(255),
    primary key (id)
);

create table payment_method (
   id  bigserial not null,
    payment_method varchar(255),
    customer_id int8,
    primary key (id)
);

create table products (
   id  bigserial not null,
    name varchar(255) not null,
    price float8 not null,
    primary key (id)
);

alter table if exists checkout
   add constraint UK_hrir0uqxr54msq1ala61bjefv unique (email);

alter table if exists customer
   add constraint UK_nonbx33y5nkpeeohhjs6r18c0 unique (email);

alter table if exists address
   add constraint FK93c3js0e22ll1xlu21nvrhqgg
   foreign key (customer_id)
   references customer;

alter table if exists checkout
   add constraint FKn6t52ojsbww8h4eu09ge0nneq
   foreign key (address_id)
   references address;

alter table if exists checkout
   add constraint FK1dodh7etoiby3irf92h05dc6j
   foreign key (payment_method_id)
   references payment_method;


alter table if exists checkout_item
   add constraint FKkyu80mi3hr6slvpvcqhfuxw0j
   foreign key (product_id)
   references products;

alter table if exists checkout_item
   add constraint FK3s3vgaf23cmakh2w8bn7y6st3
   foreign key (checkout_id)
   references checkout;

alter table if exists order_item
   add constraint FKc5uhmwioq5kscilyuchp4w49o
   foreign key (product_id)
   references products;

alter table if exists order_item
   add constraint FKt4dc2r9nbvbujrljv3e23iibt
   foreign key (order_id)
   references orders;

alter table if exists payment_method
   add constraint FK43slmstjfk0uhikc9krcvhn41
   foreign key (customer_id)
   references customer;



INSERT INTO products (id,name,price) VALUES
(1,'Orange',2.34),
 (2,'Onion', 3.00),
 (3,'Thyme', 2.13),
 (4,'Potatoe',1.00),
 (5,'Broccoli',6.33),
 (6,'Strawberry',4.22),
 (7,'Pepper',2.00),
 (8,'Oil',3.75),
 (9,'Banana',5.00),
 (10,'Peanut',0.25);

INSERT INTO customer (id, auth_server_id,email,first_name,last_login,last_name,user_name) VALUES
 (1,'6a781a47-cd59-4576-af43-329a99d09618','john@doe.com','John',null,'Doe','johndoe'),
 (2,'df968d67-5f4f-46b3-b491-9150b215fd13','jane@doe.com','Jane',null,'Doe','janedoe'),
 (3,'00000000-0000-0000-0000-000000000000','Thete@stuser.com','Thete',null,'Stuser','thetestuser');

INSERT INTO address (id, address, customer_id) VALUES
 (1, '221B Baker Street', 1),
 (2, 'Privet Drive 4', 1),
 (3, '742 Evergreen Terrace', 2),
 (4, 'Mockingbird Lane', 2);

INSERT INTO payment_method (id, payment_method, customer_id) VALUES
 (1, 'Visa', 1),
 (2, 'Bank transfer', 1),
 (3, 'Mastercard', 2),
 (4, 'Paypal', 2);

INSERT INTO orders (id, created_at, address, status, email, payment_method, total_price, user_name, auth_server_id) values
 (1, null, 'Mockingbird Lane',2,'jane@doe.com', 'Paypal', 22.68, 'janedoe','df968d67-5f4f-46b3-b491-9150b215fd13'),
 (2, null, 'Mockingbird Lane',1,'jane@doe.com', 'Mastercard', 32.00, 'janedoe','df968d67-5f4f-46b3-b491-9150b215fd13'),
 (3, null, '742 Evergreen Terrace',0,'jane@doe.com', 'Paypal', 26.66, 'janedoe','df968d67-5f4f-46b3-b491-9150b215fd13'),
 (4, null, 'En ningun lugar',1,'test@test.com', 'Paypal', 13.00, 'test','00000000-0000-0000-0000-000000000000');

INSERT INTO order_item (price,quantity,product_id,order_id) VALUES
--order 1
 (2.34, 3, 1, 1),
 (3.00, 1, 2, 1),
 (6.33, 2, 5, 1),
 --order 2
 (2.00, 5, 7, 2),
 (5.00, 4, 9, 2),
 (0.25, 8, 10, 2),
--order 3
 (1.00, 3, 4, 3),
 (2.00, 1, 7, 3),
 (3.00, 3, 2, 3),
 (4.22, 3, 6, 3),
--order 4
 (2.00, 6, 7, 4),
 (1.00, 1, 4, 4);


INSERT INTO checkout (id, email, user_name ,address_id, payment_method_id) values
 (1, 'john@doe.com', 'johndoe', 1, null),
 (2, 'jane@doe.com', 'janedoe', null, 2),
 (3, 'ran@dom.com', 'random', null, null), --TESTEAR QUE SI ELIMINO LOS PRODUCTOS SE BORRE EL CHECKOUT
 (4, 'test@example.com', 'test', null, null);


INSERT INTO checkout_item (quantity, product_id, checkout_id) VALUES
--order 1
 (3, 1, 1),
 (1, 2, 1),
 (2, 5, 1),
 --order 2
 (5, 7, 2),
 (4, 9, 2),
 (8, 10, 2),
--order 3
 (3, 4, 3),
--order 4
 (6, 7, 4),
 (1, 4, 4);


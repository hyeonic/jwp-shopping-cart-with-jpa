set foreign_key_checks = 0;
truncate table customer;
truncate table product;
truncate table cart_item;
truncate table orders;
truncate table orders_detail;
set foreign_key_checks = 1;

insert into customer (username, email, password, address, phone_number)
values ('puterism', 'puterism@email.com', '1234567890', 'test', '000-0000-0000');

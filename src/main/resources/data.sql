DROP TABLE IF EXISTS ordine;
create table ordine (
    id bigint primary key AUTO_INCREMENT,
    customer_name varchar(255),
    pizza_type varchar(255),
    status varchar(255),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP);

insert into ordine (customer_name, pizza_type, status) values ('Mario', 'MARGHERITA', 'IN_CODA');
insert into ordine (customer_name, pizza_type, status) values ('Giuseppe', 'DIAVOLA', 'IN_CODA');
insert into ordine (customer_name, pizza_type, status) values ('Franco', 'CAPRICCIOSA', 'IN_CODA');
insert into ordine (customer_name, pizza_type, status) values ('Angela', 'PATATE_SALSICCIA', 'IN_CODA');
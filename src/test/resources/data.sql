DROP TABLE IF EXISTS ordine;
create table ordine (
    id bigint primary key AUTO_INCREMENT,
    customer_name varchar(255),
    pizza_type varchar(255),
    status varchar(255),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP);
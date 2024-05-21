--SET search_path TO public;
--here we have schema problem careful
CREATE TABLE users
(
    id         serial,
    primary key (id),
    first_name varchar(255),
    last_name  varchar(255),
    user_name varchar(255),
    password varchar(255)

);
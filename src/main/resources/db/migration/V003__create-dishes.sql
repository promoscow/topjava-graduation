create sequence dishes_seq start with 1 increment by 1;

create table dishes
(
    id            bigint auto_increment sequence dishes_seq
        primary key,
    name          varchar(255),
    price         numeric(19, 2),
    active        boolean,
    restaurant_id long references restaurants (id)
);
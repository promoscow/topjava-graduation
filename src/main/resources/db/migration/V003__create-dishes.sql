create table dishes
(
    id            bigint auto_increment
        primary key,
    name          varchar(255),
    price         numeric(19, 2),
    restaurant_id long references restaurants (id)
);
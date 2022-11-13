create sequence restaurants_seq start with 1 increment by 1;

create table restaurants
(
    id   bigint auto_increment sequence restaurants_seq
        primary key,
    name varchar(255)
);
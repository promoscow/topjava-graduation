create sequence roles_seq start with 1 increment by 1;

create table roles
(
    id   bigint auto_increment sequence roles_seq
        primary key,
    name varchar(255)
);
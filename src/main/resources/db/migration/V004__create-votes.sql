create sequence votes_seq start with 1 increment by 1;

create table votes
(
    id            bigint auto_increment sequence votes_seq
        primary key,
    date          date,
    user_id       long references users (id),
    restaurant_id long references restaurants (id),
    constraint ux_date_user unique (date, user_id)
);
create table votes
(
    id            bigint auto_increment
        primary key,
    date          date,
    user_id       long references users (id),
    restaurant_id long references restaurants (id)
);
insert into users (id, username, password)
values (1, 'whiteRabbit111', '123'),
       (2, 'chiller2', '123'),
       (3, 'bestHorse', '123'),
       (4, 'veryBestEmployee777', '123'),
       (5, 'pooh', '123');

alter sequence users_seq restart with 6;
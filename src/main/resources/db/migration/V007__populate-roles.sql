insert into roles(id, name)
values (1, 'ADMIN'),
       (2, 'USER');

alter sequence roles_seq restart with 3;
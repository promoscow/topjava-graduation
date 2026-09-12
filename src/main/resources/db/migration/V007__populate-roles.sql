insert into roles(id, name)
values (1, 'USER'),
       (2, 'ADMIN');

alter table roles alter column id restart with 3;

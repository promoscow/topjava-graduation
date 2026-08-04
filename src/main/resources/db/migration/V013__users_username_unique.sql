alter table users
    add constraint ux_users_username unique (username);

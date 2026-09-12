create table users_roles
(
    user_id bigint references users (id),
    role_id bigint references roles (id)
);

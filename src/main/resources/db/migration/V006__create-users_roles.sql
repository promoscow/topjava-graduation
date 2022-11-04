create table users_roles
(
    user_id long references users (id),
    role_id long references roles (id)
);
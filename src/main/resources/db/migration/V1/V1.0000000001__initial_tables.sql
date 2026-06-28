create table if not exists users
(
    id              bigserial              primary key,
    created_at      timestamp              not null default now(),
    updated_at      timestamp              not null default now(),
    email           varchar(255)           not null unique,
    username        varchar(255)           not null unique,
    phone_number    varchar(50)            unique,
    password        varchar(255),
    role            varchar(50)            not null,
    provider        varchar(50)            not null,
    provider_id     varchar(255)
);

create table if not exists attachments
(
    id           bigserial    primary key,
    created_at   timestamp    not null default now(),
    updated_at   timestamp    not null default now(),
    uuid         varchar(36)  not null unique,
    file_name    varchar(512) not null,
    content_type varchar(255) not null,
    size         bigint       not null,
    storage_key  varchar(1024) not null
);

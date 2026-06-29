create table if not exists users
(
    id              bigserial              primary key,
    created_at      timestamp              not null default now(),
    updated_at      timestamp              not null default now(),
    created_by      bigint references users (id),
    updated_by      bigint references users (id),
    email           varchar(255)           not null unique,
    username        varchar(255)           not null unique,
    phone_number    varchar(50)            unique,
    password        varchar(255),
    role            varchar(50)            not null,
    provider        varchar(50)            not null,
    provider_id     varchar(255)
);

create table if not exists project
(
    id              bigserial                      primary key,
    created_at      timestamp                      not null default now(),
    updated_at      timestamp                      not null default now(),
    created_by      bigint references users (id)   not null,
    updated_by      bigint references users (id),
    name            varchar(50)                    not null,
    description     text,
    owner_id         bigint references users (id)  not null,
    status          varchar(50)                    not null
);

create table if not exists project_member
(
    id               bigserial                       primary key,
    created_at       timestamp                       not null default now(),
    updated_at       timestamp                       not null default now(),
    created_by       bigint references users (id)    not null,
    updated_by       bigint references users (id),
    project_id        bigint references project (id) not null,
    user_id           bigint references users (id)    not null,
    role             varchar(50)                     not null,
    invitationStatus varchar(50)                     not null
);

create table if not exists board
(
    id               bigserial                       primary key,
    created_at       timestamp                       not null default now(),
    updated_at       timestamp                       not null default now(),
    created_by       bigint references users (id)    not null,
    updated_by       bigint references users (id),
    project_id        bigint references project (id) not null,
    name             varchar(50)                     not null,
    description      text,
    status           varchar(50)                     not null,
    position         int                             not null
);

create table if not exists board_column
(
    id               bigserial                       primary key,
    created_at       timestamp                       not null default now(),
    updated_at       timestamp                       not null default now(),
    created_by       bigint references users (id)    not null,
    updated_by       bigint references users (id),
    board_id          bigint references board (id)   not null,
    position         int                             not null,
    name             varchar(50)                     not null
);


create table if not exists task
(
    id                bigserial                           primary key,
    created_at        timestamp                           not null default now(),
    updated_at        timestamp                           not null default now(),
    created_by        bigint references users (id)        not null,
    updated_by        bigint references users (id),
    board_column_id   bigint references board_column (id) not null,
    title             varchar(50)                         not null,
    description       text,
    priority          varchar(50)                         not null,
    due_date          timestamp                           not null,
    position         int                             not null
);

create table if not exists task_assignee
(
    task_id       bigint       not null,
    user_id       bigint       not null,
    primary key (task_id, user_id),
    foreign key (task_id) references task(id),
    foreign key (user_id) references users(id)
);

create table if not exists tag
(
    id              bigserial                      primary key,
    project_id       bigint references project(id) not null,
    name            varchar(50)                    not null,
    color           varchar(50)                    not null
);

create table if not exists task_tag
(
    task_id       bigint       not null,
    tag_id        bigint       not null,
    primary key (task_id, tag_id),
    foreign key (task_id) references task(id),
    foreign key (tag_id) references tag(id)
);

create table if not exists comment
(
    id               bigserial                      primary key,
    created_at       timestamp                      not null default now(),
    updated_at       timestamp                      not null default now(),
    task_id           bigint references task (id)    not null,
    author_id         bigint references users (id)  not null,
    text             text                           not null,
    parent_id        bigint references comment(id)
);

create table if not exists attachments
(
    id           bigserial    primary key,
    created_at   timestamp    not null default now(),
    updated_at   timestamp    not null default now(),
    created_by   bigint references users (id),
    updated_by   bigint references users (id),
    uuid         varchar(36)  not null unique,
    task_id      bigint references task (id),
    file_name    varchar(512) not null,
    content_type varchar(255) not null,
    size         bigint       not null,
    storage_key  varchar(1024) not null
    );


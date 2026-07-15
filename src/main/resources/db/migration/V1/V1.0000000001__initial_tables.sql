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
    owner_id        bigint references users (id)   not null,
    status          varchar(50)                    not null
);

create table if not exists project_member
(
    id               bigserial                       primary key,
    created_at       timestamp                       not null default now(),
    updated_at       timestamp                       not null default now(),
    created_by       bigint references users (id)    not null,
    updated_by       bigint references users (id),
    project_id       bigint references project (id)  not null,
    user_id          bigint references users (id)    not null,
    role             varchar(50)                     not null,
    invitation_status varchar(50)                     not null,
    unique(project_id, user_id)
);

create table if not exists board
(
    id               bigserial                       primary key,
    created_at       timestamp                       not null default now(),
    updated_at       timestamp                       not null default now(),
    created_by       bigint references users (id)    not null,
    updated_by       bigint references users (id),
    project_id       bigint references project (id)  not null,
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
    board_id         bigint references board (id)    not null,
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
    position          int                             not null
);

create table if not exists m2m_user_to_tasks
(
    user_id bigint not null references users(id) on delete cascade,
    task_id    bigint not null references task(id) on delete cascade,
    primary key (user_id, task_id)
);

create table if not exists tag
(
    id              bigserial                      primary key,
    project_id      bigint references project(id)  not null,
    name            varchar(50)                    not null,
    color           varchar(50)                    not null
);

create table if not exists m2m_task_tag_to_project
(
    project_id bigint not null references project(id) on delete cascade,
    task_id    bigint not null references task(id) on delete cascade,
    primary key (project_id, task_id)
);


create table if not exists comment
(
    id               bigserial                      primary key,
    created_at       timestamp                      not null default now(),
    updated_at       timestamp                      not null default now(),
    task_id          bigint references task (id)    not null,
    author_id        bigint references users (id)   not null,
    description      text                           not null,
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

create table if not exists m2m_tag_to_project
(
    project_id bigint not null references project(id) on delete cascade,
    tag_id     bigint not null references tag(id) on delete cascade,
    primary key (project_id, tag_id)
);

create table if not exists board_member
(
    id                  bigserial                            primary key,
    created_at          timestamp                            not null default now(),
    updated_at          timestamp                            not null default now(),
    created_by          bigint references users(id)          not null,
    updated_by          bigint references users(id),
    board_id            bigint references board(id)          not null,
    project_member_id   bigint references project_member(id) not null,
    role                varchar(50)                          not null,
    unique(board_id, project_member_id)
);
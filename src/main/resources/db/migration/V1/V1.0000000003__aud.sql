create schema if not exists aud;

create table if not exists revinfo
(
    rev        bigserial not null primary key,
    revtstmp   bigint,
    auditor_id bigint    references users (id)
);

create table if not exists aud.users_aud
(
    id           bigserial not null,
    created_at   timestamp,
    updated_at   timestamp,
    created_by   bigint,
    updated_by   bigint,
    email        varchar(255),
    username     varchar(255),
    phone_number varchar(50),
    password     varchar(255),
    role         varchar(50),
    provider     varchar(50),
    provider_id  varchar(255),
    rev          bigint    not null references revinfo (rev),
    revtype      smallint,
    constraint users_aud_pkey
    primary key (id, rev)
);

create table if not exists aud.project_aud
(
    id          bigserial not null,
    created_at  timestamp,
    updated_at  timestamp,
    created_by  bigint,
    updated_by  bigint,
    name        varchar(50),
    description text,
    owner_id    bigint,
    status      varchar(50),
    rev         bigint    not null references revinfo (rev),
    revtype     smallint,
    constraint project_aud_pkey
    primary key (id, rev)
);

create table if not exists aud.project_member_aud
(
    id                bigserial not null,
    created_at        timestamp,
    updated_at        timestamp,
    created_by        bigint,
    updated_by        bigint,
    project_id        bigint,
    user_id           bigint,
    role              varchar(50),
    invitation_status varchar(50),
    rev               bigint    not null references revinfo (rev),
    revtype           smallint,
    constraint project_member_aud_pkey
    primary key (id, rev)
);

create table if not exists aud.board_aud
(
    id          bigserial not null,
    created_at  timestamp,
    updated_at  timestamp,
    created_by  bigint,
    updated_by  bigint,
    project_id  bigint,
    name        varchar(50),
    description text,
    status      varchar(50),
    position    int,
    rev         bigint    not null references revinfo (rev),
    revtype     smallint,
    constraint board_aud_pkey
    primary key (id, rev)
);

create table if not exists aud.board_column_aud
(
    id         bigserial not null,
    created_at timestamp,
    updated_at timestamp,
    created_by bigint,
    updated_by bigint,
    board_id   bigint,
    position   int,
    name       varchar(50),
    rev        bigint    not null references revinfo (rev),
    revtype    smallint,
    constraint board_column_aud_pkey
    primary key (id, rev)
);

create table if not exists aud.task_aud
(
    id              bigserial not null,
    created_at      timestamp,
    updated_at      timestamp,
    created_by      bigint,
    updated_by      bigint,
    board_column_id bigint,
    title           varchar(50),
    description     text,
    priority        varchar(50),
    due_date        timestamp,
    position        int,
    rev             bigint    not null references revinfo (rev),
    revtype         smallint,
    constraint task_aud_pkey
    primary key (id, rev)
);

create table if not exists aud.task_assignee_aud
(
    id      bigserial not null,
    task_id bigint,
    user_id bigint,
    rev     bigint    not null references revinfo (rev),
    revtype smallint,
    constraint task_assignee_aud_pkey
    primary key (id, rev)
);

create table if not exists aud.tag_aud
(
    id         bigserial not null,
    project_id bigint,
    name       varchar(50),
    color      varchar(50),
    rev        bigint    not null references revinfo (rev),
    revtype    smallint,
    constraint tag_aud_pkey
    primary key (id, rev)
);

create table if not exists aud.task_tag_aud
(
    id      bigserial not null,
    task_id bigint,
    tag_id  bigint,
    rev     bigint    not null references revinfo (rev),
    revtype smallint,
    constraint task_tag_aud_pkey
    primary key (id, rev)
);

create table if not exists aud.comment_aud
(
    id         bigserial not null,
    created_at timestamp,
    updated_at timestamp,
    task_id    bigint,
    author_id  bigint,
    text       text,
    parent_id  bigint,
    rev        bigint    not null references revinfo (rev),
    revtype    smallint,
    constraint comment_aud_pkey
    primary key (id, rev)
);

create table if not exists aud.attachments_aud
(
    id           bigserial not null,
    created_at   timestamp,
    updated_at   timestamp,
    created_by   bigint,
    updated_by   bigint,
    uuid         varchar(36),
    task_id      bigint,
    file_name    varchar(512),
    content_type varchar(255),
    size         bigint,
    storage_key  varchar(1024),
    rev          bigint    not null references revinfo (rev),
    revtype      smallint,
    constraint attachments_aud_pkey
    primary key (id, rev)
);

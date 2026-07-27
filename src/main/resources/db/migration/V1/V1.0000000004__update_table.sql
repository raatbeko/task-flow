drop table if exists m2m_task_tag_to_project;

create table if not exists m2m_task_to_tag
(
    task_id bigint not null references task(id) on delete cascade,
    tag_id  bigint not null references tag(id) on delete cascade,
    primary key (task_id, tag_id)
);

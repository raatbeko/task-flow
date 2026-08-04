alter table task add constraint uq_task_column_position unique (board_column_id, position);
alter table board add constraint uq_board_project_position unique (project_id, position);
alter table board_column add constraint uq_column_board_position unique (board_id, position);
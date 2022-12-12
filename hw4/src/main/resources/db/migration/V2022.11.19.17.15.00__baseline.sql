create table if not exists boards
(
    board_id uuid        not null primary key,
    name     varchar(32) not null
);

create type task_status as enum ('DONE', 'NOT_DONE');

create table if not exists tasks
(
    task_id     uuid        not null primary key,
    name        varchar(32) not null,
    description text        not null,
    status      task_status not null,
    board_id    uuid        not null references boards (board_id) on delete cascade
);

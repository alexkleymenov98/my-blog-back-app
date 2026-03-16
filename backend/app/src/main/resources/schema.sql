-- Таблица с постами
create table if not exists posts(
    id bigserial primary key,
    title varchar(256) not null,
    text varchar(1000) not null,
    likes_count integer default 0,
    tags text[] default '{}'::varchar(256)[]
);

-- Таблица с постами
create table if not exists comments(
    id bigserial primary key,
    text varchar(100) not null,
    post_id INTEGER REFERENCES posts(id) ON DELETE CASCADE
);

-- Таблица с постами
create table if not exists post_images(
    id bigserial primary key,
    post_id integer not null,
    file_name varchar(256) not null
);


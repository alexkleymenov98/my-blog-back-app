DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS post_images;

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

-- Добавляем 3 поста
INSERT INTO posts (title, text, likes_count, tags) VALUES
                                                               ('Первый пост', 'Содержимое первого поста. Добро пожаловать в блог! Содержимое первого поста. Добро пожаловать в блог! Содержимое первого поста. Добро пожаловать в блог! Содержимое первого поста. Добро пожаловать в блог! Содержимое первого поста. Добро пожаловать в блог! Содержимое первого поста. Добро пожаловать в блог!' ||
                                                                               'Содержимое первого поста. Добро пожаловать в блог! Содержимое первого поста. Добро пожаловать в блог! Содержимое первого поста. Добро пожаловать в блог! Содержимое первого поста. Добро пожаловать в блог!', 15,ARRAY['java', 'spring']),
                                                               ('Второй пост', 'Учимся работать с PostgreSQL и Spring MVC. Практическое руководство.', 8,ARRAY['database', 'tutorial']),
                                                               ('Третий пост', 'Как настроить Docker для разработки. Пошаговая инструкция.',  33,ARRAY['docker', 'devops']),
('Первый пост 4', 'Содержимое первого поста. Добро пожаловать в блог!', 15,ARRAY['java', 'spring']),
('Второй пост 5', 'Учимся работать с PostgreSQL и Spring MVC. Практическое руководство.', 8,ARRAY['database', 'tutorial']),
('Третий пост 6', 'Как настроить Docker для разработки. Пошаговая инструкция.',  33,ARRAY['docker', 'devops']),
                                                               ('Первый пост 7', 'Содержимое первого поста. Добро пожаловать в блог!', 15,ARRAY['java', 'spring']),
                                                               ('Второй пост 8', 'Учимся работать с PostgreSQL и Spring MVC. Практическое руководство.', 8,ARRAY['database', 'tutorial']),
                                                               ('Третий пост 9', 'Как настроить Docker для разработки. Пошаговая инструкция.',  33,ARRAY['docker', 'devops']),
                                                               ('Первый пост 10', 'Содержимое первого поста. Добро пожаловать в блог!', 15,ARRAY['java', 'spring']),
                                                               ('Второй пост 11', 'Учимся работать с PostgreSQL и Spring MVC. Практическое руководство.', 8,ARRAY['database', 'tutorial']),
                                                               ('Третий пост 12', 'Как настроить Docker для разработки. Пошаговая инструкция.',  33,ARRAY['docker', 'devops']);

-- Добавляем по 2 комментария к каждому посту (всего 6)
INSERT INTO comments (text, post_id) VALUES
                                                 -- Комментарии к посту 1
                                                 ('Отличный пост, спасибо!', 1),
                                                 ('Очень полезная информация', 1),

                                                 -- Комментарии к посту 2
                                                 ('Наконец-то понял как это работает', 2),
                                                 ('А можно подробнее про настройку?', 2),

                                                 -- Комментарии к посту 3
                                                 ('Лучший туториал по Docker', 3),
                                                 ('Спасибо, помогло!', 3);

INSERT INTO post_images (file_name, post_id) VALUES
                                         -- Комментарии к посту 1
                                         ('Графики (1).png', 1),
                                         ('Графики (4).png', 2),
                                         ('Графики (6).png', 3)

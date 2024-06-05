CREATE TABLE task_to_categories (
   id serial PRIMARY KEY,
   task_id int not null REFERENCES tasks(id),
   categories_id int not null REFERENCES categories(id),
   UNIQUE (task_id, categories_id)
);
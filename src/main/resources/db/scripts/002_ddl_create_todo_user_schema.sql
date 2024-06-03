CREATE TABLE todo_user (
   id SERIAL PRIMARY KEY,
   name varchar not null,
   login varchar not null,
   password varchar not null
);
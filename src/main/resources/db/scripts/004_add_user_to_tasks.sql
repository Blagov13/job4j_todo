ALTER TABLE tasks
ADD COLUMN todo_user_id INT NOT NULL REFERENCES todo_user(id);


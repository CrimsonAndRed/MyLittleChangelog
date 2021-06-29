CREATE TABLE projects (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT,
    "order" SERIAL,
    user_id INTEGER NOT NULL REFERENCES users(id)
);

ALTER TABLE versions ADD COLUMN project_id INT;
ALTER TABLE versions ADD CONSTRAINT versions_project_fk FOREIGN KEY (project_id) REFERENCES projects (id);
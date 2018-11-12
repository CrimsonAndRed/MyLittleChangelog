CREATE TABLE project (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  description TEXT,
  create_user INTEGER NOT NULL REFERENCES app_user(id),
  create_date TIMESTAMP NOT NULL DEFAULT now(),
  update_date TIMESTAMP NOT NULL DEFAULT now(),
  version INT NOT NULL DEFAULT 0
);

COMMENT ON TABLE project IS 'Registered projects.';

COMMENT ON COLUMN project.id IS 'Project id';
COMMENT ON COLUMN project.name IS 'Project name';
COMMENT ON COLUMN project.description IS 'Project description';
COMMENT ON COLUMN project.create_date IS 'Project create date';
COMMENT ON COLUMN project.update_date IS 'Last time update date';
COMMENT ON COLUMN project.version IS 'Row version';
COMMENT ON COLUMN project.create_user IS 'Owner on project';
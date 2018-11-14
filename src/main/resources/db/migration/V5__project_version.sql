CREATE TABLE project_version (
    id SERIAL PRIMARY KEY,
    num TEXT NOT NULL,
    description TEXT,
    project_id INT NOT NULL REFERENCES project(id),
    internal_order INT NOT NULL,

    create_date TIMESTAMP NOT NULL DEFAULT now(),
    update_date TIMESTAMP NOT NULL DEFAULT now(),
    v INT NOT NULL DEFAULT 0
);

COMMENT ON TABLE project_version IS 'Project version.';

COMMENT ON COLUMN project_version.id IS 'Project version id';
COMMENT ON COLUMN project_version.num IS 'Project version number';
COMMENT ON COLUMN project_version.description IS 'Project description';
COMMENT ON COLUMN project_version.project_id IS 'Parent project';
COMMENT ON COLUMN project_version.internal_order IS 'Order of versions of parent project';

COMMENT ON COLUMN project_version.create_date IS 'Project create date';
COMMENT ON COLUMN project_version.update_date IS 'Last time update date';
COMMENT ON COLUMN project_version.v IS 'Row version';
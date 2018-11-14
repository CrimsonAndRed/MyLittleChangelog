CREATE TABLE changelog_route (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    project_id INT NOT NULL REFERENCES project(id),

    create_date TIMESTAMP NOT NULL DEFAULT now(),
    update_date TIMESTAMP NOT NULL DEFAULT now(),
    v INT NOT NULL DEFAULT 0
);


COMMENT ON TABLE changelog_route IS 'Route for changelog.';

COMMENT ON COLUMN changelog_route.id IS 'Route id';
COMMENT ON COLUMN changelog_route.name IS 'Route name';
COMMENT ON COLUMN changelog_route.project_id IS 'Linked project';

COMMENT ON COLUMN changelog_route.create_date IS 'Route create date';
COMMENT ON COLUMN changelog_route.update_date IS 'Route last update date';
COMMENT ON COLUMN changelog_route.v IS 'Row version';
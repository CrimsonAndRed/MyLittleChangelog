CREATE TABLE changelog (
    id SERIAL PRIMARY KEY,
    vid INT NOT NULL,
    text TEXT,
    version_id INT NOT NULL REFERENCES project_version(id),
    route_id INT NOT NULL REFERENCES changelog_route(id),
    is_deleted BOOL NOT NULL DEFAULT false,

    create_date TIMESTAMP NOT NULL DEFAULT now(),
    update_date TIMESTAMP NOT NULL DEFAULT now(),
    v INT NOT NULL DEFAULT 0
);


COMMENT ON TABLE changelog IS 'Logging smallest unit';

COMMENT ON COLUMN changelog.id IS 'Changelog id';
COMMENT ON COLUMN changelog.vid IS 'Changelog vid. It connects same changelog through time';
COMMENT ON COLUMN changelog.text IS 'Changelog text.';
COMMENT ON COLUMN changelog.version_id IS 'Version of changelog unit';
COMMENT ON COLUMN changelog.route_id IS 'Route of changelog';

COMMENT ON COLUMN changelog.create_date IS 'Changelog create date';
COMMENT ON COLUMN changelog.update_date IS 'Changelog last update date';
COMMENT ON COLUMN changelog.v IS 'Row version';
COMMENT ON COLUMN changelog.is_deleted IS 'Changelog deletion flag. Not literally the same as soft deletion';
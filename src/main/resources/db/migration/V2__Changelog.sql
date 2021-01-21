CREATE SEQUENCE versions_seq;
ALTER TABLE versions ALTER COLUMN id SET DEFAULT nextval('versions_seq');
ALTER SEQUENCE versions_seq OWNED BY versions.id;

CREATE TABLE leaves (
    id SERIAL PRIMARY KEY,
    vid INT,
    name TEXT,
    value_type INT,
    value TEXT,
    version_id INT REFERENCES versions (id),
    group_vid INT
);
CREATE TABLE groups (
    id SERIAL PRIMARY KEY,
    vid INT,
    name TEXT,
    parent_vid INT,
    version_id INT REFERENCES versions (id)
);
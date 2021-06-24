CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    login text,
    password bytea
);

CREATE TABLE versions (
    id SERIAL PRIMARY KEY,
    name TEXT,
    "order" SERIAL,
    user_id INT REFERENCES users (id)
);

CREATE TABLE groups (
    id SERIAL PRIMARY KEY,
    vid SERIAL,
    name TEXT,
    parent_vid INT,
    version_id INT REFERENCES versions (id),
    "order" SERIAL,
    is_deleted BOOLEAN
);

ALTER TABLE groups ADD CONSTRAINT groups_unq_vid_version UNIQUE (vid, version_id);

CREATE TABLE leaves (
    id SERIAL PRIMARY KEY,
    vid SERIAL,
    name TEXT,
    value_type INT,
    value TEXT,
    version_id INT REFERENCES versions (id),
    group_vid INT,
    "order" SERIAL,
    is_deleted BOOLEAN
);

ALTER TABLE leaves ADD CONSTRAINT leaves_unq_vid_version UNIQUE (vid, version_id);

CREATE OR REPLACE VIEW groups_latest AS
    SELECT grouped.id,
        grouped.vid,
        grouped.name,
        grouped.parent_vid,
        grouped.version_id,
        grouped."order",
        grouped.is_deleted
        FROM ( SELECT l.id,
                l.vid,
                l.name,
                l.parent_vid,
                l.version_id,
                l."order",
                l.is_deleted,
                max(l.version_id) OVER (PARTITION BY l.vid) AS max
               FROM groups l) grouped
    WHERE grouped.version_id = grouped.max
    ORDER BY grouped."order";

CREATE OR REPLACE VIEW leaves_latest AS
    SELECT grouped.id,
        grouped.vid,
        grouped.name,
        grouped.value_type,
        grouped.value,
        grouped.version_id,
        grouped.group_vid,
        grouped."order",
        grouped.is_deleted
        FROM ( SELECT l.id,
                l.vid,
                l.name,
                l.value_type,
                l.value,
                l.version_id,
                l.group_vid,
                l."order",
                l.is_deleted,
                max(l.version_id) OVER (PARTITION BY l.vid) AS max
               FROM leaves l) grouped
        WHERE grouped.version_id = grouped.max
        ORDER BY grouped."order";

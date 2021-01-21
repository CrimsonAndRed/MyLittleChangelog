ALTER TABLE groups ADD CONSTRAINT groups_unq_vid_version UNIQUE (vid, version_id);
ALTER TABLE leaves ADD CONSTRAINT leaves_unq_vid_version UNIQUE (vid, version_id);

CREATE VIEW groups_latest AS
SELECT grouped.* FROM (SELECT g.*, max(version_id) OVER (PARTITION BY vid) FROM groups AS g) as grouped
WHERE grouped.version_id=grouped.max;

CREATE VIEW leaves_latest AS
SELECT grouped.* FROM (SELECT l.*, max(version_id) OVER (PARTITION BY vid) FROM leaves AS l) as grouped
WHERE grouped.version_id=grouped.max;
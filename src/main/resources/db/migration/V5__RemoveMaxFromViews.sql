DROP VIEW groups_latest;
CREATE OR REPLACE VIEW groups_latest AS
SELECT
    grouped.id,
    grouped.vid,
    grouped.name,
    grouped.parent_vid,
    grouped.version_id
FROM (SELECT g.*, max(version_id) OVER (PARTITION BY vid) FROM groups AS g) as grouped
WHERE grouped.version_id=grouped.max;

DROP VIEW leaves_latest;
CREATE OR REPLACE VIEW leaves_latest AS
SELECT
    grouped.id,
    grouped.vid,
    grouped.name,
    grouped.value_type,
    grouped.value,
    grouped.version_id,
    grouped.group_vid
FROM (SELECT l.*, max(version_id) OVER (PARTITION BY vid) FROM leaves AS l) as grouped
WHERE grouped.version_id=grouped.max;
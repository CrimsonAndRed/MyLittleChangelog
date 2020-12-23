DROP VIEW groups_latest;
CREATE OR REPLACE VIEW groups_latest AS
SELECT
    grouped.id,
    grouped.vid,
    grouped.name,
    grouped.parent_vid,
    grouped.version_id,
    grouped."order"
FROM (SELECT l.*, max(version_id) OVER (PARTITION BY vid) FROM groups AS l) as grouped
WHERE grouped.version_id=grouped.max;
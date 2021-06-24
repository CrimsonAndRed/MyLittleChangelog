ALTER TABLE groups ADD COLUMN is_deleted boolean;
ALTER TABLE leaves ADD COLUMN is_deleted boolean;

UPDATE groups SET is_deleted = false;
UPDATE leaves SET is_deleted = false;

CREATE OR REPLACE VIEW groups_latest AS
    SELECT grouped.id,
        grouped.vid,
        grouped.name,
        grouped.parent_vid,
        grouped.version_id,
        grouped."order"
        FROM ( SELECT l.id,
                l.vid,
                l.name,
                l.parent_vid,
                l.version_id,
                l."order",
                l.is_deleted,
                max(l.version_id) OVER (PARTITION BY l.vid) AS max
               FROM groups l) grouped
    WHERE grouped.version_id = grouped.max AND grouped.is_deleted <> true
    ORDER BY grouped."order";

CREATE OR REPLACE VIEW leaves_latest AS
    SELECT grouped.id,
        grouped.vid,
        grouped.name,
        grouped.value_type,
        grouped.value,
        grouped.version_id,
        grouped.group_vid,
        grouped."order"
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
        WHERE grouped.version_id = grouped.max AND grouped.is_deleted <> true
        ORDER BY grouped."order";
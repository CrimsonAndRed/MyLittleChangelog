CREATE SEQUENCE leaves_order_seq;
ALTER TABLE leaves ADD COLUMN "order" INTEGER DEFAULT nextval('leaves_order_seq');
ALTER SEQUENCE leaves_order_seq OWNED BY leaves."order";

CREATE SEQUENCE groups_order_seq;
ALTER TABLE groups ADD COLUMN "order" INTEGER DEFAULT nextval('groups_order_seq');
ALTER SEQUENCE groups_order_seq OWNED BY groups."order";

DROP VIEW leaves_latest;
CREATE OR REPLACE VIEW leaves_latest AS
SELECT
    grouped.id,
    grouped.vid,
    grouped.name,
    grouped.value_type,
    grouped."value",
    grouped.version_id,
    grouped.group_vid,
    grouped."order"
FROM (SELECT l.*, max(version_id) OVER (PARTITION BY vid) FROM leaves AS l) as grouped
WHERE grouped.version_id=grouped."max"
ORDER BY grouped."order" ASC;
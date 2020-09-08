![Alt text](ActionSchema.png?raw=true "Schema")

----------
TODO
- https://ktor.io/servers/features/locations.html
----------
Запросы:

WITH RECURSIVE tmp_groups AS (
		SELECT * FROM groups_latest 
		WHERE (vid in (SELECT group_vid FROM leaves where version_id = 2)) or 
				(vid in (SELECT vid FROM groups where version_id = 2))
	UNION
		SELECT g.* FROM groups_latest AS g JOIN tmp_groups ON g.vid=tmp_groups.parent_vid
) SELECT * FROM tmp_groups;
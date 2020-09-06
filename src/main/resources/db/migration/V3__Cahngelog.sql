ALTER TABLE groups ADD CONSTRAINT groups_unq_vid_version UNIQUE (vid, version_id);
ALTER TABLE leaves ADD CONSTRAINT leaves_unq_vid_version UNIQUE (vid, version_id);
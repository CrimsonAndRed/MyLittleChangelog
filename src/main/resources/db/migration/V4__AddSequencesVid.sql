CREATE SEQUENCE groups_vid_seq;
ALTER TABLE groups ALTER COLUMN vid SET DEFAULT nextval('groups_vid_seq');
ALTER SEQUENCE groups_vid_seq OWNED BY groups.vid;

CREATE SEQUENCE leaves_vid_seq;
ALTER TABLE leaves ALTER COLUMN vid SET DEFAULT nextval('leaves_vid_seq');
ALTER SEQUENCE leaves_vid_seq OWNED BY leaves.vid;
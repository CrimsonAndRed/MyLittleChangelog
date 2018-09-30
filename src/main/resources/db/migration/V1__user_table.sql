CREATE TABLE app_user (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  password BYTEA NOT NULL,
  salt BYTEA,
  create_date TIMESTAMP NOT NULL DEFAULT now(),
  is_deleted BOOL NOT NULL DEFAULT false
);

COMMENT ON TABLE app_user IS 'System users table. Contains non-actual data.';

COMMENT ON COLUMN app_user.id IS 'User id';
COMMENT ON COLUMN app_user.name IS 'User name';
COMMENT ON COLUMN app_user.password IS 'User password''s hash';
COMMENT ON COLUMN app_user.salt IS 'User''s salt for hashing';
COMMENT ON COLUMN app_user.create_date IS 'User create date';
COMMENT ON COLUMN app_user.is_deleted IS 'Soft delete marker';


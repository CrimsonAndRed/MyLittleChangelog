CREATE TABLE user_token (
    id SERIAL PRIMARY KEY,
    token UUID NOT NULL,
    user_id INTEGER NOT NULL REFERENCES app_user(id),
    -- no update date/version since these ones are not updatable.
    create_date TIMESTAMP NOT NULL DEFAULT now(),
    end_date TIMESTAMP,
    is_deleted BOOL NOT NULL DEFAULT false
);

CREATE INDEX ON user_token (token);
CREATE INDEX ON user_token (user_id);

COMMENT ON TABLE user_token IS 'Security tokens for users. Contains non-actual data.';

COMMENT ON COLUMN user_token.id IS 'Token Id';
COMMENT ON COLUMN user_token.token IS 'Token uuid value';
COMMENT ON COLUMN user_token.user_id IS 'Token holder';

COMMENT ON COLUMN user_token.create_date IS 'Token create date';
COMMENT ON COLUMN user_token.end_date IS 'Token invalidation date';
COMMENT ON COLUMN user_token.is_deleted IS 'Soft delete marker ';
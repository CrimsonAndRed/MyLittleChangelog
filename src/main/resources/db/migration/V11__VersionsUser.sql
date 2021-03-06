ALTER TABLE versions ADD COLUMN user_id int;
ALTER TABLE versions ADD CONSTRAINT versions_user_fk FOREIGN KEY (user_id) REFERENCES users (id);
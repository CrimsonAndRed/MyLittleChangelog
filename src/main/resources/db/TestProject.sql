-- Some testing data
DO $$DECLARE
  project_id int;
  version_id int;
  route_id int;
  changelog_id int;
BEGIN
  project_id := nextval('project_id_seq');
  insert into project (id, name, description, create_user) values (project_id, 'project1', 'description1', 1);

  version_id := nextval('project_version_id_seq');
  insert into project_version (id, num, description, internal_order, project_id) values (version_id, '1.0', 'descr1', 0, project_id);

  route_id := nextval('changelog_route_id_seq');
  insert into changelog_route (id, name, project_id) values (route_id, '/root/test', project_id);

  changelog_id := nextval('changelog_id_seq');
  insert into changelog (id, vid, text, route_id, version_id) values (changelog_id, changelog_id, 'update 1: value 1', route_id, version_id);

  route_id := nextval('changelog_route_id_seq');
  insert into changelog_route (id, name, project_id) values (route_id, '/root/', project_id);

  changelog_id := nextval('changelog_id_seq');
  insert into changelog (id, vid, text, route_id, version_id) values (changelog_id, changelog_id, 'root 1: value 2', route_id, version_id);

  changelog_id := nextval('changelog_id_seq');
  insert into changelog (id, vid, text, route_id, version_id) values (changelog_id, changelog_id, 'root 1: value 3', route_id, version_id);
END$$;
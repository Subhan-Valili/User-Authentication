INSERT INTO roles (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_USER');

INSERT INTO permissions (id, name) VALUES (1, 'READ');
INSERT INTO permissions (id, name) VALUES (2, 'WRITE');
INSERT INTO permissions (id, name) VALUES (3, 'UPDATE');
INSERT INTO permissions (id, name) VALUES (4, 'DELETE');

INSERT INTO roles_permission (role_id, permission_id) VALUES (1, 1) ON CONFLICT (role_id, permission_id) DO NOTHING;
INSERT INTO roles_permission (role_id, permission_id) VALUES (1, 2) ON CONFLICT (role_id, permission_id) DO NOTHING;
INSERT INTO roles_permission (role_id, permission_id) VALUES (1, 3) ON CONFLICT (role_id, permission_id) DO NOTHING;
INSERT INTO roles_permission (role_id, permission_id) VALUES (1, 4) ON CONFLICT (role_id, permission_id) DO NOTHING;
INSERT INTO roles_permission (role_id, permission_id) VALUES (2, 1) ON CONFLICT (role_id, permission_id) DO NOTHING;


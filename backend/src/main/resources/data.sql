-- ROLE DATA

insert into role (id, name, description, is_system_role, is_deleted, created_at)
values (1, 'SYSTEM_ADMIN', 'Admin role', true, false, current_timestamp);

insert into role (id, name, description, is_system_role, is_deleted, created_at)
values (2, 'HR_ADMIN', 'Hr admin role', true, false, current_timestamp);

insert into role (id, name, description, is_system_role, is_deleted, created_at)
values (3, 'EMPLOYEE', 'Employee role', true, false, current_timestamp);


-- PERMISSION DATA

insert into permission (id, resource, action, description)
values (1, 'USER', 'CREATE', 'Create user');

insert into permission (id, resource, action, description)
values (2, 'EMPLOYEE', 'CREATE', 'Create user');

insert into permission (id, resource, action, description)
values (3, 'USER', 'DELETE', 'Create user');


-- ROLE PERMISSION MAP
insert into role_permission(id, role_id, permission_id, granted_at)
values (1, 1, 1, current_timestamp);

insert into role_permission(id, role_id, permission_id, granted_at)
values (2, 1, 2, current_timestamp);

insert into role_permission(id, role_id, permission_id, granted_at)
values (3, 1, 3, current_timestamp);
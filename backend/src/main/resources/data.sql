-- PERMISSION DATA (Resource-based CRUD)
-- User Management
INSERT INTO permission (id, resource, action, description)
VALUES (1, 'USER', 'CREATE', 'Can create new system users');
INSERT INTO permission (id, resource, action, description)
VALUES (2, 'USER', 'READ', 'Can view user profiles and lists');
INSERT INTO permission (id, resource, action, description)
VALUES (3, 'USER', 'UPDATE', 'Can edit user details');
INSERT INTO permission (id, resource, action, description)
VALUES (4, 'USER', 'DELETE', 'Can remove users from the system');

-- Employee Management
INSERT INTO permission (id, resource, action, description)
VALUES (5, 'EMPLOYEE', 'CREATE', 'Can onboard new employees');
INSERT INTO permission (id, resource, action, description)
VALUES (6, 'EMPLOYEE', 'READ', 'Can view employee professional records');
INSERT INTO permission (id, resource, action, description)
VALUES (7, 'EMPLOYEE', 'UPDATE', 'Can edit employee information');
INSERT INTO permission (id, resource, action, description)
VALUES (8, 'EMPLOYEE', 'DELETE', 'Can remove employee from the system');

-- Department & Designation
INSERT INTO permission (id, resource, action, description)
VALUES (9, 'ORGANIZATION', 'MANAGE', 'Can create/edit departments and designations');

-- Payroll/Salary (Sensitive)
INSERT INTO permission (id, resource, action, description)
VALUES (10, 'PAYROLL', 'VIEW', 'Can view salary structures');
INSERT INTO permission (id, resource, action, description)
VALUES (11, 'PAYROLL', 'EDIT', 'Can modify salary and bonuses');


-- ROLE DATA
INSERT INTO role (id, name, description, is_system_role)
VALUES (1, 'SYSTEM_ADMIN', 'Full system access', true);
INSERT INTO role (id, name, description, is_system_role)
VALUES (2, 'HR_ADMIN', 'Manage employees and departments', true);
INSERT INTO role (id, name, description, is_system_role)
VALUES (3, 'MANAGER', 'Manage team assignments and view reports', false);
INSERT INTO role (id, name, description, is_system_role)
VALUES (4, 'EMPLOYEE', 'Self-service access only', true);


-- ROLE PERMISSION MAP

-- SYSTEM_ADMIN: Gets everything (Permissions 1-10)
INSERT INTO role_permission(role_id, permission_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7),
       (1, 8),
       (1, 9),
       (1, 10),
       (1, 11);

-- HR_ADMIN: Can manage employees and organization, but maybe not delete system users
INSERT INTO role_permission(role_id, permission_id)
VALUES (2, 2),
       (2, 5),
       (2, 6),
       (2, 7),
       (2, 8),
       (2, 9);

-- MANAGER: Can read employee data and view payroll, but not edit organization or users
INSERT INTO role_permission(role_id, permission_id)
VALUES (3, 2),
       (3, 6),
       (3, 9);

-- EMPLOYEE: Only permission to read (their own) data (simulated here by a general READ)
INSERT INTO role_permission(role_id, permission_id)
VALUES (4, 6);

-- DEPARTMENT DATA
INSERT INTO department (name, code, description, is_active)
VALUES ('Human Resources', 'HR', 'Handles recruitment, payroll, and employee relations.', true),
       ('Information Technology', 'IT', 'Responsible for infrastructure, software, and support.', true),
       ('Finance', 'FIN', 'Manages company budgeting, accounting, and financial reporting.', true),
       ('Marketing', 'MKT', 'Focuses on branding, advertising, and market research.', true),
       ('Sales', 'SAL', 'Responsible for lead generation and revenue growth.', true),
       ('Operations', 'OPS', 'Manages daily business processes and efficiency.', true);


-- DESIGNATION DATA
INSERT INTO designation (name, code, description, is_active)
VALUES ('Software Engineer', 'SWE', 'Responsible for developing, testing, and maintaining software applications.',
        true),
       ('Senior Software Engineer', 'SR-SWE',
        'Leads technical design and mentors junior developers within the engineering team.', true),
       ('Project Manager', 'PM', 'Oversees project timelines, resources, and communication between stakeholders.',
        true),
       ('HR Specialist', 'HRS', 'Manages employee relations, benefits administration, and recruitment processes.',
        true),
       ('Financial Analyst', 'FIN-A', 'Analyzes financial data and trends to support business decision-making.', true),
       ('Marketing Coordinator', 'MKT-C',
        'Supports marketing campaigns and coordinates promotional events and materials.', true),
       ('Sales Executive', 'SAL-E', 'Responsible for identifying new business opportunities and closing sales deals.',
        true),
       ('Systems Administrator', 'SYS-ADMIN',
        'Maintains and secures the company IT infrastructure and server environments.', true),
       ('Chief Technology Officer', 'CTO',
        'Provides high-level strategy and oversight for the entire technology department.', true),
       ('Quality Assurance Lead', 'QA-L',
        'Ensures product quality through rigorous testing protocols and bug tracking.', true),
       ('DevOps Engineer', 'DEVOPS', 'Bridging the gap between development and operations through CI/CD automation.',
        true);
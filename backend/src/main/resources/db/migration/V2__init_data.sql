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

-- Leave Management
INSERT INTO permission (id, resource, action, description)
VALUES (9, 'LEAVE', 'APPROVE', 'Can approve / reject leave requests');


-- ROLE DATA
INSERT INTO role (id, name, description, is_system_role)
VALUES (1, 'SYSTEM_ADMIN', 'Full system access', true);
INSERT INTO role (id, name, description, is_system_role)
VALUES (2, 'HR_ADMIN', 'Manage employees and departments', false);
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
       (1, 9);

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

-- HOLIDAY DATA
INSERT INTO holiday (id, name, holiday_date, holiday_type, is_recurring, is_active)
VALUES (1, 'International Mother Language Day', '2026-02-21', 'PUBLIC', true, true),
       (2, 'Independence Day', '2026-03-26', 'PUBLIC', true, true),
       (3, 'Bengali New Year (Pohela Boishakh)', '2026-04-14', 'PUBLIC', true, true),
       (4, 'Eid-ul-Fitr', '2026-03-31', 'PUBLIC', true, true),
       (5, 'Eid-ul-Fitr Holiday', '2026-04-01', 'PUBLIC', true, true),
       (6, 'Eid-ul-Adha', '2026-06-07', 'PUBLIC', true, true),
       (7, 'Eid-ul-Adha Holiday', '2026-06-08', 'PUBLIC', true, true),
       (8, 'Victory Day', '2026-12-16', 'PUBLIC', true, true),
       (9, 'Durga Puja (Dashami)', '2026-10-22', 'OPTIONAL', true, true),
       (10, 'Christmas Day', '2026-12-25', 'OPTIONAL', true, true),
       (11, 'Company Foundation Day', '2026-07-15', 'COMPANY', true, true),
       (12, 'General Bus Strick', '2026-05-15', 'COMPANY', true, true);

-- LEAVE TYPE DATA
INSERT INTO leave_type (id, name, code, description, is_paid,
                        requires_attachment, is_active)
VALUES (1, 'Annual Leave', 'ANNUAL', 'Yearly paid leave for employees', true, false, true),
       (2, 'Sick Leave', 'SICK', 'Leave taken due to illness or medical condition', true, true, true),
       (3, 'Casual Leave', 'CASUAL', 'Short-term personal leave for urgent matters', true, false, true),
       (4, 'Maternity Leave', 'MATERNITY', 'Leave granted for childbirth and recovery', true, true, false),
       (5, 'Paternity Leave', 'PATERNITY', 'Leave granted to fathers after childbirth', true, false, false),
       (6, 'Unpaid Leave', 'UNPAID', 'Leave without salary deduction applies', false, false, true);


-- LEAVE POLICY DATA
INSERT INTO leave_policy (id, name, leave_type_id, max_days_per_year, max_days_per_request, min_days_notice,
                          allow_half_day, employment_type, effective_from, effective_to, is_active)
VALUES (1, 'Annual Leave Policy', 1, 20, 10, 2, true,
        'FULL_TIME', '2026-01-01', NULL, false),
       (2, 'Sick Leave Policy', 2, 7, 5, 0, true,
        'FULL_TIME', '2026-01-01', NULL, true),
       (3, 'Casual Leave Policy', 3, 15, 3, 1, true,
        'FULL_TIME', '2026-01-01', NULL, true),
       (4, 'Maternity Leave Policy', 4, 120, 120, 30, false,
        'FEMALE', '2026-01-01', NULL, false),
       (5, 'Paternity Leave Policy', 5, 7, 7, 7, false,
        'MALE', '2026-01-01', NULL, false),
       (6, 'Unpaid Leave Policy', 6, 15, 30, 3, true,
        'FULL_TIME', '2026-01-01', NULL, true);


-- Insert admin user only if not exists
INSERT INTO user_account (username, password_hash, status, is_deleted)
SELECT 'admin',
       'admin@123#',
       'ACTIVE',
       false WHERE NOT EXISTS (
    SELECT 1
    FROM user_account
    WHERE username = 'admin'
      AND is_deleted = false
);

-- Assign SYSTEM_ADMIN role to admin user if not assigned
INSERT INTO user_role (user_id, role_id)
SELECT u.id,
       r.id
FROM user_account u
         JOIN role r ON r.name = 'SYSTEM_ADMIN'
WHERE u.username = 'admin'
  AND NOT EXISTS (SELECT 1
                  FROM user_role ur
                  WHERE ur.user_id = u.id
                    AND ur.role_id = r.id);
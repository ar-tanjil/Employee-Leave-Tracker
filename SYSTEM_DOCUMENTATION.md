# 🏢 Employee Leave Tracker

A stateless, rule-driven HRMS designed for organizational scalability. This system manages the entire employee
lifecycle—from automated bootstrapping and JWT-based authentication to complex, multi-step leave approval workflows and
pro-rated balance logic.

---

## 🛠 1. System Initialization (Bootstrap Phase)

Upon application startup, the system executes a data seeding process to ensure the environment is ready for operation.

### Master Data Seeding

- **RBAC Initialization:** Inserts 9 core permissions (USER/EMPLOYEE CRUD + LEAVE APPROVE) and maps them to roles:
  `SYSTEM_ADMIN`, `HR_ADMIN`, `MANAGER`, and `EMPLOYEE`.
- **Org Structure:** Seeds 6 Departments (HR, IT, Finance, Marketing, Sales, Operations) and 11 Designations.
- **Leave Infrastructure:** Populates 2026 Holidays (e.g., Independence Day, Eid-ul-Fitr) and defines Leave Policies (
  Annual, Sick, Casual, etc.).

### Default Admin Guard

The system checks for an existing `admin` user. If missing, it fails to start:

- **Username:** `admin`
- **Password:** `admin@123#` (BCrypt encoded)
- **Role:** Automatically bound to `SYSTEM_ADMIN` for immediate configuration.

---

## 🔐 2. Security & Authentication

The system implements a **Stateless JWT (JSON Web Token)** architecture managed by Spring Security.

1. **Authentication:** Users login; `AuthenticationManager` validates against BCrypt hashes.
2. **Token Issuance:** Success generates a JWT containing the user's identity and roles.
3. **JwtAuthFilter:** Intercepts every request to validate the token and reconstruct the `SecurityContextHolder` on the
   fly. No server-side sessions are maintained.

---

## 👥 3. Employee Onboarding & Account Provisioning

Onboarding an employee is a synchronized workflow across multiple modules.

- **Validation:** Verifies unique emails and valid Department/Designation lookups.
- **Auto-Provisioning:** Simultaneously creates the `Employee` record and a corresponding `UserAccount`.
    - **Username Provisioning:** Automatically derived from the employee's registered email address.
    - **Initial Credential:** A default password is generated using the employee's first name and stored as a
      BCrypt-encoded value.
    - **Default Role Assignment:** Newly created accounts are automatically assigned the `EMPLOYEE` role.

- **Balance Initialization:** Automatically generates leave balances for the year using **Pro-rata Logic**.

### ⚖️ Leave Balance Pro-rata Calculation

When a balance is initialized, the system determines the allowance based on the hire date:

- **Existing Employees:** (Joined in previous years) Receive 100% of the policy's `max_days_per_year`.
- **New Hires:** (Joined in current year) Leave is calculated by the ratio of remaining days in the year.

    - **Formula:**

      $$
      \text{Allocated} = \frac{\text{MaxDays} \times \text{RemainingDaysInYear}}{\text{TotalDaysInYear}}
      $$

- **Rounding:** All results are rounded to the nearest **0.5 days** for clean accounting.

### 👤 Role Assignment Rules

Role updates are difference-based (calculating additions and removals) and enforced by organizational policy.

- **Permissions** `SYSTEM_ADMIN` and `HR_ADMIN` can assign role to employees.

- **HR Restriction:** The `HR_ADMIN` role can only be assigned to employees within the `"Human Resources"` department.

- **Manager Constraint:** Enforces a one-manager-per-department rule to prevent hierarchy conflicts.

---

## 📅 4. Leave Application Logic

The leave module acts as a validation engine to ensure compliance with company policy and calendar constraints.

### Timing & Calendar Rules

- **Boundary Validation:** The system **rejects** applications if the Start Date or End Date falls on a Weekend or a
  Public Holiday (e.g., March 26).
- **The Sandwich Rule:** If the start/end dates are valid but holidays/weekends fall in between, the system calculates "
  Total Days" based on policy (including or excluding the "sandwiched" days).
- **Balance Reservation:** Requested days are moved to a `PENDING` state in the `LeaveBalance` table to prevent
  double-booking while the request is being reviewed.

---

## ⚖️ 5. Approval Workflow & Balance Impact

Leave requests trigger a default dynamic, multi-step approval chain (`LeaveApprovalInstance`).

### Approval Chain Rules

- **Dynamic Resolution:** Follows the sequence defined in the workflow (e.g., Dept Manager → HR Admin).

- **HR Exception:**

    - **The Skip:** HR Department employees may skip the internal "Manager" step to avoid redundant approvals.
    - **The Fallback:** If an employee has no assigned manager, the request escalates directly to HR.

- **Root Requirement:** An **HR Manager** must exist in the system to act as the final authority.

### Transactional Logic

The system uses `@Transactional` methods to ensure data integrity during status changes.

#### `approveLeave`

- **Intermediate:** Sets current step to `APPROVED`, marks inactive, and activates the `nextStep`.
- **Final Approval:** Sets `LeaveRequest` to `APPROVED` and triggers the `deductLeaveBalance` service.

#### `rejectLeave`

- **Kill Switch:** Rejects the current step and **immediately** deactivates/rejects all subsequent steps in the chain.
  The request status becomes `REJECTED`.

#### Balance Service Math

| Method                         | Available Days | Used Days     | Pending Days  |
|--------------------------------|----------------|---------------|---------------|
| **Deduct** (On Final Approval) | Subtracts (−)  | Adds (+)      | Subtracts (−) |
| **Restore** (On Cancellation)  | Adds (+)       | Subtracts (−) | Subtracts (−) |

---

## 💾 6. Database Reference (Lookup Codes)

- **Departments:** `HR`, `IT`, `FIN`, `MKT`, `SAL`, `OPS`
- **Leave Types:** `ANNUAL`, `SICK`, `CASUAL`, `MATERNITY`, `PATERNITY`, `UNPAID`
- **Employment Types:** `FULL_TIME`, `PART_TIME`,
package com.employee_leave_tracker.backend.constant;

public enum UserStatus {
    ACTIVE,
    LOCKED,
    INACTIVE;

    public static UserStatus fromString(String status) {
        if (status == null) {
            return null;
        }
        try {
            return UserStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

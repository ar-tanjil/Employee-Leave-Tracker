package com.employee_leave_tracker.backend.model.leave;

import com.employee_leave_tracker.backend.constant.HolidayType;
import com.employee_leave_tracker.backend.model.employee.Department;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "holiday")
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "holiday_date", nullable = false)
    private LocalDate holidayDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "holiday_type", length = 50)
    private HolidayType holidayType;

    @Column(name = "is_recurring")
    private Boolean isRecurring;

    @Column(name = "is_active")
    private Boolean isActive;

}
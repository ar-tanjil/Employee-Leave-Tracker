package com.employee_leave_tracker.backend.model.auth;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "permission",
        uniqueConstraints = @UniqueConstraint(columnNames = {"resource", "action"})
)
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resource;
    private String action;

    @Column(columnDefinition = "TEXT")
    private String description;
}
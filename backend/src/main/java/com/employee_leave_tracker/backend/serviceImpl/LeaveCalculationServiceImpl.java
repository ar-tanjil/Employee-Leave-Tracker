package com.employee_leave_tracker.backend.serviceImpl;

import com.employee_leave_tracker.backend.constant.LeaveDuration;
import com.employee_leave_tracker.backend.exception.ArgumentNotValidException;
import com.employee_leave_tracker.backend.repository.leave.HolidayRepository;
import com.employee_leave_tracker.backend.service.LeaveCalculationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LeaveCalculationServiceImpl implements LeaveCalculationService {
    private final HolidayRepository holidayRepository;

    @Override
    public double calculateLeaveDays(LocalDate start, LocalDate end, LeaveDuration duration) {
        if (duration != LeaveDuration.FULL_DAY) return 0.5;

        long days = start.datesUntil(end.plusDays(1))
                .filter(date -> !isWeekend(date))
                .filter(date -> !isHoliday(date))
                .count();

        return applySandwichRule(start, end, days);
    }

    @Override
    public void validateLeaveDateRange(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new ArgumentNotValidException("Start date cannot be after end date");
        }
        if (start.isBefore(LocalDate.now())) {
            throw new ArgumentNotValidException("Cannot apply for leave in the past");
        }

        if (isHoliday(start) || isHoliday(end)) {
            throw new ArgumentNotValidException("Cannot apply for leave on a holiday");
        }

        if (isWeekend(start) || isWeekend(end)) {
            throw new ArgumentNotValidException("Cannot apply for leave on a weekend");
        }
    }

    private boolean isHoliday(LocalDate date) {
        return holidayRepository.existsByHolidayDate(date);
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.FRIDAY || day == DayOfWeek.SATURDAY;
    }

    private double applySandwichRule(LocalDate start, LocalDate end, double currentCount) {
        LocalDate cursor = start;
        int sandwichDays = 0;

        while (!cursor.isAfter(end)) {
            if (isWeekend(cursor) || isHoliday(cursor)) {
                sandwichDays++;
            }
            cursor = cursor.plusDays(1);
        }

        boolean startsWithWorkingDay = !(isWeekend(start) || isHoliday(start));
        boolean endsWithWorkingDay = !(isWeekend(end) || isHoliday(end));

        if (startsWithWorkingDay && endsWithWorkingDay) {
            return currentCount + sandwichDays;
        }

        return currentCount;
    }
}

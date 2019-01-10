package com.diamondLounge.entity.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDate.now;
import static java.time.LocalTime.MAX;

public class WeekDateRange {
    private LocalDate weekStart;
    private LocalDate weekEnd;
    private List<LocalDate> dateRange;

    public WeekDateRange(int offset) {
        LocalDate desiredDate = now().plusDays(offset * 7);
        int dayOfTheWeek = desiredDate.getDayOfWeek().ordinal();

        weekStart = desiredDate.minusDays(dayOfTheWeek);
        weekEnd = desiredDate.plusDays(7 - dayOfTheWeek);
        dateRange = weekStart.datesUntil(weekEnd).collect(Collectors.toList());
    }

    public List<LocalDate> getDateRange() {
        return dateRange;
    }

    public LocalDate getWeekStart() {
        return weekStart;
    }

    public LocalDate getWeekEnd() {
        return weekEnd;
    }

    public LocalDate getWeekEndExclusive() {
        return weekEnd.minusDays(1);
    }

    public LocalDateTime getWeekStartWithTime() {
        return weekStart.atStartOfDay();
    }

    public LocalDateTime getWeekEndWithTimeExclusive() {
        return weekEnd.minusDays(1).atTime(MAX);
    }
}

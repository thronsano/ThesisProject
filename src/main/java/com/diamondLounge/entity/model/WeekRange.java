package com.diamondLounge.entity.model;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDate.now;

public class WeekRange {
    private LocalDate lastMonday;
    private LocalDate nextMonday;
    private List<LocalDate> dateRange;

    public WeekRange(int offset) {
        LocalDate desiredDate = now().plusDays(offset * 7);
        int dayOfTheWeek = desiredDate.getDayOfWeek().ordinal();
        lastMonday = desiredDate.minusDays(dayOfTheWeek);
        nextMonday = desiredDate.plusDays(7 - dayOfTheWeek);
        dateRange = lastMonday.datesUntil(nextMonday).collect(Collectors.toList());
    }

    public List<LocalDate> getDateRange() {
        return dateRange;
    }

    public LocalDate getLastMonday() {
        return lastMonday;
    }

    public LocalDate getNextMonday() {
        return nextMonday;
    }
}

package com.diamondLounge.entity.models.tables;

import com.diamondLounge.entity.db.WorkDay;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Ordering;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.diamondLounge.entity.models.tables.HeadersPosition.VERTICAL;
import static com.google.common.collect.ImmutableSortedMap.copyOf;
import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;
import static java.time.format.DateTimeFormatter.ISO_DATE;

public class SalaryTableModel extends TableModel {
    public SalaryTableModel(Map<WorkDay, BigDecimal> daySalaryMap) {
        Ordering<WorkDay> workDayOrdering = Ordering.natural().onResultOf(WorkDay::getDate);
        ImmutableSortedMap<WorkDay, BigDecimal> sortedMap = copyOf(daySalaryMap, workDayOrdering);

        createEmptyTable(sortedMap);
        addHeadersAndBody(sortedMap);
        ignoreMissingValues = true;
        headersPosition = VERTICAL;
    }

    private void addHeadersAndBody(ImmutableSortedMap<WorkDay, BigDecimal> sortedMap) {
        table[0][0] = "DATE";
        table[0][1] = "MINUTES WORKED";
        table[0][2] = "SALARY EARNED";

        AtomicInteger index = new AtomicInteger(1);
        sortedMap.forEach((workDay, salary) -> {
            table[index.get()][0] = workDay.getDate().format(ISO_DATE);
            table[index.get()][1] = String.valueOf(valueOf(workDay.getHoursWorked().getSeconds()).setScale(2, HALF_UP).divide(valueOf(60), HALF_UP));
            table[index.get()][2] = String.valueOf(salary.setScale(2, HALF_UP).doubleValue());
            index.incrementAndGet();
        });
    }

    private void createEmptyTable(ImmutableSortedMap<WorkDay, BigDecimal> sortedMap) {
        width = sortedMap.size() + 1;
        height = 3;
        table = new String[width][height];
    }
}

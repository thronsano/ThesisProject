package com.diamondLounge.entity.model;

import com.diamondLounge.entity.db.Schedule;
import com.diamondLounge.entity.db.Shop;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Ordering;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.groupingBy;

public class ScheduleTableImpl {
    private String[][] table;
    private int width;
    private int height;


    public ScheduleTableImpl(List<Schedule> schedules, List<LocalDate> dateRange) {
        Ordering<Shop> shopOrdering = Ordering.natural().onResultOf(Shop::getName);
        ImmutableSortedMap<Shop, List<Schedule>> sortedMap = ImmutableSortedMap.copyOf(schedules.stream().collect(groupingBy(Schedule::getShop)), shopOrdering);
        createEmptyTable(dateRange, sortedMap);
        addHeaders(dateRange);

        AtomicInteger currentHeight = new AtomicInteger(1);

        sortedMap.forEach((shop, scheduleList) -> {
            table[0][currentHeight.get()] = shop.getName();

            scheduleList.forEach(schedule -> {
                AtomicInteger offset = new AtomicInteger();
                schedule.getEmployees().forEach(employee -> {
                    table[scheduleList.indexOf(schedule) + 1][currentHeight.get() + offset.get()] = employee.getName();
                    offset.getAndIncrement();
                });
            });

            int maxAmountOfEmployees = scheduleList.stream().mapToInt(x -> x.getEmployees().size()).max().orElse(0);
            currentHeight.getAndAdd(maxAmountOfEmployees);
        });
    }

    private void createEmptyTable(List<LocalDate> dateRange, Map<Shop, List<Schedule>> shopMap) {
        int width = dateRange.size() + 1; //accommodate shop name column
        AtomicInteger height = new AtomicInteger(1); //accommodate date headers

        shopMap.forEach(
                (Shop, ScheduleList) -> {
                    int maxAmountOfEmployees = ScheduleList.stream().mapToInt(x -> x.getEmployees().size()).max().orElse(0);
                    height.addAndGet(maxAmountOfEmployees);
                }
        );

        table = new String[width][height.get()];
        this.height = height.get() - 1;
        this.width = width - 1;
    }

    private void addHeaders(List<LocalDate> dateRange) {
        for (int date = 0; date < dateRange.size(); date++) {
            table[date + 1][0] = dateRange.get(date).toString();
        }
    }

    public String[][] getTable() {
        return table;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

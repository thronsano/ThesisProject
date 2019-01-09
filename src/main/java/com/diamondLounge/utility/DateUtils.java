package com.diamondLounge.utility;

import java.time.LocalDate;

public class DateUtils {
    public static boolean isSameOrAfter(LocalDate dateToCompare, LocalDate sameOrAfter) {
        return sameOrAfter.isEqual(dateToCompare) || dateToCompare.isAfter(sameOrAfter);
    }

    public static boolean isSameOrBefore(LocalDate dateToCompare, LocalDate sameOrBefore) {
        return sameOrBefore.isEqual(dateToCompare) || dateToCompare.isBefore(sameOrBefore);
    }
}

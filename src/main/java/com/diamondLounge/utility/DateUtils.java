package com.diamondLounge.utility;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateUtils {
    public static boolean isSameOrAfter(LocalDate dateToCompare, LocalDate sameOrAfter) {
        return sameOrAfter.isEqual(dateToCompare) || dateToCompare.isAfter(sameOrAfter);
    }

    public static boolean isSameOrBefore(LocalDate dateToCompare, LocalDate sameOrBefore) {
        return sameOrBefore.isEqual(dateToCompare) || dateToCompare.isBefore(sameOrBefore);
    }

    public static LocalDateTime getEarlierDateTime(LocalDateTime candidateOne, LocalDateTime candidateTwo) {
        if (candidateOne == null || candidateOne.isAfter(candidateTwo)) {
            return candidateTwo;
        }
        return candidateOne;
    }

    public static LocalDateTime getLaterDateTime(LocalDateTime candidateOne, LocalDateTime candidateTwo) {
        if (candidateOne == null || candidateOne.isBefore(candidateTwo)) {
            return candidateTwo;
        }
        return candidateOne;
    }
}

package com.diamondLounge.utility;

import java.sql.Timestamp;

public final class Logger {

    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RESET = "\u001B[0m";

    private Logger() {
        // Hide implicit constructor
    }

    public static void logError(String message) {
        String colorizedMessage = new Timestamp(System.currentTimeMillis()) + "\t[" + RED + "ERROR" + RESET + "]\t\t" + message;
        System.out.println(colorizedMessage);
    }

    public static void logWarning(String message) {
        String colorizedMessage = new Timestamp(System.currentTimeMillis()) + "\t[" + YELLOW + "WARNING" + RESET + "]\t" + message;
        System.out.println(colorizedMessage);
    }

    public static void log(String message) {
        String colorizedMessage = new Timestamp(System.currentTimeMillis()) + "\t[" + GREEN + "INFO" + RESET + "]\t\t" + message;
        System.out.println(colorizedMessage);
    }
}

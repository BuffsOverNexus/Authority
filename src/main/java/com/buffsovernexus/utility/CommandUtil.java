package com.buffsovernexus.utility;

public class CommandUtil {
    public static String convertArgsToString(String[] args) {
        return convertArgsToString(1, args);
    }

    public static String convertArgsToString(int start, String[] args) {
        StringBuilder result = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            result.append(args[i]);
        }
        return result.toString();
    }
}

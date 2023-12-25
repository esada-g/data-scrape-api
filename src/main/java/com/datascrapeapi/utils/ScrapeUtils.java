package com.datascrapeapi.utils;

import com.datascrapeapi.scrape.ScrapeConstants;

public class ScrapeUtils {

    private ScrapeUtils() {
    }

    public static String extractYear(String description, int start, int length) {
        if (start != -1 && start + length + 4 <= description.length()) {
            String yearSubstring = description.substring(start + length, start + length + 4);
            return yearSubstring.trim();
        }
        return ScrapeConstants.EMPTY_STRING;
    }

    public static String getYearFoundedFromString(String description) {
        int foundedIndex = findIndex(description, ScrapeConstants.FOUNDED_IN_KEYWORD);
        int incorporatedIndex = findIndex(description, ScrapeConstants.INCORPORATED_IN_KEYWORD);

        if (foundedIndex != -1) {
            return extractYear(description, foundedIndex, ScrapeConstants.FOUNDED_IN_KEYWORD.length());
        } else if (incorporatedIndex != -1) {
            return extractYear(description, incorporatedIndex, ScrapeConstants.INCORPORATED_IN_KEYWORD.length());
        }

        return ScrapeConstants.EMPTY_STRING;
    }

    public static int findIndex(String description, String keyword) {
        return description.indexOf(keyword);
    }
}

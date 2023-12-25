package com.datascrapeapi.utils;

import java.util.ArrayList;
import java.util.List;

public class AddressUtils {

    private AddressUtils() {
    }

    public static String getCity(List<String> cityAndState) {
        return cityAndState.isEmpty() ? "" : cityAndState.get(0);
    }

    public static String getState(List<String> cityAndState) {
        if (cityAndState.size() > 1) {
            String[] stateParts = cityAndState.get(1).split(" ");
            if (stateParts.length > 1) {
                return stateParts[0];
            }
        }
        return "";
    }

    public static List<String> getCityAndState(String address) {
        String[] lines = address.split("\n");
        List<String> result = new ArrayList<>();
        if (lines.length > 1) {
            int index = (lines.length == 6) ? 2 : 1;
            String[] cityAndState = lines[index].split(", ");
            if (cityAndState.length > 1) {
                result.add(cityAndState[0]);
                result.add(cityAndState[1]);
            }
        }
        return result;
    }
}

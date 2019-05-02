package com.hoangthien.pitchbooking.utils;

import java.util.List;
import java.util.stream.Collectors;

public class PitchBookingUtils {

    public static List<String> getListCostCommafyFromListCostInt(List<Integer> list) {
        return list.stream()
                .map(integer -> {
                    String regex = "(\\d)(?=(\\d{3})+$)";
                    String costCommafy = "" + integer;
                    if (costCommafy.length() > 4) {
                        return costCommafy.replaceAll(regex, "$1,");
                    }
                    return costCommafy;
                })
                .collect(Collectors.toList());
    }
}

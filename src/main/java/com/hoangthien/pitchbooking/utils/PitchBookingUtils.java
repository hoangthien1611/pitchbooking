package com.hoangthien.pitchbooking.utils;

import com.hoangthien.pitchbooking.dto.Cost;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PitchBookingUtils {

    public static String getCostCommafy(Integer cost) {
        String regex = "(\\d)(?=(\\d{3})+$)";
        String costCommafy = "" + cost;
        if (costCommafy.length() > 4) {
            return costCommafy.replaceAll(regex, "$1,");
        }
        return costCommafy;
    }

    public static List<Long> convertFromStringListToLongList(String list) {
        if (StringUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<Long> result = new ArrayList<>();
        String[] items = list.split(",");
        for (int i = 0; i < items.length; i++) {
            try {
                Long longItem = Long.valueOf(Integer.parseInt(items[i]));
                result.add(longItem);
            } catch (NumberFormatException e) {}
        }

        return result;
    }

    public static List<Integer> convertFromStringListToIntList(String list) {
        if (StringUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<Integer> result = new ArrayList<>();
        String[] items = list.split(",");
        for (int i = 0; i < items.length; i++) {
            try {
                Integer intItem = Integer.parseInt(items[i]);
                result.add(intItem);
            } catch (NumberFormatException e) {}
        }

        return result;
    }

    public static List<Cost> getCostListFromIntList(List<Integer> list) {
        return list.stream()
                .map(integer -> {
                    return new Cost(integer, getCostCommafy(integer));
                })
                .collect(Collectors.toList());
    }
}

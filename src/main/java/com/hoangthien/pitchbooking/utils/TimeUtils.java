package com.hoangthien.pitchbooking.utils;

import com.hoangthien.pitchbooking.dto.TimeFrame;

import java.util.ArrayList;
import java.util.List;

public class TimeUtils {

    public static String getTimeStringFromInt(Integer hour) {
        if (hour == null) {
            return "";
        }

        if (hour >= 0 && hour < 10) {
            return new StringBuilder()
                    .append("0")
                    .append(hour)
                    .append(":00")
                    .toString();
        }
        return new StringBuilder()
                .append(hour)
                .append(":00")
                .toString();
    }

    public static List<String> getTimeFramesFromStartToEnd(Integer start, Integer end) {
        List<String> result = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            result.add(getTimeStringFromInt(i));
        }
        return result;
    }
}

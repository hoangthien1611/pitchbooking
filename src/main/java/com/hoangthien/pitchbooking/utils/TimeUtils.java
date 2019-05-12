package com.hoangthien.pitchbooking.utils;

import com.hoangthien.pitchbooking.dto.TimeFrame;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
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

    public static int getTimeIntFromString(String hour) {
        if (StringUtils.isEmpty(hour)) {
            return 0;
        }

        String h = hour.split(":")[0];
        try {
            return Integer.parseInt(h);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static List<String> getTimeStringsFromStartToEnd(Integer start, Integer end) {
        List<String> result = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            result.add(getTimeStringFromInt(i));
        }
        return result;
    }

    public static LocalDate getLocalDateFromDateString(String date) {

        if (StringUtils.isEmpty(date)) {
            return LocalDate.now();
        }
        try {
            String[] yearMonthDay = date.split("-");
            int year = Integer.parseInt(yearMonthDay[0]);
            int month = Integer.parseInt(yearMonthDay[1]);
            int day = Integer.parseInt(yearMonthDay[2]);
            return LocalDate.of(year, month, day);
        } catch (Exception e) {
            return LocalDate.now();
        }
    }

    public static List<TimeFrame> generateTimeFrameList(Integer start, Integer end) {
        List<TimeFrame> timeFrames = new ArrayList<>();
        for (int i = start; i < end; i++) {
            timeFrames.add(new TimeFrame(getTimeStringFromInt(i), getTimeStringFromInt(i + 1)));
        }
        return timeFrames;
    }

    public static LocalTime getLocalTimeFromTimeString(String time) {
        if (StringUtils.isEmpty(time)) {
            return LocalTime.now();
        }

        try {
            String[] times = time.split(":");
            int hour = Integer.parseInt(times[0]);
            int minute = Integer.parseInt(times[1]);
            return LocalTime.of(hour, minute);
        } catch (NumberFormatException e) {
            return LocalTime.now();
        }
    }
}

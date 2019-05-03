package com.hoangthien.pitchbooking.utils;

public class PitchBookingUtils {

    public static String getCostCommafy(Integer cost) {
        String regex = "(\\d)(?=(\\d{3})+$)";
        String costCommafy = "" + cost;
        if (costCommafy.length() > 4) {
            return costCommafy.replaceAll(regex, "$1,");
        }
        return costCommafy;
    }
}

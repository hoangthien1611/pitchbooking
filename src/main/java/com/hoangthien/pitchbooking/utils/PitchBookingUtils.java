package com.hoangthien.pitchbooking.utils;

import org.apache.commons.lang3.StringUtils;

public class PitchBookingUtils {

    public static Long getDistrictIdFromPathString(String path) {
        Long result = null;
        path = path.toLowerCase();

        if (!StringUtils.isEmpty(path)) {
            switch (path) {
                case "all":
                    result = 0L;
                    break;
                case "hai-chau":
                    result = 1L;
                    break;
                case "lien-chieu":
                    result = 2L;
                    break;
                case "thanh-khue":
                    result = 3L;
                    break;
                case "ngu-hanh-son":
                    result = 4L;
                    break;
                case "hoa-vang":
                    result = 5L;
                    break;
            }
        }

        return result;
    }
}

package com.modive.adminservice.global.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateUtils {

    /**
     * 주어진 날짜로부터 현재까지의 연도 차이 계산
     *
     * @param fromDate 기준 날짜
     * @return 연도 차이
     */
    public static int getYearsSince(LocalDateTime fromDate) {
        return (int) ChronoUnit.YEARS.between(fromDate, LocalDateTime.now());
    }
}

package com.backend.wear.dto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

// CreatedAt 포맷 클래스 (몇분전 등)
public class ConvertTime {
    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 30;
    public static final int MONTH = 12;

    public static String convertLocaldatetimeToTime(LocalDateTime localDateTime) {
        LocalDateTime now = LocalDateTime.now();

        long diffTime = localDateTime.until(now, ChronoUnit.SECONDS); // now보다 이후면 +, 전이면 -

        if (diffTime < SEC)
            return diffTime + "초 전";

        diffTime = diffTime / SEC;
        if (diffTime < MIN)
            return diffTime + "분 전";

        diffTime = diffTime / MIN;
        if (diffTime < HOUR)
            return diffTime + "시간 전";

        diffTime = diffTime / HOUR;
        if (diffTime < DAY)
            return diffTime + "일 전";

        diffTime = diffTime / DAY;
        if (diffTime < MONTH)
            return diffTime + "개월 전";

        diffTime=diffTime/MONTH;
        return diffTime+"년 전";
    }
}
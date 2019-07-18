package dev.blackbeast.springsvn.date;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

public class DateParser {
    public static Date parse(String text) {
        String[] patterns = new String[] {
                "yyyy-MM-dd",
                "yyyy-MM-dd HH:mm:ss",
                "dd.MM.yyyy",
                "dd.MM.yyyy HH:mm:ss"
        };

        try {
            return DateUtils.parseDate(text, patterns);
        } catch(Exception e) {
            return new Date();
        }
    }
}

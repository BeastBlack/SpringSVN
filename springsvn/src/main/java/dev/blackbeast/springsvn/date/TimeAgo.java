package dev.blackbeast.springsvn.date;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.Locale;

public class TimeAgo {
    public static String timeAgo(Date date) {
        PrettyTime p = new PrettyTime(new Locale("pl"));
        return p.format(date);
    }
}

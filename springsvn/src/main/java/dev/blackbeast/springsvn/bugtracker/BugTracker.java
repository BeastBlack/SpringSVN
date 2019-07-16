package dev.blackbeast.springsvn.bugtracker;

import dev.blackbeast.springsvn.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BugTracker {
    @Autowired
    ConfigService configService;

    public String format(String message) {
        Pattern pattern = Pattern.compile(configService.getAppBugTrackerPattern());
        Matcher m = pattern.matcher(message);

        StringBuffer bufStr = new StringBuffer();

        while (m.find()) {
            String rep = m.group();

            String replacement = configService.getConfigAppBugTrackerReplace()
                    .replace("@pattern", rep);

            m.appendReplacement(bufStr, replacement);
        }

        m.appendTail(bufStr);
        String result = bufStr.toString();
        return result;
    }
}

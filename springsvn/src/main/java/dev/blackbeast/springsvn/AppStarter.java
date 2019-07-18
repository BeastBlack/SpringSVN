package dev.blackbeast.springsvn;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.TimeZone;

@Component
public class AppStarter implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        TimeZone.setDefault(TimeZone.getDefault());
    }
}

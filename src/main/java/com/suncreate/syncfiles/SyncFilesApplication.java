package com.suncreate.syncfiles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SyncFilesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SyncFilesApplication.class, args);
    }

}

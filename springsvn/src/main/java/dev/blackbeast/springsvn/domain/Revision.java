package dev.blackbeast.springsvn.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Revision {
    private Long id;
    private String authorId;
    private String authorName;
    private Date date;
    private String timeAgo;
    private Long timeFromNow;
    private String message;
    private String rawMessage;

    public void setDate(Date date) {
        this.date = date;

        if(date != null) {
            this.timeFromNow = (new Date().getTime() - this.date.getTime()) / 1000L;
        }
    }
}

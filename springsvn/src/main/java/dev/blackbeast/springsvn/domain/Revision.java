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
    private String message;
}

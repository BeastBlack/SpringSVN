package dev.blackbeast.springsvn.domain;

import lombok.Data;

@Data
public class Location {
    private String path;
    private Long revision;
    private Long revisionTo;
    private Long revisionMax;

    public Location(String path, Long revision) {
        this.path = path;
        this.revision = revision;
        this.revisionTo = -1L;
        this.revisionMax = -1L;
    }

    public Location(String path, Long revision, Long revisionTo, Long revisionMax) {
        this.path = path;
        this.revision = revision;
        this.revisionTo = revisionTo;
        this.revisionMax = revisionMax;
    }
}

package dev.blackbeast.springsvn.domain;

import lombok.Data;

@Data
public class Location {
    private String path;
    private String parentPath;
    private Long revision;
    private Long revisionTo;
    private String thresholdFrom;
    private String thresholdTo;
    private Long revisionMax;
    private String searchText;

    public Location(String path, Long revision) {
        this.path = path;
        this.parentPath = getParentPath(path);
        this.revision = revision;
        this.revisionTo = -1L;
        this.thresholdFrom = null;
        this.thresholdTo = null;
        this.revisionMax = -1L;
        this.searchText = null;
    }

    public Location(String path, Long revision, Long revisionTo) {
        this.path = path;
        this.parentPath = getParentPath(path);
        this.revision = revision;
        this.revisionTo = revisionTo;
        this.thresholdFrom = null;
        this.thresholdTo = null;
        this.revisionMax = -1L;
        this.searchText = null;
    }

    public Location(String path, String thresholdFrom, String thresholdTo, Long revisionMax, String searchText) {
        this.path = path;
        this.parentPath = getParentPath(path);
        this.revision = -1L;
        this.revisionTo = -1L;
        this.thresholdFrom = thresholdFrom;
        this.thresholdTo = thresholdTo;
        this.revisionMax = revisionMax;
        this.searchText = searchText;
    }

    private String getParentPath(String path) {
        if(path == null)
            return null;
        else{
            String data = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;

            int lastIndexOf = data.lastIndexOf("/");

            if(lastIndexOf < 0)
                return "/";
            else
                return data.substring(0, lastIndexOf);
        }

    }
}

package dev.blackbeast.springsvn.domain;

import lombok.Data;

@Data
public class Location {
    private String path;
    private String parentPath;
    private Long revision;
    private Long revisionTo;
    private Long revisionMax;

    public Location(String path, Long revision) {
        this.path = path;
        this.parentPath = getParentPath(path);
        this.revision = revision;
        this.revisionTo = -1L;
        this.revisionMax = -1L;
    }

    public Location(String path, Long revision, Long revisionTo) {
        this.path = path;
        this.parentPath = getParentPath(path);
        this.revision = revision;
        this.revisionTo = revisionTo;
        this.revisionMax = -1L;
    }

    public Location(String path, Long revision, Long revisionTo, Long revisionMax) {
        this.path = path;
        this.parentPath = getParentPath(path);
        this.revision = revision;
        this.revisionTo = revisionTo;
        this.revisionMax = revisionMax;
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

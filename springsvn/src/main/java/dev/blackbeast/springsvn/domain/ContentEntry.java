package dev.blackbeast.springsvn.domain;

import lombok.Data;

@Data
public class ContentEntry {
    private String name;
    private Boolean isFile;
    private Revision lastRevision;
}

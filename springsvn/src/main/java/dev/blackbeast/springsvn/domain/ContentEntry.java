package dev.blackbeast.springsvn.domain;

import lombok.Data;

@Data
public class ContentEntry {
    private String name;
    private Revision lastRevision;
}

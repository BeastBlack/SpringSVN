package dev.blackbeast.springsvn.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Location {
    private String path;
    private Long revision;
}

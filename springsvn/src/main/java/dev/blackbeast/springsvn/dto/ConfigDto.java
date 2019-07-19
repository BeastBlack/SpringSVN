package dev.blackbeast.springsvn.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ConfigDto {
    @NotNull(message = "Adres repozytorium nie może być pusty")
    private String svnRepositoryAddress;
    private String svnLogin;
    private String svnPassword;
    private Boolean svnAuthentication;
    private Boolean appAnonAccess;
    private Boolean appBugTrackerIntegration;
    private String appBugTrackerPattern;
    private String appBugTrackerReplace;
    private Long appNewItemTimeThreshold;
}

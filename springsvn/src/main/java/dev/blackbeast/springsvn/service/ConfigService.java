package dev.blackbeast.springsvn.service;

import dev.blackbeast.springsvn.domain.Config;
import dev.blackbeast.springsvn.dto.ConfigDto;
import dev.blackbeast.springsvn.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ConfigService {
    private Map<String,String> configs = null;

    private final static String CONFIG_SVN_REPOSITORY_ADDRESS = "SvnRepositoryAddress";
    private final static String CONFIG_SVN_LOGIN = "SvnLogin";
    private final static String CONFIG_SVN_PASSWORD = "SvnPassword";
    private final static String CONFIG_SVN_AUTHENTICATION = "SvnAuthentication";
    private final static String CONFIG_APP_ANON_ACCESS = "AppAnonAccess";
    private final static String CONFIG_APP_BUG_TRACKER_INTEGRATION = "AppBugTrackerIntegration";
    private final static String CONFIG_APP_BUG_TRACKER_PATTERN = "AppBugTrackerPattern";
    private final static String CONFIG_APP_BUG_TRACKER_REPLACE = "AppBugTrackerReplace";
    private final static String CONFIG_SVN_AUTH_BASIC = "basic";
    private final static String CONFIG_SVN_AUTH_NONE = "none";
    private final static String CONFIG_APP_YES = "yes";
    private final static String CONFIG_APP_NO = "no";

    @Autowired
    ConfigRepository configRepository;

    public void refreshData() {
        configs = new HashMap<>();

        for(Config config : configRepository.findAll())
            configs.put(config.getName(), config.getValue());
    }

    private String getValue(String name) {
        if(configs == null)
            refreshData();

        return configs.get(name);
    }

    public String getSvnRepositoryAddress() {
        return getValue(CONFIG_SVN_REPOSITORY_ADDRESS);
    }

    public String getSvnLogin() {
        return getValue(CONFIG_SVN_LOGIN);
    }

    public String getSvnPassword() {
        return getValue(CONFIG_SVN_PASSWORD);
    }

    public Boolean isBasicAuthentication() {
        String authentication = getValue(CONFIG_SVN_AUTHENTICATION);

        return authentication != null ? authentication.toLowerCase().equals(CONFIG_SVN_AUTH_BASIC) : Boolean.FALSE;
    }

    public Boolean isAppAnonAccess() {
        String authentication = getValue(CONFIG_APP_ANON_ACCESS);

        return authentication != null ? authentication.toLowerCase().equals(CONFIG_APP_YES) : Boolean.TRUE;
    }

    public Boolean isAppBugTrackerIntegrationEnabled() {
        String value = getValue(CONFIG_APP_BUG_TRACKER_INTEGRATION);

        return value != null ? value.toLowerCase().equals(CONFIG_APP_YES) : Boolean.FALSE;
    }

    public String getAppBugTrackerPattern() {
        return getValue(CONFIG_APP_BUG_TRACKER_PATTERN);
    }

    public String getConfigAppBugTrackerReplace() {
        return getValue(CONFIG_APP_BUG_TRACKER_REPLACE);
    }

    public ConfigDto getConfiguration() {
        ConfigDto configDto = new ConfigDto();
        configDto.setSvnRepositoryAddress(this.getSvnRepositoryAddress());
        configDto.setSvnLogin(this.getSvnLogin());
        configDto.setSvnPassword(this.getSvnPassword());
        configDto.setSvnAuthentication(this.isBasicAuthentication());
        configDto.setAppAnonAccess(this.isAppAnonAccess());
        configDto.setAppBugTrackerIntegration(this.isAppBugTrackerIntegrationEnabled());
        configDto.setAppBugTrackerPattern(this.getAppBugTrackerPattern());
        configDto.setAppBugTrackerReplace(this.getConfigAppBugTrackerReplace());

        return configDto;
    }

    public void saveConfiguration(ConfigDto config) {
        Config conf = new Config();
        conf.setName(CONFIG_SVN_REPOSITORY_ADDRESS);
        conf.setValue(config.getSvnRepositoryAddress());
        configRepository.deleteByName(CONFIG_SVN_REPOSITORY_ADDRESS);
        configRepository.save(conf);

        conf = new Config();
        conf.setName(CONFIG_SVN_AUTHENTICATION);
        conf.setValue(config.getSvnAuthentication() ? CONFIG_SVN_AUTH_BASIC : CONFIG_SVN_AUTH_NONE);
        configRepository.deleteByName(CONFIG_SVN_AUTHENTICATION);
        configRepository.save(conf);

        if(!config.getSvnLogin().isEmpty()) {
            conf = new Config();
            conf.setName(CONFIG_SVN_LOGIN);
            conf.setValue(config.getSvnLogin());
            configRepository.deleteByName(CONFIG_SVN_LOGIN);
            configRepository.save(conf);
        }

        if(!config.getSvnPassword().isEmpty()) {
            conf = new Config();
            conf.setName(CONFIG_SVN_PASSWORD);
            conf.setValue(config.getSvnPassword());
            configRepository.deleteByName(CONFIG_SVN_PASSWORD);
            configRepository.save(conf);
        }

        conf = new Config();
        conf.setName(CONFIG_APP_ANON_ACCESS);
        conf.setValue(config.getAppAnonAccess() ? CONFIG_APP_YES : CONFIG_APP_NO);
        configRepository.deleteByName(CONFIG_APP_ANON_ACCESS);
        configRepository.save(conf);

        conf = new Config();
        conf.setName(CONFIG_APP_BUG_TRACKER_INTEGRATION);
        conf.setValue(config.getAppBugTrackerIntegration() ? CONFIG_APP_YES : CONFIG_APP_NO);
        configRepository.deleteByName(CONFIG_APP_BUG_TRACKER_INTEGRATION);
        configRepository.save(conf);

        conf = new Config();
        conf.setName(CONFIG_APP_BUG_TRACKER_PATTERN);
        conf.setValue(config.getAppBugTrackerPattern());
        configRepository.deleteByName(CONFIG_APP_BUG_TRACKER_PATTERN);
        configRepository.save(conf);

        conf = new Config();
        conf.setName(CONFIG_APP_BUG_TRACKER_REPLACE);
        conf.setValue(config.getAppBugTrackerReplace());
        configRepository.deleteByName(CONFIG_APP_BUG_TRACKER_REPLACE);
        configRepository.save(conf);

        refreshData();
    }
}

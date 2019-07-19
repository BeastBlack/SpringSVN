package dev.blackbeast.springsvn.service;

import dev.blackbeast.springsvn.cipher.TextCipher;
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
    private final static String CONFIG_APP_NEW_ITEM_TIME_THRESHOLD = "AppNewItemTimeThreshold";
    private final static String CONFIG_SVN_AUTH_BASIC = "basic";
    private final static String CONFIG_SVN_AUTH_NONE = "none";
    private final static String CONFIG_APP_YES = "yes";
    private final static String CONFIG_APP_NO = "no";

    @Autowired
    ConfigRepository configRepository;

    @Autowired
    TextCipher textCipher;

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
        return textCipher.decrypt(getValue(CONFIG_SVN_PASSWORD));
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

    public Long getAppNewItemTimeThreshold() {
        String value = getValue(CONFIG_APP_NEW_ITEM_TIME_THRESHOLD);

        return value != null ? Long.parseLong(value) : 0L;
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
        configDto.setAppNewItemTimeThreshold(this.getAppNewItemTimeThreshold());

        return configDto;
    }

    public void saveConfiguration(ConfigDto config) {
        write(CONFIG_SVN_REPOSITORY_ADDRESS, config.getSvnRepositoryAddress());
        write(CONFIG_SVN_AUTHENTICATION, config.getSvnAuthentication() ? CONFIG_SVN_AUTH_BASIC : CONFIG_SVN_AUTH_NONE);

        if(!config.getSvnLogin().isEmpty())
            write(CONFIG_SVN_LOGIN, config.getSvnLogin());

        if(!config.getSvnPassword().isEmpty())
            write(CONFIG_SVN_PASSWORD, textCipher.encrypt(config.getSvnPassword()));

        write(CONFIG_APP_ANON_ACCESS, config.getAppAnonAccess() ? CONFIG_APP_YES : CONFIG_APP_NO);
        write(CONFIG_APP_BUG_TRACKER_INTEGRATION, config.getAppBugTrackerIntegration() ? CONFIG_APP_YES : CONFIG_APP_NO);
        write(CONFIG_APP_BUG_TRACKER_PATTERN, config.getAppBugTrackerPattern());
        write(CONFIG_APP_BUG_TRACKER_REPLACE, config.getAppBugTrackerReplace());
        write(CONFIG_APP_NEW_ITEM_TIME_THRESHOLD, config.getAppNewItemTimeThreshold().toString());

        refreshData();
    }

    private void write(String name, String value) {
        Config conf = new Config();
        conf.setName(name);
        conf.setValue(value);
        configRepository.deleteByName(name);
        configRepository.save(conf);
    }
}

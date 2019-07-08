package dev.blackbeast.springsvn.service;

import dev.blackbeast.springsvn.domain.Config;
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
    private final static String CONFIG_SVN_AUTH_BASIC = "basic";

    @Autowired
    ConfigRepository configRepository;

    private String getValue(String name) {
        if(configs == null) {
            configs = new HashMap<>();

            for(Config config : configRepository.findAll())
                configs.put(config.getName(), config.getValue());
        }

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
        return getValue(CONFIG_SVN_AUTHENTICATION).toLowerCase().equals(CONFIG_SVN_AUTH_BASIC);
    }
}

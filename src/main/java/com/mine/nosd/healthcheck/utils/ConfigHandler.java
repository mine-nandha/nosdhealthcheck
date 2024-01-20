package com.mine.nosd.healthcheck.utils;

import com.mine.nosd.healthcheck.model.Configuration;
import com.mine.nosd.healthcheck.repository.ConfigMongoRepository;
import org.springframework.stereotype.Service;

@Service
public class ConfigHandler {
    ConfigMongoRepository configMongoRepository;

    public ConfigHandler(ConfigMongoRepository configMongoRepository) {
        this.configMongoRepository = configMongoRepository;
    }

    public Configuration getConfig() {
        return configMongoRepository.findById("config").get();
    }

    public Configuration updateConfig(Configuration configuration) {
        configMongoRepository.save(configuration);
        return configuration;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.service;

import ch.bmec.bmecscreen.config.Config;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 *
 * @author Thom
 */
@Component
public class YamlConfigurationService implements ConfigurationService {
    
    private final Logger log = LoggerFactory.getLogger(YamlConfigurationService.class);
    
    private Config config;
    
    @PostConstruct
    public void initConfig() {
        Yaml yaml = new Yaml(new Constructor(Config.class));
        config = (Config)yaml.load(this.getClass().getClassLoader().getResourceAsStream("config.yml"));
        
        log.debug("config loaded: " + config);
    }
    
    @Override
    public Config getConfig() {
        return config;
    }    
}

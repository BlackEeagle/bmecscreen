/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.service.socket;

import ch.bmec.bmecscreen.config.Config;
import ch.bmec.bmecscreen.service.configuration.ConfigurationAware;
import ch.bmec.bmecscreen.service.configuration.ConfigurationService;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Thom
 */
public abstract class AbstractSocketCommunicationService implements ConfigurationAware {
    
    @Autowired
    private ConfigurationService configService;

    protected SocketManager socketManager;

    @PostConstruct
    public void init() {
        socketManager = createSocketManager();
    }

    @PreDestroy
    public void destroy() {
        socketManager.disconnect();
    }

    @Override
    public final Config getConfig() {
        return configService.getConfig();
    }
    
    protected abstract SocketManager createSocketManager();
}

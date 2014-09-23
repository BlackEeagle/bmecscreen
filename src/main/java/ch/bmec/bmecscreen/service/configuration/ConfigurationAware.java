/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.service.configuration;

import ch.bmec.bmecscreen.config.Config;

/**
 *
 * @author Thom
 */
public interface ConfigurationAware {
    
    public Config getConfig();
    
}

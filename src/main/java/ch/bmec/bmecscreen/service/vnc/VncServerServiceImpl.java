/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.service.vnc;

import ch.bmec.bmecscreen.config.VncConfig;
import ch.bmec.bmecscreen.service.configuration.ConfigurationService;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Thom
 */
@Component
public class VncServerServiceImpl implements VncServerService {

    private final Logger log = LoggerFactory.getLogger(VncServerServiceImpl.class);

    @Autowired
    private ConfigurationService configurationService;

    private boolean isVncServerRunning;

    @Override
    public void startVncServer() {

        if (isVncServerRunning == false) {
            executeCommands(getPathToExecutable(), "-run");
            isVncServerRunning = true;
        }
    }

    @Override
    public void shutdownVncServer() {

        if (isVncServerRunning == true) {
            executeCommands(getPathToExecutable(), "-controlapp", "-shutdown");
            isVncServerRunning = false;
        }
    }

    @Override
    public void applyNewTopLeft(int top, int left) {

        if (isVncServerRunning == true) {

            StringBuilder resAndTopLeft = new StringBuilder();
            resAndTopLeft.append(getConfig().getViewerResolution().getWidth());
            resAndTopLeft.append("x");
            resAndTopLeft.append(getConfig().getViewerResolution().getHeight());
            resAndTopLeft.append("+").append(left);
            resAndTopLeft.append("+").append(top);

            // workaround for refresh bug
            executeCommands(getPathToExecutable(), "-controlapp", "-sharerect", "0x0+0+0");
            
            try {
                Thread.sleep(400);
            } catch (InterruptedException ex) {
                // nothing
            }
            
            executeCommands(getPathToExecutable(), "-controlapp", "-sharerect", resAndTopLeft.toString());
        }
    }

    private String getPathToExecutable() {
        return getConfig().getPath() + "tvnserver.exe";
    }

    private void executeCommands(String... commands) {
        
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        
        try {            
            log.trace("execute " + Arrays.asList(commands).toString());
            processBuilder.start();
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    private VncConfig getConfig() {
        return configurationService.getConfig().getVncConfig();
    }
}

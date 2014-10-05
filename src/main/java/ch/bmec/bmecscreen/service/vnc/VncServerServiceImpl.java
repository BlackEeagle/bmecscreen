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
            executeCommands("tvnserver.exe", "-run");
            isVncServerRunning = true;
        }
    }

    @Override
    public void shutdownVncServer() {

        if (isVncServerRunning == true) {
            executeCommands("tvnserver.exe", "-controlapp", "-shutdown");
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
            resAndTopLeft.append("+").append(top).append("+").append(left);

            executeCommands("tvnserver.exe", "-controlapp", "sharerect", resAndTopLeft.toString());
        }
    }

    private void executeCommands(String... commands) {
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.directory(new File(getConfig().getPath()));

        try {
            log.trace("execute " + Arrays.asList(commands).toString() + " in " + getConfig().getPath());
            Process process = processBuilder.start();
            process.waitFor();

        } catch (IOException | InterruptedException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    private VncConfig getConfig() {
        return configurationService.getConfig().getVncConfig();
    }
}
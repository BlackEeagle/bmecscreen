/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.controller;

import ch.bmec.bmecscreen.service.configuration.ConfigurationService;
import ch.bmec.bmecscreen.service.ecos.EcosCommunicationService;
import ch.bmec.bmecscreen.service.ecos.EcosSystemStatus;
import ch.bmec.bmecscreen.service.rpi.RPiCommunicationService;
import ch.bmec.bmecscreen.service.rpi.pushbutton.PushbuttonConfiguration;
import ch.bmec.bmecscreen.service.vnc.VncServerService;
import ch.bmec.bmecscreen.ui.SystemUiController;
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
public class SystemControllerImpl implements SystemController {

    private final Logger log = LoggerFactory.getLogger(SystemControllerImpl.class);

    @Autowired
    private ConfigurationService configService;

    @Autowired
    private EcosCommunicationService ecosCommunicationService;

    @Autowired
    private RPiCommunicationService rPiCommunicationService;

    @Autowired
    private VncServerService vncServerService;

    @Autowired
    private VncViewerController vncViewerController;

    @Autowired
    private SystemUiController uiController;

    private boolean hasEcosConnection;

    private boolean rPiPowerOn;

    private boolean hasRPiConnection;

    private boolean isRPiAlive;

    private boolean pushbuttonServerThreadStarted;

    private boolean vncServerStarted;

    private boolean vncRPIStarted;

    @Override
    public Void initializeSystem() {

        checkEcosConnection();

        if (hasEcosConnection) {
            EcosSystemStatus ecosStatus = ecosCommunicationService.requestSystemStatus();

            rPiPowerOn = ecosStatus == EcosSystemStatus.ON;
        }

        if (rPiPowerOn) {
            hasRPiConnection = rPiCommunicationService.checkConnection();

            if (hasRPiConnection) {
                isRPiAlive = rPiCommunicationService.isAlive();
            }
        }
        uiController.setRPiStatus(isRPiAlive);

        uiController.setSystemStatus(isRPiAlive && pushbuttonServerThreadStarted && vncServerStarted);
        return null;
    }

    @Override
    public Void startupSystem() {

        if (hasEcosConnection == false) {
            checkEcosConnection();
        }

        if (hasEcosConnection && rPiPowerOn == false) {
            ecosCommunicationService.turnSystemOn();
            rPiPowerOn = true;
        }

        checkIfRPiIsAlive();

        if (isRPiAlive) {
            if (pushbuttonServerThreadStarted == false) {
                rPiCommunicationService.startPushbuttonServerThread();
                pushbuttonServerThreadStarted = true;
            }

            if (vncServerStarted == false) {
                vncServerService.startVncServer();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    // nothing
                }
                vncServerService.applyNewTopLeft(0, 0);
                vncServerStarted = true;
            }
            uiController.setVncServerStatus(vncServerStarted);
        }

        uiController.setSystemStatus(isRPiAlive && pushbuttonServerThreadStarted && vncServerStarted);
        return null;
    }

    @Override
    public Void shutdownSystem() {

        if (vncServerStarted) {
            vncServerService.shutdownVncServer();
            vncServerStarted = false;
        }
        uiController.setVncServerStatus(vncServerStarted);

        if (pushbuttonServerThreadStarted) {
            rPiCommunicationService.stopPushbuttonServerThread();
            pushbuttonServerThreadStarted = false;
        }

        if (vncRPIStarted) {
            stopVncOnRPi();
        }

        if (hasRPiConnection) {
            boolean shutdown = rPiCommunicationService.shutdown();
            if (shutdown) {
                rPiCommunicationService.disconnect();
                hasRPiConnection = false;
                isRPiAlive = false;
            }
        }
        uiController.setRPiStatus(hasRPiConnection);

        if (hasRPiConnection == false && hasEcosConnection) {
            // wait till RPi is shut down
            try {
                Thread.sleep(configService.getConfig().getEcosConfig().getWaitTillShutdown() * 1_000);
            } catch (InterruptedException ex) {
                // nothing
            }
            ecosCommunicationService.turnSystemOff();
            rPiPowerOn = false;
            ecosCommunicationService.disconnect();
            hasEcosConnection = false;
        }
        uiController.setEcosConnection(hasEcosConnection);

        uiController.setSystemStatus(isRPiAlive && pushbuttonServerThreadStarted && vncServerStarted);
        return null;
    }

    @Override
    public Void startVncOnRPi() {

        if (vncRPIStarted == false && isRPiAlive && vncServerStarted) {
            vncRPIStarted = rPiCommunicationService.activateVncClient();
        }

        if (vncRPIStarted) {
            PushbuttonConfiguration currentPushConfig = vncViewerController.currentPushConfig();
            rPiCommunicationService.applyPushbottunConfiguration(currentPushConfig);
        }

        uiController.setVncClientStatus(vncRPIStarted);
        return null;
    }

    @Override
    public Void stopVncOnRPi() {

        if (vncRPIStarted && isRPiAlive) {
            vncRPIStarted = !rPiCommunicationService.deactivateVncDisplay();
        }

        if (vncRPIStarted == false) {
            rPiCommunicationService.applyPushbottunConfiguration(new PushbuttonConfiguration(false, false, false, false, false, false));
            vncViewerController.reset();
        }

        uiController.setVncClientStatus(vncRPIStarted);
        return null;
    }

    @Override
    public Void rebootRPi() {

        stopVncOnRPi();

        if (hasRPiConnection) {

            boolean reboot = rPiCommunicationService.reboot();
            if (reboot) {
                rPiCommunicationService.disconnect();
                hasRPiConnection = false;
                isRPiAlive = false;
            }
        }
        uiController.setRPiStatus(hasRPiConnection);

        checkIfRPiIsAlive();
        return null;
    }

    private void checkIfRPiIsAlive() {
        if (rPiPowerOn) {
            int tries = 0;
            
            do {

                if (hasRPiConnection == false || isRPiAlive == false) {
                    hasRPiConnection = rPiCommunicationService.checkConnection();
                }

                if (hasRPiConnection) {
                    isRPiAlive = rPiCommunicationService.isAlive();
                }

                if (isRPiAlive == false) {
                    // give 1 sec to wake up and try again...
                    try {
                        Thread.sleep(1_000);
                    } catch (InterruptedException ex) {
                        log.error(ex.getMessage(), ex);
                    }
                    tries++;
                }

            } while (!isRPiAlive&& tries < 30);
        }
        uiController.setRPiStatus(isRPiAlive);
    }

    private void checkEcosConnection() {
        hasEcosConnection = ecosCommunicationService.checkConnection();
        uiController.setEcosConnection(hasEcosConnection);
    }
}

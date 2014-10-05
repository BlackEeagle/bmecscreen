/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.controller;

import ch.bmec.bmecscreen.service.ecos.EcosCommunicationService;
import ch.bmec.bmecscreen.service.ecos.EcosSystemStatus;
import ch.bmec.bmecscreen.service.rpi.RPiCommunicationService;
import ch.bmec.bmecscreen.service.vnc.VncServerService;
import ch.bmec.bmecscreen.ui.SystemUiController;
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
    private EcosCommunicationService ecosCommunicationService;
    
    @Autowired
    private RPiCommunicationService rPiCommunicationService;
    
    @Autowired
    private VncServerService vncServerService;
    
    @Autowired
    private SystemUiController uiController;
    
    private boolean hasEcosConnection;
    
    private boolean rPiPowerOn;
    
    private boolean hasRPiConnection;
    
    private boolean isRPiAlive;
    
    private boolean pushbuttonServerThreadStarted;
    
    private boolean vncServerStarted;
    
    @Override
    public void initializeSystem() {
        
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
    }
    
    @Override
    public void startupSystem() {
        
        if (hasEcosConnection == false) {
            checkEcosConnection();
        }
        
        if (hasEcosConnection && rPiPowerOn == false) {
            ecosCommunicationService.turnSystemOn();
            rPiPowerOn = true;
        }
        
        if (rPiPowerOn) {
            do {
                
                if (hasRPiConnection == false) {
                    hasRPiConnection = rPiCommunicationService.checkConnection();
                }
                
                if (hasRPiConnection) {
                    isRPiAlive = rPiCommunicationService.isAlive();
                }
                
                if (isRPiAlive == false) {
                    // give 500 millis to wake up and try again...
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        log.error(ex.getMessage(), ex);
                    }
                }
                
            } while (!isRPiAlive);
        }
        uiController.setRPiStatus(isRPiAlive);
        
        if (isRPiAlive) {
            if (pushbuttonServerThreadStarted == false) {
                rPiCommunicationService.startPushbuttonServerThread();
                pushbuttonServerThreadStarted = true;
            }
            
            if (vncServerStarted == false) {
                vncServerService.startVncServer();
                vncServerStarted = true;
            }
            uiController.setVncServerStatus(vncServerStarted);
        }
        
        uiController.setSystemStatus(isRPiAlive && pushbuttonServerThreadStarted && vncServerStarted);
    }
    
    @Override
    public void shutdownSystem() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void startVncOnRPi() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void stopVncOnRPi() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void rebootRPi() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void checkEcosConnection() {
        hasEcosConnection = ecosCommunicationService.checkConnection();
        uiController.setEcosConnection(hasEcosConnection);
    }
}

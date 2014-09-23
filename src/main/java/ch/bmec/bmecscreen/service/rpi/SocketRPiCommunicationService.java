/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.service.rpi;

import ch.bmec.bmecscreen.config.RPiConfig;
import ch.bmec.bmecscreen.service.ecos.SocketEcosCommunicationService;
import ch.bmec.bmecscreen.service.socket.AbstractSocketCommunicationService;
import ch.bmec.bmecscreen.service.socket.SocketManager;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Thom
 */
public class SocketRPiCommunicationService extends AbstractSocketCommunicationService implements RPiCommunicationService {

    private final Logger log = LoggerFactory.getLogger(SocketEcosCommunicationService.class);
    
    @Override
    protected SocketManager createSocketManager() {
        return new SocketManager(getRPiConfig().getIp(), getRPiConfig().getTcpPort(), getRPiConfig().getTimeout());
    }

    @Override
    public boolean checkConnection() {
        
        boolean isConnected = false;

        try {
            if (socketManager.isConnected() == false) {
                socketManager.connect();
            }
                
            isConnected = socketManager.isConnected();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }

        return isConnected;
        
    }

    @Override
    public boolean isAlive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean activateVncDisplay() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deactivateVncDisplay() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean rebootVncViewer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean shutdown() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean reboot() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean applyPushbottunConfiguration(PushbuttonConfiguration pushbuttonConfiguration) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean configureTv(TvCommand command) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void startPushPushedThread() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private RPiConfig getRPiConfig() {
        return getConfig().getrPiConfig();
    }
}

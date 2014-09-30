/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.service.rpi.server;

import ch.bmec.bmecscreen.service.socket.SocketManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Thom
 */
public class RPiPushButtonClientConnection implements Runnable {
    
    private final Logger log = LoggerFactory.getLogger(RPiPushButtonClientConnection.class);
    private final SocketManager socketManager;
    
    private final RPiPushButtonServerThread server;
    
    public RPiPushButtonClientConnection(SocketManager socketManager, RPiPushButtonServerThread server) {
        this.socketManager = socketManager;
        this.server = server;
    }
    
    @Override
    public void run() {
        
        try {
            while (true) {
                log.trace("wait for message...");
                String message = socketManager.read(128);
                log.trace("got message: " + message);
            }
        } catch (RemoteSocketClosedConnectionException closedEx) {
            // nothing, just ok
            log.trace("client socket closed connection");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            server.unregisterClient(socketManager.getSocket());
        }
    }
    
}

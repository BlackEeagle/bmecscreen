/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.service.rpi.server;

import java.net.Socket;

/**
 *
 * @author Thom
 */
public interface RPiPushButtonServerThread extends Runnable {

    void shutdownServerAndClients();

    void unregisterClient(Socket clientSocket);
    
}

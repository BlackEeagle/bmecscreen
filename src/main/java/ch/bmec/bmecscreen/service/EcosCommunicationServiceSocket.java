/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.springframework.stereotype.Component;

/**
 *
 * @author Thom
 */
@Component
public class EcosCommunicationServiceSocket implements EcosCommunicationService {

    @Override
    public boolean checkConnection() {
        
        boolean isConnected = false;
        
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("192.168.0.104", 15471), 2000);
            
            isConnected = socket.isConnected();
        } catch (IOException ex) {
            // nothing...
        }
        
        return isConnected;
    }

    

}

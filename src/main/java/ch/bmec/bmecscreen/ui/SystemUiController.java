/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.ui;

/**
 *
 * @author Thom
 */
public interface SystemUiController {
    
    public void setSystemStatus(boolean systemUp);
    
    public void setEcosConnection(boolean hasEcosConnection);
    
    public void setRPiStatus(boolean rPiAlive);
    
    public void setVncServerStatus(boolean vncServerStarted);
    
    public void setVncClientStatus(boolean vncClientStatus);
    
    public void setPushbuttonServerStatus(boolean serverStarted, String ipAddress, int port);
    
    public void setPushbuttonServerClientCount(int clientCount);
}

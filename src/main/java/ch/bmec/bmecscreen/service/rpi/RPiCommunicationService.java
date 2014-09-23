/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.service.rpi;

/**
 *
 * @author Thom
 */
public interface RPiCommunicationService {
    
    public boolean checkConnection();
    
    public boolean isAlive();
    
    public boolean activateVncDisplay();
    
    public boolean deactivateVncDisplay();
    
    public boolean rebootVncViewer();
    
    public boolean shutdown();
    
    public boolean reboot();
    
    public boolean applyPushbottunConfiguration(PushbuttonConfiguration pushbuttonConfiguration);
    
    public boolean configureTv(TvCommand command);
    
    public void startPushPushedThread();
    
}

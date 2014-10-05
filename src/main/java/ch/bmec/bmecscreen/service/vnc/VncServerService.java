/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.service.vnc;

/**
 *
 * @author Thom
 */
public interface VncServerService {
    
    public void startVncServer();
    
    public void shutdownVncServer();
    
    public void applyNewTopLeft(int top, int left);
    
}

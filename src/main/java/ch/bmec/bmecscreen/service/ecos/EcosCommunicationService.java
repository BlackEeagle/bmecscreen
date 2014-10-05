/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.service.ecos;

/**
 *
 * @author Thom
 */
public interface EcosCommunicationService {

    public boolean checkConnection();
    
    public boolean turnSystemOn();
    
    public boolean turnSystemOff();
    
    public EcosSystemStatus requestSystemStatus();
}

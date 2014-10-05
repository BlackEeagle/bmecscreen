/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.controller;

/**
 *
 * @author Thom
 */
public interface SystemController {
    
    public void initializeSystem();
    
    public void startupSystem();
    
    public void shutdownSystem();
    
    public void startVncOnRPi();
    
    public void stopVncOnRPi();
    
    public void rebootRPi();
}

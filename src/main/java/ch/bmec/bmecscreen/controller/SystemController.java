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
    
    public Void initializeSystem();
    
    public Void startupSystem();
    
    public Void shutdownSystem();
    
    public Void startVncOnRPi();
    
    public Void stopVncOnRPi();
    
    public Void rebootRPi();
}

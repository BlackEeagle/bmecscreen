/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.service.rpi.tvcommand;

/**
 *
 * @author Thom
 */
public interface TvCommandVisitor {
    
    public String getTvCommandPart(SimpleTvCommand command);
    
}

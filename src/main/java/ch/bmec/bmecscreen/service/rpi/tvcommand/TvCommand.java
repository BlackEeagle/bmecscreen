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
public abstract class TvCommand {
    
    protected TvCommandType type;

    public TvCommand(TvCommandType type) {
        this.type = type;
    }

    public TvCommandType getType() {
        return type;
    }
    
    public abstract String acceptTvCommandPart(TvCommandVisitor visitor);
    
    public abstract String getCommand();    
}

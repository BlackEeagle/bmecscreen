/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.config;

/**
 *
 * @author Thom
 */
public class Config {

    private EcosConfig ecosConfig;

    private RPiConfig rPiConfig;

    public Config() {

    }

    public EcosConfig getEcosConfig() {
        return ecosConfig;
    }

    public void setEcosConfig(EcosConfig ecosConfig) {
        this.ecosConfig = ecosConfig;
    }

    public RPiConfig getrPiConfig() {
        return rPiConfig;
    }

    public void setrPiConfig(RPiConfig rPiConfig) {
        this.rPiConfig = rPiConfig;
    }

    @Override
    public String toString() {
        return "Config{" + "ecosConfig=" + ecosConfig + ", rPiConfig=" + rPiConfig + '}';
    }
}

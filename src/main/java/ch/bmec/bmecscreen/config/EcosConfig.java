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
public class EcosConfig extends TcpConfig {

    private String protocol;

    private String address;

    private int waitTillShutdown;

    public EcosConfig() {
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getWaitTillShutdown() {
        return waitTillShutdown;
    }

    public void setWaitTillShutdown(int waitTillShutdown) {
        this.waitTillShutdown = waitTillShutdown;
    }

    @Override
    public String toString() {
        return "EcosConfig{:" + super.toString() + "protocol=" + protocol + ", address=" + address + '}';
    }

}

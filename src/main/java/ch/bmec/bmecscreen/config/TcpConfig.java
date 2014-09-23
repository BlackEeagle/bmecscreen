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
public class TcpConfig {

    private String ip;
    
    private int tcpPort;
    
    private int timeout;

    public TcpConfig() {
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return "TcpConfig{" + "ip=" + ip + ", tcpPort=" + tcpPort + ", timeout=" + timeout + '}';
    }
}

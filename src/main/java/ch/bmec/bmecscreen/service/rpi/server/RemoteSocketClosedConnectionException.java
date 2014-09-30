/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.service.rpi.server;

import java.net.SocketException;

/**
 *
 * @author Thom
 */
public class RemoteSocketClosedConnectionException extends SocketException {

    public RemoteSocketClosedConnectionException() {
    }

    public RemoteSocketClosedConnectionException(String msg) {
        super(msg);
    }
}

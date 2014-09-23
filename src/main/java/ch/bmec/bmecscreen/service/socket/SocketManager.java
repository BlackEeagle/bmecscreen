/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.service.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Thom
 */
public class SocketManager {

    private final Logger log = LoggerFactory.getLogger(SocketManager.class);

    private final Socket socket;

    private final String ipAddress;

    private final int port;

    private final int timeout;

    private BufferedReader reader;

    private PrintWriter writer;

    public SocketManager(String ipAddress, int port, int timeout) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.timeout = timeout;
        socket = new Socket();
    }

    public void connect() throws IOException {
        socket.connect(new InetSocketAddress(ipAddress, port), timeout);
    }

    public void disconnect() {
        try {
            if (socket.isConnected()) {
                socket.close();
            }
        } catch (IOException ioe) {
            log.error(ioe.getMessage(), ioe);
        }
    }

    public BufferedReader getReader() throws IOException {

        if (socket.isConnected() == false) {
            connect();
        }

        if (reader == null) {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        return reader;
    }

    public PrintWriter getWriter() throws IOException {

        if (socket.isConnected() == false) {
            connect();
        }

        if (writer == null) {
            writer = new PrintWriter(socket.getOutputStream());
        }

        return writer;
    }

    public boolean isConnected() {

        return socket.isConnected();
    }
}

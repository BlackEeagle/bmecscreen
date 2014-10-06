/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.service.socket;

import ch.bmec.bmecscreen.service.rpi.server.RemoteSocketClosedConnectionException;
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

    private Socket socket;

    private final String ipAddress;

    private final int port;

    private final int timeout;

    private BufferedReader reader;

    private PrintWriter writer;

    private boolean alreadyConnected;

    private boolean isConnected;
    
    public SocketManager(String ipAddress, int port, int timeout) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.timeout = timeout;
        socket = new Socket();
    }

    public SocketManager(Socket socket) {
        ipAddress = null;
        port = 0;
        timeout = 0;
        this.socket = socket;
    }

    public void connect() throws IOException {

        if (alreadyConnected) {
            if (socket.isClosed() == false) {
                socket.close();
            }
            
            socket = new Socket();
        }

        alreadyConnected = true;
        isConnected = true;
        socket.connect(new InetSocketAddress(ipAddress, port), timeout);
    }

    public void disconnect() {

        try {
            if (socket != null && socket.isConnected() && socket.isClosed() == false) {
                log.trace("close connection: " + socket.toString());
                socket.close();
                isConnected = false;
                writer = null;
                reader = null;
            }
        } catch (IOException ioe) {
            log.error(ioe.getMessage(), ioe);
        }
    }

    private BufferedReader getReader() throws IOException {

        if (socket.isConnected() == false) {
            connect();
        }

        if (reader == null) {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        return reader;
    }

    private PrintWriter getWriter() throws IOException {

        if (socket.isConnected() == false) {
            connect();
        }

        if (writer == null) {
            writer = new PrintWriter(socket.getOutputStream());
        }

        return writer;
    }

    public boolean isConnected() {

        return isConnected;
    }

    public boolean isClosed() {

        return socket.isClosed();
    }

    public String readLine() throws IOException {
        String line = getReader().readLine();
        log.trace("--> " + line);

        return line;
    }

    public String read(int buffSize) throws IOException {
        char[] buffer = new char[buffSize];
        getReader().read(buffer);

        String content = new String(buffer).trim();
        log.trace("--> " + content);

        if ("".equals(content)) {
            throw new RemoteSocketClosedConnectionException("remote socket closed connection");
        }

        return content;
    }

    public void write(String message) throws IOException {

        log.trace("<-- " + message);
        getWriter().write(message);
    }

    public void writeAndFlush(String message) throws IOException {

        log.trace("<-- " + message);
        getWriter().write(message);
        getWriter().flush();
    }

    public Socket getSocket() {
        return socket;
    }
}

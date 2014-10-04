/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.service.rpi.server;

import ch.bmec.bmecscreen.service.configuration.ConfigurationService;
import ch.bmec.bmecscreen.service.socket.SocketManager;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Thom
 */
@Component
@Scope("prototype")
public class SocketRPiPushButtonServerThread implements RPiPushButtonServerThread {

    private final Logger log = LoggerFactory.getLogger(SocketRPiPushButtonServerThread.class);

    private ExecutorService executorService;

    private Set<Socket> clientSockets;

    private ServerSocket serverSocket;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ConfigurationService configurationService;

    @PostConstruct
    public void startup() {
        executorService = Executors.newSingleThreadExecutor();
        clientSockets = new HashSet<>();
    }

    @PreDestroy
    public void cleanup() {
        shutdownServerAndClients();
    }

    @Override
    public void run() {
        try {

            int port = configurationService.getConfig().getrPiConfig().getServerPort();

            serverSocket = new ServerSocket(port);

            while (serverSocket.isClosed() == false) {

                log.trace("wait for connection...");
                Socket clientSocket = serverSocket.accept();
                clientSockets.add(clientSocket);
                log.trace("connection from " + clientSocket);

                SocketManager socketManager = new SocketManager(clientSocket);

                RPiPushButtonClientConnection clientConnection = applicationContext.getBean(RPiPushButtonClientConnection.class, socketManager, this);

                executorService.submit(clientConnection);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException ex) {
                // nothing
            }
        }
    }

    @Override
    public void shutdownServerAndClients() {

        executorService.shutdownNow();

        clientSockets.stream().forEach((socket) -> {
            try {
                socket.close();
            } catch (IOException ex) {
                // nothing, just close
            }
        });

        try {
            serverSocket.close();
        } catch (IOException ex) {
            // nothing
        }
    }

    @Override
    public void unregisterClient(Socket clientSocket) {
        clientSockets.remove(clientSocket);
    }
}

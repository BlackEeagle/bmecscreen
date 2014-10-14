/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.service.rpi;

import ch.bmec.bmecscreen.service.rpi.tvcommand.TvCommandType;
import ch.bmec.bmecscreen.service.rpi.pushbutton.PushbuttonConfiguration;
import ch.bmec.bmecscreen.config.RPiConfig;
import ch.bmec.bmecscreen.service.rpi.pushbutton.PushbuttonConfigurationService;
import ch.bmec.bmecscreen.service.rpi.server.RPiPushButtonServerThread;
import ch.bmec.bmecscreen.service.rpi.tvcommand.SimpleTvCommand;
import ch.bmec.bmecscreen.service.rpi.tvcommand.TvCommand;
import ch.bmec.bmecscreen.service.rpi.tvcommand.TvCommandVisitor;
import ch.bmec.bmecscreen.service.socket.AbstractSocketCommunicationService;
import ch.bmec.bmecscreen.service.socket.SocketManager;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Thom
 */
@Component
public class SocketRPiCommunicationService extends AbstractSocketCommunicationService implements RPiCommunicationService, TvCommandVisitor {

    private final Logger log = LoggerFactory.getLogger(SocketRPiCommunicationService.class);

    private ExecutorService executorService;

    private boolean serverThreadStarted;

    @Autowired
    private RPiPushButtonServerThread serverThread;

    @Autowired
    private PushbuttonConfigurationService pushbuttonConfigurationService;

    @PreDestroy
    public void cleanup() {
        stopPushbuttonServerThread();
    }

    @Override
    protected SocketManager createSocketManager() {
        return new SocketManager(getRPiConfig().getIp(), getRPiConfig().getTcpPort(), getRPiConfig().getTimeout());
    }

    @Override
    public boolean checkConnection() {

        boolean isConnected = false;

        try {
            if (socketManager.isConnected() == false) {
                socketManager.connect();
            }

            isConnected = socketManager.isConnected();
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }

        return isConnected;

    }

    @Override
    public void disconnect() {
        socketManager.disconnect();
    }  
    

    @Override
    public boolean isAlive() {

        return sendAndReceive("BMECScreen1-alive-", "BMECScreen2-hello-");
    }

    @Override
    public boolean activateVncClient() {

        return sendAndReceive("BMECScreen1-status-on-", "BMECScreen2-status-ok-");
    }

    @Override
    public boolean deactivateVncDisplay() {

        return sendAndReceive("BMECScreen1-status-off-", "BMECScreen2-status-ok-");
    }

    @Override
    public boolean rebootVncViewer() {

        return sendAndReceive("BMECScreen1-vncrestart-", "BMECScreen2-vncrestart-ok-");
    }

    @Override
    public boolean shutdown() {

        return sendAndReceive("BMECScreen1-shutdown-", "BMECScreen2-shutdown-ok-");
    }

    @Override
    public boolean reboot() {

        return sendAndReceive("BMECScreen1-reboot-", "BMECScreen2-reboot-ok-");
    }

    @Override
    public boolean applyPushbottunConfiguration(PushbuttonConfiguration pushbuttonConfiguration) {

        String message = pushbuttonConfigurationService.createMessageForRPi(pushbuttonConfiguration);

        return sendAndReceive(message, "BMECScreen2-pushbuttonLight-ok-");
    }

    @Override
    public boolean configureTv(TvCommand command) {
        
        StringBuilder tvCommand = new StringBuilder();
        tvCommand.append("BMECScreen1-tv-");
        tvCommand.append(command.acceptTvCommandPart(this));
        tvCommand.append("-");
        
        return sendAndReceive(tvCommand.toString(), "BMECScreen2-tv-ok-");
    }

    @Override
    public void startPushbuttonServerThread() {

        if (serverThreadStarted == false) {
            executorService = Executors.newSingleThreadExecutor();

            executorService.submit(serverThread);
            serverThreadStarted = true;
        }
    }

    @Override
    public void stopPushbuttonServerThread() {
        if (executorService != null) {
            executorService.shutdownNow();
            serverThread.shutdownServerAndClients();
            serverThreadStarted = false;
        }
    }

    private RPiConfig getRPiConfig() {
        return getConfig().getrPiConfig();
    }

    private boolean sendAndReceive(String messageToSend, String expectedAnswer) {

        boolean success = false;

        if (messageToSend != null && expectedAnswer != null) {
            try {
                socketManager.writeAndFlush(messageToSend);

                String answer = socketManager.read(128);

                success = expectedAnswer.equals(answer);

            } catch (IOException ex) {
                log.error(ex.getMessage(), ex);
            }
        }

        return success;
    }
    
    @Override
    public String getTvCommandPart(SimpleTvCommand simpleTvCommand) {
        return simpleTvCommand.getCommand();
    }
}

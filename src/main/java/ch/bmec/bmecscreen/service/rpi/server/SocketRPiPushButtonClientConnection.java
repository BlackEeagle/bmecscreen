/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.service.rpi.server;

import ch.bmec.bmecscreen.javafx.controller.RPiUiOutput;
import ch.bmec.bmecscreen.service.rpi.RPiCommunicationService;
import ch.bmec.bmecscreen.service.rpi.pushbutton.PushbuttonConfiguration;
import ch.bmec.bmecscreen.service.rpi.pushbutton.PushbuttonConfigurationService;
import ch.bmec.bmecscreen.service.socket.SocketManager;
import ch.bmec.bmecscreen.service.vnc.VncViewerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Thom
 */
@Component
@Scope("prototype")
public class SocketRPiPushButtonClientConnection implements RPiPushButtonClientConnection {

    private final Logger log = LoggerFactory.getLogger(SocketRPiPushButtonClientConnection.class);

    private final SocketManager socketManager;

    private final RPiPushButtonServerThread server;

    @Autowired
    private RPiUiOutput rPiUiOutput;

    @Autowired
    private PushbuttonConfigurationService pushbuttonConfigurationService;

    @Autowired
    private VncViewerController vncViewerController;

    @Autowired
    private RPiCommunicationService rPiCommunicationService;

    public SocketRPiPushButtonClientConnection(SocketManager socketManager, RPiPushButtonServerThread server) {
        this.socketManager = socketManager;
        this.server = server;
    }

    @Override
    public void run() {

        try {
            while (true) {
                log.trace("wait for message...");
                String message = socketManager.read(128);
                log.trace("got message: " + message);

                PushbuttonConfiguration pushbuttonConfiguration = pushbuttonConfigurationService.parseMessageFromRPi(message);

                boolean successfullyParsed = pushbuttonConfiguration != null;

                String response = buildResponse(successfullyParsed);

                socketManager.writeAndFlush(response);

                rPiUiOutput.displayOutput(message);

                if (successfullyParsed) {
                    pushbuttonConfiguration = vncViewerController.handlePush(pushbuttonConfiguration);

                    rPiCommunicationService.applyPushbottunConfiguration(pushbuttonConfiguration);
                }

            }
        } catch (RemoteSocketClosedConnectionException closedEx) {
            // nothing, just ok
            log.trace("client socket closed connection");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            server.unregisterClient(socketManager.getSocket());
        }
    }

    private String buildResponse(boolean success) {

        StringBuilder builder = new StringBuilder();

        builder.append("BMECScreen1-pushbuttonPushed-");

        if (success) {
            builder.append("ok");
        } else {
            builder.append("nok");
        }

        builder.append("-");

        return builder.toString();
    }
}

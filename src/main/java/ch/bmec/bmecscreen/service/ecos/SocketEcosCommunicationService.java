/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.service.ecos;

import ch.bmec.bmecscreen.config.EcosConfig;
import ch.bmec.bmecscreen.service.socket.AbstractSocketCommunicationService;
import ch.bmec.bmecscreen.service.socket.SocketManager;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author Thom
 */
@Component
public class SocketEcosCommunicationService extends AbstractSocketCommunicationService implements EcosCommunicationService {

    private final Logger log = LoggerFactory.getLogger(SocketEcosCommunicationService.class);

    @Override
    protected SocketManager createSocketManager() {
        return new SocketManager(getEcosConfig().getIp(), getEcosConfig().getTcpPort(), getEcosConfig().getTimeout());
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
            log.error(ex.getMessage());
        }

        return isConnected;
    }

    @Override
    public boolean turnSystemOn() {
        return modifySystemStatus(EcosSystemStatus.ON);
    }

    @Override
    public boolean turnSystemOff() {
        return modifySystemStatus(EcosSystemStatus.OFF);
    }

    @Override
    public EcosSystemStatus requestSystemStatus() {

        EcosSystemStatus systemStatus = null;

        String port = "g";
        String command = "get";

        try {

            String messageToSend = buildMessageToSend(command, port);
            socketManager.writeAndFlush(messageToSend);

            Pattern catchAnswerPattern = Pattern.compile("11 switch\\[(\\d)\\]");

            // read three lines
            String line = socketManager.readLine();
            boolean firstLineCorrect = buildFirstReplyLine(command, port).equals(line);

            line = socketManager.readLine();
            Matcher answerMatcher = catchAnswerPattern.matcher(line);
            boolean secondLineCorrect = answerMatcher.matches();

            line = socketManager.readLine();
            boolean thirdLineCorrect = "<END 0 (OK)>".equals(line);

            if (firstLineCorrect && secondLineCorrect && thirdLineCorrect) {
                int status = Integer.valueOf(answerMatcher.group(1));

                systemStatus = status == 0 ? EcosSystemStatus.OFF : EcosSystemStatus.ON;
            } else {
                log.error("not all received lines are correct (check trace log): {} {} {}", firstLineCorrect, secondLineCorrect, thirdLineCorrect);
            }

            // success = true;
        } catch (Exception anyEx) {
            log.error(anyEx.getMessage(), anyEx);
        }

        return systemStatus;
    }

    private boolean modifySystemStatus(EcosSystemStatus newSystemStatus) {

        boolean success = false;

        String port = newSystemStatus == EcosSystemStatus.ON ? "g" : "r";
        String command = "set";

        try {
            String messageToSend = buildMessageToSend(command, port);
            socketManager.writeAndFlush(messageToSend);

            for (int lines = 0; lines < 2; lines++) {
                // we're not interesed in the contetn, just receive two lines;
                socketManager.readLine();
            }

            success = true;

        } catch (IOException ioex) {
            log.error(ioex.getMessage(), ioex);
        }

        return success;
    }

    private String buildMessageToSend(String command, String port) {
        StringBuilder message = new StringBuilder();

        // set(11, switch[DCC189g])
        message.append(command);
        message.append("(11, switch[");
        message.append(getEcosConfig().getProtocol());
        message.append(getEcosConfig().getAddress());
        message.append(port);
        message.append("])");

        return message.toString();
    }

    private String buildFirstReplyLine(String command, String port) {
        StringBuilder message = new StringBuilder();

        // <REPLY get(11, switch[DCC189g])>
        message.append("<REPLY ");
        message.append(command);
        message.append("(11, switch[");
        message.append(getEcosConfig().getProtocol());
        message.append(getEcosConfig().getAddress());
        message.append(port);
        message.append("])>");

        return message.toString();
    }

    private EcosConfig getEcosConfig() {
        return getConfig().getEcosConfig();
    }
}

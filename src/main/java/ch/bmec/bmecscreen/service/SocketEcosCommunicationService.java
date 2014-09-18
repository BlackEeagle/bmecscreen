/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.service;

import ch.bmec.bmecscreen.config.EcosConfig;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Thom
 */
@Component
public class SocketEcosCommunicationService implements EcosCommunicationService {

    private final Logger log = LoggerFactory.getLogger(SocketEcosCommunicationService.class);
    
    @Autowired
    private ConfigurationService configService;

    @Override
    public boolean checkConnection() {

        boolean isConnected = false;

        try (Socket socket = new Socket()) {
            connect(socket);

            isConnected = socket.isConnected();
        } catch (IOException ex) {
            log.debug(ex.getMessage());
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

        try (Socket socket = new Socket()) {
            connect(socket);

            String port = "g";
            String command = "get";

            try (PrintWriter writer = new PrintWriter(socket.getOutputStream()); BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String messageToSend = buildMessageToSend(command, port);
                log.trace(messageToSend);

                writer.print(messageToSend);
                writer.flush();

                Pattern catchAnswerPattern = Pattern.compile("11 switch\\[(\\d)\\]");

                // read three lines
                String line = reader.readLine();
                log.trace(line);
                boolean firstLineCorrect = buildFirstReplyLine(command, port).equals(line);

                line = reader.readLine();
                log.trace(line);
                Matcher answerMatcher = catchAnswerPattern.matcher(line);
                boolean secondLineCorrect = answerMatcher.matches();

                line = reader.readLine();
                log.trace(line);
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

        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }

        return systemStatus;
    }

    private boolean modifySystemStatus(EcosSystemStatus newSystemStatus) {

        boolean success = false;

        try (Socket socket = new Socket()) {
            connect(socket);

            String port = newSystemStatus == EcosSystemStatus.ON ? "g" : "r";
            String command = "set";

            try (PrintWriter writer = new PrintWriter(socket.getOutputStream()); BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String messageToSend = buildMessageToSend(command, port);
                log.trace(messageToSend);

                writer.print(messageToSend);
                writer.flush();

                for (int lines = 0; lines < 2; lines++) {
                    // we're not interesed in the contetn, just receive two lines;
                    log.trace(reader.readLine());
                }

                success = true;

            } catch (IOException ioex) {
                log.error(ioex.getMessage(), ioex);
            }

        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }

        return success;
    }

    private void connect(final Socket socket) throws IOException {
        socket.connect(new InetSocketAddress(getEcosConfig().getIp(), getEcosConfig().getTcpPort()), getEcosConfig().getTimeout());
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
        return configService.getConfig().getEcosConfig();
    }
}

package ch.bmec.bmecscreen.ui.javafx.controller;

import ch.bmec.bmecscreen.controller.SystemController;
import ch.bmec.bmecscreen.ui.SystemUiController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FxmlController implements Initializable, SystemUiController {

    private final Logger log = LoggerFactory.getLogger(FxmlController.class);

    @Autowired
    private SystemController systemController;

    @FXML
    private Pane loadingPane;

    @FXML
    private ToggleButton systemPowerButton;

    @FXML
    private ToggleButton vncToggleButton;

    @FXML
    private Button rpiRebootButton;

    @FXML
    private Circle ecosStatus;

    @FXML
    private Button ecosRefreshButton;

    @FXML
    private Circle rpiStatus;

    @FXML
    private Button rpiRefreshButton;

    @FXML
    private Circle serverStatus;

    @FXML
    private Label serverIpAddress;

    @FXML
    private Label serverPort;

    @FXML
    private Label serverClientCount;

    @FXML
    private Circle vncStatus;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        loadingPane.setVisible(true);
        Task<Void> initSystem = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                systemController.initializeSystem();

                return null;
            }
        };
        initSystem.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, (WorkerStateEvent event) -> {
            loadingPane.setVisible(false);
        });
        new Thread(initSystem).start();
    }

    @FXML
    public void handleSystemPowerButton(ActionEvent event) {
        if (systemPowerButton.isSelected()) {
            
            loadingPane.setVisible(true);
            Task<Void> startSystem = new Task<Void>() {

                @Override
                protected Void call() throws Exception {
                    systemController.startupSystem();

                    return null;
                }
            };
            startSystem.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, (WorkerStateEvent event2) -> {
                loadingPane.setVisible(false);
            });
            new Thread(startSystem).start();
        }
    }

    @FXML
    public void handleVncToggleButton(ActionEvent event) {

    }

    @FXML
    public void handleRpiRebootButton(ActionEvent event) {

    }

    @FXML
    public void handleEcosRefreshButton(ActionEvent event) {

    }

    @FXML
    public void handleRpiRefreshButton(ActionEvent event) {

    }

    @Override
    public void setSystemStatus(boolean systemUp) {
        Platform.runLater(() -> {
            if (systemUp) {
                systemPowerButton.setText("System ausschalten");
                systemPowerButton.setSelected(true);
            } else {
                systemPowerButton.setText("System einschalten");
                systemPowerButton.setSelected(false);
            }
        });
    }

    @Override
    public void setEcosConnection(boolean hasEcosConnection) {
        Platform.runLater(() -> {
            if (hasEcosConnection) {
                ecosStatus.setFill(Color.GREEN);
            } else {
                ecosStatus.setFill(Color.RED);
            }
            ecosStatus.setVisible(true);
        });
    }

    @Override
    public void setRPiStatus(boolean rPiAlive) {
        Platform.runLater(() -> {
            if (rPiAlive) {
                rpiStatus.setFill(Color.GREEN);
                vncToggleButton.setDisable(false);
                rpiRebootButton.setDisable(false);
            } else {
                rpiStatus.setFill(Color.RED);
                vncToggleButton.setDisable(true);
                rpiRebootButton.setDisable(true);
            }
            rpiStatus.setVisible(true);
        });
    }

    @Override
    public void setVncServerStatus(boolean vncServerStarted) {
        Platform.runLater(() -> {
            if (vncServerStarted) {
                vncStatus.setFill(Color.GREEN);
            } else {
                vncStatus.setFill(Color.RED);
            }
            vncStatus.setVisible(true);
        });
    }

    @Override
    public void setPushbuttonServerStatus(boolean serverStarted, String ipAddress, int port) {
        Platform.runLater(() -> {
            if (serverStarted) {
                serverStatus.setFill(Color.GREEN);
            } else {
                serverStatus.setFill(Color.RED);
            }
            serverStatus.setVisible(true);

            serverIpAddress.setText(ipAddress);
            serverPort.setText(Integer.toString(port));
        });
    }

    @Override
    public void setPushbuttonServerClientCount(int clientCount) {
        Platform.runLater(() -> {
            serverClientCount.setText(Integer.toString(clientCount));
        });
    }
}

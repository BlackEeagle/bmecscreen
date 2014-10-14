package ch.bmec.bmecscreen.ui.javafx.controller;

import ch.bmec.bmecscreen.controller.SystemController;
import ch.bmec.bmecscreen.controller.TvController;
import ch.bmec.bmecscreen.ui.SystemUiController;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Supplier;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
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

    @Autowired
    private TvController tvController;

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

    @FXML
    private Button tvOnButton;

    @FXML
    private Button tvOffButton;

    @FXML
    private Button tvVolumeUpButton;

    @FXML
    private Button tvVolumeDownButton;

    @FXML
    private Button tvPictureDynamicButton;

    @FXML
    private Button tvPictureStandardButton;

    @FXML
    private Button tvPictureMovieButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        loadingPane.setVisible(true);
        executeTask(systemController::initializeSystem, null, loadingPane);
    }

    @FXML
    public void handleSystemPowerButton(ActionEvent event) {
        systemPowerButton.setDisable(true);
        loadingPane.setVisible(true);

        if (systemPowerButton.isSelected()) {
            executeTask(systemController::startupSystem, systemPowerButton, loadingPane);

        } else {
            executeTask(systemController::shutdownSystem, systemPowerButton, loadingPane);
        }
    }

    @FXML
    public void handleVncToggleButton(ActionEvent event) {

        vncToggleButton.setDisable(true);
        loadingPane.setVisible(true);

        if (vncToggleButton.isSelected()) {
            executeTask(systemController::startVncOnRPi, vncToggleButton, loadingPane);
        } else {
            executeTask(systemController::stopVncOnRPi, vncToggleButton, loadingPane);
        }
    }

    @FXML
    public void handleRpiRebootButton(ActionEvent event) {

        executeTaskFromNormalButton(systemController::rebootRPi, rpiRebootButton, loadingPane);
    }

    @FXML
    public void handleEcosRefreshButton(ActionEvent event) {

    }

    @FXML
    public void handleRpiRefreshButton(ActionEvent event) {

    }

    @FXML
    public void handleTvOnButton(ActionEvent event) {

        executeTaskFromNormalButton(tvController::turnOn, tvOnButton, loadingPane);
    }

    @FXML
    public void handleTvOffButton(ActionEvent event) {

        executeTaskFromNormalButton(tvController::turnOff, tvOffButton, loadingPane);
    }

    @FXML
    public void handleTvVolumeUpButton(ActionEvent event) {

        executeTaskFromNormalButton(tvController::volumeUp, tvVolumeUpButton, loadingPane);
    }

    @FXML
    public void handleTvVolumeDownButton(ActionEvent event) {

        executeTaskFromNormalButton(tvController::volumeDown, tvVolumeDownButton, loadingPane);
    }

    @FXML
    public void handleTvPictureDynamicButton(ActionEvent event) {

        executeTaskFromNormalButton(tvController::pictureModeDynamic, tvPictureDynamicButton, loadingPane);
    }

    @FXML
    public void handleTvPictureStandardButton(ActionEvent event) {

        executeTaskFromNormalButton(tvController::pictureModeStandard, tvPictureStandardButton, loadingPane);
    }

    @FXML
    public void handleTvPictureMovieButton(ActionEvent event) {

        executeTaskFromNormalButton(tvController::pictureModeMovie, tvPictureMovieButton, loadingPane);
    }

    @Override
    public void setSystemStatus(boolean systemUp) {
        Platform.runLater(() -> {
            if (systemUp) {
                systemPowerButton.setText("System ausschalten");
                systemPowerButton.setSelected(true);
                vncToggleButton.setDisable(false);
                //rpiRebootButton.setDisable(false);
                tvOnButton.setDisable(false);
                tvOffButton.setDisable(false);
                tvPictureDynamicButton.setDisable(false);
                tvPictureMovieButton.setDisable(false);
                tvPictureStandardButton.setDisable(false);
                tvVolumeDownButton.setDisable(false);
                tvVolumeUpButton.setDisable(false);
            } else {
                systemPowerButton.setText("System einschalten");
                systemPowerButton.setSelected(false);
                vncToggleButton.setDisable(true);
                //rpiRebootButton.setDisable(true);
                tvOnButton.setDisable(true);
                tvOffButton.setDisable(true);
                tvPictureDynamicButton.setDisable(true);
                tvPictureMovieButton.setDisable(true);
                tvPictureStandardButton.setDisable(true);
                tvVolumeDownButton.setDisable(true);
                tvVolumeUpButton.setDisable(true);
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
            } else {
                rpiStatus.setFill(Color.RED);
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

    @Override
    public void setVncClientStatus(boolean vncClientStatus) {
        Platform.runLater(() -> {
            if (vncClientStatus) {
                vncToggleButton.setText("VNC aus");
                vncToggleButton.setSelected(true);
            } else {
                vncToggleButton.setText("VNC ein");
                vncToggleButton.setSelected(false);
            }
        });
    }

    private void executeTaskFromNormalButton(Supplier<Void> method, Button clickedButton, Pane loadingPane) {

        loadingPane.setVisible(true);
        clickedButton.setDisable(true);

        executeTask(method, clickedButton, loadingPane);
    }

    private void executeTask(Supplier<Void> method, ButtonBase clickedButton, Pane loadingPane) {
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                method.get();
                return null;
            }
        };
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, (WorkerStateEvent event) -> {
            if (clickedButton != null) {
                clickedButton.setDisable(false);
            }
            if (loadingPane != null) {
                loadingPane.setVisible(false);
            }
        });
        new Thread(task).start();
    }

}

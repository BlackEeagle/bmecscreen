/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.javafx.controller;

import ch.bmec.bmecscreen.javafx.service.JavaFxServiceHelper;
import ch.bmec.bmecscreen.javafx.service.rpi.RPiActivateVncJavaFxService;
import ch.bmec.bmecscreen.javafx.service.rpi.RPiCheckConnectionIsAliveJavaFxService;
import ch.bmec.bmecscreen.javafx.service.rpi.RPiDeactivateVncJavaFxService;
import ch.bmec.bmecscreen.javafx.service.rpi.RPiPushbuttonLightJavaFxService;
import ch.bmec.bmecscreen.javafx.service.rpi.RPiRebootJavaFxService;
import ch.bmec.bmecscreen.javafx.service.rpi.RPiRebootVncJavaFxService;
import ch.bmec.bmecscreen.javafx.service.rpi.RPiShutdownJavaFxService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * FXML Controller class
 *
 * @author Thom
 */
@Component
public class RPiPaneController implements Initializable, RPiUiOutput {
    
    @Autowired
    private JavaFxServiceHelper serviceHelper;
    
    @Autowired
    private RPiCheckConnectionIsAliveJavaFxService rPiCheckConnectionIsAliveService;
    
    @Autowired
    private RPiActivateVncJavaFxService rPiActivateVncService;
    
    @Autowired
    private RPiDeactivateVncJavaFxService rpiDeactivateVncService;
    
    @Autowired
    private RPiRebootVncJavaFxService rpiRebootVncService;
    
    @Autowired
    private RPiRebootJavaFxService rpiRebootService;
    
    @Autowired
    private RPiShutdownJavaFxService rpiShutdownService;
    
    @Autowired
    private RPiPushbuttonLightJavaFxService rpiPushbuttonLightService;
    
    @FXML
    private Button rPiRefreshButton;
    
    @FXML
    private Circle rPiStatus;
    
    @FXML
    private Button activateVncButton;
    
    @FXML
    private Button deactivateVncButton;
    
    @FXML
    private Button rebootVncButton;
    
    @FXML
    private Button shutdownRPiButton;
    
    @FXML
    private Button rebootRPiButton;
    
    @FXML
    private Label outputLabel;
    
    @FXML
    private Button pushbuttonLightButton;
    
    @FXML
    public void handleRefreshRPiAliveButton(ActionEvent event) {
        checkConnection();
    }
    
    @FXML
    public void handleActivateVncButton(ActionEvent event) {
        serviceHelper.startOrRestart(rPiActivateVncService);
    }
    
    @FXML
    public void handleDeactivateVncButton(ActionEvent event) {
        serviceHelper.startOrRestart(rpiDeactivateVncService);
    }
    
    @FXML
    public void handleRebootVncButton(ActionEvent event) {
        serviceHelper.startOrRestart(rpiRebootVncService);
    }
    
    @FXML
    public void handleShutdownRPiButton(ActionEvent event) {
        serviceHelper.startOrRestart(rpiShutdownService);
    }
    
    @FXML
    public void handleRebootRPiButton(ActionEvent event) {
        serviceHelper.startOrRestart(rpiRebootService);
    }
    
    @FXML
    public void handlePushbuttonLightButton(ActionEvent event) {
        serviceHelper.startOrRestart(rpiPushbuttonLightService);
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rPiCheckConnectionIsAliveService.setOnSucceeded((WorkerStateEvent event) -> {
            boolean connected = (Boolean) event.getSource().getValue();
            
            rPiRefreshButton.setDisable(false);
            
            if (connected) {
                rPiStatus.setFill(Color.GREEN);
                
                activateVncButton.setDisable(false);
                deactivateVncButton.setDisable(false);
                rebootVncButton.setDisable(false);
                rebootRPiButton.setDisable(false);
                shutdownRPiButton.setDisable(false);
                pushbuttonLightButton.setDisable(false);
            } else {
                rPiStatus.setFill(Color.RED);
            }
        });
        
        checkConnection();
    }
    
    @Override
    public void displayOutput(String output) {
        
        if (Platform.isFxApplicationThread() == false) {
            Platform.runLater(() -> {
                outputLabel.setText(output);
            });
        } else {
            outputLabel.setText(output);
        }
    }
    
    private void checkConnection() {
        
        rPiStatus.setVisible(true);
        rPiStatus.setFill(Color.YELLOW);
        
        rPiRefreshButton.setDisable(true);
        
        activateVncButton.setDisable(true);
        deactivateVncButton.setDisable(true);
        rebootVncButton.setDisable(true);
        rebootRPiButton.setDisable(true);
        shutdownRPiButton.setDisable(true);
        pushbuttonLightButton.setDisable(true);
        
        serviceHelper.startOrRestart(rPiCheckConnectionIsAliveService);
    }
}

package ch.bmec.bmecscreen.javafx.controller;

import ch.bmec.bmecscreen.javafx.service.JavaFxServiceHelper;
import ch.bmec.bmecscreen.javafx.service.ecos.EcosCheckConnectionJavaFxService;
import ch.bmec.bmecscreen.javafx.service.ecos.EcosRequestSystemStatusJavaFxService;
import ch.bmec.bmecscreen.javafx.service.ecos.EcosTurnSystemOffJavaFxService;
import ch.bmec.bmecscreen.javafx.service.ecos.EcosTurnSystemOnJavaFxService;
import ch.bmec.bmecscreen.service.EcosSystemStatus;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FxmlController implements Initializable {

    private final Logger log = LoggerFactory.getLogger(FxmlController.class);

    @Autowired
    private JavaFxServiceHelper serviceHelper;

    @Autowired
    private EcosCheckConnectionJavaFxService ecosCheckConnectionService;

    @Autowired
    private EcosTurnSystemOnJavaFxService ecosTurnSystemOnService;

    @Autowired
    private EcosTurnSystemOffJavaFxService ecosTurnSystemOffService;

    @Autowired
    private EcosRequestSystemStatusJavaFxService ecosRequestSystemStatusService;

    @FXML
    private Circle ecosStatus;

    @FXML
    private ToggleButton systemToggleButton;

    @FXML
    public void handleRefreshButtonAction(ActionEvent event) {
        initEcos();
    }

    @FXML
    public void handleToggleSystem(ActionEvent event) {
        systemToggleButton.setDisable(true);

        if (systemToggleButton.isSelected()) {
            serviceHelper.startOrRestart(ecosTurnSystemOnService);
        } else {
            serviceHelper.startOrRestart(ecosTurnSystemOffService);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ecosCheckConnectionService.setOnSucceeded((WorkerStateEvent event) -> {
            boolean connected = (Boolean) event.getSource().getValue();

            if (connected) {
                ecosStatus.setFill(Color.GREEN);
                systemToggleButton.setDisable(false);
            } else {
                ecosStatus.setFill(Color.RED);
            }

            requestSystemStatus();
        });

        ecosTurnSystemOnService.setOnSucceeded((WorkerStateEvent event) -> {
            systemToggle(event, true);
        });

        ecosTurnSystemOffService.setOnSucceeded((WorkerStateEvent event) -> {
            systemToggle(event, false);
        });

        ecosRequestSystemStatusService.setOnSucceeded((WorkerStateEvent event) -> {
            EcosSystemStatus systemStatus = (EcosSystemStatus) event.getSource().getValue();

            if (systemStatus != null) {
                systemToggleButton.setDisable(false);

                if (systemStatus == EcosSystemStatus.ON) {
                    systemToggleButton.setText("System aus");
                    systemToggleButton.setSelected(true);
                } else {
                    systemToggleButton.setText("System ein");
                    systemToggleButton.setSelected(false);
                }

            } else {
                log.error("systemStatus is null: there must be a problem with the communication. see log files for more information");
            }
        });

        initEcos();
    }

    private void systemToggle(WorkerStateEvent event, boolean systemOn) {
        boolean success = (Boolean) event.getSource().getValue();

        if (success) {
            systemToggleButton.setDisable(false);
            if (systemOn) {
                systemToggleButton.setText("System aus");
            } else {
                systemToggleButton.setText("System ein");
            }
            systemToggleButton.setTextFill(Color.BLACK);
        } else {
            if (systemOn) {
                systemToggleButton.setSelected(false);
            } else {
                systemToggleButton.setSelected(false);
            }
            systemToggleButton.setTextFill(Color.RED);
        }
    }

    private void initEcos() {

        checkConnection();
    }

    private void checkConnection() {
        ecosStatus.setVisible(true);
        ecosStatus.setFill(Color.YELLOW);
        systemToggleButton.setDisable(true);

        serviceHelper.startOrRestart(ecosCheckConnectionService);
    }

    private void requestSystemStatus() {

        systemToggleButton.setDisable(true);

        serviceHelper.startOrRestart(ecosRequestSystemStatusService);
    }
}

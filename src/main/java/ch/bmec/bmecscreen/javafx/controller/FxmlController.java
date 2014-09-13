package ch.bmec.bmecscreen.javafx.controller;

import ch.bmec.bmecscreen.javafx.service.EcosJavaFxService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FxmlController implements Initializable {

    @Autowired
    private EcosJavaFxService ecosService;

    @FXML
    private Circle ecosStatus;
    
    @FXML
    private void handleRefreshButtonAction(ActionEvent event) {
        initEcos();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initEcos();
    }

    private void initEcos() {
        ecosService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                boolean connected = (Boolean) event.getSource().getValue();

                if (connected) {
                    ecosStatus.setFill(Color.GREEN);
                } else {
                    ecosStatus.setFill(Color.RED);
                }
            }
        });

        ecosStatus.setVisible(true);
        ecosStatus.setFill(Color.YELLOW);

        if (ecosService.getState() == Worker.State.READY) {
            ecosService.start();
        } else {
            ecosService.restart();
        }
    }
}

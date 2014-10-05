/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.javafx.service;

import javafx.concurrent.Service;
import javafx.concurrent.Worker;
import org.springframework.stereotype.Component;

/**
 *
 * @author Thom
 */
@Component
public class JavaFxServiceHelper {

    public void startOrRestart(Service<?> service) {
        if (service.getState() == Worker.State.READY) {
            service.start();
        } else {
            service.restart();
        }
    }

}

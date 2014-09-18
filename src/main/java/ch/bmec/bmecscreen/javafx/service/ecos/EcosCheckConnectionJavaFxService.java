/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.javafx.service.ecos;

import ch.bmec.bmecscreen.service.EcosCommunicationService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Thom
 */
@Component
public class EcosCheckConnectionJavaFxService extends Service<Boolean> {

    @Autowired
    private EcosCommunicationService ecosService;

    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {

            @Override
            protected Boolean call() throws Exception {
                return ecosService.checkConnection();
            }
        };
    }

}

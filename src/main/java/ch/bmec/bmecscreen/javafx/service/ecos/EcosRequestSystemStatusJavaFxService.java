/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.javafx.service.ecos;

import ch.bmec.bmecscreen.service.EcosCommunicationService;
import ch.bmec.bmecscreen.service.EcosSystemStatus;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Thom
 */
@Component
public class EcosRequestSystemStatusJavaFxService extends Service<EcosSystemStatus> {

    @Autowired
    private EcosCommunicationService ecosService;

    @Override
    protected Task<EcosSystemStatus> createTask() {
        return new Task<EcosSystemStatus>() {

            @Override
            protected EcosSystemStatus call() throws Exception {
                return ecosService.requestSystemStatus();
            }
        };
    }

}

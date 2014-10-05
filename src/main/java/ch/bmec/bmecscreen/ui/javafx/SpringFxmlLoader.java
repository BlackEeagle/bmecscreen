/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.ui.javafx;

import java.io.IOException;
import java.io.InputStream;
import javafx.fxml.FXMLLoader;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author Thom
 */
public class SpringFxmlLoader {

    private final Logger log = LoggerFactory.getLogger(SpringFxmlLoader.class);
    
    private final ApplicationContext context;

    public SpringFxmlLoader(ApplicationContext context) {
        this.context = context;
    }

    public Object load(String url) {
        
        try (InputStream fxmlStream = SpringFxmlLoader.class
                .getResourceAsStream(url)) {
            
            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(new Callback<Class<?>, Object>() {
                @Override
                public Object call(Class<?> clazz) {
                    return context.getBean(clazz);
                }
            });
            
            return loader.load(fxmlStream);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }
}

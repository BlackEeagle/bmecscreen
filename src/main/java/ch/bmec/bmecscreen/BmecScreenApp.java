package ch.bmec.bmecscreen;

import ch.bmec.bmecscreen.javafx.SpringFxmlLoader;
import ch.bmec.bmecscreen.config.SpringConfiguration;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BmecScreenApp extends Application {

    private AnnotationConfigApplicationContext context;

    @Override
    public void start(Stage stage) throws Exception {

        Platform.setImplicitExit(true);

        context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        SpringFxmlLoader loader = new SpringFxmlLoader(context);

        Parent root = (Parent) loader.load("/fxml/Scene.fxml");

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");

        stage.setTitle("JavaFX and Maven");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        context.close();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

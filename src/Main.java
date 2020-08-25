import csc2b.client.FileClient;
import csc2b.gui.FilePane;
import csc2b.server.FileServer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        //Present scene to stage
        primaryStage.setTitle("Peachy-Airlines");
        FilePane layout = new FilePane();
        Scene scene = new Scene(layout, 750, 500);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }


    public static void main(String[] args) {

        launch(args);

    }
}

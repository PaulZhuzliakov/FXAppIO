import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        primaryStage.setTitle("Java File Manager");
        primaryStage.setScene(new Scene(root, 1280, 600));
        primaryStage.show();
        //00
        //1
        //2

        //0
        //1
        //2
        //3
        //4
    }

    public static void main(String[] args) {
        launch();
    }
    //100
}

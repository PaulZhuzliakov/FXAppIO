import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
//1
//2
//3
//4
<<<<<<< 4be5f9fa0e0a8e8e841f93f18723885df5f7dee5
//5
//01
=======
//54
>>>>>>> 0

//1
//2_bad commit

//1
//2_bad commit
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        primaryStage.setTitle("Java File Manager");
        primaryStage.setScene(new Scene(root, 1280, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

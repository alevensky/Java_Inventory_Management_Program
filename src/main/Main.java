package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class Main extends Application {
    @Override
    public void start(Stage openingStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/OpeningScreen.fxml"));
        openingStage.setTitle("");
        openingStage.setScene(new Scene(root));
        openingStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

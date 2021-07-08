package ir.ac.kntu;

import ir.ac.kntu.menu.MainMenu;
import ir.ac.kntu.model.GlobalConstants;
import ir.ac.kntu.services.PlayersFileIO;
import ir.ac.kntu.services.PlayersService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

public class JavaFxApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        StackPane root = new StackPane();
        root.setStyle("-fx-border-width: 0 0 5 0;");
        Scene scene = new Scene(root, GlobalConstants.SCENE_WIDTH, GlobalConstants.SCENE_HEIGHT);

        PlayersService playersService = new PlayersService(new PlayersFileIO("files/Players"));
        MainMenu mainMenu = new MainMenu(root, playersService);
        mainMenu.show();

        // Setting stage properties
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("DigDig");
        stage.setScene(scene);
        stage.show();
    }
}

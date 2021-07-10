package ir.ac.kntu;

import ir.ac.kntu.menus.MainMenu;
import ir.ac.kntu.models.GlobalConstants;
import ir.ac.kntu.services.GameSaveInstanceService;
import ir.ac.kntu.services.ListFileIO;
import ir.ac.kntu.services.PlayersService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class JavaFxApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        StackPane root = new StackPane();
        root.setStyle("-fx-border-width: 0 0 5 0;");
        Scene scene = new Scene(root, GlobalConstants.SCENE_WIDTH, GlobalConstants.SCENE_HEIGHT);

        PlayersService playersService = new PlayersService(new ListFileIO<>("files/Players"));
        GameSaveInstanceService saveInstanceService = new GameSaveInstanceService(new ListFileIO<>("files/GameSaves"));
        MainMenu mainMenu = new MainMenu(root, playersService, saveInstanceService);
        mainMenu.show();

        // Setting stage properties
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("DigDig");
        stage.setScene(scene);
        stage.show();
    }
}

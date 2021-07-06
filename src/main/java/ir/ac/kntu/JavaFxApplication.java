package ir.ac.kntu;

import ir.ac.kntu.model.Map;
import ir.ac.kntu.model.Player;
import ir.ac.kntu.utils.MapLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.File;

public class JavaFxApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        Pane root = new Pane();
        root.setStyle("-fx-border-width: 0 0 5 0;");
//        Scene scene = new Scene(root, 1000, 700, Color.rgb(240, 240, 240));
        Scene scene = new Scene(root, 1000, 700, Color.BLACK);
        scene.getStylesheets().add(new File("src/main/java/ir/ac/kntu/style/SceneStyle.css").toURI().toString());
        HBox hBox = new HBox();
        root.getChildren().add(hBox);
        Player player = new Player("Erfan");
        Game game = new Game(hBox,player);
        game.start();

        // Setting stage properties
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("DigDig");
        stage.setScene(scene);
        stage.show();
    }
}

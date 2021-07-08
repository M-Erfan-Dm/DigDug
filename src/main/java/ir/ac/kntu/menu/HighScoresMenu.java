package ir.ac.kntu.menu;

import ir.ac.kntu.model.Player;
import ir.ac.kntu.services.PlayersService;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class HighScoresMenu {

    private final PlayersService playersService;

    private Pane root;

    public HighScoresMenu(PlayersService playersService) {
        this.playersService = playersService;
    }

    public void show(){
        Stage stage = new Stage();
        root = new Pane();
        Scene scene = new Scene(root,500,500);
        initNodes();

        stage.setTitle("High Scores");
        stage.setScene(scene);
        stage.show();
    }

    private void initNodes(){
        ListView<HBox> listView = new ListView<>();
        for (Player player : playersService.getPlayers()){
            HBox hBox = new HBox();
            hBox.setSpacing(50);
            Label usernameLabel = new Label("Player : " + player.getUsername());
            Label highScoreLabel = new Label("High Score : " + player.getHighScore());
            hBox.getChildren().addAll(usernameLabel,highScoreLabel);
            listView.getItems().add(hBox);
        }
        listView.setPrefWidth(root.getWidth());
        listView.setPrefHeight(root.getHeight());
        root.getChildren().add(listView);
    }
}

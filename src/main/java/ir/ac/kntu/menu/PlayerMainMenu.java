package ir.ac.kntu.menu;

import ir.ac.kntu.Game;
import ir.ac.kntu.model.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.File;

public class PlayerMainMenu {
    private final Player player;

    private final StackPane root;

    public PlayerMainMenu(Player player, Scene scene) {
        this.player = player;
        this.root = new StackPane();
        scene.setRoot(this.root);
        this.root.setPadding(new Insets(50,50,50,50));
        this.root.setBackground(new Background(new BackgroundFill(Color.rgb(240,193,4),
                CornerRadii.EMPTY,Insets.EMPTY)));
    }

    public void show(){
        initNodes();
    }

    private void initNodes(){
        initWelcomeLabel();
        initUserDataLabels();
        initStartGameButton();
    }

    private void initWelcomeLabel(){
        Label welcomeLabel = new Label("Welcome " + player.getUsername());
        welcomeLabel.setStyle("-fx-font-weight: bold;-fx-text-fill: #FFFFFF;-fx-font-size: 30px");
        root.getChildren().add(welcomeLabel);
        StackPane.setAlignment(welcomeLabel, Pos.TOP_CENTER);
    }

    private void initUserDataLabels(){
        VBox vBox = new VBox();
        vBox.setSpacing(30);
        Label totalGamesCountLabel = new Label("Total Games Count : " + player.getTotalGamesCount());
        Label highScoreLabel = new Label("High Score : " + player.getHighScore());
        totalGamesCountLabel.setStyle("-fx-text-fill: #FFFFFF;-fx-font-size: 25px");
        highScoreLabel.setStyle("-fx-text-fill: #FFFFFF;-fx-font-size: 25px");
        vBox.getChildren().addAll(totalGamesCountLabel, highScoreLabel);
        vBox.setAlignment(Pos.CENTER);
        root.getChildren().add(vBox);
    }

    private void initStartGameButton(){
        Button startGameButton = new Button("Start Game");
        startGameButton.getStylesheets().add(new File(
                "src/main/java/ir/ac/kntu/style/Button.css").toURI().toString());
        startGameButton.setPrefWidth(300);
        startGameButton.setOnMouseClicked(mouseEvent -> startGame());
        StackPane.setAlignment(startGameButton,Pos.BOTTOM_CENTER);
        root.getChildren().add(startGameButton);
    }

    private void startGame(){
        root.getChildren().clear();
        Game game = new Game(root.getScene(),player);
        game.start();
    }
}

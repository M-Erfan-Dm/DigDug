package ir.ac.kntu.menu;

import ir.ac.kntu.model.Player;
import ir.ac.kntu.services.GameSaveInstance;
import ir.ac.kntu.services.GameSaveInstanceService;
import ir.ac.kntu.services.PlayersService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.File;

public class PlayerMainMenu {
    private final Player player;

    private final StackPane root;

    private final PlayersService playersService;

    private final GameSaveInstanceService saveInstanceService;

    public PlayerMainMenu(Player player, StackPane root, PlayersService playersService, GameSaveInstanceService saveInstanceService) {
        this.player = player;
        this.playersService = playersService;
        this.saveInstanceService = saveInstanceService;
        this.root = root;
        this.root.setPadding(new Insets(50, 50, 50, 50));
        setBackground();
    }

    public void show() {
        initNodes();
    }

    private void initNodes() {
        initWelcomeLabel();
        initUserDataLabels();
        initGameButtons();
    }

    private void initWelcomeLabel() {
        Label welcomeLabel = new Label("Welcome " + player.getUsername());
        welcomeLabel.setStyle("-fx-font-weight: bold;-fx-text-fill: #FFFFFF;-fx-font-size: 30px");
        root.getChildren().add(welcomeLabel);
        StackPane.setAlignment(welcomeLabel, Pos.TOP_CENTER);
    }

    private void initUserDataLabels() {
        VBox vBox = new VBox();
        vBox.setSpacing(30);
        Label totalGamesCountLabel = new Label("Total Games Count : " + player.getTotalGamesCount());
        Label highScoreLabel = new Label("High Score : " + player.getHighScore());
        totalGamesCountLabel.setStyle("-fx-text-fill: #FFFFFF;-fx-font-size: 25px;");
        highScoreLabel.setStyle("-fx-text-fill: #FFFFFF;-fx-font-size: 25px;");
        vBox.getChildren().addAll(totalGamesCountLabel, highScoreLabel);
        vBox.setAlignment(Pos.CENTER);
        root.getChildren().add(vBox);
    }

    private void initGameButtons() {
        HBox hBox = new HBox();
        Button newGameButton = new Button("New Game");
        Button continueGameButton = new Button("Continue Game");
        setupButtonStyles(newGameButton);
        setupButtonStyles(continueGameButton);
        newGameButton.setOnMouseClicked(mouseEvent -> startNewGame());
        continueGameButton.setOnMouseClicked(mouseEvent -> continueGame());
        hBox.getChildren().addAll(continueGameButton, newGameButton);
        hBox.setSpacing(50);
        hBox.setAlignment(Pos.BOTTOM_CENTER);
        root.getChildren().add(hBox);
        if (!saveInstanceService.containsByPlayer(player)) {
            continueGameButton.setDisable(true);
        }
    }

    private void setupButtonStyles(Button button) {
        button.getStylesheets().add(new File(
                "src/main/java/ir/ac/kntu/style/Button.css").toURI().toString());
        button.setPrefWidth(300);
    }

    private void prepareStartingGame() {
        root.getChildren().clear();
        incrementPlayedGamesCount();
    }

    private void startNewGame() {
        prepareStartingGame();
        saveInstanceService.remove(player);
        root.getChildren().clear();
        HBox hBox = new HBox();
        root.getScene().setRoot(hBox);
        GameMenu gameMenu = new GameMenu(hBox, player, playersService, saveInstanceService);
        gameMenu.start();
    }

    private void continueGame() {
        prepareStartingGame();
        GameSaveInstance gameSaveInstance = saveInstanceService.getInstanceByPlayer(player);
        root.getChildren().clear();
        HBox hBox = new HBox();
        root.getScene().setRoot(hBox);
        GameMenu gameMenu = new GameMenu(hBox, gameSaveInstance, playersService, saveInstanceService);
        gameMenu.start();
    }

    private void incrementPlayedGamesCount() {
        player.incrementTotalGamesCount();
        playersService.add(player);
    }

    private void setBackground() {
        root.setBackground(new Background(new BackgroundFill(Color.rgb(240, 193, 4),
                CornerRadii.EMPTY, Insets.EMPTY)));
    }
}

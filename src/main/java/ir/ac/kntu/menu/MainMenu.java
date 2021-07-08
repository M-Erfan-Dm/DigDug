package ir.ac.kntu.menu;

import ir.ac.kntu.model.GlobalConstants;
import ir.ac.kntu.services.PlayersService;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.File;

public class MainMenu {
    private Pane root;

    private PlayersService playersService;

    private Button loginButton;

    private Button signupButton;

    private Button highScoresButton;

    private Button exitButton;

    private boolean isShowing = false;

    public MainMenu(Pane root, PlayersService playersService) {
        this.root = root;
        this.playersService = playersService;
        setBackground();
        initButtons();
    }

    private void setBackground() {
        String backgroundPath = new File("src/main/resources/assets/background.jpg")
                .toURI().toString();
        Image image = new Image(backgroundPath, GlobalConstants.SCENE_WIDTH, GlobalConstants.SCENE_HEIGHT,
                false, false);
        ImageView imageView = new ImageView(image);
        root.getChildren().add(imageView);
    }

    public void show() {
        if (!isShowing){
            VBox vBox = new VBox();
            vBox.getChildren().addAll(loginButton,signupButton, highScoresButton, exitButton);
            vBox.setAlignment(Pos.CENTER);
            vBox.setSpacing(60);
            root.getChildren().add(vBox);
            isShowing = true;
        }
    }

    private void initButtons() {
        initLoginButton();
        initSignupButton();
        initHighScoreButton();
        initExitButton();
    }

    private void initLoginButton() {
        loginButton = new Button("Login");
        setButtonStyle(loginButton);
        loginButton.setOnMouseClicked(mouseEvent -> {
            LoginMenu loginMenu = new LoginMenu(playersService);
            loginMenu.setOnPlayerLoginListener(player -> {
                root.getChildren().clear();
                PlayerMainMenu playerMainMenu = new PlayerMainMenu(player,root.getScene());
                playerMainMenu.show();
            });
            loginMenu.show();
        });
    }

    private void initSignupButton(){
        signupButton = new Button("Sign Up");
        setButtonStyle(signupButton);
        signupButton.setOnMouseClicked(mouseEvent -> {
            SignupMenu signupMenu = new SignupMenu(playersService);
            signupMenu.show();
        });
    }

    private void initHighScoreButton() {
        highScoresButton = new Button("High Scores");
        setButtonStyle(highScoresButton);
        highScoresButton.setOnMouseClicked(mouseEvent -> {
            HighScoresMenu highScoresMenu = new HighScoresMenu(playersService);
            highScoresMenu.show();
        });
    }

    private void initExitButton() {
        exitButton = new Button("Exit");
        exitButton.setOnMouseClicked(mouseEvent -> Platform.exit());
        setButtonStyle(exitButton);
    }

    private void setButtonStyle(Button button) {
        button.setPrefWidth(300);
        button.getStylesheets().add(new File(
                "src/main/java/ir/ac/kntu/style/Button.css").toURI().toString());
    }
}

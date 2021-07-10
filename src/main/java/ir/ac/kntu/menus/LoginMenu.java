package ir.ac.kntu.menus;

import ir.ac.kntu.models.Player;
import ir.ac.kntu.services.PlayersService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class LoginMenu {
    private Stage stage;

    private StackPane root;

    private TextField usernameTextField;

    private TextField passwordTextField;

    private Label messageLabel;

    private final PlayersService playersService;

    private OnPlayerLoginListener onPlayerLoginListener;

    public LoginMenu(PlayersService playersService) {
        this.playersService = playersService;
    }

    public void setOnPlayerLoginListener(OnPlayerLoginListener onPlayerLoginListener) {
        this.onPlayerLoginListener = onPlayerLoginListener;
    }

    public void show() {
        stage = new Stage();
        root = new StackPane();
        root.setPadding(new Insets(50, 50, 50, 50));
        Scene scene = new Scene(root, 500, 500);

        initNodes();
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    private void initNodes() {
        initFields();
        initLoginButton();
    }

    private void initFields() {
        Label usernameLabel = new Label("Username : ");
        Label passwordLabel = new Label("Password : ");
        usernameTextField = new TextField();
        passwordTextField = new TextField();
        messageLabel = new Label();
        VBox vBox = new VBox();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.addRow(0, usernameLabel, usernameTextField);
        gridPane.addRow(1, passwordLabel, passwordTextField);
        vBox.getChildren().addAll(gridPane, messageLabel);
        vBox.setSpacing(40);
        vBox.setAlignment(Pos.CENTER);
        root.getChildren().add(vBox);
    }

    private void initLoginButton() {
        Button loginButton = new Button("Login");
        loginButton.getStylesheets().add(new File(
                "src/main/java/ir/ac/kntu/style/Button.css").toURI().toString());
        loginButton.setPrefWidth(300);
        loginButton.setOnMouseClicked(mouseEvent -> login());
        root.getChildren().add(loginButton);
        StackPane.setAlignment(loginButton, Pos.BOTTOM_CENTER);
    }

    private void login() {
        String username = usernameTextField.getText().trim();
        String password = passwordTextField.getText().trim();
        if (username.isBlank() || password.isBlank()) {
            messageLabel.setText("Invalid data");
            return;
        }
        boolean isPlayerAuthorized = playersService.isPlayerAuthorized(username, password);
        if (!isPlayerAuthorized) {
            messageLabel.setText("Player not found");
            return;
        }
        Player player = playersService.getPlayer(username);
        if (onPlayerLoginListener != null) {
            onPlayerLoginListener.onLogin(player);
        }
        stage.close();
    }
}

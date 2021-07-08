package ir.ac.kntu.menu;

import ir.ac.kntu.model.Player;
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

public class SignupMenu {
    private final PlayersService playersService;

    private Stage stage;

    private StackPane root;

    private Label usernameLabel;

    private Label passwordLabel;

    private TextField usernameTextField;

    private TextField passwordTextField;

    private Button signupButton;

    private Button cancelButton;

    private Label messageLabel;

    public SignupMenu(PlayersService playersService) {
        this.playersService = playersService;
    }

    public void show() {
        stage = new Stage();
        root = new StackPane();
        root.setPadding(new Insets(50, 50, 50, 50));
        Scene scene = new Scene(root, 500, 500);
        initNodes();

        stage.setScene(scene);
        stage.setTitle("Sign Up");
        stage.show();
    }

    private void initNodes() {
        initSignupButton();
        initCancelButton();
        initFields();
        root.getChildren().addAll(signupButton, cancelButton);
    }

    private void initSignupButton() {
        signupButton = new Button("Sign Up");
        setButtonStyle(signupButton);
        StackPane.setAlignment(signupButton, Pos.BOTTOM_RIGHT);
        signupButton.setOnMouseClicked(mouseEvent -> signup());
    }

    private void initCancelButton() {
        cancelButton = new Button("Cancel");
        setButtonStyle(cancelButton);
        StackPane.setAlignment(cancelButton, Pos.BOTTOM_LEFT);
        cancelButton.setOnMouseClicked(mouseEvent -> stage.close());
    }

    private void initFields() {
        usernameLabel = new Label("Username : ");
        passwordLabel = new Label("Password : ");
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
        StackPane.setAlignment(vBox, Pos.CENTER);
    }


    private void setButtonStyle(Button button) {
        button.getStylesheets().add(new File(
                "src/main/java/ir/ac/kntu/style/Button.css").toURI().toString());
        button.setPrefWidth(100);
    }

    private void signup() {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        if (username.isBlank() || password.isBlank()) {
            messageLabel.setText("Invalid data");
            return;
        }
        boolean playerExists = playersService.contains(username);
        if (playerExists) {
            messageLabel.setText("Username already exists");
            return;
        }
        Player player = new Player(username, password);
        playersService.add(player);
        messageLabel.setText("User is registered :)");
    }

}

package ir.ac.kntu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.File;

public class GameInfoSideLayout {

    private VBox root;

    private Label highScoreLabel;

    private Label scoreLabel;

    private Label healthLabel;

    private Label levelLabel;

    public GameInfoSideLayout(VBox root) {
        this.root = root;
        root.setPadding(new Insets(25,0,0,10));
        root.setSpacing(30);
        initNodes();
    }

    public VBox getRoot() {
        return root;
    }

    public void show(int highScore, int score, int health, int level){
        updateHighScore(highScore);
        updateScore(score);
        updateHealth(health);
        updateLevel(level);
    }

    private void initNodes(){
        initHighScoreLabel();
        initScoreLabel();
        initHealthLayout();
        initLevelLabel();
    }

    public void updateHighScore(int highScore){
        highScoreLabel.setText("High Score : " + highScore);
    }

    public void updateScore(int score){
        scoreLabel.setText("Score : " + score);
    }

    public void updateHealth(int health){
        healthLabel.setText("Health : " + health);
    }

    public void updateLevel(int level){
        levelLabel.setText("Level : " + level);
    }

    private void initHighScoreLabel(){
        highScoreLabel = new Label();
        highScoreLabel.setTextFill(Color.WHITE);
        root.getChildren().add(highScoreLabel);
    }

    private void initScoreLabel(){
        scoreLabel = new Label();
        scoreLabel.setTextFill(Color.WHITE);
        root.getChildren().add(scoreLabel);
    }

    private void initHealthLayout(){
        HBox healthLayout = new HBox();
        healthLayout.setSpacing(10);
        healthLabel = new Label();
        healthLabel.setTextFill(Color.WHITE);
        String imagePath = new File("src/main/resources/assets/digger_simple1.png").toURI().toString();
        Image image = new Image(imagePath,
                30,30,false,false);
        ImageView imageView = new ImageView(image);
        healthLayout.getChildren().addAll(healthLabel,imageView);
        root.getChildren().add(healthLayout);
    }

    private void initLevelLabel(){
        levelLabel = new Label();
        levelLabel.setTextFill(Color.WHITE);
        root.getChildren().add(levelLabel);
    }
}

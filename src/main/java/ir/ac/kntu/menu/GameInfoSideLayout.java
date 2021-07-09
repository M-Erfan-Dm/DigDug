package ir.ac.kntu.menu;

import ir.ac.kntu.model.GlobalConstants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.File;
import java.text.DecimalFormat;

public class GameInfoSideLayout {

    private VBox root;

    private Label highScoreLabel;

    private Label scoreLabel;

    private Label healthLabel;

    private Label levelLabel;

    private Label timerLabel;

    private Label messageLabel;

    private final DecimalFormat decimalFormat;

    public GameInfoSideLayout(VBox root) {
        this.root = root;
        decimalFormat = new DecimalFormat("00");
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
        initTimerLabel();
        initMessageLabel();
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

    public void updateTimer(int minute,int second){
        timerLabel.setText("Time : " + decimalFormat.format(minute) + " : " + decimalFormat.format(second));
    }

    public void changeTimerToNormalState(){
        timerLabel.setTextFill(Color.WHITE);
    }

    public void changeTimerToWarningState(){
        timerLabel.setTextFill(Color.rgb(190,28,28));
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
        String imagePath = new File("src/main/resources/assets/heart.png").toURI().toString();
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

    private void initTimerLabel(){
        timerLabel = new Label();
        timerLabel.setTextFill(Color.WHITE);
        root.getChildren().add(timerLabel);
    }

    private void initMessageLabel(){
        messageLabel = new Label();
        messageLabel.setAlignment(Pos.CENTER);
        root.getChildren().add(messageLabel);
    }

    public void printGameOver(){
        messageLabel.setText("Game Over");
        messageLabel.setStyle("-fx-text-fill: #be1c1c;-fx-font-size: 25px");
    }

    public void printGameLose(){
        messageLabel.setText("You Lost");
        messageLabel.setStyle("-fx-text-fill: #be1c1c;-fx-font-size: 25px");
    }

    public void printGameWin(){
        messageLabel.setText("You Won");
        messageLabel.setStyle("-fx-text-fill: #43d21e;-fx-font-size: 25px");
    }

    public void clearMessage(){
        messageLabel.setText("");
    }

    public void printInitialDelay(int second){
        messageLabel.setText(decimalFormat.format(second));
        messageLabel.setStyle("-fx-text-fill: #be1c1c;-fx-font-size: 25px");
    }
}

package com.moupali;

import javafx.application.Application;
import javafx.application.Platform;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;



public class Main extends Application {

    public Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        GridPane rootGridPane = loader.load();

        controller = loader.getController();
        controller.createPlayground();

        MenuBar menuBar = creteMenu();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        Pane menuPane = (Pane) rootGridPane.getChildren().get(0);
        menuPane.getChildren().add(menuBar);




        Scene scene = new Scene(rootGridPane);

        primaryStage.setScene(scene);
        primaryStage.setTitle("ConnectFour");
        primaryStage.setResizable(false);
        primaryStage.show();


    }

    private  MenuBar creteMenu(){
        //fileMenu

        Menu fileMenu = new Menu("File");

        MenuItem newGame = new MenuItem("New game");
        newGame.setOnAction(event -> controller.resetGame());

        MenuItem resetGame = new MenuItem("Reset game");
        resetGame.setOnAction(event -> controller.resetGame());

        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        MenuItem exitGame = new MenuItem("Exit game");
        exitGame.setOnAction(event -> exitGame());

        fileMenu.getItems().addAll(newGame,resetGame,separatorMenuItem,exitGame);

        //Help Menu

        Menu helpMenu = new Menu("Help");

        MenuItem aboutGame = new MenuItem("About Connect4");
        aboutGame.setOnAction(event -> aboutConnect4());


        SeparatorMenuItem separator = new SeparatorMenuItem();
        MenuItem aboutMe = new MenuItem("About Me");
        aboutMe.setOnAction(event -> aboutMe());
        
        
        
        helpMenu.getItems().addAll(aboutGame,separator,aboutMe);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu,helpMenu);

        return menuBar;


    }

    private void aboutMe() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About The Developer");
        alert.setHeaderText("Mou Sen");
        alert.setContentText("I love to play around with code and create games. " +
                "Connect 4 is one of them. In free time " +
                "I like to spend time with nears and dears.");

        alert.show();
    }

    private void aboutConnect4() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Connect Four");
        alert.setHeaderText("How to play");
        alert.setContentText("The Connect 4 Game Rules are easy to understand." +
                " In fact, it is in the name. To win Connect Four, all you have to do" +
                " is to choose a color and then take turns dropping colored discs from" +
                " the top into a seven-column, six-row vertically suspended grid." +
                " The pieces fall straight down, occupying the next available space within the column." +
                " The objective of this game is to connect four of your colored checker pieces in a row," +
                " much the same as tic tac toe. This can be done horizontally, vertically or diagonally." +
                " Each player will drop in one checker piece at a time. This will give you a chance" +
                " to either build your row, or stop your opponent from getting four in a row." +
                "\n\n" +
                "\tThe game is over either when you or your friend reaches four in a row," +
                " or when all forty two slots are filled, ending in a stalemate." +
                " If you and your friend decide to play again, the first player typically goes first." +
                " The rules of the game are easy to learn, but difficult to master." +
                " That is the beauty of Connect Four. Now that you know the Connect 4" +
                " board game rules, now is the time to challenge everyone you know." +
                " No matter their age or skill level, they can play this game with you." +
                " Now that you understand the rules, share Connect Four with everyone around you." +
                " You’ll be glad you did.\n\n");


        alert.show();
    }


    private void exitGame() {

        Platform.exit();
        System.exit(0);
    }

    private void resetGame() {


    }

    public static void main(String[] args) {
        launch(args);

    }
}

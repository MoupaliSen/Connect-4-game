package com.moupali;

import com.sun.org.apache.xerces.internal.xinclude.XPointerElementHandler;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.awt.*;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {

    private  static  final int COLUMNS =7;
    private  static  final int ROWS = 6;
    private static final int CIRCLE_DIAMETER = 80;
    private static final  String discolour1 = "#24303E";
    private static final  String discolour2 = "#4CAA88";



    private static  String PLAYER_ONE ="Player One";
    private static String PLAYER_TWO = "Player Two";

    private boolean isPlayerOneTurn = true;

    private Disc[][] insertedDiscArray = new Disc[ROWS][COLUMNS]; // For Structural Changes


    @FXML
    public GridPane rootGridPane;

    @FXML
    public Pane insertDiscPane;

    @FXML
    public TextField player1,player2;

    @FXML
    public Button setNames;

    @FXML
    public Label playerNameLabel;

    private boolean isAllowedToInsert = true;









    public void createPlayground(){

        setNames.setOnAction(event ->
        {
            PLAYER_ONE = player1.getText();
            PLAYER_TWO = player2.getText();
        });
        Shape rectangleWithHoles = createGameStructureGrid();
        rootGridPane.add(rectangleWithHoles,0,1);

        List<Rectangle> rectangleList= createClickableColumns();


        for(Rectangle rectangle: rectangleList){
            rootGridPane.add(rectangle,0,1);
        }




    }
    private  Shape createGameStructureGrid(){
        Shape rectangleWithHoles = new Rectangle((COLUMNS+1) * CIRCLE_DIAMETER, (ROWS+1) * CIRCLE_DIAMETER);
        for(int row = 0; row <ROWS; row++){
            for(int col = 0; col<COLUMNS;col++){
                Circle circle = new Circle();
                circle.setRadius(CIRCLE_DIAMETER/ 2);
                circle.setCenterX(CIRCLE_DIAMETER/2);
                circle.setCenterY(CIRCLE_DIAMETER/2);
                circle.setSmooth(true);

                circle.setTranslateX(col * (CIRCLE_DIAMETER+5) + CIRCLE_DIAMETER/ 4);
                circle.setTranslateY(row * (CIRCLE_DIAMETER+5)+ CIRCLE_DIAMETER/ 4);


                rectangleWithHoles = Shape.subtract(rectangleWithHoles, circle);

            }
        }





        rectangleWithHoles.setFill(Color.WHITE);

        return rectangleWithHoles;

    }

    private List<Rectangle> createClickableColumns(){

        List<Rectangle> rectangleList = new ArrayList<>();

        for(int col = 0; col < COLUMNS; col++){
            Rectangle rectangle = new Rectangle(CIRCLE_DIAMETER,(ROWS+1) * CIRCLE_DIAMETER);
            rectangle.setFill(Color.BLUE);
            rectangle.setTranslateX(col * (CIRCLE_DIAMETER+5) + CIRCLE_DIAMETER/ 4);

            rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.valueOf("#eeeeee26")));
            rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));

            final  int column = col;
            rectangle.setOnMouseClicked(event -> {
                if(isAllowedToInsert){
                    isAllowedToInsert = false;
                    insertDisc(new Disc(isPlayerOneTurn) , column);
                }

            });




            rectangleList.add(rectangle);
        }





        return rectangleList;

    }

    private  void  insertDisc(Disc disc, int column){
        int row = ROWS -1;
        while(row >= 0){
            if(getDiscPresent(row,column)== null)
                break;

            row--;

        }

        if(row<0)
            return;
        insertedDiscArray[row][column] = disc;
        insertDiscPane.getChildren().add(disc);
        disc.setTranslateX(column * (CIRCLE_DIAMETER +5) + CIRCLE_DIAMETER/ 4 );

        int currentRow = row;
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5),disc);
        translateTransition.setToY(row * (CIRCLE_DIAMETER +5) + CIRCLE_DIAMETER/ 4);
        translateTransition.setOnFinished(event -> {

            isAllowedToInsert = true;

            if(gameEnded(currentRow,column)){
                gameOver();
                return;

            }
            isPlayerOneTurn = !isPlayerOneTurn;
            playerNameLabel.setText(isPlayerOneTurn? PLAYER_ONE: PLAYER_TWO);
        });
        translateTransition.play();




    }

    private boolean gameEnded(int row ,int column){


        // vertical Points

        List<Point2D> verticalPoints = IntStream.rangeClosed(row-3,row+3).mapToObj(r-> new Point2D(r,column)).collect(Collectors.toList());

        List<Point2D> horizontalPoints = IntStream.rangeClosed(column - 3, column + 3)
                .mapToObj(col -> new Point2D(row, col))
                .collect(Collectors.toList());
        Point2D startPoint1 = new Point2D(row - 3, column + 3);
        List<Point2D> diagonal1Points = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> startPoint1.add(i, -i))
                .collect(Collectors.toList());

        Point2D startPoint2 = new Point2D(row - 3, column - 3);
        List<Point2D> diagonal2Points = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> startPoint2.add(i, i))
                .collect(Collectors.toList());
        boolean isEnded = checkCombinations(verticalPoints) || checkCombinations(horizontalPoints)
                || checkCombinations(diagonal1Points)|| checkCombinations(diagonal2Points);

        return isEnded;



    }

    private boolean checkCombinations(List<Point2D> points)
    {
        int chain = 0;
        for(Point2D point : points){


            int rowIndexArray = (int) point.getX();
            int columnIndexArray = (int) point.getY();

            Disc disc = getDiscPresent(rowIndexArray,columnIndexArray);


            if(disc != null && disc.isPlayerOneMove == isPlayerOneTurn){
                chain++;
                if(chain == 4){
                    return true;
                }

            }else{
                chain = 0;

            }
        }
        return false;

    }
    private Disc getDiscPresent(int row,int column){
        if(row >= ROWS || row < 0 || column >= COLUMNS || column < 0)
            return null;

        return insertedDiscArray[row][column];

    }
    private void gameOver(){
        String winner = isPlayerOneTurn ? PLAYER_ONE : PLAYER_TWO;
        System.out.println("Winner is : " +winner);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Connect Four");
        alert.setHeaderText("The Winner is " + winner);
        alert.setContentText("Want to Play again?");

        ButtonType yesBtn = new ButtonType("yes");
        ButtonType noBtn = new ButtonType("No , Exit");
        alert.getButtonTypes().setAll(yesBtn,noBtn);


       Platform.runLater(()->{
           Optional<ButtonType>btnClicked = alert.showAndWait();
           if(btnClicked.isPresent() && btnClicked.get() == yesBtn){
               resetGame();

           }
           else{
               Platform.exit();
               System.exit(0);

           }

       });


    }

    public void resetGame() {
        insertDiscPane.getChildren().clear();
        for(int row = 0; row<insertedDiscArray.length; row++){
            for(int col = 0;col<insertedDiscArray[row].length;col++){
                insertedDiscArray[row][col] = null;

            }
        }
        isPlayerOneTurn = true;
        playerNameLabel.setText(PLAYER_ONE);


        createPlayground();
    }

    private static class  Disc extends  Circle{

        private  final  boolean isPlayerOneMove;
        public  Disc(boolean isPlayerOneMove){

            this.isPlayerOneMove = isPlayerOneMove;
            setRadius(CIRCLE_DIAMETER/ 2);
            setFill(isPlayerOneMove? Color.valueOf(discolour1): Color.valueOf(discolour2));
            setCenterX(CIRCLE_DIAMETER/ 2);
            setCenterY(CIRCLE_DIAMETER/ 2);

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

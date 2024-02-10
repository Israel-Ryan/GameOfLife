package org.example.gridpane;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @FXML
    private GridPane gameGrid;
    @FXML
    private Button nextGenButton;


    Cell[][] gameBoard = new Cell[25][25];


    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        fxmlLoader.setController(this); // Connect your class as the controller
        fxmlLoader.load(); // Load the FXML

        nextGenButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gameBoard = nextGeneration(gameBoard);

            }
        });

        gameGrid.setStyle("-fx-background-color: white; -fx-grid-lines-visible: true");

        //  Cell Logic (Correct, no change needed)
        for(int i = 0; i < gameBoard.length; i++) {
            for(int j = 0; j < gameBoard[i].length; j++) {
                gameBoard[i][j] = new Cell();
            }
        }

        // Button Adding  (Moved for correct FXML loading timing)
        for(int i = 0; i < gameBoard.length; i++) {
            for(int j = 0; j < gameBoard[i].length; j++) {

                Button button = new Button();
                button.setOnAction(this::handleButtonClick);
                button.setBackground(Background.fill(Color.WHITE));
                button.setStyle("-fx-min-width: 20px; -fx-min-height: 20px; -fx-pref-width: 20px; -fx-pref-height: 20px; ");

                GridPane.setColumnIndex(button, j);
                GridPane.setRowIndex(button, i);
                gameGrid.getChildren().add(button);
            }
        }


        Scene scene = new Scene(fxmlLoader.getRoot()); // Use the loaded root
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

    private void handleButtonClick(ActionEvent event) {
        // This is where the magic happens when a button is clicked!
        Button clickedButton = (Button) event.getSource(); // Get the Button that was clicked

        int row = GridPane.getRowIndex(clickedButton);
        int col = GridPane.getColumnIndex(clickedButton);
        Cell correspondingCell = gameBoard[row][col];

        if(correspondingCell.isAlive()) {
            clickedButton.setBackground(Background.fill(Color.WHITE));
            correspondingCell.toggleState();
        } else {
            clickedButton.setBackground(Background.fill(Color.BLACK));
            correspondingCell.toggleState();
        }
    }

    public static int countLiveNeighbors(Cell[][] board, int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int neighborRow = row + i;
                int neighborCol = col + j;

                // Check for valid bounds and skip the cell itself
                if (neighborRow >= 0 && neighborRow < board.length &&
                        neighborCol >= 0 && neighborCol < board[0].length &&
                        !(neighborRow == row && neighborCol == col)) {
                    if (board[neighborRow][neighborCol].isAlive()) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public Cell[][] nextGeneration(Cell[][] board) {
        Cell[][] nextBoard = new Cell[board.length][board[0].length];

        // Initialize the nextBoard with dead cells
        for (int i = 0; i < nextBoard.length; i++) {
            for (int j = 0; j < nextBoard[i].length; j++) {
                nextBoard[i][j] = new Cell(); // Initially dead
            }
        }

        // Apply the rules
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Cell currentCell = board[i][j];
                Cell nextCell = nextBoard[i][j];
                int liveNeighbors = countLiveNeighbors(board, i, j);

                // Apply the rules to the cell in the next generation board
                if (board[i][j].isAlive()) {
                    if (liveNeighbors == 2 || liveNeighbors == 3) {
                        nextBoard[i][j].toggleState(); // Make it alive
                    }  // else it dies
                } else { // Cell is dead
                    if (liveNeighbors == 3) {
                        nextBoard[i][j].toggleState(); // Make it alive
                    }
                }

                Button correspondingButton = (Button) gameGrid.getChildren().get(i * board.length + j);
                if (nextCell.isAlive()) {
                    correspondingButton.setBackground(Background.fill(Color.BLACK));
                } else {
                    correspondingButton.setBackground(Background.fill(Color.WHITE));
                }

            }
        }

        return nextBoard;
    }

}
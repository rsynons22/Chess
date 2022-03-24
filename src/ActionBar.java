import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Creates the status bar and buttons at the bottom of the window. Contains logic for importing and exporting files. 
 */
public class ActionBar {
    
    private String[][] fileBoardArray;
    private HBox hBox;

    public ActionBar(Stage primaryStage) {
        hBox = new HBox(5);
        hBox.setPadding(new Insets(5));

        initButtons(hBox, primaryStage);
    }

    // Initializes the buttons
    private void initButtons(HBox hBox, Stage primaryStage) {
        Button quitButton = new Button("Quit");
        Button importBoardButton = new Button("Import Board");
        Button exportMovesButton = new Button("Export Moves");
        Button resetBoardButton = new Button("Reset Board");

        hBox.getChildren().addAll(quitButton, importBoardButton, exportMovesButton, resetBoardButton);

        quitButton.setOnAction(e -> {
            Alert alert = new Alert(AlertType.NONE, "Are you sure you want to quit?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Chess");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                primaryStage.close();
            }

        });

        FileChooser fileChooser = new FileChooser();

        importBoardButton.setOnAction(e -> {

            fileBoardArray = new String[8][8]; 

            // Prefix (W or B) is team color
            // Suffix (P, Kn, R, B, Q, Ki) is piece type
            // EE is empty
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            try {
                
                if(selectedFile != null) { // Would be null if user canceled file explorer
                    Scanner sc = new Scanner(selectedFile);
                    int row = 0;
                    while(sc.hasNextLine()) {
                        fileBoardArray[row] = sc.nextLine().split(", ");
                        row++;
                    }
                    sc.close();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            if(selectedFile != null) {
                Chess.resetBoard();
                Chess.getBoard().removeAllPieceImages();
                Chess.getBoard().modifyBoardArray(formatBoard(Chess.getBoard()));
            }
        });

        exportMovesButton.setOnAction(e -> {

            try {
                File selectedFile = fileChooser.showOpenDialog(primaryStage);
                FileWriter writer;
                writer = new FileWriter(selectedFile.getPath());
            
                for(String str: Chess.getBoard().getMoveList()) {
                    writer.write(str + System.lineSeparator());
                }
                writer.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            Alert alert = new Alert(AlertType.NONE, "Moves exported", ButtonType.OK);
            alert.setTitle("Chess");
            alert.showAndWait();

        });

        resetBoardButton.setOnAction(e -> {
            Chess.resetBoard();
        });
    }

    // Formats the board recieved from the file.
    protected Piece[][] formatBoard(Board board) {
        Piece[][] newBoardArray = new Piece[8][8];

        
        for(int r = 0; r < fileBoardArray.length; r++) {
            for(int c = 0; c < fileBoardArray[r].length; c++) {
                int teamColor = 0;;
                String teamColorImageStr = "";
                if(fileBoardArray[r][c].charAt(0) == 'W') {
                    teamColor = Constants.WHITE;
                    teamColorImageStr = "white";
                } else if(fileBoardArray[r][c].charAt(0) == 'B'){
                    teamColor = Constants.BLACK;
                    teamColorImageStr = "black";
                }

                String pieceType = fileBoardArray[r][c].substring(1);

                switch(pieceType) {
                    case "E":  
                        newBoardArray[r][c] = null; 
                        break;

                    case "P": 
                        newBoardArray[r][c] = new Pawn(teamColor, board, r, c, board.initImage(teamColorImageStr + "Pawn")); 
                        break;

                    case "Kn": 
                        newBoardArray[r][c] = new Knight(teamColor, board, r, c, board.initImage(teamColorImageStr + "Knight")); 
                        break;
                    case "R": 
                        newBoardArray[r][c] = new Rook(teamColor, board, r, c, board.initImage(teamColorImageStr + "Rook")); 
                        break;

                    case "B": 
                        newBoardArray[r][c] = new Bishop(teamColor, board, r, c, board.initImage(teamColorImageStr + "Bishop")); 
                        break;

                    case "Q": 
                        newBoardArray[r][c] = new Queen(teamColor, board, r, c, board.initImage(teamColorImageStr + "Queen")); 
                        break;

                    case "Ki": 
                        newBoardArray[r][c] = new King(teamColor, board, r, c, board.initImage(teamColorImageStr + "King"));
                        break;

                    default: 
                        System.out.println("Error, unknown string format in imported file: " + pieceType);
                }

            }
        }
        return newBoardArray;

    }

    // Gets the horizontal box the buttons are stored in
    public HBox getHBox() {
        return hBox;
    }

}

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Board class that contains all the code for piece turns and check/checkmate logic. 
 */
public class Board {

    private boolean isWhiteTurn;
    private boolean checkMate;
    private int pieceState;
    private ArrayList<RedDot> possibleMoves;
    private Piece pieceClickedOn;
    private Piece[] pieceArray;
    private Piece[][] boardArray;

    private Pane pane;
    private Scene scene;
    private TextField statusMessage;

    private ArrayList<String> moveList;
    
    public Board(BorderPane pane, Scene scene, Stage primaryStage, TextField textField, ActionBar actionBar) {
        setToDefaults(pane, scene, primaryStage, textField);
    }  

    // --------------------------------------------- Main Game Methods ---------------------------------------------------------------------

    /**
     *  Main turn method that handles two states:
     *      CLICK_ON_PIECE: handles where the user clicks, will display red dots if a piece clicked on has possible moves.
     *      RED_DOTS_PLACED: will either move to the red dot clicked on or remove red dots if user clicked on the same piece. 
     * 
     * Moving pieces checks to see if the move will put their King in check and will either allow or disallow the move based on the result. 
     */
    private void turn(int row, int column, int teamColor) {

        switch(pieceState) {
            case Constants.CLICK_ON_PIECE:

                pieceClickedOn = boardArray[row][column];

                if(pieceClickedOn != null) { // If user click on a piece

                    if(pieceClickedOn.getTeamColor() == teamColor) { // If the piece is on the team of whos turn it is

                        possibleMoves = pieceClickedOn.findMoves(); // Show the possible moves
                        setDotsVisible();

                        if(!possibleMoves.isEmpty()) { // Change state if there are possible moves
                            pieceState = Constants.RED_DOTS_PLACED;
                        }

                    }
                }
                break;

            case Constants.RED_DOTS_PLACED:

                Piece king;
                Piece opposingKing;

                if(teamColor == Constants.WHITE) {
                    king = findKing(Constants.WHITE);
                    opposingKing = findKing(Constants.BLACK);
                } else {
                    king = findKing(Constants.BLACK);
                    opposingKing = findKing(Constants.WHITE);
                }
                
                Piece spotClickedOn = boardArray[row][column];

                if(spotClickedOn == null) {// If they click on an empty space
                    
                    for (int i = 0; i < possibleMoves.size(); i++) {

                        if(row == possibleMoves.get(i).getRow() && 
                           column == possibleMoves.get(i).getColumn() && 
                           !checkMate) // User clicked on a spot that is a red dot (possible move)
                        {
                            pieceClickedOn.phasePiece(row, column);

                            if(isInCheck(king, false)) { // Move would put king or have king remain in check, therefore the move is canceled
                                pieceClickedOn.unPhasePiece();
                                removeRedDots();
                                possibleMoves.clear();
                                pieceState = Constants.CLICK_ON_PIECE;
                            } else { // Move doesn't put king in check, therefore the move is executed
                                pieceClickedOn.unPhasePiece();
                                pieceClickedOn.move(row, column);
                                removeRedDots();
                                possibleMoves.clear();
                                repaint();
                                pieceState = Constants.CLICK_ON_PIECE;


                                if(teamColor == Constants.WHITE) {
                                    isWhiteTurn = false;
                                    statusMessage.setText("Black Turn");
                                } else {
                                    isWhiteTurn = true;
                                    statusMessage.setText("White Turn");
                                }
                                
                                if(isInCheck(opposingKing, true)) { // Checks to see if the opposing king is now in check due to the move
                                    isCheckMate(opposingKing);
                                };
                            }
                        }
                    }
                } else if(spotClickedOn == pieceClickedOn) { // If the user clicks on the same piece remove the red dots and return to the CLICK_ON_PIECE state
                    removeRedDots();
                    possibleMoves.clear();
                    pieceState = Constants.CLICK_ON_PIECE;
                } else if(spotClickedOn.getTeamColor() != teamColor) { // If the user clicks on an enemy piece

                        for (int i = 0; i < possibleMoves.size(); i++) {

                            if(row == possibleMoves.get(i).getRow() && 
                               column == possibleMoves.get(i).getColumn() &&
                               !checkMate) {

                                pieceClickedOn.phasePiece(row, column);
                                

                                if(isInCheck(king, true)) {
                                    pieceClickedOn.unPhasePiece();
                                    removeRedDots();
                                    possibleMoves.clear();
                                    pieceState = Constants.CLICK_ON_PIECE;
                                } else {
                                    pieceClickedOn.unPhasePiece();
                                    spotClickedOn.removePiece();
                                    pieceClickedOn.move(row, column);
                                    removeRedDots();
                                    possibleMoves.clear();
                                    repaint();
                                    pieceState = Constants.CLICK_ON_PIECE;

                                    if(teamColor == Constants.WHITE) {
                                        isWhiteTurn = false;
                                        statusMessage.setText("Black Turn");
                                    } else {
                                        isWhiteTurn = true;
                                        statusMessage.setText("White Turn");
                                    }

                                    if(isInCheck(opposingKing, true)) {
                                        isCheckMate(opposingKing);
                                    };
                                    break;
                                }
                            }
                        }
                    }
                    break;
                }  
        }

    // Sees if the king passed in is in check or not.
    private boolean isInCheck(Piece piece, boolean updateMessage) {

        for(int i = 0; i < 32; i++) { // loop through pieceArray

            if(!pieceArray[i].isCaptured()) { //&& (!pieceArray[i].isPhased())) { // check if the piece is captured or is phased
                ArrayList<RedDot> moves = pieceArray[i].findMoves(); // set the array to the possible moves of the piece

                for(int j = 0; j < moves.size(); j++) { // loop through the array of the possible moves

                    if(moves.get(j).getColumn() == piece.getCurrentColumn() && // if an imaginary red dot is on the opposite king
                      (moves.get(j).getRow() == piece.getCurrentRow()) &&
                       pieceArray[i].getTeamColor() != (piece.getTeamColor()))
                    {

                        if(updateMessage) {
                            if(piece.getTeamColor() == Constants.WHITE) {
                                statusMessage.setText("White Turn - Check");
                            } else {
                                statusMessage.setText("Black Turn - Check");
                            }
                        }

                        return true;
                    }
                }
                removeRedDots();
            }
        }

        return false;
    }

    // Determines if the king passed in to the method is in checkmate
    private boolean isCheckMate(Piece king) {

        for(int i = 0; i < pieceArray.length; i++) // Loops through all pieces on the team whose king is in check
        {
            if(pieceArray[i].getTeamColor() == king.getTeamColor() && !pieceArray[i].isCaptured()) { // Check if the piece is captured

            Piece piece = pieceArray[i];
            ArrayList<RedDot> moves = piece.findMoves();

            for(int k = 0; k < moves.size(); k++) // Loop through their possible moves to see if one of the moves makes the check go away
            {
                piece.phasePiece(moves.get(k).getRow(), moves.get(k).getColumn());

                if(!isInCheck(king, false))
                {
                    piece.unPhasePiece();
                    return false;
                }
                piece.unPhasePiece();
            }
            }
        }

        checkMate = true;

        if(king.getTeamColor() == Constants.WHITE) {
            statusMessage.setText("White king checkmated, Black wins!");
        } else {
            statusMessage.setText("Black king checkmated, White wins!");
        }

        return true;
    }

    // ---------------------------------------------------- Initialization Methods -------------------------------------------------------

    // Sets the board's variables to their default values
    public void setToDefaults(BorderPane pane, Scene scene, Stage primaryStage, TextField textField) {
        this.pane = pane;
        this.scene = scene;
        statusMessage = textField;
        isWhiteTurn = true;
        checkMate = false;
        pieceArray = new Piece[32];
        pieceState = Constants.CLICK_ON_PIECE;
        possibleMoves = new ArrayList<RedDot>();
        moveList = new ArrayList<>();
        boardArray = null;

        pane.setTop(initImage("board"));

        initPieces();
        initBoardArray();

        pane.setOnMouseClicked(e -> {
            if(e.getY() <= 480) { // Ensure that the mouse click is above the action bar
                if(!checkMate) {
                    if(isWhiteTurn) {
                        turn(convertToRC((int)(e.getY())), convertToRC((int)(e.getX())), Constants.WHITE);
                    } else {
                        turn(convertToRC((int)(e.getY())), convertToRC((int)(e.getX())), Constants.BLACK);
                    }
                }
            }
        });
    }

    // Initializes the basic pieces
    private void initPieces() {
        for (int i = 0; i < 8; i++) {
            pieceArray[i] = new Pawn(Constants.WHITE, this, 6, i, initImage("whitePawn"));
        }

        pieceArray[8] = new Rook(Constants.WHITE, this, 7, 7, initImage("whiteRook"));
        pieceArray[9] = new Rook(Constants.WHITE, this, 7, 0, initImage("whiteRook"));
        pieceArray[10] = new Knight(Constants.WHITE, this, 7, 6, initImage("whiteKnight"));
        pieceArray[11] = new Knight(Constants.WHITE, this, 7, 1, initImage("whiteKnight"));
        pieceArray[12] = new Bishop(Constants.WHITE, this, 7, 5, initImage("whiteBishop"));
        pieceArray[13] = new Bishop(Constants.WHITE, this, 7, 2, initImage("whiteBishop"));
        pieceArray[14] = new Queen(Constants.WHITE, this, 7, 3, initImage("whiteQueen"));
        pieceArray[15] = new King(Constants.WHITE, this, 7, 4, initImage("whiteKing"));

        for (int i = 16; i < 24; i++) {
            pieceArray[i] = new Pawn(Constants.BLACK, this, 1, (i - 16), initImage("blackPawn"));
        }

        pieceArray[24] = new Rook(Constants.BLACK, this, 0, 7, initImage("blackRook"));
        pieceArray[25] = new Rook(Constants.BLACK, this, 0, 0, initImage("blackRook"));
        pieceArray[26] = new Knight(Constants.BLACK, this, 0, 6, initImage("blackKnight"));
        pieceArray[27] = new Knight(Constants.BLACK, this, 0, 1, initImage("blackKnight"));
        pieceArray[28] = new Bishop(Constants.BLACK, this, 0, 5, initImage("blackBishop"));
        pieceArray[29] = new Bishop(Constants.BLACK, this, 0, 2, initImage("blackBishop"));
        pieceArray[30] = new Queen(Constants.BLACK, this, 0, 3, initImage("blackQueen"));
        pieceArray[31] = new King(Constants.BLACK, this, 0, 4, initImage("blackKing"));
    }

    // Initialize a piece image. Returns null and prints out an error statement if no image is found.
    public ImageView initImage(String imageName) {
        try {
            return new ImageView(new Image(new FileInputStream("images\\" + imageName + ".png")));
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find image named: " + imageName + ".png");
        }
        return null;
    }

    // Initialize the board array with default piece positions
    public void initBoardArray() {
    
        Piece[][] boardArray = {
            {pieceArray[25], pieceArray[27], pieceArray[29], pieceArray[30], pieceArray[31], pieceArray[28], pieceArray[26], pieceArray[24]},
            {pieceArray[16], pieceArray[17], pieceArray[18], pieceArray[19], pieceArray[20], pieceArray[21], pieceArray[22], pieceArray[23]},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {pieceArray[0], pieceArray[1], pieceArray[2], pieceArray[3], pieceArray[4], pieceArray[5], pieceArray[6], pieceArray[7]},
            {pieceArray[9], pieceArray[11], pieceArray[13], pieceArray[14], pieceArray[15], pieceArray[12], pieceArray[10], pieceArray[8]}
        };

        this.boardArray = boardArray;
    }

    // ----------------------------------------------------------------- Miscellaneous --------------------------------------------------------- 

    // Finds the king to the corresponding team color in the pieceArray.
    public Piece findKing(int teamColor) {
        for (int i = 0; i < pieceArray.length; i++) {
            if(pieceArray[i].getClass().getName().equals("King")) {
                if(pieceArray[i].getTeamColor() == teamColor)
                    return pieceArray[i];
            }
        }
        return null;
    }

    // Set the red dots visible
    public void setDotsVisible() {
        for(int i = 0; i < possibleMoves.size(); i++)
        {
            possibleMoves.get(i).setLocation();
        }
    }

    // Remove the red dots from the board
    public void removeRedDots() {
        for(int i = 0; i < possibleMoves.size(); i++)
        {
            possibleMoves.get(i).remove(pane);
        }
        repaint();
        
    }

    // "Repaints" the scene so the red dots get updated
    public void repaint() {
        scene.getWindow().setOpacity(0.999); // This must be done to reset or "repaint" the javafx scene so the red dot's removal gets updated.
        scene.getWindow().setOpacity(1); // This simply updates the frame so it checks everything again, this is due to a bug in JavaFX. 
    }

    // Removes all the piece images from the board
    public void removeAllPieceImages() {
        for (int i = 0; i < pieceArray.length; i++) {
            if(pieceArray[i] != null) {
                pane.getChildren().remove(pieceArray[i].getPieceImage());
            }
        }
    }

    // Modifys the boardArray with a new one, used in importing a new board
    public void modifyBoardArray(Piece[][] boardArray) {
        this.boardArray = boardArray;
        int pieceArrayIndex = 0;
        for(int r = 0; r < boardArray.length; r++) {
            for(int c = 0; c < boardArray[r].length; c++) {
                if(boardArray[r][c] != null) {
                    pieceArray[pieceArrayIndex] = boardArray[r][c];
                    pieceArrayIndex++;
                }
            }
        }
    }

    // Converts pixel value to the corresponding row or column value
    private int convertToRC(int num) {
        int pixels = Constants.PIECE_LENGTH;
        for(int rc = 0; rc <= 7; rc++) {
            if(num <= pixels) {
                return rc;
            }
            pixels += Constants.PIECE_LENGTH;
        }

        System.out.println("Unexpected mouse click location");
        return -1;
    }

    // ------------------------------------------------------------ Getters -----------------------------------------------------------------

    // Gets the move list
    public ArrayList<String> getMoveList() {
        return moveList;
    }

    // Gets the pieces
    public Pane getPane() {
        return pane;
    }

    // Gets the board array
    public Piece[][] getBoardArray() {
        return boardArray;
    }

    public Piece[] getPieceArray() {
        return pieceArray;
    }
}

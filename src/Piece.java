import java.util.ArrayList;
import java.util.Arrays;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * Abstract Piece class that the specific piece classes extend off of. Contains general piece methods and variables. 
 */
public abstract class Piece {    
    private boolean isCaptured;
    protected ImageView pieceImage;

    protected Board board;
    protected int teamColor;
    protected ArrayList<RedDot> possibleMoves;
    protected boolean isPhased;
    private Piece enemyPiecePhased;
    private int enemyPieceRow;
    private int enemyPieceColumn;

    private int beforePhasedRow;
    private int beforePhasedColumn;

    public Piece(int teamColor, Board board, int row, int column, ImageView pieceImage) {
        this.board = board;
        this.teamColor = teamColor;

        pieceImage.setLayoutX(column * Constants.PIECE_LENGTH);
        pieceImage.setLayoutY((row * Constants.PIECE_LENGTH));
        board.getPane().getChildren().add(pieceImage);
        this.pieceImage = pieceImage;
    }

    // Gets the current row of this piece, if it is not found it returns -1
    public int getCurrentRow() {
        Piece[][] boardArray = board.getBoardArray();

        for(int r = 0; r < boardArray.length; r++) {
            for(int c = 0; c < boardArray[r].length; c++) {
                if(boardArray[r][c] != null && boardArray[r][c].equals(this)) {
                    return r;
                }
            }
        }

        return -1;
    }

    // Gets the current column of this piece, if it is not found it returns -1
    public int getCurrentColumn() {
        Piece[][] boardArray = board.getBoardArray();

        for(int r = 0; r < boardArray.length; r++) {
            for(int c = 0; c < boardArray[r].length; c++) {
                if(boardArray[r][c] != null && boardArray[r][c].equals(this)) {
                    return c;
                }
            }
        }

        return -1;
    }

    // Gets this piece's image
    public ImageView getPieceImage() {
        return pieceImage;
    }

    // Gets the team color of this piece
    public int getTeamColor() {
        return teamColor;
    }

    // Default findMoves method to be overwritten by specific pieces' methods
    public ArrayList<RedDot> findMoves() {
        return null;
    }
    
    // Moves the piece image with fancy animations and calls change location
    public void move(int row, int column) {

        board.getMoveList().add(formatMove(row, column));
        
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), pieceImage);
        transition.setFromX(pieceImage.getTranslateX());
        transition.setFromY(pieceImage.getTranslateY());
        transition.setToX(pieceImage.getTranslateX() + ((column - getCurrentColumn()) * Constants.PIECE_LENGTH));
        transition.setToY(pieceImage.getTranslateY() + ((row - getCurrentRow()) * Constants.PIECE_LENGTH));

        transition.playFromStart();

        changeLocation(row, column, this);
    }

    // Formats the move string key for exporting moves
    private String formatMove(int row, int column) {
        String teamColor;
        String pieceType = this.getClass().getName();

        if(this.teamColor == Constants.WHITE) {
            teamColor = "White";
        } else {
            teamColor = "Black";
        }

        return teamColor + " " + pieceType + " to " + (row + 1) + ", " + (column + 1); 
    }
    
    // Changes a piece's location on the board array
    public void changeLocation(int row, int column, Piece piece) {
        board.getBoardArray()[getCurrentRow()][getCurrentColumn()] = null;
        board.getBoardArray()[row][column] = piece;
    }
    
    // Removes a piece from the array and from the board, sets isCaptured to true;
    public void removePiece() {
        board.getPane().getChildren().remove(pieceImage);
        isCaptured = true;
        board.getBoardArray()[getCurrentRow()][getCurrentColumn()] = null;

    }
    // Returns if the piece is captured
    public boolean isCaptured() {
        return isCaptured;
    }
    
    /**
     * "Phasing" a piece is theoretically moving a piece to a certain position. In code, this will change the array position to match
     * the phased coordinates, but the images on the board will not reflect this. This method is the cornerstone of the isInCheck and isCheckmate
     * method as they use phasing to determine if a move will put a king check or save a king from being in check. This method will also phase an enemy piece if
     * it were to be theoretically captured. 
     */
    public void phasePiece(int phasedRow, int phasedColumn) {

        isPhased = true;
        enemyPiecePhased = board.getBoardArray()[phasedRow][phasedColumn];
        if(enemyPiecePhased != null) { // Is actually a piece
            enemyPiecePhased.isCaptured = true;
            board.getBoardArray()[enemyPiecePhased.getCurrentRow()][enemyPiecePhased.getCurrentColumn()] = null;
        }

        beforePhasedRow = getCurrentRow();
        beforePhasedColumn = getCurrentColumn();

        board.getBoardArray()[getCurrentRow()][getCurrentColumn()] = null;

        board.getBoardArray()[phasedRow][phasedColumn] = this;

        enemyPieceRow = phasedRow;
        enemyPieceColumn = phasedColumn;
    }
    
    // Unphase the piece by returning it to its original position, and an enemy piece aswell if it was captured during phasing
    public void unPhasePiece() {
        isPhased = false;
        board.getBoardArray()[beforePhasedRow][beforePhasedColumn] = this;
        board.getBoardArray()[enemyPieceRow][enemyPieceColumn] = enemyPiecePhased;
        if(enemyPiecePhased != null) {
            enemyPiecePhased.isCaptured = false;
        }
    }

    // Get if the piece is phased
    public boolean isPhased() {
        return isPhased;
    }

    // Change a pawn into a queen
    public void changePawn(Piece piece, int teamColor) {

        String teamColorImageStr = "";
        if(teamColor == Constants.WHITE) {
            teamColorImageStr = "white";
        } else {
            teamColorImageStr = "black";
        }

        int index = Arrays.asList(board.getPieceArray()).indexOf(piece);
        
        board.getPane().getChildren().remove(pieceImage);
        board.getPieceArray()[index] = new Queen(teamColor, board, getCurrentRow(), getCurrentColumn(), board.initImage(teamColorImageStr + "Queen"));
        board.getBoardArray()[getCurrentRow()][getCurrentColumn()] = board.getPieceArray()[index];
    }
}

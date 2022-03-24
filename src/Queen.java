import java.util.ArrayList;

import javafx.scene.image.ImageView;

/**
 * Class Queen that extends piece. Contains specific moving loops for this piece.
 */
public class Queen extends Piece {

    public Queen(int teamColor, Board board, int startingRow, int startingColumn, ImageView pieceImage) {
        super(teamColor, board, startingRow, startingColumn, pieceImage);  
    }

    // Uses the same code as the rook and queen use to move.
    public ArrayList<RedDot> findMoves() {
        possibleMoves = new ArrayList<RedDot>();

        Piece[][] boardArray = board.getBoardArray();

        // Up
        for(int row = getCurrentRow() - 1; row >= 0; row--) {
            Piece piece = boardArray[row][getCurrentColumn()];

            if(piece == null) { // queen sees empty space
                possibleMoves.add(new RedDot(row, getCurrentColumn(), board));
            }
            else if(piece.getTeamColor() != teamColor) { // queen sees enemy piece
                possibleMoves.add(new RedDot(row, getCurrentColumn(), board));
                break; // break because queen stops when it would capture a piece
            }
            else { // queen sees a friendly piece
                break; // break because queen cannot skip a friendly piece
            }
        }

        // Down
        for(int row = getCurrentRow() + 1; row <= 7; row++) {
            Piece piece = boardArray[row][getCurrentColumn()];

            if(piece == null) { 
                possibleMoves.add(new RedDot(row, getCurrentColumn(), board));
            }
            else if(piece.getTeamColor() != teamColor) {
                possibleMoves.add(new RedDot(row, getCurrentColumn(), board));
                break; 
            }
            else { 
                break; 
            }
        }

        // Right 
        for(int column = getCurrentColumn() + 1; column <= 7; column++) {
            Piece piece = boardArray[getCurrentRow()][column];

            if(piece == null) {
                possibleMoves.add(new RedDot(getCurrentRow(), column, board));
            }
            else if(piece.getTeamColor() != teamColor) {
                possibleMoves.add(new RedDot(getCurrentRow(), column, board));
                break; 
            }
            else {
                break;
            }
        }
        
        // Left
        for(int column = getCurrentColumn() - 1; column >= 0; column--) {
            Piece piece = boardArray[getCurrentRow()][column];

            if(piece == null) {
                possibleMoves.add(new RedDot(getCurrentRow(), column, board));
            }
            else if(piece.getTeamColor() != teamColor) {
                possibleMoves.add(new RedDot(getCurrentRow(), column, board));
                break; 
            }
            else {
                break;
            }
        }

        int columnUpLeft = getCurrentColumn() - 1;
        int columnDownRight = getCurrentColumn() + 1;
        int rowUpRight = getCurrentRow() - 1;
        int rowDownLeft = getCurrentRow() + 1;

        // up left
        for(int row = getCurrentRow() - 1; row >= 0; row--) {
            if(columnUpLeft >= 0)
                if(boardArray[row][columnUpLeft] == null) // queen sees empty space
                {
                    RedDot dot = new RedDot(row, columnUpLeft, board);
                    possibleMoves.add(dot);
                }
                else if(boardArray[row][columnUpLeft].getTeamColor() != teamColor) // queen sees enemy piece
                {
                    RedDot dot = new RedDot(row, columnUpLeft, board);
                    possibleMoves.add(dot);
                    break; // break because queen stops when it captures a piece
                }
                else // queen sees a friendly piece
                {
                    break; // break because queen cannot skip a friendly piece
                }
            columnUpLeft--;
        }

        // down right
        for(int row = getCurrentRow() + 1; row <= 7; row++) {
            if(columnDownRight <= 7)
                if(boardArray[row][columnDownRight] == null)
                {
                    RedDot dot = new RedDot(row, columnDownRight, board);
                    possibleMoves.add(dot);
                }
                else if(boardArray[row][columnDownRight].getTeamColor() != teamColor)
                {
                    RedDot dot = new RedDot(row, columnDownRight, board);
                    possibleMoves.add(dot);
                    break;
                }
                else 
                {
                    break;
                }
            columnDownRight++;
        }

        // up right
        for(int column = getCurrentColumn() - 1; column >= 0; column--) {
            if(rowDownLeft <= 7)
                if(boardArray[rowDownLeft][column] == null) 
                {
                    RedDot dot = new RedDot(rowDownLeft, column, board);
                    possibleMoves.add(dot);
                }
                else if(boardArray[rowDownLeft][column].getTeamColor() != teamColor) 
                {
                    RedDot dot = new RedDot(rowDownLeft, column, board);
                    possibleMoves.add(dot);
                    break; 
                }
                else 
                {
                    break; 
                }
            rowDownLeft++;
        }

        // down left

        for(int column = getCurrentColumn() + 1; column <= 7; column++) {
            if(rowUpRight >= 0)
                if(boardArray[rowUpRight][column] == null) 
                {
                    RedDot dot = new RedDot(rowUpRight, column, board);
                    possibleMoves.add(dot);
                }
                else if(boardArray[rowUpRight][column].getTeamColor() != teamColor) 
                {
                    RedDot dot = new RedDot(rowUpRight, column, board);
                    possibleMoves.add(dot);
                    break; 
                }
                else 
                {
                    break; 
                }
            rowUpRight--;
        }
        
        return possibleMoves;
    }
}

import java.util.ArrayList;

import javafx.scene.image.ImageView;

/**
 * Class Bishop that extends piece. Contains specific moving loops for the bishop piece.
 */
public class Bishop extends Piece {

    public Bishop(int teamColor, Board board, int startingRow, int startingColumn, ImageView pieceImage) {
        super(teamColor, board, startingRow, startingColumn, pieceImage);  
    }

    // Finds the bishops moves by going up each diagonal, one row or column is incremented in the loop declaration and the other is at the end. 
    public ArrayList<RedDot> findMoves() {
        possibleMoves = new ArrayList<RedDot>();

        int columnUpLeft = getCurrentColumn() - 1;
        int columnDownRight = getCurrentColumn() + 1;
        int rowUpRight = getCurrentRow() - 1;
        int rowDownLeft = getCurrentRow() + 1;

        Piece[][] boardArray = board.getBoardArray();

        // up left
        for(int row = getCurrentRow() - 1; row >= 0; row--) {
            if(columnUpLeft >= 0)
                if(boardArray[row][columnUpLeft] == null) // bishop sees empty space
                {
                    possibleMoves.add(new RedDot(row, columnUpLeft, board));
                }
                else if(boardArray[row][columnUpLeft].getTeamColor() != teamColor) // bishop sees enemy piece
                {
                    possibleMoves.add(new RedDot(row, columnUpLeft, board));
                    break; // break because bishop stops when it captures a piece
                }
                else // bishop sees a friendly piece
                {
                    break; // break because bishop cannot skip a friendly piece
                }
            columnUpLeft--;
        }

        // down right
        for(int row = getCurrentRow() + 1; row <= 7; row++)
        {
            if(columnDownRight <= 7)
                if(boardArray[row][columnDownRight] == null) 
                {
                    possibleMoves.add(new RedDot(row, columnDownRight, board));
                }
                else if(boardArray[row][columnDownRight].getTeamColor() != teamColor) 
                {
                    possibleMoves.add(new RedDot(row, columnDownRight, board));
                    break; 
                }
                else 
                {
                    break; 
                }
            columnDownRight++;
        }

        // up right
        for(int column = getCurrentColumn() - 1; column >= 0; column--) 
        {
            if(rowDownLeft <= 7)
                if(boardArray[rowDownLeft][column] == null) 
                {
                    possibleMoves.add(new RedDot(rowDownLeft, column, board));
                }
                else if(boardArray[rowDownLeft][column].getTeamColor() != teamColor) 
                {
                    possibleMoves.add(new RedDot(rowDownLeft, column, board));
                    break; 
                }
                else 
                {
                    break; 
                }
            rowDownLeft++;
        }

        // down left
        for(int column = getCurrentColumn() + 1; column <= 7; column++) 
        {
            if(rowUpRight >= 0)
                if(boardArray[rowUpRight][column] == null) 
                {
                    possibleMoves.add(new RedDot(rowUpRight, column, board));
                }
                else if(boardArray[rowUpRight][column].getTeamColor() != teamColor) 
                {
                    possibleMoves.add(new RedDot(rowUpRight, column, board));
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
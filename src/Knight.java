import java.util.ArrayList;

import javafx.scene.image.ImageView;

/**
 * Class Knight that extends piece. Contains specific moves for each of the knight's possible 8 moves.
 */
public class Knight extends Piece {

    public Knight(int teamColor, Board board, int startingRow, int startingColumn, ImageView pieceImage) {
        super(teamColor, board, startingRow, startingColumn, pieceImage);  
    }

    // Finds the possible moves by simply checking in all 8 possible spaces
    public ArrayList<RedDot> findMoves()
    {
        possibleMoves = new ArrayList<RedDot>();

        Piece boardArray[][] = board.getBoardArray();

        if(getCurrentRow() - 2 >= 0 && getCurrentColumn() + 1 <= 7) {
            knightMove(getCurrentRow() - 2, getCurrentColumn() + 1, boardArray);
        }

        if(getCurrentRow() - 1 >= 0 && getCurrentColumn() + 2 <= 7) {
            knightMove(getCurrentRow() - 1, getCurrentColumn() + 2, boardArray);
        }

        if(getCurrentRow() + 1 <= 7 && getCurrentColumn() + 2 <= 7) {
            knightMove(getCurrentRow() + 1, getCurrentColumn() + 2, boardArray);
        }

        if(getCurrentRow() + 2 <= 7 && getCurrentColumn() + 1 <= 7) {
            knightMove(getCurrentRow() + 2, getCurrentColumn() + 1, boardArray);
        }

        if(getCurrentRow() + 2 <= 7 && getCurrentColumn() - 1 >= 0) {
            knightMove(getCurrentRow() + 2, getCurrentColumn() - 1, boardArray);
        }

        if(getCurrentRow() + 1 <= 7 && getCurrentColumn() - 2 >= 0) {
            knightMove(getCurrentRow() + 1, getCurrentColumn() - 2, boardArray);
        }

        if(getCurrentRow() - 1 >= 0 && getCurrentColumn() - 2 >= 0) {
            knightMove(getCurrentRow() - 1, getCurrentColumn() - 2, boardArray);
        }

        if(getCurrentRow() - 2 >= 0 && getCurrentColumn() - 1 >= 0) {
            knightMove(getCurrentRow() - 2, getCurrentColumn() - 1, boardArray);
        }

        return possibleMoves;
    }

    // Small method to simplify creating king moves
    private void knightMove(int row, int column, Piece[][] boardArray) {
        if(boardArray[row][column] == null ||
          (boardArray[row][column].getTeamColor() != teamColor))
        {
            possibleMoves.add(new RedDot(row, column, board));
        }
    }
}
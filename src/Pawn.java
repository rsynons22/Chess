import java.util.ArrayList;

import javafx.scene.image.ImageView;

/**
 * Class Pawn that extends piece. Contains specific moves for the pawn piece, and contains a special move method to deal with pawn conversion. 
 */
public class Pawn extends Piece {
    private boolean canSkip;

    public Pawn(int teamColor, Board board, int startingRow, int startingColumn, ImageView pieceImage) {
        super(teamColor, board, startingRow, startingColumn, pieceImage);  
        canSkip = true;
    }

    // For each team, pawns check their possible moves and attacks. They can skip two spaces once at the beginning. 
    public ArrayList<RedDot> findMoves() {
        possibleMoves = new ArrayList<RedDot>();

        Piece[][] boardArray = board.getBoardArray();

        if(teamColor == Constants.BLACK) {

            if(canSkip) {
                try {
                if(((getCurrentRow() + 2) <= 7) &&
                   (boardArray[getCurrentRow() + 2][getCurrentColumn()] == null) && 
                   (boardArray[getCurrentRow() + 1][getCurrentColumn()] == null)) 
                { // no piece at the space just 2 before the pawn
                    possibleMoves.add(new RedDot(getCurrentRow() + 2, getCurrentColumn(), board));
                }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println(this + " went out of bounds");
                }
            }

            if(((getCurrentRow() + 1) <= 7) &&
               (boardArray[getCurrentRow() + 1][getCurrentColumn()] == null)) 
            {// no piece at the space just one before the pawn
                possibleMoves.add(new RedDot(getCurrentRow() + 1, getCurrentColumn(), board));
            }

            if(((getCurrentRow() + 1) <= 7) && 
               (getCurrentColumn() - 1) >= 0 &&
               (boardArray[getCurrentRow() + 1][getCurrentColumn() - 1] != null) && 
               (boardArray[getCurrentRow() + 1][getCurrentColumn() - 1].getTeamColor() == Constants.WHITE))
            {
                possibleMoves.add(new RedDot(getCurrentRow() + 1, getCurrentColumn() - 1, board));
            }

            if((getCurrentRow() + 1) <= 7 && (getCurrentColumn() + 1) <= 7 &&
                boardArray[getCurrentRow() + 1][getCurrentColumn() + 1] != null && 
            (boardArray[getCurrentRow() + 1][getCurrentColumn() + 1].getTeamColor() == Constants.WHITE))
            {
                possibleMoves.add(new RedDot(getCurrentRow() + 1, getCurrentColumn() + 1, board));
            }
        }
        else
        {

            if(canSkip) {

                if(((getCurrentRow() - 2) >= 0 &&
                    boardArray[getCurrentRow() - 2][getCurrentColumn()] == null) && (boardArray[getCurrentRow() - 1][getCurrentColumn()] == null)) {
                    possibleMoves.add(new RedDot(getCurrentRow() - 2, getCurrentColumn(), board));
                }
            }

            if((getCurrentRow() - 1) >= 0 &&
                boardArray[getCurrentRow() - 1][getCurrentColumn()] == null) {
                possibleMoves.add(new RedDot(getCurrentRow() - 1, getCurrentColumn(), board));
            }

            if((getCurrentRow() - 1) >= 0 && (getCurrentColumn() + 1) <= 7 &&
              (boardArray[getCurrentRow() - 1][getCurrentColumn() + 1] != null) && 
              (boardArray[getCurrentRow() - 1][getCurrentColumn() + 1].getTeamColor() == Constants.BLACK))
            {
                possibleMoves.add(new RedDot(getCurrentRow() - 1, getCurrentColumn() + 1, board));
            }

            if((getCurrentRow() - 1) >= 0 && (getCurrentColumn() - 1) >= 0 &&
              (boardArray[getCurrentRow() - 1][getCurrentColumn() - 1] != null) && 
              (boardArray[getCurrentRow() - 1][getCurrentColumn() - 1].getTeamColor() == Constants.BLACK))
            {
                possibleMoves.add(new RedDot(getCurrentRow() - 1, getCurrentColumn() - 1, board));
            }
        }

        return possibleMoves;
    }

    // Will change the pawn to a queen if it reaches the opposite end of the board.
    public void move(int row, int column) 
    {
        super.move(row, column);
        canSkip = false;
        
        if(getCurrentRow() == 0 && teamColor == Constants.WHITE)
        {
            changePawn(this, teamColor);
        }

        if(getCurrentRow() == 7 && teamColor == Constants.BLACK)
        {
            changePawn(this, teamColor);
        }
    }
}

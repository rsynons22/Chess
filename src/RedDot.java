import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Class redDot is the class that contains image creation for a possible move dot to be shown on the board.
 */
public class RedDot {
    private int currentColumn;
    private int currentRow;
    private Board board;

    private ImageView image;

    public RedDot(int row, int column, Board board) {
        image = board.initImage("redDot");
        currentRow = row;
        currentColumn = column;
        this.board = board;
    }

    // Gets the column
    public int getColumn() {
        return currentColumn;
    }

    // Gets the row
    public int getRow() {
        return currentRow;
    }

    // Removes the red dot image from the pane
    public void remove(Pane pane) {
        pane.getChildren().remove(image);
    }

    // Sets the location of the red dot
    public void setLocation() {
        board.getPane().getChildren().add(image);
        image.setTranslateX((currentColumn * Constants.PIECE_LENGTH) + 20); // + 20 due to the red dot being low in png
        image.setTranslateY((currentRow * Constants.PIECE_LENGTH) + 20);
    }
}

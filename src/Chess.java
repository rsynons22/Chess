import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Main application that contains the start method. Creates the pane, scene, stage, and board.
 */
public class Chess extends Application {

    private static BorderPane pane;
    private static Scene scene;
    private static Stage primaryStage;
    private static TextField textField;
    private static Board board;
    private static ActionBar actionBar;

    public void start(Stage primaryStage) {

        pane = new BorderPane();
        BorderPane boardPane = new BorderPane();
        BorderPane actionPane = new BorderPane();

        textField = new TextField("White Turn");
        textField.setFont(Font.font(null, FontWeight.BOLD, 15));
        textField.setEditable(false);
        textField.setFocusTraversable(false);

        actionBar = new ActionBar(primaryStage);

        actionPane.setTop(textField);
        actionPane.setBottom(actionBar.getHBox());

        pane.setTop(boardPane);
        pane.setBottom(actionPane);

        scene = new Scene(pane, 480, 540);
        primaryStage.setTitle("Chess");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> e.consume()); // Prevents closing normally
        primaryStage.show();

        Chess.primaryStage = primaryStage;

        board = new Board(boardPane, scene, primaryStage, textField, actionBar);
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    // Gets the board object
    public static Board getBoard() {
        return board;
    }

    // Resets the board back to as if the window was just launched
    public static void resetBoard() {
        textField.setText("White Turn");
        board.setToDefaults(pane, scene, primaryStage, textField);
    }

}

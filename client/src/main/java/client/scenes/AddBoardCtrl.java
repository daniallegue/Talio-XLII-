package client.scenes;

import client.MultiboardCtrl;
import client.SceneCtrl;
import com.google.inject.Inject;
import commons.Board;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AddBoardCtrl {
    private final SceneCtrl sceneCtrl;
    private MultiboardCtrl multiboardCtrl;
    private BoardsOverviewCtrl boardsOverviewCtrl;

    private boolean isNew;
    @FXML
    private TextField title;

    @FXML
    private TextArea description;
    @FXML
    private Button applyButton;
    private Board board;

    /** Initialises the controller using dependency injection */
    @Inject
    public AddBoardCtrl(SceneCtrl sceneCtrl, MultiboardCtrl multiboardCtrl, BoardsOverviewCtrl boardsOverviewCtrl) {
        this.sceneCtrl = sceneCtrl;
        this.multiboardCtrl = multiboardCtrl;
        this.boardsOverviewCtrl = boardsOverviewCtrl;
    }


    /**
     * @param actionEvent the event that triggered the method
     *                    Updates the board
     */
    public void updateBoard(ActionEvent actionEvent) {
        board.boardTitle = title.getText();
        board.description = description.getText();
        boardsOverviewCtrl.updateBoard(board);
        cancel();
    }

    /**
     * @param actionEvent the event that triggered the method
     *                    Creates a new board
     */
    public void createBoard(ActionEvent actionEvent) {
        multiboardCtrl.createBoard(title.getText(), description.getText());
        cancel();
    }

    /**
     * Clears all fields
     */
    public void clearFields(){
        title.clear();
        description.clear();
    }

    /**
     * Exits the scene
     */
    public void cancel() {
        clearFields();
        sceneCtrl.showMultiboard();
        boardsOverviewCtrl.refresh();
    }

    /**
     * Sets the addboard controller to create mode
     */
    public void setCreateBoard() {
        applyButton.setText("Create");
        applyButton.setOnAction(this::createBoard);
    }

    /** Sets the addboard controller to edit mode
     * @param board
     */
    public void setEditBoard(Board board) {
        this.board = board;
        applyButton.setText("Save");
        title.setText(board.boardTitle);
        description.setText(board.description);
        applyButton.setOnAction(this::updateBoard);
    }
}

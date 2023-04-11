package client.components;

import client.*;
import client.utils.ServerUtils;
import com.google.inject.*;
import commons.*;
import commons.utils.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.util.UUID;


public class PreviewTagComponentCtrl {
    private final SceneCtrl sceneCtrl;
    private final IDGenerator idGenerator;
    private final ServerUtils server;

//    private final ChangeListener<Boolean> focusChangeListener;

    private Tag tag;

    @FXML
    private TextField tagTitle;

    @FXML
    private AnchorPane tagPane;

    @Inject
    public PreviewTagComponentCtrl(SceneCtrl sceneCtrl, IDGenerator idGenerator, ServerUtils server) {
        this.sceneCtrl = sceneCtrl;
        this.idGenerator = idGenerator;
        this.server = server;
//        this.focusChangeListener = (observable, oldFocus, newFocus) -> {
//            if (!newFocus) {
//                saveBoard(tag.boardId);
//            }};

    }

    /** Sets a listener on the focused property. This way we know the user has stopped typing */
    @FXML
    public void initialize() {
//        tagTitle.focusedProperty().addListener(focusChangeListener);
    }



    /**
     * Gets the task contained in this controller
     */
    public Tag getTag() {
        var title = tagTitle.getText();
        tag.tagTitle = title;
        return this.tag;
    }


    /**
     * Sets the UI components to the specified task
     */
    public void setTag(Tag tag) {
        tagTitle.setText(tag.tagTitle);
        tagPane.setStyle("-fx-background-color:" + tag.tagColor);
        this.tag = tag;
    }


    /** Gets called when the text box loses focus or the user presses enter.
     * Creates the card. */
    public void createTag(String title) {
        var tag = new Tag(
                idGenerator.generateID(),
                title,
                this.tag.tagColor,
                this.tag.board.boardID,
                this.tag.board
        );
        var result = server.addTagToBoard(tag, tag.board);
        System.out.println("Tag is created");
        if (!result.success) {
            sceneCtrl.showError(result.message, "Failed to create tag");
        }
    }

    /** Gets called when the text box loses focus or the user presses enter.
     * Creates the card. */
    public void updateTag(String title) {
        tag.setTagTitle(title);
        var result = server.updateTagFromBoard(tag.tagID, tag);
        if (!result.success) {
            sceneCtrl.showError(result.message, "Failed to update tag");
        }
    }



    /** Saves the card this task is connected to */
    public void saveBoard(UUID boardId) {
        sceneCtrl.saveBoard(boardId);
    }


    /**
     * Sets the given card attributes of the tag
     */
    public void setBoard(Board board) {
        this.tag.boardId = board.boardID;
        this.tag.board = board;
    }

    /** The onAction listener. When the user presses enter this activates */
    public void action() {
//        tagTitle.focusedProperty().removeListener(focusChangeListener);
       updateTag(tagTitle.getText());
    }


}

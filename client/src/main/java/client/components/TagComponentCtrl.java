package client.components;

import client.*;
import client.utils.ServerUtils;
import com.google.inject.*;
import commons.*;
import commons.utils.*;
import javafx.beans.value.ChangeListener;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;



public class TagComponentCtrl {
    private final SceneCtrl sceneCtrl;
    private final IDGenerator idGenerator;
    private final ServerUtils server;

//    private final ChangeListener<Boolean> focusChangeListener;

    private Tag tag;

    @FXML
    private TextField tagTitle;

    @FXML
    private Button deleteButton;

    @FXML
    private AnchorPane tagPane;

    @Inject
    public TagComponentCtrl(SceneCtrl sceneCtrl, IDGenerator idGenerator, ServerUtils server) {
        this.sceneCtrl = sceneCtrl;
        this.idGenerator = idGenerator;
        this.server = server;
//        this.focusChangeListener = (observable, oldFocus, newFocus) -> {
//            if (!newFocus) {
//                saveCard();
//            }};

    }

    /** Sets a listener on the focused property. This way we know the user has stopped typing */
    @FXML
    public void initialize() {
//        tagTitle.focusedProperty().addListener(focusChangeListener);
    }

//    /** The onAction listener. When the user presses enter this activates */
//    public void action() {
//        getTag();
//    }

    /**
     * Gets the task contained in this controller
     */
    public Tag getTag() {
        var title = tagTitle.getText();
        tag.tagTitle = title;
        return this.tag;
    }

//    /** Deletes this tag from the card */
//    public void deleteTask() {
//        sceneCtrl.deleteTag(this);
//    }

    /** Deletes this tag from the card */
    public void deleteTag() {
        sceneCtrl.deleteTag(this);
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
                this.tag.card.cardID,
                this.tag.card
        );
        var result = server.addTagToCard(tag, tag.card);
        if (!result.success) {
            sceneCtrl.showError(result.message, "Failed to create card");
        }
    }



    /** Saves the card this tag is connected to */
    public void saveCard() {
        sceneCtrl.saveCard();
    }


    /**
     * Sets the given card attributes of the tag
     */
    public void setCard(Card card) {
        this.tag.cardId = card.cardID;
        this.tag.card = card;
    }

    /** The onAction listener. When the user presses enter this activates */
    public void action() {
//        tagTitle.focusedProperty().removeListener(focusChangeListener);
        createTag(tagTitle.getText());

    }
}

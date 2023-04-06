package client.components;

import client.*;
import client.utils.ServerUtils;
import com.google.inject.*;
import commons.*;
import commons.utils.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.*;

public class TagComponentCtrl {
    private final SceneCtrl sceneCtrl;
    private final IDGenerator idGenerator;
    private final ServerUtils server;

    private Tag tag;

    private Card card;
    private UUID cardId;
    @FXML
    private Label tagLabel;

    @FXML
    private Button deleteButton;

    @FXML
    private AnchorPane tagPane;

    @Inject
    public TagComponentCtrl(SceneCtrl sceneCtrl, IDGenerator idGenerator, ServerUtils server) {
        this.sceneCtrl = sceneCtrl;
        this.idGenerator = idGenerator;
        this.server = server;
    }

    /**
     * Initialises the controller.
     * Adds a listener to the task title focus. If a user stops typing it will automatically save the card.
     */
    @FXML
    public void initialize() {
    }

    /**
     * Gets the task contained in this controller
     */
    public Tag getTag() {
        var title = tagLabel.getText();

        Tag newTag = new Tag(idGenerator.generateID(), title, "#00FFD1", cardId, card);
        return newTag;
    }

    /**
     * Sets the UI components to the specified task
     */
    public void setTag(Tag tag) {
        tagLabel.setText(tag.tagTitle);
        tagPane.setStyle("-fx-background-color:" + tag.tagColor);
        this.card = tag.card;
        this.cardId = tag.cardId;
        this.tag = tag;
    }

    /**
     * Sets the card in the ctrl
     */
    public void setCard(Card card) {
        this.card = tag.card;
        this.cardId = tag.cardId;
    }
}

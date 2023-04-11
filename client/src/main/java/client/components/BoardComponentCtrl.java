package client.components;

import client.*;
import client.interfaces.*;
import client.scenes.CustomizeBoardCtrl;
import client.utils.*;
import com.google.inject.*;
import commons.*;
import commons.utils.*;
import jakarta.ws.rs.WebApplicationException;
import javafx.application.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Pair;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Controller;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class BoardComponentCtrl implements InstanceableComponent, Closeable {


    private MyFXML fxml;
    private SceneCtrl sceneCtrl;
    private List<ListComponentCtrl> listComponentCtrls;
    private List<PreviewTagComponentCtrl> tagComponentCtrls;
    private IDGenerator idGenerator;
    private ServerUtils server;
    private StompSession.Subscription subscription;

    @FXML
    private Label boardTitle;
    @FXML
    private Label boardDescription;
    @FXML
    private HBox listContainer;
    @FXML
    AnchorPane mainPane;
    @FXML
    public Label connectionText;
    @FXML
    public Label customizeText;
    @FXML
    public Label linkText;

    @FXML
    public HBox tagBox;

    @FXML
    public Button tagButton;

    private Board board;



    /** Initialises the controller using dependency injection */
    @Inject
    public BoardComponentCtrl(RandomIDGenerator idGenerator, ServerUtils server, SceneCtrl sceneCtrl, MyFXML fxml) {
        this.server = server;
        this.sceneCtrl = sceneCtrl;
        this.fxml = fxml;
        this.listComponentCtrls = new ArrayList<>();
        this.tagComponentCtrls = new ArrayList<>();
        this.idGenerator = idGenerator;
    }

    /**
     * Initializes a new board
     */
    public UUID initializeBoard(String title, String descriptionText){

        this.board = new Board(title,new ArrayList<>(), descriptionText,
                false, null, CustomizeBoardCtrl.baseTheme);
        this.board.tagList = new ArrayList<>();
        this.board.setBoardID(idGenerator.generateID());
        sceneCtrl.setBoardIDForAllComponents(board.getBoardID());
        System.out.println("Created a new board with id: \t" + this.board.getBoardID());
        server.addBoard(this.board);
        registerForMessages();
        return board.boardID;
    }

    /** Loads in an existing board
     * @param boardid
     */
    public void setBoard(UUID boardid){
        Result<Board> res = server.getBoard(boardid);
        if(res.success){
            this.board = res.value;
            sceneCtrl.setBoardIDForAllComponents(boardid);
            System.out.println("Loaded in a board with id " + boardid);
            refresh();
        }
        else{
            sceneCtrl.showError(res.message, "Error: Could not load in board!");
            sceneCtrl.showMultiboard();
        }

    }

    /**
     * Registers the component for receiving message from the websocket
     */
    public void registerForMessages(){
        unregisterForMessages();
        System.out.println("Board: \t" + board.getBoardID() + " registered for messaging");
        subscription = server.registerForMessages("/topic/update-board/", UUID.class, payload ->{
            System.out.println("Endpoint \"/topic/update-board/\" has been hit by a board with the id:\t"
                    + payload.toString());
            try {
                if(payload.equals(this.board.getBoardID())){
                    System.out.println("Refreshing board:\t" + board.getBoardID() + "\twith\t"
                            + board.cardListList.size() + "\tlists");
                    // Needed to prevent threading issues
                    Platform.runLater(() -> refresh());
                }
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
                }
            }
        );
    }

    @Override
    public void unregisterForMessages() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    /**
     * Refreshes overview with updated data
     */
    public void refresh() {
        close();
        clearFields();
        // Make a REST call to get the updated board from the server
        Result<Board> res = server.getBoard(board.getBoardID());
        board = res.value;
        if (res.success) {
            // Update the UI with the updated board information
            boardTitle.setText(board.boardTitle);
            boardDescription.setText(board.description);
            if (board.boardTheme != null) {
                setTheme();
            }

            // Clear the list container to remove the old lists from the UI
            ObservableList<Node> listNodes = listContainer.getChildren();
            listNodes.remove(0, listNodes.size() - 1);

            // Add the updated lists to the UI
            List<CardList> cardListLists = board.getCardListList();
            for (CardList list : cardListLists) {
                addList(list);
            }

            //Get the tags for a board
            for (Tag tag : board.tagList) {
                addTagToUI(tag);
            }
            registerForMessages();
        }
    }
    /**
     * Deletes list from the overview
     */
    public void delete() {
//        Integer deletingIdx = table.getSelectionModel().getSelectedItem().cardListID;
//        if (deletingIdx != null) {
//            server.deleteList(deletingIdx);
//            table.getItems().remove(deletingIdx);
//            refresh();
//        }
    }

    /**
     * Clears all fields
     */
    public void clearFields(){
        tagBox.getChildren().clear();
        tagComponentCtrls.clear();
    }

    /**
     * Edits the name on the list by clicking on it
     */
    public void editChange(TableColumn.CellEditEvent<CardList, String> cardListStringCellEditEvent) {
//        CardList list = table.getSelectionModel().getSelectedItem();
//        list.setCardListTitle(cardListStringCellEditEvent.getNewValue());
//        server.editList(list, list.cardListID);
    }

    /**
     * Creates a new list and shows the updated list overview, takes in a title
     * @param listTitle
     */
    public void createList(String listTitle){
        CardList cardList = new CardList(listTitle, new ArrayList<>(), board);
        cardList.setCardListId(idGenerator.generateID());
        cardList.boardId = board.getBoardID();

        board.cardListList.add(cardList);
        server.addList(cardList);
        System.out.println("Creating a list with id\t " + cardList.cardListId + " on board " + cardList.boardId);
    }

    /** Adds a single list to the end of board
     * @param list the list that gets added to the board
     * */
    public void addList(CardList list) {
        ObservableList<Node> listNodes = listContainer.getChildren();
        Pair<ListComponentCtrl, Parent> component = fxml.load(
                ListComponentCtrl.class, "client", "scenes", "components", "ListComponent.fxml");
        Parent parent = component.getValue();
        ListComponentCtrl listComponentCtrl = component.getKey();
        listComponentCtrl.setTheme(board.boardTheme);
        listComponentCtrl.setList(list);
        listComponentCtrls.add(listComponentCtrl);

        listNodes.add(listNodes.size()-1, parent);
    }

    /**
     * Adds a card to the list in the UI. It finds the list it's supposed to add it to and then adds it.
     * */
    public void addCardToList(Card card, UUID cardListId) {
        for (ListComponentCtrl listComponentCtrl : listComponentCtrls) {
            if (listComponentCtrl.getListId().equals(cardListId)) {
                listComponentCtrl.addSingleCard(card);
            }
        }
    }

    /**
     * Goes to add new list scene
     */
    public void openAddListScene() {
        sceneCtrl.showAddList();
    }

    /** Getter for the boardId
     * @return UUID of the boardId
     */
    public UUID getBoardID() {
        return board.getBoardID();
    }

    /** Getter for the boardId
     * @return UUID of the boardId
     */
    public UUID generateBoardID() {
        return idGenerator.generateID();
    }

    /**
     * @param scene the scene to set
     *              Sets the scene for the board
     */
    public void setScene(Scene scene) {
        sceneCtrl.setBoard(scene);
    }

    /**Goes to the home screen */
    public void backToOverview() {
        close();
        sceneCtrl.showMultiboard();
    }
    @Override
    public void close() {
        unregisterForMessages();
        listComponentCtrls.forEach(ListComponentCtrl::close);
    }

    /** Puts the invite link on the clipboard for easy sharing */
    public void copyInviteLink(MouseEvent event) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(board.boardID.toString());
        clipboard.setContent(content);
    }

    /**
     * Sets the theme of the board
     * propagates to all the list components
     */
    public void setTheme() {
        System.out.println(this.board);
        mainPane.setStyle("-fx-background-color: " + this.board.boardTheme.boardBackgroundColor + ";");
        connectionText.setStyle("-fx-text-fill: " + this.board.boardTheme.boardFont + ";");
        linkText.setStyle("-fx-text-fill: " + this.board.boardTheme.boardFont + ";");
        customizeText.setStyle("-fx-text-fill: " + this.board.boardTheme.boardFont + ";");

        boardTitle.setStyle("-fx-text-fill: " + this.board.boardTheme.boardFont + ";");
        boardDescription.setStyle("-fx-text-fill: " + this.board.boardTheme.boardFont + ";");

        listComponentCtrls.forEach(listComponentCtrl -> listComponentCtrl.setTheme(this.board.boardTheme));
    }

    /**
     * @param mouseEvent the mouse event
     *                   Launches the color picker view
     */
    public void launchColorPicker(MouseEvent mouseEvent) {
        sceneCtrl.showCustomizeBoard(this.board);
    }

    /**
     * Adds a new tag to the board
     */
    public void addTag(){
        var tag = new Tag(idGenerator.generateID(), "New Tag", "#00FFD1", this.board.boardID, this.board);
        tag.board = board;
        tag.boardId = board.boardID;
        addTagToUI(tag);
        saveBoard();
    }

    /**
     * Goes to add new card scene
     */
    public void addTagToUI(Tag tag) {
        var tagPair = fxml.load(PreviewTagComponentCtrl.class, "client", "scenes", "components", "PreviewTagComponent.fxml");
        tagPair.getValue().setId(tag.tagID.toString());
        tagBox.getChildren().add(tagPair.getValue());
        var ctrl = tagPair.getKey();

        ctrl.setTag(tag);
        ctrl.setBoard(board);
        tagComponentCtrls.add(ctrl);
    }

//    /** Deletes the tag this component controls */
//    public void deleteTag(Tag tag) {
//        tagComponentCtrls.removeIf( ctrl -> ctrl.getTag().equals(tag));
//        tagBox.getChildren().removeIf(x -> x.getId().equals(tag.tagID.toString()));
//        saveBoard();
//    }

    /**
     * @return Board
     */
    private Board getExistingBoard() {
        var titleVar = boardTitle.getText();
        var description = boardDescription.getText();
        var lists = new ArrayList<CardList>();
        var tags = new ArrayList<Tag>();
        listComponentCtrls.forEach(listCtrl -> lists.add(listCtrl.getList()));
        tagComponentCtrls.forEach(tagCtrl -> tags.add(tagCtrl.getTag()));

        board.boardTitle = titleVar;
        board.description = description;
        board.tagList = tags;
        board.cardListList = lists;
        return board;
    }

    /**
     * Creates new card on the server
     */
    public void saveBoard(){
        try {
            var b = getExistingBoard();
            var result = server.updateBoard(b, b.boardID);
            if(!result.success) {
                sceneCtrl.showError(result.message, "Failed to save board");
            }
        } catch (WebApplicationException e) {
            sceneCtrl.showError(e.getMessage(), "Failed to save board");
        }
    }

}

package client.scenes;

import client.*;
import client.components.*;
import client.interfaces.*;
import client.utils.*;
import commons.*;
import commons.utils.*;
import jakarta.ws.rs.*;
import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import org.springframework.messaging.simp.stomp.*;

import javax.inject.*;
import java.util.*;

public class AddCardCtrl implements InstanceableComponent {
    private final ServerUtils server;
    private final SceneCtrl sceneCtrl;
    private final IDGenerator idGenerator;
    private final MultiboardCtrl multiboardCtrl;
    private final List<TaskComponentCtrl> taskComponentCtrls;
    private final MyFXML fxml;
    private StompSession.Subscription subscription;

    private CardList cardList;
    private Card card;
    private UUID cardListId;

    @FXML
    private TextField titleOfCard;

    @FXML
    private TextArea description;
    @FXML
    public ListView<Parent> taskBox;
    @FXML
    public TextField taskTitle;

    @Inject
    public AddCardCtrl (ServerUtils server,
                        SceneCtrl sceneCtrl,
                        IDGenerator idGenerator,
                        MultiboardCtrl multiboardCtrl,
                        MyFXML fxml){
        this.server = server;
        this.sceneCtrl = sceneCtrl;
        this.idGenerator = idGenerator;
        this.multiboardCtrl = multiboardCtrl;
        this.fxml = fxml;
        this.taskComponentCtrls = new ArrayList<>();
    }

    @Override
    public void registerForMessages(){
        unregisterForMessages();
        System.out.println("Editing card:\t" + card.cardID + "\tregistered for messaging");
        subscription = server.registerForMessages("/topic/update-card/", UUID.class, payload ->{
            System.out.println("Endpoint \"/topic/update-card/\" has been hit by a card with the id:\t"
                    + payload);
            try {
                if(payload.equals(card.cardID)){
                    System.out.println("Refreshing edit view of card:\t" + card.cardID);
                    // Needed to prevent threading issues
                    Platform.runLater(this::refresh);
                }
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void refresh() {
        System.out.println("Refreshing card");
        var result = server.getCard(card.cardID);
        System.out.println("result of card refresh: " + result.message);
        if (!result.success) {
            if(result.code == 35) {         //Result.CARD_DOES_NOT_EXIST
                clearFields();
                sceneCtrl.showBoard();
                sceneCtrl.showError("Card was deleted", "Failed to refresh card");
                return;
            }

            sceneCtrl.showError(result.message, "Failed to refresh card");
        }

        clearFields();
        setCard(result.value);
    }

    @Override
    public void unregisterForMessages() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    /**
     * Closes add task window
     */
    public void close(){
        unregisterForMessages();
        saveCard();
        sceneCtrl.showBoard();
        clearFields();
    }

    /**
     * @return Card
     */
    private Card getExistingCard() {
        var titleVar = titleOfCard.getText();
        var description = this.description.getText();
        var tasks = new ArrayList<Task>();
        taskComponentCtrls.forEach(ctrl -> tasks.add(ctrl.getTask()));

        card.cardTitle = titleVar;
        card.cardDescription = description;
        card.taskList = tasks;
        return card;
    }

    /**
     * Creates new card on the server
     */
    public void saveCard(){
        try {
            var result = server.updateCard(getExistingCard());
            if(!result.success) {
                sceneCtrl.showError(result.message, "Failed to save card");
            }
        } catch (WebApplicationException e) {
            sceneCtrl.showError(e.getMessage(), "Failed to save card");
        }
    }

    /**
     * Clears all fields
     */
    public void clearFields(){
        titleOfCard.clear();
        description.clear();
        taskBox.getItems().removeIf(x -> true);
        taskTitle.clear();
        taskComponentCtrls.clear();
        card = null;
    }

    /** Sets the id of the list to add the card to */
    public void setCurrentList(CardList cardList) {
        this.cardList = cardList;
    }

    /** Instantiates the view to the card to be edited by setting the ui elements to the specified data.
     * Sets a flag so it's known this is a card to be edited, not created. */
    public void edit(Card card) {
        setCard(card);
    }

    private void setCard(Card card) {
        this.card = card;

        titleOfCard.setText(card.cardTitle);
        titleOfCard.positionCaret(titleOfCard.getLength());
        description.setText(card.cardDescription);
        description.positionCaret(description.getLength());
        for(var task : card.taskList) {
            addTaskToUI(task);
        }
        registerForMessages();
    }

    private void addTaskToUI(Task task) {
        var taskPair = fxml.load(TaskComponentCtrl.class, "client", "scenes", "components", "TaskComponent.fxml");
        taskBox.getItems().add(taskPair.getValue());
        var ctrl = taskPair.getKey();

        taskComponentCtrls.add(ctrl);
        task.card = card;
        ctrl.setTask(task);
    }

    /** Adds a task from the title set in the text box above. */
    public void addTask() {
        var title = taskTitle.getText();
        if(title.isBlank()) {
            return;
        }
        taskTitle.clear();
        var task = new Task(idGenerator.generateID(), title, false);
        task.card = card;
        task.cardId = card.cardID;
        addTaskToUI(task);
        saveCard();
    }

    /** Deletes the task this component controls */
    public void deleteTask(TaskComponentCtrl taskComponentCtrl) {
        var index = taskComponentCtrls.indexOf(taskComponentCtrl);
        taskComponentCtrls.remove(index);
        taskBox.getItems().remove(index);
        saveCard();
    }

    public void dragDroppedCard(DragEvent event) {
        event.setDropCompleted(false);
        event.consume();
        refresh();
    }

    /**
     * @param event The drag event that triggered the drag over
     *
     *              Dragging over a card component enables data transfer
     */
    public void dragOver(DragEvent event){
        Dragboard db = event.getDragboard();
        if (db.hasString() && db.getString().split(" ")[0].equals("task")) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            event.consume();
        }
    }

    public void dragDroppedList(DragEvent event) {
        System.out.println("Drag drop detected");
        var dragboard = event.getDragboard();
        var strings = dragboard.getString().split(" ");
        var success = false;

        // If the dragboard has a string, then the card was dragged from another list
        if(dragboard.hasString() && strings[0].equals("task")){
            var taskResult = server.getTask(UUID.fromString(strings[1]));

            if(taskResult.success){
                var taskDragged = taskResult.value;

                System.out.println("Dropped task " + taskDragged.taskID
                        + " on card " + card.cardID);

                var indexTo = (card.taskList.size() - 1);
                System.out.println("IndexTo: " +  indexTo);

                // Move the card to the new list
                server.reorderTasks(card.cardID, taskDragged, indexTo);
                success = true;
            }
        }
        event.setDropCompleted(success);
        event.consume();
    }
}

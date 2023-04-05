package client.components;

import client.*;
import client.utils.*;
import com.google.inject.*;
import commons.*;
import commons.utils.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;

import java.util.*;

public class TaskComponentCtrl {
    private final SceneCtrl sceneCtrl;
    private final IDGenerator idGenerator;
    private final ServerUtils server;
    private Task task;
    @FXML
    private TextField taskLabel;
    @FXML
    private CheckBox isCompleted;
    @FXML
    private AnchorPane taskPane;

    @Inject
    public TaskComponentCtrl(SceneCtrl sceneCtrl, IDGenerator idGenerator, ServerUtils server) {
        this.sceneCtrl = sceneCtrl;
        this.idGenerator = idGenerator;
        this.server = server;
    }

    /** Initialises the controller.
     * Adds a listener to the task title focus. If a user stops typing it will automatically save the card. */
    @FXML
    public void initialize() {
        taskLabel.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) {
                saveCard();
            }
        });
    }

    /** Gets the task contained in this controller */
    public Task getTask() {
        var title = taskLabel.getText();
        var isCompleted = this.isCompleted.isSelected();

        task.taskTitle = title;
        task.isCompleted = isCompleted;
        return task;
    }

    /** Sets the UI components to the specified task */
    public void setTask(Task task) {
        taskLabel.setText(task.taskTitle);
        taskLabel.positionCaret(taskLabel.getLength());
        isCompleted.setSelected(task.isCompleted);
        this.task = task;
    }

    /** Saves the card this task is connected to */
    public void saveCard() {
        sceneCtrl.saveCard();
    }

    /** Deletes this task from the card */
    public void deleteTask() {
        sceneCtrl.deleteTask(this);
    }

    /**
     * @param event The mouse event that triggered the drag
     *
     *              This method is called when the user drags a card component
     */
    public void dragDetected(MouseEvent event) {

        System.out.println("Drag detected");
        Dragboard db = taskPane.startDragAndDrop(TransferMode.COPY_OR_MOVE);

        SnapshotParameters params = new SnapshotParameters();

        params.setFill(Color.TRANSPARENT);
        WritableImage image = taskPane.snapshot(params, null);

        db.setDragView(image,event.getX(),event.getY());

        ClipboardContent content = new ClipboardContent();
        content.putString("task " + task.taskID);
        db.setContent(content);

        event.consume();
    }

    /**
     * Tells status of dragging
     * @param evt
     */
    public void setOnDragDone(DragEvent evt){
        if (evt.getTransferMode() == null) {
            System.out.println("drag aborted");
        } else {
            System.out.println("drag successfully completed");
        }
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


    /**
     * @param event The drag event that triggered the drop
     * <p>
     *             This method is called when the user drops a card
     *             component on another card component.
     */
    public void dragDrop(DragEvent event){
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
                        + " on task " + task.taskID
                        + " in card " + task.cardId);

                // Index to print is the index of the card in the list
                int indexTo = task.card.taskList.indexOf(task);

                System.out.println("IndexTo: " +  indexTo);

                // Move the card to the new list
                server.reorderTasks(task.cardId, taskDragged, indexTo);
                success = true;
            }
        }
        event.setDropCompleted(success);
        event.consume();
    }
}

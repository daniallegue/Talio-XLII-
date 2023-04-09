package client.components;

import client.MultiboardCtrl;
import client.SceneCtrl;
import client.scenes.BoardsOverviewCtrl;
import client.utils.MyFXML;
import client.utils.ServerUtils;
import commons.Board;
import commons.Result;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javax.inject.Inject;
import java.util.UUID;

public class BoardCardPreviewCtrl {

    private MyFXML fxml;
    private SceneCtrl sceneCtrl;
    private BoardsOverviewCtrl boardsOverviewCtrl;
    private MultiboardCtrl multiBoardCtrl;
    private ServerUtils server;

//    private List<PreviewTagComponentCtrl> tagComponents;

    @FXML
    Label boardTitle;
    @FXML
    Label boardDescription;

//    @FXML
//    HBox tagBox;

    // replace with icon
//    @FXML
//    Label pwdProtected;
    private Board board;


    @Inject
    public BoardCardPreviewCtrl(SceneCtrl sceneCtrl,
                                BoardsOverviewCtrl boardsOverviewCtrl,
                                MultiboardCtrl multiBoardCtrl,
                                ServerUtils server,
                                MyFXML fxml) {
        this.multiBoardCtrl = multiBoardCtrl;
        this.fxml = fxml;
        this.boardsOverviewCtrl = boardsOverviewCtrl;
        this.sceneCtrl = sceneCtrl;
        this.server = server;
//        this.tagComponents = new ArrayList<>();
    }

    /**
     * @return the board id
     */
    public UUID getBoardId() {
        return this.board.boardID;
    }

    /**
     * @return the board
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * @param board the board to be set
     */
    public void setBoard(Board board) {
        this.board = board;
    }


    /**
     * @param board the board to be displayed
     *
     *              Sets the content of the board card
     */
    public void setContent(Board board) {
        this.board = board;
        boardTitle.setText(board.boardTitle);
        boardDescription.setText(board.description);
//        tagBox.getChildren().clear();
//        for(Tag tag : board.tagList){
//            addTagToUI(tag);
//        }


    }

    /**
     * Opens the board when the user clicks on it
     */
    public void openBoard(MouseEvent event) {
        multiBoardCtrl.openBoard(this.board.boardID);
        //System.out.println(event.getSource());
    }

    /**
     * Deletes the board if the user is an admin
     * or leaves the board if the user is not an admin
     */
    public void leaveOrDeleteBoard() {
        // authentication and password protection
        // should be checked here
        if(boardsOverviewCtrl.isAdmin){
            System.out.println("Deleting board with id:\t" + board.getBoardID());
            Result<Object> res = server.deleteBoard(this.board.boardID, this.board);
            if(res.success){
                boardsOverviewCtrl.deleteBoardLocal(this.board.boardID);
            }
            else{
                sceneCtrl.showError(res.message, "Error deleting board");
            }
        }else{
            System.out.println("Leaving board with id:\t" + board.getBoardID());
            boardsOverviewCtrl.deleteBoardLocal(this.board.boardID);
        }
    }

    /**
     * Opens the edit board scene
     */
    public void editBoard(MouseEvent event) {
        System.out.println("Editing board with id\t" + this.board.boardID);
        sceneCtrl.showEditBoardPopup(this.board);
    }
//
//    /**
//     * Goes to add new card scene
//     */
//    public void addTagToUI(Tag tag) {
//        var tagPair = fxml.load(PreviewTagComponentCtrl.class, "client", "scenes", "components", "PreviewTags.fxml");
//        tagBox.getChildren().add(tagPair.getValue());
//        var ctrl = tagPair.getKey();
//        ctrl.setTag(tag);
//        ctrl.setBoard(board);
//    }


}

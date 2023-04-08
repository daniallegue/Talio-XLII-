package client.scenes;

import client.MultiboardCtrl;
import client.SceneCtrl;
import client.components.BoardCardPreviewCtrl;
import client.components.BoardTagComponentCtrl;
import client.interfaces.InstanceableComponent;
import client.utils.ConnectionCtrl;
import client.utils.MyFXML;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Result;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Pair;
import org.springframework.messaging.simp.stomp.StompSession;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BoardsOverviewCtrl implements InstanceableComponent {

    private final MultiboardCtrl multiboardCtrl;
    private final ConnectionCtrl connectionCtrl;
    private List<BoardCardPreviewCtrl> boardCardPreviewCtrls;

    private List<UUID> localBoards;
    private ServerUtils server;
    private MyFXML fxml;
    private SceneCtrl sceneCtrl;
    private Color connectedColor = Color.web("#34faab",1.0);
    private Color disConnectedColor= Color.web("ff6f70",1.0);
    private List<VBox> vboxList = new ArrayList<>();
    public Boolean isAdmin = false;
    private StompSession.Subscription subscription;


    @FXML
    TextField connectionString;

    @FXML
    Button disConnectButton;

    @FXML
    Button joinButton;

    @FXML
    VBox box1;

    @FXML
    VBox box2;
    @FXML
    VBox box3;

    @FXML
    Circle status;

    @FXML
    Button adminButton;

    @FXML
    Button createButton;

    @FXML
    Label mcText;
    @FXML
    Label xlii;
    @FXML
    Label connectText;
    @FXML
    Label adminIndicator;

    @Inject
    public BoardsOverviewCtrl(ServerUtils server, MyFXML fxml, SceneCtrl sceneCtrl, MultiboardCtrl multiboardCtrl,
                              ConnectionCtrl connectionCtrl) {
        this.server = server;
        this.fxml = fxml;
        this.sceneCtrl = sceneCtrl;
        this.multiboardCtrl = multiboardCtrl;
        this.connectionCtrl = connectionCtrl;
        this.boardCardPreviewCtrls = new ArrayList<>();
    }

    /**
     * Initializes the vboxes for the previews
     */
    public void initialize(){
        vboxList.add(box1);
        vboxList.add(box2);
        vboxList.add(box3);
        createButton.setVisible(false);
        joinButton.setVisible(false);
        adminButton.setVisible(false);
    }

    /**
     * triggers the admin login form window to open
     */
    public void adminLogin(){
        if(isAdmin){
            setAdmin(false);
            refresh();
        }else{
            sceneCtrl.showAdminLoginPopup();
        }
    }

    /**
     * @param boardID the id of the board to be deleted
     *                this method is called when a board is deleted
     *                it removes the preview controller of the board
     *                from the list of preview controllers
     *                The effect of this method only has a local scope
     *                (client side
     */
    public void deleteBoardLocal(UUID boardID) {
        boardCardPreviewCtrls.removeIf(boardCardPreviewCtrl -> boardCardPreviewCtrl.getBoardId().equals(boardID));
        multiboardCtrl.deleteBoard(boardID);
        refresh();
    }


    /**
     * @param board the board to be added to the list of boards
     *              this method is called when a board is updated
     */
    public void updateBoard(Board board) {
        for (BoardCardPreviewCtrl boardCardPreviewCtrl : boardCardPreviewCtrls) {
            if (boardCardPreviewCtrl.getBoardId().equals(board.boardID)) {
                boardCardPreviewCtrl.setBoard(board);
            }
        }
        Result<Board> res = server.updateBoard(board);
        if(res.success){
            refresh();
        }
        else {
            sceneCtrl.showError(res.message, "Failed to update board");
        }
    }

    /** Tries to connect to the server filled in the text box and create a websocket,
     * f it fails it creates a popup showing the error */
    public void connectToServer(){
        if(!server.isConnected){
            if(connectionCtrl.connect(connectionString.getText()).equals(Result.SUCCESS)){
                registerForMessages();
                refresh();
                disConnectButton.setText("Disconnect");
                createButton.setVisible(true);
                adminButton.setVisible(true);
                joinButton.setVisible(true);
                mcText.setVisible(false);
                xlii.setVisible(false);
                connectText.setVisible(false);
                status.setFill(connectedColor);
            };
        }else{
            unregisterForMessages();
            connectionCtrl.disconnect();
            status.setFill(disConnectedColor);
            mcText.setVisible(true);
            xlii.setVisible(true);
            connectText.setVisible(true);
            createButton.setVisible(false);
            adminButton.setVisible(false);
            joinButton.setVisible(false);
            disConnectButton.setText("Connect");
            clearPreviews();
            this.boardCardPreviewCtrls = new ArrayList<>();

        }
    }

    /**
     * Clears all Previews
     */
    public void clearPreviews(){
        for (VBox vbox:vboxList) {
            vbox.getChildren().clear();
        };
    }

    /**
     *  loads the multiboard overview preview cards
     */
    @Override
    public void refresh() {
        System.out.println("Refreshing board overview");
        clearPreviews();
        if(isAdmin){
            Result<List<Board>> result = server.getAllBoards();
            if(result.success){
                List<Board> allBoards = result.value;
                renderPreviews(allBoards);
            }else{
                sceneCtrl.showError("Error while trying to retrieve board previews","Load error");
            }
        }else{
            this.localBoards = multiboardCtrl.loadBoards();
            List<Board> boardList = new ArrayList<>();
            for(UUID boardId:this.localBoards){
                Result<Board> result = server.getBoard(boardId);
                if(result.success) {
                    boardList.add(result.value);
                } else if (result.code == 26) {
                    continue;
                } else{
                    sceneCtrl.showError("Error while trying to retrieve board previews","Load error");
                }
            }
            renderPreviews(boardList);
        }
    }

    /** Renders the previews of all boards in the list supplied
     * @param boardList
     */
    public void renderPreviews(List<Board> boardList){
        int listIndex = 0;
        VBox vbox;
        for (Board board: boardList) {
            Pair<BoardCardPreviewCtrl, Parent> boardPair = fxml.load(BoardCardPreviewCtrl.class, "client", "scenes",
                    "components", "BoardCardPreview.fxml");
            vbox = vboxList.get(listIndex);
            BoardCardPreviewCtrl boardCardPreviewCtrl = boardPair.getKey();
            boardCardPreviewCtrl.setContent(board);
            vbox.getChildren().add(boardPair.getValue());
            boardCardPreviewCtrls.add(boardCardPreviewCtrl);
            listIndex++;
            if(listIndex >= 3){
                listIndex =0;
            }
        };
    }

    /**
     * @param actionEvent event that triggers the method
     *                    creates a new board and loads it into the multiboard
     */
    public void createBoard(ActionEvent actionEvent) {
        sceneCtrl.showCreateBoardPopup();
        System.out.println("Creating new board");
    }


    /** Shows the dialog to join a board using an invite link */
    public void joinViaLink(ActionEvent actionEvent) {
        sceneCtrl.showJoinBoard();
    }

    /**
     * Stops long polling
     */
    public void stopPolling() {
        server.stopPolling();
    }

    /** Enables admin mode
     * @param isAdmin
     */
    public void setAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
        adminIndicator.setVisible(isAdmin);
        if(isAdmin){
            System.out.println("Admin mode is enabled");
            adminButton.setText("Log out");
            refresh();
        }else{
            System.out.println("Admin mode is disabled");
            adminButton.setText("Admin log in");
        }
    }

    @Override
    public void registerForMessages() {
        unregisterForMessages();
        System.out.println("Board overview registered for messaging");
        subscription = server.registerForMessages("/topic/update-overview/", UUID.class, payload ->{
            try {
                System.out.println("Endpoint \"/topic/update-overview/\"");
                // Needed to prevent threading issues
                Platform.runLater(() -> refresh());
            }catch (Exception e){
                System.out.println(e);
            }
        });
    }

    @Override
    public void unregisterForMessages() {
        if (subscription != null) {
            subscription.unsubscribe();
            System.out.println("Board overview unregistered for messaging");
        }
    }

    public void deleteTag(BoardTagComponentCtrl boardTagComponentCtrl) {

    }
}

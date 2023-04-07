package client.scenes;

import client.*;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Theme;
import commons.utils.IDGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class CustomizeBoardCtrl {

    public static Theme baseTheme;
    private final ServerUtils server;

    private final SceneCtrl sceneCtrl;

    private Board board;
    private final IDGenerator idGenerator;

    //This needs to be decided by the team
//    public final static Theme reset = new Theme("#2A2A2A", "#1b1b1b","#280DF2", "#00ffd1");

    @FXML
    public ListView<String> presetList;
    @FXML
    public ColorPicker listFont;
    @FXML
    public ColorPicker listBackground;
    @FXML
    public ColorPicker boardFont;
    @FXML
    public ColorPicker boardBackground;

    @FXML
    public ColorPicker cardFontH;
    @FXML
    public ColorPicker cardBackgroundH;
    @FXML
    public ColorPicker cardFontNormal;
    @FXML
    public ColorPicker cardBackgroundNormal;
    @FXML
    public ColorPicker cardFontLow;
    @FXML
    public ColorPicker cardBackgroundLow;

    @FXML
    private Label boardName;
    @FXML
    public TextField presetName;


    @Inject
    public CustomizeBoardCtrl(ServerUtils server, SceneCtrl sceneCtrl, Board board, IDGenerator idGenerator) {
        this.server = server;
        this.sceneCtrl = sceneCtrl;
        this.board = board;
        this.idGenerator = idGenerator;
        baseTheme = new Theme("base","#FF00FF",
                "#40E0D0","#40E0D0",
                "#FFDB58","#FFDB58",
                "#FF00FF","#FF00FF",
                "#00ffd1","#2A2A2A","#FF00FF");
    }

    public void loadPresets() {


    }
    public void savePreset() {
        String name = presetName.getText();
        presetName.clear();
        if(name.isEmpty()){
            sceneCtrl.showError("Please enter a name for the preset", "No name entered");
        }
        else {
            Theme theme = new Theme(name,
                    boardBackground.getValue().toString(),boardFont.getValue().toString(),
                    listBackground.getValue().toString(),listFont.getValue().toString(),
                    cardBackgroundNormal.getValue().toString(),cardFontNormal.getValue().toString(),
                    cardBackgroundH.getValue().toString(),cardFontH.getValue().toString(),
                    cardBackgroundLow.getValue().toString(),cardFontLow.getValue().toString());
            presetList.getItems().add(name);
            board.setBoardTheme(theme);
            server.updateBoardTheme(theme, board.boardID);
            sceneCtrl.showBoard();
        }
    }



    /**
     * Retrieves the values for the new Theme, updates the board and returns to board overview.
     */
    public void save() {
        savePreset();


//        Theme newTheme = new Theme(backgroundColor.getValue().toString(),
//                cardColor.getValue().toString(), fontColor.getValue().toString(),listColor.getValue().toString());
//        board.setBoardTheme(newTheme);
//        server.updateBoard(this.board,board.boardID);
//        //Should eventually return to board overview, not list overview
//        sceneCtrl.showBoard();
    }

    /**
     * Closes the customization operation, returns to the board overview scene
     */
    public void close() {
        sceneCtrl.showBoard(); //Should show the board overview
    }

    /**
     * Resets the values of the color pickers to the inital values
     * defined in the application
     */
    public void resetValues(){
//        this.backgroundColor.setValue(Color.web(reset.backgroundColor));
//        this.cardColor.setValue(Color.web(reset.cardColor));
//        this.fontColor.setValue(Color.web(reset.textColor));
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
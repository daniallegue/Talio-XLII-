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
        baseTheme = new Theme("base",
                "#2A2A2A", "#40E0D0",
                "#1b1b1b", "#40E0D0",
                "#FFDB58", "#FF00FF",
                "#2A2A2A", "#00ffd1",
                "#2A2A2A","#FF00FF");
    }

    public void loadPresets() {


    }
    public void savePreset() {
//        String name = presetName.getText();
//        presetName.clear();
//        if(name.isEmpty()){
//            sceneCtrl.showError("Please enter a name for the preset", "No name entered");
//        }
        Theme theme = new Theme("name",
                boardBackground.getValue().toString().replaceAll("0x", "#"),boardFont.getValue().toString().replaceAll("0x", "#"),
                listBackground.getValue().toString().replaceAll("0x", "#"),listFont.getValue().toString().replaceAll("0x", "#"),
                cardBackgroundNormal.getValue().toString().replaceAll("0x", "#"),cardFontNormal.getValue().toString().replaceAll("0x", "#"),
                cardBackgroundH.getValue().toString().replaceAll("0x", "#"),cardFontH.getValue().toString().replaceAll("0x", "#"),
                cardBackgroundLow.getValue().toString().replaceAll("0x", "#"),cardFontLow.getValue().toString().replaceAll("0x", "#"));
        board.setBoardTheme(theme);
        server.updateBoardTheme(theme, board.boardID);
        sceneCtrl.showBoard();
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
    public void resetAllValues(){
        resetBoardValues();
        resetListValues();
        resetCardValues();
    }

    public void resetBoardValues(){
        boardBackground.setValue(Color.valueOf(baseTheme.boardBackgroundColor));
        boardFont.setValue(Color.valueOf(baseTheme.boardFont));
    }
    public void resetListValues(){
        listBackground.setValue(Color.valueOf(baseTheme.listBackgroundColor));
        listFont.setValue(Color.valueOf(baseTheme.listFont));
    }
    public void resetCardValues(){
        cardBackgroundNormal.setValue(Color.valueOf(baseTheme.cardBackgroundColorNormal));
        cardFontNormal.setValue(Color.valueOf(baseTheme.cardFontNormal));
        cardBackgroundH.setValue(Color.valueOf(baseTheme.cardBackgroundColorHighlighted));
        cardFontH.setValue(Color.valueOf(baseTheme.cardFontHighlighted));
        cardBackgroundLow.setValue(Color.valueOf(baseTheme.cardBackgroundColorLow));
        cardFontLow.setValue(Color.valueOf(baseTheme.cardFontLow));
    }

    public void setBoard(Board board) {
        this.board = board;
        boardBackground.setValue(Color.valueOf(board.boardTheme.boardBackgroundColor));
        boardFont.setValue(Color.valueOf(board.boardTheme.boardFont));
        listBackground.setValue(Color.valueOf(board.boardTheme.listBackgroundColor));
        listFont.setValue(Color.valueOf(board.boardTheme.listFont));
        cardBackgroundNormal.setValue(Color.valueOf(board.boardTheme.cardBackgroundColorNormal));
        cardFontNormal.setValue(Color.valueOf(board.boardTheme.cardFontNormal));
        cardBackgroundH.setValue(Color.valueOf(board.boardTheme.cardBackgroundColorHighlighted));
        cardFontH.setValue(Color.valueOf(board.boardTheme.cardFontHighlighted));
        cardBackgroundLow.setValue(Color.valueOf(board.boardTheme.cardBackgroundColorLow));
        cardFontLow.setValue(Color.valueOf(board.boardTheme.cardFontLow));

    }
}
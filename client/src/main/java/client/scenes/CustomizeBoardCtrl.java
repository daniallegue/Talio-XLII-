package client.scenes;

import client.*;
import client.utils.MyFXML;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Theme;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class CustomizeBoardCtrl {

    public static Theme baseTheme;
    private final ServerUtils server;

    private final SceneCtrl sceneCtrl;


    private Board board;


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
    private Label customizeText;
    @FXML
    public TextField presetName;

    public List<Theme> localPresets;

    private MyFXML fxml;

    @FXML
    public ListView<String> presetList;



    @Inject
    public CustomizeBoardCtrl(ServerUtils server, SceneCtrl sceneCtrl, Board board, MyFXML fxml) {
        this.server = server;
        this.fxml = fxml;
        this.sceneCtrl = sceneCtrl;
        this.board = board;
        baseTheme = new Theme("base",
                "#2A2A2A", "#40E0D0",
                "#1b1b1b", "#40E0D0",
                "#FFDB58", "#FF00FF",
                "#2A2A2A", "#00ffd1",
                "#2A2A2A","#FF00FF");
    }


    /**
     * Retrieves the values for the new Theme, updates the board and returns to board overview.
     */
    public void savePreset() {
        String name = presetName.getText();
        presetName.clear();
        if(name.isEmpty()){
            sceneCtrl.showError("Please enter a name for the preset", "No name entered");
        }
        else{
            Theme theme = new Theme(name,
                    boardBackground.getValue().toString().replaceAll("0x", "#"),
                    boardFont.getValue().toString().replaceAll("0x", "#"),

                    listBackground.getValue().toString().replaceAll("0x", "#"),
                    listFont.getValue().toString().replaceAll("0x", "#"),

                    cardBackgroundH.getValue().toString().replaceAll("0x", "#"),
                    cardFontH.getValue().toString().replaceAll("0x", "#"),

                    cardBackgroundNormal.getValue().toString().replaceAll("0x", "#"),
                    cardFontNormal.getValue().toString().replaceAll("0x", "#"),

                    cardBackgroundLow.getValue().toString().replaceAll("0x", "#"),
                    cardFontLow.getValue().toString().replaceAll("0x", "#"));
            localPresets.add(theme);
            presetList.getItems().add(theme.themeName);
            saveTheme(theme);
        }

    }

    /**
     * Saves the preset theme locally.
     */
    public void setPresetDefault() {
        File file = new File("defaultPreset");
        try {
            Theme def = getTheme(presetList.getSelectionModel().getSelectedItem());
            if(def == null){
                return;
            }
            if (file.exists()) {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));

                oos.writeObject(def);
                oos.close();
                loadDefault();
            } else {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(def);
                oos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a default theme if it exists
     */
    public void loadDefault(){

        File file = new File("defaultPreset");
        try {
            if (file.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                Theme defaultTheme = (Theme) ois.readObject();
                ois.close();
                baseTheme = defaultTheme;
                setColorPickers(baseTheme);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private Theme getTheme(String selectedItem) {
        if(localPresets == null){
            localPresets = loadPresets();
        }
        for (int i = 0; i < localPresets.size(); i++) {
            if(localPresets.get(i).themeName.equals(selectedItem)){
                return localPresets.get(i);
            }
        }
        return null;
    }

    private void saveTheme(Theme theme) {

        File file = new File("localPresets");

        try {
            if (file.exists()) {
                localPresets = loadPresets();
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                localPresets.add(theme);
                oos.writeObject(localPresets);
                oos.close();
                loadPresets();
            } else {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                localPresets = new ArrayList<>();
                localPresets.add(theme);
                oos.writeObject(localPresets);
                oos.close();
            }
            System.out.println("Saved new preset to local storage, there are now " +
                    localPresets.size() + " presets saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return a list of all the local presets
     * @throws IOException if the file cannot be opened
     * @throws ClassNotFoundException if the file is not found
     *
     * Loads all the local presets from the localPresets file
     */
    public List<Theme> loadPresets() {

        ArrayList<Theme> localPresets = new ArrayList<>();

        File file = new File("localPresets");

        if (file.exists()) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                localPresets = (ArrayList<Theme>) ois.readObject();
                this.localPresets = localPresets;
                ois.close();

                presetList.getItems().clear();
                for (Theme theme : localPresets) {
                    presetList.getItems().add(theme.themeName);
                }



            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return localPresets;
    }

    /**
     * Loads the selected preset into the color pickers
     */
    public void loadSelected() {
        var selected = presetList.getSelectionModel().getSelectedItem();
        if(localPresets == null){
            localPresets = loadPresets();
        }
        else{
            for (int i = 0; i < localPresets.size(); i++) {
                if(localPresets.get(i).themeName.equals(selected)){
                    Theme theme = localPresets.get(i);
                    boardBackground.setValue(Color.web(theme.boardBackgroundColor));
                    boardFont.setValue(Color.web(theme.boardFont));

                    listBackground.setValue(Color.web(theme.listBackgroundColor));
                    listFont.setValue(Color.web(theme.listFont));

                    cardBackgroundH.setValue(Color.web(theme.cardBackgroundColorHighlighted));
                    cardFontH.setValue(Color.web(theme.cardFontHighlighted));

                    cardBackgroundNormal.setValue(Color.web(theme.cardBackgroundColorNormal));
                    cardFontNormal.setValue(Color.web(theme.cardFontNormal));

                    cardBackgroundLow.setValue(Color.web(theme.cardBackgroundColorLow));
                    cardFontLow.setValue(Color.web(theme.cardFontLow));
                }

            }
        }
    }

    /**
     * Retrieves the values for the new Theme, updates the board and returns to board overview.
     */
    public void save() {
        Theme theme = new Theme("name",
                boardBackground.getValue().toString().replaceAll("0x", "#"),
                boardFont.getValue().toString().replaceAll("0x", "#"),

                listBackground.getValue().toString().replaceAll("0x", "#"),
                listFont.getValue().toString().replaceAll("0x", "#"),

                cardBackgroundH.getValue().toString().replaceAll("0x", "#"),
                cardFontH.getValue().toString().replaceAll("0x", "#"),

                cardBackgroundNormal.getValue().toString().replaceAll("0x", "#"),
                cardFontNormal.getValue().toString().replaceAll("0x", "#"),

                cardBackgroundLow.getValue().toString().replaceAll("0x", "#"),
                cardFontLow.getValue().toString().replaceAll("0x", "#"));

        board.setBoardTheme(theme);
        server.updateBoardTheme(theme, board.boardID);
        sceneCtrl.showBoard();

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

    /**
     * Resets the values of the color pickers for boards to the inital values
     */
    public void resetBoardValues(){
        boardBackground.setValue(Color.valueOf(baseTheme.boardBackgroundColor));
        boardFont.setValue(Color.valueOf(baseTheme.boardFont));
    }

    /**
     * Resets the values of the color pickers for lists to the inital values
     */
    public void resetListValues(){
        listBackground.setValue(Color.valueOf(baseTheme.listBackgroundColor));
        listFont.setValue(Color.valueOf(baseTheme.listFont));
    }

    /**
     * Resets the values of the color pickers for cards to the inital values
     */
    public void resetCardValues(){
        cardBackgroundNormal.setValue(Color.valueOf(baseTheme.cardBackgroundColorNormal));
        cardFontNormal.setValue(Color.valueOf(baseTheme.cardFontNormal));
        cardBackgroundH.setValue(Color.valueOf(baseTheme.cardBackgroundColorHighlighted));
        cardFontH.setValue(Color.valueOf(baseTheme.cardFontHighlighted));
        cardBackgroundLow.setValue(Color.valueOf(baseTheme.cardBackgroundColorLow));
        cardFontLow.setValue(Color.valueOf(baseTheme.cardFontLow));
    }

    /**
     * @param board the board to be customized
     *              Sets the color pickers to the current values of the board theme
     */
    public void setBoard(Board board) {
        this.board = board;
        customizeText.setText("Customize board: " + board.boardTitle);
        setColorPickers(board.boardTheme);
    }

    private void setColorPickers(Theme theme) {
        boardBackground.setValue(Color.web(theme.boardBackgroundColor));
        boardFont.setValue(Color.web(theme.boardFont));

        listBackground.setValue(Color.web(theme.listBackgroundColor));
        listFont.setValue(Color.web(theme.listFont));

        cardBackgroundH.setValue(Color.web(theme.cardBackgroundColorHighlighted));
        cardFontH.setValue(Color.web(theme.cardFontHighlighted));

        cardBackgroundNormal.setValue(Color.web(theme.cardBackgroundColorNormal));
        cardFontNormal.setValue(Color.web(theme.cardFontNormal));

        cardBackgroundLow.setValue(Color.web(theme.cardBackgroundColorLow));
        cardFontLow.setValue(Color.web(theme.cardFontLow));
    }


    /**
     * Removes the selected preset from the list of presets
     */
    public void removeSelected() {
        var selected = presetList.getSelectionModel().getSelectedItem();
        if(localPresets == null){
            localPresets = loadPresets();
        }
        else{
            for (int i = 0; i < localPresets.size(); i++) {
                if(localPresets.get(i).themeName.equals(selected)){
                    localPresets.remove(i);
                    saveCurrentPresets();
                    loadPresets();
                }
            }
        }

    }

    private void saveCurrentPresets() {
        File file = new File("localPresets");

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(localPresets);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
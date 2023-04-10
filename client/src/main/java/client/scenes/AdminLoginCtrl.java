package client.scenes;

import client.SceneCtrl;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class AdminLoginCtrl {
    private final ServerUtils server;

    private final SceneCtrl sceneCtrl;

    // VERY bad for cybersecurity
    private String adminPassword;

    @FXML
    private TextField password;

    @Inject
    public AdminLoginCtrl(ServerUtils server, SceneCtrl sceneCtrl) {
        this.server = server;
        this.sceneCtrl = sceneCtrl;
    }


    /**
     *Method to login as admin in order to enable admin features
     */
    public void login() {
        if(adminPassword.equals(password.getText())){
            sceneCtrl.showMultiboard(true);
            password.clear();
        }else{
            System.out.println("Password:\t" + adminPassword + "\tInput:\t" + password.getText());
            sceneCtrl.showError("Password incorrect","Authentication failed");
        }
    }

    /**
     * Reset the password for the server
     */
    public void resetPassword(){
        server.resetPassword();
        sceneCtrl.showError("Password has been reset","Reset password");
    }


    /**
     * On click, the user is directed to the main page, the multiboard overview
     */
    public void close() {
        sceneCtrl.showMultiboard();
        password.clear();
    }


    /** Sets the password
     * @param password
     */
    public void setPassword(String password) {
        this.adminPassword = password;
    }

}

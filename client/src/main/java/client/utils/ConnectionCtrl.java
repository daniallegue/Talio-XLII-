package client.utils;

import client.*;
import client.scenes.AdminLoginCtrl;
import com.google.inject.*;
import commons.*;
import org.springframework.messaging.converter.*;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.*;
import org.springframework.web.socket.messaging.*;

import java.util.concurrent.*;

public class ConnectionCtrl {

    private AdminLoginCtrl adminLoginCtrl;
    private ServerUtils server;
    private SceneCtrl sceneCtrl;
    private MultiboardCtrl multiboardCtrl;
    private String serverUrl;
    private StompSession session;


    /** Initialises the controller using dependency injection */
    @Inject
    public ConnectionCtrl(ServerUtils server, SceneCtrl sceneCtrl, MultiboardCtrl multiboardCtrl,
                          AdminLoginCtrl adminLoginCtrl) {
        this.server = server;
        this.sceneCtrl = sceneCtrl;
        this.multiboardCtrl = multiboardCtrl;
        this.adminLoginCtrl = adminLoginCtrl;
    }

    /** Tries to connect to the server filled in the text box and create a websocket,
     * f it fails it creates a popup showing the error */
    public Result connect(String serverUrl) {
        if(serverUrl.isEmpty()){
            this.serverUrl = "http://localhost:8080/";

        }else{
            this.serverUrl = serverUrl;
        }

        //The following code should not need a try catch block right?
        // Since the error handling is managed with the Result objects
        try {
            server.setServer(this.serverUrl);

            Result<Object> serverConnectionResult = server.connect();
            Result<Object> webSocketConnectionResult = startWebsocket();

            if (!serverConnectionResult.success) {
                sceneCtrl.showError(serverConnectionResult.message, "Can't reach server");
            }else if(!webSocketConnectionResult.success){
                sceneCtrl.showError(webSocketConnectionResult.message, "Failed to start websocket");
            } else {
                multiboardCtrl.setWorkspaceKey(this.serverUrl);
                System.out.println("*Adjusts hacker glasses* I'm in");
                sceneCtrl.showMultiboard();
                server.isConnected = true;
                //Not the appropriate place, but will do for the moment
                adminLoginCtrl.setPassword(server.getPassword().value);
                server.checkPasswordChange(password->{
                    adminLoginCtrl.setPassword(password);
                });
                System.out.println("*Adjusts hacker glasses* I'm in");
                return Result.SUCCESS;
            }
        } catch (RuntimeException e) {
            sceneCtrl.showError(e.getMessage(), "Failed to connect");
        }
        return Result.FAILED_TO_CONNECT_TO_SERVER;
    }

    /** Creates a websocket session using Stomp protocol and sets it in serverUtils
     * @return result Result Object containing status and a payload from the server
     */
    public Result<Object> startWebsocket(){
        System.out.println("Websocket connection established");
        String url = "ws://" + this.serverUrl.split("//")[1]  +"/websocket";
        StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        try{
            this.session = stompClient.connect(url, new StompSessionHandlerAdapter(){}).get();
            server.setSession(session);
            return Result.SUCCESS;
        }catch (ExecutionException | InterruptedException e){
            return Result.FAILED_WEBSOCKET_CONNECTION.of(e);
        }
    }

    /**
     * Disconnects the websockets and the server
     */
    public void disconnect(){
        server.disconnect();
        session.disconnect();
    }

}

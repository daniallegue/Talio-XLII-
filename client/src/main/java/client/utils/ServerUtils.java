/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import commons.*;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {

    public boolean isConnected = false;
    private String serverUrl;
    private StompSession session;

    private ExecutorService EXEC;

    //Getters - Setters

    /**
     * setter for serverUrl
     *
     * @param serverUrl
     */
    public void setServer(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * getter for serverUrl
     *
     * @return serverUrl String
     */
    public String getServerUrl() {
        return serverUrl;
    }

    /**
     * sets the stompSession from the connectionHandler
     *
     * @param stompSession websocket session
     */
    public void setSession(StompSession stompSession) {
        this.session = stompSession;
    }

    /**
     * Tries to "connect" to a server by trying to see if the server exists.
     */
    public Result<Object> connect() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverUrl).path("/api/status/")
                .request(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    /**
     * Post request to add the CardList list to the server repository
     *
     * @return Result Object containing status and an empty payload
     */
    public Result<CardList> addList(CardList list) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverUrl).path("api/list/create/") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(list, APPLICATION_JSON), new GenericType<>() {
                });
    }

    /**
     * Get request to get all the CardLists from the server repository
     *
     * @return Result Object containing status and an empty payload
     */
    public Result<List<CardList>> getLists() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverUrl).path("api/list/get-all/") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    /**
     * @param cardID the id of the card to get
     * @return the card with the given id
     */
    public Result<Card> getCard(UUID cardID) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverUrl).path("api/card/get/" + cardID) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    /**
     * Delete request to delete the CardList with given id from the server repository
     *
     * @param listId id of the list to be deleted
     * @return Result Object containing status and an empty payload
     */
    public Result<Object> deleteList(UUID listId, CardList list) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverUrl).path("api/list/delete/" + listId) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(list, APPLICATION_JSON), new GenericType<>() {
                });
    }

    /**
     * Put request to update the CardList with given id to the CardList list
     *
     * @param list   list Object that needs to be updated on the server
     * @param listId id of that list
     * @return Result Object containing status and an empty payload
     */
    public Result<CardList> editList(CardList list, UUID listId) {
        return ClientBuilder.newClient(new ClientConfig())//
                .target(serverUrl).path("api/list/update/" + listId)//
                .request(APPLICATION_JSON)//
                .accept(APPLICATION_JSON)//
                .put(Entity.entity(list, APPLICATION_JSON), new GenericType<>() {
                });
    }

    /**
     * Put request to remove the card in the request body
     * from the list with the given id
     *
     * @param card   card Object gets deleted
     * @param listId id of that list
     * @return Result Object containing status and an empty payload
     */
    public Result<CardList> removeCardFromList(UUID listId, Card card) {
        return ClientBuilder.newClient(new ClientConfig())//
                .target(serverUrl).path("api/list/delete-card/" + listId)//
                .request(APPLICATION_JSON)//
                .accept(APPLICATION_JSON)//
                .put(Entity.entity(card, APPLICATION_JSON), new GenericType<>() {
                });
    }

    /**
     * Put request to add the card in the request body
     * to the list with the given id
     *
     * @param card   card Object needs to be added
     * @param listId id of the list the card gets added to
     * @return Result Object containing status and an empty payload
     */
    public Result<Card> addCardToList(Card card, UUID listId) {
        return ClientBuilder.newClient(new ClientConfig())//
                .target(serverUrl).path("api/list/add-card/" + listId)//
                .request(APPLICATION_JSON)//
                .accept(APPLICATION_JSON)//
                .put(Entity.entity(card, APPLICATION_JSON), new GenericType<>() {
                });
    }

    /**
     * Put request to move the card in the request body
     * from the list with id = id_from
     * to the list with id = id_to
     *
     * @param card           card Object gets moved
     * @param cardListIdFrom id of origin of card
     * @param cardListIdTo   id of destination of card
     * @return Result Object containing status and an empty payload
     */

    public Result<Card> moveCardBetweenLists(Card card, UUID cardListIdFrom, UUID cardListIdTo, Integer indexTo) {
        return ClientBuilder.newClient(new ClientConfig())//
                .target(serverUrl).path("api/list/move-card/" + cardListIdFrom + "/" + cardListIdTo + "/" + indexTo)//
                .request(APPLICATION_JSON)//
                .accept(APPLICATION_JSON)//
                .put(Entity.entity(card, APPLICATION_JSON), new GenericType<>() {
                });
    }

    /**
     * Put request to add the task in the request body
     * to the card with the given id
     */
    public Result<Card> addTaskToCard(Task task, UUID cardId) {
        return ClientBuilder.newClient(new ClientConfig())//
                .target(serverUrl).path("api/card/add-task/" + cardId)//
                .request(APPLICATION_JSON)//
                .accept(APPLICATION_JSON)//
                .put(Entity.entity(task, APPLICATION_JSON), new GenericType<>() {
                });
    }

    /**
     * Put request to remove the task in the request body
     * from the card with the given id
     *
     * @param task   task Object gets removed
     * @param cardId id of parent Card
     * @return Result Object containing status and an empty payload
     */
    public Result<Card> removeTaskFromCard(Task task, UUID cardId) {
        return ClientBuilder.newClient(new ClientConfig())//
                .target(serverUrl).path("api/card/remove-task/" + cardId)//
                .request(APPLICATION_JSON)//
                .accept(APPLICATION_JSON)//
                .put(Entity.entity(task, APPLICATION_JSON), new GenericType<>() {
                });
    }

    /**
     * Updates the board theme of the board with given id,
     * and saves it in the repository
     */
    public Result<Board> updateBoardTheme(Theme theme,UUID boardId) {
        return ClientBuilder.newClient(new ClientConfig())//
                .target(serverUrl).path("api/board/update-theme/" + boardId)//
                .request(APPLICATION_JSON)//
                .accept(APPLICATION_JSON)//
                .put(Entity.entity(theme, APPLICATION_JSON), new GenericType<>() {
                });
    }


    /**
     * Get request to get the Board from the server repository
     */
    public Result<Board> getBoard(UUID boardID) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverUrl).path("api/board/get/" + boardID)
                .request(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    /**
     * @param boardID id of the board to be deleted
     * @param board  board object to be deleted
     * @return Result Object containing status and an empty payload
     */
    public Result<Object> deleteBoard(UUID boardID, Board board) {
        return ClientBuilder.newClient(new ClientConfig())//
                .target(serverUrl).path("api/board/delete/" + boardID)//
                .request(APPLICATION_JSON)//
                .accept(APPLICATION_JSON)//
                .put(Entity.entity(board, APPLICATION_JSON), new GenericType<>() {
                });
    }

    /**
     * Adding a board on the server
     *
     * @param board board object to be added
     * @return Result Object containing status and board payload
     */
    public Result<Board> addBoard(Board board) {
        //System.out.println("Requesting the server to create a board with id " + board.getBoardID());
        return ClientBuilder.newClient(new ClientConfig())//
                .target(serverUrl).path("api/board/create/")//
                .request(APPLICATION_JSON)//
                .accept(APPLICATION_JSON)//
                .post(Entity.entity(board, APPLICATION_JSON), new GenericType<>() {
                });
    }

    /**
     * Get request to get the Board from the server repository
     */
    public Result<CardList> getList(UUID listID) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverUrl).path("api/list/get/" + listID)
                .request(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    /**
     * Put request to update a card with the same id by adding a tag to the card
     *
     * @param card the card to update
     * @return Result object containing the success status and the updated card
     */
    public Result<Card> addTagToCard(Tag tag, Card card) {
        return ClientBuilder.newClient(new ClientConfig())//
                .target(serverUrl).path("api/card/add-tag/" + card.cardID)//
                .request(APPLICATION_JSON)//
                .accept(APPLICATION_JSON)//
                .put(Entity.entity(tag, APPLICATION_JSON), new GenericType<>() {
                });
    }

    /**
     * Put request to update a BOARD with the same id by adding a tag to the board
     *
     * @param board the card to update
     * @return Result object containing the success status and the updated board
     */
    public Result<Board> addTagToBoard(Tag tag, Board board) {
        return ClientBuilder.newClient(new ClientConfig())//
                .target(serverUrl).path("api/board/add-tag/" + board.boardID)//
                .request(APPLICATION_JSON)//
                .accept(APPLICATION_JSON)//
                .put(Entity.entity(tag, APPLICATION_JSON), new GenericType<>() {
                });
    }



    /**
     * Deletes card with given id from repository and its corresponding list
     */
    public Result<CardList> deleteCard(Card card) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverUrl).path("api/list/delete-card/" + card.cardListId)
                .request(APPLICATION_JSON)
                .put(Entity.entity(card, APPLICATION_JSON), new GenericType<>() {
                });
    }



    /**
     * Updates the tag with the given id
     */
    public Result<Tag> updateTag(UUID tagID, Tag newTag) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverUrl).path("api/tag/update/" + tagID)
                .request(APPLICATION_JSON)
                .put(Entity.entity(newTag, APPLICATION_JSON), new GenericType<>() {
                });
    }

    /**
     * Updates the tag with the given id
     */
    public Result<Tag> updateTagFromBoard(UUID tagID, Tag newTag) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverUrl).path("api/tag/update-from-board/" + tagID)
                .request(APPLICATION_JSON)
                .put(Entity.entity(newTag, APPLICATION_JSON), new GenericType<>() {
                });
    }


    /**
     * @param board the board to update
     * @return Result object containing the success status and the updated board
     */
    public Result<Board> updateBoard(Board board, UUID boardID) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverUrl).path("api/board/update/" + board.boardID)
                .request(APPLICATION_JSON)
                .put(Entity.entity(board, APPLICATION_JSON), new GenericType<>() {
                });
    }



    /**
     * Put request to update a card with the same id as the card in the body
     *
     * @param card the card to update
     * @return Result object containing the success status and the updated card
     */
    public Result<Card> updateCard(Card card) {
        return ClientBuilder.newClient(new ClientConfig())//
                .target(serverUrl).path("api/card/update/" + card.cardID)//
                .request(APPLICATION_JSON)//
                .accept(APPLICATION_JSON)//
                .put(Entity.entity(card, APPLICATION_JSON), new GenericType<>() {
                });
    }

    //Websocket related utils

    /**
     * Generic method to register websockets listeners
     *
     * @param dest     destination websocket endpoint
     * @param consumer function, gets called from accept()
     */
    public <T> StompSession.Subscription registerForMessages(String dest, Class<T> type, Consumer<T> consumer) {
        return session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @Override
            @SuppressWarnings("unchecked")
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((T) payload);
            }
        });
    }

    /**
     * Disconnect from websockets
     */
    public void disconnect() {
        serverUrl = null;
        session = null;
        isConnected = false;
    }

    /** Gets a task with the specified id from the server */
    public Result<Task> getTask(UUID taskId) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverUrl).path("api/task/get/" + taskId)
                .request(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    /** Reorders the task into the specified index of the given card */
    public Result<Task> reorderTasks(UUID cardId, Task taskDragged, int indexTo) {
        return ClientBuilder.newClient(new ClientConfig())//
                .target(serverUrl).path("api/card/reorder-task/" + cardId  + "/" + indexTo)//
                .request(APPLICATION_JSON)//
                .accept(APPLICATION_JSON)//
                .put(Entity.entity(taskDragged, APPLICATION_JSON), new GenericType<>() {
                });
    }


    /**
     * Tells the server to reset the admin password
     */
    public Result<String> resetPassword() {
        return ClientBuilder.newClient(new ClientConfig())//
                .target(serverUrl).path("api/admin/reset/")//
                .request(APPLICATION_JSON)//
                .accept(APPLICATION_JSON)//
                .get(new GenericType<>() {
                });
    }


    /** Checks if the server password has changed using long polling, runs in its own thread
     * @param consumer anonymous function that gets called upon receiving updates from the server
     */
    public void checkPasswordChange(Consumer<String> consumer) {
        EXEC = Executors.newFixedThreadPool(10);
        EXEC.submit(()->{
            System.out.println("Started long-polling");
            while(!Thread.interrupted() && serverUrl != null){
                try{
                    Result<String> res = ClientBuilder.newClient(new ClientConfig())//
                            .target(serverUrl).path("api/admin/check-password-change/")//
                            .request(APPLICATION_JSON)//
                            .accept(APPLICATION_JSON)//
                            .get(new GenericType<Result<String>>() {
                            });
                    if(Objects.equals(res, Result.NO_CONTENT)) {
                        continue;
                    }
                    System.out.println("Admin password has been updated");
                    consumer.accept(res.value);
                }catch (Exception e){
                    System.out.println(e);
                }
            }
            System.out.println("Stopped long polling");
        });
    }


    /**
     * Shuts down the long polling thread
     */
    public void stopPolling(){
        if(EXEC != null){
            EXEC.shutdownNow();
        }
    }


    /** Gets the password from the server
     * @return Result String
     */
    public Result<String> getPassword() {
        return ClientBuilder.newClient(new ClientConfig())//
                .target(serverUrl).path("api/admin/get-password/")//
                .request(APPLICATION_JSON)//
                .accept(APPLICATION_JSON)//
                .get(new GenericType<>() {
                });
    }

    /**
     * Tells the server to reset the admin password
     */
    public Result<List<Board>> getAllBoards() {
        return ClientBuilder.newClient(new ClientConfig())//
                .target(serverUrl).path("api/board/get-all/")//
                .request(APPLICATION_JSON)//
                .accept(APPLICATION_JSON)//
                .get(new GenericType<>() {
                });
    }


}


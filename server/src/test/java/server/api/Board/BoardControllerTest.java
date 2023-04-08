package server.api.Board;

import commons.*;
import commons.utils.HardcodedIDGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.api.Card.CardController;
import server.api.Card.CardService;
import server.api.List.ListController;
import server.api.List.ListService;
import server.api.Task.TaskService;
import server.database.BoardRepository;
import server.database.CardRepository;
import server.database.ListRepository;
import server.database.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class BoardControllerTest {




    @Mock
    BoardRepository boardRepository;
    @Mock
    SimpMessagingTemplate msg;
    @InjectMocks
    BoardController boardController;

    BoardService boardService;
    Board board1;
    CardList list1;
    Theme baseTheme;



    @BeforeEach
    public void setUp(){
        //init mocks
        MockitoAnnotations.openMocks(this);
        boardService = new BoardService(boardRepository);
        boardController = new BoardController(boardService, msg);
        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("1");
        baseTheme = new Theme("base",
                "#2A2A2A", "#40E0D0",
                "#1b1b1b", "#40E0D0",
                "#FFDB58", "#FF00FF",
                "#2A2A2A", "#00ffd1",
                "#2A2A2A","#FF00FF");
        board1 = new Board(idGenerator1.generateID(), "Board Title 1", new ArrayList<>(),"Description 1",
                false, "password1", baseTheme);

        list1 = new CardList(idGenerator1.generateID(), "Test List",
                new ArrayList<>(), new Board());
        list1.setCardListId(idGenerator1.generateID());
    }

    @Test
    void getAllBoards() {
        List<Board> allBoards = new ArrayList<>();
        allBoards.add(board1);

        doReturn(allBoards).when(boardRepository).findAll();

        Result<List<Board>> result = boardController.getAllBoards();
        assertEquals(Result.SUCCESS.of(allBoards), result);
    }

    @Test
    void getBoard() {
        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("1");

        doReturn(Optional.of(board1)).when(boardRepository).findById(idGenerator1.generateID());

        Result<Board> result = boardController.getBoard(idGenerator1.generateID());
        assertEquals(Result.SUCCESS.of(board1), result);
    }

    @Test
    void createNewBoard() {
        doReturn(board1).when(boardRepository).save(board1);

        Result<Board> result = boardController.createBoard(board1);
        assertEquals(Result.SUCCESS.of(board1), result);
    }

    @Test
    void updateBoard() {
        doReturn(board1).when(boardRepository).save(board1);
        Result<Board> result = boardController.updateBoard(board1,board1.boardID);
        assertEquals(Result.SUCCESS.of(board1), result);
    }

    @Test
    void deleteBoard() {
        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("1");

        Result<Object> result = boardController.deleteBoard(idGenerator1.generateID());
        assertEquals(Result.SUCCESS.of(null), result);
    }

    @Test
    void updateBoardTheme() {
        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("1");


        doReturn(Optional.of(board1)).when(boardRepository).findById(idGenerator1.generateID());
        doReturn(board1).when(boardRepository).save(board1);

        Result<Board> result = boardController.updateBoardTheme(idGenerator1.generateID(), this.baseTheme);
        assertEquals(Result.SUCCESS.of(board1), result);
    }

    @Test
    void addListToBoard() {
        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("1");

        doReturn(Optional.of(board1)).when(boardRepository).findById(list1.boardId);
        doReturn(board1).when(boardRepository).save(board1);

        assertEquals(0, board1.cardListList.size());
        Result<Board> result = boardController.addListToBoard(new CardList("List Title 1", new ArrayList<>(), board1), idGenerator1.generateID());
        assertEquals(Result.SUCCESS.of(board1), result);
        assertEquals(1, board1.cardListList.size());
    }
}
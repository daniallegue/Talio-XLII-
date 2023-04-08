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
import server.api.Tag.TagService;
import server.database.BoardRepository;

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

    TagService tagService;
    Board board1;
    Board board2;
    CardList list1;



    @BeforeEach
    public void setUp(){
        //init mocks
        MockitoAnnotations.openMocks(this);
        boardService = new BoardService(boardRepository, tagService);
        boardController = new BoardController(boardService, msg);
        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("1");
        board1 = new Board(idGenerator1.generateID(), "Board Title 1", new ArrayList<>(),"Description 1",
                false, "password1", new Theme("#2A2A2A", "#1B1B1B", "#00"));
        board1.tagList  = new ArrayList<>();
        board2 = new Board(idGenerator1.generateID(), "Board Title 1", new ArrayList<>(),"Description 1",
                false, "password1", new Theme("#2A2A2A", "#1B1B1B", "#00"));
        board2.tagList  = new ArrayList<>();

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
       Result<Board> result = boardController.updateBoard(board1, board1.boardID);
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

        Result<Board> result = boardController.updateBoardTheme(idGenerator1.generateID(), new Theme("#2A2A2A", "#1B1B1B", "#FFFFFF"));
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
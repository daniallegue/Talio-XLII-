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
import server.api.Tag.TagService;
import server.database.BoardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    BoardRepository boardRepository;
    @InjectMocks
    BoardService boardService;

    @Mock
    TagService tagService;

    Board board1;
    Board board2;
    CardList list1;

    Theme baseTheme;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        boardService = new BoardService(boardRepository, tagService);

        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("1");
        HardcodedIDGenerator idGenerator2 = new HardcodedIDGenerator();
        idGenerator2.setHardcodedID("2");

        baseTheme = new Theme("base",
                "#2A2A2A", "#40E0D0",
                "#1b1b1b", "#40E0D0",
                "#FFDB58", "#FF00FF",
                "#2A2A2A", "#00ffd1",
                "#2A2A2A","#FF00FF");

        board1 = new Board(idGenerator1.generateID(), "Board Title 1", new ArrayList<>(),"Description 1",
                false, "password1", baseTheme);
        board2 = new Board(idGenerator2.generateID(), "Board Title 2", new ArrayList<>(),"Description 2",
                true, "password2", baseTheme);
        list1 = new CardList(idGenerator1.generateID(), "Test List",
                new ArrayList<>(), board1.boardID, board1);
    }

    @Test
    void getAllBoards() {
        List<Board> allBoards = new ArrayList<>();
        allBoards.addAll(List.of(board1,board2));

        doReturn(List.of(board1,board2)).when(boardRepository).findAll();

        Result<List<Board>> result = boardService.getAllBoards();
        assertEquals(Result.SUCCESS.of(allBoards), result);
    }

    @Test
    void getAllBoardsFAIL() {
        doThrow(new RuntimeException()).when(boardRepository).findAll();

        Result<List<Board>> result = boardService.getAllBoards();
        assertEquals(Result.FAILED_GET_ALL_BOARDS.of(null), result);
    }

    @Test
    void getBoardById() {
        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("1");
        doReturn(Optional.of(board1)).when(boardRepository).findById(idGenerator1.generateID());

        Result<Board> result = boardService.getBoardById(idGenerator1.generateID());
        assertEquals(Result.SUCCESS.of(board1), result);
    }

    @Test
    void getBoardByIdFAILNotPresent() {
        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("1");
        doReturn(Optional.empty()).when(boardRepository).findById(idGenerator1.generateID());

        Result<Board> result = boardService.getBoardById(idGenerator1.generateID());
        assertEquals(Result.FAILED_RETRIEVE_BOARD_BY_ID.of(null), result);
    }

    @Test
    void getBoardByIdFAILException() {
        HardcodedIDGenerator idGenerator42 = new HardcodedIDGenerator();
        idGenerator42.setHardcodedID("42");
        doThrow(new RuntimeException()).when(boardRepository).findById(idGenerator42.generateID());

        Result<Board> result = boardService.getBoardById(idGenerator42.generateID());
        assertEquals(Result.FAILED_RETRIEVE_BOARD_BY_ID.of(null), result);
    }
    @Test
    void addNewBoard(){
        doReturn(board1).when(boardRepository).save(board1);

        Result<Board> result = boardService.addNewBoard(board1);
        assertEquals(Result.SUCCESS.of(board1), result);
    }

    @Test
    void addNewBoardNull() {
        Result<Board> result = boardService.addNewBoard(null);
        assertEquals(Result.OBJECT_ISNULL.of(null), result);
    }

    @Test
    void addNewBoardFAIL() {
        doThrow(new RuntimeException()).when(boardRepository).save(board1);

        Result<Board> result = boardService.addNewBoard(board1);
        assertEquals(Result.FAILED_ADD_NEW_BOARD.of(null), result);
    }

    @Test
    void deleteBoard() {
        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("1");
        Result<Object> result = boardService.deleteBoard(idGenerator1.generateID());
        assertEquals(Result.SUCCESS.of(null), result);
    }

    @Test
    void updateBoard() {
        doReturn(board1).when(boardRepository).save(board1);
        Result<Board> result = boardService.updateBoard(board1);
        assertEquals(Result.SUCCESS.of(board1), result);
    }

    @Test
    void updateBoardFAIL() {
        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("1");
        Result<Board> result = boardService.updateBoard(board1,idGenerator1.generateID());
        assertEquals(Result.FAILED_UPDATE_BOARD, result);
    }

    @Test
    void updateBoardOtherFAIL() {
        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("1");
        doThrow(new RuntimeException()).when(boardRepository).save(board1);
        Result<Board> result = boardService.updateBoard(board1);
        assertEquals(Result.FAILED_UPDATE_BOARD, result);
    }

    @Test
    void deleteBoardFAIL() {
        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("1");
        doThrow(new RuntimeException()).when(boardRepository).deleteById(idGenerator1.generateID());

        Result<Object> result = boardService.deleteBoard(idGenerator1.generateID());
        assertEquals(Result.FAILED_DELETE_BOARD.of(null), result);
    }

    @Test
    void updateBoardTheme() {
        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("1");
        Board boardTheme =  new Board(idGenerator1.generateID(), "Board Title 1", new ArrayList<>(),"Description 1",
                false, "password1", new Theme("base",
                "#2A2A2A", "#40E0D0",
                "#1b1b1b", "#40E0D0",
                "#FFDB58", "#FF00FF",
                "#2A2A2A", "#00ffd1",
                "#2A2A2A","#FF00FF"));
        doReturn(Optional.of(boardTheme)).when(boardRepository).findById(idGenerator1.generateID());

        Result<Board> result = boardService.updateBoardTheme(idGenerator1.generateID(), new Theme("base",
                "#2A2A2A", "#40E0D0",
                "#1b1b1b", "#40E0D0",
                "#FFDB58", "#FF00FF",
                "#2A2A2A", "#00ffd1",
                "#2A2A2A","#differentColor"));
        assertEquals(Result.SUCCESS.of(boardTheme), result);
    }

    @Test
    void updateBoardThemeFAIL() {
        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("1");
        doThrow(new RuntimeException()).when(boardRepository).findById(board1.boardID);

        Result<Board> result = boardService.updateBoardTheme(idGenerator1.generateID(), baseTheme);
        assertEquals(Result.FAILED_UPDATE_BOARD_THEME.of(null), result);
    }
    @Test
    void updateBoardAddList() {
        HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
        idGenerator1.setHardcodedID("1");
        doReturn(Optional.of(board1)).when(boardRepository).findById(idGenerator1.generateID());
        doReturn(board1).when(boardRepository).save(board1);

        Result<Board> result = boardService.updateBoardAddList(list1);
        assertEquals(Result.SUCCESS.of(board1), result);
    }
    @Test
    void updateBoardAddListFAIL() {
        doThrow(new RuntimeException()).when(boardRepository).findById(board1.boardID);

        Result<Board> result = boardService.updateBoardAddList(list1);
        assertEquals(Result.FAILED_TO_ADD_LIST_TO_BOARD.of(null), result);
    }
    @Test
    void deleteList() {
            HardcodedIDGenerator idGenerator1 = new HardcodedIDGenerator();
            idGenerator1.setHardcodedID("1");
            doReturn(Optional.of(board1)).when(boardRepository).findById(idGenerator1.generateID());
            doReturn(board1).when(boardRepository).save(board1);

            Result<Board> result = boardService.deleteList(list1);
            assertEquals(Result.SUCCESS.of(board1), result);
    }

    @Test
    void deleteListFAIL() {
        doThrow(new RuntimeException()).when(boardRepository).findById(board1.boardID);

        Result<Board> result = boardService.deleteList(list1);
        assertEquals(Result.FAILED_UPDATE_BOARD.of(null), result);
    }

    @Test
    void addTagToBoard() {
        HardcodedIDGenerator idGenerator = new HardcodedIDGenerator();
        idGenerator.setHardcodedID("58");
        Tag tag = new Tag(idGenerator.generateID(),"Tag Title","#000000");
        board1.tagList = new ArrayList<>();

        doReturn(Optional.of(board1)).when(boardRepository).findById(board1.boardID);
        doReturn(board1).when(boardRepository).save(board1);

        var result = boardService.addTagToBoard(tag, board1.boardID);
        assertEquals(Result.SUCCESS.of(board1), result);
    }

    @Test
    void addTagToBoardFAILNullCase() {
        HardcodedIDGenerator idGenerator = new HardcodedIDGenerator();
        idGenerator.setHardcodedID("58");
        Tag tag = new Tag(idGenerator.generateID(),"Tag Title","#000000");

        var result = boardService.addTagToBoard(tag,null);
        assertEquals(Result.OBJECT_ISNULL.of(null), result);
    }

    @Test
    void addTagToBoardFAIL() {
        HardcodedIDGenerator idGenerator = new HardcodedIDGenerator();
        idGenerator.setHardcodedID("58");
        Tag tag = new Tag(idGenerator.generateID(),"Tag Title","#000000");
        doThrow(new RuntimeException()).when(boardRepository).findById(board1.boardID);
        board1.tagList = new ArrayList<>();

        var result = boardService.addTagToBoard(tag ,board2.boardID);
        assertEquals(Result.FAILED_ADD_TAG_TO_BOARD.of(null), result);
    }
}